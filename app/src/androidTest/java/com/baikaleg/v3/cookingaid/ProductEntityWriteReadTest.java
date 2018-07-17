package com.baikaleg.v3.cookingaid;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.baikaleg.v3.cookingaid.data.database.AppDatabase;
import com.baikaleg.v3.cookingaid.data.database.dao.CatalogDao;
import com.baikaleg.v3.cookingaid.data.database.dao.ProductDao;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class ProductEntityWriteReadTest {

    private ProductDao productDao;

    private AppDatabase db;
    private Context context;

    @Before
    public void createDb() {
        context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        productDao = db.productDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }


}
