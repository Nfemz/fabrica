# FlagArg

**Package:** `com.hypixel.hytale.server.core.command.system.arguments.system`
**Type:** Class
**Extends:** `AbstractOptionalArg<FlagArg, Boolean>`
**Implements:** `DefaultValueArgument<Boolean>`

## Overview

A boolean flag argument that defaults to false. When the flag name is present in the command, the value becomes true.

## Constructor

```java
public FlagArg(AbstractCommand command, String name, String description)
```
Usually created via `withFlagArg()` on the command.

## Methods

### get()
```java
public Boolean get(CommandContext ctx)
```
Returns `true` if flag is present, `false` otherwise.

### getDefaultValue()
```java
public Boolean getDefaultValue()
```
Always returns `false`.

## Creating Flag Arguments

### In Command Constructor
```java
public class MyCommand extends CommandBase {
    private final RequiredArg<String> targetArg;
    private final FlagArg verboseFlag;
    private final FlagArg forceFlag;
    private final FlagArg silentFlag;

    public MyCommand() {
        super("mycommand", "Description");

        targetArg = withRequiredArg("target", "Target", ArgTypes.STRING);

        // Flag arguments
        verboseFlag = withFlagArg("verbose", "Show detailed output");
        forceFlag = withFlagArg("force", "Skip confirmation");
        silentFlag = withFlagArg("silent", "Suppress messages");
    }
}
```

## Using Flag Arguments

### Simple Check
```java
@Override
protected void executeSync(@Nonnull CommandContext ctx) {
    String target = targetArg.get(ctx);
    boolean verbose = verboseFlag.get(ctx);  // true or false
    boolean force = forceFlag.get(ctx);
    boolean silent = silentFlag.get(ctx);

    if (verbose) {
        ctx.sendMessage(Message.raw("Verbose mode enabled"));
    }

    if (!force) {
        ctx.sendMessage(Message.raw("Use --force to skip confirmation"));
        return;
    }

    // Proceed with operation...
    if (!silent) {
        ctx.sendMessage(Message.raw("Done!"));
    }
}
```

## Example: Complete Command

```java
public class CleanupCommand extends CommandBase {
    private final DefaultArg<Integer> olderThanArg;
    private final FlagArg dryRunFlag;
    private final FlagArg verboseFlag;
    private final FlagArg forceFlag;

    public CleanupCommand() {
        super("cleanup", "Clean up old data");
        this.requirePermission("myplugin.admin");

        olderThanArg = withDefaultArg(
            "days", "Delete items older than N days",
            ArgTypes.INTEGER, 30, "30"
        );

        dryRunFlag = withFlagArg("dry-run", "Show what would be deleted without deleting");
        verboseFlag = withFlagArg("verbose", "List each deleted item");
        forceFlag = withFlagArg("force", "Delete without confirmation");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        int days = olderThanArg.get(ctx);
        boolean dryRun = dryRunFlag.get(ctx);
        boolean verbose = verboseFlag.get(ctx);
        boolean force = forceFlag.get(ctx);

        ctx.sendMessage(Message.raw("Cleaning items older than " + days + " days"));

        if (dryRun) {
            ctx.sendMessage(Message.raw("[DRY RUN] Would delete 42 items"));
            return;
        }

        if (!force) {
            ctx.sendMessage(Message.raw("Add --force to actually delete"));
            return;
        }

        // Perform cleanup...
        if (verbose) {
            ctx.sendMessage(Message.raw("- Deleted item1"));
            ctx.sendMessage(Message.raw("- Deleted item2"));
        }

        ctx.sendMessage(Message.raw("Cleanup complete: 42 items deleted"));
    }
}
// Usage: /cleanup
// Usage: /cleanup 7
// Usage: /cleanup 7 --dry-run
// Usage: /cleanup --force --verbose
// Usage: /cleanup 7 --force --verbose
```

## Flag Syntax

Flags typically use `--flagname` syntax:
```
/command target --verbose --force
/command target --dry-run
```

The exact syntax depends on Hytale's command parser implementation.

## FlagArg vs OptionalArg<Boolean>

| Feature | FlagArg | OptionalArg<Boolean> |
|---------|---------|---------------------|
| Syntax | `--flag` | `true`/`false` |
| Default | `false` | `null` |
| Purpose | Switches/toggles | Explicit boolean value |

Use `FlagArg` for on/off switches.
Use `OptionalArg<Boolean>` when you need explicit true/false/unset states.

## Gotchas

1. **Never null**: `get()` always returns `true` or `false`.

2. **Position flexible**: Flags can typically appear anywhere after required args.

3. **Naming**: Use descriptive flag names like `--verbose`, `--force`, `--dry-run`.

4. **Multiple flags**: You can have many flags; order usually doesn't matter.

## Related

- [RequiredArg](RequiredArg.md) - Required argument
- [OptionalArg](OptionalArg.md) - Optional argument
- [DefaultArg](DefaultArg.md) - Argument with default
- [ArgTypes](ArgTypes.md) - Other argument types
