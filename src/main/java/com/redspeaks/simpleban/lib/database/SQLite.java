package com.redspeaks.simpleban.lib.database;

import com.redspeaks.simpleban.lib.*;
import com.redspeaks.simpleban.lib.Time;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public class SQLite implements Database {

    private final String dbName;
    private Connection connection;
    private final JavaPlugin plugin;
    private final String table = "bans";
    public SQLite(FileConfiguration config, JavaPlugin plugin) {
        this.plugin = plugin;
        this.dbName = config.getString("sqlite.filename");
    }

    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbName+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbName+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library.");
        }
        return null;
    }

    @Override
    public void init() {
        connection = getSQLConnection();
        Scheduler.runAsync(() -> {
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                connection = getSQLConnection();
                statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + table +
                        " (uuid varchar(100), reason varchar(100), timeframe BIGINT(100), PRIMARY KEY(uuid))");
                statement.executeUpdate();
            }catch(SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (statement != null)
                        statement.close();
                    if (connection != null)
                        connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void banCheck(OfflinePlayer player, Callable<BanResult> callable) {
        Scheduler.runAsync(() -> {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet set = null;
            try {
                connection = getSQLConnection();
                statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE uuid=?");
                set = statement.executeQuery();
                final BanResult banResult = set.next() ? new BanResult(set.getString("reason"), new Time(set.getLong("timeframe")),player) : new BanResult(null, null, null);
                Scheduler.runSync(() -> callable.onFinish(banResult));
            }catch(SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(set != null)
                        set.close();
                    if (statement != null)
                        statement.close();
                    if (connection != null)
                        connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void clear(OfflinePlayer player) {
        Scheduler.runAsync(() -> {
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                connection = getSQLConnection();
                statement = connection.prepareStatement("DELETE FROM " + table + " WHERE uuid=?");
                statement.executeUpdate();
            }catch(SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (statement != null)
                        statement.close();
                    if (connection != null)
                        connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
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
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                connection = getSQLConnection();
                statement = connection.prepareStatement("INSERT INTO " + table + " VALUES(?,?,?)");
                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, reason);
                statement.setLong(3, time.getMillis());
                statement.executeUpdate();
            }catch(SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (statement != null)
                        statement.close();
                    if (connection != null)
                        connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void ban(OfflinePlayer player, String reason) {
        ban(player, reason, new Time(-1));
    }
}
