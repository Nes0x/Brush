package me.nesox.brush.commands;

import me.nesox.brush.Brush;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrushesTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("brush.give")) return Collections.EMPTY_LIST;
        final List<String> give = new ArrayList<>();
        final List<String> levels = new ArrayList<>();
        final List<String> players = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        give.add("give");

        for (int i = 1; i <= Brush.getInstance().getConfig().getInt("brush.max-level"); i++) {
            levels.add(String.valueOf(i));
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(player.getName());
        }

        if (args.length == 1){
            StringUtil.copyPartialMatches(args[0], give, completions);
        }

        if (args.length == 2){
            StringUtil.copyPartialMatches(args[1], players, completions);
        }

        if (args.length == 3){
            StringUtil.copyPartialMatches(args[2], levels, completions);
        }

        if (args.length >= 4) {
            return Collections.EMPTY_LIST;
        }
        Collections.sort(completions);
        return completions;
    }

}

