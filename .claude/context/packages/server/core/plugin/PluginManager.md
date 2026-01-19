# PluginManager

**Package:** `com.hypixel.hytale.server.core.plugin`
**Type:** Class (Singleton)

## Overview

Manages the lifecycle of all plugins on the server. Handles loading, unloading, reloading, and provides access to loaded plugins. This is an internal class - you typically don't interact with it directly.

## Static Fields

### MODS_PATH
```java
public static final Path MODS_PATH
```
The path where plugin JARs are loaded from (typically `mods/` in the server directory).

### METRICS_REGISTRY
```java
public static final MetricsRegistry<PluginManager> METRICS_REGISTRY
```
Metrics registry for plugin manager performance monitoring.

## Static Methods

### get()
```java
public static PluginManager get()
```
**Returns:** The singleton PluginManager instance

## Instance Methods

### Plugin Lifecycle

#### setup()
```java
public void setup()
```
Called during server startup to discover and load plugins.

#### start()
```java
public void start()
```
Called to start all loaded plugins.

#### shutdown()
```java
public void shutdown()
```
Called during server shutdown to stop all plugins.

### Plugin State

#### getState()
```java
public PluginState getState()
```
**Returns:** Current state of the plugin manager

### Plugin Access

#### getPlugins()
```java
public List<PluginBase> getPlugins()
```
**Returns:** List of all loaded plugins

#### getPlugin(PluginIdentifier)
```java
public PluginBase getPlugin(PluginIdentifier identifier)
```
**Returns:** The plugin with the given identifier, or `null`

#### hasPlugin(PluginIdentifier, SemverRange)
```java
public boolean hasPlugin(PluginIdentifier identifier, SemverRange versionRange)
```
**Returns:** `true` if a plugin matching the identifier and version range is loaded

#### getAvailablePlugins()
```java
public Map<PluginIdentifier, PluginManifest> getAvailablePlugins()
```
**Returns:** Map of all discovered plugin manifests (including unloaded plugins)

### Dynamic Loading

#### load(PluginIdentifier)
```java
public boolean load(PluginIdentifier identifier)
```
Load a plugin by identifier.
**Returns:** `true` if successfully loaded

#### unload(PluginIdentifier)
```java
public boolean unload(PluginIdentifier identifier)
```
Unload a plugin by identifier.
**Returns:** `true` if successfully unloaded

#### reload(PluginIdentifier)
```java
public boolean reload(PluginIdentifier identifier)
```
Reload a plugin (unload then load).
**Returns:** `true` if successfully reloaded

### Internal Methods

#### registerCorePlugin(PluginManifest)
```java
public void registerCorePlugin(PluginManifest manifest)
```
Register a core (built-in) plugin. Internal use only.

#### getBridgeClassLoader()
```java
public PluginBridgeClassLoader getBridgeClassLoader()
```
**Returns:** The bridge classloader for inter-plugin class access

#### getSessionSettingsComponentType()
```java
public ComponentType<EntityStore, PluginListPageManager.SessionSettings>
    getSessionSettingsComponentType()
```
**Returns:** Component type for plugin session settings

## Example Usage

While you typically don't use `PluginManager` directly, here are some scenarios:

### Getting Another Plugin
```java
// In your plugin code
PluginManager pm = PluginManager.get();
PluginBase otherPlugin = pm.getPlugin(PluginIdentifier.of("OtherPlugin"));
if (otherPlugin != null && otherPlugin.isEnabled()) {
    // Interact with other plugin
}
```

### Checking Dependencies
```java
// Check if a dependency is available
boolean hasEconomy = PluginManager.get().hasPlugin(
    PluginIdentifier.of("Economy"),
    SemverRange.parse(">=1.0.0")
);
```

### Listing Plugins (Debug)
```java
for (PluginBase plugin : PluginManager.get().getPlugins()) {
    getLogger().atInfo().log("Loaded: " + plugin.getName() +
        " v" + plugin.getManifest().getVersion());
}
```

## Gotchas

1. **Singleton access**: Use `PluginManager.get()` to get the instance.

2. **Don't call lifecycle methods**: `setup()`, `start()`, and `shutdown()` are called by the server, not plugins.

3. **Dynamic loading limitations**: Hot-reloading may not work perfectly for all plugins. Some resources may persist.

4. **Thread safety**: Plugin manager methods should be called from the main server thread.

5. **Plugin dependencies**: The manager handles dependency resolution automatically based on manifests.

## Related

- [PluginBase](PluginBase.md) - Base class for plugins
- [PluginIdentifier](PluginIdentifier.md) - Plugin identifier
- [PluginState](PluginState.md) - Plugin states
