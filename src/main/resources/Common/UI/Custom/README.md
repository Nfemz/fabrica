# Fabrica UI Templates

This folder contains GUI layout templates for Fabrica machines.

## Important Note

These `.ui.json` files are **templates** that will need to be converted to Hytale's actual UI format once it's fully documented. The structure follows common game UI patterns.

## Current Templates

| File | Purpose |
|------|---------|
| `GeneratorGUI.ui.json` | Solid Fuel Generator interface |
| `BatteryGUI.ui.json` | Battery storage interface |
| `ProcessingGUI.ui.json` | Macerator and Electric Furnace interface |

## Template Structure

```json
{
  "id": "unique_gui_id",
  "title": "Display Title",
  "width": 176,
  "height": 166,
  "background": { ... },
  "elements": [ ... ],
  "dataBindings": { ... }
}
```

### Element Types

- `label` - Static text
- `dynamic_label` - Text that updates from machine data
- `slot` - Inventory slot for items
- `progress_bar` - Animated bar showing progress/energy
- `indicator` - On/off light indicator
- `player_inventory` - Player's inventory grid

### Data Bindings

Data bindings connect UI elements to machine properties:

```json
"dataBindings": {
  "progress": {
    "source": "machine",
    "property": "progress",
    "type": "float",
    "range": [0.0, 1.0]
  }
}
```

## PNG Assets

Located in this same folder:

| File | Size | Purpose |
|------|------|---------|
| `MachineGUI_Background.png` | 256x166 | Main GUI background |
| `ProgressArrow_Empty.png` | 24x16 | Empty progress arrow |
| `ProgressArrow_Full.png` | 24x16 | Filled progress arrow |
| `BurnProgress_Empty.png` | 16x16 | Empty burn indicator |
| `BurnProgress_Full.png` | 16x16 | Burning flame |
| `EnergyBar_Empty.png` | 16x52 | Empty energy bar |
| `EnergyBar_Full.png` | 16x52 | Full energy bar |
| `PowerIndicator_Off.png` | 8x8 | Power LED off |
| `PowerIndicator_On.png` | 8x8 | Power LED on |
| `Slot_Background.png` | 18x18 | Item slot frame |

## Converting to Hytale Format

When Hytale's Pages system is documented:

1. Check Hytale's UI schema/format
2. Convert the JSON structure to match
3. Update texture paths if needed
4. Register GUIs in `MachineUIHandler.java`
5. Connect to machine `onInteract()` methods

## References

- [Hytale Modding Documentation](https://britakee-studios.gitbook.io/hytale-modding-documentation)
- [HytaleDocs Community Wiki](https://hytale-docs.com/)
