package com.e.race_20;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class MainActivity extends AppCompatActivity  {

    public boolean soundOn = true;
    MediaPlayer mediaPlayer;
    AudioManager amir;
    public static  int []music = {
            R.raw.blinding_lights,
            R.raw.secrets,
            R.raw.illegal_rider,
            R.raw.in_your_eyes
    };
    Random r = new Random();

    @SuppressLint("ClickableViewAccessibility")
    @OnTouch(R.id.StartButton)
    boolean btn_startOnTouchListener(View v , MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
            }
            break;
            case MotionEvent.ACTION_UP: {
                v.clearAnimation();
                startActivity(new Intent(this, ChooseCarAndLocationActivity.class));
            }
            break;
        }
        return false;
    }

    @OnTouch(R.id.OptionsButton)
    boolean btn_optionOnTouchListener(View v , MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
            }
            break;
            case MotionEvent.ACTION_UP: {
                Toast.makeText(getApplicationContext(),
                        "Для старта зажмите колёсико,а затем отпустите\n" +
                                "Для отключения музыки нажмите на колонку\n"+
                             "НАЖАТИЕ НА МАШИНКУ МЕНЯЕТ ТРЕК",
                        Toast.LENGTH_LONG).show();
            }
            break;
        }
        return false;
    }

    @OnClick(R.id.SoundButton)
    void btn_soundOnClickListener(View v){
                if(soundOn)
                {
                    v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
                    v.setBackgroundResource(R.drawable.button_sound_off);
                    soundOn=false;
                    mediaPlayer.pause();
                }
                else
                {
                    v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
                    v.setBackgroundResource(R.drawable.button_sound_on);
                    mediaPlayer.start();
                    soundOn = true;
                }
    }

    @OnClick(R.id.animation_car)
    void car_OnClickListener(View v)
    {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.trans_left));
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        int t = r.nextInt(4);
        mediaPlayer = MediaPlayer.create(this, music[t]);
        if (soundOn) {
            mediaPlayer.start();
        }
        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.reset());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
            amir = (AudioManager) getSystemService(AUDIO_SERVICE);
            int t = r.nextInt(4);
            mediaPlayer = MediaPlayer.create(this, music[t]);
            if (soundOn) {
              mediaPlayer.start();
              mediaPlayer.setLooping(true);
            }
            mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.reset());
    }
}


