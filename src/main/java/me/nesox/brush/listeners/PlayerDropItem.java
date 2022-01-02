package me.nesox.brush.listeners;

import me.nesox.brush.Brush;
import me.nesox.brush.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDropItem implements Listener {
    private final FileConfiguration config = Brush.getInstance().getConfig();

    @EventHandler
    public void onDropBrush(PlayerDropItemEvent event) {
        ItemStack droppedItem = event.getItemDrop().getItemStack();

        if (config.getBoolean("settings.brush-drop")) return;
        if (!(droppedItem.getType() == Material.getMaterial(config.getString("brush.item")))) return;
        if (!droppedItem.getItemMeta().getDisplayName().contains(ChatUtil.fixColors(config.getString("brush.name").replace("%level%", "")))) return;

        event.getPlayer().sendMessage(ChatUtil.fixColors(config.getString("messages.brush-drop")));
        event.setCancelled(true);
    }
}
