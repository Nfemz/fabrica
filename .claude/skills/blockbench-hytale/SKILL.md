---
name: blockbench-hytale
description: Create and edit Hytale-format models in Blockbench using MCP tools. Handles shading modes, quads, attachments, and stretch values.
---

# Hytale Model Creation

Create and edit Hytale-format models using the Blockbench MCP tools. This skill focuses on Hytale-specific features like shading modes, quads, attachments, and stretch values.

## Usage

```
/blockbench-hytale <action> [args]
```

**Actions:**
- `create <name>` - Create a new Hytale model project
- `add-cube <name>` - Add a cube with Hytale properties
- `add-quad <name>` - Add a 2D quad plane
- `validate` - Validate model against Hytale constraints
- `attachment <action>` - Manage attachments

## Prerequisites

- Blockbench must be running with the MCP plugin enabled
- Hytale Models plugin installed in Blockbench

## Steps

### Creating a New Hytale Model

1. **Create project** using `mcp__blockbench__create_project`:
   - Set `format` to `"hytale"` for Hytale format
   - Provide model name

2. **Get format info** using `mcp__blockbench__hytale_get_format_info`:
   - Returns current format settings and constraints
   - Use to verify Hytale mode is active

### Adding Cubes with Hytale Properties

1. **Place cube** using `mcp__blockbench__place_cube`:
   - Specify `position`, `size`, and `name`
   - Optional: `rotation`, `origin`, `group`

2. **Set Hytale properties** using `mcp__blockbench__hytale_set_cube_properties`:
   - `shading_mode`: One of `"standard"`, `"flat"`, `"fullbright"`, `"reflective"`
     - `standard` - Normal lighting (default)
     - `flat` - No shading, uniform color
     - `fullbright` - Emissive, ignores lighting
     - `reflective` - Shiny/metallic appearance
   - `cube_uuid`: Target cube UUID

3. **Adjust stretch** using `mcp__blockbench__hytale_set_cube_stretch`:
   - Changes visual size without affecting UV mapping
   - Useful for layered elements (clothing over body)
   - `stretch`: `[x, y, z]` offset values

### Creating Quads (2D Planes)

1. **Create quad** using `mcp__blockbench__hytale_create_quad`:
   - `name`: Quad name
   - `position`: `[x, y, z]` position
   - `size`: `[width, height]` dimensions
   - `face`: Direction plane faces (`"north"`, `"south"`, `"east"`, `"west"`, `"up"`, `"down"`)
   - Use for flat surfaces like signs, paper, leaves

### Managing Attachments

1. **List attachments** using `mcp__blockbench__hytale_list_attachments`:
   - Shows all attachment points on the model

2. **List attachment pieces** using `mcp__blockbench__hytale_list_attachment_pieces`:
   - Shows available pieces for an attachment slot

3. **Set attachment piece** using `mcp__blockbench__hytale_set_attachment_piece`:
   - `attachment_name`: Attachment slot name
   - `piece_name`: Piece to attach

### Validating the Model

1. **Validate** using `mcp__blockbench__hytale_validate_model`:
   - Checks against Hytale constraints:
     - Maximum 255 nodes (cubes + groups)
     - UV mapping rules
     - Required bone structure
   - Returns list of errors/warnings

2. **List outline** using `mcp__blockbench__list_outline`:
   - View complete model hierarchy
   - Verify group structure

## Hytale Model Constraints

| Constraint | Limit |
|------------|-------|
| Maximum nodes | 255 (cubes + groups combined) |
| UV mapping | Per-face, no negative values |
| Texture size | Power of 2 recommended |
| Root bone | Required for rigged models |

## Integration with Fabrica

After creating a model in Blockbench:

1. **Export** the model as `.hyt` format
2. **Place texture** in `Common/BlockTextures/`
3. **Reference** in item JSON at `Server/Item/Items/`

## Example Workflow

```
User: Create a machine block model

1. Create Hytale project named "generator"
2. Add base cube 16x16x16 at origin
3. Set shading to "standard"
4. Add detail cubes for buttons/displays
5. Set display cubes to "fullbright" shading
6. Validate model
7. Export for use in Fabrica
```
