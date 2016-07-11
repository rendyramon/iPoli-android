package io.ipoli.android.quest.events.subquests;

import io.ipoli.android.app.events.EventSource;
import io.ipoli.android.quest.data.Subquest;

/**
 * Created by Polina Zhelyazkova <polina@ipoli.io>
 * on 7/11/16.
 */
public class DeleteSubquestEvent {
    public final Subquest subquest;
    public final EventSource source;

    public DeleteSubquestEvent(Subquest subquest, EventSource source) {
        this.subquest = subquest;
        this.source = source;
    }
}
