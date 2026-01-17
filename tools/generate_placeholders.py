#!/usr/bin/env python3
"""
Generates placeholder PNG textures for Fabrica.
Uses only Python built-in modules (no PIL required).
"""

import zlib
import struct
import os

def create_png(width, height, rgb_color, output_path):
    """
    Creates a simple solid-color PNG file.

    Args:
        width: Image width in pixels
        height: Image height in pixels
        rgb_color: Tuple of (R, G, B) values 0-255
        output_path: Path to save the PNG
    """
    def png_chunk(chunk_type, data):
        """Create a PNG chunk with CRC."""
        chunk = chunk_type + data
        crc = zlib.crc32(chunk) & 0xffffffff
        return struct.pack('>I', len(data)) + chunk + struct.pack('>I', crc)

    # PNG signature
    signature = b'\x89PNG\r\n\x1a\n'

    # IHDR chunk (image header)
    ihdr_data = struct.pack('>IIBBBBB', width, height, 8, 2, 0, 0, 0)
    ihdr = png_chunk(b'IHDR', ihdr_data)

    # IDAT chunk (image data)
    raw_data = b''
    for y in range(height):
        raw_data += b'\x00'  # Filter type: None
        for x in range(width):
            raw_data += bytes(rgb_color)

    compressed = zlib.compress(raw_data, 9)
    idat = png_chunk(b'IDAT', compressed)

    # IEND chunk
    iend = png_chunk(b'IEND', b'')

    # Write file
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    with open(output_path, 'wb') as f:
        f.write(signature + ihdr + idat + iend)

    print(f"Created: {output_path}")


def create_png_with_border(width, height, fill_color, border_color, output_path):
    """Creates a PNG with a 1-pixel border."""
    def png_chunk(chunk_type, data):
        chunk = chunk_type + data
        crc = zlib.crc32(chunk) & 0xffffffff
        return struct.pack('>I', len(data)) + chunk + struct.pack('>I', crc)

    signature = b'\x89PNG\r\n\x1a\n'
    ihdr_data = struct.pack('>IIBBBBB', width, height, 8, 2, 0, 0, 0)
    ihdr = png_chunk(b'IHDR', ihdr_data)

    raw_data = b''
    for y in range(height):
        raw_data += b'\x00'
        for x in range(width):
            if x == 0 or x == width-1 or y == 0 or y == height-1:
                raw_data += bytes(border_color)
            else:
                raw_data += bytes(fill_color)

    compressed = zlib.compress(raw_data, 9)
    idat = png_chunk(b'IDAT', compressed)
    iend = png_chunk(b'IEND', b'')

    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    with open(output_path, 'wb') as f:
        f.write(signature + ihdr + idat + iend)

    print(f"Created: {output_path}")


# Base paths
BLOCK_TEX = "src/main/resources/Common/BlockTextures"
ITEM_ICON = "src/main/resources/Common/Icons/ItemsGenerated"
UI_PATH = "src/main/resources/Common/UI/Custom"

# Color definitions (R, G, B)
COLORS = {
    'tin': (192, 192, 192),        # Silver
    'copper': (184, 115, 51),      # Copper orange
    'bronze': (205, 127, 50),      # Bronze
    'iron': (105, 105, 105),       # Dark gray
    'steel': (112, 128, 144),      # Steel gray
    'energy': (0, 191, 255),       # Cyan/blue
    'fire': (255, 102, 0),         # Orange
    'green': (0, 200, 0),          # Indicator green
    'dark': (47, 47, 47),          # Dark background
    'slot': (60, 60, 60),          # Slot background
}

print("=== Generating Fabrica Placeholder Textures ===\n")

# --- Block Textures (32x32) ---
print("--- Block Textures ---")

# Ores
create_png_with_border(32, 32, (128, 128, 128), COLORS['tin'], f"{BLOCK_TEX}/Tin_Ore.png")
create_png_with_border(32, 32, (128, 128, 128), COLORS['copper'], f"{BLOCK_TEX}/Copper_Ore.png")

# Machines
create_png_with_border(32, 32, COLORS['steel'], COLORS['fire'], f"{BLOCK_TEX}/Machine_Generator_Front.png")
create_png_with_border(32, 32, COLORS['steel'], (80, 80, 80), f"{BLOCK_TEX}/Machine_Generator_Side.png")
create_png_with_border(32, 32, COLORS['steel'], (80, 80, 80), f"{BLOCK_TEX}/Machine_Generator_Top.png")

create_png_with_border(32, 32, COLORS['steel'], COLORS['energy'], f"{BLOCK_TEX}/Machine_Battery_Front.png")
create_png_with_border(32, 32, COLORS['steel'], (80, 80, 80), f"{BLOCK_TEX}/Machine_Battery_Side.png")

create_png_with_border(32, 32, COLORS['steel'], COLORS['iron'], f"{BLOCK_TEX}/Machine_Macerator_Front.png")
create_png_with_border(32, 32, COLORS['steel'], (80, 80, 80), f"{BLOCK_TEX}/Machine_Macerator_Top.png")

create_png_with_border(32, 32, COLORS['steel'], COLORS['fire'], f"{BLOCK_TEX}/Machine_Electric_Furnace_Front.png")
create_png_with_border(32, 32, COLORS['steel'], (80, 80, 80), f"{BLOCK_TEX}/Machine_Electric_Furnace_Side.png")

create_png_with_border(32, 32, COLORS['copper'], COLORS['dark'], f"{BLOCK_TEX}/Machine_Cable.png")
create_png_with_border(32, 32, COLORS['iron'], COLORS['dark'], f"{BLOCK_TEX}/Conveyor_Belt.png")

# --- Item Icons (32x32) ---
print("\n--- Item Icons ---")

# Ores (items)
create_png_with_border(32, 32, (128, 128, 128), COLORS['tin'], f"{ITEM_ICON}/Tin_Ore.png")
create_png_with_border(32, 32, (128, 128, 128), COLORS['copper'], f"{ITEM_ICON}/Copper_Ore.png")

# Dusts
create_png(32, 32, COLORS['iron'], f"{ITEM_ICON}/Iron_Dust.png")
create_png(32, 32, COLORS['copper'], f"{ITEM_ICON}/Copper_Dust.png")
create_png(32, 32, COLORS['tin'], f"{ITEM_ICON}/Tin_Dust.png")
create_png(32, 32, COLORS['bronze'], f"{ITEM_ICON}/Bronze_Dust.png")

# Ingots
create_png_with_border(32, 32, COLORS['tin'], (220, 220, 220), f"{ITEM_ICON}/Tin_Ingot.png")
create_png_with_border(32, 32, COLORS['copper'], (220, 150, 100), f"{ITEM_ICON}/Copper_Ingot.png")
create_png_with_border(32, 32, COLORS['bronze'], (230, 170, 100), f"{ITEM_ICON}/Bronze_Ingot.png")

# Machine items
create_png_with_border(32, 32, COLORS['steel'], COLORS['fire'], f"{ITEM_ICON}/Machine_Generator.png")
create_png_with_border(32, 32, COLORS['steel'], COLORS['energy'], f"{ITEM_ICON}/Machine_Battery.png")
create_png_with_border(32, 32, COLORS['steel'], COLORS['iron'], f"{ITEM_ICON}/Machine_Macerator.png")
create_png_with_border(32, 32, COLORS['steel'], COLORS['fire'], f"{ITEM_ICON}/Machine_Electric_Furnace.png")
create_png_with_border(32, 32, COLORS['copper'], COLORS['dark'], f"{ITEM_ICON}/Machine_Cable.png")
create_png_with_border(32, 32, COLORS['iron'], COLORS['dark'], f"{ITEM_ICON}/Conveyor_Belt.png")

# --- GUI Elements ---
print("\n--- GUI Elements ---")

# Main GUI background (256x166)
create_png_with_border(256, 166, COLORS['dark'], COLORS['steel'], f"{UI_PATH}/MachineGUI_Background.png")

# Progress arrow (24x16)
create_png(24, 16, (80, 80, 80), f"{UI_PATH}/ProgressArrow_Empty.png")
create_png(24, 16, COLORS['fire'], f"{UI_PATH}/ProgressArrow_Full.png")

# Burn progress (16x16)
create_png(16, 16, (80, 80, 80), f"{UI_PATH}/BurnProgress_Empty.png")
create_png(16, 16, COLORS['fire'], f"{UI_PATH}/BurnProgress_Full.png")

# Energy bar (16x52)
create_png(16, 52, (40, 40, 40), f"{UI_PATH}/EnergyBar_Empty.png")
create_png(16, 52, COLORS['energy'], f"{UI_PATH}/EnergyBar_Full.png")

# Power indicators (8x8)
create_png(8, 8, (60, 60, 60), f"{UI_PATH}/PowerIndicator_Off.png")
create_png(8, 8, COLORS['green'], f"{UI_PATH}/PowerIndicator_On.png")

# Slot background (18x18)
create_png_with_border(18, 18, COLORS['slot'], (80, 80, 80), f"{UI_PATH}/Slot_Background.png")

print("\n=== Done! Generated all placeholder textures ===")
