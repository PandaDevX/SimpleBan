package com.redspeaks.simpleban;

import com.redspeaks.simpleban.commands.Ban;
import com.redspeaks.simpleban.commands.CheckBan;
import com.redspeaks.simpleban.lib.Database;
import com.redspeaks.simpleban.lib.database.MySQL;
import com.redspeaks.simpleban.lib.database.SQLite;
import com.redspeaks.simpleban.lib.menu.MainMenu;
import com.redspeaks.simpleban.listener.Join;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleBan extends JavaPlugin {

    private static SimpleBan instance;
    private MainMenu mainMenu = null;
    private Database database = null;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        mainMenu = new MainMenu();
        mainMenu.init();


        getCommand("ban").setExecutor(new Ban());
        getCommand("checkban").setExecutor(new CheckBan());

        if(getConfig().getString("database").equals("mysql")) {
            database = new MySQL(getConfig());
        } else {
            database = new SQLite(getConfig(), this);
        }

        database.init();

        getServer().getPluginManager().registerEvents(new Join(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public static SimpleBan getInstance() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }
}
