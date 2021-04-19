package com.e.race_20;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

//Класс таблички Купить?
public class CoinFragment extends Fragment {
    public CoinFragment() {
    }

    public interface OnYesListener{
        void buyEvent(boolean choose);
    }
    private OnYesListener oyl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coin, container, false);
        view.findViewById(R.id.yes_button).setOnClickListener(menuButtonsListener);
        view.findViewById(R.id.no_button).setOnClickListener(menuButtonsListener);
       return view;
    }
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            oyl = (OnYesListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Something go wrong");
        }
    }

    private final View.OnClickListener menuButtonsListener = v -> {
        switch(v.getId()) {
            //продолжить гонку
            case R.id.yes_button: {
                FragmentTransaction gt =getFragmentManager().beginTransaction();
                      gt.hide(CoinFragment.this)
                        .commit();
                      oyl.buyEvent(true);
            }
            break;
            //рестарт
            case R.id.no_button: {
                FragmentTransaction gt =getFragmentManager().beginTransaction();
                gt.hide(CoinFragment.this)
                        .commit();

            }
            break;
        }
    };
}
