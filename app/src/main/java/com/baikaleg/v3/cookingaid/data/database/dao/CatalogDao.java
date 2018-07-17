package com.baikaleg.v3.cookingaid.data.database.dao;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import com.baikaleg.v3.cookingaid.data.database.entity.product.CatalogEntity;

import java.util.List;

@Dao
public interface CatalogDao {
    @Query("SELECT * FROM catalog")
    List<CatalogEntity> loadAllProducts();

    @Insert
    void insertProduct(CatalogEntity productEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProduct(CatalogEntity productEntry);

    @Delete
    void deleteProduct(CatalogEntity productEntry);

    @Query("SELECT * FROM catalog WHERE id = :id")
    CatalogEntity loadProductById(int id);

    @RawQuery(observedEntities = CatalogEntity.class)
    List<CatalogEntity> loadProductsByQuery(SupportSQLiteQuery query);
}
