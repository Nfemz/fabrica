---
name: blockbench-texture
description: Create, apply, and manage textures on Blockbench models. Handle UV mapping and texture organization.
---

# Texturing

Create, apply, and manage textures on models in Blockbench. Handle UV mapping and texture organization.

## Usage

```
/blockbench-texture <action> [args]
```

**Actions:**
- `create <name> <width> <height>` - Create a new texture
- `apply <texture> <element>` - Apply texture to element
- `list` - List all textures
- `group <name>` - Create texture group

## Prerequisites

- Blockbench must be running with the MCP plugin enabled
- A model must be open

## Steps

### Creating Textures

1. **Create texture** using `mcp__blockbench__create_texture`:
   ```json
   {
     "name": "skin",
     "width": 64,
     "height": 64,
     "color": "#FFFFFF"
   }
   ```
   - `name`: Texture identifier
   - `width`: Width in pixels (power of 2 recommended)
   - `height`: Height in pixels (power of 2 recommended)
   - `color`: Optional fill color (hex)

### Applying Textures

1. **Apply to element** using `mcp__blockbench__apply_texture`:
   ```json
   {
     "texture_uuid": "<texture-uuid>",
     "element_uuid": "<element-uuid>",
     "faces": ["north", "south", "east", "west", "up", "down"]
   }
   ```
   - `texture_uuid`: Texture to apply
   - `element_uuid`: Target cube/mesh
   - `faces`: Which faces to texture (optional, defaults to all)

### Managing Textures

1. **List textures** using `mcp__blockbench__list_textures`:
   - Returns all textures in the project
   - Includes UUID, name, dimensions

2. **Get texture** using `mcp__blockbench__get_texture`:
   - `texture_uuid`: Texture to retrieve
   - Returns texture data including base64 image

3. **Add texture group** using `mcp__blockbench__add_texture_group`:
   - `name`: Group name
   - Organizes textures into folders

## Common Texture Sizes

| Use Case | Size | Notes |
|----------|------|-------|
| Character skin | 64x64 | Standard player format |
| Block | 16x16 | Per-face tiling |
| Entity | 64x32 | Varies by complexity |
| HD texture | 128x128+ | Higher detail |

## UV Mapping Tips

- **Auto UV**: Blockbench generates UVs automatically for cubes
- **Per-face UV**: Each face can have different UV coordinates
- **Box UV**: Legacy mode where all faces share UV space
- **Mirror**: Can mirror UVs for symmetric elements

## Integration with Fabrica

1. **Export texture** from Blockbench as PNG
2. **Place in project**:
   - Block textures: `Common/BlockTextures/`
   - Item textures: `Common/Icons/ItemsGenerated/`
3. **Reference in JSON**:
   ```json
   {
     "TextureLocation": "Common/BlockTextures/my_texture.png"
   }
   ```

## Example Workflow

```
User: Texture a machine block

1. List existing textures to check what's available
2. Create texture "machine_base" 32x32 pixels
3. List outline to get element UUIDs
4. Apply texture to base cube (all faces)
5. Create texture "machine_display" 16x16 for screen
6. Apply display texture to front face only
7. Export textures for use in Fabrica
```

## Texture Best Practices

| Practice | Reason |
|----------|--------|
| Power of 2 sizes | GPU optimization |
| Consistent resolution | Visual coherence |
| Group related textures | Organization |
| Descriptive names | Easy identification |
| Minimize texture count | Performance |
