/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.nuklear;

import org.lwjgl.system.*;
import org.lwjgl.system.libffi.*;

import static org.lwjgl.system.APIUtil.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.libffi.LibFFI.*;

/** Callback function: {@link #invoke nk_query_font_glyph_f} */
@FunctionalInterface
@NativeType("nk_query_font_glyph_f")
public interface NkQueryFontGlyphCallbackI extends CallbackI {

    FFICIF CIF = apiCreateCIF(
        ffi_type_void,
        ffi_type_pointer, ffi_type_float, ffi_type_pointer, ffi_type_uint32, ffi_type_uint32
    );

    @Override
    default FFICIF getCallInterface() { return CIF; }

    @Override
    default void callback(long ret, long args) {
        invoke(
            memGetAddress(memGetAddress(args)),
            memGetFloat(memGetAddress(args + POINTER_SIZE)),
            memGetAddress(memGetAddress(args + 2 * POINTER_SIZE)),
            memGetInt(memGetAddress(args + 3 * POINTER_SIZE)),
            memGetInt(memGetAddress(args + 4 * POINTER_SIZE))
        );
    }

    /** {@code void (* nk_query_font_glyph_f) (nk_handle handle, float font_height, struct nk_user_font_glyph * glyph, nk_rune codepoint, nk_rune next_codepoint)} */
    void invoke(@NativeType("nk_handle") long handle, float font_height, @NativeType("struct nk_user_font_glyph *") long glyph, @NativeType("nk_rune") int codepoint, @NativeType("nk_rune") int next_codepoint);

}