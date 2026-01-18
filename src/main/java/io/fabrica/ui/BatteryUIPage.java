package io.fabrica.ui;

import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.BasicCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import io.fabrica.api.power.IPowerNetworkManager;
import io.fabrica.machine.BatteryMachine;

import javax.annotation.Nonnull;

/**
 * Custom UI page for the Battery machine.
 * Shows charge level and capacity.
 */
public class BatteryUIPage extends BasicCustomUIPage {

    private final BatteryMachine battery;

    public BatteryUIPage(@Nonnull PlayerRef playerRef, @Nonnull BatteryMachine battery) {
        super(playerRef, CustomPageLifetime.CanDismiss);
        this.battery = battery;
    }

    @Override
    public void build(@Nonnull UICommandBuilder commands) {
        // Load the UI template - use just filename per Hytale docs
        commands.append("BatteryGUI.ui");

        // Set dynamic values
        int chargePercent = (int) (battery.getChargePercentage() * 100);
        String energyDisplay = IPowerNetworkManager.formatEnergy(battery.getStoredEnergy());
        String capacityDisplay = IPowerNetworkManager.formatEnergy(battery.getMaxCapacity());

        commands.set("#ChargeLabel.TextSpans", Message.raw("Charge: " + chargePercent + "% (" + energyDisplay + " / " + capacityDisplay + ")"));
    }
}
