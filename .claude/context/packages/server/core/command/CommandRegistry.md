# CommandRegistry

**Package:** `com.hypixel.hytale.server.core.command.system`
**Type:** Class
**Extends:** `Registry<CommandRegistration>`

## Overview

The registry for registering commands with the Hytale server. Access via `getCommandRegistry()` from your plugin.

## Methods

### registerCommand()
```java
public CommandRegistration registerCommand(AbstractCommand command)
```
Register a command with the server.

| Parameter | Type | Description |
|-----------|------|-------------|
| command | AbstractCommand | The command to register |

**Returns:** `CommandRegistration` - Registration handle (can be used to unregister)

## Example Usage

### Basic Registration
```java
@Override
protected void setup() {
    getCommandRegistry().registerCommand(new MyCommand());
    getCommandRegistry().registerCommand(new AnotherCommand());
}
```

### Multiple Commands
```java
@Override
protected void setup() {
    CommandRegistry registry = getCommandRegistry();

    // Register all commands
    registry.registerCommand(new MainCommand());
    registry.registerCommand(new AdminCommand());
    registry.registerCommand(new DebugCommand());

    getLogger().atInfo().log("Registered 3 commands");
}
```

### With Subcommands
```java
@Override
protected void setup() {
    // Main command has subcommands added in its constructor
    MainCommand main = new MainCommand();
    // main.addSubCommand() called internally

    getCommandRegistry().registerCommand(main);
}
```

## Registration Lifecycle

1. Create command instance (constructor runs)
2. Call `registerCommand()` in `setup()`
3. Command system validates and registers
4. Command becomes available to players
5. On plugin disable, registrations are automatically cleaned up

## Notes

- Commands are automatically unregistered when the plugin is disabled
- Duplicate command names will conflict (last registered wins or error)
- The registry is provided by `PluginBase.getCommandRegistry()`

## Gotchas

1. **Timing**: Only register in `setup()`, not in the constructor or `preLoad()`.

2. **Ownership**: The registry automatically sets the command's owner to your plugin.

3. **Cleanup**: Don't manually unregister - the system handles cleanup on plugin disable.

4. **Namespace**: Consider prefixing commands to avoid conflicts (e.g., `myplugin:command`).

## Related

- [AbstractCommand](AbstractCommand.md) - Base command class
- [CommandBase](basecommands/CommandBase.md) - Simple command base
- [PluginBase](../plugin/PluginBase.md) - `getCommandRegistry()` method
