package com.e.race_20;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class Background {
    //Класс,инициализирующий бэкграунд
    int x = 0;
    int y = 0;
    int id;
    Bitmap background;
    public static int []locations = {
            R.drawable.road1,
            R.drawable.road_elite_race,
            R.drawable.space_race,
    };

    Background(int ScreenX,int ScreenY,Resources res,int road_num){
        background = BitmapFactory.decodeResource(res,locations[road_num]);
        background = Bitmap.createScaledBitmap(background,ScreenX ,ScreenY,false);

        id = locations[road_num];
    }

    int getId(){
        return id;
    }
}

