package com.tinyshellzz.separatedLootChest.listeners;


import com.tinyshellzz.separatedLootChest.entity.MyLocation;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

import java.util.HashSet;

import static com.tinyshellzz.separatedLootChest.ObjectPool.chunkScannedMapper;
import static com.tinyshellzz.separatedLootChest.ObjectPool.lootTableMapper;

public class ChunkChestScanner implements Listener {
    public static HashSet<MyLocation> chunkScanned = null;

    public ChunkChestScanner(){
        // 从数据库加载已扫描的区块
        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "加载已扫描的所有区块");
        chunkScanned = chunkScannedMapper.get_all();
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        MyLocation loc = new MyLocation();
        loc.x = chunk.getX();
        loc.z = chunk.getZ();
        loc.world = chunk.getWorld().getName();

        if(!chunkScanned.contains(loc)) {
            for (BlockState state : chunk.getTileEntities()) {
                if (state instanceof Container container) {
                    Material type = container.getType();
                    if (type == Material.CHEST || type == Material.BARREL || type == Material.TRAPPED_CHEST) {
                        if(state instanceof Lootable lootable) {
                            MyLocation state_loc = new MyLocation(state.getLocation());
                            LootTable lootTable = lootable.getLootTable();
                            // 将箱子的lootTable保存到数据库中
                            if(lootTable != null) {
                                String lootTableKey = lootTable.getKey().toString();
                                lootTableMapper.insert(state_loc, lootTableKey);
                            }
                        }
                    }
                }
            }

            chunkScanned.add(loc);
            // 将扫描过的区块，保存到数据库中
            chunkScannedMapper.insert(loc);
        }
    }
}