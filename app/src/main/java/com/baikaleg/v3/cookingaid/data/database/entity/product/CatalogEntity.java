package com.baikaleg.v3.cookingaid.data.database.entity.product;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.baikaleg.v3.cookingaid.data.model.Ingredient;

@Entity(tableName = "catalog")
public class CatalogEntity extends Ingredient implements Product {

    @PrimaryKey(autoGenerate = true)
    private int id;
    //Number of calories for 100 grams
    private float calories;
    //Density of ingredient for wright calculating (g/ml)
    private float density;
    //Price for one gram of product
    private float price;
    //Number of days to the expiry date
    private int expiration;

    //This block of information is valid if measure of ingredient is "UNIT"
    private float unitQuantity;
    private String unitMeasure;

    public CatalogEntity(float quantity, String measure, String ingredient) {
        super(quantity, measure, ingredient);
    }

    private float convertToGrams(String from, float value, float density) {
        switch (from) {
            case "G":
                return value;
            case "K":
                return 1000 * value;
            case "TSP":
                return 5f * density * value;
            case "TBLSP":
                return 15f * density * value;
            case "CUP":
                return 236.588f * density * value;
            case "OZ":
                return 29.5735f * density * value;
            default:
                return 0;
        }
    }

    private float convertFromGrams(String to, float value, float density) {
        switch (to) {
            case "G":
                return value;
            case "K":
                return value / 1000;
            case "TSP":
                return value / (5f * density);
            case "TBLSP":
                return value / (15f * density);
            case "CUP":
                return value / (236.588f * density);
            case "OZ":
                return value / (29.5735f * density);
            default:
                return 0;
        }
    }

    @Override
    public float getTotalPrice() {
        if (getMeasure().equals("UNIT")) {
            float gramsInUnit = convertToGrams(unitMeasure, unitQuantity, density);
            return price * gramsInUnit * getQuantity();
        } else {
            return convertToGrams(getMeasure(), getQuantity(), density) * price;
        }
    }

    public void fromTotalPrice(float totalPrice) {
        if (getMeasure().equals("UNIT")) {
            float gramsInUnit = convertToGrams(unitMeasure, unitQuantity, density);
            this.price = totalPrice / (gramsInUnit * getQuantity());
        } else {
            this.price = totalPrice / convertToGrams(getMeasure(), getQuantity(), density);
        }
    }

    /**
     * Transform quantity of product to 'GRAMS" and calculate calories
     *
     * @return number of calories for whole quantity of product
     */
    @Override
    public float getTotalCalories() {
        if (getMeasure().equals("UNIT")) {
            float gramsInUnit = convertToGrams(unitMeasure, unitQuantity, density);
            return gramsInUnit * getQuantity() * (calories / 100);
        }
        return convertToGrams(getMeasure(), getQuantity(), density) * (calories / 100);
    }

    /**
     * Transform quantity of product to 'KILOGRAMS"
     *
     * @return number of kilograms for whole quantity of product
     */
    @Override
    public float getTotalWeight() {
        if (getMeasure().equals("UNIT")) {
            float gramsInUnit = convertToGrams(unitMeasure, unitQuantity, density);
            return gramsInUnit * getQuantity() / 1000;
        } else {
            return convertToGrams(getMeasure(), getQuantity(), density) / 1000;
        }
    }

    @Override
    public float getTransformedQuantity(String measure) {
        if (measure.equals("UNIT")) {
            return getQuantity();
        } else {
            float quantityInGrams = convertToGrams(measure, getQuantity(), getDensity());
            return convertFromGrams(measure, quantityInGrams, getDensity());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(float unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

    public void setUnitMeasure(String unitMeasure) {
        this.unitMeasure = unitMeasure;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }
}
