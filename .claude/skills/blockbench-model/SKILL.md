---
name: blockbench-model
description: Create and manipulate 3D geometry in Blockbench - cubes, meshes, groups, and organizational structure.
---

# General 3D Modeling

Create and manipulate 3D geometry in Blockbench including cubes, meshes, primitives, and hierarchical organization.

## Usage

```
/blockbench-model <action> [args]
```

**Actions:**
- `cube <name>` - Create a cube
- `sphere <name>` - Create a sphere mesh
- `cylinder <name>` - Create a cylinder mesh
- `group <name>` - Create a group for organization
- `modify <uuid>` - Modify existing element
- `list` - List all elements in outline

## Prerequisites

- Blockbench must be running with the MCP plugin enabled

## Steps

### Creating Cubes

1. **Place cube** using `mcp__blockbench__place_cube`:
   ```json
   {
     "name": "body",
     "position": [0, 0, 0],
     "size": [8, 12, 4],
     "origin": [0, 0, 0],
     "rotation": [0, 0, 0]
   }
   ```

2. **Modify cube** using `mcp__blockbench__modify_cube`:
   - `uuid`: Target cube UUID
   - `position`, `size`, `rotation`: New values
   - `inflate`: Expand/shrink without changing UV

### Creating Mesh Primitives

1. **Create sphere** using `mcp__blockbench__create_sphere`:
   - `name`: Mesh name
   - `position`: Center position
   - `radius`: Sphere radius
   - `segments`: Detail level (higher = smoother)

2. **Create cylinder** using `mcp__blockbench__create_cylinder`:
   - `name`: Mesh name
   - `position`: Base position
   - `radius`: Cylinder radius
   - `height`: Cylinder height
   - `segments`: Circular detail level

3. **Place mesh** using `mcp__blockbench__place_mesh`:
   - Generic mesh creation
   - `vertices`: Array of vertex positions
   - `faces`: Array of face definitions

### Mesh Operations

1. **Extrude mesh** using `mcp__blockbench__extrude_mesh`:
   - Select faces/edges first
   - Extrude along normal or custom direction

2. **Subdivide mesh** using `mcp__blockbench__subdivide_mesh`:
   - Increase mesh detail
   - Splits faces into smaller polygons

3. **Merge vertices** using `mcp__blockbench__merge_mesh_vertices`:
   - Combine vertices at same position
   - Useful for cleaning up geometry

### Vertex Editing

1. **Select elements** using `mcp__blockbench__select_mesh_elements`:
   - `element_type`: `"vertices"`, `"edges"`, or `"faces"`
   - `indices`: Array of element indices to select

2. **Move vertices** using `mcp__blockbench__move_mesh_vertices`:
   - `vertex_indices`: Which vertices to move
   - `offset`: `[x, y, z]` movement

### Organization

1. **Add group** using `mcp__blockbench__add_group`:
   - `name`: Group name
   - `parent`: Parent group UUID (optional)
   - Groups act as bones for animation

2. **List outline** using `mcp__blockbench__list_outline`:
   - Returns full model hierarchy
   - Shows all cubes, meshes, and groups

3. **Rename element** using `mcp__blockbench__rename_element`:
   - `uuid`: Element to rename
   - `name`: New name

4. **Duplicate element** using `mcp__blockbench__duplicate_element`:
   - `uuid`: Element to duplicate
   - Returns new element UUID

5. **Remove element** using `mcp__blockbench__remove_element`:
   - `uuid`: Element to delete
   - Removes from model

## Best Practices

| Practice | Reason |
|----------|--------|
| Use descriptive names | Easier animation and debugging |
| Organize with groups | Creates bone hierarchy |
| Keep cube count low | Better performance |
| Use origin points wisely | Affects rotation pivot |

## Coordinate System

Blockbench uses a right-handed coordinate system:
- **X**: Left (-) to Right (+)
- **Y**: Down (-) to Up (+)
- **Z**: Back (-) to Front (+)

## Example Workflow

```
User: Create a character body

1. Add group "root" at origin
2. Add group "body" under root
3. Place cube "torso" [8, 12, 4] in body group
4. Add group "head" under body
5. Place cube "head" [8, 8, 8] in head group
6. Add groups for limbs
7. Place arm/leg cubes in respective groups
8. List outline to verify structure
```
