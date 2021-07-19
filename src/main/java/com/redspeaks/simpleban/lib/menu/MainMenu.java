package com.redspeaks.simpleban.lib.menu;

import com.redspeaks.simpleban.SimpleBan;
import com.redspeaks.simpleban.lib.GUI;
import com.redspeaks.simpleban.lib.Time;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends GUI {

    public MainMenu() {
        super("Ban Menu", 6);
    }

    @Override
    public void init() {
        setItem(4, new ItemStack(Material.PLAYER_HEAD));
        List<String> keys = new ArrayList<>(SimpleBan.getInstance().getConfig().getConfigurationSection("ban-reasons").getKeys(false));
        for(int i = 0; i < keys.size(); i++) {
            String timeframe = SimpleBan.getInstance().getConfig().getString("ban-reasons." + keys.get(i) + ".timeframe");
            setItem(i + 9, build("&6" + keys.get(i) + " - &c" + (timeframe.equals("-1") ? "Permanent" : timeframe), Material.BOOK));
        }
    }

    @Override
    public void open(Player player) {
        player.openInventory(getInventory());
    }

    public void open(Player player, OfflinePlayer target) {
        setItem(4, head(target));
        open(player);
    }

    @Override
    public void onClick(InventoryClickEvent e, Player player) {
        if(e.getCurrentItem() == null) {
            return;
        }
        if(e.getCurrentItem().getType() == Material.BOOK && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
            String reason = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().split(" - ")[0]);
            String timeframe = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().split(" - ")[1]);
            e.setCancelled(true);
            player.closeInventory();
            String playerName = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName());
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Player &a" + playerName + " &7has been banned for &c" + reason + " &7in &c" + timeframe + " &7by &a" + player.getDisplayName())));
            Time time = new Time(timeframe);
            if(time.getMillis() == -1) {
                SimpleBan.getInstance().getDatabase().ban(player, reason);
            } else {
                SimpleBan.getInstance().getDatabase().ban(player, reason, time);
            }
        }
    }
}
