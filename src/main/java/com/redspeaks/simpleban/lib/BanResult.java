package com.redspeaks.simpleban.lib;

import org.bukkit.OfflinePlayer;

public class BanResult {

    private final String reason;
    private final Time timeframe;
    private final boolean noResult;
    private final OfflinePlayer offender;
    public BanResult(String reason, Time timeframe, OfflinePlayer offender) {
        this.reason = reason;
        this.timeframe = timeframe;
        this.offender = offender;
        this.noResult = !offender.hasPlayedBefore();
    }

    public OfflinePlayer getOffender() {
        return offender;
    }

    public boolean isBanned() {
        if(noResult) {
            return false;
        }
        return System.currentTimeMillis() <= timeframe.getMillis();
    }

    public String getReason() {
        return reason;
    }

    public Time timeframe() {
        return timeframe;
    }
}
