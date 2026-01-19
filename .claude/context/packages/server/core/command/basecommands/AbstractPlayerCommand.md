# AbstractPlayerCommand

**Package:** `com.hypixel.hytale.server.core.command.system.basecommands`
**Type:** Abstract Class
**Extends:** [AbstractAsyncCommand](AbstractAsyncCommand.md)

## Overview

Base class for commands that need access to the player's ECS components. Automatically handles threading to ensure safe component access.

## Constructors

```java
public AbstractPlayerCommand(String name, String description)
public AbstractPlayerCommand(String name, String description, boolean hidden)
public AbstractPlayerCommand(String name)
```

## Abstract Method

Override the `execute` method (not shown in javap, but required):

```java
protected abstract void execute(
    @Nonnull CommandContext ctx,
    @Nonnull Store<EntityStore> store,
    @Nonnull Ref<EntityStore> ref,
    @Nonnull PlayerRef playerRef,
    @Nonnull World world
)
```

| Parameter | Type | Description |
|-----------|------|-------------|
| ctx | CommandContext | Command execution context |
| store | Store<EntityStore> | ECS store for component access |
| ref | Ref<EntityStore> | Entity reference for the player |
| playerRef | PlayerRef | Player-specific reference |
| world | World | The world the player is in |

## Example Usage

### Get Player Position
```java
public class WhereAmICommand extends AbstractPlayerCommand {
    public WhereAmICommand() {
        super("whereami", "Shows your current position");
        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void execute(@Nonnull CommandContext ctx,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        // Get transform component
        TransformComponent transform = store.getComponent(ref,
            TransformComponent.getComponentType());

        Vector3f pos = transform.getPosition();
        ctx.sendMessage(Message.raw(String.format(
            "Position: %.1f, %.1f, %.1f",
            pos.x(), pos.y(), pos.z()
        )));
    }
}
```

### Get Player Component
```java
public class HealthCommand extends AbstractPlayerCommand {
    public HealthCommand() {
        super("health", "Shows your health");
        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void execute(@Nonnull CommandContext ctx,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        // Get player component
        Player player = store.getComponent(ref, Player.getComponentType());

        // Send message to player
        player.sendMessage(Message.raw("Your health info..."));
    }
}
```

### Modify Player State
```java
public class HealCommand extends AbstractPlayerCommand {
    public HealCommand() {
        super("heal", "Heals the player");
        this.requirePermission("myplugin.heal");
    }

    @Override
    protected void execute(@Nonnull CommandContext ctx,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        // Access and modify components safely
        // The execute method runs on the correct thread

        ctx.sendMessage(Message.raw("You have been healed!"));
    }
}
```

## When to Use

Use `AbstractPlayerCommand` when you need to:
- Read player ECS components (position, health, inventory, etc.)
- Modify player state
- Access the player's world
- Query entities near the player

Use alternatives when:
- No ECS access needed: Use [CommandBase](CommandBase.md)
- Console-compatible command: Use [CommandBase](CommandBase.md) with `ctx.isPlayer()` check

## Component Access Patterns

### Get Component
```java
TransformComponent transform = store.getComponent(ref,
    TransformComponent.getComponentType());
```

### Check Component Exists
```java
if (store.hasComponent(ref, SomeComponent.getComponentType())) {
    // Component exists
}
```

### Get Player Directly
```java
Player player = store.getComponent(ref, Player.getComponentType());
```

## Gotchas

1. **Player-only**: This command will fail if executed from console. The framework handles this automatically.

2. **Threading**: The `execute` method runs on the appropriate thread for safe ECS access. Don't spawn additional threads for component access.

3. **World reference**: The `world` parameter is the player's current world. Use it for world operations.

4. **Component types**: Use `ComponentType.getComponentType()` static methods to get component types.

5. **Null components**: Always check if a component exists before using it if it's optional.

## Related

- [AbstractAsyncCommand](AbstractAsyncCommand.md) - Parent class
- [CommandBase](CommandBase.md) - Simpler sync commands
- [component package](../../component/_index.md) - ECS documentation
- [Store](../../component/Store.md) - Component storage
