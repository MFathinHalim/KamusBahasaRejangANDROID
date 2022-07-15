package org.kamusbahasarejang.kamusbahasarejang;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;


public class AppWidgetProviderSearch extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, searchengine.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_search);
            views.setOnClickPendingIntent(R.id.WidgetCariB, pendingIntent);

            Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}