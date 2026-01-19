# JavaPluginInit

**Package:** `com.hypixel.hytale.server.core.plugin`
**Type:** Class
**Extends:** [PluginInit](PluginInit.md)

## Overview

The initialization context passed to `JavaPlugin` constructors. Contains the plugin manifest, file paths, and classloader. You receive this as a constructor parameter but rarely need to use it directly.

## Constructor

```java
public JavaPluginInit(
    PluginManifest manifest,
    Path dataDirectory,
    Path file,
    PluginClassLoader classLoader
)
```

| Parameter | Type | Description |
|-----------|------|-------------|
| manifest | PluginManifest | Parsed plugin manifest |
| dataDirectory | Path | Plugin's data directory path |
| file | Path | Path to the plugin JAR file |
| classLoader | PluginClassLoader | ClassLoader for this plugin |

## Methods

### getFile()
```java
public Path getFile()
```
**Returns:** Path to the plugin's JAR file

### getClassLoader()
```java
public PluginClassLoader getClassLoader()
```
**Returns:** The plugin's class loader

### isInServerClassPath()
```java
public boolean isInServerClassPath()
```
**Returns:** `true` if the plugin is in the server's classpath (not typical)

## Inherited Methods (from PluginInit)

### getPluginManifest()
```java
public PluginManifest getPluginManifest()
```
**Returns:** The parsed plugin manifest

### getDataDirectory()
```java
public Path getDataDirectory()
```
**Returns:** Path to the plugin's data directory

## Example Usage

```java
public class MyPlugin extends JavaPlugin {
    public MyPlugin(@Nonnull JavaPluginInit init) {
        super(init);  // Pass to parent

        // You can access init data before calling super, but it's rarely needed
        // Most commonly, just pass it to super and use getManifest(), etc. later
    }
}
```

## When You Might Use Init Directly

While most plugins just pass `init` to `super()`, you might access it directly for:

1. **Early logging before super()**: (not recommended)
2. **Conditional initialization**: Check manifest before super
3. **Debug/testing**: Inspect initialization parameters

```java
public MyPlugin(@Nonnull JavaPluginInit init) {
    // Check something before initialization (rare case)
    if (init.getPluginManifest().getDependencies().isEmpty()) {
        System.out.println("No dependencies");
    }

    super(init);  // Always call this

    // After super(), use inherited getters instead:
    getLogger().atInfo().log("JAR: " + getFile());
}
```

## Gotchas

1. **Always pass to super**: The `init` object must be passed to `super(init)`.

2. **Prefer inherited methods**: After construction, use `getManifest()`, `getDataDirectory()`, etc. from `JavaPlugin` instead of storing the init object.

3. **Don't store init**: The init object is only meant for the construction phase.

## Related

- [PluginInit](PluginInit.md) - Parent class
- [JavaPlugin](JavaPlugin.md) - Receives this in constructor
- [PluginManifest](PluginManifest.md) - Manifest data structure
