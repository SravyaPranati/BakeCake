package com.example.bakecake;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.bakecake.activities.MainActivity;

public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String ingData) {




        Log.e("NewAppWidget","updateAppWidget");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);


        Intent i=new Intent(context, MainActivity.class);
        PendingIntent p=PendingIntent.getActivity(context,0,i,0);

        views.setTextViewText(R.id.appwidget_text,ingData);

        views.setOnClickPendingIntent(R.id.appwidget_text,p);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        BakingService.myServiceMethod(context,"ingredients data appears here");
        Log.e("NewAppWidget","onupdate");
    }

    static  void onUpdateIngredients(Context context,AppWidgetManager appWidgetManager, int[] appWidgetIds,String ingString){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,ingString);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


