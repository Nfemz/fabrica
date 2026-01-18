# Hytale Plugin API Reference

## Plugin Lifecycle

Plugins extend `JavaPlugin` (`com.hypixel.hytale.server.core.plugin.JavaPlugin`):

```java
public class MyPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public MyPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void preLoad() {
        // Async config loading before initialization
    }

    @Override
    protected void setup() {
        // Register commands, events, assets
    }

    @Override
    protected void start() {
        // Post-setup logic, all plugins initialized
    }

    @Override
    protected void shutdown() {
        // Cleanup before registry finalization
    }
}
```

## Available Registries

| Registry | Access Method | Purpose |
|----------|---------------|---------|
| CommandRegistry | `getCommandRegistry()` | Player/console commands |
| EventRegistry | `getEventRegistry()` | Game event listeners |
| TaskRegistry | `getTaskRegistry()` | Async/scheduled tasks |
| EntityRegistry | `getEntityRegistry()` | Custom entity types |
| BlockStateRegistry | `getBlockStateRegistry()` | Custom block states |
| AssetRegistry | `getAssetRegistry()` | Textures, models, sounds |
| EntityStoreRegistry | `getEntityStoreRegistry()` | Entity ECS components |
| ChunkStoreRegistry | `getChunkStoreRegistry()` | Chunk ECS components |
| ClientFeatureRegistry | `getClientFeatureRegistry()` | Client behavior features |

## Utility Methods

- `getLogger()` → `HytaleLogger` for logging
- `getDataDirectory()` → `Path` for plugin config/data folder
- `getManifest()` → `PluginManifest` for metadata access
- `withConfig()` → Configuration file registration

---

## Commands

### Basic Command (CommandBase)

```java
public class MyCommand extends CommandBase {
    public MyCommand() {
        super("commandname", "Description");
        this.setPermissionGroup(GameMode.Adventure); // Allows all players
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(Message.raw("Response text"));
    }
}
```

### Player Command with ECS Access (AbstractPlayerCommand)

Use when accessing player components (thread-safe ECS access):

```java
public class WhereAmICommand extends AbstractPlayerCommand {
    public WhereAmICommand() {
        super("whereami", "Shows your location");
        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void execute(@Nonnull CommandContext ctx,
            @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
        player.sendMessage(Message.raw("Position: " + transform.getPosition()));
    }
}
```

### Command Arguments

```java
// In constructor:
RequiredArg<String> messageArg = this.withRequiredArg("message", "The message", ArgTypes.STRING);
RequiredArg<Integer> countArg = this.withRequiredArg("count", "Number", ArgTypes.INTEGER);

// In execute:
String message = messageArg.get(ctx);
int count = countArg.get(ctx);
```

**Argument Types**: `ArgTypes.STRING`, `INTEGER`, `BOOLEAN`, `FLOAT`, `DOUBLE`, `UUID`

### Registration

```java
@Override
protected void setup() {
    this.getCommandRegistry().registerCommand(new MyCommand());
}
```

---

## Events

### Standard Event Registration

```java
// Basic registration
getEventRegistry().register(PlayerJoinEvent.class, this::onPlayerJoin);

// With priority (FIRST, EARLY, NORMAL, LATE, LAST)
getEventRegistry().register(EventPriority.EARLY, SomeEvent.class, this::handleEarly);

// Global registration (all instances)
getEventRegistry().registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);

// Keyed events (specific context)
getEventRegistry().register(WorldEvent.class, "world_name", this::onWorldEvent);
```

### Event Handler Example

```java
public void onPlayerReady(PlayerReadyEvent event) {
    Player player = event.getPlayer();
    player.sendMessage(Message.raw("Welcome " + player.getDisplayName()));
}
```

### Common Events

- **Server**: `BootEvent`, `ShutdownEvent`, `PluginSetupEvent`
- **World**: `AddWorldEvent`, `RemoveWorldEvent`, `AllWorldsLoadedEvent`
- **Player**: `PlayerConnectEvent`, `PlayerDisconnectEvent`, `PlayerChatEvent`, `PlayerReadyEvent`
- **Block**: `PlaceBlockEvent`, `BreakBlockEvent`, `DamageBlockEvent` (all implement `ICancellable`)
- **Entity**: `EntityRemoveEvent`, `EntitySpawnEvent`

### Cancellable Events

```java
public void onBlockBreak(BreakBlockEvent event) {
    if (someCondition) {
        event.setCancelled(true);
    }
}
```

### ECS Entity Events (EntityEventSystem)

For events in the Entity Component System:

```java
class CancelCraftSystem extends EntityEventSystem<EntityStore, CraftRecipeEvent.Pre> {
    public CancelCraftSystem() {
        super(CraftRecipeEvent.Pre.class);
    }

    @Override
    public void handle(int index, @Nonnull ArchetypeChunk<EntityStore> chunk,
            @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> buffer,
            @Nonnull CraftRecipeEvent.Pre event) {
        if (shouldCancel(event.getCraftedRecipe())) {
            event.setCancelled(true);
        }
    }

    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}

// Registration in setup():
this.getEntityStoreRegistry().registerSystem(new CancelCraftSystem());
```

---

## Task Scheduling & Async

### TaskRegistry

```java
TaskRegistry tasks = getTaskRegistry();

// Register CompletableFuture (auto-cancelled on plugin disable)
CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
    // Background work
});
tasks.registerTask(future);
```

### Scheduled Tasks

```java
// Using HytaleServer.SCHEDULED_EXECUTOR (single-threaded)
ScheduledFuture<?> scheduled = HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(
    () -> saveData(),
    5, 5, TimeUnit.MINUTES
);
getTaskRegistry().registerTask((ScheduledFuture<Void>) scheduled);
```

### World Thread Execution

World state must only be modified on the world thread:

```java
world.execute(() -> {
    // Safe to modify blocks, entities here
});

// Async load then apply on world thread
CompletableFuture.supplyAsync(() -> loadData())
    .thenAcceptAsync(data -> applyData(data), world);
```

**Never** block the world thread with `.join()` or `Thread.sleep()`.

---

## Entity Spawning

Requires understanding of the Entity Component System (ECS).

```java
world.execute(() -> {
    Store<EntityStore> store = world.getEntityStore().getStore();
    Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();

    // Model
    ModelAsset modelAsset = ModelAsset.getAssetMap().getAsset("Minecart");
    Model model = Model.createScaledModel(modelAsset, 1.0f);

    // Required components
    holder.ensureComponent(UUIDComponent.getComponentType());
    // Add TransformComponent, ModelComponent, BoundingBox, NetworkId, etc.

    store.addEntity(holder, AddReason.SPAWN);
});
```

---

## Permissions

### Permission Checking

```java
PermissionsModule permissions = PermissionsModule.get();
boolean canTeleport = permissions.hasPermission(playerUuid, "hytale.command.teleport");
```

### Built-in Permission Groups

- **OP**: Has `*` wildcard (all permissions)
- **Default**: Assigned to all players

### GameMode-Based Permissions

```java
this.setPermissionGroup(GameMode.Adventure); // All players
this.setPermissionGroup(GameMode.Creative);  // Creative mode only
this.requirePermission(HytalePermissions.fromCommand("mycommand.use"));
```

---

## Logging

```java
private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

LOGGER.atInfo().log("Information message");
LOGGER.atWarning().log("Warning message");
LOGGER.atSevere().withCause(exception).log("Error occurred");
```
