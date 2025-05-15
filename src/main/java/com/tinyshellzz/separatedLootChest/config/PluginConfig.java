package com.tinyshellzz.separatedLootChest.config;


import org.bukkit.configuration.file.YamlConfiguration;

import static com.tinyshellzz.separatedLootChest.ObjectPool.gson;
import static com.tinyshellzz.separatedLootChest.ObjectPool.plugin;


public class PluginConfig {
    public static boolean debug = false;
    private static final ConfigWrapper configWrapper = new ConfigWrapper(plugin, "config.yml");
    public static String db_host;
    public static int db_port;
    public static String db_user;
    public static String db_passwd;
    public static String db_database;

    public static void reload() {
        configWrapper.reloadConfig(); // 重新加载配置文件

        YamlConfiguration yamlconfig = configWrapper.getConfig();
        debug = yamlconfig.getBoolean("debug");
        db_host = yamlconfig.getString("db_host");
        db_port = yamlconfig.getInt("db_port");
        db_user = yamlconfig.getString("db_user");
        db_passwd = yamlconfig.getString("db_passwd");
        db_database = yamlconfig.getString("db_database");
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
