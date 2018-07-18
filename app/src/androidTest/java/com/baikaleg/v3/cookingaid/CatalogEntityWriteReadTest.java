package com.baikaleg.v3.cookingaid;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.baikaleg.v3.cookingaid.data.database.AppDatabase;
import com.baikaleg.v3.cookingaid.data.database.dao.CatalogDao;
import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductList;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
       /* List<CatalogEntity> catalogEntities = catalogDao.loadAllProducts();
        assertEquals(16, catalogEntities.size());*/
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
            CatalogEntity entity = loadCatalogEntity(ingredient);
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
        CatalogEntity entity = loadCatalogEntity(ingredient);
        entity.setMeasure(ingredient.getMeasure());
        entity.setQuantity(ingredient.getQuantity());
        return entity;
    }

    //This implementation of searching is very raw because of not structured names of ingredients
    public CatalogEntity loadCatalogEntity(Ingredient ingredient) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                generateQuery("catalog", "ingredient", ingredient.getIngredient()));
        List<CatalogEntity> entities = catalogDao.loadProductsByQuery(query);
        if (entities == null) {
            return null;
        }
        for (int i = 0; i < entities.size(); i++) {
            if (ingredient.getIngredient().contains(entities.get(i).getIngredient())) {
                return entities.get(i);
            }
        }
        return null;
    }

    public String generateQuery(String table, String column, String ingredient) {
        String temp = ingredient;
        if (temp.contains("(") || temp.contains(")")) {
            int first = temp.indexOf("(");
            int last = temp.indexOf(")");
            temp = temp.replace(temp.substring(first, last), "");
        }

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ").append(table).append(" WHERE ");
        List<String> parts = Arrays.asList(temp.split(" "));
        for (int i = 0; i < parts.size(); i++) {
            String request = parts.get(i);
            char c = request.charAt(parts.get(i).length() - 1);
            if (c == 's') {
                request = request.substring(0, request.length() - 1);
            }
            builder.append(column);
            builder.append(" LIKE ");
            builder.append("'%");
            builder.append(request);
            builder.append("%'");
            if (i != parts.size() - 1) {
                builder.append(" OR ");
            } else {
                builder.append(" ORDER BY LENGTH(");
                builder.append(column);
                builder.append(") DESC");
            }
        }
        return builder.toString();
    }
}
