package com.redspeaks.simpleban.lib;

import org.bukkit.OfflinePlayer;

public interface Database {

    void init();
    void banCheck(OfflinePlayer player, Callable<BanResult> callable);
    void clear(OfflinePlayer player);
    void unBan(OfflinePlayer player);
    void ban(OfflinePlayer player, String reason, Time time);
    void ban(OfflinePlayer player, String reason);

}
