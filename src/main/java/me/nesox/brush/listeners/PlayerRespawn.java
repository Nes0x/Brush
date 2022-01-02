package me.nesox.brush.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerRespawn implements Listener {

    @EventHandler
    public void onRespawnGiveBrush(PlayerRespawnEvent event) {
        if (PlayerDeath.getSaveBrush().containsKey(event.getPlayer())) {
            for (ItemStack item : PlayerDeath.getSaveBrush().get(event.getPlayer())) {
                event.getPlayer().getInventory().addItem(item);
            }
            PlayerDeath.getSaveBrush().remove(event.getPlayer());
        }
    }

}
