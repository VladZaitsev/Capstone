package com.baikaleg.v3.cookingaid.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.baikaleg.v3.cookingaid.R;

import java.util.ArrayList;
import java.util.List;

public class WidgetProductsFactory implements RemoteViewsFactory {

    private List<String> data, products;
    private Context context;

    WidgetProductsFactory(Context context, Intent intent) {
        this.context = context;
        data = intent.getStringArrayListExtra(WidgetProductsProvider.WIDGET_PRODUCTS_EXTRA);
    }

    @Override
    public void onCreate() {
        products = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        products.clear();
        products.addAll(data);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rView = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        rView.setTextViewText(R.id.widget_item_product, products.get(i));
        return rView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
