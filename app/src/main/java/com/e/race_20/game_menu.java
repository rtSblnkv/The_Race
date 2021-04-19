package com.e.race_20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;


public class  game_menu extends Fragment {
    private FragmentManager fm;

    public interface OnResumeListener{
         void OnResume();
    }

    private OnResumeListener orl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
           View view = inflater.inflate(R.layout.fragment_game_menu, container, false);
        view.findViewById(R.id.button_menu_resume).setOnClickListener(menuButtonsListener);
        view.findViewById(R.id.button_menu_restart).setOnClickListener(menuButtonsListener);
        view.findViewById(R.id.button_menu_exit).setOnClickListener(menuButtonsListener);
        fm = getActivity().getSupportFragmentManager();
        return view;
    }
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            orl = (OnResumeListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Something go wrong");
        }
    }

    private final View.OnClickListener menuButtonsListener = v -> {
        v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale));
        switch(v.getId())
        {
            //продолжить гонку
            case R.id.button_menu_resume:
            {
                      fm.beginTransaction()
                        .hide(game_menu.this)
                        .commit();
                orl.OnResume();
            }
            break;
            //рестарт
            case R.id.button_menu_restart:
            {
                startActivity(new Intent(getActivity(), ChooseCarAndLocationActivity.class));
                getActivity().finish();
            }
            break;
            //выход в меню
            case R.id.button_menu_exit:
            {
                getActivity().finish();
            }
            break;
        }
    };
}
