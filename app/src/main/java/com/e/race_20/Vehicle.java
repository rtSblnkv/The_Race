package com.e.race_20;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

class Vehicle  {
        int x,
            y,
            width,
                id,
            max_speed,
            power,
            turn_speed,
            height = 0;
        double  current_speed;

    private Bitmap user_car;

    public static int [][] vhcl_id = {
            {
                    R.drawable.vhcl_tractor,
                    R.drawable.vhcl_boevaya_klassika,
                    R.drawable.vhcl_boevaya_klassika1,
                    R.drawable.vhcl_boevaya_klassika2,
                    R.drawable.vhcl_boevaya_klassika3,
                    R.drawable.vhcl_boevaya_klassika4,
                    R.drawable.vhcl_vesta,
                    R.drawable.vhcl_kalina,
                    R.drawable.vhcl_granta
            },
            {
                R.drawable.taxi,
                R.drawable.vhclc_croossover,
                R.drawable.vhcl_mazda,
                R.drawable.vhcl_audi,
                R.drawable.vhcl_bmw,
                R.drawable.vhcl_boss ,
            },
            {
                    R.drawable.spacer1,
                    R.drawable.spacer2,
                    R.drawable.spacer3,
                    R.drawable.space_scout,
            }
    };

     Vehicle(Resources res,int screenX,int screenY, int car_ind,int level_num)
    {
        id = vhcl_id[level_num][car_ind];
        user_car = BitmapFactory.decodeResource(res, id);
        width = user_car.getWidth();
        height = user_car.getHeight();
        width /= 1.9;
        height /= 1.9;

        user_car = Bitmap.createScaledBitmap(user_car, width, height, false);

        current_speed = 2;
        power = (car_ind + 1) + 4;
        max_speed = (level_num + 2) * power;
        turn_speed = max_speed - (level_num + power);
        y = screenY - height - 20;
        x = screenX/2;
    }
    int getVehicleId(){
         return id;
    }

    Bitmap getCar()
    {
        return user_car;
    }

    Rect getCollisionShape () {
        return new Rect(x - 10, y - 10,x + width - 10, y + height - 10);
    }
}



