package com.tinyshellzz.separatedLootChest.listeners;

import com.tinyshellzz.separatedLootChest.config.PluginConfig;
import com.tinyshellzz.separatedLootChest.entity.LootChest;
import com.tinyshellzz.separatedLootChest.entity.MyLocation;
import com.tinyshellzz.separatedLootChest.services.MCPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.tinyshellzz.separatedLootChest.ObjectPool.*;

public class ContainerInteractListener implements Listener {
    public static HashMap<String, InventoryHolder> opened_chests = new HashMap<>();
    public static HashMap<String, Integer> opened_counts = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryOpen(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if(!(block.getState() instanceof Container container)) return;


        Inventory inv = container.getInventory();
        InventoryHolder holder = inv.getHolder();
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        Inventory customChest = null;
        if (holder instanceof DoubleChest doubleChest) {
            // 要先验证是否是战利品箱子, 然后从数据库取数据
            // 双层箱子要按照两个箱子来存储
            Chest leftSide = (Chest) doubleChest.getLeftSide();
            Chest rightSide = (Chest) doubleChest.getRightSide();
            LootTable lootTable_left = scan(leftSide);
            LootTable lootTable_right = scan(rightSide);
            if(lootTable_left == null) lootTable_left = lootTableMapper.get_loot_table(leftSide.getLocation());
            if(lootTable_right == null) lootTable_right = lootTableMapper.get_loot_table(rightSide.getLocation());
            if(PluginConfig.debug || lootTable_left != null || lootTable_right != null) {
                updateChestLoot(leftSide, lootTable_left, playerUUID);
                updateChestLoot(rightSide, lootTable_right, playerUUID);

                String title = generateChestTitle();
                customChest = Bukkit.createInventory(null, inv.getSize(), title);
                for (int i = 0; i < inv.getSize(); i++) {
                    customChest.setItem(i, inv.getItem(i));
                }
                inv.clear();
                player.openInventory(customChest);
                opened_chests.put(title, holder);
                if (opened_counts.containsKey(title)) {
                    opened_counts.put(title, opened_counts.get(title) + 1);
                } else {
                    opened_counts.put(title, 1);
                }
                leftSide.open();
                rightSide.open();
                event.setCancelled(true);
            }
        } else if (holder instanceof Chest chest) {
            LootTable lootTable = scan(chest);
            if(lootTable == null) lootTable = lootTableMapper.get_loot_table(chest.getLocation());
            if(lootTable != null) {
                // 第一次打开，则生成战利品。否则就加载以前箱子里的东西
                updateChestLoot(chest, lootTable, playerUUID);

                String title = generateChestTitle();
                customChest = Bukkit.createInventory(null, inv.getSize(), title);
                for (int i = 0; i < inv.getSize(); i++) {
                    customChest.setItem(i, inv.getItem(i));
                }
                inv.clear();
                player.openInventory(customChest);
                opened_chests.put(title, holder);
                if (opened_counts.containsKey(title)) {
                    opened_counts.put(title, opened_counts.get(title) + 1);
                } else {
                    opened_counts.put(title, 1);
                }

                chest.open();
                event.setCancelled(true);
            }
        } else if (holder instanceof Barrel barrel) {
            LootTable lootTable = scan(barrel);
            if(lootTable == null) lootTable = lootTableMapper.get_loot_table(barrel.getLocation());
            if(lootTable != null) {
                // 第一次打开，则生成战利品。否则就加载以前箱子里的东西
                updateChestLoot(barrel, lootTable, playerUUID);

                String title = generateChestTitle();
                customChest = Bukkit.createInventory(null, inv.getSize(), title);
                for (int i = 0; i < inv.getSize(); i++) {
                    customChest.setItem(i, inv.getItem(i));
                }
                inv.clear();
                player.openInventory(customChest);
                opened_chests.put(title, holder);
                if (opened_counts.containsKey(title)) {
                    opened_counts.put(title, opened_counts.get(title) + 1);
                } else {
                    opened_counts.put(title, 1);
                }
                barrel.open();
                event.setCancelled(true);
            }
        } else if (holder instanceof ShulkerBox shulkerBox) {
            LootTable lootTable = scan(shulkerBox);
            if(lootTable == null) lootTable = lootTableMapper.get_loot_table(shulkerBox.getLocation());
            if(lootTable != null) {
                // 第一次打开，则生成战利品。否则就加载以前箱子里的东西
                updateChestLoot(shulkerBox, lootTable, playerUUID);

                String title = generateChestTitle();
                customChest = Bukkit.createInventory(null, inv.getSize(), title);
                for (int i = 0; i < inv.getSize(); i++) {
                    customChest.setItem(i, inv.getItem(i));
                }
                inv.clear();
                player.openInventory(customChest);
                opened_chests.put(title, holder);
                if (opened_counts.containsKey(title)) {
                    opened_counts.put(title, opened_counts.get(title) + 1);
                } else {
                    opened_counts.put(title, 1);
                }
                shulkerBox.open();
                event.setCancelled(true);
            }
        }
    }

    public static String generateChestTitle() {
        String res = UUID.randomUUID().toString().substring(0, 8);
        while(opened_chests.containsKey(res)) res = UUID.randomUUID().toString().substring(0, 8);
        return res;
    }

    public static LootTable scan(BlockState state){
        if (state == null) return null;
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
                    new Thread(() -> {
                        if (lootTableMapper.exists(state_loc)) {
                            lootTableMapper.update(state_loc, lootTableKey, 0);
                        } else {
                            lootTableMapper.insert(state_loc, lootTableKey);
                        }
                    }).start();


                    return lootTable;
                }
            }
        }

        return null;
    }

    /**
     * 关闭时，要保存箱子中的内容。并且从opened_chests列表中移除该位置
     * 问题: 是否每次容器操作都保存一次内容(服务器重启，没有关闭箱子事件是可能出问题)
     * @param event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (PluginConfig.debug) Bukkit.getConsoleSender().sendMessage("容器关闭事件");

        // 保存容器的内容
        String title = event.getView().getTitle();
        if (!opened_chests.containsKey(title)) return;
        Inventory inv = event.getInventory();
        InventoryHolder holder_of_title = opened_chests.get(title);
        UUID playerUUID = event.getPlayer().getUniqueId();

        if (holder_of_title instanceof DoubleChest doubleChest) {
            Chest left = (Chest) doubleChest.getLeftSide();
            Chest right = (Chest) doubleChest.getRightSide();
            ItemStack leftContents[] = new ItemStack[27];
            ItemStack rightContents[] = new ItemStack[27];

            for (int i = 0; i < 27; i++) {
                leftContents[i] = inv.getItem(i);
            }

            for (int i = 27; i < 54; i++) {
                rightContents[i-27] = inv.getItem(i);
            }

            new Thread(() -> {
                lootChestMapper.update(new LootChest(left.getLocation(), playerUUID, leftContents));
                lootChestMapper.update(new LootChest(right.getLocation(), playerUUID, rightContents));
            }).start();
        } else if (holder_of_title instanceof Chest chest) {
            new Thread(() -> {
                lootChestMapper.update(new LootChest(chest.getLocation(), playerUUID, inv.getContents()));
            }).start();
        } else if (holder_of_title instanceof Barrel barrel) {
            new Thread(() -> {
                lootChestMapper.update(new LootChest(barrel.getLocation(), playerUUID, inv.getContents()));
            }).start();

        } else if (holder_of_title instanceof ShulkerBox shulkerBox) {
            new Thread(() -> {
                lootChestMapper.update(new LootChest(shulkerBox.getLocation(), playerUUID, inv.getContents()));
            }).start();

        }

        int count = opened_counts.get(title) - 1;
        if (count == 0) {
            if (holder_of_title instanceof DoubleChest doubleChest) {
                Chest left = (Chest)(doubleChest.getLeftSide());
                Chest right = (Chest)(doubleChest.getRightSide());
                if(left != null) left.close();
                if(right != null) right.close();
            } else if (holder_of_title instanceof Chest chest) chest.close();
            else if (holder_of_title instanceof Barrel barrel) barrel.close();
            else if (holder_of_title instanceof ShulkerBox shulkerBox) shulkerBox.close();

            opened_counts.remove(title);
            opened_chests.remove(title);
        } else {
            opened_counts.put(title, count);
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
        BlockState state = block.getState();
//        LootChestScanner.scan(state);      // 扫描该箱子, PlayerInteractEvent包括BlockBreak的情况
        UUID playerUUID = event.getPlayer().getUniqueId();

        if (state instanceof Chest chest) {
//            // 如果是第一次破坏，则生成战利品; 否则就调用以前的战利品
//            LootChest loot_chest = lootChestMapper.get(chest.getLocation(), playerUUID);
//            if (loot_chest != null) {
//                chest.getInventory().setContents(loot_chest.contents);
//            } else {
//                LootTable lootTable = lootTableMapper.get_loot_table(chest.getLocation());
//                updateChestLoot(chest, lootTable, playerUUID);
//            }
            // 将箱子标记为被破坏
            new Thread(() -> {
                lootTableMapper.update_broken(chest.getLocation(), true);
                lootChestMapper.delete(chest.getLocation());
            }).start();
        } else if (state instanceof Barrel barrel) {
            // 如果是第一次破坏，则生成战利品; 否则就调用以前的战利品
//            LootChest loot_chest = lootChestMapper.get(barrel.getLocation(), playerUUID);
//            if (loot_chest != null) {
//                barrel.getInventory().setContents(loot_chest.contents);
//            } else {
//                LootTable lootTable = lootTableMapper.get_loot_table(barrel.getLocation());
//                updateChestLoot(barrel, lootTable, playerUUID);
//            }
            // 将箱子标记为被破坏
            new Thread(()->{
                lootTableMapper.update_broken(barrel.getLocation(), true);
                lootChestMapper.delete(barrel.getLocation());
            }).start();
        } else if (state instanceof ShulkerBox shulkerBox) {
            // 如果是第一次破坏，则生成战利品; 否则就调用以前的战利品
//            LootChest loot_chest = lootChestMapper.get(shulkerBox.getLocation(), playerUUID);
//            if (loot_chest != null) {
//                shulkerBox.getInventory().setContents(loot_chest.contents);
//            } else {
//                LootTable lootTable = lootTableMapper.get_loot_table(shulkerBox.getLocation());
//                updateChestLoot(shulkerBox, lootTable, playerUUID);
//            }
            // 将箱子标记为被破坏
            new Thread(() ->{
                lootTableMapper.update_broken(shulkerBox.getLocation(), true);
                lootChestMapper.delete(shulkerBox.getLocation());
            }).start();
        }
    }

    /**
     * 追踪爆炸破坏战利品箱子事件
     * @param event
     */
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        // 如果是苦力怕的爆炸，则什么都不做
        if(event.getEntity() instanceof Creeper) return;

        List<Block> blocks = event.blockList();
        for (Block block : blocks) {
            Material type = block.getType();
            if (type == Material.CHEST || type == Material.BARREL || type == Material.TRAPPED_CHEST || type == Material.SHULKER_BOX) {
                // 将箱子标记为被破坏
                new Thread(() -> {
                    lootTableMapper.update_broken(block.getLocation(), true);
                    lootChestMapper.delete(block.getLocation());
                }).start();
            }
        }
    }

    private static void updateChestLoot(Container container, LootTable lootTable, UUID playerUUID) {
        Location loc = container.getLocation();

        if (container instanceof Lootable lootable) {
            LootChest loot_chest = lootChestMapper.get(loc, playerUUID);
            if (loot_chest != null) {  // 有过去打开的数据
                container.getInventory().setContents(loot_chest.contents);
            } else {
                container.getInventory().clear();
                lootable.setLootTable(lootTable);
                lootable.setSeed(System.currentTimeMillis()); // optional: ensures variety
                container.update(false); // apply changes

                new Thread(() -> {
                    MCPlayerManager.addLootChestOpened(playerUUID, 1);
                    lootChestMapper.insert(new LootChest(loc, playerUUID, container.getInventory().getContents()));
                }).start();
            }
        }
    }
}
