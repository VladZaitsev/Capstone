package com.baikaleg.v3.cookingaid.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.data.Repository;

public class WidgetProductsProvider extends AppWidgetProvider {
    public final static String WIDGET_PRODUCTS_EXTRA = "widget_products_extra";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int id : appWidgetIds) {
            update(context, appWidgetManager, id);
        }
    }

    private static void update(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_products);
        Repository repository = Repository.getInstance(context);

        repository.loadExpiryProductsNames().subscribe(strings -> {
            rv.setViewVisibility(R.id.widget_empty_txt, View.GONE);

            Intent adapter = new Intent(context, WidgetProductsService.class);
            adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            Uri data = Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME));
            adapter.putStringArrayListExtra(WIDGET_PRODUCTS_EXTRA, strings);

            adapter.setData(data);
            rv.setRemoteAdapter(R.id.widget_products_list, adapter);

            appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProductsProvider.class), rv);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_products_list);
        });
    }
}
