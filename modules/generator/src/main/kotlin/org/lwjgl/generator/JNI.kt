/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.generator

import java.io.*
import java.util.concurrent.*

/** Deduplicates JNI signatures from bindings and generates the org.lwjgl.system.JNI class. */
object JNI : GeneratorTargetNative(Module.CORE, "JNI") {

    // TODO: store the deduplicated signatures in a versioned file to avoid surprising changes when a module is disabled
    // TODO: ignore the cached signatures with a build flag

    private val signatures = ConcurrentHashMap<Signature, Unit>()
    private val signaturesArray = ConcurrentHashMap<SignatureArray, Unit>()

    init {
        // Force generation of signatures that are not used by any binding, but are required for
        // bootstrapping or other internal functionality.

        // invokePPV(NSView, setWantsBestResolutionOpenGLSurface, true/false, objc_msgSend);
        signatures[Signature(CallingConvention.DEFAULT, void, listOf(opaque_p, opaque_p, bool))] = Unit
    }

    private val sortedSignatures by lazy(LazyThreadSafetyMode.NONE) { signatures.keys.sorted() }
    private val sortedSignaturesArray by lazy(LazyThreadSafetyMode.NONE) { signaturesArray.keys.sorted() }

    internal fun register(function: Func) = signatures.put(Signature(function), Unit)
    internal fun registerArray(function: Func) = signaturesArray.put(SignatureArray(function), Unit)

    internal fun register(function: CallbackFunction) = signatures.put(Signature(function), Unit)

    init {
		javaImport("org.jspecify.annotations.*")
    }

    override fun PrintWriter.generateJava() {
        generateJavaPreamble()
        println(processDocumentation(
            """
            This class contains native methods that can be used to call dynamically loaded functions. It is used internally by the LWJGL bindings, but can also
            be used to call other dynamically loaded functions. Not all possible signatures are available, only those needed by the LWJGL bindings. To call a
            function that does not have a matching JNI method, {@link org.lwjgl.system.libffi.LibFFI LibFFI} can used.

            All JNI methods in this class take an extra parameter, called {@code $FUNCTION_ADDRESS}. This must be a valid pointer to a native function with a
            matching signature. Due to overloading, method names are partially mangled:
            ${ul(
                """
                {@code call} or {@code invoke}

                Methods with the {@code invoke} prefix will invoke the native function with the default calling convention. Methods with the {@code call}
                prefix will invoke the native function with the {@code __stdcall} calling convention on Windows and the default calling convention on other
                systems.
                """,
                "a {@code U} (unsigned) or a {@code B} (signed) for each {@code byte} parameter",
                "a {@code C} (unsigned) or an {@code S} (signed) for each {@code short} parameter",
                """
                a {@code J}, a {@code N} or a {@code P} for each {@code long} parameter

                {@code J} parameters represent 64-bit integer values. {@code N} parameters represent C long values, which may be 32-bit or 64-bit integers
                depending on the platform. {@code P} parameters represent pointer addresses, which maybe 32-bit or 64-bit values depending on the JVM.
                """,
                """
                the return value <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/types.html\#type_signatures">JNI type signature</a>
                """
            )}
            """
        ).toJavaDoc(indentation = ""))
        print("""public final class JNI {

    static {
        Library.initialize();
    }

    private JNI() {}

    // Pointer API

""")
        sortedSignatures.forEach {
            print("${t}public static native ${it.returnType.nativeMethodType} ${it.signature}(")
            if (it.arguments.isNotEmpty())
                print(it.arguments.asSequence()
                    .mapIndexed { i, param -> "${param.nativeMethodType} param$i" }
                    .joinToString(", ", postfix = ", "))
            println("long $FUNCTION_ADDRESS);")
        }

        println("\n$t// Array API\n")

        sortedSignaturesArray.forEach {
            print("${t}public static native ${it.returnType.nativeMethodType} ${it.signature}(")
            if (it.arguments.isNotEmpty())
                print(it.arguments.asSequence()
                    .mapIndexed { i, param -> if (param is ArrayType<*>) "${param.mapping.primitive} @Nullable [] param$i" else "${param.nativeMethodType} param$i" }
                    .joinToString(", ", postfix = ", "))
            println("long $FUNCTION_ADDRESS);")
        }
        println("\n}")
    }

    fun PrintWriter.generateJavaFFM() {
        javaImport(
            "org.lwjgl.system.ffm.*",
            "java.lang.foreign.*",
            "java.lang.invoke.*",
            "java.util.*"
        )
        generateJavaPreamble()
        print("""public final class JNI {

    static final class LazyInit {
        static final SharedLibrary LWJGL = Library.loadNative(JNI.class, "org.lwjgl", "lwjgl");
    }

    @FFMFunctionAddress
    private interface JNIBindings {""")
        sortedSignatures.forEach {
            print("\n$t${t}${if (it.returnType.isPointer) "@FFMPointer " else ""}public ${it.returnType.nativeMethodType} ${it.signature}(MemorySegment __functionAddress")
            if (it.arguments.isNotEmpty()) {
                print(it.arguments.asSequence()
                    .mapIndexed { i, param -> "${if (param.isPointer) "@FFMNullable @FFMPointer " else ""}${param.nativeMethodType} param$i" }
                    .joinToString(", ", prefix = ", "))
            }
            print(");")
        }
    print("""
    }

    /*private static final TraceConsumer TRACER = (method, returnValue, args) -> {
        var prefix = method.getDeclaringClass().getAnnotation(FFMPrefix.class);
        if (prefix != null) {
            System.err.print(prefix.value());
        }
        System.err.println(method.getName() + '(' + Stream.of(args)
            .skip(1)
            .map(JNI::render)
            .collect(Collectors.joining(", ")) + ")" + (returnValue == null ? "" : " : " + render(returnValue)));
    };

    private static String render(Object value) {
        if (value instanceof MemorySegment segment) {
            return "0x" + Long.toHexString(segment.address()) + (segment.byteSize() == 0 ? "" : (" [" + (segment.byteSize() == Long.MAX_VALUE ? "?" : segment.byteSize()) + "]"));
        } else {
            return value.toString();
        }
    }*/

    /*private static int count;
    private static long lastT = System.nanoTime();
    private static final TraceConsumer TRACER = (_, _, _) -> {
        count++;
        long t = System.nanoTime();
        if (t - lastT > 1_000_000_000L) {
            System.err.println("JNI calls: " + count + "/s");
            lastT = t;
            count = 0;
        }
    };*/

    private static final JNIBindings jni = BindingGenerator.generate(
        MethodHandles.lookup(),
        JNIBindings.class,
        BindingConfig.builder()
            .withLookup((SymbolLookup)name -> Optional.of(MemorySegment.ofAddress(LazyInit.LWJGL.getFunctionAddress(name))))
            //.withTracing(TRACER)
            .build()
    );

    private JNI() {}

    // Pointer API

""")
        sortedSignatures.forEach {
            print("${t}public static ${it.returnType.nativeMethodType} ${it.signature}(")
            if (it.arguments.isNotEmpty())
                print(it.arguments.asSequence()
                    .mapIndexed { i, param -> "${param.nativeMethodType} param$i" }
                    .joinToString(", ", postfix = ", "))
            print("long $FUNCTION_ADDRESS) { ")
            if (it.returnType.mapping !== TypeMapping.VOID) {
                print("return ")
            }
            print("jni.${it.signature}(MemorySegment.ofAddress(__functionAddress)")
            if (it.arguments.isNotEmpty()) {
                print(it.arguments.asSequence()
                    .mapIndexed { i, param -> "param$i" }
                    .joinToString(", ", prefix = ", "))
            }
            println("); }")
        }

        println("\n$t// Array API\n")

        sortedSignaturesArray.forEach {
            print("${t}public static native ${it.returnType.nativeMethodType} ${it.signature}(")
            if (it.arguments.isNotEmpty())
                print(it.arguments.asSequence()
                    .mapIndexed { i, param -> if (param is ArrayType<*>) "@Nullable ${param.mapping.primitive}[] param$i" else "${param.nativeMethodType} param$i" }
                    .joinToString(", ", postfix = ", "))
            println("long $FUNCTION_ADDRESS);")
        }
        println("\n}")
    }

    private val NativeType.nativeType
        get() = if (this.isPointer)
            "uintptr_t"
        else if (this.mapping == PrimitiveMapping.CLONG)
            "long"
        else
            this.jniFunctionType

    private val NativeType.jniFunctionTypeArray get() = if (this is ArrayType<*>) "j${this.mapping.primitive}Array" else this.jniFunctionType

    override fun PrintWriter.generateNative() {
        nativeDirective("""
#ifdef LWJGL_WINDOWS
    #define APIENTRY __stdcall
#else
    #define APIENTRY
#endif
""")

        print(HEADER)
        preamble.printNative(this)

        sortedSignatures.forEach {
            print("JNIEXPORT ${it.returnType.jniFunctionType} JNICALL Java_org_lwjgl_system_JNI_${it.signatureNative}(JNIEnv *$JNIENV, jclass clazz, ")
            if (it.arguments.isNotEmpty())
                print(it.arguments.asSequence()
                    .mapIndexed { i, param -> "${param.jniFunctionType} param$i" }
                    .joinToString(", ", postfix = ", "))
            print("""jlong $FUNCTION_ADDRESS) {
    UNUSED_PARAMS($JNIENV, clazz)
    """)
            if (it.returnType.mapping !== TypeMapping.VOID) {
                print("return ")
                val resultType = it.returnType.jniFunctionType
                if (it.returnType.abiType != resultType)
                    print("($resultType)")
            }
            print("((${it.returnType.abiType} (${if (it.callingConvention === CallingConvention.STDCALL) "APIENTRY " else ""}*) ")
            print(if (it.arguments.isEmpty())
                "(void)"
            else
                it.arguments.asSequence()
                    .joinToString(", ", prefix = "(", postfix = ")") { arg -> arg.abiType }
            )
            print(")(uintptr_t)$FUNCTION_ADDRESS)(")
            print(it.arguments.asSequence()
                .mapIndexed { i, arg ->
                    val abiType = arg.abiType
                    if (abiType.startsWith("j"))
                        "param$i"
                    else
                        "($abiType)param$i"
                }
                .joinToString(", "))
            print(""");
}
""")
        }

        println()

        sortedSignaturesArray.forEach {
            print(
                """JNIEXPORT ${it.returnType.jniFunctionType} JNICALL Java_org_lwjgl_system_JNI_${it.signatureArray}(JNIEnv *$JNIENV, jclass clazz, ${
                if (it.arguments.isEmpty()) "" else it.arguments
                    .mapIndexed { i, param -> "${param.jniFunctionTypeArray} param$i" }
                    .joinToString(", ")
                }, jlong $FUNCTION_ADDRESS) {
    UNUSED_PARAMS($JNIENV, clazz)
    ${it.arguments.asSequence()
        .mapIndexedNotNull { i, type -> if (type !is ArrayType<*>) null else "void *paramArray$i = param$i == NULL ? NULL : (*$JNIENV)->Get${type.mapping.box}ArrayElements($JNIENV, param$i, NULL);" }
        .joinToString("\n$t")}
    """)
            if (it.returnType.mapping !== TypeMapping.VOID) {
                val resultType = it.returnType.jniFunctionType
                print("$resultType $RESULT = ")
                if (it.returnType.abiType != resultType)
                    print("($resultType)")
            }
            print("((${it.returnType.abiType} (${if (it.callingConvention === CallingConvention.STDCALL) "APIENTRY " else ""}*) ")
            print(it.arguments.asSequence()
                .joinToString(", ", prefix = "(", postfix = ")") { arg -> arg.nativeType })
            print(")(uintptr_t)$FUNCTION_ADDRESS)(")
            print(it.arguments.asSequence()
                .mapIndexed { i, param -> if (param is ArrayType<*>)
                    "(uintptr_t)paramArray$i"
                else if (param.isPointer)
                    "(uintptr_t)param$i"
                else if (param.mapping === PrimitiveMapping.CLONG)
                    "(long)param$i"
                else
                    "param$i" }
                .joinToString(", "))

            println(""");
    ${it.arguments.asSequence()
        .withIndex()
        .sortedByDescending { (index) -> index }
        .mapNotNull { (index, type) ->
            if (type !is ArrayType<*>)
                null
            else
                "if (param$index != NULL) { (*$JNIENV)->Release${type.mapping.box}ArrayElements($JNIENV, param$index, paramArray$index, 0); }"
        }
        .joinToString("\n$t")}${if (it.returnType.mapping === TypeMapping.VOID) "" else """
    return $RESULT;"""}
}""")
        }

        println("\nEXTERN_C_EXIT")
    }
}

private open class Signature constructor(
    val callingConvention: CallingConvention,
    val returnType: NativeType,
    val arguments: List<NativeType>
) : Comparable<Signature> {

    //val key = "${callingConvention.method}${arguments.asSequence().joinToString("") { it.jniSignature }}${returnType.jniSignature}"

    val signature = "${callingConvention.method}${arguments.asSequence().joinToString("") { it.jniSignatureJava }}${returnType.jniSignature}"
    val signatureNative = "${signature}__${arguments.asSequence().joinToString("") { it.jniSignatureStrict }}J"

    constructor(function: Func) : this(
        function.nativeClass.callingConvention,
        function.returns.nativeType,
        function.parameters.asSequence()
            .filter { it !== EXPLICIT_FUNCTION_ADDRESS }
            .map { it.nativeType }
            .toList()
    )

    constructor(function: CallbackFunction) : this(
        function.module.callingConvention,
        function.returns,
        function.signature.asSequence()
            .map { it.nativeType }
            .toList()
    )

    override fun equals(other: Any?) = other is Signature && this.signatureNative == other.signatureNative

    override fun hashCode(): Int = signatureNative.hashCode()

    override fun compareTo(other: Signature): Int {
        this.callingConvention.ordinal.compareTo(other.callingConvention.ordinal).let { if (it != 0) return it }
        this.returnType.jniSignature.compareTo(other.returnType.jniSignature).let { if (it != 0) return it }

        val javaSignature0 = this.arguments.asSequence().joinToString("") { it.jniSignatureJava }
        val javaSignature1 = other.arguments.asSequence().joinToString("") { it.jniSignatureJava }

        javaSignature0.length.compareTo(javaSignature1.length).let { if (it != 0) return it }
        this.arguments.size.compareTo(other.arguments.size).let { if (it != 0) return it }
        javaSignature0.compareTo(javaSignature1).let { if (it != 0) return it }

        return this.signatureNative.compareTo(other.signatureNative)
    }

}

private class SignatureArray constructor(
    callingConvention: CallingConvention,
    returnType: NativeType,
    arguments: List<NativeType>
) : Signature(callingConvention, returnType, arguments) {

    val signatureArray = "${signature}__${arguments.asSequence().joinToString("") { if (it is ArrayType<*>) it.jniSignatureArray else it.jniSignatureStrict }}J"

    constructor(function: Func) : this(
        function.nativeClass.callingConvention,
        function.returns.nativeType,
        function.parameters.asSequence()
            .filter { it !== EXPLICIT_FUNCTION_ADDRESS }
            .map { it.nativeType }
            .toList()
    )

    override fun equals(other: Any?) = other is SignatureArray && this.signatureArray == other.signatureArray

    override fun hashCode(): Int = signatureArray.hashCode()

    override fun compareTo(other: Signature): Int {
        this.callingConvention.ordinal.compareTo(other.callingConvention.ordinal).let { if (it != 0) return it }
        this.returnType.jniSignature.compareTo(other.returnType.jniSignature).let { if (it != 0) return it }

        val javaSignature0 = this.arguments.asSequence().joinToString("") { it.jniSignatureJava }
        val javaSignature1 = other.arguments.asSequence().joinToString("") { it.jniSignatureJava }

        javaSignature0.length.compareTo(javaSignature1.length).let { if (it != 0) return it }
        this.arguments.size.compareTo(other.arguments.size).let { if (it != 0) return it }
        javaSignature0.compareTo(javaSignature1).let { if (it != 0) return it }

        return this.signatureArray.compareTo((other as SignatureArray).signatureArray)
    }

}