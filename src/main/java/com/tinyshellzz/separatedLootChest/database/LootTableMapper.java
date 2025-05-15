package com.tinyshellzz.separatedLootChest.database;

import com.tinyshellzz.separatedLootChest.entity.LootChest;
import com.tinyshellzz.separatedLootChest.entity.MyLocation;
import com.tinyshellzz.separatedLootChest.services.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.loot.LootTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.tinyshellzz.separatedLootChest.ObjectPool.gson;

public class LootTableMapper {
    public LootTableMapper() {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS loot_tables (" +
                    "location Varchar(128)," +
                    "table_key Varchar(256)," +
                    "broken TINYINT," +     // 0代表完好无损，-1代表已经生成过一次战利品，1代表已经被破坏
                    "UNIQUE KEY (location)" +
                    ") ENGINE=InnoDB CHARACTER SET=utf8;");
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LootTableMapper.LootTableMapper:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public void insert(MyLocation location, String tableKey) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("INSERT INTO loot_tables VALUES (?, ?, ?)");
            stmt.setString(1, gson.toJson(location));
            stmt.setString(2, tableKey);
            stmt.setInt(3, 0);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LootTableMapper.insert:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public LootTable get_loot_table(MyLocation location) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        LootTable ret = null;

        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT * FROM loot_tables where location = ?");
            stmt.setString(1, gson.toJson(location));
            rs = stmt.executeQuery();

            if(rs.next()) {
                NamespacedKey key = NamespacedKey.fromString(rs.getString("table_key"));
                if(key != null && rs.getInt("broken") != 1) {
                    ret = Bukkit.getLootTable(key);
                }
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LootTableMapper.get:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }

        return ret;
    }

    public void update_broken(MyLocation location, int broken) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE loot_tables SET broken=? WHERE location=?");
            stmt.setInt(1, broken);
            stmt.setString(2, gson.toJson(location));
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LootTableMapper.update_broken:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public int get_broken(MyLocation location) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        int ret = 1;

        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT * FROM loot_tables where location = ?");
            stmt.setString(1, gson.toJson(location));
            rs = stmt.executeQuery();
            if(rs.next()) {
                ret = rs.getInt("broken");
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LootTableMapper.get:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }

        return ret;
    }

    public void update_broken(Location location, boolean broken) {
        update_broken(new MyLocation(location), broken ? 1 : 0);
    }

    public LootTable get_loot_table(Location location) {
        return get_loot_table(new MyLocation(location));
    }
}
