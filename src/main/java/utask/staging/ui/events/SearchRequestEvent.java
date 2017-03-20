package utask.staging.ui.events;

import utask.commons.events.BaseEvent;

public class SearchRequestEvent extends BaseEvent {

    public final String searchKeywords;

    public SearchRequestEvent(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
