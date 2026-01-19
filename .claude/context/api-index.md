# Hytale Server API Documentation Index

> **Auto-generated**: Last updated 2026-01-18
> **Source**: `HytaleServer.jar` (Early Access)
> **Total Classes**: 7,154 Hytale-specific classes

## Progress Dashboard

| Priority | Packages | Classes | Documented | Progress |
|----------|----------|---------|------------|----------|
| Critical | 4 | 499 | 25 | 5% |
| High | 4 | 760 | 0 | 0% |
| Medium | 2 | 550 | 0 | 0% |
| **Total** | **10** | **1,809** | **25** | **1.4%** |

---

## Critical Packages (Document First)

These packages are essential for any plugin development.

### [server.core.plugin](packages/server/core/plugin/_index.md)
**Status**: Complete | **Classes**: 33 (10 documented)

Plugin lifecycle management, `JavaPlugin` base class, and all registries.

| Class | Status | Description |
|-------|--------|-------------|
| JavaPlugin | - | Base class for all plugins |
| JavaPluginInit | - | Plugin initialization context |
| PluginManager | - | Plugin loading/unloading |
| IRegistry | - | Registry interface |
| AssetRegistry | - | Asset registration |
| CodecMapRegistry | - | Codec registration |

### [server.core.command](packages/server/core/command/_index.md)
**Status**: Complete | **Classes**: 337 (15 core classes documented)

Command framework including base classes, argument types, and execution.

| Subpackage | Classes | Description |
|------------|---------|-------------|
| system | ~45 | Command framework core |
| system.arguments | ~64 | Argument parsing |
| commands | ~100+ | Built-in commands |

### [event](packages/event/_index.md)
**Status**: Not Started | **Classes**: 23

Event system base classes and common events.

### [component](packages/component/_index.md)
**Status**: Not Started | **Classes**: 106

Entity Component System (ECS) implementation - Store, Ref, Query.

---

## High Priority Packages

### [server.core.entity](packages/server/core/entity/_index.md)
**Status**: Not Started | **Classes**: 92

Entity system including Player, Entity, and related components.

### [server.core.ui](packages/server/core/ui/_index.md)
**Status**: Not Started | **Classes**: 20

Custom UI pages and server-side UI management.

### [server.core.universe.world](packages/server/core/universe/world/_index.md)
**Status**: Not Started | **Classes**: 294

World access, chunk management, block operations.

### [protocol.packets](packages/protocol/_index.md)
**Status**: Not Started | **Classes**: 354

Network protocol packets for client-server communication.

---

## Medium Priority Packages

### [server.core.asset](packages/server/core/asset/_index.md)
**Status**: Not Started | **Classes**: ~150

Asset loading, validation, and management.

### [server.core.modules](packages/server/core/modules/_index.md)
**Status**: Not Started | **Classes**: ~400

Built-in game systems: damage, physics, collision, interaction.

---

## Category Documentation

Cross-cutting topics that span multiple packages.

| Topic | File | Description |
|-------|------|-------------|
| ECS Architecture | [categories/ecs-architecture.md](categories/ecs-architecture.md) | How the Entity Component System works |
| Threading Model | [categories/threading-model.md](categories/threading-model.md) | Server threading and task scheduling |
| Event Patterns | [categories/event-patterns.md](categories/event-patterns.md) | Common event usage patterns |

---

## Quick Links

- [Original Quick Reference](hytale-api.md) - Concise API summary
- [Asset Formats](asset-formats.md) - Block/Item/Recipe JSON formats
- [Troubleshooting](troubleshooting.md) - Common issues and solutions

---

## Tools

### Decompilation
```bash
# Decompile a specific package
./scripts/decompile-package.sh server.core.plugin

# View class signatures without decompilation
javap -classpath "$HYTALE_JAR" com.hypixel.hytale.server.core.plugin.JavaPlugin
```

### Package Discovery
```bash
# List all classes in a package
jar tf "$HYTALE_JAR" | grep "server/core/plugin.*\.class$"
```
