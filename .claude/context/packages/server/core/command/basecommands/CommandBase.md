# CommandBase

**Package:** `com.hypixel.hytale.server.core.command.system.basecommands`
**Type:** Abstract Class
**Extends:** [AbstractCommand](../AbstractCommand.md)

## Overview

The simplest command base class. Use for commands that execute synchronously and don't need special ECS access. This is the most commonly used command base.

## Constructors

```java
public CommandBase(String name, String description)
public CommandBase(String name, String description, boolean hidden)
public CommandBase(String name)
```

| Parameter | Type | Description |
|-----------|------|-------------|
| name | String | Command name (e.g., "mycommand" for /mycommand) |
| description | String | Help text for the command |
| hidden | boolean | If true, command won't appear in help listings |

## Abstract Method

### executeSync()
```java
protected abstract void executeSync(@Nonnull CommandContext ctx)
```
Override this to implement your command logic.

## Example Usage

### Minimal Command
```java
public class HelloCommand extends CommandBase {
    public HelloCommand() {
        super("hello", "Says hello");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(Message.raw("Hello!"));
    }
}
```

### With Permission
```java
public class AdminCommand extends CommandBase {
    public AdminCommand() {
        super("admin", "Admin-only command");
        this.setPermissionGroup(GameMode.Creative);
        // Or: this.requirePermission("myplugin.admin");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(Message.raw("Admin command executed"));
    }
}
```

### With Arguments
```java
public class GreetCommand extends CommandBase {
    private final RequiredArg<String> nameArg;
    private final DefaultArg<Integer> countArg;

    public GreetCommand() {
        super("greet", "Greet someone");
        this.setPermissionGroup(GameMode.Adventure);

        nameArg = withRequiredArg("name", "Person to greet", ArgTypes.STRING);
        countArg = withDefaultArg("count", "Times", ArgTypes.INTEGER, 1, "1");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        String name = nameArg.get(ctx);
        int count = countArg.get(ctx);

        for (int i = 0; i < count; i++) {
            ctx.sendMessage(Message.raw("Hello, " + name + "!"));
        }
    }
}
```

### With Subcommands
```java
public class MainCommand extends CommandBase {
    public MainCommand() {
        super("main", "Main command with subcommands");

        addSubCommand(new ListSubCommand());
        addSubCommand(new AddSubCommand());
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        // Called when /main is used without subcommand
        ctx.sendMessage(Message.raw("Usage: /main <list|add>"));
    }
}

class ListSubCommand extends CommandBase {
    public ListSubCommand() {
        super("list", "List items");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(Message.raw("Items: ..."));
    }
}
```

### Hidden Command
```java
public class DebugCommand extends CommandBase {
    public DebugCommand() {
        super("debug", "Internal debug command", true); // hidden=true
        this.requirePermission("myplugin.debug");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        // Debug logic
    }
}
```

## When to Use

Use `CommandBase` when:
- Command logic is simple and fast
- You don't need ECS component access
- You don't need async operations

Use alternatives when:
- Need ECS access: Use [AbstractPlayerCommand](AbstractPlayerCommand.md)
- Need async operations: Use [AbstractAsyncCommand](AbstractAsyncCommand.md)

## Gotchas

1. **Sync execution**: `executeSync` runs on the command thread. Don't do heavy I/O here.

2. **Player check**: If you need player-specific features, check `ctx.isPlayer()` first.

3. **Exception handling**: Unhandled exceptions will show an error to the player.

4. **Thread safety**: Don't access world state directly - use proper async patterns.

## Related

- [AbstractCommand](../AbstractCommand.md) - Parent class
- [AbstractAsyncCommand](AbstractAsyncCommand.md) - Async variant
- [AbstractPlayerCommand](AbstractPlayerCommand.md) - Player ECS access
- [CommandContext](../CommandContext.md) - Execution context
