package com.baikaleg.v3.cookingaid;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.baikaleg.v3.cookingaid.data.database.AppDatabase;
import com.baikaleg.v3.cookingaid.data.database.dao.CatalogDao;
import com.baikaleg.v3.cookingaid.data.database.entity.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.ProductList;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;
import com.baikaleg.v3.cookingaid.util.AppUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CatalogEntityWriteReadTest {

    private CatalogDao catalogDao;
    private AppDatabase db;
    private Context context;

    @Before
    public void createDb() throws Exception {
        context = InstrumentationRegistry.getTargetContext();

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        catalogDao = db.catalogDao();

        List<CatalogEntity> catalogList = TestUtil.createCatalogEntityList(context);
        for (CatalogEntity entity :
                catalogList) {
            catalogDao.insertProduct(entity);
        }
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeCatalogItemsAndReadInListTest() {
        catalogDao.loadAllProducts().subscribeOn(Schedulers.io())
                .test().assertNoErrors();
    }

    @Test
    public void calculateCaloriesForTSPIngredientTest() {
        float expectedCalories = 113.76f;
        Ingredient ingredient = new Ingredient(40, "TSP", "baking powder");
        float result = createEntity(ingredient).getTotalCalories();
        assertEquals(expectedCalories, result, 0.1);
    }

    @Test
    public void calculateCaloriesForUnitIngredientTest() {
        float expectedCalories = 800f;
        Ingredient ingredient = new Ingredient(10, "UNIT", "large eggs");
        float result = createEntity(ingredient).getTotalCalories();
        assertEquals(expectedCalories, result, 0.1);
    }

    @Test
    public void calculateCaloriesForIngredientListTest() throws Exception {
        float expectedCalories = 913.76f;
        ProductList productList = new ProductList();
        productList.add(createEntity(new Ingredient(10, "UNIT", "large eggs")));
        productList.add(createEntity(new Ingredient(40, "TSP", "baking powder")));
        float result = productList.getTotalCalories();
        assertEquals(expectedCalories, result, 0.1);
    }

    @Test
    public void mergeCatalogAndIngredientsListTest() throws Exception {
        List<Ingredient> ingredients = TestUtil.createIngredientsList(context);
        List<CatalogEntity> catalogEntities = new ArrayList<>();
        for (Ingredient ingredient :
                ingredients) {
            CatalogEntity entity = loadCatalogEntity(ingredient).blockingGet();
            if (entity != null) {
                entity.setQuantity(ingredient.getQuantity());
                entity.setMeasure(ingredient.getMeasure());
                catalogEntities.add(entity);
            }
        }
        assertEquals(ingredients.size(), catalogEntities.size());
    }

    @Test
    public void calculateWeightForIngredientTest() {
        float expectedWeight = 0.144f;
        Ingredient ingredient = new Ingredient(40, "TSP", "baking powder");
        float result = createEntity(ingredient).getTotalWeight();
        assertEquals(expectedWeight, result, 0.1);
    }

    @Test
    public void mergeWeightForDifferentIngredientMeasureTest() {
        float expectedWeight = 0.144f;
        Ingredient ingredient = new Ingredient(40, "TSP", "baking powder");
        float result = createEntity(ingredient).getTransformedQuantity("K");
        assertEquals(expectedWeight, result, 0.1);
    }

    public CatalogEntity createEntity(Ingredient ingredient) {
        CatalogEntity entity = loadCatalogEntity(ingredient).blockingGet();
        entity.setMeasure(ingredient.getMeasure());
        entity.setQuantity(ingredient.getQuantity());
        return entity;
    }

    //This implementation of searching is very raw because of not structured names of ingredients
    public Maybe<CatalogEntity> loadCatalogEntity(Ingredient ingredient) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                AppUtils.productLoadQuery("catalog", "ingredient", ingredient.getIngredient()));
        return catalogDao.loadProductsByQuery(query)
                .flatMapIterable((Function<List<CatalogEntity>, Iterable<CatalogEntity>>) entities -> entities)
                .filter((entity -> ingredient.getIngredient().contains(entity.getIngredient())))
                .firstElement();
    }
}
