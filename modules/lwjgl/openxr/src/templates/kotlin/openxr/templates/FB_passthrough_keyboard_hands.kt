/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package openxr.templates

import org.lwjgl.generator.*
import openxr.*

val FB_passthrough_keyboard_hands = "FBPassthroughKeyboardHands".nativeClassXR("FB_passthrough_keyboard_hands", type = "instance", postfix = "FB") {
    documentation =
        """
        The <a href="https://registry.khronos.org/OpenXR/specs/1.0/html/xrspec.html\#XR_FB_passthrough_keyboard_hands">XR_FB_passthrough_keyboard_hands</a> extension.

        This extension enables applications to show passthrough hands when hands are placed over the tracked keyboard. It enables users to see their hands over the keyboard in a mixed reality application. This extension is dependent on {@link FBPassthrough XR_FB_passthrough} extension which can be used to create a passthrough layer for hand presence use-case.

        The extension supports a single pair of hands (one left and one right hand), multiple pair of hands are not supported.

        This extension allows:

        <ul>
            <li>Creation of keyboard hands passthrough layer using #CreatePassthroughLayerFB()</li>
            <li>Setting the level of intensity for the hand mask in a passthrough layer with purpose {@code XrPassthroughLayerPurposeFB} as #PASSTHROUGH_LAYER_PURPOSE_TRACKED_KEYBOARD_HANDS_FB or #PASSTHROUGH_LAYER_PURPOSE_TRACKED_KEYBOARD_MASKED_HANDS_FB</li>
        </ul>
        """

    IntConstant(
        "The extension specification version.",

        "FB_passthrough_keyboard_hands_SPEC_VERSION".."2"
    )

    StringConstant(
        "The extension name.",

        "FB_PASSTHROUGH_KEYBOARD_HANDS_EXTENSION_NAME".."XR_FB_passthrough_keyboard_hands"
    )

    EnumConstant(
        "Extends {@code XrPassthroughLayerPurposeFB}.",

        "PASSTHROUGH_LAYER_PURPOSE_TRACKED_KEYBOARD_HANDS_FB".."1000203001",
        "PASSTHROUGH_LAYER_PURPOSE_TRACKED_KEYBOARD_MASKED_HANDS_FB".."1000203002"
    )

    EnumConstant(
        "Extends {@code XrStructureType}.",

        "TYPE_PASSTHROUGH_KEYBOARD_HANDS_INTENSITY_FB".."1000203002"
    )

    XrResult(
        "PassthroughLayerSetKeyboardHandsIntensityFB",
        """
        Renders hands over the keyboard (keyboard hands) with a specific intensity of hands passthrough layer.

        <h5>C Specification</h5>
        The #PassthroughLayerSetKeyboardHandsIntensityFB() function is defined as:

        <pre><code>
￿XrResult xrPassthroughLayerSetKeyboardHandsIntensityFB(
￿    XrPassthroughLayerFB                        layer,
￿    const XrPassthroughKeyboardHandsIntensityFB* intensity);</code></pre>

        <h5>Description</h5>
        Sets an ##XrPassthroughKeyboardHandsIntensityFB intensity on an {@code XrPassthroughLayerFB} layer.

        <h5>Valid Usage (Implicit)</h5>
        <ul>
            <li>The {@link FBPassthroughKeyboardHands XR_FB_passthrough_keyboard_hands} extension <b>must</b> be enabled prior to calling #PassthroughLayerSetKeyboardHandsIntensityFB()</li>
            <li>{@code layer} <b>must</b> be a valid {@code XrPassthroughLayerFB} handle</li>
            <li>{@code intensity} <b>must</b> be a pointer to a valid ##XrPassthroughKeyboardHandsIntensityFB structure</li>
        </ul>

        <h5>Return Codes</h5>
        <dl>
            <dt>On success, this command returns</dt>
            <dd><ul>
                <li>#SUCCESS</li>
                <li>#SESSION_LOSS_PENDING</li>
            </ul></dd>

            <dt>On failure, this command returns</dt>
            <dd><ul>
                <li>#ERROR_FUNCTION_UNSUPPORTED</li>
                <li>#ERROR_VALIDATION_FAILURE</li>
                <li>#ERROR_RUNTIME_FAILURE</li>
                <li>#ERROR_HANDLE_INVALID</li>
                <li>#ERROR_INSTANCE_LOST</li>
                <li>#ERROR_SESSION_LOST</li>
                <li>#ERROR_FEATURE_UNSUPPORTED</li>
            </ul></dd>
        </dl>

        <h5>See Also</h5>
        ##XrPassthroughKeyboardHandsIntensityFB
        """,

        XrPassthroughLayerFB("layer", "the {@code XrPassthroughLayerFB} to apply the intensity."),
        XrPassthroughKeyboardHandsIntensityFB.const.p("intensity", "the ##XrPassthroughKeyboardHandsIntensityFB to be set.")
    )
}