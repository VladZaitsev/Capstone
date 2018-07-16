package com.baikaleg.v3.cookingaid;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.baikaleg.v3.cookingaid.data.database.AppDatabase;
import com.baikaleg.v3.cookingaid.data.database.dao.CatalogDao;
import com.baikaleg.v3.cookingaid.data.database.dao.ProductDao;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;
import com.baikaleg.v3.cookingaid.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class CatalogEntityWriteReadTest {

    private CatalogDao catalogDao;
    private AppDatabase db;
    private Context context;

    @Before
    public void createDb() {
        context = InstrumentationRegistry.getTargetContext();

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        catalogDao = db.catalogDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeCatalogItemsAndReadInList() throws Exception {
        List<CatalogEntity> catalogEntities = TestUtil.createCatalogEntityList(context);

        for (CatalogEntity entity :
                catalogEntities) {
            Log.i("entity", entity.toString());
        }

        List<Ingredient> ingredients = TestUtil.createIngredientsList(context);
        for (Ingredient ingredient :
                ingredients) {
            Log.i("ingredient", ingredient.toString());
        }

        //List<CatalogEntity> catalogEntities = catalogDao.loadAllProducts();
        // assertEquals(16, catalogEntities.size());
    }


}
