package tcc.youajing.tcctools.entity;

import java.util.UUID;

public class MCPlayer {
    public String name;
    public UUID uuid;
    public int fished_times;
    public int debris_mined;

    public MCPlayer(String name, UUID uuid, int fished_times, int debris_mined) {
        this.name = name.toLowerCase();
        this.uuid = uuid;
        this.fished_times = fished_times;
        this.debris_mined = debris_mined;
    }
}
