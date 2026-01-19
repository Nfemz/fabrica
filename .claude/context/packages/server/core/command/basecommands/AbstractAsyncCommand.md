# AbstractAsyncCommand

**Package:** `com.hypixel.hytale.server.core.command.system.basecommands`
**Type:** Abstract Class
**Extends:** [AbstractCommand](../AbstractCommand.md)

## Overview

Base class for commands that perform asynchronous operations. Use when your command needs to do I/O, network calls, or other blocking operations.

## Constructors

```java
public AbstractAsyncCommand(String name, String description)
public AbstractAsyncCommand(String name, String description, boolean hidden)
public AbstractAsyncCommand(String name)
```

## Methods

### runAsync()
```java
public CompletableFuture<Void> runAsync(
    CommandContext ctx,
    Runnable task,
    Executor executor
)
```
Helper method to run a task asynchronously.

| Parameter | Type | Description |
|-----------|------|-------------|
| ctx | CommandContext | Command context |
| task | Runnable | Task to run |
| executor | Executor | Executor to run on |

**Returns:** `CompletableFuture<Void>` - Future that completes when task finishes

## Example Usage

### Basic Async Command
```java
public class FetchCommand extends AbstractAsyncCommand {
    public FetchCommand() {
        super("fetch", "Fetch data from server");
        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(Message.raw("Fetching data..."));

        return CompletableFuture.runAsync(() -> {
            // Heavy I/O operation
            String data = fetchFromServer();
            ctx.sendMessage(Message.raw("Result: " + data));
        });
    }

    private String fetchFromServer() {
        // Simulated network call
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "data";
    }
}
```

### Using runAsync Helper
```java
public class ProcessCommand extends AbstractAsyncCommand {
    public ProcessCommand() {
        super("process", "Process something");
    }

    @Override
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext ctx) {
        return runAsync(ctx, () -> {
            // This runs on the default async executor
            doHeavyProcessing();
            ctx.sendMessage(Message.raw("Processing complete"));
        }, ForkJoinPool.commonPool());
    }
}
```

### Chained Operations
```java
@Override
protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext ctx) {
    return CompletableFuture
        .supplyAsync(this::loadData)
        .thenApplyAsync(this::processData)
        .thenAccept(result -> {
            ctx.sendMessage(Message.raw("Result: " + result));
        });
}
```

## When to Use

Use `AbstractAsyncCommand` when:
- Performing file I/O
- Making network requests
- Running database queries
- Any operation that might block

Don't use when:
- Simple, fast operations (use `CommandBase`)
- Need ECS player access (use `AbstractPlayerCommand`)

## Gotchas

1. **World access**: Don't modify world state directly in async code. Use `world.execute()` to run on the world thread.

2. **Exception handling**: Handle exceptions in your CompletableFuture chain to avoid silent failures.

3. **Player validity**: The player might disconnect while your async operation runs. Check validity before sending messages.

4. **Context thread safety**: `CommandContext` and `Message.raw()` are safe to use from any thread.

## Related

- [CommandBase](CommandBase.md) - Sync alternative
- [AbstractPlayerCommand](AbstractPlayerCommand.md) - Player-specific async
- [AbstractCommand](../AbstractCommand.md) - Parent class
