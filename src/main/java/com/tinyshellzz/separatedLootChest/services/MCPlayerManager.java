package com.tinyshellzz.separatedLootChest.services;

import com.tinyshellzz.separatedLootChest.entity.MCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import com.tinyshellzz.separatedLootChest.utils.MyPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.tinyshellzz.separatedLootChest.ObjectPool.mcPlayerMapper;

public class MCPlayerManager {
    private static final Map<UUID, Integer> debrisMined = new HashMap<>();
    private static final Map<UUID, Integer> fishedTimes = new HashMap<>();

    public static int getLootChestOpened(UUID uuid) {
        if(debrisMined.containsKey(uuid)) {
            return debrisMined.get(uuid);
        } else {
            MCPlayer userByUuid = mcPlayerMapper.get_user_by_uuid(uuid);
            if(userByUuid == null){ return -1;}
            debrisMined.put(uuid, userByUuid.loot_chest_opened);
            return userByUuid.loot_chest_opened;
        }
    }

    public static void addLootChestOpened(UUID playerUUID, int lootChestOpened) {
        mcPlayerMapper.add_loot_chest_opened(playerUUID, lootChestOpened);

        MCPlayer userByUuid = mcPlayerMapper.get_user_by_uuid(playerUUID);
        debrisMined.put(playerUUID, userByUuid.loot_chest_opened);
    }

    public static ArrayList<MyPair<String, Integer>> get_loot_chest_opened_rank() {
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        ArrayList<MyPair<String, Integer>> ranks = new ArrayList<>();
        for(OfflinePlayer offlinePlayer : offlinePlayers) {
            int opened_num = MCPlayerManager.getLootChestOpened(offlinePlayer.getUniqueId());
            if(opened_num == -1) continue;  // 该玩家不存在

            if(ranks.size() < 10) { // 只保留前10的数据
                boolean insert_flag = false;
                for(int i = 0; i < ranks.size(); i++) {
                    if(ranks.get(i).getValue() < opened_num) {
                        ranks.add(i, new MyPair<>(offlinePlayer.getName(), opened_num));
                        insert_flag = true;
                        break;
                    }
                }
                if(!insert_flag) {ranks.add(new MyPair<>(offlinePlayer.getName(), opened_num));}
            } else {
                for(int i = 0; i < ranks.size(); i++) {
                    if(ranks.get(i).getValue() < opened_num) {
                        ranks.add(i, new MyPair<>(offlinePlayer.getName(), opened_num));
                        break;
                    }
                }
                ranks.removeLast();
            }
        }

        return ranks;
    }
}
