package com.baikaleg.v3.cookingaid.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
}
