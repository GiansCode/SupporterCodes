package io.alerium.supportercodes.object;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public final class Creator {

    private final UUID uuid;
    private Long supporters = null;
    private Long supportCodeUses = null;

    public Creator(final UUID uuid, final Long supporters, final Long supportCodeUses) {
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

    public Long getSupporters() {
        return this.supporters;
    }

    public Long getSupportCodeUses() {
        return this.supportCodeUses;
    }
}
