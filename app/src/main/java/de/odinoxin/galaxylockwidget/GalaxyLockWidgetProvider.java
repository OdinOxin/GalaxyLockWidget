package de.odinoxin.galaxylockwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.RemoteViews;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GalaxyLockWidgetProvider extends AppWidgetProvider {

    public static final String PREFS_NAME = "de.odinoxin.galaxylockwidget.GalaxyLockWidgetProvider.PREFERENCES";
    public static final String KEY_GRAVITY = "de.odinoxin.galaxylockwidget.GalaxyLockWidgetProvider.KEY_GRAVITY";
    private static final String ACTION_UPDATE = "de.odinoxin.galaxylockwidget.GalaxyLockWidgetProvider.ACTION_UPDATE";

    private static long lastClick = 0;
    private static int currentLayout = 0;

    @Override
    public void onEnabled(Context context) {
        this.update(context);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        this.update(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        this.update(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent != null && intent.getAction().equals(GalaxyLockWidgetProvider.ACTION_UPDATE)) {
            this.update(context);

            long now = System.currentTimeMillis();
            if (now - GalaxyLockWidgetProvider.lastClick < 1000) {
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.urbandroid.sleep");
                if (launchIntent != null)
                    context.startActivity(launchIntent);
                launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.sec.android.app.clockpackage");
                if (launchIntent != null)
                    context.startActivity(launchIntent);
            }
            GalaxyLockWidgetProvider.lastClick = now;
        }
    }

    private void update(final Context context) {
        if (GalaxyLockWidgetProvider.currentLayout == 0) {
            SharedPreferences prefs = context.getSharedPreferences(GalaxyLockWidgetProvider.PREFS_NAME, Context.MODE_PRIVATE);
            GalaxyLockWidgetProvider.currentLayout = prefs.getInt(GalaxyLockWidgetProvider.KEY_GRAVITY, R.layout.widgetlayout_top_start);
        }
        RemoteViews views = new RemoteViews(context.getPackageName(), GalaxyLockWidgetProvider.currentLayout);
        views.setTextViewText(R.id.tvWidget, context.getString(R.string.updating));
        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, GalaxyLockWidgetProvider.class), views);
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.schedule(new Runnable() {
            @Override
            public void run() {
                Intent nextIntent = new Intent(context, GalaxyLockWidgetProvider.class);
                nextIntent.setAction(GalaxyLockWidgetProvider.ACTION_UPDATE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent, 0);
                AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                long trigger = alarmMgr.getNextAlarmClock() == null ? -1 : alarmMgr.getNextAlarmClock().getTriggerTime();
                SharedPreferences prefs = context.getSharedPreferences(GalaxyLockWidgetProvider.PREFS_NAME, Context.MODE_PRIVATE);
                int nextLayout = prefs.getInt(GalaxyLockWidgetProvider.KEY_GRAVITY, GalaxyLockWidgetProvider.currentLayout);
                RemoteViews views = new RemoteViews(context.getPackageName(), nextLayout);
                views.setTextViewText(R.id.tvWidget, trigger < 0 ? context.getString(R.string.no_alarm) : DateUtils.getRelativeDateTimeString(context, trigger, DateUtils.DAY_IN_MILLIS, DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_NO_NOON));
                views.setOnClickPendingIntent(R.id.tvWidget, pendingIntent);
                AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, GalaxyLockWidgetProvider.class), views);
                GalaxyLockWidgetProvider.currentLayout = nextLayout;
            }
        }, 500, TimeUnit.MILLISECONDS);
    }
}
