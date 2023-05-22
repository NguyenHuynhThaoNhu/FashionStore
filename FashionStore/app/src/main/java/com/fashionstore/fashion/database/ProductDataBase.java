package com.fashionstore.fashion.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.fashionstore.fashion.model.Converters;
import com.fashionstore.fashion.model.Product;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ProductDataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "product.db";
    private static ProductDataBase instance;

    public static synchronized ProductDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ProductDataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract ProductDAO productDAO();
}