# Fabrica Asset Generation TODO

This document tracks visual assets that need to be created by image-generation LLMs or artists. Each asset includes specifications for the generation prompt.

## Status Legend
- [ ] Not started
- [P] Placeholder exists (colored square)
- [X] Final asset complete

---

## 1. Block Textures

Location: `src/main/resources/Common/BlockTextures/`
Format: PNG, 32x32 pixels (or 16x16 for simple blocks)
Style: Industrial/steampunk factory aesthetic

### Ores

| File | Status | Description | Generation Prompt |
|------|--------|-------------|-------------------|
| `Tin_Ore.png` | [P] | Tin ore block | "Minecraft-style ore texture, 32x32 pixels, gray stone with silvery-white tin metal veins, industrial look" |
| `Copper_Ore.png` | [P] | Copper ore block | "Minecraft-style ore texture, 32x32 pixels, gray stone with orange-brown copper metal veins, oxidized green hints" |

### Machines

| File | Status | Description | Generation Prompt |
|------|--------|-------------|-------------------|
| `Machine_Generator_Front.png` | [P] | Generator front face | "Industrial generator front panel, 32x32 pixels, metal casing with coal intake slot, orange glow indicating fire inside, steampunk style, rivets and bolts" |
| `Machine_Generator_Side.png` | [P] | Generator side | "Industrial machine side panel, 32x32 pixels, metal plating with rivets, exhaust vents, steampunk factory style" |
| `Machine_Generator_Top.png` | [P] | Generator top | "Industrial machine top, 32x32 pixels, metal grating with smoke/heat vents, steampunk style" |
| `Machine_Battery_Front.png` | [P] | Battery front face | "Industrial battery front, 32x32 pixels, metal casing with energy level indicator (glowing bars), electrical terminals, copper accents" |
| `Machine_Battery_Side.png` | [P] | Battery side | "Industrial battery side panel, 32x32 pixels, metal with copper coils visible, insulated sections" |
| `Machine_Macerator_Front.png` | [P] | Macerator front | "Industrial grinder/crusher front, 32x32 pixels, metal casing with grinding wheel visible, input hopper, dust output, steampunk" |
| `Machine_Macerator_Top.png` | [P] | Macerator top | "Industrial grinder top view, 32x32 pixels, circular grinding mechanism, metal housing" |
| `Machine_Electric_Furnace_Front.png` | [P] | Electric furnace front | "Electric smelter front, 32x32 pixels, metal casing with heating coils visible (orange glow), industrial door, temperature gauge" |
| `Machine_Electric_Furnace_Side.png` | [P] | Electric furnace side | "Electric smelter side, 32x32 pixels, insulated metal panels, heat vents, industrial style" |
| `Machine_Cable.png` | [P] | Power cable | "Industrial power cable, 32x32 pixels, copper wire bundle with rubber/cloth insulation, connectors on ends, can tile seamlessly" |
| `Conveyor_Belt.png` | [P] | Conveyor belt | "Factory conveyor belt top-down, 32x32 pixels, rubber belt with metal rollers on sides, directional arrows, industrial" |

---

## 2. Item Icons

Location: `src/main/resources/Common/Icons/ItemsGenerated/`
Format: PNG, 32x32 pixels with transparency
Style: Clean item icons, slight 3D shading

### Raw Materials

| File | Status | Description | Generation Prompt |
|------|--------|-------------|-------------------|
| `Tin_Ore.png` | [P] | Tin ore item | "Item icon, 32x32, transparent background, chunk of gray rock with silvery tin metal, Minecraft style" |
| `Copper_Ore.png` | [P] | Copper ore item | "Item icon, 32x32, transparent background, chunk of rock with orange-copper metal veins, Minecraft style" |

### Dusts

| File | Status | Description | Generation Prompt |
|------|--------|-------------|-------------------|
| `Iron_Dust.png` | [P] | Iron dust pile | "Item icon, 32x32, transparent background, small pile of dark gray metallic powder/dust, Minecraft style" |
| `Copper_Dust.png` | [P] | Copper dust pile | "Item icon, 32x32, transparent background, small pile of orange-brown metallic powder, Minecraft style" |
| `Tin_Dust.png` | [P] | Tin dust pile | "Item icon, 32x32, transparent background, small pile of light silvery-gray powder, Minecraft style" |
| `Bronze_Dust.png` | [P] | Bronze dust pile | "Item icon, 32x32, transparent background, small pile of golden-brown metallic powder, Minecraft style" |

### Ingots

| File | Status | Description | Generation Prompt |
|------|--------|-------------|-------------------|
| `Tin_Ingot.png` | [P] | Tin ingot | "Item icon, 32x32, transparent background, shiny silver-white metal ingot bar, Minecraft style" |
| `Copper_Ingot.png` | [P] | Copper ingot | "Item icon, 32x32, transparent background, shiny orange-copper metal ingot bar, Minecraft style" |
| `Bronze_Ingot.png` | [P] | Bronze ingot | "Item icon, 32x32, transparent background, shiny golden-brown bronze metal ingot bar, Minecraft style" |

### Machine Items (for inventory)

| File | Status | Description | Generation Prompt |
|------|--------|-------------|-------------------|
| `Machine_Generator.png` | [P] | Generator item | "Item icon, 32x32, transparent background, miniature industrial generator box with coal slot and smoke stack, steampunk" |
| `Machine_Battery.png` | [P] | Battery item | "Item icon, 32x32, transparent background, industrial battery box with energy indicator and terminals, steampunk" |
| `Machine_Macerator.png` | [P] | Macerator item | "Item icon, 32x32, transparent background, miniature industrial grinder/crusher machine, steampunk" |
| `Machine_Electric_Furnace.png` | [P] | Electric furnace item | "Item icon, 32x32, transparent background, miniature electric smelter with heating coils, steampunk" |
| `Machine_Cable.png` | [P] | Cable item | "Item icon, 32x32, transparent background, coiled copper power cable with insulation, industrial" |
| `Conveyor_Belt.png` | [P] | Conveyor item | "Item icon, 32x32, transparent background, rolled up conveyor belt segment, industrial" |

---

## 3. GUI Elements

Location: `src/main/resources/Common/UI/Custom/`
Format: PNG for sprites, `.ui` for layouts
Style: Industrial/steampunk control panel aesthetic

### GUI Backgrounds

| File | Status | Description | Generation Prompt |
|------|--------|-------------|-------------------|
| `MachineGUI_Background.png` | [P] | Base machine GUI | "Game UI background, 256x166 pixels, industrial metal panel with rivets, darker slot areas, steampunk control panel style" |
| `GeneratorGUI_Background.png` | [ ] | Generator-specific GUI | "Game UI, 256x166, industrial generator control panel, fuel slot area, burn progress area, power output display, steampunk gauges" |
| `BatteryGUI_Background.png` | [ ] | Battery-specific GUI | "Game UI, 256x166, industrial battery control panel, large energy bar area, charge/discharge indicators, steampunk style" |
| `ProcessingGUI_Background.png` | [ ] | Macerator/Furnace GUI | "Game UI, 256x166, industrial processing machine panel, input slot, output slot, progress arrow area, power indicator" |

### GUI Sprites/Elements

| File | Status | Description | Generation Prompt |
|------|--------|-------------|-------------------|
| `ProgressArrow_Empty.png` | [P] | Empty progress arrow | "UI element, 24x16, transparent, empty industrial arrow outline pointing right, metallic style" |
| `ProgressArrow_Full.png` | [P] | Filled progress arrow | "UI element, 24x16, transparent, filled industrial arrow pointing right, glowing orange/yellow, metallic" |
| `BurnProgress_Empty.png` | [P] | Empty burn indicator | "UI element, 16x16, transparent, empty flame/fire outline, industrial gauge style" |
| `BurnProgress_Full.png` | [P] | Burning indicator | "UI element, 16x16, transparent, burning flame icon, orange and yellow fire" |
| `EnergyBar_Empty.png` | [P] | Empty energy bar | "UI element, 16x52, transparent, empty vertical bar with tick marks, industrial battery gauge" |
| `EnergyBar_Full.png` | [P] | Full energy bar | "UI element, 16x52, transparent, filled vertical bar glowing blue/cyan, energy indicator" |
| `PowerIndicator_Off.png` | [P] | Power off indicator | "UI element, 8x8, transparent, dark/gray circle, LED indicator off" |
| `PowerIndicator_On.png` | [P] | Power on indicator | "UI element, 8x8, transparent, glowing green circle, LED indicator on" |
| `Slot_Background.png` | [P] | Inventory slot | "UI element, 18x18, transparent, industrial metal slot frame with dark center, riveted corners" |

---

## 4. Animation Frames (Optional/Future)

These are optional animated textures for machines when active.

| Asset | Frames | Description |
|-------|--------|-------------|
| `Generator_Active_*.png` | 4 frames | Flickering fire glow animation |
| `Macerator_Active_*.png` | 4 frames | Spinning grinder animation |
| `Furnace_Active_*.png` | 4 frames | Heating coil glow pulse |
| `Conveyor_Active_*.png` | 4 frames | Belt movement animation |

---

## 5. Batch Generation Instructions

When generating these assets with an image AI:

### For Block Textures:
```
Base prompt additions:
- "seamless tileable texture"
- "pixel art style" or "voxel game style"
- "32x32 pixels, no anti-aliasing on edges"
- "GregTech/IndustrialCraft inspired"
```

### For Item Icons:
```
Base prompt additions:
- "item icon on transparent background"
- "32x32 pixels"
- "slight 3D shading, isometric view"
- "clean edges, game inventory icon"
```

### For GUI Elements:
```
Base prompt additions:
- "game UI element"
- "transparent background where needed"
- "steampunk industrial control panel style"
- "muted metal colors with accent glows"
```

---

## 6. Color Palette Reference

For consistency across all assets:

| Material | Primary Color | Accent Color | Hex Codes |
|----------|--------------|--------------|-----------|
| Tin | Silver-white | Light gray | #C0C0C0, #A0A0A0 |
| Copper | Orange-brown | Oxidized green | #B87333, #4A7C59 |
| Bronze | Golden-brown | Dark bronze | #CD7F32, #8B4513 |
| Iron | Dark gray | Rust hints | #696969, #8B4513 |
| Machine Casing | Steel gray | Rivet bronze | #708090, #8B7355 |
| Energy/Power | Cyan/Blue | Bright glow | #00FFFF, #00BFFF |
| Heat/Fire | Orange | Yellow core | #FF6600, #FFCC00 |
| Cable Copper | Copper orange | Insulation black | #B87333, #2F2F2F |

---

## 7. File Checklist Summary

### Must Have (MVP):
- [ ] 2 ore block textures
- [ ] 6 machine block textures (at minimum front faces)
- [ ] 1 cable texture
- [ ] 1 conveyor texture
- [ ] 6 dust/ingot item icons
- [ ] 6 machine item icons
- [ ] 1 base GUI background
- [ ] Basic GUI sprites (progress, energy, slots)
- [ ] Translation file (server.lang) ✓

### Nice to Have:
- [ ] Multi-face machine textures (top, side, front)
- [ ] Machine-specific GUI backgrounds
- [ ] Animation frames
- [ ] Particle effects

---

## 8. Handoff Notes

When requesting assets from image-generation LLMs:

1. **Request one category at a time** - e.g., "all dust icons" or "all machine front textures"
2. **Provide the color palette** - Include hex codes for consistency
3. **Specify exact dimensions** - 32x32, 16x16, etc.
4. **Request transparent backgrounds** for items and GUI elements
5. **Ask for tileable versions** for block textures
6. **Save as PNG** - Preserves transparency and pixel accuracy

### Example Batch Request:
```
Generate 4 item icons for metal dusts in a Minecraft/voxel game style:
- Iron Dust: dark gray metallic powder pile
- Copper Dust: orange-brown metallic powder pile
- Tin Dust: light silver-gray powder pile
- Bronze Dust: golden-brown metallic powder pile

Specifications:
- 32x32 pixels each
- Transparent background
- Slight 3D shading, viewed from above at angle
- Clean pixel art edges
- Color palette: Iron #696969, Copper #B87333, Tin #C0C0C0, Bronze #CD7F32
```
