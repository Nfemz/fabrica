package io.fabrica.event;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.UseBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import io.fabrica.api.power.BlockPos;
import io.fabrica.machine.*;
import io.fabrica.ui.BatteryUIPage;
import io.fabrica.ui.GeneratorUIPage;
import io.fabrica.ui.ProcessingUIPage;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * ECS Event System that handles block interactions for Fabrica machines.
 * Opens the appropriate UI when a player interacts with a machine block.
 */
public class MachineInteractionSystem extends EntityEventSystem<EntityStore, UseBlockEvent.Pre> {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    // Fabrica machine block IDs
    private static final Set<String> MACHINE_BLOCK_IDS = Set.of(
        "Machine_Generator",
        "Machine_Battery",
        "Machine_Electric_Furnace",
        "Machine_Macerator"
    );

    public MachineInteractionSystem() {
        super(UseBlockEvent.Pre.class);
    }

    @Override
    public void handle(int index, @Nonnull ArchetypeChunk<EntityStore> chunk,
                       @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> buffer,
                       @Nonnull UseBlockEvent.Pre event) {

        String blockTypeId = event.getBlockType().getId();
        LOGGER.atInfo().log("UseBlockEvent.Pre fired for block: " + blockTypeId);

        // Only handle Fabrica machine blocks
        if (!MACHINE_BLOCK_IDS.contains(blockTypeId)) {
            return;
        }

        // Get the entity (player) that triggered the interaction
        Ref<EntityStore> entityRef = event.getContext().getEntity();
        if (entityRef == null) {
            return;
        }

        // Get the Player component
        Player player = store.getComponent(entityRef, Player.getComponentType());
        if (player == null) {
            return;
        }

        // Get the PlayerRef
        PlayerRef playerRef = store.getComponent(entityRef, PlayerRef.getComponentType());
        if (playerRef == null) {
            return;
        }

        // Get block position
        var targetBlock = event.getTargetBlock();
        BlockPos pos = new BlockPos(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());

        LOGGER.atInfo().log("Opening UI for " + blockTypeId + " at " + pos);

        // Open the appropriate UI based on block type
        switch (blockTypeId) {
            case "Machine_Generator" -> openGeneratorUI(player, playerRef, entityRef, store, pos);
            case "Machine_Battery" -> openBatteryUI(player, playerRef, entityRef, store, pos);
            case "Machine_Electric_Furnace", "Machine_Macerator" -> openProcessingUI(player, playerRef, entityRef, store, pos, blockTypeId);
        }

        // Cancel the default interaction
        event.setCancelled(true);
    }

    private void openGeneratorUI(Player player, PlayerRef playerRef, Ref<EntityStore> ref, Store<EntityStore> store, BlockPos pos) {
        // For now, create a temporary generator - later we'll get from MachineRegistry
        GeneratorMachine generator = new GeneratorMachine(pos);
        GeneratorUIPage page = new GeneratorUIPage(playerRef, generator);
        player.getPageManager().openCustomPage(ref, store, page);
    }

    private void openBatteryUI(Player player, PlayerRef playerRef, Ref<EntityStore> ref, Store<EntityStore> store, BlockPos pos) {
        BatteryMachine battery = new BatteryMachine(pos);
        BatteryUIPage page = new BatteryUIPage(playerRef, battery);
        player.getPageManager().openCustomPage(ref, store, page);
    }

    private void openProcessingUI(Player player, PlayerRef playerRef, Ref<EntityStore> ref, Store<EntityStore> store, BlockPos pos, String machineType) {
        ProcessingMachine machine;
        if ("Machine_Electric_Furnace".equals(machineType)) {
            machine = new ElectricFurnaceMachine(pos);
        } else {
            machine = new MaceratorMachine(pos);
        }
        ProcessingUIPage page = new ProcessingUIPage(playerRef, machine);
        player.getPageManager().openCustomPage(ref, store, page);
    }

    @Override
    public Query<EntityStore> getQuery() {
        // Match entities with Player component
        return Query.and(Player.getComponentType());
    }
}
