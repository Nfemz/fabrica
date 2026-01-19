# JavaPlugin

**Package:** `com.hypixel.hytale.server.core.plugin`
**Type:** Abstract Class
**Extends:** [PluginBase](PluginBase.md)

## Overview

The base class that all user-created plugins must extend. Provides the entry point for plugin development and access to all server systems through inherited registry getters.

## Constructor

```java
public JavaPlugin(JavaPluginInit init)
```

| Parameter | Type | Description |
|-----------|------|-------------|
| init | JavaPluginInit | Plugin initialization context containing manifest, paths, and classloader |

## Methods

### getFile()
```java
public Path getFile()
```
Returns the path to this plugin's JAR file.

**Returns:** `Path` - Absolute path to the plugin JAR

### getClassLoader()
```java
public PluginClassLoader getClassLoader()
```
Returns the classloader used to load this plugin.

**Returns:** `PluginClassLoader` - The plugin's classloader

### getType()
```java
public final PluginType getType()
```
Returns the type of this plugin. Always returns `PluginType.PLUGIN` for user plugins.

**Returns:** `PluginType` - Always `PLUGIN`

## Lifecycle Methods (from PluginBase)

Override these methods to customize plugin behavior:

### preLoad()
```java
public CompletableFuture<Void> preLoad()
```
Called before setup, allows async initialization.

**Default:** Returns completed future

### setup()
```java
protected void setup()
```
Called to register commands, events, and systems. **Override this method.**

**Default:** No-op

### start()
```java
protected void start()
```
Called after setup when the plugin should become active.

**Default:** No-op

### shutdown()
```java
protected void shutdown()
```
Called when the plugin is being disabled.

**Default:** No-op

## Example Usage

### Minimal Plugin
```java
public class MinimalPlugin extends JavaPlugin {
    public MinimalPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        getCommandRegistry().registerCommand(new HelloCommand());
    }
}
```

### Full-Featured Plugin
```java
public class FullPlugin extends JavaPlugin {

    private static FullPlugin instance;
    private MyManager manager;
    private ScheduledExecutorService scheduler;

    public FullPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
        getLogger().atInfo().log("Plugin v" + getManifest().getVersion() + " loading");
    }

    public static FullPlugin getInstance() {
        return instance;
    }

    @Override
    public CompletableFuture<Void> preLoad() {
        // Initialize systems that don't need registries
        manager = new MyManager();
        return CompletableFuture.completedFuture(null);
    }

    @Override
    protected void setup() {
        // Register all plugin components
        getCommandRegistry().registerCommand(new MainCommand(manager));
        getEventRegistry().register(BlockBreakEvent.class, this::onBlockBreak);
        getEntityStoreRegistry().registerSystem(new MySystem());
    }

    @Override
    protected void start() {
        // Start background processes
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(manager::tick, 0, 50, TimeUnit.MILLISECONDS);
        getLogger().atInfo().log("Plugin started");
    }

    @Override
    protected void shutdown() {
        // Clean up resources
        if (scheduler != null) {
            scheduler.shutdown();
        }
        getLogger().atInfo().log("Plugin shutdown");
    }

    private void onBlockBreak(BlockBreakEvent event) {
        manager.handleBreak(event);
    }
}
```

## Inherited Registry Getters

From `PluginBase`, you inherit these registry accessors:

```java
getCommandRegistry()        // CommandRegistry
getEventRegistry()          // EventRegistry
getTaskRegistry()           // TaskRegistry
getEntityRegistry()         // EntityRegistry
getEntityStoreRegistry()    // ComponentRegistryProxy<EntityStore>
getChunkStoreRegistry()     // ComponentRegistryProxy<ChunkStore>
getBlockStateRegistry()     // BlockStateRegistry
getAssetRegistry()          // AssetRegistry
getCodecRegistry(codec)     // CodecMapRegistry<T, C>
getClientFeatureRegistry()  // ClientFeatureRegistry
```

## Utility Methods (from PluginBase)

```java
getName()           // String - Plugin name from manifest
getLogger()         // HytaleLogger - Logger with plugin context
getIdentifier()     // PluginIdentifier - Unique plugin ID
getManifest()       // PluginManifest - Full manifest data
getDataDirectory()  // Path - Plugin's data directory
getState()          // PluginState - Current lifecycle state
isEnabled()         // boolean - True if state is ENABLED
isDisabled()        // boolean - True if state is DISABLED
getBasePermission() // String - Base permission node for this plugin
```

## Gotchas

1. **Constructor must call super**: Always call `super(init)` first in the constructor.

2. **Don't register in constructor**: Registration APIs are not available until `setup()`.

3. **preLoad threading**: The `preLoad()` method may run on a background thread. Ensure thread-safe initialization.

4. **Instance pattern**: Store a static instance reference in the constructor if you need global access to your plugin.

5. **Shutdown cleanup**: Always clean up scheduled executors, file handles, and other resources in `shutdown()`.

## Related

- [JavaPluginInit](JavaPluginInit.md) - Constructor parameter
- [PluginBase](PluginBase.md) - Parent class with all registries
- [PluginState](PluginState.md) - Lifecycle states
