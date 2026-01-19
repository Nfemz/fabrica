# OptionalArg

**Package:** `com.hypixel.hytale.server.core.command.system.arguments.system`
**Type:** Generic Class
**Extends:** `AbstractOptionalArg<OptionalArg<DataType>, DataType>`

## Overview

Represents an optional command argument. Returns null if not provided by the user.

## Type Parameters

| Parameter | Description |
|-----------|-------------|
| DataType | The type of data this argument holds |

## Constructor

```java
public OptionalArg(
    AbstractCommand command,
    String name,
    String description,
    ArgumentType<DataType> type
)
```
Usually created via `withOptionalArg()` on the command.

## Methods

### get()
```java
public DataType get(CommandContext ctx)
```
Get the parsed value, or **null** if not provided.

### provided()
```java
public boolean provided(CommandContext ctx)
```
Check if the argument was provided. Inherited from `Argument`.

## Creating Optional Arguments

### In Command Constructor
```java
public class MyCommand extends CommandBase {
    private final RequiredArg<String> nameArg;      // Required first
    private final OptionalArg<Integer> countArg;    // Optional after
    private final OptionalArg<Boolean> verboseArg;

    public MyCommand() {
        super("mycommand", "Description");

        // Required args first
        nameArg = withRequiredArg("name", "Target name", ArgTypes.STRING);

        // Optional args after
        countArg = withOptionalArg("count", "Number of times", ArgTypes.INTEGER);
        verboseArg = withOptionalArg("verbose", "Show details", ArgTypes.BOOLEAN);
    }
}
```

### List Variant
```java
OptionalArg<List<String>> tagsArg = withListOptionalArg(
    "tags", "Optional tags", ArgTypes.STRING
);
```

## Using Optional Arguments

### Check and Get
```java
@Override
protected void executeSync(@Nonnull CommandContext ctx) {
    String name = nameArg.get(ctx);  // Always has value

    // Check if optional was provided
    if (ctx.provided(countArg)) {
        int count = countArg.get(ctx);
        // Use count...
    } else {
        // Use default behavior
    }

    // Or use null-safe pattern
    Integer count = countArg.get(ctx);
    int actualCount = (count != null) ? count : 1;
}
```

### With Default Fallback
```java
@Override
protected void executeSync(@Nonnull CommandContext ctx) {
    // Provide default if null
    Integer count = countArg.get(ctx);
    int actualCount = Objects.requireNonNullElse(count, 1);

    Boolean verbose = verboseArg.get(ctx);
    boolean showDetails = Boolean.TRUE.equals(verbose);
}
```

## Example: Complete Command

```java
public class SearchCommand extends CommandBase {
    private final RequiredArg<String> queryArg;
    private final OptionalArg<Integer> limitArg;
    private final OptionalArg<Boolean> caseSensitiveArg;

    public SearchCommand() {
        super("search", "Search for items");
        this.setPermissionGroup(GameMode.Adventure);

        queryArg = withRequiredArg("query", "Search term", ArgTypes.STRING);
        limitArg = withOptionalArg("limit", "Max results", ArgTypes.INTEGER);
        caseSensitiveArg = withOptionalArg("case", "Case sensitive", ArgTypes.BOOLEAN);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        String query = queryArg.get(ctx);

        // Apply defaults for optionals
        int limit = ctx.provided(limitArg) ? limitArg.get(ctx) : 10;
        boolean caseSensitive = Boolean.TRUE.equals(caseSensitiveArg.get(ctx));

        ctx.sendMessage(Message.raw("Searching for: " + query));
        ctx.sendMessage(Message.raw("Limit: " + limit + ", Case: " + caseSensitive));

        // Perform search...
    }
}
// Usage: /search diamond
// Usage: /search diamond 5
// Usage: /search diamond 5 true
```

## OptionalArg vs DefaultArg

| Feature | OptionalArg | DefaultArg |
|---------|-------------|------------|
| When not provided | Returns `null` | Returns default value |
| `provided()` check | Needed | Not needed |
| Usage in help | Shows as `[arg]` | Shows as `[arg=default]` |
| Best for | Flags, rare options | Common options |

**Prefer `DefaultArg`** when you always need a value.
**Use `OptionalArg`** when null is meaningful (e.g., "not specified").

## Gotchas

1. **Null checks**: Always check `provided()` or handle null.

2. **Argument order**: Optional arguments must come after required arguments.

3. **Boolean optionals**: `Boolean.TRUE.equals()` safely handles null.

4. **Parsing stops**: If parsing fails mid-way, later optionals aren't checked.

## Related

- [RequiredArg](RequiredArg.md) - Required argument
- [DefaultArg](DefaultArg.md) - Argument with default value
- [FlagArg](FlagArg.md) - Boolean flag
- [ArgTypes](ArgTypes.md) - Available argument types
