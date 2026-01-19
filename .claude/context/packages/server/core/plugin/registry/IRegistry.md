# IRegistry

**Package:** `com.hypixel.hytale.server.core.plugin.registry`
**Type:** Interface

## Overview

Base interface for all plugin registries. Provides a common `shutdown()` method for cleanup.

## Methods

### shutdown()
```java
void shutdown()
```
Called when the registry should clean up its resources. Typically called automatically when the plugin is disabled.

## Implementing Classes

| Class | Purpose |
|-------|---------|
| [AssetRegistry](AssetRegistry.md) | Asset store registration |
| [CodecMapRegistry](CodecMapRegistry.md) | Codec registration |
| [MapKeyMapRegistry](MapKeyMapRegistry.md) | Map-key codec registration |

## Example

```java
// Internal - you typically don't implement this yourself
public class MyRegistry implements IRegistry {
    private List<Resource> resources = new ArrayList<>();

    public void register(Resource r) {
        resources.add(r);
    }

    @Override
    public void shutdown() {
        resources.forEach(Resource::close);
        resources.clear();
    }
}
```

## Notes

Plugin developers don't typically implement `IRegistry` directly. Instead, use the registry instances provided by `PluginBase` getters.

## Related

- [AssetRegistry](AssetRegistry.md)
- [CodecMapRegistry](CodecMapRegistry.md)
- [PluginBase](../PluginBase.md) - Provides registry accessors
