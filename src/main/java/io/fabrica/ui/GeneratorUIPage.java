package io.fabrica.ui;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.BasicCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import io.fabrica.api.power.IPowerNetworkManager;
import io.fabrica.machine.GeneratorMachine;
import io.fabrica.machine.ItemStack;

import javax.annotation.Nonnull;

/**
 * Custom UI page for the Generator machine.
 * Shows fuel status, burn progress, power output, and energy buffer.
 *
 * Note: Full interactive features (drag-and-drop) require further SDK investigation.
 * Currently displays machine state in a polished read-only format.
 */
public class GeneratorUIPage extends BasicCustomUIPage {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final GeneratorMachine generator;

    public GeneratorUIPage(@Nonnull PlayerRef playerRef, @Nonnull GeneratorMachine generator) {
        super(playerRef, CustomPageLifetime.CanDismiss);
        this.generator = generator;
    }

    @Override
    public void build(@Nonnull UICommandBuilder commands) {
        // Load the UI template
        commands.append("GeneratorGUI.ui");

        // Update all dynamic values
        updateDisplayValues(commands);
    }

    /**
     * Updates all dynamic display values in the UI.
     */
    private void updateDisplayValues(@Nonnull UICommandBuilder commands) {
        // Power output display
        String powerOutput = IPowerNetworkManager.formatPower(generator.getCurrentProductionRate());
        commands.set("#PowerLabel.TextSpans", Message.raw(powerOutput));

        // Energy display text
        double energyPercent = generator.getStoredEnergy() / GeneratorMachine.INTERNAL_BUFFER;
        String energyText = String.format("%.0f / %.0f J",
                generator.getStoredEnergy(), GeneratorMachine.INTERNAL_BUFFER);
        commands.set("#EnergyLabel.TextSpans", Message.raw(energyText));

        // Fuel slot display
        ItemStack fuel = generator.getFuelSlot();
        if (!fuel.isEmpty()) {
            commands.set("#FuelCountLabel.TextSpans", Message.raw(String.valueOf(fuel.getCount())));
            commands.set("#FuelItemLabel.TextSpans", Message.raw(formatItemName(fuel.getItemId())));
        } else {
            commands.set("#FuelCountLabel.TextSpans", Message.raw(""));
            commands.set("#FuelItemLabel.TextSpans", Message.raw("Empty"));
        }

        // Log state for debugging
        double burnProgress = generator.getBurnProgress();
        boolean isActive = generator.isProducing();
        LOGGER.atInfo().log(String.format("Generator UI: power=%s, burn=%.2f, energy=%.2f, active=%s",
                powerOutput, burnProgress, energyPercent, isActive));
    }

    /**
     * Formats an item ID for display (e.g., "Oak_Log" -> "Oak Log").
     */
    private String formatItemName(String itemId) {
        if (itemId == null || itemId.isEmpty()) {
            return "";
        }
        return itemId.replace("_", " ");
    }
}
