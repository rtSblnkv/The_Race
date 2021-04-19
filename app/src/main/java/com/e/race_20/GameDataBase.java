package com.e.race_20;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities ={GameDb.class},version = 1,exportSchema = false)
public abstract class GameDataBase extends RoomDatabase {
    public abstract GameDao gamedao();

    private static volatile GameDataBase INSTANCE;
    private static final int  NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseGameExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static  GameDataBase getGameDataBase(final Context context){
        if(INSTANCE == null){
            synchronized(GameDataBase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GameDataBase.class,"info")
                            .allowMainThreadQueries()
                            .addCallback(DBcallBack)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback DBcallBack = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
          createDB();
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            createDB();
        };
    };

    private static void createDB() {
        databaseGameExecutor.execute(()-> {
            GameDao gamedao = INSTANCE.gamedao();
            //о монетках
            GameDb coin = new GameDb(
                    "Coin",
                    -1,
                    -1,
                    -1,
                    10000000,
                    false
            );
            gamedao.insert(coin);

            for(int level = 0;level <3;level++)
            {
                //о машиинах
                for(int i = 0;i < Vehicle.vhcl_id[level].length;i++)
                {
                    String name = "Vehicle "+ (i + 1);
                    int price = ((level + 1) * (i + 1)) * 3000;
                    boolean is_bought = (i == 0);
                    GameDb levels = new GameDb(
                            name,
                            Vehicle.vhcl_id[level][i],
                            level,
                            0,
                            price,
                            is_bought
                    );
                    gamedao.insert(levels);
                }
                //о локациях
                String n ="Location "+ (level + 1);
                int rd = (level + 1) * (level + 1 ) * 12;
                int pr = rd * 3080;
                boolean isBought = (level == 0);
                GameDb gd = new GameDb(
                        n,
                        Background.locations[level],
                        level + 4,
                        rd,
                        pr,
                        isBought
                );
                gamedao.insert(gd);
            }
        });

    }
}
