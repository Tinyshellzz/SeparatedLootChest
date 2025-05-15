package com.tinyshellzz.separatedLootChest.entity;

import com.tinyshellzz.separatedLootChest.services.ItemStackManager;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static com.tinyshellzz.separatedLootChest.ObjectPool.gson;
import static com.tinyshellzz.separatedLootChest.ObjectPool.gson;

public class LootChest {
    public MyLocation location;
    public UUID player_uuid;
    public ItemStack[] contents;

    public LootChest(MyLocation location, UUID player_uuid, ItemStack[] contents) {
        this.location = location;
        this.player_uuid = player_uuid;
        this.contents = contents;
    }

    public LootChest(Location location, UUID player_uuid, ItemStack[] contents) {
        this.location = new MyLocation(location);
        this.player_uuid = player_uuid;
        this.contents = contents;
    }

    public LootChest(String location, String player_uuid, String contents) {
        this.location = gson.fromJson(location, MyLocation.class);
        this.player_uuid = UUID.fromString(player_uuid);
        this.contents = ItemStackManager.Base64ToItemStackArray(contents);
    }
}
