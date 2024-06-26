/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import org.lwjgl.system.*;

import java.lang.foreign.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

/**
 * Binding configuration with builder support.
 *
 * @param lookup           the {@code SymbolLookup} to use. Required if there are functions without the {@link FFMFunctionAddress} annotation.
 * @param traceConsumer    the consumer to use for tracing native function calls. Optional.
 * @param tracingFilter    can be used to limit tracing instrumentation to specific methods. Optional.
 * @param criticalOverride can be used to override which methods will have the {@link FFMCritical} annotation. Optional.
 */
public record BindingConfig(
    SymbolLookup lookup,
    // TODO: tracing pre, post, return values, transformed signature, etc.
    Object mappers,
    TraceConsumer traceConsumer,
    Predicate<Method> tracingFilter,
    Function<Method, Boolean> criticalOverride
) {
    /** Creates an empty binding configuration builder. */
    public static Builder builder() {
        return new Builder();
    }

    /** A builder class for {@link BindingConfig}. */
    public static final class Builder {
        private SymbolLookup              lookup;
        private Object                    mappers;
        private TraceConsumer             traceConsumer;
        private Predicate<Method>         tracingFilter;
        private Function<Method, Boolean> criticalOverride;

        Builder() {
        }

        /** Enables function pointer retrieval using a {@link SymbolLookup}. */
        public Builder withLookup(SymbolLookup lookup) {
            this.lookup = lookup;
            return this;
        }

        public Builder withMappers(Object mappers) {
            this.mappers = mappers;
            return this;
        }

        /** Enables function pointer retrieval using a {@link FunctionProvider}. */
        public Builder withLookup(FunctionProvider provider) {
            return this.withLookup((SymbolLookup)name -> Optional.of(MemorySegment.ofAddress(provider.getFunctionAddress(name))));
        }

        /**
         * Enables function call tracing.
         *
         * @param consumer the trace consumer
         */
        public Builder withTracing(TraceConsumer consumer) {
            this.traceConsumer = consumer;
            return this;
        }

        /**
         * Enables function call tracing with filtering.
         *
         * @param consumer the trace consumer
         * @param filter   the filter that selects which methods will be instrumented for tracing. Optional.
         */
        public Builder withTracing(TraceConsumer consumer, Predicate<Method> filter) {
            this.traceConsumer = consumer;
            this.tracingFilter = filter;
            return this;
        }

        /**
         * Enables {@link FFMCritical} annotation overriding.
         *
         * <p>Interfaces and methods may be statically configured with the {@link FFMCritical} annotation. This configuration is respected when no critical
         * override is specified in the {@code BindingConfig}. When an override is specified:</p>
         * <ul>
         *     <li>If the override returns null for a method, the static configuration is respected.</li>
         *     <li>If the override returns {@link Boolean#TRUE} for a method, {@link Linker.Option#critical} is forcibly enabled.</li>
         *     <li>If the override returns {@link Boolean#FALSE} for a method, {@link Linker.Option#critical} is forcibly disabled.</li>
         * </ul>
         *
         * <p>This override is useful when the static configuration is not appropriate in certain execution contexts. For example, when running an application
         * with debug facilities enabled, functions that would normally never call back into Java may start doing so. This violates the critical function
         * requirements. Conversely, functions that were not annotated as critical may be forcibly made critical in release builds of the application.</p>
         */
        public Builder withCriticalOverride(Function<Method, Boolean> criticalOverride) {
            this.criticalOverride = criticalOverride;
            return this;
        }

        /** Builds the binding configuration instance. */
        public BindingConfig build() {
            return new BindingConfig(
                lookup,
                mappers,
                traceConsumer,
                tracingFilter,
                criticalOverride
            );
        }
    }
}