# Blockbench MCP - Tips & Best Practices

Quick reference for using Blockbench MCP tools effectively with Hytale models.

## Texture Creation

### Pixel Density Requirements
- **Hytale characters**: 64x64 textures, UV size 64
- **Hytale props**: 32x32 textures, UV size 32

### Texture UV Size
Each texture has `uv_width`/`uv_height` properties that must match its resolution. If you see "resolution does not match UV size" errors:
```javascript
// Fix texture UV size to match resolution (e.g., 32x32 for props)
let tex = Texture.all.find(t => t.name === 'texture_name');
tex.uv_width = 32;
tex.uv_height = 32;
Canvas.updateAll();
```

### Texture Size Issues
- **CRITICAL**: The `create_texture` tool may create 16x16 textures even when you request 32x32
- Always verify texture size after creation:
```javascript
// Via risky_eval
let tex = Texture.all.find(t => t.name === 'texture_name');
({width: tex.width, height: tex.height})
```

### Resizing Textures
If texture is wrong size, resize via risky_eval:
```javascript
let tex = Texture.all[0];
tex.edit((canvas) => {
  let ctx = canvas.getContext('2d');
  let imgData = ctx.getImageData(0, 0, 16, 16);
  let newCanvas = document.createElement('canvas');
  newCanvas.width = 32;
  newCanvas.height = 32;
  let newCtx = newCanvas.getContext('2d');
  newCtx.putImageData(imgData, 0, 0);
  ctx.canvas.width = 32;
  ctx.canvas.height = 32;
  ctx.drawImage(newCanvas, 0, 0);
}, {no_undo: true});
tex.width = 32;
tex.height = 32;
tex.updateSource();
```

## UV Mapping

### Auto-UV Behavior
- Blockbench uses **auto-UV by default** which calculates UV coordinates based on cube dimensions
- Setting `uv_offset` or face UVs directly often gets overwritten by auto-UV
- The `autouv` property: 0 = disabled, 1 = enabled, 2 = relative

### Texture Atlas Approach (Recommended)
Instead of fighting UV coordinates, **paint the texture to match where auto-UV samples**:

1. Create model elements first
2. Check where each element's UV coordinates point:
```javascript
Cube.all.map(c => ({name: c.name, north: c.faces.north.uv}))
```
3. Paint colored regions at those UV coordinates on the texture
4. Auto-UV will naturally sample the correct colors

### UV Coordinate Format
- UVs are `[x1, y1, x2, y2]` defining a rectangle on the texture
- Coordinates can extend beyond texture bounds (will wrap/tile)
- Different faces (north, south, east, west, up, down) have different UV mappings

## Element Identification

### Finding Elements
- `modify_cube` and similar tools often fail to find elements by name
- Use UUIDs instead, obtained from `list_outline` or:
```javascript
Cube.all.map(c => ({name: c.name, uuid: c.uuid}))
```

### Direct Manipulation via risky_eval
When MCP tools fail, use `risky_eval` for direct access:
```javascript
// Find and modify a cube
let cube = Cube.all.find(c => c.name === 'cube_name');
cube.uv_offset = [16, 0];
cube.autouv = 0;
Canvas.updateAll();
```

## Drawing on Textures

### Shape Tool
- Use `draw_shape_tool` for painting colored regions
- Do NOT include `opacity` parameter - it causes errors
- Shapes: `rectangle`, `rectangle_h`, `ellipse`, `ellipse_h`

```javascript
// Correct usage
{
  "texture_id": "texture_name",
  "shape": "rectangle",
  "start": {"x": 0, "y": 0},
  "end": {"x": 16, "y": 16},
  "color": "#5a6a7a"
}
```

### Color Regions Strategy
For multi-colored models, divide your 32x32 texture into regions:
- Top-left (0-16, 0-16): Primary color (e.g., steel gray)
- Top-right (16-32, 0-16): Secondary color (e.g., copper)
- Bottom-left (0-16, 16-32): Tertiary color (e.g., dark metal)
- Bottom-right (16-32, 16-32): Accent colors (brass, fire glow, indicators)

## Model Creation Best Practices

### Workflow
1. Create project with correct format (`hytale_prop` for props)
2. Create texture at correct size (32x32 for props)
3. Create group hierarchy for organization
4. Place cubes/cylinders with texture assigned
5. Check UV mappings to see where elements sample
6. Paint texture regions to match UV sampling locations
7. Refine as needed

### Cylinders and Meshes
- `create_cylinder` and `create_sphere` create mesh geometry
- These use different UV mapping than cubes
- Cylinders default to sampling from texture top-left region

### Updating the View
After making changes via risky_eval, refresh the view:
```javascript
Canvas.updateAll();
// Or for specific element updates:
cube.preview_controller.updateFaces(cube);
cube.preview_controller.updateUV(cube);
```

## Quick Checklist

1. **Texture size**: Props = 32x32, Characters = 64x64
2. **Texture UV size**: Set `tex.uv_width`/`uv_height` to match texture resolution
3. **UV mapping**: Auto-UV overwrites manual settings; paint texture to match sampling locations
4. **Element lookup**: Use UUIDs, not names
5. **draw_shape_tool**: Don't include `opacity` parameter

## Debugging

### Check Texture
```javascript
let tex = Texture.all[0];
({name: tex.name, width: tex.width, height: tex.height, uuid: tex.uuid})
```

### Check Cube UVs
```javascript
let cube = Cube.all.find(c => c.name === 'name');
({
  north: cube.faces.north.uv,
  south: cube.faces.south.uv,
  autouv: cube.autouv
})
```

### List All Elements
```javascript
Cube.all.map(c => ({name: c.name, uuid: c.uuid}))
```
