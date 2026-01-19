# CodecMapRegistry

**Package:** `com.hypixel.hytale.server.core.plugin.registry`
**Type:** Generic Class
**Implements:** [IRegistry](IRegistry.md)

## Overview

Registry for codecs (serializers/deserializers) mapped by string keys. Used to register custom types for JSON serialization in the Hytale asset system.

## Type Parameters

| Parameter | Constraint | Description |
|-----------|------------|-------------|
| T | - | The type being serialized |
| C | extends Codec<? extends T> | The codec type |

## Constructor

```java
public CodecMapRegistry(
    List<BooleanConsumer> callbacks,
    StringCodecMapCodec<T, C> codec
)
```
Internal constructor - use `getCodecRegistry(codec)` from your plugin.

## Methods

### register(String, Class, Codec)
```java
public CodecMapRegistry<T, C> register(
    String key,
    Class<? extends T> type,
    C codec
)
```
Register a codec with default priority.

| Parameter | Type | Description |
|-----------|------|-------------|
| key | String | Unique key for this type |
| type | Class | The class being serialized |
| codec | C | The codec instance |

**Returns:** `this` for method chaining

### register(Priority, String, Class, Codec)
```java
public CodecMapRegistry<T, C> register(
    Priority priority,
    String key,
    Class<? extends T> type,
    C codec
)
```
Register a codec with specific priority.

| Parameter | Type | Description |
|-----------|------|-------------|
| priority | Priority | Registration priority |
| key | String | Unique key for this type |
| type | Class | The class being serialized |
| codec | C | The codec instance |

**Returns:** `this` for method chaining

### shutdown()
```java
public void shutdown()
```
Clean up registered codecs. Called automatically.

## Example Usage

### Registering a Custom Codec

```java
// Define your type
public class MyCustomType {
    private String name;
    private int value;

    // Constructor, getters, setters
}

// Create a codec
public class MyCustomTypeCodec implements Codec<MyCustomType> {
    @Override
    public MyCustomType decode(JsonElement json, DecodeContext ctx) {
        JsonObject obj = json.getAsJsonObject();
        return new MyCustomType(
            obj.get("name").getAsString(),
            obj.get("value").getAsInt()
        );
    }

    @Override
    public JsonElement encode(MyCustomType value, EncodeContext ctx) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", value.getName());
        obj.addProperty("value", value.getValue());
        return obj;
    }
}

// In your plugin setup
@Override
protected void setup() {
    getCodecRegistry(SomeCodecMapCodec.INSTANCE)
        .register("mycustom", MyCustomType.class, new MyCustomTypeCodec());
}
```

## When to Use

- Adding custom block/item properties that need serialization
- Creating new asset types with custom JSON structures
- Extending existing Hytale types with additional data

## Gotchas

1. **Codec selection**: You need the appropriate `StringCodecMapCodec` for the type you're extending.

2. **Key uniqueness**: Keys must be unique within the codec map. Consider namespacing (e.g., "myplugin:mytype").

3. **Priority**: Higher priority codecs are tried first during deserialization.

4. **Registration timing**: Must be done in `setup()`.

## Related

- [IRegistry](IRegistry.md) - Base interface
- [MapKeyMapRegistry](MapKeyMapRegistry.md) - Alternative registry type
- [PluginBase](../PluginBase.md) - `getCodecRegistry()` method
