package com.baikaleg.v3.cookingaid.util;

import android.content.Context;

import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
            entity.setExpiration(object.getInt("expiration"));
            list.add(entity);
        }

        return list;
    }

    public static String productLoadQuery(String table, String column, String ingredient) {
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
