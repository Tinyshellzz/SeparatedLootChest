package com.tinyshellzz.separatedLootChest.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;

public class MyLocation {
    public String world;
    public double x;
    public double y;
    public double z;
    public float pitch;
    public float yaw;

    public MyLocation(){}

    public MyLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyLocation that = (MyLocation) o;
        return Double.compare(x, that.x) == 0 && Double.compare(y, that.y) == 0 && Double.compare(z, that.z) == 0 && Float.compare(pitch, that.pitch) == 0 && Float.compare(yaw, that.yaw) == 0 && Objects.equals(world, that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z, pitch, yaw);
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
    }
}
