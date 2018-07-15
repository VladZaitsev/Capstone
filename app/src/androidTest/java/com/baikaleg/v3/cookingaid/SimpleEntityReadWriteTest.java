package com.baikaleg.v3.cookingaid;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.baikaleg.v3.cookingaid.data.database.AppDatabase;
import com.baikaleg.v3.cookingaid.data.database.dao.CatalogDao;
import com.baikaleg.v3.cookingaid.data.database.dao.ProductDao;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {

    private CatalogDao catalogDao;
    private ProductDao productDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        catalogDao = db.catalogDao();
        productDao = db.productDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeCatalogItemAndReadInList() throws Exception {
        CatalogEntity catalogEntity = new CatalogEntity(4, "kg", "bread");
        ProductEntity productEntity = new ProductEntity(5, "unit", "egg");

        catalogDao.insertProduct(catalogEntity);
        productDao.insertProduct(productEntity);

        List<CatalogEntity> catalogEntities = catalogDao.loadAllProducts();
        List<ProductEntity> productEntities = productDao.loadAllProducts();

        // List<User> byName = catalogDao.findUsersByName("george");
        assertNotEquals(0, catalogEntities.size());
        assertNotEquals(0, productEntities.size());
    }
}
