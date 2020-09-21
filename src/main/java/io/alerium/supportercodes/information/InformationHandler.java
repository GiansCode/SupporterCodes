package io.alerium.supportercodes.information;

import io.alerium.supportercodes.information.wrapper.CreatorWrapper;
import io.alerium.supportercodes.information.wrapper.InformationWrapper;
import io.alerium.supportercodes.information.wrapper.SupporterWrapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public boolean isCreator(final UUID uuid) {
        return getWrapper(uuid) instanceof CreatorWrapper;
    }

    public void stopUserSupporting(final UUID uuid) {
        final SupporterWrapper wrapper = (SupporterWrapper) getWrapper(uuid);
        final CreatorWrapper creatorWrapper = (CreatorWrapper) getWrapper(UUID.fromString(wrapper.getSupportedCreatorID()));

        wrapper.setSupportedCreatorID("none");
        wrapper.setSupporterSince(0);
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
        final SupporterWrapper supporterWrapper = (SupporterWrapper) getWrapper(uuid);
        final CreatorWrapper creatorWrapper = (CreatorWrapper) getWrapper(UUID.fromString(supporterWrapper.getSupportedCreatorID()));

        creatorWrapper.setSupportCodeUses(+1);

        this.storage.setInformationData(creatorWrapper.getUserID(), creatorWrapper);
    }

    public List<InformationWrapper> getCreatorWrappers() {
        return storage.getInformation().keySet().stream()
                .map(this::getWrapper)
                .filter(CreatorWrapper.class::isInstance)
                .collect(Collectors.toList());
    }

    public Map<UUID, InformationWrapper> getWrappers() {
        return this.storage.getInformation();
    }
}
