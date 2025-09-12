#!/bin/bash
# build.sh - compile all JavaFX source files in src/

set -e  # Stop on error

SRC_DIR="src"
OUT_DIR="out"
JAVAFX_PATH="/Users/rishonjacob/javafx-sdk-24.0.2/lib"

# Create output folder if it doesn't exist
mkdir -p $OUT_DIR

# Compile all Java files in src/ and put classes in out/
javac --module-path $JAVAFX_PATH \
      --add-modules javafx.controls \
      -d $OUT_DIR $SRC_DIR/*.java

echo "✅ Build successful! Classes are in $OUT_DIR/"
