#!/bin/bash
set -e

cd dyncall-1.0


chmod +x configure
bash configure --prefix=ios_arm64_build --target=iOS --sdk=11.0

# rm -rf ios_arm64_build
# mkdir ios_arm64_build
# cd ios_arm64_build

# cmake -G Xcode -B build \
#     -DCMAKE_SYSTEM_NAME=iOS \
#     -DCMAKE_Swift_COMPILER_FORCED=true \
#     -DCMAKE_OSX_DEPLOYMENT_TARGET=11.0 \
#     -DCMAKE_C_FLAGS="-no-integrated-as" ..
