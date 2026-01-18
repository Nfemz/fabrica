---
name: blockbench-texture
description: Create, apply, and manage textures on Blockbench models using the Material Palette approach.
---

# Blockbench Texture - 256x256 Palette System

Create and apply textures to an existing Blockbench model using a 256x256 texture atlas with 16 procedurally generated material regions.

## Overview

Hytale Prop format requires a **single texture**. This skill:
1. **Interviews** the user about their texture needs
2. Creates a 256x256 texture atlas with 16 regions (64x64 each)
3. Generates procedural textures for each region
4. Maps cube UVs to the correct material region

---

## Architecture: The 256x256 Palette

```
256x256 Texture Atlas (4x4 grid of 64x64 regions):

     Col 0      Col 1      Col 2      Col 3
    (0-64)    (64-128)  (128-192)  (192-256)
   ┌──────────┬──────────┬──────────┬──────────┐
R0 │ Slot 0   │ Slot 1   │ Slot 2   │ Slot 3   │  (0-64)
   │          │          │          │          │
   ├──────────┼──────────┼──────────┼──────────┤
R1 │ Slot 4   │ Slot 5   │ Slot 6   │ Slot 7   │  (64-128)
   │          │          │          │          │
   ├──────────┼──────────┼──────────┼──────────┤
R2 │ Slot 8   │ Slot 9   │ Slot 10  │ Slot 11  │  (128-192)
   │          │          │          │          │
   ├──────────┼──────────┼──────────┼──────────┤
R3 │ Slot 12  │ Slot 13  │ Slot 14  │ Slot 15  │  (192-256)
   │          │          │          │          │
   └──────────┴──────────┴──────────┴──────────┘

Each slot: 64x64 pixels - enough for detailed procedural patterns
```

### Slot Coordinates Reference

| Slot | X Start | Y Start | UV Coordinates |
|------|---------|---------|----------------|
| 0 | 0 | 0 | [0, 0, 64, 64] |
| 1 | 64 | 0 | [64, 0, 128, 64] |
| 2 | 128 | 0 | [128, 0, 192, 64] |
| 3 | 192 | 0 | [192, 0, 256, 64] |
| 4 | 0 | 64 | [0, 64, 64, 128] |
| 5 | 64 | 64 | [64, 64, 128, 128] |
| 6 | 128 | 64 | [128, 64, 192, 128] |
| 7 | 192 | 64 | [192, 64, 256, 128] |
| 8 | 0 | 128 | [0, 128, 64, 192] |
| 9 | 64 | 128 | [64, 128, 128, 192] |
| 10 | 128 | 128 | [128, 128, 192, 192] |
| 11 | 192 | 128 | [192, 128, 256, 192] |
| 12 | 0 | 192 | [0, 192, 64, 256] |
| 13 | 64 | 192 | [64, 192, 128, 256] |
| 14 | 128 | 192 | [128, 192, 192, 256] |
| 15 | 192 | 192 | [192, 192, 256, 256] |

---

## Phase 1: Interview User

Use `AskUserQuestion` to determine palette contents. Ask about:

### Question 1: Visual Style
- Industrial/mechanical
- Fantasy/magical
- Natural/organic
- Sci-fi/futuristic
- Mixed

### Question 2: Primary Materials Needed
Based on the model elements, ask which material types they need:
- **Metals**: Steel, copper, brass, bronze, dark iron, rust
- **Surfaces**: Riveted panels, vents/slats, grates, pipes
- **Controls**: Button panels, gauges, switches, displays
- **Effects**: Glow, fire, warning stripes, hazard markings
- **Natural**: Wood, stone, brick, fabric

### Question 3: Color Palette Preference
- Cool tones (grays, blues, silvers)
- Warm tones (coppers, oranges, browns)
- Dark/gritty
- Bright/clean
- Custom colors

### Question 4: Specific Slots
For each slot (0-15), confirm what texture should go there, or use defaults.

---

## Phase 2: Create Atlas Texture

### 2.1 Create Base Texture

```
mcp__blockbench__create_texture({
  name: "palette",
  width: 256,
  height: 256,
  fill_color: "#808080"
})
```

### 2.2 CRITICAL: Resize Canvas (MCP Bug Workaround)

**BUG**: The `create_texture` MCP tool creates 16x16 textures regardless of requested size.

```javascript
const SIZE = 256;
const tex = Texture.all.find(t => t.name.includes('palette'));
const newCanvas = document.createElement('canvas');
newCanvas.width = SIZE;
newCanvas.height = SIZE;
const newCtx = newCanvas.getContext('2d');
newCtx.fillStyle = '#808080';
newCtx.fillRect(0, 0, SIZE, SIZE);
tex.canvas = newCanvas;
tex.ctx = newCtx;
tex.width = SIZE;
tex.height = SIZE;
tex.uv_width = SIZE;
tex.uv_height = SIZE;
Project.texture_width = SIZE;
Project.texture_height = SIZE;
tex.updateSource(newCanvas.toDataURL());
'Canvas resized to 256x256'
```

---

## Phase 3: Generate Textures

### Available Pattern Generators

#### Brushed Metal
```javascript
function brushedMetal(x, y, w, h, r, g, b) {
  for (let row = 0; row < h; row++) {
    const v = Math.floor(Math.random() * 20) - 10;
    ctx.fillStyle = 'rgb('+Math.max(0,Math.min(255,r+v))+','+Math.max(0,Math.min(255,g+v))+','+Math.max(0,Math.min(255,b+v))+')';
    ctx.fillRect(x, y + row, w, 1);
  }
  ctx.fillStyle = 'rgb('+(r+20)+','+(g+20)+','+(b+20)+')';
  ctx.fillRect(x, y, w, 2);
  ctx.fillRect(x, y, 2, h);
  ctx.fillStyle = 'rgb('+Math.max(0,r-20)+','+Math.max(0,g-20)+','+Math.max(0,b-20)+')';
  ctx.fillRect(x+w-2, y, 2, h);
  ctx.fillRect(x, y+h-2, w, 2);
}
```

#### Vent Slats
```javascript
function ventSlats(x, y, w, h, metalR, metalG, metalB) {
  ctx.fillStyle = '#1a1a1a';
  ctx.fillRect(x, y, w, h);
  const slatHeight = 6;
  const gapHeight = 4;
  for (let row = 2; row < h - 2; row += slatHeight + gapHeight) {
    for (let col = 0; col < w; col++) {
      const v = Math.floor(Math.random() * 15) - 7;
      ctx.fillStyle = 'rgb('+Math.max(0,metalR+v)+','+Math.max(0,metalG+v)+','+Math.max(0,metalB+v)+')';
      ctx.fillRect(x + col, y + row, 1, slatHeight);
    }
    ctx.fillStyle = 'rgb('+(metalR+25)+','+(metalG+25)+','+(metalB+25)+')';
    ctx.fillRect(x, y + row, w, 1);
    ctx.fillStyle = 'rgb('+Math.max(0,metalR-25)+','+Math.max(0,metalG-25)+','+Math.max(0,metalB-25)+')';
    ctx.fillRect(x, y + row + slatHeight - 1, w, 1);
  }
}
```

#### Button Panel
```javascript
function buttonPanel(x, y, w, h, baseR, baseG, baseB, cols, rows, btnColors) {
  ctx.fillStyle = 'rgb('+baseR+','+baseG+','+baseB+')';
  ctx.fillRect(x, y, w, h);
  const btnSize = 12;
  const btnGap = (w - cols * btnSize) / (cols + 1);
  const rowGap = (h - rows * btnSize) / (rows + 1);
  for (let r = 0; r < rows; r++) {
    for (let c = 0; c < cols; c++) {
      const bx = x + btnGap + c * (btnSize + btnGap);
      const by = y + rowGap + r * (btnSize + rowGap);
      ctx.fillStyle = '#222222';
      ctx.fillRect(bx - 1, by - 1, btnSize + 2, btnSize + 2);
      ctx.fillStyle = btnColors[(r * cols + c) % btnColors.length];
      ctx.fillRect(bx, by, btnSize, btnSize);
      ctx.fillStyle = 'rgba(255,255,255,0.3)';
      ctx.fillRect(bx, by, btnSize, 2);
      ctx.fillRect(bx, by, 2, btnSize);
      ctx.fillStyle = 'rgba(0,0,0,0.3)';
      ctx.fillRect(bx, by + btnSize - 2, btnSize, 2);
      ctx.fillRect(bx + btnSize - 2, by, 2, btnSize);
    }
  }
}
```

#### Riveted Panel
```javascript
function rivetPanel(x, y, w, h, r, g, b, rivetSpacing) {
  for (let row = 0; row < h; row++) {
    const v = Math.floor(Math.random() * 15) - 7;
    ctx.fillStyle = 'rgb('+Math.max(0,r+v)+','+Math.max(0,g+v)+','+Math.max(0,b+v)+')';
    ctx.fillRect(x, y + row, w, 1);
  }
  ctx.fillStyle = 'rgb('+(r+20)+','+(g+20)+','+(b+20)+')';
  ctx.fillRect(x, y, w, 3);
  ctx.fillRect(x, y, 3, h);
  ctx.fillStyle = 'rgb('+Math.max(0,r-20)+','+Math.max(0,g-20)+','+Math.max(0,b-20)+')';
  ctx.fillRect(x+w-3, y, 3, h);
  ctx.fillRect(x, y+h-3, w, 3);
  for (let ry = rivetSpacing; ry < h - 4; ry += rivetSpacing) {
    for (let rx = rivetSpacing; rx < w - 4; rx += rivetSpacing) {
      ctx.fillStyle = 'rgb('+Math.max(0,r-30)+','+Math.max(0,g-30)+','+Math.max(0,b-30)+')';
      ctx.beginPath();
      ctx.arc(x + rx, y + ry, 3, 0, Math.PI * 2);
      ctx.fill();
      ctx.fillStyle = 'rgb('+(r+10)+','+(g+10)+','+(b+10)+')';
      ctx.beginPath();
      ctx.arc(x + rx - 1, y + ry - 1, 1, 0, Math.PI * 2);
      ctx.fill();
    }
  }
}
```

#### Gauge/Dial
```javascript
function gaugeTexture(x, y, w, h) {
  ctx.fillStyle = '#1a1a1a';
  ctx.fillRect(x, y, w, h);
  const cx = x + w/2;
  const cy = y + h/2;
  const radius = Math.min(w, h) / 2 - 4;
  ctx.fillStyle = '#333333';
  ctx.beginPath();
  ctx.arc(cx, cy, radius, 0, Math.PI * 2);
  ctx.fill();
  ctx.fillStyle = '#111111';
  ctx.beginPath();
  ctx.arc(cx, cy, radius - 3, 0, Math.PI * 2);
  ctx.fill();
  for (let i = 0; i < 10; i++) {
    const angle = (Math.PI * 0.75) + (i / 9) * (Math.PI * 1.5);
    const x1 = cx + Math.cos(angle) * (radius - 8);
    const y1 = cy + Math.sin(angle) * (radius - 8);
    const x2 = cx + Math.cos(angle) * (radius - 4);
    const y2 = cy + Math.sin(angle) * (radius - 4);
    ctx.strokeStyle = '#aaaaaa';
    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2, y2);
    ctx.stroke();
  }
  const needleAngle = Math.PI * 1.2;
  ctx.strokeStyle = '#ff3333';
  ctx.lineWidth = 2;
  ctx.beginPath();
  ctx.moveTo(cx, cy);
  ctx.lineTo(cx + Math.cos(needleAngle) * (radius - 12), cy + Math.sin(needleAngle) * (radius - 12));
  ctx.stroke();
}
```

#### Glow/Emissive
```javascript
function glowTexture(x, y, w, h, r, g, b) {
  for (let py = 0; py < h; py++) {
    for (let px = 0; px < w; px++) {
      const dx = px - w/2;
      const dy = py - h/2;
      const dist = Math.sqrt(dx*dx + dy*dy) / (w/2);
      const intensity = Math.max(0, 1 - dist * 0.7);
      const flicker = Math.random() * 0.15;
      const fr = Math.min(255, Math.floor(r * (intensity + flicker)));
      const fg = Math.min(255, Math.floor(g * (intensity + flicker)));
      const fb = Math.min(255, Math.floor(b * (intensity + flicker)));
      ctx.fillStyle = 'rgb('+fr+','+fg+','+fb+')';
      ctx.fillRect(x + px, y + py, 1, 1);
    }
  }
}
```

#### Warning Stripes
```javascript
function warningStripes(x, y, w, h) {
  ctx.fillStyle = '#222222';
  ctx.fillRect(x, y, w, h);
  const stripeW = 12;
  ctx.fillStyle = '#ffcc00';
  for (let i = -h; i < w + h; i += stripeW * 2) {
    ctx.beginPath();
    ctx.moveTo(x + i, y + h);
    ctx.lineTo(x + i + stripeW, y + h);
    ctx.lineTo(x + i + h + stripeW, y);
    ctx.lineTo(x + i + h, y);
    ctx.closePath();
    ctx.fill();
  }
}
```

#### Metal Grate
```javascript
function grateTexture(x, y, w, h, gridSize) {
  ctx.fillStyle = '#0a0a0a';
  ctx.fillRect(x, y, w, h);
  const barWidth = 2;
  ctx.fillStyle = '#3a3a3a';
  for (let gx = 0; gx < w; gx += gridSize) {
    ctx.fillRect(x + gx, y, barWidth, h);
  }
  for (let gy = 0; gy < h; gy += gridSize) {
    ctx.fillRect(x, y + gy, w, barWidth);
  }
  ctx.fillStyle = '#4a4a4a';
  for (let gx = 0; gx < w; gx += gridSize) {
    ctx.fillRect(x + gx, y, 1, h);
  }
}
```

#### Pipe/Cylinder
```javascript
function pipeTexture(x, y, w, h, r, g, b) {
  for (let py = 0; py < h; py++) {
    const curve = Math.sin((py / h) * Math.PI);
    const shade = Math.floor(curve * 40) - 20;
    for (let px = 0; px < w; px++) {
      const v = Math.floor(Math.random() * 10) - 5;
      const fr = Math.max(0, Math.min(255, r + shade + v));
      const fg = Math.max(0, Math.min(255, g + shade + v));
      const fb = Math.max(0, Math.min(255, b + shade + v));
      ctx.fillStyle = 'rgb('+fr+','+fg+','+fb+')';
      ctx.fillRect(x + px, y + py, 1, 1);
    }
  }
  ctx.fillStyle = 'rgb('+(r+30)+','+(g+30)+','+(b+30)+')';
  ctx.fillRect(x, y, w, 4);
  ctx.fillRect(x, y + h - 4, w, 4);
}
```

### Material Color Presets

| Material | R | G | B | Hex |
|----------|---|---|---|-----|
| steel | 74 | 74 | 82 | #4a4a52 |
| copper | 184 | 115 | 51 | #b87333 |
| dark_iron | 42 | 42 | 48 | #2a2a30 |
| brass | 181 | 166 | 66 | #b5a642 |
| bronze | 140 | 120 | 83 | #8c7853 |
| rust | 183 | 65 | 14 | #b7410e |
| light_steel | 100 | 100 | 110 | #64646e |

---

## Phase 4: Apply UVs to Cubes

### Define Palette Mapping

Create a mapping from element names to slot numbers:

```javascript
const atlas = Texture.all.find(t => t.name.includes('palette'));
const SLOT = 64;

function getSlotUV(slot) {
  const col = slot % 4;
  const row = Math.floor(slot / 4);
  return [col * SLOT, row * SLOT, (col + 1) * SLOT, (row + 1) * SLOT];
}

const materialMap = {
  steel: 0,
  copper: 1,
  dark_iron: 2,
  light_steel: 3,
  vent: 4,
  buttons: 5,
  riveted: 6,
  glow_warm: 7,
  pipe: 8,
  trim: 9,
  glow_hot: 10,
  gauge: 11,
  switch_on: 12,
  switch_off: 13,
  warning: 14,
  grate: 15
};

function getMaterial(name) {
  const n = name.toLowerCase();
  if (n.includes('vent')) return 'vent';
  if (n.includes('grate')) return 'grate';
  if (n.includes('gauge') || n.includes('dial')) return 'gauge';
  if (n.includes('switch')) return 'switch_on';
  if (n.includes('button') || n.includes('control_panel')) return 'buttons';
  if (n.includes('pipe')) return 'pipe';
  if (n.includes('glow') || n.includes('fire')) return 'glow_hot';
  if (n.includes('warning') || n.includes('hazard')) return 'warning';
  if (n.includes('trim')) return 'trim';
  if (n.includes('base') || n.includes('frame') || n.includes('corner')) return 'dark_iron';
  if (n.includes('body') || n.includes('panel')) return 'riveted';
  return 'steel';
}

Cube.all.forEach(cube => {
  const mat = getMaterial(cube.name);
  const slot = materialMap[mat] || 0;
  const uv = getSlotUV(slot);
  Object.keys(cube.faces).forEach(face => {
    cube.faces[face].texture = atlas.uuid;
    cube.faces[face].uv = [...uv];
  });
});

Canvas.updateAllFaces();
```

---

## Phase 5: Refresh and Verify

```javascript
const tex = Texture.all[0];
tex.updateSource(tex.canvas.toDataURL());
tex.updateMaterial();
Canvas.updateAllFaces();
```

Take screenshots from multiple angles to verify.

---

## Example Industrial Palette

A typical industrial/mechanical palette:

| Slot | Texture | Use For |
|------|---------|---------|
| 0 | Brushed steel | Default surfaces |
| 1 | Brushed copper | Accent metal |
| 2 | Dark iron | Structural frames |
| 3 | Light steel | Secondary panels |
| 4 | Vent slats | Air vents |
| 5 | Button panel | Control interfaces |
| 6 | Riveted panel | Main body panels |
| 7 | Warm glow | Lights, indicators |
| 8 | Copper pipe | Piping, tubes |
| 9 | Dark trim | Edge trim, borders |
| 10 | Hot glow | Fire, heat sources |
| 11 | Gauge face | Dials, meters |
| 12 | Switch ON | Toggle switches |
| 13 | Switch OFF | Toggle switches |
| 14 | Warning stripes | Hazard markings |
| 15 | Metal grate | Floors, walkways |

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| All cubes same texture | Re-run UV mapping code |
| Texture not updating | Call `tex.updateSource(tex.canvas.toDataURL())` |
| "Resolution does not match UV size" | Set BOTH `tex.uv_width/height` AND `Project.texture_width/height` to 256 |
| Preview wrong after changes | Call `tex.updateMaterial()` then `Canvas.updateAllFaces()` |
| `create_texture` ignores size | Known MCP bug - always resize canvas manually |

---

## DO NOT

- Use multiple textures (Hytale requires single texture)
- Use `apply_texture` MCP tool (broken)
- Click Validator "Fix UV Size" button (destroys all UV assignments)
- Forget to resize canvas after `create_texture`
- Apply detailed textures (like button panels) to tiny elements - they won't be visible
