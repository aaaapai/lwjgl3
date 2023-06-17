#!/bin/bash
set -e
export LIBFFI_VERSION=3.4.2
export LWJGL_BUILD_OFFLINE=1
export LWJGL_BUILD_ARCH=arm64

LWJGL_NATIVE=bin/libs/native/macos/$LWJGL_BUILD_ARCH/org/lwjgl
mkdir -p $LWJGL_NATIVE

if [ "$SKIP_LIBFFI" != "1" ]; then
  # Get libffi
  if [ ! -f libffi ]; then
    wget https://github.com/libffi/libffi/releases/download/v$LIBFFI_VERSION/libffi-$LIBFFI_VERSION.tar.gz
    tar xvf libffi-$LIBFFI_VERSION.tar.gz
    mv libffi-$LIBFFI_VERSION libffi
  fi
  cd libffi

  # Build libffi
  bash configure --host=aarch64-apple-ios --prefix=$PWD/aarch64-apple-ios \
    CC="$(xcrun -find -sdk iphoneos clang)" \
    CXX="$(xcrun -find -sdk iphoneos clang++)" \
    CFLAGS="-Wno-implicit-function-declaration -O3 -arch arm64 -isysroot $(xcrun --sdk iphoneos --show-sdk-path) -miphoneos-version-min=14.0" \
    || (cat */config.log && exit 1)
  make -j4
  cd ..

  # Copy libffi
  cp libffi/aarch64-apple-ios/.libs/libffi.a $LWJGL_NATIVE/
fi

# Download libraries
#POJAV_NATIVES="https://github.com/PojavLauncherTeam/PojavLauncher_iOS/raw/main/Natives/resources/Frameworks"
#wget -nc $POJAV_NATIVES/libopenal.so -P $LWJGL_NATIVE/openal
wget -nc "https://nightly.link/khanhduytran0/shaderc/workflows/ios/main/libshaderc.zip"
unzip -o libshaderc.zip -d $LWJGL_NATIVE/shaderc
rm $LWJGL_NATIVE/shaderc/libshaderc_shared.1.dylib
mv $LWJGL_NATIVE/shaderc/libshaderc_shared.dylib $LWJGL_NATIVE/shaderc/libshaderc.dylib

# HACK: Skip compiling and running the generator to save time and keep LWJGLX functions
mkdir -p bin/classes/{generator,templates/META-INF}
touch bin/classes/{generator,templates}/touch.txt bin/classes/generator/generated-touch.txt

# Build LWJGL 3
ant -version
yes | ant -Dplatform.macos=true \
  -Dbinding.assimp=false \
  -Dbinding.bgfx=false \
  -Dbinding.cuda=false \
  -Dbinding.egl=false \
  -Dbinding.glfw=false \
  -Dbinding.jawt=false \
  -Dbinding.jemalloc=false \
  -Dbinding.libdivide=false \
  -Dbinding.llvm=false \
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
  -Dbuild.type=release/3.3.1 \
  -Djavadoc.skip=true \
  -Dnashorn.args="--no-deprecation-warning" \
  compile compile-native release

# Copy native libraries
rm -rf bin/out; mkdir bin/out
find $LWJGL_NATIVE -name 'liblwjgl*.dylib' -exec cp {} bin/out/ \;
cp $LWJGL_NATIVE/shaderc/libshaderc.dylib bin/out/

# Cleanup unused output jar files
find bin/RELEASE \( -name '*-natives-*' -o -name '*-sources.jar' \) -delete
