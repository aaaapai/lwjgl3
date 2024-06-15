/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package llvm.templates

import llvm.*
import org.lwjgl.generator.*

val LLVMTargetMachine = "LLVMTargetMachine".nativeClass(
    Module.LLVM,
    prefixConstant = "LLVM",
    prefixMethod = "LLVM",
    binding = LLVM_BINDING_DELEGATE
) {
    documentation = ""

    EnumConstant(
        "{@code LLVMCodeGenOptLevel}",

        "CodeGenLevelNone".enum("", "0"),
        "CodeGenLevelLess".enum,
        "CodeGenLevelDefault".enum,
        "CodeGenLevelAggressive".enum
    )

    EnumConstant(
        "{@code LLVMRelocMode}",

        "RelocDefault".enum("", "0"),
        "RelocStatic".enum,
        "RelocPIC".enum,
        "RelocDynamicNoPic".enum,
        "RelocROPI".enum,
        "RelocRWPI".enum,
        "RelocROPI_RWPI".enum
    )

    EnumConstant(
        "{@code LLVMCodeModel}",

        "CodeModelDefault".enum("", "0"),
        "CodeModelJITDefault".enum,
        "CodeModelTiny".enum,
        "CodeModelSmall".enum,
        "CodeModelKernel".enum,
        "CodeModelMedium".enum,
        "CodeModelLarge".enum
    )

    EnumConstant(
        "{@code LLVMCodeGenFileType}",

        "AssemblyFile".enum("", "0"),
        "ObjectFile".enum
    )

    LLVMTargetRef(
        "GetFirstTarget",
        "Returns the first {@code llvm::Target} in the registered targets list.",

        void()
    )

    LLVMTargetRef(
        "GetNextTarget",
        "Returns the next {@code llvm::Target} given a previous one (or null if there's none)",

        LLVMTargetRef("T", "")
    )

    LLVMTargetRef(
        "GetTargetFromName",
        "Finds the target corresponding to the given name and stores it in {@code T}. Returns 0 on success.",

        charUTF8.const.p("Name", "")
    )

    LLVMBool(
        "GetTargetFromTriple",
        """
        Finds the target corresponding to the given triple and stores it in {@code T}. Returns 0 on success. Optionally returns any error in
        {@code ErrorMessage}. Use #DisposeMessage() to dispose the message.
        """,

        charUTF8.const.p("Triple", ""),
        Check(1)..LLVMTargetRef.p("T", ""),
        Check(1)..charUTF8.p.p("ErrorMessage", "")
    )

    charUTF8.const.p(
        "GetTargetName",
        "Returns the name of a target. See {@code llvm::Target::getName}",

        LLVMTargetRef("T", "")
    )

    charUTF8.const.p(
        "GetTargetDescription",
        "Returns the description  of a target. See {@code llvm::Target::getDescription}",

        LLVMTargetRef("T", "")
    )

    LLVMBool(
        "TargetHasJIT",
        "Returns if the target has a JIT",

        LLVMTargetRef("T", "")
    )

    LLVMBool(
        "TargetHasTargetMachine",
        "Returns if the target has a {@code TargetMachine} associated",

        LLVMTargetRef("T", "")
    )

    LLVMBool(
        "TargetHasAsmBackend",
        "Returns if the target as an ASM backend (required for emitting output)",

        LLVMTargetRef("T", "")
    )

    LLVMTargetMachineRef(
        "CreateTargetMachine",
        "Creates a new {@code llvm::TargetMachine}. See {@code llvm::Target::createTargetMachine}",

        LLVMTargetRef("T", ""),
        charUTF8.const.p("Triple", ""),
        charUTF8.const.p("CPU", ""),
        charUTF8.const.p("Features", ""),
        LLVMCodeGenOptLevel("Level", ""),
        LLVMRelocMode("Reloc", ""),
        LLVMCodeModel("CodeModel", "")
    )

    void(
        "DisposeTargetMachine",
        "Dispose the {@code LLVMTargetMachineRef} instance generated by #CreateTargetMachine().",

        LLVMTargetMachineRef("T", "")
    )

    LLVMTargetRef(
        "GetTargetMachineTarget",
        "Returns the {@code Target} used in a {@code TargetMachine}",

        LLVMTargetMachineRef("T", "")
    )

    charUTF8.p(
        "GetTargetMachineTriple",
        """
        Returns the triple used creating this target machine. See {@code llvm::TargetMachine::getTriple}. The result needs to be disposed with
        #DisposeMessage().
        """,

        LLVMTargetMachineRef("T", "")
    )

    charUTF8.p(
        "GetTargetMachineCPU",
        "Returns the cpu used creating this target machine. See {@code llvm::TargetMachine::getCPU}. The result needs to be disposed with #DisposeMessage().",

        LLVMTargetMachineRef("T", "")
    )

    charUTF8.p(
        "GetTargetMachineFeatureString",
        """
        Returns the feature string used creating this target machine. See {@code llvm::TargetMachine::getFeatureString}. The result needs to be disposed with
        #DisposeMessage().
        """,

        LLVMTargetMachineRef("T", "")
    )

    LLVMTargetDataRef(
        "CreateTargetDataLayout",
        "Create a {@code DataLayout} based on the target machine.",

        LLVMTargetMachineRef("T", "")
    )

    void(
        "SetTargetMachineAsmVerbosity",
        "Set the target machine's ASM verbosity.",

        LLVMTargetMachineRef("T", ""),
        LLVMBool("VerboseAsm", "")
    )

    LLVMBool(
        "TargetMachineEmitToFile",
        """
        Emits an asm or object file for the given module to the filename. This wraps several c++ only classes (among them a file stream). Returns any error in
        {@code ErrorMessage}. Use #DisposeMessage() to dispose the message.
        """,

        LLVMTargetMachineRef("T", ""),
        LLVMModuleRef("M", ""),
        charUTF8.const.p("Filename", ""),
        LLVMCodeGenFileType("codegen", ""),
        Check(1)..charUTF8.p.p("ErrorMessage", "")
    )

    LLVMBool(
        "TargetMachineEmitToMemoryBuffer",
        "Compile the LLVM IR stored in {@code M} and store the result in {@code OutMemBuf}.",

        LLVMTargetMachineRef("T", ""),
        LLVMModuleRef("M", ""),
        LLVMCodeGenFileType("codegen", ""),
        Check(1)..charUTF8.p.p("ErrorMessage", ""),
        Check(1)..LLVMMemoryBufferRef.p("OutMemBuf", "")
    )

    charUTF8.p(
        "GetDefaultTargetTriple",
        "Get a triple for the host machine as a string. The result needs to be disposed with #DisposeMessage().",

        void()
    )

    IgnoreMissing..charUTF8.p(
        "NormalizeTargetTriple",
        "Normalize a target triple. The result needs to be disposed with #DisposeMessage().",

        charUTF8.const.p("triple", "")
    )

    IgnoreMissing..charUTF8.p(
        "GetHostCPUName",
        "Get the host CPU as a string. The result needs to be disposed with #DisposeMessage().",

        void()
    )

    IgnoreMissing..charUTF8.p(
        "GetHostCPUFeatures",
        "Get the host CPU's features as a string. The result needs to be disposed with #DisposeMessage().",

        void()
    )

    void(
        "AddAnalysisPasses",
        "Adds the target-specific analysis passes to the pass manager.",

        LLVMTargetMachineRef("T", ""),
        LLVMPassManagerRef("PM", "")
    )
}