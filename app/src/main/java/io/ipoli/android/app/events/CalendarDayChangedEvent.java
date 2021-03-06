package io.ipoli.android.app.events;

import org.joda.time.LocalDate;

import io.ipoli.android.app.utils.Time;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 5/1/16.
 */
public class CalendarDayChangedEvent {
    public final LocalDate date;
    public final Source source;
    public final Time time;

    public CalendarDayChangedEvent(LocalDate date, Source source) {
        this.date = date;
        this.source = source;
        this.time = null;
    }

    public CalendarDayChangedEvent(LocalDate date, Time time, Source source) {
        this.date = date;
        this.source = source;
        this.time = time;
    }

    public enum Source {SWIPE, MENU, CALENDAR}
}