package com.tinyshellzz.separatedLootChest.listeners;

import com.tinyshellzz.separatedLootChest.entity.MyLocation;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

import static com.tinyshellzz.separatedLootChest.ObjectPool.lootTableMapper;

public class LootChestScanner implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event){
        Player player = event.getPlayer();

        Block targetBlock = player.getTargetBlockExact(10); // 10 blocks max range
        if(targetBlock == null){ return; }
        BlockState state = targetBlock.getState();

        scan(state);
    }

    public static void scan(BlockState state){
        if (state == null) return;
        if(state instanceof Lootable lootable){
            Material type = state.getType();
            if (type == Material.CHEST || type == Material.BARREL || type == Material.TRAPPED_CHEST || type == Material.SHULKER_BOX) {
                LootTable lootTable = lootable.getLootTable();

//                Bukkit.getConsoleSender().sendMessage("扫描该箱子");
                // 将箱子的lootTable保存到数据库中
                if (lootTable != null) {
                    MyLocation state_loc = new MyLocation(state.getLocation());
                    String lootTableKey = lootTable.getKey().toString();
//                    Bukkit.getConsoleSender().sendMessage("获取lootTableKey" + lootTableKey);

                    // 将箱子的lootTable保存到数据库中
                    Thread th = new Thread(() -> {
                        if (lootTableMapper.exists(state_loc)) {
                            lootTableMapper.update(state_loc, lootTableKey, 0);
                        } else {
                            lootTableMapper.insert(state_loc, lootTableKey);
                        }
                    });

                    th.start();
                }
            }
        }
    }
}
