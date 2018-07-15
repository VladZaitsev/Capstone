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
    //Density of ingredient for wright calculating
    private float density;

    //This block of information is valid if measure of ingredient is "UNIT"
    private float oneUnitPrice;
    private float oneUnitQuantity;
    private String oneUnitMeasure;

    private float price;
    private String currency;

    private int expiration;

    public CatalogEntity(double quantity, String measure, String ingredient) {
        super(quantity, measure, ingredient);
    }


    @Override
    public float getPrice() {
        return 0;
    }

    @Override
    public float getCalories() {
        return 0;
    }

    @Override
    public float getWeight() {
        return 0;
    }

    @Override
    public float getQuantity(String measure) {

        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public float getOneUnitPrice() {
        return oneUnitPrice;
    }

    public void setOneUnitPrice(float oneUnitPrice) {
        this.oneUnitPrice = oneUnitPrice;
    }

    public float getOneUnitQuantity() {
        return oneUnitQuantity;
    }

    public void setOneUnitQuantity(float oneUnitQuantity) {
        this.oneUnitQuantity = oneUnitQuantity;
    }

    public String getOneUnitMeasure() {
        return oneUnitMeasure;
    }

    public void setOneUnitMeasure(String oneUnitMeasure) {
        this.oneUnitMeasure = oneUnitMeasure;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }
}
