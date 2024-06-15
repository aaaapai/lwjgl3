#!/bin/bash
set -e
#wget https://dl.google.com/android/repository/android-ndk-r26d-linux.zip
#unzip android-ndk-r26d-linux.zip >> /dev/null
export ANDROID=1 LWJGL_BUILD_OFFLINE=1
#export LWJGL_BUILD_ARCH=arm64

# Setup env
if   [ "$LWJGL_BUILD_ARCH" == "arm64" ]; then
  export NDK_ABI=arm64-v8a NDK_TARGET=aarch64
elif [ "$LWJGL_BUILD_ARCH" == "arm32" ]; then
  export NDK_ABI=armeabi-v7a NDK_TARGET=armv7a NDK_SUFFIX=eabi
elif [ "$LWJGL_BUILD_ARCH" == "x86" ]; then
  export NDK_ABI=x86 NDK_TARGET=i686
  # Workaround: LWJGL 3 lacks of x86 Linux libraries
  mkdir -p bin/libs/native/linux/x86/org/lwjgl/glfw
  touch bin/libs/native/linux/x86/org/lwjgl/glfw/libglfw.so
elif [ "$LWJGL_BUILD_ARCH" == "x64" ]; then
  export NDK_ABI=x86_64 NDK_TARGET=x86_64
fi

export TARGET=$NDK_TARGET-linux-android$NDK_SUFFIX
export PATH=$PATH:$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/linux-x86_64/bin
LWJGL_NATIVE=bin/libs/native/linux/$LWJGL_BUILD_ARCH/org/lwjgl
mkdir -p $LWJGL_NATIVE

if [ "$SKIP_LIBFFI" != "1" ]; then
  # Get libffi
  if [ ! -d libffi ]; then
    git clone --depth 1 https://github.com/aaaapai/libffi ${PWD}/libffi
  fi
  cd libffi

  # Build libffi
  ./autogen.sh
  ./configure
  bash configure --host=$TARGET --prefix=$PWD/$NDK_TARGET-unknown-linux-android$NDK_SUFFIX CC=${TARGET}21-clang CXX=${TARGET}21-clang++
  make -j4
  cd ..

  # Copy libffi
  cp libffi/$NDK_TARGET-linux-android$NDK_SUFFIX/.libs/libffi.a $LWJGL_NATIVE/
fi

# Download libraries
POJAV_NATIVES="https://github.com/aaaapai/PojavLauncher-Beta-Zink/raw/exp_v3/app_pojavlauncher/src/main/jniLibs/$NDK_ABI"
wget -nc $POJAV_NATIVES/libopenal.so -P $LWJGL_NATIVE/openal
wget -nc "https://nightly.link/aaaapai/shaderc/workflows/android/main/libshaderc-$NDK_ABI.zip"
unzip -o libshaderc-$NDK_ABI.zip -d $LWJGL_NATIVE/shaderc

# Build LWJGL 3
ant -version
yes | ant -Dplatform.linux=true \
  -Dplatform.freebsd=false \
  -Dplatform.windows=false \
  -Dplatform.macos=false \
  -Dbinding.jemalloc=false \
  -Dbinding.llvm=true \
  -Dbinding.assimp=false \
  -Dbinding.bgfx=false \
  -Dbinding.cuda=false \
  -Dbinding.egl=false \
  -Dbinding.jawt=false \
  -Dbinding.libdivide=false \
  -Dbinding.lmdb=false \
  -Dbinding.lz4=false \
  -Dbinding.meow=false \
  -Dbinding.meshoptimizer=false \
  -Dbinding.nfd=false \
  -Dbinding.nuklear=false \
  -Dbinding.odbc=false \
  -Dbinding.opencl=false \
  -Dbinding.openvr=false \
  -Dbinding.openxr=false \
  -Dbinding.opus=false \
  -Dbinding.par=false \
  -Dbinding.remotery=false \
  -Dbinding.rpmalloc=false \
  -Dbinding.spvc=false \
  -Dbinding.sse=false \
  -Dbinding.tinyexr=false \
  -Dbinding.tootle=false \
  -Dbinding.xxhash=false \
  -Dbinding.yoga=false \
  -Dbinding.zstd=false \
  -Dbuild.type=nightly/3.4.0 \
  -Djavadoc.skip=true \
  -Dnashorn.args="--no-deprecation-warning" \
  compile compile-templates compile-native debug

# Copy native libraries
rm -rf bin/out; mkdir bin/out
find $LWJGL_NATIVE -name 'liblwjgl*.so' -exec cp {} bin/out/ \;
cp $LWJGL_NATIVE/shaderc/libshaderc.so bin/out/

# Cleanup unused output jar files
find bin/RELEASE \( -name '*-natives-*' -o -name '*-sources.jar' \) -delete
