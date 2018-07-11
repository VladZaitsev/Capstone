package com.baikaleg.v3.cookingaid.data.model;

public class Product {
    private String uuid;
    private String name;
    private String measure;
    private String unitWeight;
    private float density;

    //Optional information
    private int isPrice;
    private int isExpiration;
    private int isCalories;
    private float price;
    private int expiration;
    private float calories;

    //Information about single unit of product
    private float oneUnitPrice;
    private float oneUnitQuantity;
    private String oneUnitMeasure;

    private String currency;
    private String purchaseDate;
    private String shoppingListName;
    private int isBought;
    private float currentQuantity;
    private float initialQuantity;
}
