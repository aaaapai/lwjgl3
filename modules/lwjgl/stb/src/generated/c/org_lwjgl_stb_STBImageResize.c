/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
DISABLE_WARNINGS()
#include "lwjgl_malloc.h"
#define STBIR_MALLOC(size,user_data) org_lwjgl_malloc(size)
#define STBIR_FREE(ptr,user_data)    org_lwjgl_free(ptr)
#define STBIR_ASSERT(x)
#define STB_IMAGE_RESIZE_IMPLEMENTATION
#define STB_IMAGE_RESIZE_STATIC
#include "stb_image_resize2.h"
ENABLE_WARNINGS()

EXTERN_C_ENTER

JNIEXPORT jlong JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1uint8_1srgb(JNIEnv *__env, jclass clazz, jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes, jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes, jint pixel_type) {
    unsigned char const *input_pixels = (unsigned char const *)(uintptr_t)input_pixelsAddress;
    unsigned char *output_pixels = (unsigned char *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jlong)(uintptr_t)stbir_resize_uint8_srgb(input_pixels, input_w, input_h, input_stride_in_bytes, output_pixels, output_w, output_h, output_stride_in_bytes, (stbir_pixel_layout)pixel_type);
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1uint8_1linear(JNIEnv *__env, jclass clazz, jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes, jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes, jint pixel_type) {
    unsigned char const *input_pixels = (unsigned char const *)(uintptr_t)input_pixelsAddress;
    unsigned char *output_pixels = (unsigned char *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jlong)(uintptr_t)stbir_resize_uint8_linear(input_pixels, input_w, input_h, input_stride_in_bytes, output_pixels, output_w, output_h, output_stride_in_bytes, (stbir_pixel_layout)pixel_type);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1uint8(JNIEnv *__env, jclass clazz, 
    jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes,
    jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes, 
    jint num_channels) {
    
    unsigned char const *input_pixels = (unsigned char const *)(uintptr_t)input_pixelsAddress;
    unsigned char *output_pixels = (unsigned char *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    
    // 通道数到像素布局的映射
    stbir_pixel_layout pixel_type;
    switch (num_channels) {
        case 1:
            pixel_type = STBIR_1CHANNEL;
            break;
        case 2:
            pixel_type = STBIR_2CHANNEL;
            break;
        case 3:
            pixel_type = STBIR_RGB;
            break;
        case 4:
            pixel_type = STBIR_RGBA;
            break;
        default:
            pixel_type = STBIR_4CHANNEL;
            break;
    }
    
    // 使用线性色彩空间进行缩放
    unsigned char *result = stbir_resize_uint8_linear(
        input_pixels, input_w, input_h, input_stride_in_bytes,
        output_pixels, output_w, output_h, output_stride_in_bytes,
        pixel_type
    );
    
    return (jint)(result != NULL);
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1float_1linear(JNIEnv *__env, jclass clazz, jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes, jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes, jint pixel_type) {
    float const *input_pixels = (float const *)(uintptr_t)input_pixelsAddress;
    float *output_pixels = (float *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jlong)(uintptr_t)stbir_resize_float_linear(input_pixels, input_w, input_h, input_stride_in_bytes, output_pixels, output_w, output_h, output_stride_in_bytes, (stbir_pixel_layout)pixel_type);
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize(JNIEnv *__env, jclass clazz, jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes, jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes, jint pixel_layout, jint data_type, jint edge, jint filter) {
    void const *input_pixels = (void const *)(uintptr_t)input_pixelsAddress;
    void *output_pixels = (void *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jlong)(uintptr_t)stbir_resize(input_pixels, input_w, input_h, input_stride_in_bytes, output_pixels, output_w, output_h, output_stride_in_bytes, (stbir_pixel_layout)pixel_layout, (stbir_datatype)data_type, (stbir_edge)edge, (stbir_filter)filter);
}

JNIEXPORT void JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1init(JNIEnv *__env, jclass clazz, jlong resizeAddress, jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes, jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes, jint pixel_layout, jint data_type) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    void const *input_pixels = (void const *)(uintptr_t)input_pixelsAddress;
    void *output_pixels = (void *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    stbir_resize_init(resize, input_pixels, input_w, input_h, input_stride_in_bytes, output_pixels, output_w, output_h, output_stride_in_bytes, (stbir_pixel_layout)pixel_layout, (stbir_datatype)data_type);
}

JNIEXPORT void JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1set_1datatypes(JNIEnv *__env, jclass clazz, jlong resizeAddress, jint input_type, jint output_type) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    stbir_set_datatypes(resize, (stbir_datatype)input_type, (stbir_datatype)output_type);
}

JNIEXPORT void JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1set_1pixel_1callbacks(JNIEnv *__env, jclass clazz, jlong resizeAddress, jlong input_cbAddress, jlong output_cbAddress) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    stbir_input_callback *input_cb = (stbir_input_callback *)(uintptr_t)input_cbAddress;
    stbir_output_callback *output_cb = (stbir_output_callback *)(uintptr_t)output_cbAddress;
    UNUSED_PARAMS(__env, clazz)
    stbir_set_pixel_callbacks(resize, input_cb, output_cb);
}

JNIEXPORT void JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1set_1user_1data(JNIEnv *__env, jclass clazz, jlong resizeAddress, jlong user_dataAddress) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    void *user_data = (void *)(uintptr_t)user_dataAddress;
    UNUSED_PARAMS(__env, clazz)
    stbir_set_user_data(resize, user_data);
}

JNIEXPORT void JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1set_1buffer_1ptrs(JNIEnv *__env, jclass clazz, jlong resizeAddress, jlong input_pixelsAddress, jint input_stride_in_bytes, jlong output_pixelsAddress, jint output_stride_in_bytes) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    void const *input_pixels = (void const *)(uintptr_t)input_pixelsAddress;
    void *output_pixels = (void *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    stbir_set_buffer_ptrs(resize, input_pixels, input_stride_in_bytes, output_pixels, output_stride_in_bytes);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1set_1pixel_1layouts(JNIEnv *__env, jclass clazz, jlong resizeAddress, jint input_pixel_layout, jint output_pixel_layout) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)stbir_set_pixel_layouts(resize, (stbir_pixel_layout)input_pixel_layout, (stbir_pixel_layout)output_pixel_layout);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1set_1edgemodes(JNIEnv *__env, jclass clazz, jlong resizeAddress, jint horizontal_edge, jint vertical_edge) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)stbir_set_edgemodes(resize, (stbir_edge)horizontal_edge, (stbir_edge)vertical_edge);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1set_1filters(JNIEnv *__env, jclass clazz, jlong resizeAddress, jint horizontal_filter, jint vertical_filter) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)stbir_set_filters(resize, (stbir_filter)horizontal_filter, (stbir_filter)vertical_filter);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1set_1filter_1callbacks(JNIEnv *__env, jclass clazz, jlong resizeAddress, jlong horizontal_filterAddress, jlong horizontal_supportAddress, jlong vertical_filterAddress, jlong vertical_supportAddress) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    stbir__kernel_callback *horizontal_filter = (stbir__kernel_callback *)(uintptr_t)horizontal_filterAddress;
    stbir__support_callback *horizontal_support = (stbir__support_callback *)(uintptr_t)horizontal_supportAddress;
    stbir__kernel_callback *vertical_filter = (stbir__kernel_callback *)(uintptr_t)vertical_filterAddress;
    stbir__support_callback *vertical_support = (stbir__support_callback *)(uintptr_t)vertical_supportAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)stbir_set_filter_callbacks(resize, horizontal_filter, horizontal_support, vertical_filter, vertical_support);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1set_1pixel_1subrect(JNIEnv *__env, jclass clazz, jlong resizeAddress, jint subx, jint suby, jint subw, jint subh) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)stbir_set_pixel_subrect(resize, subx, suby, subw, subh);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1set_1input_1subrect(JNIEnv *__env, jclass clazz, jlong resizeAddress, jdouble s0, jdouble t0, jdouble s1, jdouble t1) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)stbir_set_input_subrect(resize, s0, t0, s1, t1);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1set_1output_1pixel_1subrect(JNIEnv *__env, jclass clazz, jlong resizeAddress, jint subx, jint suby, jint subw, jint subh) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)stbir_set_output_pixel_subrect(resize, subx, suby, subw, subh);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1set_1non_1pm_1alpha_1speed_1over_1quality(JNIEnv *__env, jclass clazz, jlong resizeAddress, jint non_pma_alpha_speed_over_quality) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)stbir_set_non_pm_alpha_speed_over_quality(resize, non_pma_alpha_speed_over_quality);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1build_1samplers(JNIEnv *__env, jclass clazz, jlong resizeAddress) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)stbir_build_samplers(resize);
}

JNIEXPORT void JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1free_1samplers(JNIEnv *__env, jclass clazz, jlong resizeAddress) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    stbir_free_samplers(resize);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1extended(JNIEnv *__env, jclass clazz, jlong resizeAddress) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)stbir_resize_extended(resize);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1build_1samplers_1with_1splits(JNIEnv *__env, jclass clazz, jlong resizeAddress, jint try_splits) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)stbir_build_samplers_with_splits(resize, try_splits);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1extended_1split(JNIEnv *__env, jclass clazz, jlong resizeAddress, jint split_start, jint split_count) {
    STBIR_RESIZE *resize = (STBIR_RESIZE *)(uintptr_t)resizeAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)stbir_resize_extended_split(resize, split_start, split_count);
}

// ==============================
// 兼容层函数实现（保持旧API）
// ==============================

// 1. stbir_resize_float (旧版本)
JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1float__JIIIJIIII(
    JNIEnv *__env, jclass clazz,
    jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes,
    jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes,
    jint num_channels) {
    
    float const *input_pixels = (float const *)(uintptr_t)input_pixelsAddress;
    float *output_pixels = (float *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    
    // 将通道数转换为像素布局
    stbir_pixel_layout pixel_type;
    switch (num_channels) {
        case 1: pixel_type = STBIR_1CHANNEL; break;
        case 2: pixel_type = STBIR_2CHANNEL; break;
        case 3: pixel_type = STBIR_RGB; break;
        case 4: pixel_type = STBIR_RGBA; break;
        default: pixel_type = STBIR_4CHANNEL; break;
    }
    
    // 调用新的float_linear函数
    float *result = stbir_resize_float_linear(
        input_pixels, input_w, input_h, input_stride_in_bytes,
        output_pixels, output_w, output_h, output_stride_in_bytes,
        pixel_type
    );
    
    return (jint)(result != NULL);
}

// 2. stbir_resize_uint8_srgb_edgemode
JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1uint8_1srgb_1edgemode(
    JNIEnv *__env, jclass clazz,
    jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes,
    jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes,
    jint num_channels, jint alpha_channel, jint flags, jint edge_wrap_mode) {
    
    unsigned char const *input_pixels = (unsigned char const *)(uintptr_t)input_pixelsAddress;
    unsigned char *output_pixels = (unsigned char *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    
    // 将通道数转换为像素布局
    stbir_pixel_layout pixel_type;
    switch (num_channels) {
        case 1: pixel_type = STBIR_1CHANNEL; break;
        case 2: pixel_type = STBIR_2CHANNEL; break;
        case 3: pixel_type = STBIR_RGB; break;
        case 4: pixel_type = STBIR_RGBA; break;
        default: pixel_type = STBIR_4CHANNEL; break;
    }
    
    // 注意：新版本可能不支持edge_wrap_mode参数，这里忽略
    unsigned char *result = stbir_resize_uint8_srgb(
        input_pixels, input_w, input_h, input_stride_in_bytes,
        output_pixels, output_w, output_h, output_stride_in_bytes,
        pixel_type
    );
    
    return (jint)(result != NULL);
}

// 3. stbir_resize_uint8_generic
JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1uint8_1generic(
    JNIEnv *__env, jclass clazz,
    jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes,
    jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes,
    jint num_channels, jint alpha_channel, jint flags, jint edge_wrap_mode,
    jint filter, jint space, jlong alloc_contextAddress) {
    
    unsigned char const *input_pixels = (unsigned char const *)(uintptr_t)input_pixelsAddress;
    unsigned char *output_pixels = (unsigned char *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    
    // 将通道数转换为像素布局
    stbir_pixel_layout pixel_type;
    switch (num_channels) {
        case 1: pixel_type = STBIR_1CHANNEL; break;
        case 2: pixel_type = STBIR_2CHANNEL; break;
        case 3: pixel_type = STBIR_RGB; break;
        case 4: pixel_type = STBIR_RGBA; break;
        default: pixel_type = STBIR_4CHANNEL; break;
    }
    
    // 根据色彩空间选择函数
    unsigned char *result;
    if (space == 1) { // STBIR_COLORSPACE_SRGB
        result = stbir_resize_uint8_srgb(
            input_pixels, input_w, input_h, input_stride_in_bytes,
            output_pixels, output_w, output_h, output_stride_in_bytes,
            pixel_type
        );
    } else { // STBIR_COLORSPACE_LINEAR
        result = stbir_resize_uint8_linear(
            input_pixels, input_w, input_h, input_stride_in_bytes,
            output_pixels, output_w, output_h, output_stride_in_bytes,
            pixel_type
        );
    }
    
    return (jint)(result != NULL);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1uint16_1generic__JIIIJIIIIIIIIIJ(
    JNIEnv *__env, jclass clazz,
    jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes,
    jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes,
    jint num_channels, jint alpha_channel, jint flags, jint edge_wrap_mode,
    jint filter, jint space, jlong alloc_contextAddress) {
    
    stbir_uint16 const *input_pixels = (stbir_uint16 const *)(uintptr_t)input_pixelsAddress;
    stbir_uint16 *output_pixels = (stbir_uint16 *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    
    stbir_pixel_layout pixel_type;
    switch (num_channels) {
        case 1: pixel_type = STBIR_1CHANNEL; break;
        case 2: pixel_type = STBIR_2CHANNEL; break;
        case 3: pixel_type = STBIR_RGB; break;
        case 4: pixel_type = STBIR_RGBA; break;
        default: pixel_type = STBIR_4CHANNEL; break;
    }
    
    void *result = stbir_resize(
        input_pixels, input_w, input_h, input_stride_in_bytes,
        output_pixels, output_w, output_h, output_stride_in_bytes,
        pixel_type, STBIR_TYPE_UINT16, STBIR_EDGE_CLAMP, STBIR_FILTER_DEFAULT
    );
    
    return (jint)(result != NULL);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1float_1generic__JIIIJIIIIIIIIIJ(
    JNIEnv *__env, jclass clazz,
    jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes,
    jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes,
    jint num_channels, jint alpha_channel, jint flags, jint edge_wrap_mode,
    jint filter, jint space, jlong alloc_contextAddress) {
    
    float const *input_pixels = (float const *)(uintptr_t)input_pixelsAddress;
    float *output_pixels = (float *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    
    stbir_pixel_layout pixel_type;
    switch (num_channels) {
        case 1: pixel_type = STBIR_1CHANNEL; break;
        case 2: pixel_type = STBIR_2CHANNEL; break;
        case 3: pixel_type = STBIR_RGB; break;
        case 4: pixel_type = STBIR_RGBA; break;
        default: pixel_type = STBIR_4CHANNEL; break;
    }
    
    void *result = stbir_resize(
        input_pixels, input_w, input_h, input_stride_in_bytes,
        output_pixels, output_w, output_h, output_stride_in_bytes,
        pixel_type, STBIR_TYPE_FLOAT, STBIR_EDGE_CLAMP, STBIR_FILTER_DEFAULT
    );
    
    return (jint)(result != NULL);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize__JIIIJIIIIIIIIIJ(
    JNIEnv *__env, jclass clazz,
    jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes,
    jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes,
    jint datatype, jint num_channels, jint alpha_channel, jint flags,
    jint edge_mode_horizontal, jint edge_mode_vertical,
    jint filter_horizontal, jint filter_vertical, jint space, jlong alloc_contextAddress) {
    
    void const *input_pixels = (void const *)(uintptr_t)input_pixelsAddress;
    void *output_pixels = (void *)(uintptr_t)output_pixelsAddress;
    UNUSED_PARAMS(__env, clazz)
    
    stbir_pixel_layout pixel_type;
    switch (num_channels) {
        case 1: pixel_type = STBIR_1CHANNEL; break;
        case 2: pixel_type = STBIR_2CHANNEL; break;
        case 3: pixel_type = STBIR_RGB; break;
        case 4: pixel_type = STBIR_RGBA; break;
        default: pixel_type = STBIR_4CHANNEL; break;
    }
    
    stbir_datatype data_type;
    switch (datatype) {
        case 0: data_type = STBIR_TYPE_UINT8; break;
        case 1: data_type = STBIR_TYPE_UINT16; break;
        case 2: data_type = /*STBIR_TYPE_UINT32*/ STBIR_TYPE_UINT16; break;
        case 3: data_type = STBIR_TYPE_FLOAT; break;
        default: data_type = STBIR_TYPE_UINT8; break;
    }
    
    void *result = stbir_resize(
        input_pixels, input_w, input_h, input_stride_in_bytes,
        output_pixels, output_w, output_h, output_stride_in_bytes,
        pixel_type, data_type, edge_mode_horizontal, filter_horizontal
    );
    
    return (jint)(result != NULL);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1subpixel(
    JNIEnv *__env, jclass clazz,
    jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes,
    jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes,
    jint datatype, jint num_channels, jint alpha_channel, jint flags,
    jint edge_mode_horizontal, jint edge_mode_vertical,
    jint filter_horizontal, jint filter_vertical, jint space,
    jlong alloc_contextAddress, jfloat x_scale, jfloat y_scale,
    jfloat x_offset, jfloat y_offset) {
    
    return Java_org_lwjgl_stb_STBImageResize_nstbir_1resize__JIIIJIIIIIIIIIJ(
        __env, clazz,
        input_pixelsAddress, input_w, input_h, input_stride_in_bytes,
        output_pixelsAddress, output_w, output_h, output_stride_in_bytes,
        datatype, num_channels, alpha_channel, flags,
        edge_mode_horizontal, edge_mode_vertical,
        filter_horizontal, filter_vertical, space, alloc_contextAddress
    );
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1region(
    JNIEnv *__env, jclass clazz,
    jlong input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes,
    jlong output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes,
    jint datatype, jint num_channels, jint alpha_channel, jint flags,
    jint edge_mode_horizontal, jint edge_mode_vertical,
    jint filter_horizontal, jint filter_vertical, jint space,
    jlong alloc_contextAddress, jfloat s0, jfloat t0, jfloat s1, jfloat t1) {
    
    return Java_org_lwjgl_stb_STBImageResize_nstbir_1resize__JIIIJIIIIIIIIIJ(
        __env, clazz,
        input_pixelsAddress, input_w, input_h, input_stride_in_bytes,
        output_pixelsAddress, output_w, output_h, output_stride_in_bytes,
        datatype, num_channels, alpha_channel, flags,
        edge_mode_horizontal, edge_mode_vertical,
        filter_horizontal, filter_vertical, space, alloc_contextAddress
    );
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1float___3FIII_3FIIII(
    JNIEnv *__env, jclass clazz,
    jfloatArray input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes,
    jfloatArray output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes,
    jint num_channels) {
    
    jint __result;
    jfloat *input_pixels = (*__env)->GetFloatArrayElements(__env, input_pixelsAddress, NULL);
    jfloat *output_pixels = (*__env)->GetFloatArrayElements(__env, output_pixelsAddress, NULL);
    
    UNUSED_PARAMS(__env, clazz)
    
    __result = Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1float__JIIIJIIII(
        __env, clazz,
        (jlong)(uintptr_t)input_pixels, input_w, input_h, input_stride_in_bytes,
        (jlong)(uintptr_t)output_pixels, output_w, output_h, output_stride_in_bytes,
        num_channels
    );
    
    (*__env)->ReleaseFloatArrayElements(__env, output_pixelsAddress, output_pixels, 0);
    (*__env)->ReleaseFloatArrayElements(__env, input_pixelsAddress, input_pixels, 0);
    
    return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1uint16_1generic___3SIII_3SIIIIIIIIIJ(
    JNIEnv *__env, jclass clazz,
    jshortArray input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes,
    jshortArray output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes,
    jint num_channels, jint alpha_channel, jint flags, jint edge_wrap_mode,
    jint filter, jint space, jlong alloc_contextAddress) {
    
    jint __result;
    jshort *input_pixels = (*__env)->GetShortArrayElements(__env, input_pixelsAddress, NULL);
    jshort *output_pixels = (*__env)->GetShortArrayElements(__env, output_pixelsAddress, NULL);
    
    UNUSED_PARAMS(__env, clazz)
    
    __result = Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1uint16_1generic__JIIIJIIIIIIIIIJ(
        __env, clazz,
        (jlong)(uintptr_t)input_pixels, input_w, input_h, input_stride_in_bytes,
        (jlong)(uintptr_t)output_pixels, output_w, output_h, output_stride_in_bytes,
        num_channels, alpha_channel, flags, edge_wrap_mode,
        filter, space, alloc_contextAddress
    );
    
    (*__env)->ReleaseShortArrayElements(__env, output_pixelsAddress, output_pixels, 0);
    (*__env)->ReleaseShortArrayElements(__env, input_pixelsAddress, input_pixels, 0);
    
    return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1float_1generic___3FIII_3FIIIIIIIIIJ(
    JNIEnv *__env, jclass clazz,
    jfloatArray input_pixelsAddress, jint input_w, jint input_h, jint input_stride_in_bytes,
    jfloatArray output_pixelsAddress, jint output_w, jint output_h, jint output_stride_in_bytes,
    jint num_channels, jint alpha_channel, jint flags, jint edge_wrap_mode,
    jint filter, jint space, jlong alloc_contextAddress) {
    
    jint __result;
    jfloat *input_pixels = (*__env)->GetFloatArrayElements(__env, input_pixelsAddress, NULL);
    jfloat *output_pixels = (*__env)->GetFloatArrayElements(__env, output_pixelsAddress, NULL);
    
    UNUSED_PARAMS(__env, clazz)
    
    __result = Java_org_lwjgl_stb_STBImageResize_nstbir_1resize_1float_1generic__JIIIJIIIIIIIIIJ(
        __env, clazz,
        (jlong)(uintptr_t)input_pixels, input_w, input_h, input_stride_in_bytes,
        (jlong)(uintptr_t)output_pixels, output_w, output_h, output_stride_in_bytes,
        num_channels, alpha_channel, flags, edge_wrap_mode,
        filter, space, alloc_contextAddress
    );
    
    (*__env)->ReleaseFloatArrayElements(__env, output_pixelsAddress, output_pixels, 0);
    (*__env)->ReleaseFloatArrayElements(__env, input_pixelsAddress, input_pixels, 0);
    
    return __result;
}

EXTERN_C_EXIT
