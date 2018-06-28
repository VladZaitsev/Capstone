package com.baikaleg.v3.cookingaid.data.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.baikaleg.v3.cookingaid.R;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeApi {
    private Context context;

    public RecipeApi(Context context) {
        this.context = context;
    }

    @NonNull
    public RecipeService createService() {
        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.recipes_resource))
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RecipeService.class);
    }
}