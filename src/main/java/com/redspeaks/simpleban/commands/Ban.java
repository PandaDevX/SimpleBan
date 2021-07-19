package com.redspeaks.simpleban.commands;

import com.redspeaks.simpleban.SimpleBan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ban implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be a player to do that"));
            return true;
        }
        Player player = (Player)sender;
        if(args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l(!) &e/ban <player>"));
            return true;
        }
        /*
        if(player.getName().equalsIgnoreCase(args[0]) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l(!) &eYou can't ban yourself"));
            return true;
        }
         */
        OfflinePlayer target = Bukkit.getPlayer(args[0]);
        if(!target.hasPlayedBefore()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l(!) &ePlayer never played the server before!"));
            return true;
        }
        SimpleBan.getInstance().getDatabase().banCheck(target, (result) -> {
            if(result.isBanned()) {
                player.sendMessage(ChatColor.RED + "That player is already banned!");
            } else {
                SimpleBan.getInstance().getMainMenu().open(player, target);
            }
        });

        return false;
    }
}
