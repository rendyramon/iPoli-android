package io.ipoli.android.quest.data;

import java.util.Date;

import io.ipoli.android.app.net.RemoteObject;
import io.ipoli.android.app.utils.DateUtils;
import io.ipoli.android.app.utils.IDGenerator;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 3/26/16.
 */
public class Reminder extends RealmObject implements RemoteObject<Reminder> {

    @Required
    @PrimaryKey
    private String id;

    private String message;

    @Required
    private Long minutesFromStart;

    @Required
    private Integer notificationId;

    @Required
    private Integer intentId;

    @Required
    private Date createdAt;

    @Required
    private Date updatedAt;

    private Boolean needsSyncWithRemote;
    private String remoteId;
    private Boolean isDeleted;

    public Reminder() {
    }

    public Reminder(long minutesFromStart, int notificationId, int intentId) {
        this.id = IDGenerator.generate();
        this.intentId = intentId;
        this.notificationId = notificationId;
        this.minutesFromStart = minutesFromStart;
        createdAt = DateUtils.nowUTC();
        updatedAt = DateUtils.nowUTC();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void markUpdated() {
        setNeedsSync();
        setUpdatedAt(DateUtils.nowUTC());
    }

    @Override
    public void setNeedsSync() {
        needsSyncWithRemote = true;
    }

    @Override
    public boolean needsSyncWithRemote() {
        return needsSyncWithRemote;
    }

    @Override
    public void setSyncedWithRemote() {
        needsSyncWithRemote = false;
    }


    @Override
    public String getRemoteId() {
        return remoteId;
    }

    @Override
    public boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public void markDeleted() {
        isDeleted = true;
        markUpdated();
    }

    @Override
    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getMinutesFromStart() {
        return minutesFromStart;
    }

    public void setMinutesFromStart(long minutesFromStart) {
        this.minutesFromStart = minutesFromStart;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public Integer getIntentId() {
        return intentId;
    }

    public void setIntentId(int intentId) {
        this.intentId = intentId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}
