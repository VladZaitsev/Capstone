package com.baikaleg.v3.cookingaid;

import android.content.Context;

import com.baikaleg.v3.cookingaid.data.database.entity.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.model.Ingredient;
import com.baikaleg.v3.cookingaid.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class TestUtil {

    public static List<Ingredient> createIngredientsList(Context context) throws Exception {
        String str_data = AppUtils.readFromFile("ingredients.txt", context);
        JSONArray array = new JSONArray(str_data);
        List<Ingredient> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Ingredient ingredient = new Ingredient(
                    (float) object.getDouble("quantity"),
                    object.getString("measure"),
                    object.getString("ingredient"));
            list.add(ingredient);
        }

        return list;
    }

    public static List<CatalogEntity> createCatalogEntityList(Context context) throws Exception {
        String str_data = AppUtils.readFromFile("catalog.txt", context);
        JSONArray array = new JSONArray(str_data);
        List<CatalogEntity> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            CatalogEntity entity = new CatalogEntity(0, null, object.getString("ingredient"));
            entity.setCalories((float) object.getDouble("calories"));
            entity.setDensity((float) object.getDouble("density"));
            entity.setUnitMeasure(object.getString("unit_measure"));
            entity.setUnitQuantity((float) object.getDouble("unit_weight"));
            list.add(entity);
        }

        return list;
    }
}
