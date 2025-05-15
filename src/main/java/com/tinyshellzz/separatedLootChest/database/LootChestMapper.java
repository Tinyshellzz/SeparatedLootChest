package com.tinyshellzz.separatedLootChest.database;

import com.tinyshellzz.separatedLootChest.entity.LootChest;
import com.tinyshellzz.separatedLootChest.entity.MyLocation;
import com.tinyshellzz.separatedLootChest.services.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static com.tinyshellzz.separatedLootChest.ObjectPool.gson;

public class LootChestMapper {
    public LootChestMapper() {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS loot_chests (" +
                    "location Varchar(128)," +
                    "player_uuid Char(36)," +
                    "contents Varchar(20000)," +
                    "world TinyInt," +
                    "UNIQUE KEY (location, player_uuid)" +
                    ") ENGINE=InnoDB CHARACTER SET=utf8;");
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LootChestMapper.LootChestMapper:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * 插入前，要确保新主键不在表中
     * @param loot_chest
     */
    public void insert(LootChest loot_chest){
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("INSERT INTO loot_chests VALUES (?, ?, ?, ?)");
            stmt.setString(1, gson.toJson(loot_chest.location));
            stmt.setString(2, loot_chest.player_uuid.toString());
            stmt.setString(3, ItemStackManager.ItemStackArrayToBase64(loot_chest.contents));
            Bukkit.getConsoleSender().sendMessage(loot_chest.location.world.toString());
            if(loot_chest.location.world.equals("world")) {
                stmt.setInt(4, 0);
            } else if(loot_chest.location.world.equals("world_nether")) {
                stmt.setInt(4, 1);
            } else if (loot_chest.location.world.equals("world_the_end")) {
                stmt.setInt(4, 2);
            }
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LootChestMapper.insert:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * 更新箱子的内容
     * @param loot_chest
     */
    public void update(LootChest loot_chest){
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE loot_chests SET contents=? WHERE location=? AND player_uuid=?");
            stmt.setString(1, ItemStackManager.ItemStackArrayToBase64(loot_chest.contents));
            stmt.setString(2, gson.toJson(loot_chest.location));
            stmt.setString(3, loot_chest.player_uuid.toString());
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LootChestMapper.update:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     *
     * @param location
     * @param player_uuid
     * @return 如果该箱子不存在，即该玩家第一次打开箱子，返回null
     */
    public LootChest get(MyLocation location, UUID player_uuid){
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        LootChest ret = null;

        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT * FROM loot_chests where location = ? and player_uuid = ?");
            stmt.setString(1, gson.toJson(location));
            stmt.setString(2, player_uuid.toString());
            rs = stmt.executeQuery();

            if(rs.next()) {
                ret = new LootChest(rs.getString(1), rs.getString(2), rs.getString(3));
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LootChestMapper.get:" + e.getMessage());
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

    public LootChest get(Location location, UUID player_uuid){
        return get(new MyLocation(location), player_uuid);
    }

    public boolean exists(MyLocation location) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        boolean ret = false;

        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM loot_chests where location = ?");
            stmt.setString(1, gson.toJson(location));
            rs = stmt.executeQuery();

            if(rs.next() && rs.getInt(1) > 0) {
                ret = true;
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LootChestMapper.get:" + e.getMessage());
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

    public boolean exists(Location location){
        return exists(new MyLocation(location));
    }

    /**
     * 如果箱子被破坏，则调用该方法来删除数据库中的箱子
     * @param location
     * @return
     */
    public void delete(MyLocation location){
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        LootChest ret = null;

        try {
            conn = MysqlConfig.connect();

            stmt = conn.prepareStatement("DELETE FROM loot_chests where location = ?");
            stmt.setString(1, gson.toJson(location));
            stmt.executeQuery();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LootChestMapper.delete:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public void delete(Location location){
        delete(new MyLocation(location));
    }
}
