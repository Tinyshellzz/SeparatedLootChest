package com.tinyshellzz.separatedLootChest.services;

import com.tinyshellzz.separatedLootChest.database.ChunkScannedMapper;
import com.tinyshellzz.separatedLootChest.listeners.ChunkChestScanner;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static com.tinyshellzz.separatedLootChest.ObjectPool.chunkScannedMapper;
import static com.tinyshellzz.separatedLootChest.ObjectPool.lootChestMapper;

public class ChestCommandService {
    public static boolean refresh(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.YELLOW + "用法: /schest refresh <世界>");
            return true;
        }

        if (args[1].equalsIgnoreCase("world") || args[1].equalsIgnoreCase("0")) {
            lootChestMapper.delete_by_world(0);
            sender.sendMessage(ChatColor.GREEN + args[1] + " 主世界战利品箱子刷新成功");
        } else if (args[1].equalsIgnoreCase("nether") || args[1].equalsIgnoreCase("1")) {
            lootChestMapper.delete_by_world(1);
            sender.sendMessage(ChatColor.GREEN + args[1] + " 下界战利品箱子刷新成功");
        } else if (args[1].equalsIgnoreCase("end") || args[1].equalsIgnoreCase("2")) {
            lootChestMapper.delete_by_world(2);
            sender.sendMessage(ChatColor.GREEN + args[1] + " 末地战利品箱子刷新成功");
        } else {
            sender.sendMessage(ChatColor.RED + args[1] + " 世界不存在");
        }

        return true;
    }

    public static boolean rescan(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        chunkScannedMapper.delete_table();
        new ChunkScannedMapper();
        ChunkChestScanner.chunkScanned.clear();
        return true;
    }
}
