---
name: blockbench-animate
description: Create and edit animations with keyframes, bone rigging, and Hytale-specific features like visibility keyframes.
---

# Animation Creation

Create and edit animations in Blockbench with keyframes, bone rigging, and Hytale-specific animation features.

## Usage

```
/blockbench-animate <action> [args]
```

**Actions:**
- `create <name>` - Create a new animation
- `keyframe <bone> <property>` - Add keyframes
- `rig` - Set up bone hierarchy
- `play` - Preview animation
- `visibility <bone>` - Toggle bone visibility (Hytale)

## Prerequisites

- Blockbench must be running with the MCP plugin enabled
- A model must be open with proper bone structure (groups)

## Steps

### Creating Animations

1. **Create animation** using `mcp__blockbench__create_animation`:
   ```json
   {
     "name": "walk",
     "length": 1.0,
     "loop": "loop"
   }
   ```
   - `name`: Animation identifier
   - `length`: Duration in seconds
   - `loop`: `"loop"`, `"hold"`, or `"once"`

### Managing Keyframes

1. **Add keyframe** using `mcp__blockbench__manage_keyframes`:
   ```json
   {
     "action": "create",
     "animation": "walk",
     "bone": "left_leg",
     "channel": "rotation",
     "time": 0.0,
     "value": [0, 0, 0]
   }
   ```
   - `action`: `"create"`, `"update"`, or `"delete"`
   - `channel`: `"position"`, `"rotation"`, or `"scale"`
   - `time`: Time in seconds
   - `value`: `[x, y, z]` values

2. **Edit keyframe** - same tool with `action: "update"`:
   ```json
   {
     "action": "update",
     "keyframe_uuid": "<uuid>",
     "value": [15, 0, 0],
     "interpolation": "smooth"
   }
   ```

3. **Delete keyframe** - same tool with `action: "delete"`:
   ```json
   {
     "action": "delete",
     "keyframe_uuid": "<uuid>"
   }
   ```

### Animation Graph Editor

1. **Edit curves** using `mcp__blockbench__animation_graph_editor`:
   - Adjust easing between keyframes
   - `keyframe_uuid`: Target keyframe
   - `interpolation`: `"linear"`, `"smooth"`, `"step"`, `"bezier"`
   - For bezier: `handle_left`, `handle_right` control points

### Bone Rigging

1. **Set up bones** using `mcp__blockbench__bone_rigging`:
   - Groups in Blockbench act as bones
   - `parent_bone`: Parent bone UUID
   - `child_bone`: Child bone UUID
   - Creates parent-child relationship

### Timeline Control

1. **Control playback** using `mcp__blockbench__animation_timeline`:
   - `action`: `"play"`, `"pause"`, `"stop"`, `"seek"`
   - `time`: Seek position (for seek action)
   - `speed`: Playback speed multiplier

### Batch Operations

1. **Batch keyframes** using `mcp__blockbench__batch_keyframe_operations`:
   - Apply changes to multiple keyframes
   - `operation`: `"shift"`, `"scale"`, `"delete"`
   - `keyframes`: Array of keyframe UUIDs
   - `value`: Operation value

2. **Copy/paste animation** using `mcp__blockbench__animation_copy_paste`:
   - `action`: `"copy"` or `"paste"`
   - `source_bone`: Bone to copy from
   - `target_bone`: Bone to paste to
   - `mirror`: Boolean to mirror values

### Hytale-Specific Animation

1. **Visibility keyframe** using `mcp__blockbench__hytale_create_visibility_keyframe`:
   - Toggle bone visibility at specific times
   - `animation`: Animation name
   - `bone`: Target bone
   - `time`: Keyframe time
   - `visible`: Boolean visibility state

2. **Set loop mode** using `mcp__blockbench__hytale_set_animation_loop`:
   - `animation`: Animation name
   - `mode`: `"loop"`, `"hold"`, or `"once"`
     - `loop` - Repeat indefinitely
     - `hold` - Stay at last frame
     - `once` - Play once and stop

## Keyframe Channels

| Channel | Values | Description |
|---------|--------|-------------|
| position | [x, y, z] | Translation offset |
| rotation | [x, y, z] | Euler angles in degrees |
| scale | [x, y, z] | Scale multiplier |

## Interpolation Modes

| Mode | Behavior |
|------|----------|
| linear | Constant speed between keyframes |
| smooth | Ease in/out (catmull-rom) |
| step | Instant jump at keyframe |
| bezier | Custom curve with handles |

## Example Workflow

```
User: Create a walking animation

1. Create animation "walk" length 1.0s, loop mode
2. Add keyframes for left_leg rotation:
   - 0.0s: [0, 0, 0]
   - 0.25s: [-30, 0, 0]
   - 0.5s: [0, 0, 0]
   - 0.75s: [30, 0, 0]
   - 1.0s: [0, 0, 0]
3. Copy animation to right_leg with mirror
4. Add arm swing keyframes (opposite to legs)
5. Set smooth interpolation on all keyframes
6. Preview with timeline play
7. Adjust timing as needed
```

## Animation Tips

- **Start with blocking**: Key poses first, then refine
- **Use smooth interpolation**: More natural movement
- **Mirror for symmetry**: Copy/paste with mirror for limbs
- **Test frequently**: Use timeline playback
- **Keep it loopable**: First and last frame should match for loop
