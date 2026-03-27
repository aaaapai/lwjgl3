/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.stb;

import org.jspecify.annotations.*;

import java.nio.*;

import org.lwjgl.system.*;

import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.MemoryUtil.*;

/** Native bindings to stb_image_resize2.h from the <a href="https://github.com/nothings/stb">stb library</a>. */
public class STBImageResize {

    static { LibSTB.initialize(); }

    /**
     * {@code stbir_pixel_layout} specifies:
     * 
     * <ul>
     * <li>number of channels</li>
     * <li>order of channels</li>
     * <li>whether color is premultiplied by alpha</li>
     * </ul>
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #STBIR_1CHANNEL 1CHANNEL}</li>
     * <li>{@link #STBIR_2CHANNEL 2CHANNEL}</li>
     * <li>{@link #STBIR_RGB RGB}</li>
     * <li>{@link #STBIR_BGR BGR}</li>
     * <li>{@link #STBIR_4CHANNEL 4CHANNEL}</li>
     * <li>{@link #STBIR_RGBA RGBA}</li>
     * <li>{@link #STBIR_BGRA BGRA}</li>
     * <li>{@link #STBIR_ARGB ARGB}</li>
     * <li>{@link #STBIR_ABGR ABGR}</li>
     * <li>{@link #STBIR_RA RA}</li>
     * <li>{@link #STBIR_AR AR}</li>
     * <li>{@link #STBIR_RGBA_PM RGBA_PM}</li>
     * <li>{@link #STBIR_BGRA_PM BGRA_PM}</li>
     * <li>{@link #STBIR_ARGB_PM ARGB_PM}</li>
     * <li>{@link #STBIR_ABGR_PM ABGR_PM}</li>
     * <li>{@link #STBIR_RA_PM RA_PM}</li>
     * <li>{@link #STBIR_AR_PM AR_PM}</li>
     * <li>{@link #STBIR_RGBA_NO_AW RGBA_NO_AW}</li>
     * <li>{@link #STBIR_BGRA_NO_AW BGRA_NO_AW}</li>
     * <li>{@link #STBIR_ARGB_NO_AW ARGB_NO_AW}</li>
     * <li>{@link #STBIR_ABGR_NO_AW ABGR_NO_AW}</li>
     * <li>{@link #STBIR_RA_NO_AW RA_NO_AW}</li>
     * <li>{@link #STBIR_AR_NO_AW AR_NO_AW}</li>
     * </ul>
     */
    public static final int
        STBIR_1CHANNEL   = 1,
        STBIR_2CHANNEL   = 2,
        STBIR_RGB        = 3,
        STBIR_BGR        = 0,
        STBIR_4CHANNEL   = 5,
        STBIR_RGBA       = 4,
        STBIR_BGRA       = 6,
        STBIR_ARGB       = 7,
        STBIR_ABGR       = 8,
        STBIR_RA         = 9,
        STBIR_AR         = 10,
        STBIR_RGBA_PM    = 11,
        STBIR_BGRA_PM    = 12,
        STBIR_ARGB_PM    = 13,
        STBIR_ABGR_PM    = 14,
        STBIR_RA_PM      = 15,
        STBIR_AR_PM      = 16,
        STBIR_RGBA_NO_AW = 11,
        STBIR_BGRA_NO_AW = 12,
        STBIR_ARGB_NO_AW = 13,
        STBIR_ABGR_NO_AW = 14,
        STBIR_RA_NO_AW   = 15,
        STBIR_AR_NO_AW   = 16;

    /**
     * {@code stbir_edge}
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #STBIR_EDGE_CLAMP EDGE_CLAMP}</li>
     * <li>{@link #STBIR_EDGE_REFLECT EDGE_REFLECT}</li>
     * <li>{@link #STBIR_EDGE_WRAP EDGE_WRAP} - This edge mode is slower and uses more memory.</li>
     * <li>{@link #STBIR_EDGE_ZERO EDGE_ZERO}</li>
     * </ul>
     */
    public static final int
        STBIR_EDGE_CLAMP   = 0,
        STBIR_EDGE_REFLECT = 1,
        STBIR_EDGE_WRAP    = 2,
        STBIR_EDGE_ZERO    = 3;

    /**
     * {@code stbir_filter}
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #STBIR_FILTER_DEFAULT FILTER_DEFAULT} - Use same filter type that easy-to-use API chooses.</li>
     * <li>{@link #STBIR_FILTER_BOX FILTER_BOX} - A trapezoid w/1-pixel wide ramps, same result as box for integer scale ratios.</li>
     * <li>{@link #STBIR_FILTER_TRIANGLE FILTER_TRIANGLE} - On upsampling, produces same results as bilinear texture filtering.</li>
     * <li>{@link #STBIR_FILTER_CUBICBSPLINE FILTER_CUBICBSPLINE} - The cubic b-spline (aka Mitchell-Netrevalli with {@code B=1,C=0}), gaussian-esque.</li>
     * <li>{@link #STBIR_FILTER_CATMULLROM FILTER_CATMULLROM} - An interpolating cubic spline.</li>
     * <li>{@link #STBIR_FILTER_MITCHELL FILTER_MITCHELL} - Mitchell-Netrevalli filter with {@code B=1/3,C=1/3}.</li>
     * <li>{@link #STBIR_FILTER_POINT_SAMPLE FILTER_POINT_SAMPLE} - Simple point sampling.</li>
     * <li>{@link #STBIR_FILTER_OTHER FILTER_OTHER} - User callback specified.</li>
     * </ul>
     */
    public static final int
        STBIR_FILTER_DEFAULT      = 0,
        STBIR_FILTER_BOX          = 1,
        STBIR_FILTER_TRIANGLE     = 2,
        STBIR_FILTER_CUBICBSPLINE = 3,
        STBIR_FILTER_CATMULLROM   = 4,
        STBIR_FILTER_MITCHELL     = 5,
        STBIR_FILTER_POINT_SAMPLE = 6,
        STBIR_FILTER_OTHER        = 7;

    /**
     * {@code stbir_datatype}
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #STBIR_TYPE_UINT8 TYPE_UINT8}</li>
     * <li>{@link #STBIR_TYPE_UINT8_SRGB TYPE_UINT8_SRGB}</li>
     * <li>{@link #STBIR_TYPE_UINT8_SRGB_ALPHA TYPE_UINT8_SRGB_ALPHA} - Alpha channel, when present, should also be SRGB (this is very unusual).</li>
     * <li>{@link #STBIR_TYPE_UINT16 TYPE_UINT16}</li>
     * <li>{@link #STBIR_TYPE_FLOAT TYPE_FLOAT}</li>
     * <li>{@link #STBIR_TYPE_HALF_FLOAT TYPE_HALF_FLOAT}</li>
     * </ul>
     */
    public static final int
        STBIR_TYPE_UINT8            = 0,
        STBIR_TYPE_UINT8_SRGB       = 1,
        STBIR_TYPE_UINT8_SRGB_ALPHA = 2,
        STBIR_TYPE_UINT16           = 3,
        STBIR_TYPE_FLOAT            = 4,
        STBIR_TYPE_HALF_FLOAT       = 5;

    protected STBImageResize() {
        throw new UnsupportedOperationException();
    }

    // --- [ stbir_resize_uint8_srgb ] ---

    public static native long nstbir_resize_uint8_srgb(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int pixel_type);

    @NativeType("unsigned char *")
    public static @Nullable ByteBuffer stbir_resize_uint8_srgb(@NativeType("unsigned char const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("unsigned char *") @Nullable ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, @NativeType("stbir_pixel_layout") int pixel_type) {
        int length = calculateBufferSize(output_w, output_h, output_stride_in_bytes, pixel_type, 1);
        if (CHECKS) {
            checkSafe(output_pixels, length);
        }
        long __result = nstbir_resize_uint8_srgb(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddressSafe(output_pixels), output_w, output_h, output_stride_in_bytes, pixel_type);
        return memByteBufferSafe(__result, length);
    }

    @NativeType("unsigned char *")
    public static @Nullable ByteBuffer stbir_resize_uint8_srgb(@NativeType("unsigned char const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("unsigned char *") @Nullable ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, @NativeType("stbir_pixel_layout") int pixel_type, long length) {
        if (CHECKS) {
            checkSafe(output_pixels, length);
        }
        long __result = nstbir_resize_uint8_srgb(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddressSafe(output_pixels), output_w, output_h, output_stride_in_bytes, pixel_type);
        return memByteBufferSafe(__result, (int)length);
    }

    // --- [ stbir_resize_uint8_linear ] ---

    public static native long nstbir_resize_uint8_linear(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int pixel_type);

    @NativeType("unsigned char *")
    public static @Nullable ByteBuffer stbir_resize_uint8_linear(@NativeType("unsigned char const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("unsigned char *") @Nullable ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, @NativeType("stbir_pixel_layout") int pixel_type) {
        int length = calculateBufferSize(output_w, output_h, output_stride_in_bytes, pixel_type, 1);
        if (CHECKS) {
            checkSafe(output_pixels, length);
        }
        long __result = nstbir_resize_uint8_linear(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddressSafe(output_pixels), output_w, output_h, output_stride_in_bytes, pixel_type);
        return memByteBufferSafe(__result, length);
    }

    @NativeType("unsigned char *")
    public static @Nullable ByteBuffer stbir_resize_uint8_linear(@NativeType("unsigned char const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("unsigned char *") @Nullable ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, @NativeType("stbir_pixel_layout") int pixel_type, long length) {
        if (CHECKS) {
            checkSafe(output_pixels, length);
        }
        long __result = nstbir_resize_uint8_linear(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddressSafe(output_pixels), output_w, output_h, output_stride_in_bytes, pixel_type);
        return memByteBufferSafe(__result, (int)length);
    }

    // --- [ stbir_resize_float_linear ] ---

    public static native long nstbir_resize_float_linear(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int pixel_type);

    @NativeType("float *")
    public static @Nullable FloatBuffer stbir_resize_float_linear(@NativeType("float const *") FloatBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("float *") @Nullable FloatBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, @NativeType("stbir_pixel_layout") int pixel_type) {
        int length = calculateBufferSize(output_w, output_h, output_stride_in_bytes, pixel_type, 4);
        if (CHECKS) {
            checkSafe(output_pixels, length);
        }
        long __result = nstbir_resize_float_linear(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddressSafe(output_pixels), output_w, output_h, output_stride_in_bytes, pixel_type);
        return memFloatBufferSafe(__result, length);
    }

    @NativeType("float *")
    public static @Nullable FloatBuffer stbir_resize_float_linear(@NativeType("float const *") FloatBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("float *") @Nullable FloatBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, @NativeType("stbir_pixel_layout") int pixel_type, long length) {
        if (CHECKS) {
            checkSafe(output_pixels, length);
        }
        long __result = nstbir_resize_float_linear(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddressSafe(output_pixels), output_w, output_h, output_stride_in_bytes, pixel_type);
        return memFloatBufferSafe(__result, (int)length);
    }

    // --- [ stbir_resize ] ---

    public static native long nstbir_resize(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int pixel_layout, int data_type, int edge, int filter);

    @NativeType("void *")
    public static @Nullable ByteBuffer stbir_resize(@NativeType("void const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("void *") @Nullable ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, @NativeType("stbir_pixel_layout") int pixel_layout, @NativeType("stbir_datatype") int data_type, @NativeType("stbir_edge") int edge, @NativeType("stbir_filter") int filter) {
        int length = calculateBufferSize(output_w, output_h, output_stride_in_bytes, pixel_layout, stbir_type_size[data_type]);
        if (CHECKS) {
            checkSafe(output_pixels, length);
        }
        long __result = nstbir_resize(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddressSafe(output_pixels), output_w, output_h, output_stride_in_bytes, pixel_layout, data_type, edge, filter);
        return memByteBufferSafe(__result, length);
    }

    @NativeType("void *")
    public static @Nullable ByteBuffer stbir_resize(@NativeType("void const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("void *") @Nullable ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, @NativeType("stbir_pixel_layout") int pixel_layout, @NativeType("stbir_datatype") int data_type, @NativeType("stbir_edge") int edge, @NativeType("stbir_filter") int filter, long length) {
        if (CHECKS) {
            checkSafe(output_pixels, length);
        }
        long __result = nstbir_resize(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddressSafe(output_pixels), output_w, output_h, output_stride_in_bytes, pixel_layout, data_type, edge, filter);
        return memByteBufferSafe(__result, (int)length);
    }

    // --- [ stbir_resize_init ] ---

    public static native void nstbir_resize_init(long resize, long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int pixel_layout, int data_type);

    public static void stbir_resize_init(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, @NativeType("void const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("void *") @Nullable ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, @NativeType("stbir_pixel_layout") int pixel_layout, @NativeType("stbir_datatype") int data_type) {
        if (CHECKS) {
            checkSafe(output_pixels, calculateBufferSize(output_w, output_h, output_stride_in_bytes, pixel_layout, stbir_type_size[data_type]));
        }
        nstbir_resize_init(resize.address(), memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddressSafe(output_pixels), output_w, output_h, output_stride_in_bytes, pixel_layout, data_type);
    }

    // --- [ stbir_set_datatypes ] ---

    public static native void nstbir_set_datatypes(long resize, int input_type, int output_type);

    public static void stbir_set_datatypes(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, @NativeType("stbir_datatype") int input_type, @NativeType("stbir_datatype") int output_type) {
        nstbir_set_datatypes(resize.address(), input_type, output_type);
    }

    // --- [ stbir_set_pixel_callbacks ] ---

    public static native void nstbir_set_pixel_callbacks(long resize, long input_cb, long output_cb);

    public static void stbir_set_pixel_callbacks(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, @NativeType("stbir_input_callback *") @Nullable STBIRInputCallbackI input_cb, @NativeType("stbir_output_callback *") @Nullable STBIROutputCallbackI output_cb) {
        nstbir_set_pixel_callbacks(resize.address(), memAddressSafe(input_cb), memAddressSafe(output_cb));
    }

    // --- [ stbir_set_user_data ] ---

    public static native void nstbir_set_user_data(long resize, long user_data);

    public static void stbir_set_user_data(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, @NativeType("void *") long user_data) {
        nstbir_set_user_data(resize.address(), user_data);
    }

    // --- [ stbir_set_buffer_ptrs ] ---

    public static native void nstbir_set_buffer_ptrs(long resize, long input_pixels, int input_stride_in_bytes, long output_pixels, int output_stride_in_bytes);

    public static void stbir_set_buffer_ptrs(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, @NativeType("void const *") ByteBuffer input_pixels, int input_stride_in_bytes, @NativeType("void *") @Nullable ByteBuffer output_pixels, int output_stride_in_bytes) {
        nstbir_set_buffer_ptrs(resize.address(), memAddress(input_pixels), input_stride_in_bytes, memAddressSafe(output_pixels), output_stride_in_bytes);
    }

    // --- [ stbir_set_pixel_layouts ] ---

    /** Unsafe version of: {@link #stbir_set_pixel_layouts set_pixel_layouts} */
    public static native int nstbir_set_pixel_layouts(long resize, int input_pixel_layout, int output_pixel_layout);

    /** Sets new buffer layouts. */
    public static int stbir_set_pixel_layouts(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, @NativeType("stbir_pixel_layout") int input_pixel_layout, @NativeType("stbir_pixel_layout") int output_pixel_layout) {
        return nstbir_set_pixel_layouts(resize.address(), input_pixel_layout, output_pixel_layout);
    }

    // --- [ stbir_set_edgemodes ] ---

    public static native int nstbir_set_edgemodes(long resize, int horizontal_edge, int vertical_edge);

    public static int stbir_set_edgemodes(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, @NativeType("stbir_edge") int horizontal_edge, @NativeType("stbir_edge") int vertical_edge) {
        return nstbir_set_edgemodes(resize.address(), horizontal_edge, vertical_edge);
    }

    // --- [ stbir_set_filters ] ---

    public static native int nstbir_set_filters(long resize, int horizontal_filter, int vertical_filter);

    public static int stbir_set_filters(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, @NativeType("stbir_filter") int horizontal_filter, @NativeType("stbir_filter") int vertical_filter) {
        return nstbir_set_filters(resize.address(), horizontal_filter, vertical_filter);
    }

    // --- [ stbir_set_filter_callbacks ] ---

    public static native int nstbir_set_filter_callbacks(long resize, long horizontal_filter, long horizontal_support, long vertical_filter, long vertical_support);

    public static int stbir_set_filter_callbacks(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, @NativeType("stbir__kernel_callback *") @Nullable STBIRKernelCallbackI horizontal_filter, @NativeType("stbir__support_callback *") @Nullable STBIRSupportCallbackI horizontal_support, @NativeType("stbir__kernel_callback *") @Nullable STBIRKernelCallbackI vertical_filter, @NativeType("stbir__support_callback *") @Nullable STBIRSupportCallbackI vertical_support) {
        return nstbir_set_filter_callbacks(resize.address(), memAddressSafe(horizontal_filter), memAddressSafe(horizontal_support), memAddressSafe(vertical_filter), memAddressSafe(vertical_support));
    }

    // --- [ stbir_set_pixel_subrect ] ---

    /** Unsafe version of: {@link #stbir_set_pixel_subrect set_pixel_subrect} */
    public static native int nstbir_set_pixel_subrect(long resize, int subx, int suby, int subw, int subh);

    /** Sets input sub-region (full region by default). */
    public static int stbir_set_pixel_subrect(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, int subx, int suby, int subw, int subh) {
        return nstbir_set_pixel_subrect(resize.address(), subx, suby, subw, subh);
    }

    // --- [ stbir_set_input_subrect ] ---

    public static native int nstbir_set_input_subrect(long resize, double s0, double t0, double s1, double t1);

    public static int stbir_set_input_subrect(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, double s0, double t0, double s1, double t1) {
        return nstbir_set_input_subrect(resize.address(), s0, t0, s1, t1);
    }

    // --- [ stbir_set_output_pixel_subrect ] ---

    /** Unsafe version of: {@link #stbir_set_output_pixel_subrect set_output_pixel_subrect} */
    public static native int nstbir_set_output_pixel_subrect(long resize, int subx, int suby, int subw, int subh);

    /** Sets output sub-region (full region by default). */
    public static int stbir_set_output_pixel_subrect(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, int subx, int suby, int subw, int subh) {
        return nstbir_set_output_pixel_subrect(resize.address(), subx, suby, subw, subh);
    }

    // --- [ stbir_set_non_pm_alpha_speed_over_quality ] ---

    /** Unsafe version of: {@link #stbir_set_non_pm_alpha_speed_over_quality set_non_pm_alpha_speed_over_quality} */
    public static native int nstbir_set_non_pm_alpha_speed_over_quality(long resize, int non_pma_alpha_speed_over_quality);

    /**
     * When inputting AND outputting non-premultiplied alpha pixels, we use a slower but higher quality technique that fills the zero alpha pixel's RGB values
     * with something plausible. If you don't care about areas of zero alpha, you can call this function to get about a 25% speed improvement for {@link #STBIR_RGBA RGBA} to
     * {@code STBIR_RGBA} types of resizes.
     */
    public static int stbir_set_non_pm_alpha_speed_over_quality(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, @NativeType("int") boolean non_pma_alpha_speed_over_quality) {
        return nstbir_set_non_pm_alpha_speed_over_quality(resize.address(), non_pma_alpha_speed_over_quality ? 1 : 0);
    }

    // --- [ stbir_build_samplers ] ---

    /** Unsafe version of: {@link #stbir_build_samplers build_samplers} */
    public static native int nstbir_build_samplers(long resize);

    /** Builds the samplers and does one allocation. */
    public static int stbir_build_samplers(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize) {
        return nstbir_build_samplers(resize.address());
    }

    // --- [ stbir_free_samplers ] ---

    /** Unsafe version of: {@link #stbir_free_samplers free_samplers} */
    public static native void nstbir_free_samplers(long resize);

    /** You MUST call this, if you call {@link #stbir_build_samplers build_samplers} or {@link #stbir_build_samplers_with_splits build_samplers_with_splits}. */
    public static void stbir_free_samplers(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize) {
        nstbir_free_samplers(resize.address());
    }

    // --- [ stbir_resize_extended ] ---

    /** Unsafe version of: {@link #stbir_resize_extended resize_extended} */
    public static native int nstbir_resize_extended(long resize);

    /** The main function to perform the resize synchronously on one thread. */
    public static int stbir_resize_extended(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize) {
        return nstbir_resize_extended(resize.address());
    }

    // --- [ stbir_build_samplers_with_splits ] ---

    /** Unsafe version of: {@link #stbir_build_samplers_with_splits build_samplers_with_splits} */
    public static native int nstbir_build_samplers_with_splits(long resize, int try_splits);

    /**
     * Build samplers for threading.
     * 
     * <p>You can pass in the number of threads you'd like to use ({@code try_splits}). It returns the number of splits (threads) that you can call it with. It
     * might be less if the image resize can't be split up that many ways.</p>
     */
    public static int stbir_build_samplers_with_splits(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, int try_splits) {
        return nstbir_build_samplers_with_splits(resize.address(), try_splits);
    }

    // --- [ stbir_resize_extended_split ] ---

    /** Unsafe version of: {@link #stbir_resize_extended_split resize_extended_split} */
    public static native int nstbir_resize_extended_split(long resize, int split_start, int split_count);

    /**
     * This function does a split of the resizing (you call this fuction for each split, on multiple threads). A split is a piece of the output resize pixel
     * space.
     * 
     * <p>Note that you MUST call {@link #stbir_build_samplers_with_splits build_samplers_with_splits} before {@code stbir_resize_extended_split}!</p>
     * 
     * <p>Usually, you will always call {@code stbir_resize_extended_split} with {@code split_start} as the {@code thread_index} and "1" for the
     * {@code split_count}. But, if you have a weird situation where you MIGHT want 8 threads, but sometimes only 4 threads, you can use 0,2,4,6 for the
     * {@code split_start}'s and use "2" for the {@code split_count} each time to turn in into a 4 thread resize. (This is unusual).</p>
     */
    public static int stbir_resize_extended_split(@NativeType("STBIR_RESIZE *") STBIR_RESIZE resize, int split_start, int split_count) {
        return nstbir_resize_extended_split(resize.address(), split_start, split_count);
    }

    private static final int[] stbir_pixel_layout_channels = {
        3, 1, 2, 3, 4,
        4, 4, 4, 4, 2, 2,
        4, 4, 4, 4, 2, 2,
    };

    private static int calculateBufferSize(int width, int height, int stride_in_bytes, int pixel_type, int type_size) {
        return height * (stride_in_bytes == 0 ? width * stbir_pixel_layout_channels[pixel_type] * type_size : stride_in_bytes);
    }

    private static final int[] stbir_type_size = {
        1, 1, 1, 2, 4, 2
    };


    /* 
    * ==============================
    * 旧API  来自 https://github.com/aaaapai/lwjgl3/blob/main/modules/lwjgl/stb/src/generated/java/org/lwjgl/stb/STBImageResize.java
    * ==============================
    */

    /** Set this flag if you have no alpha channel, or otherwise provide the index of the alpha channel. */
    public static final int STBIR_ALPHA_CHANNEL_NONE = -1;

    /**
     * Set this flag if your texture has premultiplied alpha. Otherwise, stbir will use alpha-weighted resampling (effectively premultiplying, resampling,
     * then unpremultiplying).
     */
    public static final int STBIR_FLAG_ALPHA_PREMULTIPLIED = 1 << 0;

    /** The specified alpha channel should be handled as gamma-corrected value even when doing sRGB operations. */
    public static final int STBIR_FLAG_ALPHA_USES_COLORSPACE = 1 << 1;

    /**
     * Colorspace.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #STBIR_COLORSPACE_LINEAR COLORSPACE_LINEAR}</li>
     * <li>{@link #STBIR_COLORSPACE_SRGB COLORSPACE_SRGB}</li>
     * </ul>
     */
    public static final int
        STBIR_COLORSPACE_LINEAR = 0,
        STBIR_COLORSPACE_SRGB   = 1;

    public static final int
        STBIR_TYPE_UINT32 = 2;

    
    // --- [ stbir_resize_uint8 ] ---

    /** Unsafe version of: {@link #stbir_resize_uint8 resize_uint8} */
    public static native int nstbir_resize_uint8(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels);

    @NativeType("int")
    public static boolean stbir_resize_uint8(@NativeType("unsigned char const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("unsigned char *") ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels) {
        return nstbir_resize_uint8(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddress(output_pixels), output_w, output_h, output_stride_in_bytes, num_channels) != 0;
    }

    // --- [ stbir_resize_float ] (old version) ---

    /** Unsafe version of: {@link #stbir_resize_float resize_float} */
    public static native int nstbir_resize_float(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels);

    /**
     * Float version of {@link #stbir_resize_uint8 resize_uint8}.
     *
     * @param input_pixels           the source image data
     * @param input_w                the source image width
     * @param input_h                the source image height
     * @param input_stride_in_bytes  the offset between successive rows of the source image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param output_pixels          returns the scaled image data
     * @param output_w               the resized image width
     * @param output_h               the resized image height
     * @param output_stride_in_bytes the offset between successive rows of the resized image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param num_channels           the number of channels in the image (e.g. RGB=3, RGBA=4)
     *
     * @return 1 on success, 0 on failure
     */
    @NativeType("int")
    public static boolean stbir_resize_float(@NativeType("float const *") FloatBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("float *") FloatBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels) {
        return nstbir_resize_float(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddress(output_pixels), output_w, output_h, output_stride_in_bytes, num_channels) != 0;
    }

    // --- [ stbir_resize_uint8_srgb_edgemode ] ---

    /** Unsafe version of: {@link #stbir_resize_uint8_srgb_edgemode resize_uint8_srgb_edgemode} */
    public static native int nstbir_resize_uint8_srgb_edgemode(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels, int alpha_channel, int flags, int edge_wrap_mode);

    /**
     * Same as {@link #stbir_resize_uint8_srgb resize_uint8_srgb}, but adds the ability to specify how requests to sample off the edge of the image are handled.
     *
     * @param input_pixels           the source image data
     * @param input_w                the source image width
     * @param input_h                the source image height
     * @param input_stride_in_bytes  the offset between successive rows of the source image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param output_pixels          returns the scaled image data
     * @param output_w               the resized image width
     * @param output_h               the resized image height
     * @param output_stride_in_bytes the offset between successive rows of the resized image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param num_channels           the number of channels in the image (e.g. RGB=3, RGBA=4)
     * @param alpha_channel          the alpha channel index, or {@link #STBIR_ALPHA_CHANNEL_NONE ALPHA_CHANNEL_NONE} if there is no alpha channel
     * @param flags                  the alpha channel flags. 0 will probably do the right thing if you're not sure what the flags mean. One of:<br><table><tr><td>{@link #STBIR_FLAG_ALPHA_PREMULTIPLIED FLAG_ALPHA_PREMULTIPLIED}</td><td>{@link #STBIR_FLAG_ALPHA_USES_COLORSPACE FLAG_ALPHA_USES_COLORSPACE}</td></tr></table>
     * @param edge_wrap_mode         the edge wrap mode. One of:<br><table><tr><td>{@link #STBIR_EDGE_CLAMP EDGE_CLAMP}</td><td>{@link #STBIR_EDGE_REFLECT EDGE_REFLECT}</td><td>{@link #STBIR_EDGE_WRAP EDGE_WRAP}</td><td>{@link #STBIR_EDGE_ZERO EDGE_ZERO}</td></tr></table>
     *
     * @return 1 on success, 0 on failure
     */
    @NativeType("int")
    public static boolean stbir_resize_uint8_srgb_edgemode(@NativeType("unsigned char const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("unsigned char *") ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels, int alpha_channel, int flags, @NativeType("stbir_edge") int edge_wrap_mode) {
        return nstbir_resize_uint8_srgb_edgemode(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddress(output_pixels), output_w, output_h, output_stride_in_bytes, num_channels, alpha_channel, flags, edge_wrap_mode) != 0;
    }

    // --- [ stbir_resize_uint8_generic ] ---

    /** Unsafe version of: {@link #stbir_resize_uint8_generic resize_uint8_generic} */
    public static native int nstbir_resize_uint8_generic(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels, int alpha_channel, int flags, int edge_wrap_mode, int filter, int space, long alloc_context);

    /**
     * Medium-complexity version of {@link #stbir_resize_uint8 resize_uint8}.
     *
     * @param input_pixels           the source image data
     * @param input_w                the source image width
     * @param input_h                the source image height
     * @param input_stride_in_bytes  the offset between successive rows of the source image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param output_pixels          returns the scaled image data
     * @param output_w               the resized image width
     * @param output_h               the resized image height
     * @param output_stride_in_bytes the offset between successive rows of the resized image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param num_channels           the number of channels in the image (e.g. RGB=3, RGBA=4)
     * @param alpha_channel          the alpha channel index, or {@link #STBIR_ALPHA_CHANNEL_NONE ALPHA_CHANNEL_NONE} if there is no alpha channel
     * @param flags                  the alpha channel flags. 0 will probably do the right thing if you're not sure what the flags mean. One of:<br><table><tr><td>{@link #STBIR_FLAG_ALPHA_PREMULTIPLIED FLAG_ALPHA_PREMULTIPLIED}</td><td>{@link #STBIR_FLAG_ALPHA_USES_COLORSPACE FLAG_ALPHA_USES_COLORSPACE}</td></tr></table>
     * @param edge_wrap_mode         the edge wrap mode. One of:<br><table><tr><td>{@link #STBIR_EDGE_CLAMP EDGE_CLAMP}</td><td>{@link #STBIR_EDGE_REFLECT EDGE_REFLECT}</td><td>{@link #STBIR_EDGE_WRAP EDGE_WRAP}</td><td>{@link #STBIR_EDGE_ZERO EDGE_ZERO}</td></tr></table>
     * @param filter                 the scale filter. One of:<br><table><tr><td>{@link #STBIR_FILTER_DEFAULT FILTER_DEFAULT}</td><td>{@link #STBIR_FILTER_BOX FILTER_BOX}</td><td>{@link #STBIR_FILTER_TRIANGLE FILTER_TRIANGLE}</td><td>{@link #STBIR_FILTER_CUBICBSPLINE FILTER_CUBICBSPLINE}</td><td>{@link #STBIR_FILTER_CATMULLROM FILTER_CATMULLROM}</td></tr><tr><td>{@link #STBIR_FILTER_MITCHELL FILTER_MITCHELL}</td></tr></table>
     * @param space                  the image colorspace. One of:<br><table><tr><td>{@link #STBIR_COLORSPACE_LINEAR COLORSPACE_LINEAR}</td><td>{@link #STBIR_COLORSPACE_SRGB COLORSPACE_SRGB}</td></tr></table>
     *
     * @return 1 on success, 0 on failure
     */
    @NativeType("int")
    public static boolean stbir_resize_uint8_generic(@NativeType("unsigned char const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("unsigned char *") ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels, int alpha_channel, int flags, @NativeType("stbir_edge") int edge_wrap_mode, @NativeType("stbir_filter") int filter, @NativeType("stbir_colorspace") int space) {
        return nstbir_resize_uint8_generic(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddress(output_pixels), output_w, output_h, output_stride_in_bytes, num_channels, alpha_channel, flags, edge_wrap_mode, filter, space, NULL) != 0;
    }

    // --- [ stbir_resize_uint16_generic ] ---

    /** Unsafe version of: {@link #stbir_resize_uint16_generic resize_uint16_generic} */
    public static native int nstbir_resize_uint16_generic(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels, int alpha_channel, int flags, int edge_wrap_mode, int filter, int space, long alloc_context);

    /**
     * Short version of {@link #stbir_resize_uint8_generic resize_uint8_generic}.
     *
     * @param input_pixels           the source image data
     * @param input_w                the source image width
     * @param input_h                the source image height
     * @param input_stride_in_bytes  the offset between successive rows of the source image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param output_pixels          returns the scaled image data
     * @param output_w               the resized image width
     * @param output_h               the resized image height
     * @param output_stride_in_bytes the offset between successive rows of the resized image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param num_channels           the number of channels in the image (e.g. RGB=3, RGBA=4)
     * @param alpha_channel          the alpha channel index, or {@link #STBIR_ALPHA_CHANNEL_NONE ALPHA_CHANNEL_NONE} if there is no alpha channel
     * @param flags                  the alpha channel flags. 0 will probably do the right thing if you're not sure what the flags mean. One of:<br><table><tr><td>{@link #STBIR_FLAG_ALPHA_PREMULTIPLIED FLAG_ALPHA_PREMULTIPLIED}</td><td>{@link #STBIR_FLAG_ALPHA_USES_COLORSPACE FLAG_ALPHA_USES_COLORSPACE}</td></tr></table>
     * @param edge_wrap_mode         the edge wrap mode. One of:<br><table><tr><td>{@link #STBIR_EDGE_CLAMP EDGE_CLAMP}</td><td>{@link #STBIR_EDGE_REFLECT EDGE_REFLECT}</td><td>{@link #STBIR_EDGE_WRAP EDGE_WRAP}</td><td>{@link #STBIR_EDGE_ZERO EDGE_ZERO}</td></tr></table>
     * @param filter                 the scale filter. One of:<br><table><tr><td>{@link #STBIR_FILTER_DEFAULT FILTER_DEFAULT}</td><td>{@link #STBIR_FILTER_BOX FILTER_BOX}</td><td>{@link #STBIR_FILTER_TRIANGLE FILTER_TRIANGLE}</td><td>{@link #STBIR_FILTER_CUBICBSPLINE FILTER_CUBICBSPLINE}</td><td>{@link #STBIR_FILTER_CATMULLROM FILTER_CATMULLROM}</td></tr><tr><td>{@link #STBIR_FILTER_MITCHELL FILTER_MITCHELL}</td></tr></table>
     * @param space                  the image colorspace. One of:<br><table><tr><td>{@link #STBIR_COLORSPACE_LINEAR COLORSPACE_LINEAR}</td><td>{@link #STBIR_COLORSPACE_SRGB COLORSPACE_SRGB}</td></tr></table>
     *
     * @return 1 on success, 0 on failure
     */
    @NativeType("int")
    public static boolean stbir_resize_uint16_generic(@NativeType("stbir_uint16 const *") ShortBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("stbir_uint16 *") ShortBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels, int alpha_channel, int flags, @NativeType("stbir_edge") int edge_wrap_mode, @NativeType("stbir_filter") int filter, @NativeType("stbir_colorspace") int space) {
        return nstbir_resize_uint16_generic(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddress(output_pixels), output_w, output_h, output_stride_in_bytes, num_channels, alpha_channel, flags, edge_wrap_mode, filter, space, NULL) != 0;
    }

    // --- [ stbir_resize_float_generic ] ---

    /** Unsafe version of: {@link #stbir_resize_float_generic resize_float_generic} */
    public static native int nstbir_resize_float_generic(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels, int alpha_channel, int flags, int edge_wrap_mode, int filter, int space, long alloc_context);

    /**
     * Float version of {@link #stbir_resize_uint8_generic resize_uint8_generic}.
     *
     * @param input_pixels           the source image data
     * @param input_w                the source image width
     * @param input_h                the source image height
     * @param input_stride_in_bytes  the offset between successive rows of the source image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param output_pixels          returns the scaled image data
     * @param output_w               the resized image width
     * @param output_h               the resized image height
     * @param output_stride_in_bytes the offset between successive rows of the resized image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param num_channels           the number of channels in the image (e.g. RGB=3, RGBA=4)
     * @param alpha_channel          the alpha channel index, or {@link #STBIR_ALPHA_CHANNEL_NONE ALPHA_CHANNEL_NONE} if there is no alpha channel
     * @param flags                  the alpha channel flags. 0 will probably do the right thing if you're not sure what the flags mean. One of:<br><table><tr><td>{@link #STBIR_FLAG_ALPHA_PREMULTIPLIED FLAG_ALPHA_PREMULTIPLIED}</td><td>{@link #STBIR_FLAG_ALPHA_USES_COLORSPACE FLAG_ALPHA_USES_COLORSPACE}</td></tr></table>
     * @param edge_wrap_mode         the edge wrap mode. One of:<br><table><tr><td>{@link #STBIR_EDGE_CLAMP EDGE_CLAMP}</td><td>{@link #STBIR_EDGE_REFLECT EDGE_REFLECT}</td><td>{@link #STBIR_EDGE_WRAP EDGE_WRAP}</td><td>{@link #STBIR_EDGE_ZERO EDGE_ZERO}</td></tr></table>
     * @param filter                 the scale filter. One of:<br><table><tr><td>{@link #STBIR_FILTER_DEFAULT FILTER_DEFAULT}</td><td>{@link #STBIR_FILTER_BOX FILTER_BOX}</td><td>{@link #STBIR_FILTER_TRIANGLE FILTER_TRIANGLE}</td><td>{@link #STBIR_FILTER_CUBICBSPLINE FILTER_CUBICBSPLINE}</td><td>{@link #STBIR_FILTER_CATMULLROM FILTER_CATMULLROM}</td></tr><tr><td>{@link #STBIR_FILTER_MITCHELL FILTER_MITCHELL}</td></tr></table>
     * @param space                  the image colorspace. One of:<br><table><tr><td>{@link #STBIR_COLORSPACE_LINEAR COLORSPACE_LINEAR}</td><td>{@link #STBIR_COLORSPACE_SRGB COLORSPACE_SRGB}</td></tr></table>
     *
     * @return 1 on success, 0 on failure
     */
    @NativeType("int")
    public static boolean stbir_resize_float_generic(@NativeType("float const *") FloatBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("float *") FloatBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels, int alpha_channel, int flags, @NativeType("stbir_edge") int edge_wrap_mode, @NativeType("stbir_filter") int filter, @NativeType("stbir_colorspace") int space) {
        return nstbir_resize_float_generic(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddress(output_pixels), output_w, output_h, output_stride_in_bytes, num_channels, alpha_channel, flags, edge_wrap_mode, filter, space, NULL) != 0;
    }

    // --- [ stbir_resize ] (旧版本复杂参数) ---

    /** Unsafe version of: {@link #stbir_resize resize} */
    public static native int nstbir_resize(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int datatype, int num_channels, int alpha_channel, int flags, int edge_mode_horizontal, int edge_mode_vertical, int filter_horizontal, int filter_vertical, int space, long alloc_context);

    /**
     * Full-complexity version of {@link #stbir_resize_uint8_generic resize_uint8_generic}.
     *
     * @param input_pixels           the source image data
     * @param input_w                the source image width
     * @param input_h                the source image height
     * @param input_stride_in_bytes  the offset between successive rows of the source image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param output_pixels          returns the scaled image data
     * @param output_w               the resized image width
     * @param output_h               the resized image height
     * @param output_stride_in_bytes the offset between successive rows of the resized image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param datatype               the image data type. One of:<br><table><tr><td>{@link #STBIR_TYPE_UINT8 TYPE_UINT8}</td><td>{@link #STBIR_TYPE_UINT16 TYPE_UINT16}</td><td>{@link #STBIR_TYPE_UINT32 TYPE_UINT32}</td><td>{@link #STBIR_TYPE_FLOAT TYPE_FLOAT}</td></tr></table>
     * @param num_channels           the number of channels in the image (e.g. RGB=3, RGBA=4)
     * @param alpha_channel          the alpha channel index, or {@link #STBIR_ALPHA_CHANNEL_NONE ALPHA_CHANNEL_NONE} if there is no alpha channel
     * @param flags                  the alpha channel flags. 0 will probably do the right thing if you're not sure what the flags mean. One of:<br><table><tr><td>{@link #STBIR_FLAG_ALPHA_PREMULTIPLIED FLAG_ALPHA_PREMULTIPLIED}</td><td>{@link #STBIR_FLAG_ALPHA_USES_COLORSPACE FLAG_ALPHA_USES_COLORSPACE}</td></tr></table>
     * @param edge_mode_horizontal   the horizontal edge wrap mode
     * @param edge_mode_vertical     the vertical edge wrap mode
     * @param filter_horizontal      the horizontal scale filter
     * @param filter_vertical        the vertical scale filter
     * @param space                  the image colorspace. One of:<br><table><tr><td>{@link #STBIR_COLORSPACE_LINEAR COLORSPACE_LINEAR}</td><td>{@link #STBIR_COLORSPACE_SRGB COLORSPACE_SRGB}</td></tr></table>
     *
     * @return 1 on success, 0 on failure
     */
    @NativeType("int")
    public static boolean stbir_resize(@NativeType("void const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("void *") ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, @NativeType("stbir_datatype") int datatype, int num_channels, int alpha_channel, int flags, @NativeType("stbir_edge") int edge_mode_horizontal, @NativeType("stbir_edge") int edge_mode_vertical, @NativeType("stbir_filter") int filter_horizontal, @NativeType("stbir_filter") int filter_vertical, @NativeType("stbir_colorspace") int space) {
        return nstbir_resize(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddress(output_pixels), output_w, output_h, output_stride_in_bytes, datatype, num_channels, alpha_channel, flags, edge_mode_horizontal, edge_mode_vertical, filter_horizontal, filter_vertical, space, NULL) != 0;
    }

    // --- [ stbir_resize_subpixel ] ---

    /** Unsafe version of: {@link #stbir_resize_subpixel resize_subpixel} */
    public static native int nstbir_resize_subpixel(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int datatype, int num_channels, int alpha_channel, int flags, int edge_mode_horizontal, int edge_mode_vertical, int filter_horizontal, int filter_vertical, int space, long alloc_context, float x_scale, float y_scale, float x_offset, float y_offset);

    /**
     * Subpixel version of {@link #stbir_resize resize}.
     *
     * @param input_pixels           the source image data
     * @param input_w                the source image width
     * @param input_h                the source image height
     * @param input_stride_in_bytes  the offset between successive rows of the source image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param output_pixels          returns the scaled image data
     * @param output_w               the resized image width
     * @param output_h               the resized image height
     * @param output_stride_in_bytes the offset between successive rows of the resized image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param datatype               the image data type. One of:<br><table><tr><td>{@link #STBIR_TYPE_UINT8 TYPE_UINT8}</td><td>{@link #STBIR_TYPE_UINT16 TYPE_UINT16}</td><td>{@link #STBIR_TYPE_UINT32 TYPE_UINT32}</td><td>{@link #STBIR_TYPE_FLOAT TYPE_FLOAT}</td></tr></table>
     * @param num_channels           the number of channels in the image (e.g. RGB=3, RGBA=4)
     * @param alpha_channel          the alpha channel index, or {@link #STBIR_ALPHA_CHANNEL_NONE ALPHA_CHANNEL_NONE} if there is no alpha channel
     * @param flags                  the alpha channel flags. 0 will probably do the right thing if you're not sure what the flags mean. One of:<br><table><tr><td>{@link #STBIR_FLAG_ALPHA_PREMULTIPLIED FLAG_ALPHA_PREMULTIPLIED}</td><td>{@link #STBIR_FLAG_ALPHA_USES_COLORSPACE FLAG_ALPHA_USES_COLORSPACE}</td></tr></table>
     * @param edge_mode_horizontal   the horizontal edge wrap mode
     * @param edge_mode_vertical     the vertical edge wrap mode
     * @param filter_horizontal      the horizontal scale filter
     * @param filter_vertical        the vertical scale filter
     * @param space                  the image colorspace. One of:<br><table><tr><td>{@link #STBIR_COLORSPACE_LINEAR COLORSPACE_LINEAR}</td><td>{@link #STBIR_COLORSPACE_SRGB COLORSPACE_SRGB}</td></tr></table>
     * @param x_scale                horizontal scale for subpixel correctness
     * @param y_scale                vertical scale for subpixel correctness
     * @param x_offset               horizontal offset for subpixel correctness
     * @param y_offset               vertical offset for subpixel correctness
     *
     * @return 1 on success, 0 on failure
     */
    @NativeType("int")
    public static boolean stbir_resize_subpixel(@NativeType("void const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("void *") ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, @NativeType("stbir_datatype") int datatype, int num_channels, int alpha_channel, int flags, @NativeType("stbir_edge") int edge_mode_horizontal, @NativeType("stbir_edge") int edge_mode_vertical, @NativeType("stbir_filter") int filter_horizontal, @NativeType("stbir_filter") int filter_vertical, @NativeType("stbir_colorspace") int space, float x_scale, float y_scale, float x_offset, float y_offset) {
        return nstbir_resize_subpixel(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddress(output_pixels), output_w, output_h, output_stride_in_bytes, datatype, num_channels, alpha_channel, flags, edge_mode_horizontal, edge_mode_vertical, filter_horizontal, filter_vertical, space, NULL, x_scale, y_scale, x_offset, y_offset) != 0;
    }

    // --- [ stbir_resize_region ] ---

    /** Unsafe version of: {@link #stbir_resize_region resize_region} */
    public static native int nstbir_resize_region(long input_pixels, int input_w, int input_h, int input_stride_in_bytes, long output_pixels, int output_w, int output_h, int output_stride_in_bytes, int datatype, int num_channels, int alpha_channel, int flags, int edge_mode_horizontal, int edge_mode_vertical, int filter_horizontal, int filter_vertical, int space, long alloc_context, float s0, float t0, float s1, float t1);

    /**
     * Region version of {@link #stbir_resize resize}, using texture coordinates.
     *
     * @param input_pixels           the source image data
     * @param input_w                the source image width
     * @param input_h                the source image height
     * @param input_stride_in_bytes  the offset between successive rows of the source image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param output_pixels          returns the scaled image data
     * @param output_w               the resized image width
     * @param output_h               the resized image height
     * @param output_stride_in_bytes the offset between successive rows of the resized image data in memory, in bytes. You can specify 0 to mean packed continuously in memory
     * @param datatype               the image data type. One of:<br><table><tr><td>{@link #STBIR_TYPE_UINT8 TYPE_UINT8}</td><td>{@link #STBIR_TYPE_UINT16 TYPE_UINT16}</td><td>{@link #STBIR_TYPE_UINT32 TYPE_UINT32}</td><td>{@link #STBIR_TYPE_FLOAT TYPE_FLOAT}</td></tr></table>
     * @param num_channels           the number of channels in the image (e.g. RGB=3, RGBA=4)
     * @param alpha_channel          the alpha channel index, or {@link #STBIR_ALPHA_CHANNEL_NONE ALPHA_CHANNEL_NONE} if there is no alpha channel
     * @param flags                  the alpha channel flags. 0 will probably do the right thing if you're not sure what the flags mean. One of:<br><table><tr><td>{@link #STBIR_FLAG_ALPHA_PREMULTIPLIED FLAG_ALPHA_PREMULTIPLIED}</td><td>{@link #STBIR_FLAG_ALPHA_USES_COLORSPACE FLAG_ALPHA_USES_COLORSPACE}</td></tr></table>
     * @param edge_mode_horizontal   the horizontal edge wrap mode
     * @param edge_mode_vertical     the vertical edge wrap mode
     * @param filter_horizontal      the horizontal scale filter
     * @param filter_vertical        the vertical scale filter
     * @param space                  the image colorspace. One of:<br><table><tr><td>{@link #STBIR_COLORSPACE_LINEAR COLORSPACE_LINEAR}</td><td>{@link #STBIR_COLORSPACE_SRGB COLORSPACE_SRGB}</td></tr></table>
     * @param s0                     the left texture coordinate of the region to scale
     * @param t0                     the top texture coordinate of the region to scale
     * @param s1                     the right texture coordinate of the region to scale
     * @param t1                     the bottom texture coordinate of the region to scale
     *
     * @return 1 on success, 0 on failure
     */
    @NativeType("int")
    public static boolean stbir_resize_region(@NativeType("void const *") ByteBuffer input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("void *") ByteBuffer output_pixels, int output_w, int output_h, int output_stride_in_bytes, @NativeType("stbir_datatype") int datatype, int num_channels, int alpha_channel, int flags, @NativeType("stbir_edge") int edge_mode_horizontal, @NativeType("stbir_edge") int edge_mode_vertical, @NativeType("stbir_filter") int filter_horizontal, @NativeType("stbir_filter") int filter_vertical, @NativeType("stbir_colorspace") int space, float s0, float t0, float s1, float t1) {
        return nstbir_resize_region(memAddress(input_pixels), input_w, input_h, input_stride_in_bytes, memAddress(output_pixels), output_w, output_h, output_stride_in_bytes, datatype, num_channels, alpha_channel, flags, edge_mode_horizontal, edge_mode_vertical, filter_horizontal, filter_vertical, space, NULL, s0, t0, s1, t1) != 0;
    }

    // --- [ 数组版本函数 ] ---

    /** Array version of: {@link #nstbir_resize_float} */
    public static native int nstbir_resize_float(float[] input_pixels, int input_w, int input_h, int input_stride_in_bytes, float[] output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels);

    /** Array version of: {@link #stbir_resize_float resize_float} */
    @NativeType("int")
    public static boolean stbir_resize_float(@NativeType("float const *") float[] input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("float *") float[] output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels) {
        return nstbir_resize_float(input_pixels, input_w, input_h, input_stride_in_bytes, output_pixels, output_w, output_h, output_stride_in_bytes, num_channels) != 0;
    }

    /** Array version of: {@link #nstbir_resize_uint16_generic} */
    public static native int nstbir_resize_uint16_generic(short[] input_pixels, int input_w, int input_h, int input_stride_in_bytes, short[] output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels, int alpha_channel, int flags, int edge_wrap_mode, int filter, int space, long alloc_context);

    /** Array version of: {@link #stbir_resize_uint16_generic resize_uint16_generic} */
    @NativeType("int")
    public static boolean stbir_resize_uint16_generic(@NativeType("stbir_uint16 const *") short[] input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("stbir_uint16 *") short[] output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels, int alpha_channel, int flags, @NativeType("stbir_edge") int edge_wrap_mode, @NativeType("stbir_filter") int filter, @NativeType("stbir_colorspace") int space) {
        return nstbir_resize_uint16_generic(input_pixels, input_w, input_h, input_stride_in_bytes, output_pixels, output_w, output_h, output_stride_in_bytes, num_channels, alpha_channel, flags, edge_wrap_mode, filter, space, NULL) != 0;
    }

    /** Array version of: {@link #nstbir_resize_float_generic} */
    public static native int nstbir_resize_float_generic(float[] input_pixels, int input_w, int input_h, int input_stride_in_bytes, float[] output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels, int alpha_channel, int flags, int edge_wrap_mode, int filter, int space, long alloc_context);

    /** Array version of: {@link #stbir_resize_float_generic resize_float_generic} */
    @NativeType("int")
    public static boolean stbir_resize_float_generic(@NativeType("float const *") float[] input_pixels, int input_w, int input_h, int input_stride_in_bytes, @NativeType("float *") float[] output_pixels, int output_w, int output_h, int output_stride_in_bytes, int num_channels, int alpha_channel, int flags, @NativeType("stbir_edge") int edge_wrap_mode, @NativeType("stbir_filter") int filter, @NativeType("stbir_colorspace") int space) {
        return nstbir_resize_float_generic(input_pixels, input_w, input_h, input_stride_in_bytes, output_pixels, output_w, output_h, output_stride_in_bytes, num_channels, alpha_channel, flags, edge_wrap_mode, filter, space, NULL) != 0;
    }

}