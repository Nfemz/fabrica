# ArgTypes

**Package:** `com.hypixel.hytale.server.core.command.system.arguments.types`
**Type:** Final Class

## Overview

Provides all built-in argument types for command arguments. These are static fields you use with `withRequiredArg()`, `withOptionalArg()`, etc.

## Primitive Types

| Field | Type | Description | Example Input |
|-------|------|-------------|---------------|
| `BOOLEAN` | `Boolean` | true/false | `true`, `false` |
| `INTEGER` | `Integer` | Whole number | `42`, `-10` |
| `FLOAT` | `Float` | Decimal number | `3.14`, `-1.5` |
| `DOUBLE` | `Double` | High-precision decimal | `3.14159` |
| `STRING` | `String` | Text | `hello`, `"hello world"` |
| `UUID` | `UUID` | UUID string | `550e8400-e29b-41d4-a716-446655440000` |

## Player Types

| Field | Type | Description |
|-------|------|-------------|
| `PLAYER_UUID` | `UUID` | Player UUID with validation |
| `PLAYER_REF` | `PlayerRef` | Reference to online player |
| `GAME_PROFILE_LOOKUP` | `PublicGameProfile` | Lookup player profile (sync) |
| `GAME_PROFILE_LOOKUP_ASYNC` | `CompletableFuture<PublicGameProfile>` | Lookup player profile (async) |

## Position Types

| Field | Type | Description |
|-------|------|-------------|
| `VECTOR2I` | `Vector2i` | 2D integer position |
| `VECTOR3I` | `Vector3i` | 3D integer position |
| `RELATIVE_BLOCK_POSITION` | `RelativeIntPosition` | Block position (supports ~) |
| `RELATIVE_POSITION` | `RelativeDoublePosition` | Entity position (supports ~) |
| `RELATIVE_CHUNK_POSITION` | `RelativeChunkPosition` | Chunk position (supports ~) |
| `ROTATION` | `Vector3f` | Rotation angles |

## Relative Number Types

| Field | Type | Description |
|-------|------|-------------|
| `RELATIVE_INTEGER` | `RelativeInteger` | Integer with ~ support |
| `RELATIVE_FLOAT` | `RelativeFloat` | Float with ~ support |
| `RELATIVE_DOUBLE_COORD` | `Coord` | Coordinate with ~ support |
| `RELATIVE_INT_COORD` | `IntCoord` | Integer coordinate with ~ |

## Range Types

| Field | Type | Description |
|-------|------|-------------|
| `INT_RANGE` | `Pair<Integer, Integer>` | Min..max range |
| `RELATIVE_INT_RANGE` | `RelativeIntegerRange` | Range with ~ support |

## Asset Types

| Field | Type | Description |
|-------|------|-------------|
| `ITEM_ASSET` | `Item` | Item definition |
| `BLOCK_TYPE_ASSET` | `BlockType` | Block type definition |
| `MODEL_ASSET` | `ModelAsset` | 3D model asset |
| `WEATHER_ASSET` | `Weather` | Weather configuration |
| `EFFECT_ASSET` | `EntityEffect` | Entity effect |
| `ENVIRONMENT_ASSET` | `Environment` | Environment settings |
| `PARTICLE_SYSTEM` | `ParticleSystem` | Particle system |
| `SOUND_EVENT_ASSET` | `SoundEvent` | Sound event |
| `AMBIENCE_FX_ASSET` | `AmbienceFX` | Ambience effect |
| `INTERACTION_ASSET` | `Interaction` | Interaction definition |
| `ROOT_INTERACTION_ASSET` | `RootInteraction` | Root interaction |

## World Types

| Field | Type | Description |
|-------|------|-------------|
| `WORLD` | `World` | World reference |
| `BLOCK_TYPE_KEY` | `String` | Block type identifier |
| `BLOCK_ID` | `Integer` | Block ID number |
| `BLOCK_PATTERN` | `BlockPattern` | Pattern for block matching |
| `BLOCK_MASK` | `BlockMask` | Mask for block filtering |
| `WEIGHTED_BLOCK_TYPE` | `Pair<Integer, String>` | Weighted block for random |

## Game Types

| Field | Type | Description |
|-------|------|-------------|
| `GAME_MODE` | `GameMode` | Game mode |
| `SOUND_CATEGORY` | `SoundCategory` | Sound category |
| `TICK_RATE` | `Integer` | Tick rate value |
| `COLOR` | `Integer` | Color as integer |

## Entity Types

| Field | Type | Description |
|-------|------|-------------|
| `ENTITY_ID` | `EntityWrappedArg` | Entity by UUID |
| `HITBOX_COLLISION_CONFIG` | `HitboxCollisionConfig` | Hitbox config |
| `REPULSION_CONFIG` | `RepulsionConfig` | Repulsion config |

## Operator Types

| Field | Type | Description |
|-------|------|-------------|
| `INTEGER_COMPARISON_OPERATOR` | `IntegerComparisonOperator` | <, >, =, etc. |
| `INTEGER_OPERATION` | `IntegerOperation` | +, -, *, etc. |

## Static Methods

### forEnum()
```java
public static <E extends Enum<E>> SingleArgumentType<E> forEnum(
    String name,
    Class<E> enumClass
)
```
Create an argument type for any enum.

```java
// Custom enum argument
RequiredArg<MyEnum> modeArg = withRequiredArg(
    "mode", "Operation mode",
    ArgTypes.forEnum("mode", MyEnum.class)
);
```

## Example Usage

### Common Patterns
```java
public class MyCommand extends CommandBase {
    // Primitives
    private final RequiredArg<String> nameArg;
    private final DefaultArg<Integer> countArg;
    private final OptionalArg<Boolean> verboseArg;

    // Position
    private final RequiredArg<RelativeIntPosition> posArg;

    // Asset
    private final RequiredArg<Item> itemArg;

    public MyCommand() {
        super("mycommand", "Example command");

        nameArg = withRequiredArg("name", "Target name", ArgTypes.STRING);
        countArg = withDefaultArg("count", "Amount", ArgTypes.INTEGER, 1, "1");
        verboseArg = withOptionalArg("verbose", "Verbose output", ArgTypes.BOOLEAN);
        posArg = withRequiredArg("pos", "Position", ArgTypes.RELATIVE_BLOCK_POSITION);
        itemArg = withRequiredArg("item", "Item to give", ArgTypes.ITEM_ASSET);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        String name = nameArg.get(ctx);
        int count = countArg.get(ctx);
        boolean verbose = ctx.provided(verboseArg) && verboseArg.get(ctx);

        // Position resolves ~ relative to executor
        RelativeIntPosition relPos = posArg.get(ctx);
        // Get absolute position if executor is a player...

        Item item = itemArg.get(ctx);
    }
}
```

## Gotchas

1. **Relative positions**: `~` positions need a reference point (player position). They won't work from console.

2. **Asset types**: Tab completion shows available assets. Invalid names throw parse errors.

3. **Enum types**: Use `forEnum()` for custom enums instead of STRING.

4. **Player lookups**: Async lookups (`GAME_PROFILE_LOOKUP_ASYNC`) don't block command execution.

## Related

- [RequiredArg](RequiredArg.md) - Required argument wrapper
- [OptionalArg](OptionalArg.md) - Optional argument wrapper
- [DefaultArg](DefaultArg.md) - Default value argument
- [ArgumentType](ArgumentType.md) - Custom argument type base
