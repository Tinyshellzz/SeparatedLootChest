package tcc.youajing.tcctools.database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tcc.youajing.tcctools.entity.MCPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static tcc.youajing.tcctools.ObjectPool.gson;

public class MCPlayerMapper {
    public MCPlayerMapper() {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS mc_players (" +
                    "name Varchar(48)," +
                    "uuid Char(36)," +
                    "fished_times Int," +
                    "debris_mined Int," +
                    "KEY (name)," +
                    "UNIQUE KEY (uuid)," +
                    "KEY (fished_times)," +
                    "KEY (debris_mined)" +
                    ") ENGINE=InnoDB CHARACTER SET=utf8;");
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MCPlayerMapper.MCPlayerMapper:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public void insert_player(MCPlayer player){
        // uuid已存在
        if (exists_uuid(player.uuid)) {
            // update
            return;
        }

        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("INSERT INTO mc_players VALUES (?, ?, ?, ?)");
            stmt.setString(1, player.name);
            stmt.setString(2, player.uuid.toString());
            stmt.setInt(3, player.fished_times);
            stmt.setInt(4, player.debris_mined);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MCPlayerMapper.insert:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public MCPlayer get_user_by_uuid(String mc_uuid) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        MCPlayer player = null;
        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT * FROM mc_players WHERE uuid=?");
            stmt.setString(1, mc_uuid);
            rs = stmt.executeQuery();
            if(rs.next()) {
                player =  new MCPlayer(rs.getString(1), UUID.fromString(rs.getString(2)), rs.getInt(3), rs.getInt(4));
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MCPlayerMapper.get_user_by_uuid:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }

        return player;
    }

    public MCPlayer get_user_by_name(String name) {
        name = name.toLowerCase();

        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        MCPlayer player = null;
        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT * FROM mc_players WHERE name=?");
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if(rs.next()) {
                player =  new MCPlayer(rs.getString(1), UUID.fromString(rs.getString(2)), rs.getInt(3), rs.getInt(4));
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MCPlayerMapper.get_user_by_name:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }

        return player;
    }


    public boolean exists_uuid(UUID uuid) {
        return get_user_by_uuid(uuid) != null;
    }

    public MCPlayer get_user_by_uuid(UUID uuid) {
        return get_user_by_uuid(uuid.toString());
    }

    public void update_player_name(Player player){
        MCPlayer mcPlayer = null;
        if(!exists_uuid(player.getUniqueId())){
            mcPlayer = new MCPlayer(player.getName().toLowerCase(), player.getUniqueId(),0, 0);
            insert_player(mcPlayer);
        } else {
            mcPlayer = get_user_by_uuid(player.getUniqueId());

            PreparedStatement stmt = null;
            Connection conn = null;
            ResultSet rs = null;
            try {
                conn = MysqlConfig.connect();
                stmt = conn.prepareStatement("UPDATE mc_players SET name = ? WHERE uuid=?");
                stmt.setString(1, player.getName().toLowerCase());
                stmt.setString(2, player.getUniqueId().toString());
                stmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "TeamMapper.update_player_name:" + e.getMessage());
            } finally {
                try {
                    if(stmt != null) stmt.close();
                    if(rs != null) rs.close();
                    if(conn != null) conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void add_fished_times(Player player, int fished_times) {
        MCPlayer mcPlayer = get_user_by_uuid(player.getUniqueId());

        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE mc_players SET fished_times = ? WHERE uuid=?");
            stmt.setInt(1, mcPlayer.fished_times + fished_times);
            stmt.setString(2, player.getUniqueId().toString());
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "TeamMapper.add_fished_times:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public void add_debris_mined(Player player, int debris_mined) {
        MCPlayer mcPlayer = get_user_by_uuid(player.getUniqueId());

        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE mc_players SET debris_mined = ? WHERE uuid=?");
            stmt.setInt(1, mcPlayer.debris_mined + debris_mined);
            stmt.setString(2, player.getUniqueId().toString());
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "TeamMapper.add_debris_mined:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public boolean exists_name(String name) {
        return get_user_by_name(name) != null;
    }
}
