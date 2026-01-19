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

**Reference**: https://hytalemodding.dev/en/docs/guides/plugin/ui

### File Location

All `.ui` files must be in `src/main/resources/Common/UI/Custom/`. The manifest must have `"IncludesAssetPack": true`.

### Correct .ui Syntax

```
Group {
  LayoutMode: Center;

  Group #MyPanel {
    Background: #000000(0.85);
    Anchor: (Width: 260, Height: 180);
    Padding: (Left: 12, Right: 12, Top: 12, Bottom: 12);
    LayoutMode: Top;
    OutlineColor: #93844c(0.5);
    OutlineSize: 1.5;

    Group {
      LayoutMode: Left;
      Anchor: (Height: 24);

      Label {FlexWeight: 1;}

      Label #Title {
        Style: (FontSize: 18, TextColor: #93844c, RenderBold: true);
        Text: "My Title";
      }

      Label {FlexWeight: 1;}
    }

    Label #InfoLabel {
      Style: (FontSize: 12, TextColor: #aaaaaa);
      Text: "Info text";
    }
  }
}
```

### Key Rules

- **NOT JSON**: Do not use `{}` with colons and commas like JSON
- **File extension**: Must be `.ui` only
- **Elements**: `Group`, `Label`, `TextField`, `Button`, `TextButton`, `ProgressBar`
- **IDs**: Use `#elementId` after the element type to reference from Java

### Available Properties

| Property | Syntax | Example |
|----------|--------|---------|
| `Background` | Color or texture | `#000000(0.8)` or `"path/texture.png"` |
| `Anchor` | Positioning/size | `(Width: 200, Height: 100, Top: 10)` |
| `Padding` | Spacing | `(Left: 10, Right: 10, Top: 5, Bottom: 5)` |
| `LayoutMode` | Child layout | `Top`, `Left`, `Center`, `TopScrolling` |
| `Style` | Text styling | `(FontSize: 16, TextColor: #fff, RenderBold: true)` |
| `OutlineColor` | Border color | `#93844c(0.5)` |
| `OutlineSize` | Border width | `1.5` |
| `FlexWeight` | Flexible sizing | `1` |
| `Text` | Label content | `"My Text"` |

### Text Centering Pattern

Use `FlexWeight` with empty Labels to center content:

```
Group {
  LayoutMode: Left;

  Label {FlexWeight: 1;}

  Label #CenteredText {
    Style: (FontSize: 16, TextColor: #ffffff);
    Text: "Centered!";
  }

  Label {FlexWeight: 1;}
}
```

### ProgressBar Element

```
Group {
  Anchor: (Height: 8);
  Background: "Common/ProgressBar.png";

  ProgressBar #MyBar {
    BarTexturePath: "Common/ProgressBarFill.png";
    Value: 0.5;
  }
}
```

### Variables & Includes

```
$Common = "../Common.ui";
@MyStyle = PatchStyle(TexturePath: "MyBackground.png");

$Common.@PageOverlay {
  Background: @MyStyle;
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
        // Use just the filename - files must be in Common/UI/Custom/
        commands.append("MachineUI.ui");

        // Use #ElementId.TextSpans to set label text (NOT .Text)
        commands.set("#powerLabel.TextSpans", Message.raw("Power: 100 W"));
    }
}

// To open:
Player player = store.getComponent(ref, Player.getComponentType());
player.getPageManager().openCustomPage(ref, store, new MachineUI(playerRef));
```

### UICommandBuilder Methods

| Method | Usage |
|--------|-------|
| `append(filename)` | Load a .ui file (just filename, not path) |
| `set(selector, Message)` | Set element property using `#ElementId.Property` format |
| `remove(selector)` | Remove an element |
| `clear(selector)` | Clear element contents |

### Common Selectors

| Selector | Description |
|----------|-------------|
| `#LabelId.TextSpans` | Set Label text content (use with `Message.raw()`) |
| `#ProgressBarId.Value` | Set ProgressBar value (0.0 to 1.0) |

### Reference Examples

- **AdminUI Plugin**: https://github.com/Buuz135/AdminUI - Working UI examples

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
