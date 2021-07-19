package com.redspeaks.simpleban.listener;

import com.redspeaks.simpleban.SimpleBan;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class Join implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        SimpleBan.getInstance().getDatabase().banCheck(e.getPlayer(), (result) -> {
            if(result.isBanned()) {
                e.getPlayer().kickPlayer("You are banned from the server!\n" + "Reason: " + result.getReason() + "\nRemaining Time:" + result.timeframe().parseTime());
            } else {
                SimpleBan.getInstance().getDatabase().unBan(e.getPlayer());
            }
        });
    }
}
