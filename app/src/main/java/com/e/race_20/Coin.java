package com.e.race_20;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;
//класс,инициализирующий монетки
public class Coin {
         int
            x,
            y,
            width,
            height,
            screen_size;
    private Bitmap coin;
    //Конструктор,создание изображения,
    public Coin(Resources res,int screen_size){
        this.screen_size = screen_size;
            coin = BitmapFactory.decodeResource(res, R.drawable.coin);
            //получение размеров
            width = coin.getWidth()/3;
            height = coin.getHeight()/3;
            coin =  Bitmap.createScaledBitmap(coin, width, height, false);

            //Задание начальных координат
            x = x_update();
            Random rand = new Random();
            y = -100 * (rand.nextInt(40) + 1);
    }

     int x_update()
    {
        Random r = new Random();
        x = r.nextInt(13)  * screen_size/13;
        if(x > screen_size - width - 50){
            x = screen_size - width - 20;
        }
        if(x < 20){
            x = 20;
        }
        return x;
    }
    //получение изображения монетки
    Bitmap getCoin () {
        return coin;
    }

    //территория монетки
    Rect getCollision(){return new Rect(x , y ,x + width , y + height);}
}
