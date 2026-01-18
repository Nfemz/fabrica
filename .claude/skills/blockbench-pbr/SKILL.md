---
name: blockbench-pbr
description: Create and configure PBR (physically-based rendering) materials with multiple texture channels in Blockbench.
---

# PBR Materials

Create and configure physically-based rendering materials with multiple texture channels for realistic rendering.

## Usage

```
/blockbench-pbr <action> [args]
```

**Actions:**
- `create <name>` - Create a new PBR material
- `configure <material>` - Set material properties
- `channel <material> <channel>` - Assign texture to channel
- `list` - List all materials
- `export <material>` - Export texture_set.json

## Prerequisites

- Blockbench must be running with the MCP plugin enabled
- PBR materials plugin may be required depending on format

## Steps

### Creating PBR Materials

1. **Create material** using `mcp__blockbench__create_pbr_material`:
   ```json
   {
     "name": "metal_surface",
     "channels": ["color", "normal", "mer"]
   }
   ```
   - `name`: Material identifier
   - `channels`: Which PBR channels to enable

### Configuring Materials

1. **Configure properties** using `mcp__blockbench__configure_material`:
   ```json
   {
     "material_uuid": "<uuid>",
     "metalness": 1.0,
     "roughness": 0.3,
     "emissive": 0.0,
     "subsurface_scattering": false
   }
   ```
   - `metalness`: 0.0 (dielectric) to 1.0 (metal)
   - `roughness`: 0.0 (smooth/shiny) to 1.0 (rough/matte)
   - `emissive`: Emission intensity
   - `subsurface_scattering`: Enable SSS for skin/wax/etc.

### Assigning Texture Channels

1. **Assign texture** using `mcp__blockbench__assign_texture_channel`:
   ```json
   {
     "material_uuid": "<material-uuid>",
     "channel": "normal",
     "texture_uuid": "<texture-uuid>"
   }
   ```

### Managing Materials

1. **List materials** using `mcp__blockbench__list_materials`:
   - Returns all PBR materials in the project
   - Includes channel assignments

2. **Get material info** using `mcp__blockbench__get_material_info`:
   - `material_uuid`: Material to inspect
   - Returns full configuration
   - Includes texture_set.json preview

### Import/Export

1. **Import texture set** using `mcp__blockbench__import_texture_set`:
   - `path`: Path to texture_set.json
   - Imports Bedrock/RTX format materials

2. **Save material config** using `mcp__blockbench__save_material_config`:
   - `material_uuid`: Material to export
   - `path`: Output path
   - Exports texture_set.json format

## PBR Channels

| Channel | Description | Format |
|---------|-------------|--------|
| color | Base color/albedo | RGB |
| normal | Surface normals | RGB (tangent space) |
| mer | Metalness/Emissive/Roughness | RGB packed |
| metalness | Metallic property | Grayscale |
| roughness | Surface roughness | Grayscale |
| emissive | Light emission | RGB |
| heightmap | Displacement/parallax | Grayscale |
| ao | Ambient occlusion | Grayscale |

## MER Channel Packing

The MER texture packs three properties into RGB:
- **R**: Metalness (0-255 maps to 0.0-1.0)
- **G**: Emissive (0-255 maps to 0.0-1.0)
- **B**: Roughness (0-255 maps to 0.0-1.0)

## Common Material Presets

| Material | Metalness | Roughness | Notes |
|----------|-----------|-----------|-------|
| Metal | 1.0 | 0.3-0.5 | Reflective |
| Plastic | 0.0 | 0.4-0.6 | Slight sheen |
| Wood | 0.0 | 0.7-0.9 | Matte |
| Glass | 0.0 | 0.0-0.1 | Very smooth |
| Stone | 0.0 | 0.8-1.0 | Very rough |
| Skin | 0.0 | 0.5 | Enable SSS |

## Example Workflow

```
User: Create a metallic machine material

1. Create PBR material "machine_metal" with channels [color, normal, mer]
2. Configure: metalness 1.0, roughness 0.4
3. Create/assign color texture with base metal color
4. Create/assign normal texture for surface detail
5. Create MER texture:
   - R channel: 255 (full metalness)
   - G channel: 0 (no emission)
   - B channel: 102 (0.4 roughness)
6. Assign MER texture to mer channel
7. Export texture_set.json for Bedrock/RTX
```

## Integration with Fabrica

For Hytale models, PBR is handled through shading modes:
- Use `fullbright` shading for emissive surfaces
- Use `reflective` shading for metallic surfaces
- Standard PBR export is for Bedrock/RTX rendering

## texture_set.json Format

```json
{
  "format_version": "1.16.100",
  "minecraft:texture_set": {
    "color": "machine_color",
    "normal": "machine_normal",
    "metalness_emissive_roughness": "machine_mer"
  }
}
```
