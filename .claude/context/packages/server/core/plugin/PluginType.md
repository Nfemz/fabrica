# PluginType

**Package:** `com.hypixel.hytale.server.core.plugin`
**Type:** Enum

## Overview

Enum representing the type of plugin. Currently, only `PLUGIN` is available for user-created plugins.

## Values

| Value | Description |
|-------|-------------|
| `PLUGIN` | Standard user-created plugin |

## Methods

### values()
```java
public static PluginType[] values()
```
**Returns:** Array of all enum values

### valueOf(String)
```java
public static PluginType valueOf(String name)
```
**Returns:** Enum value with the given name

### getDisplayName()
```java
public String getDisplayName()
```
**Returns:** Human-readable name for the plugin type

## Example Usage

```java
// JavaPlugin always returns PLUGIN
PluginType type = myPlugin.getType();
// type == PluginType.PLUGIN

String display = type.getDisplayName();
// display == "Plugin" (or similar)
```

## Notes

This enum exists for future extensibility. Currently all user plugins are of type `PLUGIN`. Internal/core plugins may have different types in the future.

## Related

- [JavaPlugin](JavaPlugin.md) - `getType()` always returns `PLUGIN`
- [PluginBase](PluginBase.md) - Abstract `getType()` method
