package io.ipoli.android.quest.schedulers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;

import java.util.concurrent.TimeUnit;

import io.ipoli.android.Constants;
import io.ipoli.android.app.utils.IntentUtils;
import io.ipoli.android.quest.receivers.ShowQuestCompleteNotificationReceiver;
import io.ipoli.android.quest.receivers.StartQuestTimerReceiver;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 2/2/16.
 */
public class QuestNotificationScheduler {

    public static void scheduleUpdateTimer(String questId, Context context) {
        Intent intent = getQuestTimerIntent(questId);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                TimeUnit.MINUTES.toMillis(1), IntentUtils.getBroadcastPendingIntent(context, intent));
    }

    public static void stopTimer(String questId, Context context) {
        cancelUpdateTimerIntent(questId, context);
        dismissTimerNotification(context);
    }

    @NonNull
    private static Intent getQuestTimerIntent(String questId) {
        Intent intent = new Intent(StartQuestTimerReceiver.ACTION_SHOW_QUEST_TIMER);
        intent.putExtra(Constants.QUEST_ID_EXTRA_KEY, questId);
        return intent;
    }

    private static void cancelUpdateTimerIntent(String questId, Context context) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(IntentUtils.getBroadcastPendingIntent(context, getQuestTimerIntent(questId)));
    }

    private static void dismissTimerNotification(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel(Constants.QUEST_TIMER_NOTIFICATION_ID);
    }

    public static void scheduleDone(String questId, long scheduleTimeMillis, Context context) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent showDonePendingIntent = getShowDonePendingIntent(questId, context);
        if (Build.VERSION.SDK_INT > 22) {
            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, scheduleTimeMillis, showDonePendingIntent);
        } else {
            alarm.setExact(AlarmManager.RTC_WAKEUP, scheduleTimeMillis, showDonePendingIntent);
        }
    }

    public static void stopDone(String questId, Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel(Constants.QUEST_COMPLETE_NOTIFICATION_ID);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(getShowDonePendingIntent(questId, context));
    }

    private static PendingIntent getShowDonePendingIntent(String questId, Context context) {
        Intent intent = new Intent(ShowQuestCompleteNotificationReceiver.ACTION_SHOW_DONE_QUEST_NOTIFICATION);
        intent.putExtra(Constants.QUEST_ID_EXTRA_KEY, questId);
        return IntentUtils.getBroadcastPendingIntent(context, intent);
    }

    public static void stopAll(String questId, Context context) {
        stopDone(questId, context);
        stopTimer(questId, context);
    }
}
