package com.tinyshellzz.separatedLootChest.listeners;

import com.tinyshellzz.separatedLootChest.entity.LootChest;
import com.tinyshellzz.separatedLootChest.entity.MyLocation;
import org.bukkit.Bukkit;
import org.bukkit.block.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

import java.util.HashMap;
import java.util.UUID;

import static com.tinyshellzz.separatedLootChest.ObjectPool.lootChestMapper;
import static com.tinyshellzz.separatedLootChest.ObjectPool.lootTableMapper;

public class ContainerInteractListener implements Listener {
    HashMap<MyLocation, UUID> opened_chests = new HashMap<>();      // 存储所有打开的箱子

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        UUID playerUUID = event.getPlayer().getUniqueId();

        if (holder instanceof DoubleChest doubleChest) {
            Chest left = (Chest) doubleChest.getLeftSide();
            Chest right = (Chest) doubleChest.getRightSide();
            if(opened_chests.containsKey(new MyLocation(left.getLocation()))) {     // 箱子已被玩家打开，则不进行任何操作
                return;
            } else {
                opened_chests.put(new MyLocation(left.getLocation()), playerUUID);
                opened_chests.put(new MyLocation(right.getLocation()), playerUUID);
            }

            // 更新left箱子中的战利品
            updateChestLoot(left, playerUUID);

            // 更新right箱子中的战利品
            updateChestLoot(right, playerUUID);

        } else if (holder instanceof Chest chest) {
            if(opened_chests.containsKey(new MyLocation(chest.getLocation()))) {     // 箱子已被玩家打开，则不进行任何操作
                return;
            } else {
                opened_chests.put(new MyLocation(chest.getLocation()), playerUUID);
            }

            // 第一次打开，则生成战利品。否则就加载以前箱子里的东西
            updateChestLoot(chest, playerUUID);
        } else if (holder instanceof Barrel barrel) {
            if(opened_chests.containsKey(new MyLocation(barrel.getLocation()))) {     // 箱子已被玩家打开，则不进行任何操作
                return;
            } else {
                opened_chests.put(new MyLocation(barrel.getLocation()), playerUUID);
            }

            // 第一次打开，则生成战利品。否则就加载以前箱子里的东西
            updateChestLoot(barrel, playerUUID);
        }
    }

    /**
     * 关闭时，要保存箱子中的内容。并且从opened_chests列表中移除该位置
     * 问题: 是否每次容器操作都保存一次内容(服务器重启，没有关闭箱子事件是可能出问题)
     * @param event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        UUID playerUUID = event.getPlayer().getUniqueId();

        // Only act on container inventories (like chests, barrels, etc.)
        if (holder instanceof DoubleChest doubleChest) {
            Chest left = (Chest) doubleChest.getLeftSide();
            Chest right = (Chest) doubleChest.getRightSide();
            if(opened_chests.containsKey(new MyLocation(left.getLocation()))) {
                playerUUID = opened_chests.get(new MyLocation(left.getLocation()));
            }

            opened_chests.remove(new MyLocation(left.getLocation()));
            opened_chests.remove(new MyLocation(right.getLocation()));


            lootChestMapper.update(new LootChest(left.getLocation(), playerUUID, left.getInventory().getContents()));
            lootChestMapper.update(new LootChest(right.getLocation(), playerUUID, right.getInventory().getContents()));

            left.getInventory().clear();
            right.getInventory().clear();
        } else if(holder instanceof Chest chest) {
            if(opened_chests.containsKey(new MyLocation(chest.getLocation()))) {
                playerUUID = opened_chests.get(new MyLocation(chest.getLocation()));
            }
            opened_chests.remove(new MyLocation(chest.getLocation()));


            Bukkit.getConsoleSender().sendMessage("保存箱子内容");
            lootChestMapper.update(new LootChest(chest.getLocation(), playerUUID, chest.getInventory().getContents()));

            chest.getInventory().clear();
        } else if(holder instanceof Barrel barrel) {
            if(opened_chests.containsKey(new MyLocation(barrel.getLocation()))) {
                playerUUID = opened_chests.get(new MyLocation(barrel.getLocation()));
            }
            opened_chests.remove(new MyLocation(barrel.getLocation()));

            lootChestMapper.update(new LootChest(barrel.getLocation(), playerUUID, barrel.getInventory().getContents()));
            barrel.getInventory().clear();
        }
    }

    /**
     * 玩家第一次破坏箱子，要依据LootTable生成战利品
     * 如果箱子正在被打开，就不进行仍和操作。仅从opened_chests列表中移除该位置
     * @param event
     */
    @EventHandler
    public void onChestBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        UUID playerUUID = event.getPlayer().getUniqueId();      // 该事件只能由玩家触发

        if (block.getState() instanceof Chest chest) {
            // 如果是第一次破坏，则生成战利品; 否则就调用以前的战利品
            LootChest loot_chest = lootChestMapper.get(chest.getLocation(), playerUUID);
            if (loot_chest != null) {
                chest.getInventory().setContents(loot_chest.contents);
            } else {
                updateChestLoot(chest, playerUUID);
            }
            // 注: 箱子中的数据，不应该删除
        } else if (block.getState() instanceof Barrel barrel) {
            // 如果是第一次破坏，则生成战利品; 否则就调用以前的战利品
            LootChest loot_chest = lootChestMapper.get(barrel.getLocation(), playerUUID);
            if (loot_chest != null) {
                barrel.getInventory().setContents(loot_chest.contents);
            } else {
                updateChestLoot(barrel, playerUUID);
            }
        }

    }


    private static void updateChestLoot(Container container, UUID playerUUID) {
        if(container instanceof Lootable lootable) {
            LootTable lootTable = lootTableMapper.get_loot_table(container.getLocation());
            if (lootTable != null) {   // 判断是否是战利品箱子
                LootChest left_loot_chest = lootChestMapper.get(container.getLocation(), playerUUID);
                if (left_loot_chest != null) {
                    container.getInventory().setContents(left_loot_chest.contents);
                } else {
                    if(lootChestMapper.exists(container.getLocation())) {
                        lootable.setLootTable(lootTable);
                        lootable.setSeed(System.currentTimeMillis()); // optional: ensures variety
                        container.update(true); // apply changes
                    }

                    lootChestMapper.insert(new LootChest(container.getLocation(), playerUUID, container.getInventory().getContents()));
                }
            }
        }
    }
}
