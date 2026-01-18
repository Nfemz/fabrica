# Fabrica - Hytale Factory Automation Mod

A factory automation mod for Hytale - from steam age to quantum tech.

**Early Access Warning**: Hytale is in Early Access. APIs are subject to breaking changes.

## Specialized Context

For detailed documentation, see these files:

| Topic | File | When to Read |
|-------|------|--------------|
| Plugin API | [/context/hytale-api.md](.claude/context/hytale-api.md) | Writing Java plugin code (commands, events, tasks, ECS) |
| Asset Formats | [/context/asset-formats.md](.claude/context/asset-formats.md) | Creating blocks, items, recipes, or UI files |
| Troubleshooting | [/context/troubleshooting.md](.claude/context/troubleshooting.md) | Debugging build, runtime, or asset errors |

---

## Quick Reference

### File Locations

| Purpose | Path |
|---------|------|
| Plugin code | `src/main/java/io/fabrica/` |
| Plugin manifest | `src/main/resources/manifest.json` |
| Block/Item definitions | `src/main/resources/Server/Item/Items/` |
| Recipes | `src/main/resources/Server/Item/Recipes/` |
| Block textures | `src/main/resources/Common/BlockTextures/` |
| Custom UI files | `src/main/resources/Common/UI/Custom/` |
| Build output | `build/libs/Fabrica-<version>.jar` |
| Version config | `gradle.properties` |

### Build Commands

```bash
./gradlew build           # Build plugin JAR
./gradlew compileJava     # Quick compile check
./gradlew clean build     # Full rebuild
```

### Common Tasks

| Task | Action |
|------|--------|
| Add a command | Create class extending `CommandBase`, register in `FabricaPlugin.setup()` |
| Add a block/item | Create JSON in `Server/Item/Items/`, add textures to `Common/BlockTextures/` |
| Add a recipe | Create JSON in `Server/Item/Recipes/` using Input array format (NOT Pattern/Key) |
| Add custom UI | Create `.ui` file in `Common/UI/Custom/` using CSS-like syntax (NOT JSON) |

---

## Development Workflow

### Prerequisites

- Java 25 SDK
- Hytale launcher with game installed
- `hytale_home` set in `gradle.properties` (currently: `/mnt/c/Users/femia/AppData/Roaming/Hytale`)

### Build → Deploy → Test

1. Make changes
2. Run `./gradlew build`
3. Use `/deploy-plugin` skill to deploy to mods folder
4. Restart Hytale client
5. Connect to `127.0.0.1` and test

### First-Time Server Setup

```
auth login device       # Follow device code flow
auth persistence Encrypted
```

---

## LLM Guidelines

### Before Making Changes
1. Read relevant source files first
2. Check `manifest.json` Main class matches `io.fabrica.FabricaPlugin`
3. For assets, verify all referenced textures exist

### After Making Changes
1. Run `./gradlew build` to verify
2. Check for errors - asset errors appear in client logs at `%APPDATA%/Hytale/UserData/Logs/`
3. Remind user to restart client if testing

### Critical Gotchas

| Topic | Gotcha |
|-------|--------|
| Recipes | Use `Input` array format, NOT Minecraft Pattern/Key |
| UI Files | Use CSS-like `.ui` syntax, NOT JSON |
| UI Alignment | Use `HorizontalAlignment`, NOT `Alignment` |
| Textures | Ensure ALL referenced textures exist before testing |
| Blocks | Must have `PlayerAnimationsId: "Block"` and `HitboxType: "Full"` to be placeable |

---

## Blockbench MCP (3D Modeling)

Blockbench runs on Windows, Claude Code runs in WSL. The MCP connection requires the Windows host IP.

**If Blockbench MCP fails to connect after reboot**, run this to update the IP:
```bash
NEW_IP=$(ip route show | grep -i default | awk '{print $3}') && sed -i "s|http://[0-9.]*:3000/bb-mcp|http://$NEW_IP:3000/bb-mcp|g" ~/.claude.json && echo "Updated to $NEW_IP"
```
Then restart Claude Code and run `/mcp`.

---

## External Documentation

- [Hytale Modding Documentation (Britakee Studios)](https://britakee-studios.gitbook.io/hytale-modding-documentation)
- [HytaleDocs Community Wiki](https://hytale-docs.com/docs/modding/plugins/overview)
