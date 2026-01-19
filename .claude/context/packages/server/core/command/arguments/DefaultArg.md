# DefaultArg

**Package:** `com.hypixel.hytale.server.core.command.system.arguments.system`
**Type:** Generic Class
**Extends:** `AbstractOptionalArg<DefaultArg<DataType>, DataType>`
**Implements:** `DefaultValueArgument<DataType>`

## Overview

An optional argument that provides a default value when not specified by the user. Preferred over `OptionalArg` when you always need a valid value.

## Type Parameters

| Parameter | Description |
|-----------|-------------|
| DataType | The type of data this argument holds |

## Constructor

```java
public DefaultArg(
    AbstractCommand command,
    String name,
    String description,
    ArgumentType<DataType> type,
    DataType defaultValue,
    String defaultDescription
)
```
Usually created via `withDefaultArg()` on the command.

## Methods

### get()
```java
public DataType get(CommandContext ctx)
```
Get the parsed value, or the default if not provided. **Never returns null**.

### getDefaultValue()
```java
public final DataType getDefaultValue()
```
Get the configured default value.

### getDefaultValueDescription()
```java
public String getDefaultValueDescription()
```
Get the human-readable description of the default.

## Creating Default Arguments

### In Command Constructor
```java
public class MyCommand extends CommandBase {
    private final RequiredArg<String> nameArg;
    private final DefaultArg<Integer> countArg;
    private final DefaultArg<String> modeArg;

    public MyCommand() {
        super("mycommand", "Description");

        nameArg = withRequiredArg("name", "Target", ArgTypes.STRING);

        // Default arguments with fallback values
        countArg = withDefaultArg(
            "count",           // name
            "Number of times", // description
            ArgTypes.INTEGER,  // type
            1,                 // default value
            "1"                // default description for help
        );

        modeArg = withDefaultArg(
            "mode", "Operation mode",
            ArgTypes.STRING,
            "normal",
            "normal"
        );
    }
}
```

### List Variant
```java
DefaultArg<List<String>> tagsArg = withListDefaultArg(
    "tags", "Tags to apply",
    ArgTypes.STRING,
    List.of("default"),
    "default"
);
```

## Using Default Arguments

### Simple Get
```java
@Override
protected void executeSync(@Nonnull CommandContext ctx) {
    String name = nameArg.get(ctx);
    int count = countArg.get(ctx);    // Returns 1 if not provided
    String mode = modeArg.get(ctx);   // Returns "normal" if not provided

    ctx.sendMessage(Message.raw("Name: " + name));
    ctx.sendMessage(Message.raw("Count: " + count));
    ctx.sendMessage(Message.raw("Mode: " + mode));
}
```

### Check If Explicitly Provided
```java
@Override
protected void executeSync(@Nonnull CommandContext ctx) {
    int count = countArg.get(ctx);

    // Check if user explicitly set it vs using default
    if (ctx.provided(countArg)) {
        ctx.sendMessage(Message.raw("Using specified count: " + count));
    } else {
        ctx.sendMessage(Message.raw("Using default count: " + count));
    }
}
```

## Example: Complete Command

```java
public class RepeatCommand extends CommandBase {
    private final RequiredArg<String> messageArg;
    private final DefaultArg<Integer> timesArg;
    private final DefaultArg<Integer> delayArg;

    public RepeatCommand() {
        super("repeat", "Repeat a message");
        this.setPermissionGroup(GameMode.Adventure);

        messageArg = withRequiredArg("message", "Message to repeat", ArgTypes.STRING);
        timesArg = withDefaultArg("times", "Repetitions", ArgTypes.INTEGER, 3, "3");
        delayArg = withDefaultArg("delay", "Delay in ticks", ArgTypes.INTEGER, 20, "20");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        String message = messageArg.get(ctx);
        int times = timesArg.get(ctx);     // Never null, defaults to 3
        int delay = delayArg.get(ctx);     // Never null, defaults to 20

        ctx.sendMessage(Message.raw(
            "Will repeat '" + message + "' " + times +
            " times with " + delay + " tick delay"
        ));
    }
}
// Usage: /repeat "Hello"
// Usage: /repeat "Hello" 5
// Usage: /repeat "Hello" 5 40
```

## Help Text

Default arguments show their default in help:
```
/mycommand <name> [count=1] [mode=normal]
```

## DefaultArg vs OptionalArg

| Feature | DefaultArg | OptionalArg |
|---------|------------|-------------|
| Returns null? | **Never** | Yes |
| Null check needed? | **No** | Yes |
| Best for | Most optional args | When null is meaningful |

**Use DefaultArg** for most optional parameters.

## Gotchas

1. **Never null**: Unlike `OptionalArg`, `get()` always returns a valid value.

2. **Validation**: The default value should pass any validators you add.

3. **Description**: Provide a clear default description for help text.

4. **Type matching**: Default value must match the argument type exactly.

## Related

- [RequiredArg](RequiredArg.md) - Required argument
- [OptionalArg](OptionalArg.md) - Nullable optional argument
- [FlagArg](FlagArg.md) - Boolean flag
- [ArgTypes](ArgTypes.md) - Available argument types
