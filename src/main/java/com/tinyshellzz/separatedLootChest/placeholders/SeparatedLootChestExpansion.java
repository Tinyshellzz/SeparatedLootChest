package com.tinyshellzz.separatedLootChest.placeholders;

import com.tinyshellzz.separatedLootChest.services.MCPlayerManager;
import com.tinyshellzz.separatedLootChest.utils.MyPair;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SeparatedLootChestExpansion extends PlaceholderExpansion {
    private final Plugin plugin;

    public SeparatedLootChestExpansion(Plugin plugin) {
        this.plugin = plugin;
    }

    // 重写getIdentifier方法，返回一个唯一的标识符
    @Override
    public @NotNull String getIdentifier() {
        return "schest";
    }

    // 重写getAuthor方法，返回插件的作者
    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    // 重写getVersion方法，返回插件的版本
    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    // 重写onPlaceholderRequest方法，处理占位符请求
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if(identifier.startsWith("loot-")) {
            ArrayList<MyPair<String, Integer>> ranks = MCPlayerManager.get_loot_chest_opened_rank();

            int num = Integer.parseInt(identifier.split("[_-]")[1]);
            if(ranks.size() < num || num <= 0) {
                return "非法排名" + num;
            }
            if(identifier.endsWith("-name")) {
                return ranks.get(num-1).getKey();
            } else if(identifier.endsWith("-value")) {
                return ranks.get(num-1).getValue().toString();
            }
        }

        return null;
    }
}
