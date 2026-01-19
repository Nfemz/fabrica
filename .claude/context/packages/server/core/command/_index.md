# server.core.command Package

**Package:** `com.hypixel.hytale.server.core.command`
**Classes:** 337 (system: ~45, arguments: ~70, built-in commands: ~220)
**Priority:** Critical
**Status:** Complete

The command framework provides a robust system for creating slash commands with typed arguments, tab completion, permissions, and subcommands.

## Package Structure

```
server.core.command/
├── system/                    # Core framework (document this)
│   ├── AbstractCommand        # Base command class
│   ├── CommandRegistry        # Registration
│   ├── CommandContext         # Execution context
│   ├── CommandSender          # Command sender interface
│   ├── basecommands/          # Convenience base classes
│   │   ├── CommandBase        # Simple sync command
│   │   ├── AbstractAsyncCommand
│   │   └── AbstractPlayerCommand
│   ├── arguments/
│   │   ├── system/            # Argument classes
│   │   │   ├── RequiredArg
│   │   │   ├── OptionalArg
│   │   │   ├── DefaultArg
│   │   │   └── FlagArg
│   │   └── types/             # Built-in argument types
│   │       ├── ArgumentType
│   │       └── ArgTypes
│   ├── exceptions/            # Command exceptions
│   └── suggestion/            # Tab completion
└── commands/                  # Built-in commands (reference only)
```

## Quick Start

### Simple Command
```java
public class HelloCommand extends CommandBase {
    public HelloCommand() {
        super("hello", "Says hello");
        this.setPermissionGroup(GameMode.Adventure); // All players
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(Message.raw("Hello, " + ctx.sender().getDisplayName() + "!"));
    }
}

// Registration in plugin setup()
getCommandRegistry().registerCommand(new HelloCommand());
```

### Command with Arguments
```java
public class GreetCommand extends CommandBase {
    private final RequiredArg<String> nameArg;
    private final DefaultArg<Integer> countArg;

    public GreetCommand() {
        super("greet", "Greet someone multiple times");
        this.setPermissionGroup(GameMode.Adventure);

        // Define arguments
        nameArg = withRequiredArg("name", "Person to greet", ArgTypes.STRING);
        countArg = withDefaultArg("count", "Times to greet", ArgTypes.INTEGER, 1, "1");
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

## Core Classes

| Class | Purpose |
|-------|---------|
| [AbstractCommand](AbstractCommand.md) | Base class for all commands |
| [CommandRegistry](CommandRegistry.md) | Register commands with the system |
| [CommandContext](CommandContext.md) | Execution context with sender, args, etc. |
| [CommandSender](CommandSender.md) | Interface for command senders |

## Base Command Classes

| Class | When to Use |
|-------|-------------|
| [CommandBase](basecommands/CommandBase.md) | Simple synchronous commands |
| [AbstractAsyncCommand](basecommands/AbstractAsyncCommand.md) | Commands with async operations |
| [AbstractPlayerCommand](basecommands/AbstractPlayerCommand.md) | Commands needing ECS access to player |

## Argument System

| Class | Purpose |
|-------|---------|
| [RequiredArg](arguments/RequiredArg.md) | Mandatory argument |
| [OptionalArg](arguments/OptionalArg.md) | Optional argument (null if not provided) |
| [DefaultArg](arguments/DefaultArg.md) | Optional with default value |
| [FlagArg](arguments/FlagArg.md) | Boolean flag (--flag) |
| [ArgTypes](arguments/ArgTypes.md) | Built-in argument types |

## Command Hierarchy

```
                AbstractCommand
                      │
           ┌──────────┴──────────┐
           │                     │
       CommandBase        AbstractAsyncCommand
           │                     │
      (Your sync            AbstractPlayerCommand
       commands)                 │
                            (Your player
                             commands with
                             ECS access)
```

## Permissions

```java
// Allow all players
this.setPermissionGroup(GameMode.Adventure);

// Creative mode only
this.setPermissionGroup(GameMode.Creative);

// Custom permission
this.requirePermission("myplugin.admin");
```

## Subcommands

```java
public class MainCommand extends CommandBase {
    public MainCommand() {
        super("main", "Main command");

        // Add subcommands
        addSubCommand(new SubCommand1());
        addSubCommand(new SubCommand2());
    }
}

// Usage: /main sub1 [args]
// Usage: /main sub2 [args]
```

## Aliases

```java
public MyCommand() {
    super("mycommand", "Description");
    addAliases("mc", "mycmd");  // /mc and /mycmd work too
}
```

## Common Patterns

### Check if Player
```java
@Override
protected void executeSync(@Nonnull CommandContext ctx) {
    if (!ctx.isPlayer()) {
        ctx.sendMessage(Message.raw("This command requires a player"));
        return;
    }
    // Player-specific logic
}
```

### Get Player Reference
```java
Ref<EntityStore> playerRef = ctx.senderAsPlayerRef();
```

### Async Operations
```java
public class AsyncCommand extends AbstractAsyncCommand {
    @Override
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext ctx) {
        return CompletableFuture.runAsync(() -> {
            // Heavy work here
            ctx.sendMessage(Message.raw("Done!"));
        });
    }
}
```

## Gotchas

1. **Registration timing**: Commands must be registered in `setup()`, not the constructor.

2. **Thread safety**: `executeSync` runs on the command thread. Use `AbstractAsyncCommand` for heavy work.

3. **Player commands**: Use `AbstractPlayerCommand` when you need ECS component access - it handles threading correctly.

4. **Argument order**: Arguments are parsed in the order they're defined.

5. **Required before optional**: Define required arguments before optional ones.

6. **Permission groups**: `GameMode.Adventure` is the least restrictive, `GameMode.Creative` is more restrictive.

## Related

- [server.core.plugin](../plugin/_index.md) - Plugin base with `getCommandRegistry()`
- [event](../../event/_index.md) - Event system for game events
