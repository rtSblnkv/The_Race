package com.e.race_20;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;

import java.util.Objects;
//Оно работает,но у меня не получается запустить его в игровом потоке
//Фрагмент появляется при game over
public class game_restart_menu extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_game_restart_menu, container, false);
        Objects.requireNonNull(view).findViewById(R.id.button_gameover_restart).setOnClickListener(v -> {

            v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale));
            startActivity(new Intent(getActivity(), ChooseCarAndLocationActivity.class));
            Objects.requireNonNull(getActivity()).finish();

        });
        return view;
    }
}
