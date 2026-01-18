---
name: blockbench-create-model
description: Create a complete, fully-textured 3D model in Blockbench through an interactive interview process.
---

# Create 3D Model (Geometry Only)

Build a 3D model's geometry in Blockbench. This skill creates the shape - use `blockbench-texture` afterward to add materials.

## Overview

1. **Interview** the user about the model
2. **Create** the project (Hytale Prop format)
3. **Build** geometry (cubes and groups)
4. **Verify** with screenshots
5. **Hand off** to texture skill

---

## Phase 1: Interview

Use `AskUserQuestion` to gather:

### Required Information

| Question | Example Answer |
|----------|----------------|
| What is this model? | "A steam-powered generator" |
| Model name? | "fabrica_generator" |
| Approximate size? | "16x16x16 blocks" |
| Main components? | "base, body, chimney, control panel, vents" |
| Detail level? | Simple (5-10), Medium (10-25), Detailed (25+) |

### Component Naming Convention

Name components by their intended material for easy texturing later:

| Prefix/Contains | Intended Material |
|-----------------|-------------------|
| `base_`, `frame_`, `trim_` | dark_iron |
| `body_`, `panel_`, `vent_` | steel |
| `dial_`, `switch_`, `pipe_` | copper |
| `fire_`, `glow_`, `heat_` | fire/emissive |

---

## Phase 2: Create Project

```
mcp__blockbench__create_project({
  name: "model_name",
  format: "hytale_prop"
})
```

**Important**: Close any other open projects first (single project rule).

---

## Phase 3: Build Geometry

### 3.1 Create Root Group

```
mcp__blockbench__add_group({
  name: "root",
  origin: [0, 0, 0],
  rotation: [0, 0, 0]
})
```

### 3.2 Build Order

1. **Base/frame** - Bottom structural elements
2. **Main body** - Largest central volumes
3. **Secondary shapes** - Attached components
4. **Detail pieces** - Small trim, accents, controls

### 3.3 Place Cubes

```
mcp__blockbench__place_cube({
  elements: [
    { name: "base_plate", from: [-8, 0, -8], to: [8, 2, 8] },
    { name: "body_main", from: [-6, 2, -6], to: [6, 12, 6] }
  ],
  group: "root"
})
```

### Geometry Guidelines

| Guideline | Details |
|-----------|---------|
| Coordinate system | Y is up, centered on origin |
| `from` / `to` | Min corner to max corner |
| Naming | Use material-indicative names |
| Batch placement | Place multiple cubes in one call when possible |

### 3.4 Add Detail

For medium/detailed models, add:
- **Corner posts** - Small vertical cubes at corners
- **Trim pieces** - Thin horizontal cubes at edges
- **Vents/grills** - Repeating thin cubes
- **Control panels** - Recessed or protruding sections
- **Pipes/tubes** - Cylindrical arrangements of cubes

---

## Phase 4: Verify Geometry

### Take Screenshots

```
mcp__blockbench__set_camera_angle({
  position: [30, 20, 30],
  target: [0, 8, 0],
  projection: "perspective"
})

mcp__blockbench__capture_screenshot()
```

### Check from Multiple Angles

- Front: `position: [0, 10, 30]`
- Side: `position: [30, 10, 0]`
- Top: `position: [0, 40, 0]`

### Verify Element List

```
mcp__blockbench__list_outline()
```

Confirm all components are created with correct names.

---

## Phase 5: Hand Off to Texturing

After geometry is complete, inform the user:

> "Model geometry is complete with X elements. Use `/blockbench-texture` to create and apply materials."

Provide a summary of element names grouped by intended material:
- **Dark iron**: base_plate, frame_front, trim_bottom...
- **Steel**: body_main, panel_left, vent_1...
- **Copper**: dial_1, switch_2, pipe_inlet...

---

## DO NOT

- Create textures (that's the texture skill's job)
- Apply materials or set UVs
- Use the `texture` parameter in `place_cube`

## Quick Reference

| Tool | Purpose |
|------|---------|
| `create_project` | Start new Hytale Prop project |
| `add_group` | Create bone/group hierarchy |
| `place_cube` | Add geometry |
| `modify_cube` | Adjust existing cube |
| `list_outline` | Verify element names |
| `capture_screenshot` | Visual verification |
