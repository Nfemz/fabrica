# RequiredArg

**Package:** `com.hypixel.hytale.server.core.command.system.arguments.system`
**Type:** Generic Class
**Extends:** `Argument<RequiredArg<DataType>, DataType>`

## Overview

Represents a required command argument. The command will fail if this argument is not provided.

## Type Parameters

| Parameter | Description |
|-----------|-------------|
| DataType | The type of data this argument holds |

## Constructor

```java
public RequiredArg(
    AbstractCommand command,
    String name,
    String description,
    ArgumentType<DataType> type
)
```
Usually created via `withRequiredArg()` on the command.

## Methods

### get()
```java
public DataType get(CommandContext ctx)
```
Get the parsed argument value. Inherited from `Argument`.

### getUsageMessage() / getUsageOneLiner()
Get formatted usage strings for help text.

## Creating Required Arguments

### In Command Constructor
```java
public class MyCommand extends CommandBase {
    private final RequiredArg<String> nameArg;
    private final RequiredArg<Integer> countArg;

    public MyCommand() {
        super("mycommand", "Description");

        // Create required arguments
        nameArg = withRequiredArg("name", "The target name", ArgTypes.STRING);
        countArg = withRequiredArg("count", "Number of times", ArgTypes.INTEGER);
    }
}
```

### List Variant
```java
// Accept multiple values: /cmd value1 value2 value3
RequiredArg<List<String>> valuesArg = withListRequiredArg(
    "values", "List of values", ArgTypes.STRING
);
```

## Using Required Arguments

### Get Value
```java
@Override
protected void executeSync(@Nonnull CommandContext ctx) {
    String name = nameArg.get(ctx);  // Always has a value
    int count = countArg.get(ctx);

    // Use values...
}
```

### Add Validator
```java
RequiredArg<Integer> ageArg = withRequiredArg("age", "Age", ArgTypes.INTEGER)
    .addValidator(Validator.range(0, 150));
```

### Custom Suggestions
```java
RequiredArg<String> modeArg = withRequiredArg("mode", "Mode", ArgTypes.STRING)
    .suggest((sender, input, position, result) -> {
        result.addSuggestion("fast");
        result.addSuggestion("slow");
        result.addSuggestion("normal");
    });
```

## Example: Complete Command

```java
public class TeleportCommand extends CommandBase {
    private final RequiredArg<PlayerRef> playerArg;
    private final RequiredArg<RelativeDoublePosition> posArg;

    public TeleportCommand() {
        super("tp", "Teleport a player");
        this.setPermissionGroup(GameMode.Creative);

        playerArg = withRequiredArg("player", "Player to teleport", ArgTypes.PLAYER_REF);
        posArg = withRequiredArg("position", "Destination", ArgTypes.RELATIVE_POSITION);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        PlayerRef player = playerArg.get(ctx);
        RelativeDoublePosition pos = posArg.get(ctx);

        // Teleport logic...
        ctx.sendMessage(Message.raw("Teleported!"));
    }
}
// Usage: /tp PlayerName 100 64 -200
// Usage: /tp PlayerName ~ ~10 ~
```

## Gotchas

1. **Order matters**: Arguments are parsed in definition order.

2. **Validation errors**: If validation fails, the command shows an error to the user.

3. **No null**: Required arguments always have a value if the command executes.

4. **Space handling**: Strings with spaces need quotes: `"hello world"`

## Related

- [OptionalArg](OptionalArg.md) - Optional argument
- [DefaultArg](DefaultArg.md) - Argument with default value
- [ArgTypes](ArgTypes.md) - Available argument types
- [AbstractCommand](../AbstractCommand.md) - `withRequiredArg()` method
