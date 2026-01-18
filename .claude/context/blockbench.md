# Blockbench MCP - Atlas-Based Texturing for Hytale

Reference for using Blockbench MCP tools with Hytale models. Based on tested behaviors as of January 2025.

---

## CRITICAL: Hytale = Single Texture

**Hytale Prop format uses `singleTexture: true`.** You cannot have multiple textures per model.

### The Solution: Texture Atlas

Use ONE 32x32 texture with color regions, map cube UVs to the correct region.

```
32x32 Texture Atlas:
┌───────────────┬───────────────┐
│   steel       │   copper      │
│   (0,0)       │   (16,0)      │
├───────────────┼───────────────┤
│   dark_iron   │   fire        │
│   (0,16)      │   (16,16)     │
└───────────────┴───────────────┘
```

---

## What Works vs What Doesn't

### Works

| Feature | Method |
|---------|--------|
| Create texture (16x16 only) | `create_texture(name="atlas")` - **ignores width/height params** |
| Resize to 32x32 | `risky_eval` → manually create new canvas (see below) |
| Set UV coordinates | `risky_eval` → `cube.faces[face].uv = [x1,y1,x2,y2]` |
| Procedural textures | `risky_eval` → `tex.ctx` (Canvas 2D API) |
| Refresh preview | `tex.updateSource()` + `tex.updateMaterial()` + `Canvas.updateAllFaces()` |
| Set UV size | `risky_eval` → Set BOTH `Project.texture_width` AND `tex.uv_width` |

### Does NOT Work

| Feature | Issue |
|---------|-------|
| `create_texture` width/height | **BUG: Always creates 16x16** regardless of params |
| Multiple textures | Hytale format is single-texture only |
| `apply_texture` tool | Bug - applies to ALL elements |
| `texture` param in `place_cube` | Ignored |
| Validator "Fix UV Size" button | **Destroys all texture assignments** |
| MCP UV tools (`modify_cube` uv_offset) | Ignored in per-face UV mode |

### Canvas Resize Workaround

The `create_texture` MCP tool always creates 16x16 textures. To get larger sizes (32x32, 64x64, 128x128, etc.):

```javascript
const SIZE = 128;  // Can be 32, 64, 128, etc.
const tex = Texture.all.find(t => t.name.includes('atlas'));
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
tex.uv_width = SIZE;   // CRITICAL for Hytale format
tex.uv_height = SIZE;  // CRITICAL for Hytale format
Project.texture_width = SIZE;
Project.texture_height = SIZE;
tex.updateSource(newCanvas.toDataURL());
```

**Larger textures = higher fidelity**:
- 32x32: 4 regions of 16x16 (basic)
- 64x64: 4 regions of 32x32 (good detail)
- 128x128: 4 regions of 64x64 (high detail) or 16 regions of 32x32 (more variety)

**Without canvas resize**: Only the top-left 16x16 region works.

---

## The Two-Skill Workflow

### 1. `blockbench-create-model` - Geometry Only

- Interview user about design
- Create project (hytale_prop format)
- Build cubes with material-indicative names:
  - `base_*`, `frame_*`, `trim_*` → dark_iron
  - `body_*`, `panel_*`, `vent_*` → steel
  - `dial_*`, `switch_*`, `pipe_*` → copper
  - `fire_*`, `glow_*` → emissive

### 2. `blockbench-texture` - Apply Materials

- Create 32x32 atlas texture
- Set `Project.texture_width/height = 32`
- Generate procedural materials via Canvas 2D API
- Map cube UVs to atlas regions based on naming
- Refresh preview

---

## Procedural Texture Generation

Access texture's Canvas 2D context via `risky_eval`:

```javascript
const tex = Texture.all.find(t => t.name.includes('atlas'));
const ctx = tex.ctx;

// Brushed metal with edge highlights
function brushedMetal(x, y, w, h, r, g, b) {
  for (let row = 0; row < h; row++) {
    const v = Math.floor(Math.random() * 25) - 12;
    ctx.fillStyle = 'rgb('+(r+v)+','+(g+v)+','+(b+v)+')';
    ctx.fillRect(x, y + row, w, 1);
  }
  // Highlight edges
  ctx.fillStyle = 'rgb('+(r+20)+','+(g+20)+','+(b+20)+')';
  ctx.fillRect(x, y, w, 1);
  ctx.fillRect(x, y, 1, h);
  // Shadow edges
  ctx.fillStyle = 'rgb('+(r-20)+','+(g-20)+','+(b-20)+')';
  ctx.fillRect(x+w-1, y, 1, h);
  ctx.fillRect(x, y+h-1, w, 1);
}

// Generate all regions
brushedMetal(0, 0, 16, 16, 74, 74, 82);      // steel
brushedMetal(16, 0, 16, 16, 184, 115, 51);   // copper
brushedMetal(0, 16, 16, 16, 42, 42, 48);     // dark_iron
brushedMetal(16, 16, 16, 16, 255, 102, 0);   // fire

// CRITICAL: Persist and refresh
tex.updateSource(tex.canvas.toDataURL());
tex.updateMaterial();
Canvas.updateAllFaces();
```

### Material Presets (RGB)

| Material | R | G | B | Hex |
|----------|---|---|---|-----|
| steel | 74 | 74 | 82 | #4a4a52 |
| copper | 184 | 115 | 51 | #b87333 |
| dark_iron | 42 | 42 | 48 | #2a2a30 |
| fire | 255 | 102 | 0 | #ff6600 |
| brass | 181 | 166 | 66 | #b5a642 |
| bronze | 140 | 120 | 83 | #8c7853 |
| rust | 183 | 65 | 14 | #b7410e |

---

## UV Mapping via risky_eval

```javascript
const atlas = Texture.all.find(t => t.name.includes('atlas'));
const regions = {
  steel: [0, 0, 16, 16],
  copper: [16, 0, 32, 16],
  dark_iron: [0, 16, 16, 32],
  fire: [16, 16, 32, 32]
};

function getMaterial(name) {
  const n = name.toLowerCase();
  if (n.includes('base') || n.includes('frame') || n.includes('trim')) return 'dark_iron';
  if (n.includes('dial') || n.includes('switch') || n.includes('pipe')) return 'copper';
  if (n.includes('fire') || n.includes('glow')) return 'fire';
  return 'steel';
}

Cube.all.forEach(cube => {
  const mat = getMaterial(cube.name);
  const uv = regions[mat];
  Object.keys(cube.faces).forEach(face => {
    cube.faces[face].texture = atlas.uuid;
    cube.faces[face].uv = [...uv];
  });
});

Canvas.updateAllFaces();
```

---

## Troubleshooting

### CRITICAL: Validator "Fix UV Size" Button

**NEVER click this button.** It destroys all texture/UV assignments.

**Safe fix for UV size mismatch:**
```javascript
Project.texture_width = 32;
Project.texture_height = 32;
```

### Texture Not Updating in Preview

Call all three:
```javascript
tex.updateSource(tex.canvas.toDataURL());
tex.updateMaterial();
Canvas.updateAllFaces();
```

### All Cubes Same Color

Re-run the UV mapping code. Make sure cube names follow the naming convention.

### "Cube with ID not found"

Close all other project tabs. Only one project should be open.

### Copper/Dark Iron Regions Are Black

The canvas is still 16x16. Run the canvas resize workaround after `create_texture`.

---

## MCP Tool Quick Reference

| Tool | Use For |
|------|---------|
| `create_project` | Start new Hytale Prop project |
| `create_texture` | Create the 32x32 atlas |
| `add_group` | Bone/group hierarchy |
| `place_cube` | Add geometry (no texture param) |
| `modify_cube` | Adjust position/size/rotation |
| `list_outline` | Get element names |
| `list_textures` | Get texture info |
| `risky_eval` | UV mapping, procedural textures, preview refresh |
| `capture_screenshot` | Visual verification |
| `set_camera_angle` | Position camera |

---

## DO NOT

- Use multiple textures (Hytale = single texture)
- Use `apply_texture` MCP tool (broken)
- Click Validator "Fix UV Size" (destroys assignments)
- Use `texture` parameter in `place_cube` (doesn't work)
- Forget to call `updateSource()` after canvas changes
- Forget `updateMaterial()` + `Canvas.updateAllFaces()` for preview
