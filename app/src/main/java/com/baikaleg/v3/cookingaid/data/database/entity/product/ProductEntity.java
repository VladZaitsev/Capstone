package com.baikaleg.v3.cookingaid.data.database.entity.product;

import android.arch.persistence.room.Entity;

import java.util.Date;
@Entity(tableName = "product")
public class ProductEntity extends CatalogEntity {

    private Date purchaseDate;
    private String shoppingListName;
    private float initialQuantity;
    private int state;//1 - not bought, not selected; 2 - selected; 3 - bought.

    public ProductEntity(float quantity, String measure, String ingredient) {
        super(quantity, measure, ingredient);
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getShoppingListName() {
        return shoppingListName;
    }

    public void setShoppingListName(String shoppingListName) {
        this.shoppingListName = shoppingListName;
    }

    public float getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(float initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
