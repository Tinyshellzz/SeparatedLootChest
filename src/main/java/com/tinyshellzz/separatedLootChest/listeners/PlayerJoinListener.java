package com.tinyshellzz.separatedLootChest.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static com.tinyshellzz.separatedLootChest.ObjectPool.mcPlayerMapper;

/**
 * 新玩家欢迎
 * 给新玩家一个床
 */
public class PlayerJoinListener implements Listener {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        new Thread(() -> {mcPlayerMapper.update_player_name(player);}).start();
    }
}
