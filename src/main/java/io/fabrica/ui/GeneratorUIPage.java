package io.fabrica.ui;

import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.BasicCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import io.fabrica.api.power.IPowerNetworkManager;
import io.fabrica.machine.GeneratorMachine;

import javax.annotation.Nonnull;

/**
 * Custom UI page for the Generator machine.
 * Shows fuel status, power output, and energy buffer.
 */
public class GeneratorUIPage extends BasicCustomUIPage {

    private final GeneratorMachine generator;

    public GeneratorUIPage(@Nonnull PlayerRef playerRef, @Nonnull GeneratorMachine generator) {
        super(playerRef, CustomPageLifetime.CanDismiss);
        this.generator = generator;
    }

    @Override
    public void build(@Nonnull UICommandBuilder commands) {
        // Load the UI template - use just filename per Hytale docs
        commands.append("GeneratorGUI.ui");

        // Set dynamic values - use #ElementId.TextSpans for label text
        String powerOutput = IPowerNetworkManager.formatPower(generator.getCurrentProductionRate());
        commands.set("#PowerLabel.TextSpans", Message.raw("Output: " + powerOutput));

        // Show fuel info if available
        if (!generator.getFuelSlot().isEmpty()) {
            String fuelInfo = generator.getFuelSlot().getItemId() + " x" + generator.getFuelSlot().getCount();
            commands.set("#Title.TextSpans", Message.raw("Generator - " + fuelInfo));
        }
    }
}
