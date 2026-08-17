package org.lwjgl.sdl;

import org.lwjgl.system.*;

public final class SDL {

    private static final SharedLibrary SDL;

    // Native method to load SDL library via dlopen
    private static native long nLoadSDL(String libName);

    static {
        String specifiedLib = Configuration.SDL_LIBRARY_NAME.get();
        SharedLibrary loaded = null;

        if (specifiedLib != null) {
            long handle = nLoadSDL(specifiedLib);
            if (handle != 0) {
                loaded = Library.createFromHandle(specifiedLib, handle);
                apiLog("[SDL] Loaded specified library: " + specifiedLib + " via native dlopen.");
            } else {
                apiLog("[SDL] Failed to load specified library: " + specifiedLib + ", falling back to default.");
            }
        }

        if (loaded == null) {
            loaded = Library.loadNative(SDL.class, "org.lwjgl.sdl", Configuration.SDL_LIBRARY_NAME.get(Platform.mapLibraryNameBundled("SDL3")), true);
        }

        SDL = loaded;
    }

    /** Returns the SDL {@link SharedLibrary}. */
    public static SharedLibrary getLibrary() {
        return SDL;
    }

    private SDL() {
        throw new UnsupportedOperationException();
    }

    private static void apiLog(String msg) {
        System.out.println(msg);
    }
}

