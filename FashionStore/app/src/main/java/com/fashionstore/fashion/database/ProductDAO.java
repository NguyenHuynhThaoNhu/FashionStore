package com.fashionstore.fashion.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.fashionstore.fashion.model.Product;

import java.util.List;

@Dao
public interface ProductDAO {

    @Insert
    void insertFood(Product product);

    @Query("SELECT * FROM product")
    List<Product> list();

    @Query("SELECT * FROM product WHERE id=:id")
    List<Product> checkProductInCart(int id);

    @Delete
    void delete(Product product);

    @Update
    void update(Product product);

    @Query("DELETE from product")
    void deleteAllFood();
}
