/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import java.nio.*;

import org.lwjgl.system.*;

import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * The <a href="https://registry.khronos.org/OpenXR/specs/1.1/html/xrspec.html#XR_KHR_convert_timespec_time">XR_KHR_convert_timespec_time</a> extension.
 * 
 * <p>This extension provides two functions for converting between timespec monotonic time and {@code XrTime}. The {@link #xrConvertTimespecTimeToTimeKHR ConvertTimespecTimeToTimeKHR} function converts from timespec time to {@code XrTime}, while the {@link #xrConvertTimeToTimespecTimeKHR ConvertTimeToTimespecTimeKHR} function converts {@code XrTime} to timespec monotonic time. The primary use case for this functionality is to be able to synchronize events between the local system and the OpenXR system.</p>
 */
public class KHRConvertTimespecTime {

    /** The extension specification version. */
    public static final int XR_KHR_convert_timespec_time_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_KHR_CONVERT_TIMESPEC_TIME_EXTENSION_NAME = "XR_KHR_convert_timespec_time";

    protected KHRConvertTimespecTime() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrConvertTimespecTimeToTimeKHR ] ---

    /** Unsafe version of: {@link #xrConvertTimespecTimeToTimeKHR ConvertTimespecTimeToTimeKHR} */
    public static int nxrConvertTimespecTimeToTimeKHR(XrInstance instance, long timespecTime, long time) {
        long __functionAddress = instance.getCapabilities().xrConvertTimespecTimeToTimeKHR;
        if (CHECKS) {
            check(__functionAddress);
            check(timespecTime);
        }
        return callPPPI(instance.address(), timespecTime, time, __functionAddress);
    }

    /**
     * Convert timespec monotonic time to XrTime.
     * 
     * <h5>C Specification</h5>
     * 
     * <p>To convert from timespec monotonic time to {@code XrTime}, call:</p>
     * 
     * <pre><code>
     * XrResult xrConvertTimespecTimeToTimeKHR(
     *     XrInstance                                  instance,
     *     const struct timespec*                      timespecTime,
     *     XrTime*                                     time);</code></pre>
     * 
     * <h5>Description</h5>
     * 
     * <p>The {@link #xrConvertTimespecTimeToTimeKHR ConvertTimespecTimeToTimeKHR} function converts a time obtained by the {@code clock_gettime} function to the equivalent {@code XrTime}.</p>
     * 
     * <p>If the output {@code time} cannot represent the input {@code timespecTime}, the runtime <b>must</b> return {@link XR10#XR_ERROR_TIME_INVALID ERROR_TIME_INVALID}.</p>
     * 
     * <h5>Valid Usage (Implicit)</h5>
     * 
     * <ul>
     * <li>The {@link KHRConvertTimespecTime XR_KHR_convert_timespec_time} extension <b>must</b> be enabled prior to calling {@link #xrConvertTimespecTimeToTimeKHR ConvertTimespecTimeToTimeKHR}</li>
     * <li>{@code instance} <b>must</b> be a valid {@code XrInstance} handle</li>
     * <li>{@code timespecTime} <b>must</b> be a pointer to a valid {@code timespec} value</li>
     * <li>{@code time} <b>must</b> be a pointer to an {@code XrTime} value</li>
     * </ul>
     * 
     * <h5>Return Codes</h5>
     * 
     * <dl>
     * <dt>On success, this command returns</dt>
     * <dd><ul>
     * <li>{@link XR10#XR_SUCCESS SUCCESS}</li>
     * </ul></dd>
     * <dt>On failure, this command returns</dt>
     * <dd><ul>
     * <li>{@link XR10#XR_ERROR_FUNCTION_UNSUPPORTED ERROR_FUNCTION_UNSUPPORTED}</li>
     * <li>{@link XR10#XR_ERROR_VALIDATION_FAILURE ERROR_VALIDATION_FAILURE}</li>
     * <li>{@link XR10#XR_ERROR_RUNTIME_FAILURE ERROR_RUNTIME_FAILURE}</li>
     * <li>{@link XR10#XR_ERROR_HANDLE_INVALID ERROR_HANDLE_INVALID}</li>
     * <li>{@link XR10#XR_ERROR_INSTANCE_LOST ERROR_INSTANCE_LOST}</li>
     * <li>{@link XR10#XR_ERROR_TIME_INVALID ERROR_TIME_INVALID}</li>
     * </ul></dd>
     * </dl>
     *
     * @param instance     an {@code XrInstance} handle previously created with {@link XR10#xrCreateInstance CreateInstance}.
     * @param timespecTime a {@code timespec} obtained from {@code clock_gettime} with {@code CLOCK_MONOTONIC}.
     * @param time         the resulting {@code XrTime} that is equivalent to the {@code timespecTime}.
     */
    @NativeType("XrResult")
    public static int xrConvertTimespecTimeToTimeKHR(XrInstance instance, @NativeType("struct timespec const *") long timespecTime, @NativeType("XrTime *") LongBuffer time) {
        if (CHECKS) {
            check(time, 1);
        }
        return nxrConvertTimespecTimeToTimeKHR(instance, timespecTime, memAddress(time));
    }

    // --- [ xrConvertTimeToTimespecTimeKHR ] ---

    /**
     * Convert XrTime to timespec monotonic time.
     * 
     * <h5>C Specification</h5>
     * 
     * <p>To convert from {@code XrTime} to timespec monotonic time, call:</p>
     * 
     * <pre><code>
     * XrResult xrConvertTimeToTimespecTimeKHR(
     *     XrInstance                                  instance,
     *     XrTime                                      time,
     *     struct timespec*                            timespecTime);</code></pre>
     * 
     * <h5>Description</h5>
     * 
     * <p>The {@link #xrConvertTimeToTimespecTimeKHR ConvertTimeToTimespecTimeKHR} function converts an {@code XrTime} to time as if generated by {@code clock_gettime}.</p>
     * 
     * <p>If the output {@code timespecTime} cannot represent the input {@code time}, the runtime <b>must</b> return {@link XR10#XR_ERROR_TIME_INVALID ERROR_TIME_INVALID}.</p>
     * 
     * <h5>Valid Usage (Implicit)</h5>
     * 
     * <ul>
     * <li>The {@link KHRConvertTimespecTime XR_KHR_convert_timespec_time} extension <b>must</b> be enabled prior to calling {@link #xrConvertTimeToTimespecTimeKHR ConvertTimeToTimespecTimeKHR}</li>
     * <li>{@code instance} <b>must</b> be a valid {@code XrInstance} handle</li>
     * <li>{@code timespecTime} <b>must</b> be a pointer to a {@code timespec} value</li>
     * </ul>
     * 
     * <h5>Return Codes</h5>
     * 
     * <dl>
     * <dt>On success, this command returns</dt>
     * <dd><ul>
     * <li>{@link XR10#XR_SUCCESS SUCCESS}</li>
     * </ul></dd>
     * <dt>On failure, this command returns</dt>
     * <dd><ul>
     * <li>{@link XR10#XR_ERROR_FUNCTION_UNSUPPORTED ERROR_FUNCTION_UNSUPPORTED}</li>
     * <li>{@link XR10#XR_ERROR_VALIDATION_FAILURE ERROR_VALIDATION_FAILURE}</li>
     * <li>{@link XR10#XR_ERROR_RUNTIME_FAILURE ERROR_RUNTIME_FAILURE}</li>
     * <li>{@link XR10#XR_ERROR_HANDLE_INVALID ERROR_HANDLE_INVALID}</li>
     * <li>{@link XR10#XR_ERROR_INSTANCE_LOST ERROR_INSTANCE_LOST}</li>
     * <li>{@link XR10#XR_ERROR_TIME_INVALID ERROR_TIME_INVALID}</li>
     * </ul></dd>
     * </dl>
     *
     * @param instance     an {@code XrInstance} handle previously created with {@link XR10#xrCreateInstance CreateInstance}.
     * @param time         an {@code XrTime}.
     * @param timespecTime the resulting timespec time that is equivalent to a {@code timespec} obtained from {@code clock_gettime} with {@code CLOCK_MONOTONIC}.
     */
    @NativeType("XrResult")
    public static int xrConvertTimeToTimespecTimeKHR(XrInstance instance, @NativeType("XrTime") long time, @NativeType("struct timespec *") long timespecTime) {
        long __functionAddress = instance.getCapabilities().xrConvertTimeToTimespecTimeKHR;
        if (CHECKS) {
            check(__functionAddress);
            check(timespecTime);
        }
        return callPJPI(instance.address(), time, timespecTime, __functionAddress);
    }

}