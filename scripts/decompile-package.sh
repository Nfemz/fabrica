#!/bin/bash
# Hytale API extraction helper
# Usage:
#   ./scripts/decompile-package.sh server.core.plugin         # List signatures (fast)
#   ./scripts/decompile-package.sh server.core.plugin --full  # Full decompile (slow)

set -e

PACKAGE="${1:-server.core.plugin}"
MODE="${2:---signatures}"
JAR_PATH="/mnt/c/Users/femia/AppData/Roaming/Hytale/install/release/package/game/latest/Server/HytaleServer.jar"
CFR_JAR="$HOME/tools/cfr.jar"
OUTPUT_DIR=".claude/context/decompiled"

# Convert package to path
PACKAGE_PATH=$(echo "$PACKAGE" | sed 's/\./\//g')

echo "=== Hytale API Extractor ==="
echo "Package: $PACKAGE"
echo "Mode: $MODE"
echo ""

# Check JAR exists
if [ ! -f "$JAR_PATH" ]; then
    echo "ERROR: HytaleServer.jar not found at $JAR_PATH"
    exit 1
fi

if [ "$MODE" == "--full" ]; then
    # Full decompilation with CFR (slow, memory-intensive)
    if [ ! -f "$CFR_JAR" ]; then
        echo "ERROR: CFR not found. Install with:"
        echo "  wget -O ~/tools/cfr.jar https://github.com/leibnitz27/cfr/releases/download/0.152/cfr-0.152.jar"
        exit 1
    fi

    mkdir -p "$OUTPUT_DIR"
    FILTER="com/hypixel/hytale/${PACKAGE_PATH}/.*"

    echo "Decompiling with CFR (this may take a while)..."
    java -Xmx2g -jar "$CFR_JAR" "$JAR_PATH" \
        --outputdir "$OUTPUT_DIR" \
        --jarfilter "$FILTER" \
        --silent true

    COUNT=$(find "$OUTPUT_DIR" -name "*.java" -type f 2>/dev/null | wc -l)
    echo "Done! Decompiled $COUNT files to $OUTPUT_DIR"
else
    # Fast signature extraction with javap (recommended)
    echo "Extracting class signatures..."
    echo ""

    # List all classes in the package
    CLASSES=$(jar tf "$JAR_PATH" | grep "^com/hypixel/hytale/${PACKAGE_PATH}/[^/]*\.class$" | \
              sed 's|/|.|g' | sed 's|\.class$||' | sort)

    if [ -z "$CLASSES" ]; then
        echo "No classes found in package. Checking subpackages..."
        jar tf "$JAR_PATH" | grep "^com/hypixel/hytale/${PACKAGE_PATH}/" | head -20
        exit 0
    fi

    for CLASS in $CLASSES; do
        echo "=== $(echo $CLASS | sed 's/.*\.//' ) ==="
        javap -classpath "$JAR_PATH" -public "$CLASS" 2>&1 || echo "(error)"
        echo ""
    done
fi
