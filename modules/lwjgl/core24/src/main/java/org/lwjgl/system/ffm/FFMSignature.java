/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import java.lang.annotation.*;

/**
 * Defines a native function signature.
 *
 * <p>There is no functionality associated with this annotation, it's only used for documentation purposes.</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FFMSignature {
    /** The function signature as specified in the native language. */
    String value();
}