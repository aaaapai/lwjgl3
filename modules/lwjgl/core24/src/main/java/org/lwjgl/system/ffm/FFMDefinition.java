/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import java.lang.annotation.*;

/**
 * Defines a struct or union.
 *
 * <p>There is no functionality associated with this annotation, it's only used for documentation purposes.</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FFMDefinition {
    /** The struct or union definition as specified in the native language. */
    String value();
}