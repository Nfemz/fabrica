# CommandContext

**Package:** `com.hypixel.hytale.server.core.command.system`
**Type:** Final Class

## Overview

Provides access to command execution context: the sender, arguments, and utility methods for responding to the command.

## Constructor

```java
public CommandContext(
    AbstractCommand command,
    CommandSender sender,
    String inputString
)
```
Created internally by the command system.

## Methods

### Argument Access

#### get()
```java
public <DataType> DataType get(Argument<?, DataType> argument)
```
Get the parsed value of an argument.

```java
String name = nameArg.get(ctx);  // Get argument value
```

#### provided()
```java
public boolean provided(Argument<?, ?> argument)
```
Check if an optional argument was provided.

```java
if (ctx.provided(optionalArg)) {
    // Argument was given
}
```

#### getInput()
```java
public String[] getInput(Argument<?, ?> argument)
```
Get the raw input strings for an argument.

#### getInputString()
```java
public String getInputString()
```
Get the full raw input string.

### Sender Access

#### sender()
```java
public CommandSender sender()
```
Get the command sender.

```java
CommandSender sender = ctx.sender();
String name = sender.getDisplayName();
```

#### senderAs()
```java
public <T extends CommandSender> T senderAs(Class<T> type)
```
Cast sender to a specific type.

#### isPlayer()
```java
public boolean isPlayer()
```
Check if the sender is a player.

```java
if (!ctx.isPlayer()) {
    ctx.sendMessage(Message.raw("Players only!"));
    return;
}
```

#### senderAsPlayerRef()
```java
public Ref<EntityStore> senderAsPlayerRef()
```
Get the player's entity reference (for ECS access).

```java
Ref<EntityStore> playerRef = ctx.senderAsPlayerRef();
// Use with Store to access components
```

### Output

#### sendMessage()
```java
public void sendMessage(Message message)
```
Send a message to the command sender.

```java
ctx.sendMessage(Message.raw("Hello!"));
ctx.sendMessage(Message.raw("Value: " + value));
```

### Command Info

#### getCalledCommand()
```java
public AbstractCommand getCalledCommand()
```
Get the command that was executed.

## Example Usage

### Complete Command
```java
public class InfoCommand extends CommandBase {
    private final RequiredArg<String> targetArg;
    private final OptionalArg<Boolean> verboseArg;

    public InfoCommand() {
        super("info", "Show info about a target");
        targetArg = withRequiredArg("target", "Target name", ArgTypes.STRING);
        verboseArg = withOptionalArg("verbose", "Show details", ArgTypes.BOOLEAN);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        // Get sender info
        String senderName = ctx.sender().getDisplayName();

        // Get arguments
        String target = targetArg.get(ctx);
        boolean verbose = ctx.provided(verboseArg) && verboseArg.get(ctx);

        // Send response
        ctx.sendMessage(Message.raw("Info for: " + target));
        if (verbose) {
            ctx.sendMessage(Message.raw("Requested by: " + senderName));
        }
    }
}
```

### Player-Only Command
```java
@Override
protected void executeSync(@Nonnull CommandContext ctx) {
    if (!ctx.isPlayer()) {
        ctx.sendMessage(Message.raw("This command requires a player!"));
        return;
    }

    Ref<EntityStore> playerRef = ctx.senderAsPlayerRef();
    // Use playerRef with Store...
}
```

## Gotchas

1. **Argument.get() vs ctx.get()**: Both work, but `argument.get(ctx)` is more readable:
   ```java
   // Both equivalent:
   String value = nameArg.get(ctx);
   String value = ctx.get(nameArg);
   ```

2. **Check optional args**: Always use `ctx.provided()` before getting optional args to avoid null.

3. **Player ref validity**: `senderAsPlayerRef()` returns null if sender is not a player.

4. **Message formatting**: Use `Message.raw()` for plain text. Hytale may support richer formatting.

## Related

- [CommandSender](CommandSender.md) - Sender interface
- [Argument](arguments/Argument.md) - Argument base class
- [Message](../Message.md) - Message formatting
