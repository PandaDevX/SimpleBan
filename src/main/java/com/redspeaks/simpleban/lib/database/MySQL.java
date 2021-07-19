package com.redspeaks.simpleban.lib.database;

import com.redspeaks.simpleban.lib.*;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL implements Database {

    private final FileConfiguration config;
    private final HikariDataSource hikari;
    public MySQL(FileConfiguration config) {
        this.config = config;
        this.hikari = new HikariDataSource();
    }

    @Override
    public void init() {
        hikari.setJdbcUrl("jdbc:mysql://" + config.getString("mysql.host") + ":" + config.getString("mysql.port") + "/" + config.getString("mysql.database"));
        hikari.setUsername(config.getString("mysql.user"));
        hikari.setPassword(config.getString("mysql.pass"));

        Scheduler.runAsync(() -> {
            try(Connection connection = hikari.getConnection(); PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS bans" +
                    " (uuid varchar(100), reason varchar(100), timeframe BIGINT(100), PRIMARY KEY(uuid))")) {
                statement.executeUpdate();
            }catch(SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void banCheck(OfflinePlayer player, Callable<BanResult> callable) {
        Scheduler.runAsync(() -> {
            try(Connection con = hikari.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM bans WHERE uuid=?")) {
                statement.setString(1, player.getUniqueId().toString());
                try(ResultSet rs = statement.executeQuery()) {
                    final BanResult banResult = rs.next() ? new BanResult(rs.getString("reason"), new Time(rs.getLong("timeframe")),player) : new BanResult(null, null, null);
                    Scheduler.runSync(() -> callable.onFinish(banResult));
                }
            }catch(SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void clear(OfflinePlayer player) {
        Scheduler.runAsync(() -> {
            try(Connection con = hikari.getConnection(); PreparedStatement statement = con.prepareStatement("DELETE FROM bans WHERE uuid=?")) {
                statement.setString(1, player.getUniqueId().toString());
                statement.executeUpdate();
            }catch(SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void unBan(OfflinePlayer player) {
        clear(player);
    }

    @Override
    public void ban(OfflinePlayer player, String reason, Time time) {
        Scheduler.runAsync(() -> {
            try(Connection con = hikari.getConnection(); PreparedStatement statement = con.prepareStatement("INSERT INTO bans VALUES(?,?,?)")) {
                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, reason);
                statement.setLong(3, time.getMillis());
                statement.executeUpdate();

            }catch(SQLException e) {
                e.printStackTrace();
            }
            if(player.isOnline()) {
                Scheduler.runSync(() -> player.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', "You are banned from the server!\n" + "Reason: " + reason + "\nRemaining Time:" + time.parseTime() + "")));
            }
        });
    }

    @Override
    public void ban(OfflinePlayer player, String reason) {
        ban(player, reason, new Time(-1));
    }
}
