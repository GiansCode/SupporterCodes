package io.alerium.supportercodes.storage;

import io.alerium.supportercodes.object.Creator;
import io.alerium.supportercodes.object.Supporter;

import java.util.*;

public final class InformationStorage {

    private final Map<UUID, Supporter> supporters = new HashMap<>();
    private final Map<UUID, Creator> creators = new HashMap<>();
    private final List<UUID> removal = new ArrayList<>();

    public Creator getCreator(final UUID identifier) {
        return creators.get(identifier);
    }

    public Supporter getSupporter(final UUID identifier) {
        return supporters.get(identifier);
    }

    public void setSupporter(final UUID identifier, final Supporter supporter) {
        this.supporters.put(identifier, supporter);
    }

    public void setCreator(final UUID identifier, final Creator creator) {
        this.creators.put(identifier, creator);
    }

    public Map<UUID, Creator> getCreators() {
        return this.creators;
    }

    public Map<UUID, Supporter> getSupporters() {
        return this.supporters;
    }

    public List<UUID> getRemoval() {
        return this.removal;
    }
}
