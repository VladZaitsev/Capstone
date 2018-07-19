package com.baikaleg.v3.cookingaid.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;
import com.baikaleg.v3.cookingaid.data.database.entity.product.ProductEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product")
    List<ProductEntity> loadAllProducts();

    @Insert
    void insertProduct(ProductEntity productEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProduct(ProductEntity productEntry);

    @Delete
    void deleteProduct(ProductEntity productEntry);

    @Query("SELECT * FROM product WHERE id = :id")
    Flowable<ProductEntity> loadProductById(int id);

}
