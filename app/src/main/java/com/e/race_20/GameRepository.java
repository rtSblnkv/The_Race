package com.e.race_20;

import android.app.Application;

import static com.e.race_20.GameDataBase.databaseGameExecutor;

class GameRepository {
    private GameDao GR_gamedao;

    GameRepository(Application app){
        GameDataBase gb = GameDataBase.getGameDataBase(app);
        GR_gamedao = gb.gamedao();
    }

    GameDb get_spriteById(int id){
          return GR_gamedao.getById(id);
    }

    void upgradeRec (int id,int level) {
        databaseGameExecutor.execute(() ->
        {
            GR_gamedao.updateRecord(id, level);
        });
    }

    void upgradeInfo(int id,boolean is_bought){
        databaseGameExecutor.execute(() ->
        {
            GR_gamedao.updateInfo(id, is_bought);
        });
    }

    void upgradePrice (int price) {
        databaseGameExecutor.execute(() ->
        {
            GR_gamedao.updatePrice(price);
        });
    }
}
