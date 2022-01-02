package me.nesox.brush.listeners;

import me.nesox.brush.Brush;
import me.nesox.brush.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerDeath implements Listener {
    private final FileConfiguration config = Brush.getInstance().getConfig();
    private static final HashMap<Player, ArrayList<ItemStack>> saveBrush = new HashMap<>();

    @EventHandler
    public void onDeathSaveBrush(PlayerDeathEvent event) {


        if (!config.getBoolean("settings.player-death-save-brush")) return;

        List<ItemStack> items = event.getDrops();

        if (items.isEmpty()) return;

        ArrayList<ItemStack> brushes = new ArrayList<>();
        for (ItemStack item : items) {
            if (item.getType() == Material.getMaterial(config.getString("brush.item"))) {
                if (item.getItemMeta().getDisplayName().contains(ChatUtil.fixColors(config.getString("brush.name").replace("%level%", "")))) {
                    brushes.add(item);
                }
            }
        }

        if (!brushes.isEmpty()) {
            for (ItemStack item : brushes) {
                event.getDrops().remove(item);
            }
            saveBrush.put(event.getEntity(), brushes);
        }

        }

    public static HashMap<Player, ArrayList<ItemStack>> getSaveBrush() {
        return saveBrush;
    }
}
