/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import org.lwjgl.system.*;

import java.lang.classfile.*;
import java.lang.constant.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

import static java.lang.classfile.ClassFile.*;
import static java.lang.constant.ConstantDescs.*;
import static java.lang.invoke.MethodHandles.*;
import static org.lwjgl.system.APIUtil.*;
import static org.lwjgl.system.ffm.BytecodeUtil.*;
import static org.lwjgl.system.ffm.ConstantDescs.*;

/**
 * Generates LWJGL bindings from an interface, optionally annotated with {@code FFM*} annotations.
 *
 * <p>The objective is to allow users to create their own LWJGL-style bindings, dynamically at run-time, without sacrificing performance.</p>
 *
 * <p>Several modern Java technologies are being utilized to achieve this:</p>
 * <ul>
 *     <li>Dynamic Class-File Constants (JEP 309, JDK 11, a.k.a. Condy): Used to lazily initialize bindings, without precompiling a private nested class per function.</li>
 *     <li>Hidden Classes (JEP 371, JDK 15): Used to generate classes with minimal metadata and no interaction with ClassLoaders.</li>
 *     <li>Class data support for hidden classes (JDK-8256214, JDK 16): Used to boostrap Condy values.</li>
 *     <li>Class-File API (JEP 457, JDK 22, preview): Used to generate bytecode at runtime, without 3rd-party dependencies.</li>
 *     <li>Foreign Function & Memory API (JEP 442, JDK 22): Used for native interoperation without JNI.</li>
 * </ul>
 *
 * <p>The {@link #generate} method is designed to be relatively cheap. The instance returned is lightweight and its methods are never materialized, unless
 * actually used by the application. More specifically:</p>
 * <ul>
 *    <li>A minimal class is generated that implements the input interface.</li>
 *    <li>There is no state in the generated class, only methods.</li>
 *    <li>All methods are simple, with minimal bytecode. A method handle is retrieved using Condy and arguments are passed directly to {@link MethodHandle#invokeExact}.</li>
 *    <li>There is no overhead from the Condy {@code ldc} instruction. It is executed only once, if and when the method is called.</li>
 *    <li>
 *        Execution of relatively expensive binding code is deferred to the Condy bootstrap. This includes:
 *        <ul>
 *            <li>lookup of {@code FFM*} annotations and associated logic</li>
 *            <li>function address lookup and creation of FFM method handles</li>
 *            <li>further bytecode generation and wrapping of FFM method handles if necessary</li>
 *        </ul>
 *    </li>
 *    <li>
 *        Hidden class data is used:
 *        <ul>
 *            <li>to pass reflected {@link Method} instances to the Condy bootstrap</li>
 *            <li>to pass FFM {@link MethodHandle}/{@link MemorySegment} instances to generated wrapper methods</li>
 *        </ul>
 *    </li>
 * </ul>
 *
 * <p>When stored in {@code static final} fields, performance of the generated bindings is equivalent to direct FFM calls. The JVM can inline through
 * everything, for the following reasons:</p>
 * <ul>
 *     <li>The binding instance is constant.</li>
 *     <li>There is a single implementation of the binding interface.</li>
 *     <li>The {@link MethodHandle} instances created via Condy are also treated as constants by the JVM.</li>
 * </ul>
 *
 * <p>The generator DSL supports the specification of 3 virtual parameters, which must be present in a strict order before other parameters:</p>
 * <ul>
 *     <li>
 *         A {@link MemorySegment} parameter in methods annotated with {@link FFMFunctionAddress}.
 *
 *         <p>It must be specified before any other virtual or normal parameter.</p>
 *     </li>
 *     <li>
 *         A {@link SegmentAllocator} or {@link StackAllocator} parameter. This parameter will be used to allocate storage for struct values passed or returned
 *         by-value and also for temporary storage needed internally by the method call (e.g. for {@link FFMReturn} buffers).
 *
 *         <p>If the parameter is of type {@link StackAllocator}, a stack frame will be pushed & popped inside the method call when temporary storage is
 *         needed.</p>
 *
 *         <p>It must be specified after the {@link FFMFunctionAddress} parameter, if one exists, and before any other virtual or normal parameter.</p>
 *     </li>
 *     <li>
 *         A {@link MemorySegment} parameter annotated with {@link FFMCaptureCallState}.
 *
 *         <p>It must be specified after other virtual parameters and before any normal parameter.</p>
 *     </li>
 * </ul>
 */
public final class BindingGenerator {

    public static final AddressLayout C_POINTER = ValueLayout.ADDRESS
        .withTargetLayout(MemoryLayout.sequenceLayout(Long.MAX_VALUE, ValueLayout.JAVA_BYTE));

    private static final DirectMethodHandleDesc
        BSM_BOOTSTRAP_DOWNCALL = ofConstantBootstrap(CD_BindingGenerator, "bootstrapDowncall", CD_MethodHandle, CD_int),
        BSM_BOOTSTRAP_UPCALL   = ofConstantBootstrap(CD_BindingGenerator, "bootstrapUpcall", CD_UpcallMapper, CD_int),
        BSM_BOOTSTRAP_STRUCT   = ofConstantBootstrap(CD_BindingGenerator, "bootstrapGroup", CD_StructMapper, CD_int, CD_boolean),
        BSM_BOOTSTRAP_UNION    = ofConstantBootstrap(CD_BindingGenerator, "bootstrapGroup", CD_UnionMapper, CD_int, CD_boolean);

    private static final Set<String> STANDARD_CHARSETS = Arrays.stream(StandardCharsets.class.getDeclaredFields())
        .map(Field::getName)
        .collect(Collectors.toUnmodifiableSet());

    private BindingGenerator() {
    }

    public static <T> T generate(Lookup context, Class<T> bindingInterface)                            { return generate(context, bindingInterface, BindingConfig.builder().build()); }
    public static <T> T generate(Lookup context, Class<T> bindingInterface, FunctionProvider provider) { return generate(context, bindingInterface, BindingConfig.builder().withLookup(provider).build()); }
    public static <T> T generate(Lookup context, Class<T> bindingInterface, SymbolLookup lookup)       { return generate(context, bindingInterface, BindingConfig.builder().withLookup(lookup).build()); }
    @SuppressWarnings("unchecked")
    public static <T> T generate(Lookup context, Class<T> bindingInterface, BindingConfig config) {
        try {
            return ((Class<T>)generateImplementation(context, bindingInterface, config).lookupClass())
                .getDeclaredConstructor()
                .newInstance();
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static Lookup generateImplementation(Lookup context, Class<?> functionsClass, BindingConfig config) throws IllegalAccessException {
        var methods = functionsClass.getMethods();

        var mappers = new HashMap<Type, Method>();

        var thisClass = ClassDesc.of(functionsClass.getPackageName(), functionsClass.getSimpleName() + "Impl");
        var bytecode = ClassFile.of().build(thisClass, classBuilder -> {
            startHiddenClass(classBuilder);

            for (int m = 0; m < methods.length; m++) {
                var method = methods[m];

                var methodTypeDesc = getMethodTypeDesc(method);

                if (method.getParameterCount() == 0 && method.getGenericReturnType() instanceof ParameterizedType type) {
                    var rawType = type.getRawType();

                    var argumentType = type.getActualTypeArguments()[0];
                    mappers.put(argumentType instanceof ParameterizedType pt ? pt.getRawType() : argumentType, method);

                    DynamicConstantDesc<?> condy;
                    if (rawType == StructMapper.class) {
                        condy = DynamicConstantDesc
                            .ofNamed(BSM_BOOTSTRAP_STRUCT, method.getName(), CD_StructMapper, m, 0);
                    } else if (rawType == UnionMapper.class) {
                        condy = DynamicConstantDesc
                            .ofNamed(BSM_BOOTSTRAP_UNION, method.getName(), CD_UnionMapper, m, 1);
                    } else if (rawType == UpcallMapper.class) {
                        condy = DynamicConstantDesc
                            .ofNamed(BSM_BOOTSTRAP_UPCALL, method.getName(), CD_UpcallMapper, m);
                    } else {
                        throw new IllegalStateException("Unsupported mapper type: " + rawType);
                    }

                    classBuilder.withMethod(method.getName(), methodTypeDesc, ACC_PUBLIC, mb -> mb.withCode(cb -> cb
                        // Do the Condy lookup
                        .ldc(condy)
                        // Return mapper object
                        .areturn()));
                } else {
                    var condy = DynamicConstantDesc.<MethodHandle>ofNamed(BSM_BOOTSTRAP_DOWNCALL, method.getName(), CD_MethodHandle, m);
                    classBuilder.withMethod(method.getName(), methodTypeDesc, ACC_PUBLIC, mb -> mb.withCode(cb -> {
                        // Do the Condy lookup
                        cb.ldc(condy);
                        // Pass arguments unmodified
                        for (int p = 0; p < methodTypeDesc.parameterCount(); p++) {
                            cb.loadLocal(TypeKind.from(methodTypeDesc.parameterType(p)), cb.parameterSlot(p));
                        }
                        cb
                            // Invoke the generated method handle
                            .invokevirtual(CD_MethodHandle, "invokeExact", methodTypeDesc)
                            // Return result if non-void, also unmodified
                            .return_(TypeKind.from(methodTypeDesc.returnType()));
                    }));
                }
            }

            var functionsClassInterface = functionsClass.describeConstable().orElseThrow();

            List<ClassDesc> interfaces;
            if (!mappers.isEmpty()) {
                interfaces = List.of(functionsClassInterface, CD_MapperContainer);
                classBuilder.withMethod("$mappers", MTD_MapperContainer_$mappers, ACC_PUBLIC, mb -> mb.withCode(cb -> cb
                    .ldc(condyCDataAt(CD_Map, 2))
                    .areturn()));
            } else {
                interfaces = List.of(functionsClassInterface);
            }
            classBuilder.withInterfaceSymbols(interfaces);
        });

        return context.defineHiddenClassWithClassData(bytecode, mappers.isEmpty()
            ? List.of(config, methods)
            : List.of(config, methods, mappers), false);
    }

    // The Condy boostrap method for downcalls, called the first time the LDC bytecode is executed.
    @SuppressWarnings("unused")
    public static MethodHandle bootstrapDowncall(MethodHandles.Lookup lookup, String name, Class<?> bootstrapClass, int methodIndex) {
        var debugGenerator = Configuration.DEBUG_GENERATOR.get(false);
        if (debugGenerator) {
            apiLog("BOOTSTRAPPING DOWNCALL#" + methodIndex + ": " + name);
        }

        return new CallDown(lookup, methodIndex)
            .bootstrap(debugGenerator);
    }

    // The Condy boostrap method for upcalls, called the first time the above LDC bytecode is executed.
    @SuppressWarnings("unused")
    public static UpcallMapper<?> bootstrapUpcall(Lookup lookup, String name, Class<?> bootstrapClass, int methodIndex) {
        var debugGenerator = Configuration.DEBUG_GENERATOR.get(false);
        if (debugGenerator) {
            apiLog("BOOTSTRAPPING UPCALL#" + methodIndex + ": " + name);
        }

        return new CallUp(lookup, methodIndex)
            .bootstrap(debugGenerator);
    }

    // The Condy boostrap method for upcalls, called the first time the above LDC bytecode is executed.
    @SuppressWarnings("unused")
    public static GroupMapper<?, ?> bootstrapGroup(Lookup lookup, String name, Class<?> bootstrapClass, int methodIndex, boolean isUnion) {
        var debugGenerator = Configuration.DEBUG_GENERATOR.get(false);
        if (debugGenerator) {
            apiLog("BOOTSTRAPPING " + (isUnion ? "UNION" : "STRUCT") + "#" + methodIndex + ": " + name);
        }

        throw new UnsupportedOperationException();
    }

    static ClassDesc getClassDescWrapper(Method method) {
        var declaringClass = method.getDeclaringClass();
        return ClassDesc.of(
            declaringClass.getPackageName(),
            declaringClass.getSimpleName() + "$" + method.getName() // TODO: do we need to handle multiple overloads?
        );
    }

    static MethodTypeDesc getMethodTypeDesc(Method method) {
        return MethodTypeDesc.of(
            method.getReturnType().describeConstable().orElseThrow(),
            getParameterDescs(method)
        );
    }

    private static ClassDesc[] getParameterDescs(Method method) {
        var parameterTypes = method.getParameterTypes();
        var parameterDescs = new ClassDesc[parameterTypes.length];
        for (int p = 0; p < parameterTypes.length; p++) {
            parameterDescs[p] = parameterTypes[p].describeConstable().orElseThrow();
        }
        return parameterDescs;
    }

    private static ClassDesc[] getParameterDescsNative(Method method, ReturnTransform returnTransform) {
        var parameterTypes = method.getParameterTypes();
        var parameterDescs = new ClassDesc[parameterTypes.length];
        for (int p = 0; p < parameterTypes.length; p++) {
            parameterDescs[p] = getClassDescNative(parameterTypes[p]);
        }
        return parameterDescs;
    }

    private static ClassDesc getClassDescNative(Class<?> type) {
        Class<?> nativeType;
        if (type == String.class) {
            nativeType = MemorySegment.class;
        } else {
            nativeType = type;
        }
        return nativeType.describeConstable().orElseThrow();
    }

}