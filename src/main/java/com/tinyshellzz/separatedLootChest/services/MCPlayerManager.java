package tcc.youajing.tcctools.services;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import tcc.youajing.tcctools.entity.MCPlayer;
import tcc.youajing.tcctools.utils.MyPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static tcc.youajing.tcctools.ObjectPool.mcPlayerMapper;

public class MCPlayerManager {
    private static final Map<UUID, Integer> debrisMined = new HashMap<>();
    private static final Map<UUID, Integer> fishedTimes = new HashMap<>();

    public static int getDebrisMined(UUID uuid) {
        if(debrisMined.containsKey(uuid)) {
            return debrisMined.get(uuid);
        } else {
            MCPlayer userByUuid = mcPlayerMapper.get_user_by_uuid(uuid);
            if(userByUuid == null){ return -1;}
            debrisMined.put(uuid, userByUuid.debris_mined);
            return userByUuid.debris_mined;
        }
    }

    public static int getFishedTimes(UUID uuid) {
        if(fishedTimes.containsKey(uuid)) {
            return fishedTimes.get(uuid);
        } else {
            MCPlayer userByUuid = mcPlayerMapper.get_user_by_uuid(uuid);
            if(userByUuid == null){ return -1;}
            fishedTimes.put(uuid, userByUuid.fished_times);
            return userByUuid.fished_times;
        }
    }

    public static void add_debris_mined(Player player, int debris_mined) {
        mcPlayerMapper.add_debris_mined(player, debris_mined);

        MCPlayer userByUuid = mcPlayerMapper.get_user_by_uuid(player.getUniqueId());
        debrisMined.put(player.getUniqueId(), userByUuid.debris_mined);
    }

    public static void add_fished_times(Player player, int fished_times) {
        mcPlayerMapper.add_fished_times(player, fished_times);

        MCPlayer userByUuid = mcPlayerMapper.get_user_by_uuid(player.getUniqueId());
        debrisMined.put(player.getUniqueId(), userByUuid.fished_times);
    }

    public static ArrayList<MyPair<String, Integer>> get_debris_mined_rank() {
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        ArrayList<MyPair<String, Integer>> ranks = new ArrayList<>();
        for(OfflinePlayer offlinePlayer : offlinePlayers) {
            int mined_num = MCPlayerManager.getDebrisMined(offlinePlayer.getUniqueId());
            if(mined_num == -1) continue;  // 该玩家不存在

            if(ranks.size() < 10) { // 只保留前10的数据
                boolean insert_flag = false;
                for(int i = 0; i < ranks.size(); i++) {
                    if(ranks.get(i).getValue() < mined_num) {
                        ranks.add(i, new MyPair<>(offlinePlayer.getName(), mined_num));
                        insert_flag = true;
                        break;
                    }
                }
                if(!insert_flag) {ranks.add(new MyPair<>(offlinePlayer.getName(), mined_num));}
            } else {
                for(int i = 0; i < ranks.size(); i++) {
                    if(ranks.get(i).getValue() < mined_num) {
                        ranks.add(i, new MyPair<>(offlinePlayer.getName(), mined_num));
                        break;
                    }
                }
                ranks.removeLast();
            }
        }

        return ranks;
    }

    public static ArrayList<MyPair<String, Integer>> get_fished_times_rank() {
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        ArrayList<MyPair<String, Integer>> ranks = new ArrayList<>();
        for(OfflinePlayer offlinePlayer : offlinePlayers) {
            int fished_num = MCPlayerManager.getFishedTimes(offlinePlayer.getUniqueId());
            if(fished_num == -1) continue;  // 该玩家不存在

            if(ranks.size() < 10) { // 只保留前10的数据
                boolean insert_flag = false;
                for(int i = 0; i < ranks.size(); i++) {
                    if(ranks.get(i).getValue() < fished_num) {
                        ranks.add(i, new MyPair<>(offlinePlayer.getName(), fished_num));
                        insert_flag = true;
                        break;
                    }
                }
                if(!insert_flag) {ranks.add(new MyPair<>(offlinePlayer.getName(), fished_num));}
            } else {
                for(int i = 0; i < ranks.size(); i++) {
                    if(ranks.get(i).getValue() < fished_num) {
                        ranks.add(i, new MyPair<>(offlinePlayer.getName(), fished_num));
                        break;
                    }
                }
                ranks.removeLast();
            }
        }

        return ranks;
    }
}
