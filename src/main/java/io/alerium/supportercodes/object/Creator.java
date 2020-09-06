package io.alerium.supportercodes.object;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public final class Creator {

    private final UUID uuid;
    private long supporters;
    private long supportCodeUses;

    public Creator(final UUID uuid, final long supporters, final long supportCodeUses) {
        this.uuid = uuid;
        this.supporters = supporters;
        this.supportCodeUses = supportCodeUses;
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }

    public UUID getId() {
        return this.uuid;
    }

    public void incrementSupporters() {
        this.supporters += 1;
    }

    public void incrementCodeUses() {
        this.supportCodeUses += 1;
    }

    public long getSupporters() {
        return this.supporters;
    }

    public long getSupportCodeUses() {
        return this.supportCodeUses;
    }
}
