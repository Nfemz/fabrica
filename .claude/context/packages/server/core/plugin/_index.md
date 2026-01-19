# server.core.plugin Package

**Package:** `com.hypixel.hytale.server.core.plugin`
**Classes:** 33
**Priority:** Critical
**Status:** Complete

The plugin system is the foundation of all Hytale modding. This package provides the base classes for creating plugins, managing their lifecycle, and accessing all server registries.

## Overview

Every plugin extends `JavaPlugin`, which provides access to all server systems through registry getters. The plugin lifecycle follows a strict order: construct -> preLoad -> setup -> start -> shutdown.

## Class Hierarchy

```
PluginBase (abstract)
├── JavaPlugin (abstract) <- Your plugins extend this
└── [Internal plugin types]
```

## Core Classes

| Class | Type | Description |
|-------|------|-------------|
| [JavaPlugin](JavaPlugin.md) | Abstract Class | Base class for all user plugins |
| [JavaPluginInit](JavaPluginInit.md) | Class | Initialization context passed to plugin constructor |
| [PluginBase](PluginBase.md) | Abstract Class | Internal base with all registry accessors |
| [PluginManager](PluginManager.md) | Class | Manages plugin loading/unloading |
| [PluginState](PluginState.md) | Enum | Plugin lifecycle states |
| [PluginType](PluginType.md) | Enum | Types of plugins (currently only PLUGIN) |

## Registry Classes

| Class | Type | Description |
|-------|------|-------------|
| [IRegistry](registry/IRegistry.md) | Interface | Base interface for all registries |
| [AssetRegistry](registry/AssetRegistry.md) | Class | Register custom asset stores |
| [CodecMapRegistry](registry/CodecMapRegistry.md) | Class | Register codecs for serialization |
| [MapKeyMapRegistry](registry/MapKeyMapRegistry.md) | Class | Register map-key based codecs |

## Plugin Lifecycle

```
Constructor           Called when plugin JAR is loaded
     │                - Receive JavaPluginInit
     │                - Store instance reference
     │                - Basic field initialization only
     ▼
preLoad()             Async pre-loading (optional override)
     │                - Initialize managers/systems
     │                - Load configuration
     │                - Return CompletableFuture<Void>
     ▼
setup()               Registration phase (override this)
     │                - Register commands
     │                - Register events
     │                - Register ECS systems
     │                - Register custom entities
     ▼
start()               Plugin activation (optional override)
     │                - Start background tasks
     │                - Initialize runtime systems
     │                - Plugin is now fully active
     ▼
shutdown()            Cleanup phase (optional override)
                      - Cancel scheduled tasks
                      - Save state
                      - Release resources
```

## Available Registries

All registries are accessed via `PluginBase` getter methods:

| Registry | Getter | Purpose |
|----------|--------|---------|
| Commands | `getCommandRegistry()` | Register slash commands |
| Events | `getEventRegistry()` | Subscribe to game events |
| Tasks | `getTaskRegistry()` | Track async tasks |
| Entities | `getEntityRegistry()` | Register custom entity types |
| Entity Components | `getEntityStoreRegistry()` | Register ECS systems for entities |
| Chunk Components | `getChunkStoreRegistry()` | Register ECS systems for chunks |
| Block States | `getBlockStateRegistry()` | Register custom block state logic |
| Assets | `getAssetRegistry()` | Register custom asset stores |
| Codecs | `getCodecRegistry()` | Register serialization codecs |
| Client Features | `getClientFeatureRegistry()` | Register client-side features |

## Quick Start Example

```java
package io.example;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class MyPlugin extends JavaPlugin {

    private static MyPlugin instance;

    public MyPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
        getLogger().atInfo().log("MyPlugin loading...");
    }

    public static MyPlugin getInstance() {
        return instance;
    }

    @Override
    public CompletableFuture<Void> preLoad() {
        // Optional: async initialization
        return CompletableFuture.completedFuture(null);
    }

    @Override
    protected void setup() {
        // Register commands
        getCommandRegistry().registerCommand(new MyCommand());

        // Register events
        getEventRegistry().register(PlayerJoinEvent.class, this::onPlayerJoin);

        // Register ECS systems
        getEntityStoreRegistry().registerSystem(new MyEntitySystem());
    }

    @Override
    protected void start() {
        getLogger().atInfo().log("MyPlugin started!");
    }

    @Override
    protected void shutdown() {
        getLogger().atInfo().log("MyPlugin shutting down...");
    }

    private void onPlayerJoin(PlayerJoinEvent event) {
        // Handle player join
    }
}
```

## Manifest Requirements

Your plugin must have a `manifest.json` in the JAR root:

```json
{
  "Name": "MyPlugin",
  "Version": "1.0.0",
  "Main": "io.example.MyPlugin",
  "Authors": ["Your Name"],
  "Description": "My awesome plugin"
}
```

## Common Patterns

### Singleton Access
```java
private static MyPlugin instance;

public MyPlugin(@Nonnull JavaPluginInit init) {
    super(init);
    instance = this;
}

public static MyPlugin getInstance() {
    return instance;
}
```

### Getting Plugin Data Directory
```java
Path configFile = getDataDirectory().resolve("config.json");
```

### Checking Plugin State
```java
if (getState() == PluginState.ENABLED) {
    // Plugin is fully running
}
```

## Gotchas

1. **Constructor limitations**: Only do minimal initialization in the constructor. Heavy work should go in `preLoad()` or `setup()`.

2. **Thread safety**: `preLoad()` may run on a different thread. Use `CompletableFuture` properly.

3. **Registration order**: Commands, events, and systems MUST be registered in `setup()`, not the constructor.

4. **Shutdown cleanup**: Always cancel scheduled tasks and release resources in `shutdown()`.

5. **Logger usage**: Use `getLogger()` instead of creating custom loggers for proper plugin identification.

## Related Packages

- [server.core.command](../command/_index.md) - Command framework
- [event](../../event/_index.md) - Event system
- [component](../../component/_index.md) - ECS system
- [server.core.task](../task/_index.md) - Task scheduling
