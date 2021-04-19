package com.e.race_20;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

class GameViewModel extends AndroidViewModel {

    private GameRepository gameRepository;

    public GameViewModel(@NonNull Application application) {
        super(application);
        gameRepository = new GameRepository(application);
    }

    void upgradeRec (int id,int level){gameRepository.upgradeRec(id,level);}

    void upgradeInfo(int id,boolean is_bought){gameRepository.upgradeInfo(id,is_bought);}

    void upgradePrice (int price){ gameRepository.upgradePrice(price);}

    GameDb getElementById(int id)
    {
        return gameRepository.get_spriteById(id);
    }
}
