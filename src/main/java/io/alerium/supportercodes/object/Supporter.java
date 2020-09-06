package io.alerium.supportercodes.object;

import java.util.UUID;

public final class Supporter {

    private final UUID uuid;
    private Long supportSince = null;
    private String supporting = null;

    public Supporter(final UUID uuid) {
        this.uuid = uuid;
    }

    public void setSupporterSince(final Long time) {
        this.supportSince = time;
    }

    public String getSupporting() {
        return this.supporting;
    }

    public void setSupporting(final String creatorUUID) {
        this.supportSince = System.currentTimeMillis();
        this.supporting = creatorUUID;
    }

    public Long getSupportingSince() {
        return this.supportSince;
    }

    public UUID getId() {
        return this.uuid;
    }
}
