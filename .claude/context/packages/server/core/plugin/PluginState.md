# PluginState

**Package:** `com.hypixel.hytale.server.core.plugin`
**Type:** Enum

## Overview

Represents the lifecycle state of a plugin. States transition in order during plugin loading and unloading.

## Values

| Value | Description |
|-------|-------------|
| `NONE` | Initial state before any initialization |
| `SETUP` | Plugin is in the `setup()` phase |
| `START` | Plugin is in the `start()` phase |
| `ENABLED` | Plugin is fully active and running |
| `SHUTDOWN` | Plugin is in the `shutdown()` phase |
| `DISABLED` | Plugin has been disabled/unloaded |

## State Transitions

```
NONE → SETUP → START → ENABLED → SHUTDOWN → DISABLED
```

Normal lifecycle:
1. **NONE**: Plugin JAR loaded, constructor called
2. **SETUP**: `preLoad()` and `setup()` being executed
3. **START**: `start()` being executed
4. **ENABLED**: Plugin is active
5. **SHUTDOWN**: `shutdown()` being executed (during unload/server stop)
6. **DISABLED**: Plugin is no longer active

## Methods

### values()
```java
public static PluginState[] values()
```
**Returns:** Array of all enum values

### valueOf(String)
```java
public static PluginState valueOf(String name)
```
**Returns:** Enum value with the given name

## Example Usage

### Checking State
```java
// In your plugin
if (getState() == PluginState.ENABLED) {
    // Plugin is fully operational
}

// Using helper methods (preferred)
if (isEnabled()) {
    // Same as above
}

if (isDisabled()) {
    // Plugin has been shut down
}
```

### Conditional Behavior
```java
public void doSomething() {
    if (getState() != PluginState.ENABLED) {
        throw new IllegalStateException("Plugin not ready");
    }
    // ... perform action
}
```

### Logging State
```java
@Override
protected void start() {
    getLogger().atInfo().log("Plugin state: " + getState());
    // Output: Plugin state: START
}
```

## Gotchas

1. **State during methods**: During `setup()`, state is `SETUP`. During `start()`, state is `START`. State becomes `ENABLED` only after `start()` completes.

2. **Thread safety**: State changes are atomic but checking state and acting on it is not atomic.

3. **Disabled vs None**: `DISABLED` means the plugin ran and shut down. `NONE` means it hasn't started yet.

4. **Use helper methods**: Prefer `isEnabled()` and `isDisabled()` over direct state comparison when possible.

## Related

- [JavaPlugin](JavaPlugin.md) - `getState()` method
- [PluginBase](PluginBase.md) - `isEnabled()` and `isDisabled()` helpers
- [PluginManager](PluginManager.md) - Manages state transitions
