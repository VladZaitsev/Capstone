package com.baikaleg.v3.cookingaid.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetProductsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetProductsFactory(getApplicationContext(), intent);
    }
}
