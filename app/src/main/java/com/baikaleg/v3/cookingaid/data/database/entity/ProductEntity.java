package com.baikaleg.v3.cookingaid.data.database.entity;

import android.arch.persistence.room.Entity;

import com.baikaleg.v3.cookingaid.data.database.entity.CatalogEntity;

import java.util.Date;

@Entity(tableName = "product")
public class ProductEntity extends CatalogEntity {

    private Date purchaseDate;
    //private String shoppingListName;
    //private float initialQuantity;
    private int productState;//1 - not bought, not selected; 2 - selected; 3 - bought.

    public ProductEntity(float quantity, String measure, String ingredient) {
        super(quantity, measure, ingredient);
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getProductState() {
        return productState;
    }

    public void setProductState(int productState) {
        this.productState = productState;
    }
}
