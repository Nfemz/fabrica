# CommandSender

**Package:** `com.hypixel.hytale.server.core.command.system`
**Type:** Interface
**Extends:** `IMessageReceiver`, `PermissionHolder`

## Overview

Interface representing an entity that can send commands. Can be a player, console, or other command source.

## Methods

### getDisplayName()
```java
String getDisplayName()
```
Get the display name of the sender.

**Returns:** Display name string (e.g., player name, "Console")

### getUuid()
```java
UUID getUuid()
```
Get the sender's UUID.

**Returns:** UUID of the sender (null for console)

## Inherited Methods

### From IMessageReceiver
```java
void sendMessage(Message message)
```
Send a message to the sender.

### From PermissionHolder
```java
boolean hasPermission(String permission)
```
Check if sender has a permission.

## Example Usage

### Basic Usage
```java
@Override
protected void executeSync(@Nonnull CommandContext ctx) {
    CommandSender sender = ctx.sender();

    // Get sender info
    String name = sender.getDisplayName();
    UUID uuid = sender.getUuid();

    // Send message
    sender.sendMessage(Message.raw("Hello, " + name));

    // Check permission
    if (sender.hasPermission("myplugin.admin")) {
        // Admin-only logic
    }
}
```

### Check Sender Type
```java
@Override
protected void executeSync(@Nonnull CommandContext ctx) {
    // Using context helper
    if (ctx.isPlayer()) {
        // Player-specific logic
        Ref<EntityStore> playerRef = ctx.senderAsPlayerRef();
    } else {
        // Console or other sender
        ctx.sendMessage(Message.raw("Console command executed"));
    }
}
```

### Cast to Specific Type
```java
// If you know the sender type
PlayerSender player = ctx.senderAs(PlayerSender.class);
```

## Common Sender Types

| Type | Description |
|------|-------------|
| Player | In-game player |
| Console | Server console |
| Command Block | (If supported) |

## Gotchas

1. **UUID may be null**: Console senders don't have a UUID.

2. **Player check**: Use `ctx.isPlayer()` before assuming player-specific features.

3. **Permission inheritance**: Senders inherit permissions from their permission groups.

4. **Message delivery**: Messages to console appear in server logs.

## Related

- [CommandContext](CommandContext.md) - Access sender via `ctx.sender()`
- [IMessageReceiver](../receiver/IMessageReceiver.md) - Message sending
- [PermissionHolder](../permissions/PermissionHolder.md) - Permission checking
