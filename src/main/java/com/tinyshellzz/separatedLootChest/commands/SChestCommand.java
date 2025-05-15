package com.tinyshellzz.separatedLootChest.commands;

import com.tinyshellzz.separatedLootChest.config.PluginConfig;
import com.tinyshellzz.separatedLootChest.services.ChestCommandService;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SChestCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // 判断命令发送者是否是玩家，
        Matcher _m = Pattern.compile("^.*CraftRemoteConsoleCommandSender.*$").matcher(sender.toString());
        if(!(sender instanceof ConsoleCommandSender || _m.find() || sender.isOp())){
            sender.sendMessage("只有控制台和op才能使用该命令");
            return true;
        }


        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "参数不足");
            return true;
        }

        String subcommand = args[0].toLowerCase();
        if(subcommand.equals("reload")) {
            PluginConfig.reload();
            return true;
        } else if (subcommand.equals("refresh")) {
            return ChestCommandService.refresh(sender, command, s, args);
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // 判断命令参数的长度
        if (args.length == 1) {
            // 如果只有一个参数，返回所有子命令的列表
            return Arrays.asList("reload", "refresh");
        } else if (args.length == 2) {
            // 如果有两个参数，根据第一个参数返回不同的补全列表
            String subcommand = args[0].toLowerCase();

            if(subcommand.equals("refresh")) {
                // 返回所有玩家昵称
                return Arrays.asList("world", "nether", "end");
            }
        }

        return null;
    }
}
