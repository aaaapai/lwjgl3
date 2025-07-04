/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.util.yoga;

import org.lwjgl.system.*;
import org.lwjgl.system.libffi.*;

import static org.lwjgl.system.APIUtil.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.libffi.LibFFI.*;

/** Callback function: {@link #invoke YGBaselineFunc} */
@FunctionalInterface
@NativeType("YGBaselineFunc")
public interface YGBaselineFuncI extends CallbackI {

    FFICIF CIF = apiCreateCIF(
        ffi_type_float,
        ffi_type_pointer, ffi_type_float, ffi_type_float
    );

    @Override
    default FFICIF getCallInterface() { return CIF; }

    @Override
    default void callback(long ret, long args) {
        float __result = invoke(
            memGetAddress(memGetAddress(args)),
            memGetFloat(memGetAddress(args + POINTER_SIZE)),
            memGetFloat(memGetAddress(args + 2 * POINTER_SIZE))
        );
        apiClosureRet(ret, __result);
    }

    /** {@code float (* YGBaselineFunc) (YGNodeConstRef node, float width, float height)} */
    float invoke(@NativeType("YGNodeConstRef") long node, float width, float height);

}