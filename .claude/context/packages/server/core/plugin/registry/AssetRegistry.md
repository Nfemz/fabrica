# AssetRegistry

**Package:** `com.hypixel.hytale.server.core.plugin.registry`
**Type:** Class

## Overview

Registry for custom asset stores. Used to register plugin-specific asset types that can be loaded from JSON files.

## Constructor

```java
public AssetRegistry(List<BooleanConsumer> callbacks)
```
Internal constructor - use `getAssetRegistry()` from your plugin instead.

## Methods

### register()
```java
public <K, T extends JsonAssetWithMap<K, M>, M extends AssetMap<K, T>,
        S extends AssetStore<K, T, M>>
    AssetRegistry register(S store)
```
Register a custom asset store.

| Type Parameter | Description |
|----------------|-------------|
| K | Key type for the assets |
| T | Asset type (extends JsonAssetWithMap) |
| M | Map type for storing assets |
| S | Store type |

**Parameters:**
- `store` - The asset store to register

**Returns:** `this` for method chaining

### shutdown()
```java
public void shutdown()
```
Clean up all registered stores. Called automatically.

## Example Usage

### Creating a Custom Asset Store

```java
// Define your asset type
public class MyAsset extends JsonAssetWithMap<String, MyAssetMap> {
    private String name;
    private int value;

    // Getters, setters, codec, etc.
}

// In your plugin setup
@Override
protected void setup() {
    // Register your custom asset store
    getAssetRegistry().register(new MyAssetStore());
}
```

## Notes

Asset registration is an advanced feature used when you need to define entirely new asset types. For most plugins, you'll use the built-in block, item, and recipe asset types instead.

## When to Use

- Creating custom data-driven content types
- Defining new JSON-based configuration formats
- Building systems that need hot-reloadable data

## Gotchas

1. **Complexity**: Asset stores have complex generic signatures. Consider if simpler approaches (like custom JSON loading) would suffice.

2. **Registration timing**: Must be done in `setup()`.

3. **Automatic loading**: Registered stores will automatically load assets from the appropriate directories.

## Related

- [IRegistry](IRegistry.md) - Base interface
- [PluginBase](../PluginBase.md) - `getAssetRegistry()` method
