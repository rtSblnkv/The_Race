package com.e.race_20;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

class NPC {

    int
               x,
               y,
               width,
               height;
    double speed;
    private int
               screen_size,
               pos;
    private Bitmap npc;

     private static int []npcID = {
            R.drawable.vhcl_npc_auto1,
            R.drawable.vhcl_npc_auto2,
            R.drawable.vhcl_npc_auto,
            R.drawable.taxi,
    };

     private static int[]spaceNpc = {
            R.drawable.space_npc,
            R.drawable.space_npc2,
            R.drawable.spacer1,
            R.drawable.space_scout,
    };

    NPC(Resources res, int level_num, int pos, int screen_size) {
        if(level_num % 3 != 2) {
            npc = BitmapFactory.decodeResource(res,npcID[pos]);
        }
        else{
            npc = (BitmapFactory.decodeResource(res, spaceNpc[pos]));
        }
        width = npc.getWidth();
        height = npc.getHeight();
        width /= 1.7;
        height /= 1.7;

        npc = Bitmap.createScaledBitmap(npc, width, height, false);
        this.screen_size = screen_size;
        this.pos = pos;

        speed = 15 + pos * 2;
        x = x_update();
        y = -10 * height - (400 * (pos + 1)) * (level_num + 1);
    }

    private int x_update()
    {
        Random r = new Random();
        x = r.nextInt(5) *  pos * screen_size/13;
        if(x > screen_size - width - 50){
            x = screen_size - width - 20;
        }
        if(x < 20){
            x = 20;
        }
        return x;
    }

    Bitmap getNpc () {
        return npc;
    }

    Rect getCollision(){return new Rect(x - 10 , y - 10,x + width - 20, y + height - 10);}
}
