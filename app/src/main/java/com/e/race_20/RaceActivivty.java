package com.e.race_20;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

public class RaceActivivty extends AppCompatActivity implements game_menu.OnResumeListener {
    GameView gameView;
    ImageButton menu_button;
    FrameLayout fr;
    game_menu menu;

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_start);

        int level_index = (int) getIntent().getExtras().get("lvl");
        int car_index = (int) getIntent().getExtras().get("car");

        Point point = new Point();
        Display display = getWindowManager().getDefaultDisplay();//размер экрана
        display.getSize(point);

        fr = findViewById(R.id.gameView);
        game_restart_menu restart = new game_restart_menu();
        gameView = new GameView(this, point.x, point.y, level_index, car_index);
        fr.addView(gameView);

        menu = new game_menu();

        menu_button = findViewById(R.id.mha);
        menu_button.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
            FragmentTransaction ftgo = getSupportFragmentManager().beginTransaction();
            if (menu.isAdded()) {
                ftgo.show(menu);
            } else {
                ftgo.add(R.id.game_place, menu);
                ftgo.commit();
                onPause();
            }

            MutableLiveData<Boolean> isGameOver = gameView.isGameOver;
            isGameOver.observe(this, over -> {
                if (over== Boolean.TRUE) {
                    menu_button.setVisibility(View.INVISIBLE);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.game_place, restart);
                    ft.commit();
                }
            });
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            gameView.pause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    public void OnResume() {
        onResume();
    }
}
