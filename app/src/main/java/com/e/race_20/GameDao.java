package com.e.race_20;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

    @Dao
    public interface GameDao {

        @Query("SELECT * FROM info WHERE id = :id")
        GameDb getById(int id);

        @Query("UPDATE info SET price =:price WHERE id = -1")
        void updatePrice(int price);

        @Query("UPDATE info SET record =:record WHERE id = :id")
        void updateRecord(int id,int record);

        @Query("UPDATE info SET is_bought =:is_bought WHERE id = :id")
        void updateInfo(int id,boolean is_bought);

        @Insert(onConflict = OnConflictStrategy.IGNORE )
        void insert(GameDb go);
    }

