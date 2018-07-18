package com.baikaleg.v3.cookingaid.util;

import android.content.Context;

import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AppUtils {

    public static String readFromFile(String fileName, Context context) {
        StringBuilder result = new StringBuilder();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            is = context.getResources().getAssets()
                    .open(fileName);
            isr = new InputStreamReader(is);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (is != null)
                    is.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return result.toString();
    }

    public static List<CatalogEntity> createCatalogEntityList(Context context) throws Exception {
        String str_data = readFromFile("catalog.txt", context);
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
