package io.alerium.supportercodes.object;

import java.util.UUID;

public final class Supporter {

    private final UUID uuid;
    private Long supportSince;
    private UUID supporting;

    public Supporter(final UUID uuid) {
        this.uuid = uuid;
    }

    public void setSupporterSince(final Long time) {
        this.supportSince = time;
    }

    public UUID getSupporting() {
        return this.supporting;
    }

    public void setSupporting(final UUID creatorUUID) {
        this.supportSince = System.currentTimeMillis();
        this.supporting = creatorUUID;
    }

    public long getSupportingSince() {
        return this.supportSince;
    }

    public UUID getId() {
        return this.uuid;
    }
}
