package me.nesox.brush.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.nesox.brush.Brush;
import me.nesox.brush.utils.ChatUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;


public class PlayerInteract implements Listener {
    private final FileConfiguration config = Brush.getInstance().getConfig();

    private boolean isOnRegion(Block block) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(block.getWorld()));
        for (ProtectedRegion region : regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(block.getLocation()))) {
            if (config.getStringList("settings.disabled-regions").contains(region.getId())) {
                return true;
            }
        }
        return false;
    }


    @EventHandler
    public void onClickWithBrush(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        PlayerInventory playerInventory = player.getInventory();

        if (playerInventory.getItemInMainHand().getType() == Material.AIR) return;
        ItemMeta itemInMainHand = playerInventory.getItemInMainHand().getItemMeta();
        if (!(playerInventory.getItemInMainHand().getType() == Material.getMaterial(config.getString("brush.item")))) return;
        if (!itemInMainHand.getDisplayName().contains(ChatUtil.fixColors(config.getString("brush.name").replace("%level%", "")))) return;
        if (clickedBlock == null) return;
        event.setCancelled(true);

        if (clickedBlock.getType() != Material.AIR) {
            if (itemInMainHand.getLore().get(0).replace("§6", "").equals("1")) {
                if (clickedBlock.getType() == Material.BEDROCK) return;

                if (isOnRegion(clickedBlock)) {
                    player.sendMessage(ChatUtil.fixColors(config.getString("messages.brush-use-on-region")));
                    return;
                }

                if (config.getBoolean("settings.drop-items")) {
                    clickedBlock.breakNaturally();
                } else {
                    clickedBlock.setType(Material.AIR);
                }

                Brush.getInstance().getServer().getPluginManager().callEvent(new BlockBreakEvent(clickedBlock, player));
            } else {
                int level = Integer.parseInt(itemInMainHand.getLore().get(0).replace("§6", "")) - 1;

                //algorithm from spigotmc because algorithms are for bosses so not for me :)
                for (int x = clickedBlock.getX() - level; x <= clickedBlock.getX() + level; x++) {
                    for (int y = clickedBlock.getY() - level; y <= clickedBlock.getY() + level; y++) {
                        for (int z = clickedBlock.getZ() - level; z <= clickedBlock.getZ() + level; z++) {
                            Block blockToDestroy = player.getWorld().getBlockAt(x, y, z);
                            if (blockToDestroy.getType() != Material.BEDROCK) {
                                if (isOnRegion(blockToDestroy)) {
                                    player.sendMessage(ChatUtil.fixColors(config.getString("messages.brush-use-on-region")));
                                    return;
                                } else {

                                    if (config.getBoolean("settings.drop-items")) {
                                        blockToDestroy.breakNaturally();
                                    } else {
                                        blockToDestroy.setType(Material.AIR);
                                    }

                                    Brush.getInstance().getServer().getPluginManager().callEvent(new BlockBreakEvent(blockToDestroy, player));
                                }
                            }

                        }
                    }
                }


            }

        }

    }



}
