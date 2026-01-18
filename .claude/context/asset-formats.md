# Hytale Asset Pack Formats

This document covers JSON formats for blocks, items, recipes, and UI files.

## Directory Structure

```
src/main/resources/
├── Common/
│   ├── Blocks/<BlockName>/  # Custom model blocks (model.blockymodel + texture.png)
│   ├── BlockTextures/       # Cube block textures (16x16 or 32x32 PNG)
│   └── Icons/ItemsGenerated/
└── Server/
    ├── Block/
    ├── Item/
    │   ├── Items/         # Item JSON definitions
    │   ├── Category/
    │   └── Recipes/       # Crafting recipes
    └── Languages/
        └── en-US/
            └── server.lang
```

Set `includes_pack=true` in gradle.properties to enable asset packs.

---

## Block/Item Definitions

### Basic Block

`Server/Item/Items/Example_Block.json`:
```json
{
  "TranslationProperties": { "Name": "server.Example_Block.name" },
  "MaxStack": 100,
  "Icon": "Icons/ItemsGenerated/Example_Block.png",
  "Categories": ["Blocks.Rocks"],
  "PlayerAnimationsId": "Block",
  "BlockType": {
    "Material": "Solid",
    "DrawType": "Cube",
    "HitboxType": "Full",
    "Textures": [{"All": "BlockTextures/Example_Block.png"}],
    "BlockSoundSetId": "Stone",
    "ParticleColor": "#aeae8c"
  }
}
```

**Required fields for placeable blocks:**
- `PlayerAnimationsId: "Block"` - At item level, enables block-holding animation
- `HitboxType: "Full"` - In BlockType, makes the block solid and placeable
```

### Per-Side Textures

**IMPORTANT**: Use `Up`/`Down` for vertical faces, NOT `Top`/`Bottom`. Using `Top`/`Bottom` causes missing textures.

```json
"BlockType": {
  "Material": "Solid",
  "DrawType": "Cube",
  "HitboxType": "Full",
  "Textures": [{
    "Up": "BlockTextures/Block_Top.png",
    "Down": "BlockTextures/Block_Bottom.png",
    "Sides": "BlockTextures/Block_Sides.png"
  }],
  "BlockSoundSetId": "Stone",
  "ParticleColor": "#aeae8c"
}
```

### Model Block (Custom 3D Model)

For blocks with custom 3D models (not simple cubes), use `DrawType: "Model"` with `CustomModel` and `CustomModelTexture`.

**File structure:**
```
Common/Blocks/<BlockName>/
├── model.blockymodel    # 3D model from Blockbench
└── texture.png          # Texture atlas for the model
```

**Block definition** (`Server/Item/Items/Machine_Generator.json`):
```json
{
  "TranslationProperties": { "Name": "server.Machine_Generator.name" },
  "MaxStack": 1,
  "Icon": "Icons/ItemsGenerated/Machine_Generator.png",
  "Categories": ["Blocks.Machines"],
  "PlayerAnimationsId": "Block",
  "BlockType": {
    "Material": "Solid",
    "DrawType": "Model",
    "CustomModel": "Blocks/Machine_Generator/model.blockymodel",
    "CustomModelTexture": [
      {
        "Weight": 1,
        "Texture": "Blocks/Machine_Generator/texture.png"
      }
    ],
    "HitboxType": "Full",
    "BlockSoundSetId": "Metal",
    "ParticleColor": "#4a4a4a"
  }
}
```

**Key properties:**
- `DrawType: "Model"` - Use custom 3D model instead of cube
- `CustomModel` - Path to `.blockymodel` file (relative to `Common/`)
- `CustomModelTexture` - Array of texture objects with `Weight` and `Texture` path
- `CustomModelScale` - Optional, scale factor (e.g., `0.8` for 80% size)

**IMPORTANT**: Do NOT use `Model` or `ModelId` properties - they are invalid. Only `CustomModel` works.

### Translation File

`Server/Languages/en-US/server.lang`:
```
Example_Block.name = Example Block
```

---

## Crafting Recipes

**CRITICAL**: Hytale does NOT use Minecraft-style Pattern/Key recipe format. Using the wrong format causes "Input: Can't be null!" validation errors.

### Correct Recipe Format

`Server/Item/Recipes/My_Recipe.json`:
```json
{
  "Input": [
    {"ItemId": "Iron_Ingot", "Quantity": 5},
    {"ItemId": "Copper_Ingot", "Quantity": 3}
  ],
  "PrimaryOutput": {"ItemId": "My_Item", "Quantity": 1},
  "Output": [{"ItemId": "My_Item", "Quantity": 1}],
  "BenchRequirement": [{"Type": "Crafting", "Id": "Workbench"}],
  "TimeSeconds": 3
}
```

### Recipe Fields

| Field | Required | Description |
|-------|----------|-------------|
| `Input` | Yes | Array of input items with `ItemId` and `Quantity` |
| `PrimaryOutput` | Yes | The main output item |
| `Output` | Yes | Array of all output items (usually same as PrimaryOutput) |
| `BenchRequirement` | Yes | Where the recipe can be crafted |
| `TimeSeconds` | No | How long crafting takes (default varies by bench) |

### Bench Types

| Bench ID | Type | Description |
|----------|------|-------------|
| `Fieldcraft` | Crafting | Hand-crafting (no bench needed), use with `"Categories": ["Materials"]` or `["Tools"]` |
| `Workbench` | Crafting | Standard workbench |
| `Salvagebench` | Processing | For breaking down items |

### Example: Fieldcraft Recipe (Hand-Crafting)

```json
{
  "Input": [
    {"ItemId": "Copper_Dust", "Quantity": 3},
    {"ItemId": "Tin_Dust", "Quantity": 1}
  ],
  "PrimaryOutput": {"ItemId": "Bronze_Dust", "Quantity": 4},
  "Output": [{"ItemId": "Bronze_Dust", "Quantity": 4}],
  "BenchRequirement": [{"Type": "Crafting", "Id": "Fieldcraft", "Categories": ["Materials"]}],
  "TimeSeconds": 2
}
```

### Example: Workbench Recipe

```json
{
  "Input": [
    {"ItemId": "Iron_Ingot", "Quantity": 5},
    {"ItemId": "Furnace", "Quantity": 1},
    {"ItemId": "Copper_Ingot", "Quantity": 3}
  ],
  "PrimaryOutput": {"ItemId": "Machine_Generator", "Quantity": 1},
  "Output": [{"ItemId": "Machine_Generator", "Quantity": 1}],
  "BenchRequirement": [{"Type": "Crafting", "Id": "Workbench"}],
  "TimeSeconds": 4
}
```

### WRONG Format (Do NOT Use)

This Minecraft-style format will cause validation errors:
```json
// WRONG - DO NOT USE
{
  "Type": "Crafting",
  "Pattern": ["III", "IFI", "CCC"],
  "Key": {"I": "Iron_Ingot", "F": "Furnace", "C": "Copper_Ingot"},
  "Result": {"Item": "Machine_Generator", "Count": 1}
}
```

### Embedded Recipes (Alternative)

Recipes can also be embedded directly in item definitions:
```json
{
  "TranslationProperties": {"Name": "server.My_Item.name"},
  "Recipe": {
    "Input": [
      {"Quantity": 4, "ItemId": "Wood_Plank"},
      {"Quantity": 2, "ItemId": "Iron_Ingot"}
    ],
    "BenchRequirement": [{"Type": "Crafting", "Id": "Workbench"}]
  },
  "BlockType": { ... }
}
```

---

## Custom UI (.ui Files)

Hytale uses `.ui` files for custom user interfaces. These files use a **CSS/HTML-like syntax, NOT JSON**.

### File Location

All `.ui` files must be in `src/main/resources/Common/UI/Custom/`. The manifest must have `"IncludesAssetPack": true`.

### Correct .ui Syntax

```
Group {
    LayoutMode: Center;
    Anchor: (Width: 200, Height: 100);

    Group #panelId {
        Background: #000000(0.8);
        Anchor: (Width: 200, Height: 100);
        Padding: (Horizontal: 10, Vertical: 10);
        LayoutMode: Top;

        Label #titleLabel {
            Style: (FontSize: 16, HorizontalAlignment: Center);
            Text: "Title";
        }

        Label #infoLabel {
            Style: (FontSize: 12);
            Text: "Info text";
        }
    }
}
```

### Key Rules

- **NOT JSON**: Do not use `{}` with colons and commas like JSON. Use `Property: value;` syntax
- **File extension**: Must be `.ui` only. Files like `.ui.json` will cause "Failed to load CustomUI documents" errors
- **Elements**: `Group` (container/div), `Label` (text), `TextField` (input), `Button`, `TextButton`
- **Properties**: `Anchor` for positioning/size, `Background` for colors/textures, `Style` for fonts, `LayoutMode` for layout, `Padding` for spacing
- **IDs**: Use `#elementId` after the element type to reference from Java

### Backgrounds

```
// Solid color with opacity (0.0-1.0)
Background: #000000(0.8);

// Texture (relative path when in same folder)
Background: "MyTexture.png";

// Texture with border for 9-slice scaling
Background: (TexturePath: "Common/ContainerPatch.png", Border: 20);
```

### Text Alignment

**IMPORTANT**: `Alignment: Left` does NOT work. Use these instead:
- `HorizontalAlignment: Center` / `Left` / `Right`
- `VerticalAlignment: Center` / `Top` / `Bottom`

```
Label #myLabel {
    Style: (FontSize: 16, HorizontalAlignment: Center, VerticalAlignment: Center);
    Text: "Centered text";
}
```

### Opening Custom UI from Java

```java
public class MachineUI extends BasicCustomUIPage {
    public MachineUI(PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss);
    }

    @Override
    public void build(UICommandBuilder commands) {
        commands.append("Common/UI/Custom/MachineUI.ui");
        commands.set("#powerLabel", Message.raw("Power: 100 W"));  // Must use Message, not String
    }
}

// To open:
Player player = store.getComponent(ref, Player.getComponentType());
player.getPageManager().openCustomPage(ref, store, new MachineUI(playerRef));
```

### Reference Examples

Extract Hytale's own .ui files from `Assets.zip` to see working examples:
- `Common/UI/Custom/Common.ui` - Shared styles and components
- `Common/UI/Custom/Hud/TimeLeft.ui` - Simple HUD example
- `Common/UI/Custom/Pages/` - Page-based UI examples

Use PowerShell to list/extract:
```powershell
# List .ui files
Add-Type -AssemblyName System.IO.Compression.FileSystem
[System.IO.Compression.ZipFile]::OpenRead('Assets.zip').Entries | Where-Object { $_.Name -like '*.ui' }
```
