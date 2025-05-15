package com.tinyshellzz.separatedLootChest;

import com.tinyshellzz.separatedLootChest.config.PluginConfig;
import com.tinyshellzz.separatedLootChest.database.ChunkScannedMapper;
import com.tinyshellzz.separatedLootChest.database.LootChestMapper;
import com.tinyshellzz.separatedLootChest.database.LootTableMapper;
import com.tinyshellzz.separatedLootChest.listeners.ChunkChestScanner;
import com.tinyshellzz.separatedLootChest.listeners.ContainerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public class SeparatedLootChest extends JavaPlugin {

    @Override
    public void onEnable() {
        // team,启动！
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TccTools]" + ChatColor.AQUA + "######################");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TccTools]" + ChatColor.AQUA + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TccTools]" + ChatColor.AQUA + "#SeparatedLootChest已启动#");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TccTools]" + ChatColor.AQUA + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TccTools]" + ChatColor.AQUA + "######################");

        init();
        register();
    }

    public void init(){
        ObjectPool.plugin = this;
        PluginConfig.reload();

        ObjectPool.lootChestMapper = new LootChestMapper();
        ObjectPool.lootTableMapper = new LootTableMapper();
        ObjectPool.chunkScannedMapper = new ChunkScannedMapper();
    }

    public void register() {
        // 注册监听器
        this.getServer().getPluginManager().registerEvents(new ContainerInteractListener(), this);
        this.getServer().getPluginManager().registerEvents(new ChunkChestScanner(), this);

    }

    @Override
    public void onDisable() {
        //TODO
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TccTools]" + ChatColor.RED + "######################");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TccTools]" + ChatColor.RED + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TccTools]" + ChatColor.RED + "#SeparatedLootChest已关闭#");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TccTools]" + ChatColor.RED + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TccTools]" + ChatColor.RED + "######################");
    }

}