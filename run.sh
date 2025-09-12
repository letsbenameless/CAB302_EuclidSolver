#!/bin/bash
# run.sh - run JavaFX project

OUT_DIR="out"
JAVAFX_PATH="/Users/rishonjacob/javafx-sdk-24.0.2/lib"

# Find the main class automatically
# (replace 'EuclidSolverHome' with your main class if known)
MAIN_CLASS="EuclidSolverHome"

java --enable-native-access=javafx.graphics \
     --module-path $JAVAFX_PATH \
     --add-modules javafx.controls \
     -cp $OUT_DIR $MAIN_CLASS
