#!/bin/bash
# Java App Packager for JBang (Cross-Platform)
# Usage: ./package.sh

# Detect OS using uname
OS=$(uname -s | tr '[:upper:]' '[:lower:]')
case $OS in
  linux*)  PLATFORM="linux" ;;
  darwin*) PLATFORM="macos" ;;
  cygwin*|mingw*|msys*) PLATFORM="windows" ;;
  *) echo "Unsupported OS"; exit 1 ;;
esac

# Get input file
read -p "Enter JBang Java file (e.g., MyApp.java): " JFILE
if [ ! -f "$JFILE" ]; then
  echo "Error: File $JFILE not found"
  exit 1
fi

# Extract base name
APP_NAME=$(basename "$JFILE" .java)
BUILD_DIR="build/$APP_NAME"
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

echo -e "\n🔨 Building $APP_NAME for $PLATFORM..."

# Build with JBang and create fatjar
jbang export --verbose --force --output="$BUILD_DIR/$APP_NAME.jar" "$JFILE"

# Create runtime image
echo -e "\n📦 Creating minimal JRE..."
JDK_PATH=$(dirname $(which java))
MODULES=$(jdeps --print-module-deps --ignore-missing-deps "$BUILD_DIR/$APP_NAME.jar")

jlink --strip-debug \
      --no-header-files \
      --no-man-pages \
      --add-modules "$MODULES" \
      --output "$BUILD_DIR/runtime"

# Platform-specific packaging
case $PLATFORM in
  linux)
    echo -e "\n📦 Creating AppImage..."
    mkdir -p "$BUILD_DIR/AppImage/usr/bin"
    cp "$BUILD_DIR/$APP_NAME.jar" "$BUILD_DIR/AppImage/usr/bin/"
    cp -r "$BUILD_DIR/runtime" "$BUILD_DIR/AppImage/usr/"
    
    cat > "$BUILD_DIR/AppImage/AppRun" <<EOF
#!/bin/sh
HERE=\$(dirname "\$(readlink -f "\$0")")
exec "\$HERE/usr/runtime/bin/java" -jar "\$HERE/usr/bin/$APP_NAME.jar"
EOF
    chmod +x "$BUILD_DIR/AppImage/AppRun"
    
    # Download appimagetool if missing
    if [ ! -f appimagetool ]; then
      wget -q https://github.com/AppImage/AppImageKit/releases/download/continuous/appimagetool-x86_64.AppImage
      mv appimagetool-x86_64.AppImage appimagetool
      chmod +x appimagetool
    fi
    
    ./appimagetool "$BUILD_DIR/AppImage" "$BUILD_DIR/$APP_NAME.AppImage"
    ;;
  
  macos)
    echo -e "\n📦 Creating macOS bundle..."
    jpackage --name "$APP_NAME" \
             --input "$BUILD_DIR" \
             --main-jar "$APP_NAME.jar" \
             --runtime-image "$BUILD_DIR/runtime" \
             --type dmg \
             --dest "$BUILD_DIR/pkg"
    ;;
  
  windows)
    echo -e "\n📦 Creating Windows installer..."
    jpackage --name "$APP_NAME" \
             --input "$BUILD_DIR" \
             --main-jar "$APP_NAME.jar" \
             --runtime-image "$BUILD_DIR/runtime" \
             --type msi \
             --dest "$BUILD_DIR/pkg"
    ;;
esac

echo -e "\n✅ Packaging complete! Output files:"
find "$BUILD_DIR" -type f \( -name "*.AppImage" -o -name "*.dmg" -o -name "*.msi" \)
