package org.kamusbahasarejang.kamusbahasarejang;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;


public class AppWidgetProviderKaganga extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, Kamus_manual.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_kaganga);
            views.setOnClickPendingIntent(R.id.Widget_Kaganga, pendingIntent);

            Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}