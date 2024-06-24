/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import java.lang.classfile.*;
import java.lang.constant.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

import static java.lang.constant.ConstantDescs.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;
import static org.lwjgl.system.APIUtil.*;
import static org.lwjgl.system.Checks.*;

/**
 * Generates LWJGL bindings from an interface, optionally annotated with {@code FFM*} annotations.
 *
 * <p>The objective is to allow users to create their own LWJGL-style bindings, dynamically at run-time, without sacrificing performance.
 * It should be feature-complete and powerful enough, such that LWJGL itself uses the same mechanism internally for its static bindings.</p>
 *
 * <p>Several modern Java technologies are being utilized to achieve this:</p>
 * <ul>
 *     <li>Dynamic Class-File Constants (JEP 309, JDK 11, a.k.a. Condy): Used to lazily initialize bindings, without precompiling a private nested classes per function.</li>
 *     <li>Hidden Classes (JEP 371, JDK 15): Used to generate classes with minimal metadata and no interaction with ClassLoaders.</li>
 *     <li>Class data support for hidden classes (JDK-8256214, JDK 16): Used for constant pool patching, to boostrap Condy values.</li>
 *     <li>Class-File API (JEP 457, JDK 22, preview): Used to generate bytecode at runtime, without 3rd-party dependencies.</li>
 *     <li>Foreign Function & Memory API (JEP 442, JDK 22): Used for native interoperation without JNI.</li>
 * </ul>
 *
 * <p>Laziness is achieved as follows:</p>
 * <ul>
 *    <li>A minimal class is generated that implements the input interface.</li>
 *    <li>There are no fields in the generated class, only methods.</li>
 *    <li>Each method </li>
 * </ul>
 */
public final class BindingGenerator {

    public static final AddressLayout C_POINTER = ValueLayout.ADDRESS
        .withTargetLayout(MemoryLayout.sequenceLayout(Long.MAX_VALUE, ValueLayout.JAVA_BYTE));

    private static final DirectMethodHandleDesc BSM_BOOTSTRAP = ofConstantBootstrap(
        BindingGenerator.class.describeConstable().orElseThrow(),
        "bootstrap",
        CD_MethodHandle,
        CD_int
    );

    private static final int METHOD_FLAGS_PUBLIC        = AccessFlags.ofMethod(AccessFlag.PUBLIC).flagsMask();
    private static final int METHOD_FLAGS_PUBLIC_STATIC = AccessFlags.ofMethod(AccessFlag.PUBLIC, AccessFlag.STATIC).flagsMask();

    private static final ClassDesc CD_Charset          = Charset.class.describeConstable().orElseThrow();
    private static final ClassDesc CD_MemorySegment    = MemorySegment.class.describeConstable().orElseThrow();
    private static final ClassDesc CD_StackArena       = StackArena.class.describeConstable().orElseThrow();
    private static final ClassDesc CD_StandardCharsets = StandardCharsets.class.describeConstable().orElseThrow();

    private static final MethodTypeDesc MTD_Charset_forName         = MethodTypeDesc.of(CD_Charset, CD_String);
    private static final MethodTypeDesc MTD_MemorySegment_getString = MethodTypeDesc.of(CD_String, CD_long, CD_Charset);
    private static final MethodTypeDesc MTD_Throwable_addSuppressed = MethodTypeDesc.of(CD_void, CD_Throwable);

    private static final Set<String> STANDARD_CHARSETS = Set.of("US_ASCII", "ISO_8859_1", "UTF_8", "UTF_16BE", "UTF_16LE", "UTF_16", "UTF_32BE", "UTF_32LE", "UTF_32");

    private BindingGenerator() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T generate(Lookup lookup, Class<T> bindingInterface, SymbolLookup loader) {
        try {
            return ((Class<T>)generateImplementation(lookup, bindingInterface, loader).lookupClass())
                .getDeclaredConstructor()
                .newInstance();
        } catch (Throwable t) {
            if (DEBUG) {
                t.printStackTrace(DEBUG_STREAM);
            }
            throw new RuntimeException(t);
        }
    }

    private static Lookup generateImplementation(Lookup lookup, Class<?> functionsClass, SymbolLookup loader) throws IllegalAccessException {
        var methods = functionsClass.getMethods();

        var classData = new ArrayList<>(1 + methods.length);
        classData.add(loader);
        classData.addAll(Arrays.asList(methods));

        var thisClass = ClassDesc.of(functionsClass.getPackageName(), functionsClass.getSimpleName() + "Impl");
        var bytecode = ClassFile.of().build(thisClass, classBuilder -> {
            classBuilder
                .withVersion(ClassFile.latestMajorVersion(), ClassFile.latestMinorVersion())
                .withFlags(AccessFlag.PUBLIC, AccessFlag.FINAL)
                .withSuperclass(CD_Object)
                .withInterfaceSymbols(functionsClass.describeConstable().orElseThrow())
                // private <init>() { super(); }
                .withMethod(INIT_NAME, MTD_void, METHOD_FLAGS_PUBLIC, methodBuilder -> methodBuilder
                    .withCode(cb -> cb
                        .aload(0)
                        .invokespecial(CD_Object, INIT_NAME, MTD_void, false)
                        .return_()));

            var constantPool = classBuilder.constantPool();
            for (int m = 0; m < methods.length; m++) {
                var method = methods[m];

                var condy = DynamicConstantDesc.ofNamed(
                    BSM_BOOTSTRAP,
                    method.getName(),
                    CD_MethodHandle,
                    constantPool.intEntry(m + 1).constantValue());

                var methodTypeDesc = MethodTypeDesc.of(
                    method.getReturnType().describeConstable().orElseThrow(),
                    getParameterDescs(method)
                );

                classBuilder
                    .withMethod(method.getName(), methodTypeDesc, METHOD_FLAGS_PUBLIC, mb -> mb
                        .withCode(cb -> {
                            cb.ldc(condy);
                            for (int p = 0; p < methodTypeDesc.parameterCount(); p++) {
                                cb.loadInstruction(TypeKind.from(methodTypeDesc.parameterType(p)), p + 1);
                            }
                            cb
                                .invokevirtual(CD_MethodHandle, "invokeExact", methodTypeDesc)
                                .returnInstruction(TypeKind.from(methodTypeDesc.returnType()));
                        })
                    );
            }
        });

        return lookup.defineHiddenClassWithClassData(bytecode, classData, false);
    }

    @SuppressWarnings("unused")
    public static MethodHandle bootstrap(Lookup lookup, String name, Class<?> type, int index) {
        System.err.println("BOOTSTRAPPING: " + name + " (offset: " + index + ")");
        try {
            var loader = classDataAt(lookup, DEFAULT_NAME, SymbolLookup.class, 0);
            var method = classDataAt(lookup, DEFAULT_NAME, Method.class, index);

            var parameters = method.getParameters();

            var hasStack = method.getReturnType() == String.class || Stream.of(parameters).anyMatch(p -> p.getType() == String.class);

            var options = new ArrayList<Linker.Option>();

            var critical = getCritical(method);
            if (critical != null) {
                options.add(Linker.Option.critical(critical.value()));
            }

            var nativeName = getNativeName(method);
            System.err.println("\t-> " + nativeName);

            var resLayout = method.getReturnType() == void.class ? null : memoryLayout(method);
            var argLayouts = Stream.of(parameters)
                .map(BindingGenerator::memoryLayout)
                .toArray(MemoryLayout[]::new);

            // The target handle
            var nativeHandle = Linker.nativeLinker()
                .downcallHandle(resLayout == null
                    ? FunctionDescriptor.ofVoid(argLayouts)
                    : FunctionDescriptor.of(resLayout, argLayouts), options.toArray(Linker.Option[]::new))
                .bindTo(loader.find(nativeName).orElseThrow());

            if (hasStack) {
                // Wrap the target handle in a try/finally that pushes a new frame to the stack arena.
                // Transform parameters that use the arena accordingly.
                System.err.println("\t-> generating StackArena wrapper");

                var bytecode = ClassFile.of().build(getClassDescWrapper(method), classBuilder -> {
                    classBuilder
                        .withVersion(ClassFile.latestMajorVersion(), ClassFile.latestMinorVersion())
                        .withFlags(AccessFlag.PUBLIC, AccessFlag.FINAL)
                        .withSuperclass(CD_Object)
                        // private <init>() { super(); }
                        .withMethod(INIT_NAME, MTD_void, METHOD_FLAGS_PUBLIC, methodBuilder -> methodBuilder
                            .withCode(cb -> cb
                                .aload(0)
                                .invokespecial(CD_Object, INIT_NAME, MTD_void, false)
                                .return_()));

                    var methodTypeDesc = MethodTypeDesc.of(
                        method.getReturnType().describeConstable().orElseThrow(),
                        getParameterDescs(method)
                    );

                    classBuilder
                        .withMethod(method.getName(), methodTypeDesc, METHOD_FLAGS_PUBLIC_STATIC, methodBuilder -> methodBuilder
                            .withCode(cb -> {
                                var stackSlot = cb.allocateLocal(TypeKind.ReferenceType);
                                cb
                                    .invokestatic(CD_StackArena, "stackPush", MethodTypeDesc.of(CD_StackArena))
                                    .astore(stackSlot)
                                    .trying(bcb -> {
                                            // Load target handle via Condy, using MethodHandles::classData as the bootstrap method.
                                            bcb.ldc(DynamicConstantDesc.ofNamed(BSM_CLASS_DATA, DEFAULT_NAME, CD_MethodHandle));
                                            for (int p = 0; p < methodTypeDesc.parameterCount(); p++) {
                                                if (parameters[p].getType() == String.class) {
                                                    bcb
                                                        .aload(stackSlot)
                                                        .aload(p);

                                                    var charsetName = getCharset(parameters[p]);
                                                    if (STANDARD_CHARSETS.contains(charsetName)) {
                                                        bcb.getstatic(CD_StandardCharsets, charsetName, CD_Charset);
                                                    } else {
                                                        bcb
                                                            .ldc(bcb.constantPool().stringEntry(charsetName).constantValue())
                                                            .invokestatic(CD_Charset, "forName", MTD_Charset_forName);
                                                    }

                                                    bcb.invokevirtual(CD_StackArena, "allocateFrom", MethodTypeDesc.of(CD_MemorySegment, CD_String, CD_Charset));
                                                } else {
                                                    bcb.loadInstruction(TypeKind.from(methodTypeDesc.parameterType(p)), p);
                                                }
                                            }
                                            bcb.invokevirtual(CD_MethodHandle, "invokeExact", MethodTypeDesc.of(getClassDescNative(method.getReturnType()), getParameterDescsNative(method)));
                                            if (method.getReturnType() != void.class) {
                                                if (method.getReturnType() == String.class) {
                                                    bcb.ldc(bcb.constantPool().longEntry(0L).constantValue());
                                                    var charsetName = getCharset(method);
                                                    if (STANDARD_CHARSETS.contains(charsetName)) {
                                                        bcb.getstatic(CD_StandardCharsets, charsetName, CD_Charset);
                                                    } else {
                                                        bcb
                                                            .ldc(bcb.constantPool().stringEntry(charsetName).constantValue())
                                                            .invokestatic(CD_Charset, "forName", MTD_Charset_forName);
                                                    }
                                                    bcb.invokeinterface(CD_MemorySegment, "getString", MTD_MemorySegment_getString);
                                                }
                                                bcb.storeInstruction(TypeKind.from(methodTypeDesc.returnType()), stackSlot + 1);
                                            }
                                        },
                                        cab0 -> cab0.catchingAll(bcb0 -> bcb0
                                            .astore(stackSlot + 1)
                                            .trying(
                                                bcb1 -> bcb1
                                                    .aload(stackSlot)
                                                    .invokevirtual(CD_StackArena, "close", MTD_void),
                                                cab1 -> cab1.catchingAll(bcb1 -> bcb1
                                                    .astore(stackSlot + 2)
                                                    .aload(stackSlot + 1)
                                                    .aload(stackSlot + 2)
                                                    .invokevirtual(CD_Throwable, "addSuppressed", MTD_Throwable_addSuppressed))
                                            )
                                            .aload(stackSlot + 1)
                                            .throwInstruction()
                                        )
                                    )
                                    .aload(stackSlot)
                                    .invokevirtual(CD_StackArena, "close", MTD_void)
                                    .aload(stackSlot + 1)
                                    .returnInstruction(TypeKind.from(methodTypeDesc.returnType()));
                            })
                        );
                });

                // Store the target handle as class data in the hidden class.
                var wrapperLookup = lookup.defineHiddenClassWithClassData(bytecode, nativeHandle, true);
                var wrapperHandle = wrapperLookup.findStatic(
                    wrapperLookup.lookupClass(),
                    method.getName(),
                    methodType(method.getReturnType(), method.getParameterTypes())
                );
                //System.out.println(wrapperHandle);
                return wrapperHandle;

                /*
                // T nativeCall(...);
                // T innerCall2(..., Arena stack, argN, ...) {
                //     return nativeCall(..., stack.allocateFrom(argN), ...);
                // }
                var handle = collectArguments(
                    nativeHandle,
                    2,
                    lookup().findStatic(BindingGenerator.class, "allocateFrom", methodType(MemorySegment.class, Arena.class, String.class))
                );
                // T innerCall1(Arena stack, ...) {
                //     return innerCell2(..., stack, argN, ...);
                // }
                handle = permuteArguments(
                    handle,
                    methodType(MemorySegment.class, Arena.class, int.class, int.class, String.class, MemorySegment.class, MemorySegment.class),
                    1, 2, 0, 3, 4, 5
                );
                // T innerCall0(Arena stack, ...) {
                //     try {
                //         return innerCall1(stack, ...);
                //     } finally {
                //         cleanup(throwable, result, stack); // stack.close() is called here
                //     }
                // }
                handle = tryFinally(
                    handle,
                    foldArguments(
                        permuteArguments(
                            identity(method.getReturnType()),
                            methodType(method.getReturnType(), Throwable.class, method.getReturnType(), Arena.class),
                            1
                        ),
                        2,
                        publicLookup().findVirtual(Arena.class, "close", methodType(void.class))
                    )

                );
                // T wrappedCall(...) {
                //     var stack = stackPush();
                //     return innerCall0(stack, ...);
                // }
                handle = foldArguments(
                    handle,
                    0,
                    publicLookup().findStatic(Arena.class, "ofConfined", methodType(Arena.class))
                );

                return handle;
                 */
            }

            return nativeHandle;
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }

    private static ClassDesc[] getParameterDescs(Method method) {
        var parameterTypes = method.getParameterTypes();
        var parameterDescs = new ClassDesc[parameterTypes.length];
        for (int p = 0; p < parameterTypes.length; p++) {
            parameterDescs[p] = parameterTypes[p].describeConstable().orElseThrow();
        }
        return parameterDescs;
    }

    private static ClassDesc[] getParameterDescsNative(Method method) {
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

    private static ClassDesc getClassDescWrapper(Method method) {
        var declaringClass = method.getDeclaringClass();
        return ClassDesc.of(
            declaringClass.getPackageName(),
            declaringClass.getSimpleName() + "$" + method.getName() // TODO: do we need to handle multiple overloads?
        );
    }

    private static MemoryLayout memoryLayout(Method method)       { return memoryLayout(method.getReturnType()); }
    private static MemoryLayout memoryLayout(Parameter parameter) { return memoryLayout(parameter.getType()); }
    private static MemoryLayout memoryLayout(Class<?> type) {
        if (type == String.class) {
            return C_POINTER;
        }
        if (type == MemorySegment.class) {
            return ValueLayout.ADDRESS;
        }
        if (type == byte.class) {
            return ValueLayout.JAVA_BYTE;
        }
        if (type == short.class) {
            return ValueLayout.JAVA_SHORT;
        }
        if (type == int.class) {
            return ValueLayout.JAVA_INT;
        }
        if (type == long.class) {
            return ValueLayout.JAVA_LONG;
        }
        if (type == float.class) {
            return ValueLayout.JAVA_FLOAT;
        }
        if (type == double.class) {
            return ValueLayout.JAVA_DOUBLE;
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    private static String getNativeName(Method method) {
        var nativeName = method.getAnnotation(FFMName.class);

        String name;
        if (nativeName != null) {
            name = nativeName.value();
        } else {
            name = method.getName();

            var nativePrefix = method.getDeclaringClass().getAnnotation(FFMPrefix.class);
            if (nativePrefix != null && method.getAnnotation(FFMNoPrefix.class) == null) {
                name = nativePrefix.value() + name;
            }
        }
        return name;
    }

    private static FFMCritical getCritical(Method method) {
        var critical = method.getAnnotation(FFMCritical.class);
        if (critical != null) {
            return critical;
        }
        return method.getDeclaringClass().getAnnotation(FFMCritical.class);
    }

    private static String getCharset(Method method) {
        var annotation = method.getAnnotation(FFMCharset.class);
        if (annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(FFMCharset.class);
        }
        return annotation != null ? annotation.value() : "ISO_8859_1";
    }

    private static String getCharset(Parameter parameter) {
        var annotation = parameter.getAnnotation(FFMCharset.class);
        if (annotation == null) {
            annotation = parameter.getDeclaringExecutable().getDeclaringClass().getAnnotation(FFMCharset.class);
        }
        return annotation != null ? annotation.value() : "ISO_8859_1";
    }

    private static void printModel(CompoundElement<?> model, int intent) {
        for (Object t : model) {
            System.err.println("\t".repeat(intent) + t.toString());
            if (t instanceof CompoundElement<?>) {
                var ce = (CompoundElement<?>)t;
                printModel(ce, intent + 1);
            }
        }
    }

}