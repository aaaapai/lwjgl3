/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

/**
 * The <a href="https://registry.khronos.org/OpenXR/specs/1.1/html/xrspec.html#XR_EXT_user_presence">XR_EXT_user_presence</a> extension.
 * 
 * <p>This extension introduces a new event to notify when the system detected the change of user presence, such as when the user has taken off or put on an XR headset.</p>
 * 
 * <p>This event is typically used by an XR applications with non-XR experiences outside of the XR headset. For instance, some applications pause the game logic or video playback until the user puts on the headset, displaying an instructional message to the user in the mirror window on the desktop PC monitor. As another example, the application might use this event to disable a head-tracking driven avatar in an online meeting when the user has taken off the headset.</p>
 * 
 * <p>The user presence is fundamentally decoupled from the session lifecycle. Although the core spec for {@code XrSessionState} hinted potential correlation between the session state and user presence, in practice, such a connection <b>may</b> not consistently hold across various runtimes. Application <b>should</b> avoid relying on assumptions regarding these relationships between session state and user presence, instead, they should utilize this extension to reliably obtain user presence information.</p>
 */
public final class EXTUserPresence {

    /** The extension specification version. */
    public static final int XR_EXT_user_presence_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_EXT_USER_PRESENCE_EXTENSION_NAME = "XR_EXT_user_presence";

    /**
     * Extends {@code XrStructureType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_TYPE_EVENT_DATA_USER_PRESENCE_CHANGED_EXT TYPE_EVENT_DATA_USER_PRESENCE_CHANGED_EXT}</li>
     * <li>{@link #XR_TYPE_SYSTEM_USER_PRESENCE_PROPERTIES_EXT TYPE_SYSTEM_USER_PRESENCE_PROPERTIES_EXT}</li>
     * </ul>
     */
    public static final int
        XR_TYPE_EVENT_DATA_USER_PRESENCE_CHANGED_EXT = 1000470000,
        XR_TYPE_SYSTEM_USER_PRESENCE_PROPERTIES_EXT  = 1000470001;

    private EXTUserPresence() {}

}