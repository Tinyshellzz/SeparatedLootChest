package com.tinyshellzz.separatedLootChest;

import com.google.gson.Gson;
import com.tinyshellzz.separatedLootChest.database.LootChestMapper;
import com.tinyshellzz.separatedLootChest.database.LootTableMapper;
import com.tinyshellzz.separatedLootChest.database.MCPlayerMapper;
import org.bukkit.plugin.Plugin;

public class ObjectPool {
    public static Plugin plugin;
    public static Gson gson = new Gson();

    public static LootChestMapper lootChestMapper;
    public static LootTableMapper lootTableMapper;
    public static MCPlayerMapper mcPlayerMapper;
}
