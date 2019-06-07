package com.example.bakecake;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class BakingService extends IntentService {

    private static String string = "key";

    public BakingService() {
        super("BakingService");
    }

   public static void myServiceMethod(Context context, String ingData) {
        Intent i = new Intent(context, BakingService.class);
        i.putExtra("recipeIngData", ingData);
        i.setAction(string);
        context.startService(i);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent.getAction().equals(string)) {
            myMethod(intent.getStringExtra("recipeIngData"));

        }

    }

    void myMethod(String recipeIngredients) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetids = appWidgetManager.getAppWidgetIds(new ComponentName(this, NewAppWidget.class));
        NewAppWidget.onUpdateIngredients(this, appWidgetManager, appWidgetids, recipeIngredients);
    }


}

