# MapKeyMapRegistry

**Package:** `com.hypixel.hytale.server.core.plugin.registry`
**Type:** Generic Class
**Implements:** [IRegistry](IRegistry.md)

## Overview

Registry for codecs keyed by map keys (typically enums or other structured keys). Similar to `CodecMapRegistry` but uses typed keys instead of strings.

## Type Parameters

| Parameter | Description |
|-----------|-------------|
| V | The value type being serialized |

## Constructor

```java
public MapKeyMapRegistry(
    List<BooleanConsumer> callbacks,
    MapKeyMapCodec<V> codec
)
```
Internal constructor - use `getCodecRegistry(codec)` from your plugin.

## Methods

### register()
```java
public <T extends V> MapKeyMapRegistry<V> register(
    Class<T> type,
    String key,
    Codec<T> codec
)
```
Register a codec for a specific type.

| Parameter | Type | Description |
|-----------|------|-------------|
| type | Class<T> | The class being serialized |
| key | String | String key for this type |
| codec | Codec<T> | The codec instance |

**Returns:** `this` for method chaining

### shutdown()
```java
public void shutdown()
```
Clean up registered codecs. Called automatically.

## Example Usage

```java
// In your plugin setup
@Override
protected void setup() {
    getCodecRegistry(SomeMapKeyMapCodec.INSTANCE)
        .register(MyType.class, "mytype", new MyTypeCodec());
}
```

## Difference from CodecMapRegistry

| Feature | CodecMapRegistry | MapKeyMapRegistry |
|---------|------------------|-------------------|
| Key type | String | Typed (often enum) |
| Type param | `<T, C>` | `<V>` |
| Use case | String-keyed lookups | Type-safe key lookups |

## When to Use

- When the codec map uses typed keys instead of strings
- When extending type-safe registry systems
- When the Hytale API provides a `MapKeyMapCodec` for the system you're extending

## Gotchas

1. **Codec type**: Must match the `MapKeyMapCodec` type expected by the system.

2. **Type safety**: The generic type `V` constrains what types can be registered.

3. **Registration timing**: Must be done in `setup()`.

## Related

- [IRegistry](IRegistry.md) - Base interface
- [CodecMapRegistry](CodecMapRegistry.md) - String-keyed alternative
- [PluginBase](../PluginBase.md) - `getCodecRegistry()` method
