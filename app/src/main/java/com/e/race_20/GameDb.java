package com.e.race_20;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "info")
public class GameDb {
    @PrimaryKey
    public long id;

    public String name;
    public int level;
    public int record;
    public int price;
    public boolean is_bought;
    // инициализация
    public GameDb(String name,
                  long id,
                  int level,
                  int record,
                  int price,
                  boolean is_bought)
    {
        this.name = name;
        this.id = id;
        this.level = level;
        this.record = record;
        this.price = price;
        this.is_bought = is_bought;
    }
}
