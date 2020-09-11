package io.alerium.supportercodes.information;

import io.alerium.supportercodes.information.wrapper.CreatorWrapper;
import io.alerium.supportercodes.information.wrapper.InformationWrapper;
import io.alerium.supportercodes.information.wrapper.SupporterWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class InformationHandler {

    private final InformationStorage storage;

    InformationHandler(final InformationStorage storage) {
        this.storage = storage;
    }

    public void setWrapper(final UUID uuid, final InformationWrapper wrapper) {
        this.storage.setInformationData(uuid, wrapper);
    }

    public InformationWrapper getWrapper(final UUID uuid) {
        return this.storage.getInformationData(uuid);
    }

    public void removeWrapper(final UUID uuid) {
        this.storage.getInformationRemoval().add(this.storage.getInformationData(uuid));
        this.storage.removeInformationData(uuid);
    }

    public boolean wrapperExists(final UUID uuid) {
        return getWrapper(uuid) != null;
    }

    public boolean isSupporting(final UUID uuid) {
        final SupporterWrapper wrapper = (SupporterWrapper) getWrapper(uuid);

        return !wrapper.getSupportedCreatorID().equalsIgnoreCase("none");
    }

    public boolean isSupportingCreator(final UUID userID, final UUID creatorID) {
        final SupporterWrapper wrapper = (SupporterWrapper) getWrapper(userID);

        return wrapper.getSupportedCreatorID().equalsIgnoreCase(creatorID.toString());
    }

    public boolean isCreator(final UUID uuid) {
        return getWrapper(uuid) instanceof CreatorWrapper;
    }

    public void stopUserSupporting(final UUID uuid) {
        final SupporterWrapper wrapper = (SupporterWrapper) getWrapper(uuid);
        final CreatorWrapper creatorWrapper = (CreatorWrapper) getWrapper(UUID.fromString(wrapper.getSupportedCreatorID()));

        wrapper.setSupportedCreatorID("none");
        creatorWrapper.setSupporters(-1);

        this.storage.setInformationData(uuid, wrapper);
        this.storage.setInformationData(creatorWrapper.getUserID(), creatorWrapper);
    }

    public void setUserSupporting(final UUID uuid, final UUID creatorUUID) {
        final SupporterWrapper wrapper = (SupporterWrapper) getWrapper(uuid);
        final CreatorWrapper creatorWrapper = (CreatorWrapper) getWrapper(creatorUUID);

        wrapper.setSupportedCreatorID(creatorUUID.toString());
        wrapper.setSupporterSince(System.currentTimeMillis());

        creatorWrapper.setSupporters(+1);

        this.storage.setInformationData(uuid, wrapper);
        this.storage.setInformationData(creatorUUID, creatorWrapper);
    }

    public void handleSupportCodeUses(final UUID uuid) {
        final SupporterWrapper wrapper = (SupporterWrapper) getWrapper(uuid);
        final CreatorWrapper creatorWrapper = (CreatorWrapper) getWrapper(UUID.fromString(wrapper.getSupportedCreatorID()));

        creatorWrapper.setSupportCodeUses(+1);

        this.storage.setInformationData(creatorWrapper.getUserID(), creatorWrapper);
    }

    public List<CreatorWrapper> getCreatorWrappers() {
        final List<CreatorWrapper> wrappers = new ArrayList<>();

        for (final UUID identifier : storage.getInformation().keySet()) {
            final InformationWrapper wrapper = getWrapper(identifier);
            if (!(wrapper instanceof CreatorWrapper)) {
                continue;
            }

            wrappers.add((CreatorWrapper) wrapper);
        }

        return wrappers;
    }
}
