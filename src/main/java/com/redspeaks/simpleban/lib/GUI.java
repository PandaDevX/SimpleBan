package com.redspeaks.simpleban.lib;

import com.redspeaks.simpleban.SimpleBan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public abstract class GUI implements InventoryHolder, Listener {

    private final Inventory inventory;
    private final String title;
    public GUI(String name, int rows) {
        this.inventory = Bukkit.createInventory(this, 9 * rows, ChatColor.translateAlternateColorCodes('&', name));
        this.title = ChatColor.translateAlternateColorCodes('&', name);
        SimpleBan.getInstance().getServer().getPluginManager().registerEvents(this, SimpleBan.getInstance());
    }

    public abstract void init();
    public abstract void open(Player player);
    public abstract void onClick(InventoryClickEvent e, Player player);

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getInventory().getHolder() instanceof GUI) {
            if(e.getView().getTitle().equals(title)) {
                onClick(e, (Player)e.getWhoClicked());
            }
        }
    }

    public void setItem(int slot, ItemStack stack) {
        getInventory().setItem(slot, stack);
    }

    public ItemStack build(String name, Material type) {
        ItemStack itemStack = new ItemStack(type);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack head(OfflinePlayer owner) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + owner.getName()));
        meta.setOwningPlayer(owner);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
