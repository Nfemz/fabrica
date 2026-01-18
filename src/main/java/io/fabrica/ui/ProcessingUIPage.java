package io.fabrica.ui;

import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.BasicCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import io.fabrica.api.power.IPowerNetworkManager;
import io.fabrica.machine.ProcessingMachine;

import javax.annotation.Nonnull;

/**
 * Custom UI page for processing machines (Macerator, Electric Furnace).
 * Shows processing progress, input/output, and power consumption.
 */
public class ProcessingUIPage extends BasicCustomUIPage {

    private final ProcessingMachine machine;

    public ProcessingUIPage(@Nonnull PlayerRef playerRef, @Nonnull ProcessingMachine machine) {
        super(playerRef, CustomPageLifetime.CanDismiss);
        this.machine = machine;
    }

    @Override
    public void build(@Nonnull UICommandBuilder commands) {
        // Load the UI template - use just filename per Hytale docs
        commands.append("ProcessingGUI.ui");

        // Set the title based on machine type
        commands.set("#Title.TextSpans", Message.raw(machine.getMachineTypeName()));

        // Set progress
        int progressPercent = (int) (machine.getProgress() * 100);
        String powerConsumption = IPowerNetworkManager.formatPower(machine.getConsumptionRate());
        commands.set("#ProgressLabel.TextSpans", Message.raw("Progress: " + progressPercent + "% | Power: " + powerConsumption));
    }
}
