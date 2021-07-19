package com.redspeaks.simpleban.commands;

import com.redspeaks.simpleban.SimpleBan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CheckBan implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l(!) &e/checkban <player>"));
            return true;
        }
        OfflinePlayer target = Bukkit.getPlayer(args[0]);
        if(!target.hasPlayedBefore()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l(!) &ePlayer never played the server before!"));
            return true;
        }
        SimpleBan.getInstance().getDatabase().banCheck(target, (result) -> {
            if(result.isBanned()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Player &a" + result.getOffender().getName() + " &7is banned for &c" + result.getReason() + " &7for &c" + result.timeframe().parseTime()));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Player &a" + result.getOffender().getName() + " &7is not &abanned."));
                SimpleBan.getInstance().getDatabase().unBan(target);
            }

        });
        return false;
    }
}
