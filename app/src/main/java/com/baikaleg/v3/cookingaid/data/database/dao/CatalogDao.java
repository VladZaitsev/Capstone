package com.baikaleg.v3.cookingaid.data.database.dao;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import com.baikaleg.v3.cookingaid.data.database.entity.CatalogEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CatalogDao {
    @Query("SELECT * FROM catalog")
    Single<List<CatalogEntity>> loadAllProducts();

    @Insert
    void insertProduct(CatalogEntity productEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProduct(CatalogEntity productEntry);

    @Query("SELECT * FROM catalog WHERE ingredient = :name")
    Single<CatalogEntity> loadProductByName(String name);

    @RawQuery(observedEntities = CatalogEntity.class)
    Flowable<List<CatalogEntity>> loadProductsByQuery(SupportSQLiteQuery query);
}
