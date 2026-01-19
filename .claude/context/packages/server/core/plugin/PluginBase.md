# PluginBase

**Package:** `com.hypixel.hytale.server.core.plugin`
**Type:** Abstract Class
**Implements:** `CommandOwner`

## Overview

The internal base class for all plugins. Contains all registry accessors and utility methods. User plugins should extend `JavaPlugin` which extends this class.

## Constructor

```java
public PluginBase(PluginInit init)
```

| Parameter | Type | Description |
|-----------|------|-------------|
| init | PluginInit | Basic plugin initialization context |

## Static Fields

### METRICS_REGISTRY
```java
public static final MetricsRegistry<PluginBase> METRICS_REGISTRY
```
Metrics registry for plugin performance monitoring.

## Lifecycle Methods

### preLoad()
```java
public CompletableFuture<Void> preLoad()
```
Called before setup for async initialization. Override to perform pre-registration setup.

**Returns:** `CompletableFuture<Void>` - Completes when pre-loading is done

### setup() (abstract)
Override in subclass to register commands, events, and systems.

### start() (abstract)
Override in subclass to activate the plugin.

### shutdown() (abstract)
Override in subclass to clean up resources.

## Utility Methods

### getName()
```java
public String getName()
```
**Returns:** Plugin name from manifest

### getLogger()
```java
public HytaleLogger getLogger()
```
**Returns:** Logger instance with plugin context

### getIdentifier()
```java
public PluginIdentifier getIdentifier()
```
**Returns:** Unique plugin identifier (name + version)

### getManifest()
```java
public PluginManifest getManifest()
```
**Returns:** Full plugin manifest data

### getDataDirectory()
```java
public Path getDataDirectory()
```
**Returns:** Path to plugin's data directory (created automatically)

### getState()
```java
public PluginState getState()
```
**Returns:** Current plugin lifecycle state

### isEnabled()
```java
public boolean isEnabled()
```
**Returns:** `true` if plugin state is `ENABLED`

### isDisabled()
```java
public boolean isDisabled()
```
**Returns:** `true` if plugin state is `DISABLED`

### getBasePermission()
```java
public final String getBasePermission()
```
**Returns:** Base permission node for this plugin (e.g., "myplugin")

### getType() (abstract)
```java
public abstract PluginType getType()
```
**Returns:** Type of plugin (implemented by subclasses)

## Registry Accessors

### Command Registry
```java
public CommandRegistry getCommandRegistry()
```
Register commands with `registerCommand(AbstractCommand)`.

### Event Registry
```java
public EventRegistry getEventRegistry()
```
Subscribe to events with `register(Class, Consumer)`.

### Task Registry
```java
public TaskRegistry getTaskRegistry()
```
Track async tasks with `registerTask(CompletableFuture)`.

### Entity Registry
```java
public EntityRegistry getEntityRegistry()
```
Register custom entity types.

### Entity Store Registry
```java
public ComponentRegistryProxy<EntityStore> getEntityStoreRegistry()
```
Register ECS systems that operate on entity stores.

### Chunk Store Registry
```java
public ComponentRegistryProxy<ChunkStore> getChunkStoreRegistry()
```
Register ECS systems that operate on chunk stores.

### Block State Registry
```java
public BlockStateRegistry getBlockStateRegistry()
```
Register custom block state handlers.

### Asset Registry
```java
public AssetRegistry getAssetRegistry()
```
Register custom asset stores.

### Codec Registry (String-keyed)
```java
public <T, C extends Codec<? extends T>> CodecMapRegistry<T, C>
    getCodecRegistry(StringCodecMapCodec<T, C> codec)
```
Get or create a codec registry for the given codec type.

### Codec Registry (Asset-keyed)
```java
public <K, T extends JsonAsset<K>> CodecMapRegistry$Assets<T, ?>
    getCodecRegistry(AssetCodecMapCodec<K, T> codec)
```
Get or create an asset codec registry.

### Codec Registry (Map-keyed)
```java
public <V> MapKeyMapRegistry<V> getCodecRegistry(MapKeyMapCodec<V> codec)
```
Get or create a map-key codec registry.

### Client Feature Registry
```java
public ClientFeatureRegistry getClientFeatureRegistry()
```
Register client-side features.

## Example Usage

```java
// In your JavaPlugin subclass:

@Override
protected void setup() {
    // Commands
    getCommandRegistry().registerCommand(new MyCommand());

    // Events
    getEventRegistry().register(PlayerJoinEvent.class, event -> {
        getLogger().atInfo().log("Player joined: " + event.getPlayer().getName());
    });

    // ECS Systems
    getEntityStoreRegistry().registerSystem(new MyEntitySystem());

    // Log plugin info
    getLogger().atInfo().log("Plugin data at: " + getDataDirectory());
}
```

## Gotchas

1. **Registry access timing**: Registries are only valid during and after `setup()`. Do not access them in the constructor.

2. **Thread safety**: Most registries are thread-safe, but event handlers may be called on different threads.

3. **Data directory**: The data directory is created automatically on first access.

4. **Logger context**: Always use `getLogger()` instead of creating your own logger - it includes plugin context.

## Related

- [JavaPlugin](JavaPlugin.md) - User-facing plugin base class
- [PluginInit](PluginInit.md) - Basic initialization context
- [CommandRegistry](../command/CommandRegistry.md) - Command registration
- [EventRegistry](../../event/EventRegistry.md) - Event subscription
