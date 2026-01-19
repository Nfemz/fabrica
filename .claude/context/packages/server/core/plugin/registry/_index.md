# server.core.plugin.registry Package

**Package:** `com.hypixel.hytale.server.core.plugin.registry`
**Classes:** 5
**Parent:** [server.core.plugin](_index.md)

## Overview

This subpackage contains the registry interfaces and implementations for registering custom codecs and asset stores.

## Classes

| Class | Type | Description |
|-------|------|-------------|
| [IRegistry](IRegistry.md) | Interface | Base interface for all registries |
| [AssetRegistry](AssetRegistry.md) | Class | Register custom asset stores |
| [CodecMapRegistry](CodecMapRegistry.md) | Class | Register string-keyed codecs |
| [CodecMapRegistry.Assets](CodecMapRegistry.md#assets) | Inner Class | Asset-specific codec registry |
| [MapKeyMapRegistry](MapKeyMapRegistry.md) | Class | Register type-keyed codecs |

## Usage

These registries are accessed through `PluginBase` methods:

```java
// In your plugin's setup()

// Asset registration
getAssetRegistry().register(myAssetStore);

// Codec registration (string-keyed)
getCodecRegistry(someStringCodecMap)
    .register("mykey", MyType.class, new MyCodec());

// Codec registration (map-keyed)
getCodecRegistry(someMapKeyCodec)
    .register(MyType.class, "mykey", new MyCodec());
```

## When to Use

These registries are for advanced use cases:

| Use Case | Registry |
|----------|----------|
| New asset types (JSON data files) | AssetRegistry |
| Custom serialization for existing types | CodecMapRegistry |
| Extending type-safe systems | MapKeyMapRegistry |

For most plugins, you won't need these registries directly. Instead use:
- `getCommandRegistry()` for commands
- `getEventRegistry()` for events
- `getEntityStoreRegistry()` for ECS systems

## Related

- [PluginBase](../PluginBase.md) - Registry accessors
- [JavaPlugin](../JavaPlugin.md) - Plugin base class
