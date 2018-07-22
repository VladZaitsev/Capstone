package com.baikaleg.v3.cookingaid.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.baikaleg.v3.cookingaid.data.database.converters.DateConverter;
import com.baikaleg.v3.cookingaid.data.database.dao.CatalogDao;
import com.baikaleg.v3.cookingaid.data.database.dao.ProductDao;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.util.AppUtils;

import java.util.List;

@Database(entities = {CatalogEntity.class, ProductEntity.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "cookingaid";
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                new Thread(() -> {
                                    try {
                                        List<CatalogEntity> catalogList = AppUtils.createCatalogEntityList(context);
                                        if (catalogList != null) {
                                            for (CatalogEntity entity :
                                                    catalogList) {
                                                getInstance(context).catalogDao().insertProduct(entity);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }).start();
                            }
                        })
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return instance;
    }

    public abstract CatalogDao catalogDao();

    public abstract ProductDao productDao();
}