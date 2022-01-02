package me.nesox.brush.commands;

import me.nesox.brush.Brush;
import me.nesox.brush.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Brushes implements CommandExecutor {
    private final FileConfiguration config = Brush.getInstance().getConfig();

    private ItemStack brushItem(String level) {
        ItemStack brush = new ItemStack(Material.getMaterial(config.getString("brush.item")), 1);
        ItemMeta brushMeta = brush.getItemMeta();
        brushMeta.setDisplayName(ChatUtil.fixColors(config.getString("brush.name").replace("%level%", level)));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + level);
        brushMeta.setLore(lore);
        brush.setItemMeta(brushMeta);


        return brush;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!((sender) instanceof Player)) {
            Bukkit.getLogger().info(ChatUtil.fixColors(config.getString("messages.console-execute-command")));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("brush.give")) {
            player.sendMessage(ChatUtil.fixColors(me.nesox.brush.Brush.getInstance().getConfig().getString("messages.no-permission")));
            return true;
        }

        if (args.length == 3) {
            if (Integer.parseInt(args[2]) > config.getInt("brush.max-level")) {
                player.sendMessage(ChatUtil.fixColors(config.getString("messages.unknown-brush-level").replace("%level%", args[2])));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(ChatUtil.fixColors(config.getString("messages.unknown-player").replace("%player%", args[1])));
                return true;
            }

            player.sendMessage(ChatUtil.fixColors(config.getString("messages.brush-give-success").replace("%player%", args[1]).replace("%level%", args[2])));
            player.sendMessage(ChatUtil.fixColors(config.getString("messages.brush-give-success-to-target").replace("%level%", args[2])));

            target.getInventory().addItem(brushItem(args[2]));

        } else {
           player.sendMessage(ChatUtil.fixColors(config.getString("messages.no-arguments")));
        }


        return true;
    }
}
