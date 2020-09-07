package io.alerium.supportercodes.listener.event;

import io.alerium.supportercodes.information.wrapper.SupporterWrapper;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class CreatorSupportEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final SupporterWrapper supporterWrapper;

    public CreatorSupportEvent(final SupporterWrapper supporter) {
        this.supporterWrapper = supporter;
    }

    public SupporterWrapper getSupporterWrapper() {
        return this.supporterWrapper;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
