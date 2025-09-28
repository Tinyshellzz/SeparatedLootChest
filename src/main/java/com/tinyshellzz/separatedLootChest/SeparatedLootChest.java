package com.tinyshellzz.separatedLootChest;

import com.tinyshellzz.separatedLootChest.commands.SChestCommand;
import com.tinyshellzz.separatedLootChest.config.PluginConfig;
import com.tinyshellzz.separatedLootChest.database.LootChestMapper;
import com.tinyshellzz.separatedLootChest.database.LootTableMapper;
import com.tinyshellzz.separatedLootChest.database.MCPlayerMapper;
import com.tinyshellzz.separatedLootChest.listeners.ContainerInteractListener;
import com.tinyshellzz.separatedLootChest.listeners.PlayerJoinListener;
import com.tinyshellzz.separatedLootChest.placeholders.SeparatedLootChestExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public class SeparatedLootChest extends JavaPlugin {

    @Override
    public void onEnable() {
        // team,启动！
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[schest]" + ChatColor.AQUA + "######################");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[schest]" + ChatColor.AQUA + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[schest]" + ChatColor.AQUA + "#SeparatedLootChest已启动#");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[schest]" + ChatColor.AQUA + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[schest]" + ChatColor.AQUA + "######################");

        init();
//        test.run();
        register();
    }

    public void init(){
        ObjectPool.plugin = this;
        PluginConfig.reload();

        ObjectPool.lootChestMapper = new LootChestMapper();
        ObjectPool.lootTableMapper = new LootTableMapper();
        ObjectPool.mcPlayerMapper = new MCPlayerMapper();
    }

    public void register() {
        // 注册监听器
        this.getServer().getPluginManager().registerEvents(new ContainerInteractListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        // 注册命令
        this.getCommand("schest").setExecutor(new SChestCommand());

        // 注册 PlaceHolder
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SeparatedLootChestExpansion(this).register();
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[schest]" + ChatColor.GREEN + "PlaceholderAPI 准备就绪");
        }
    }

    @Override
    public void onDisable() {
        //TODO
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[schest]" + ChatColor.RED + "######################");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[schest]" + ChatColor.RED + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[schest]" + ChatColor.RED + "#SeparatedLootChest已关闭#");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[schest]" + ChatColor.RED + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[schest]" + ChatColor.RED + "######################");
    }

}