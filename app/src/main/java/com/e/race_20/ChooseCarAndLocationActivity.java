package com.e.race_20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import com.e.race_20.databinding.ActivityGarageBinding;

import static android.view.View.INVISIBLE;

public class ChooseCarAndLocationActivity extends AppCompatActivity implements View.OnClickListener, ViewSwitcher.ViewFactory, CoinFragment.OnYesListener {
    private ImageSwitcher choosecar;
    private ImageSwitcher chooseroad;

    int level = 0;
    int position_vhcl = 0;
    private static int[][] choose_vhcl = Vehicle.vhcl_id;
    private static int[] choose_level =  Background.locations;
    private boolean can_buy;

    //slide the roads
    ImageButton prev;//предыдущая локация
    ImageButton nex;//следующая локация

    ImageButton prev_car;//пердыдущая машина
    ImageButton nex_car;//следующая машина

    ImageButton next_choose;//переход к выбору авто
    ImageButton start;//начало гонки
    ImageButton buy;//купить элемент

    CoinFragment coin_menu;//купить?
    private GameViewModel gvm ;//бд

    TextView coinsInfo;

    ActivityGarageBinding binding;

    boolean check = true;
    int price ;

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
        switch (v.getId()) {
            case R.id.nextcar: {
                position_vhcl++;
                if (position_vhcl > choose_vhcl.length - 1) position_vhcl = 0;
                choosecar.setImageResource(choose_vhcl[level][position_vhcl]);
                getInformation(choose_vhcl[level][position_vhcl]);
            }
            break;
            case R.id.previouscar: {
                position_vhcl--;
                if (position_vhcl < 0) position_vhcl = choose_vhcl.length - 1;
                choosecar.setImageResource(choose_vhcl[level][position_vhcl]);
                getInformation(choose_vhcl[level][position_vhcl]);
            }
            break;
            case R.id.button_start: {
                Intent intent = new Intent(this, RaceActivivty.class);
                intent.putExtra("car", position_vhcl);
                intent.putExtra("lvl", level);
                startActivity(intent);
                this.finish();
            }
            break;
            case R.id.button_buy:{
                FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                if(coin_menu.isAdded()){
                    fm.show(coin_menu);
                    fm.commit();
                }
                else
                {
                    fm.add(R.id.buy_place,coin_menu);
                    fm.commit();
                }
            }
            break;

            case R.id.nextroad: {
                level++;
                if (level > choose_level.length - 1) level = 0;
                chooseroad.setImageResource(choose_level[level]);
                getInformation(choose_level[level]);
            }
            break;
            case R.id.previousroad: {
                level--;
                if (level < 0) level = choose_level.length - 1;
                chooseroad.setImageResource(choose_level[level]);
                getInformation(choose_level[level]);
            }
            break;
            case R.id.button_next: {
                choosecar.setImageResource(choose_vhcl[level][position_vhcl]);
                getInformation(choose_vhcl[level][position_vhcl]);

                start.setVisibility(View.VISIBLE);
                start.setEnabled(true);
                chooseRoadDelete();
            }
            break;
        }
    }

    private void chooseRoadDelete()
    {
        chooseroad.setVisibility(INVISIBLE);
        chooseroad.setEnabled(false);

        nex.setVisibility(INVISIBLE);
        nex.setEnabled(false);

        prev.setEnabled(false);
        prev.setVisibility(INVISIBLE);

        next_choose.setVisibility(View.INVISIBLE);
        next_choose.setEnabled(false);
    }

    private void initializeButtons()
    {
        prev = findViewById(R.id.previousroad);
        prev.setOnClickListener(this);

        nex = findViewById(R.id.nextroad);
        nex.setOnClickListener(this);

        prev_car = findViewById(R.id.previouscar);
        prev_car.setOnClickListener(this);

        nex_car = findViewById(R.id.nextcar);
        nex_car.setOnClickListener(this);

        next_choose = findViewById(R.id.button_next);
        next_choose.setOnClickListener(this);

        start = findViewById(R.id.button_start);
        start.setOnClickListener(this);

        buy = findViewById(R.id.button_buy);
        buy.setOnClickListener(this);
        buy.setEnabled(false);

    }
    private void InitializeImageSwitchers()
    {
        chooseroad = findViewById(R.id.IsBack);
        chooseroad.setFactory(this);
        chooseroad.setImageResource(choose_level[level]);

        choosecar = findViewById(R.id.IsCar);
        choosecar.setFactory(this);
    }

    private void getInformation(int id) {
            GameDb gc = gvm.getElementById(id);
            binding.setGameDB(gc);
            check = gc.is_bought;
            price = gc.price;
            can_buy = getCoins(-1) > price;
            if (check && chooseroad.isEnabled())
                next_choose.setVisibility(View.VISIBLE);
            else next_choose.setVisibility(View.INVISIBLE);
            next_choose.setEnabled(check && chooseroad.isEnabled());

            if (check && !chooseroad.isEnabled())
                start.setVisibility(View.VISIBLE);
            else start.setVisibility(View.INVISIBLE);
            start.setEnabled(check && !chooseroad.isEnabled());

            buy.setEnabled(!check);
            if (check) buy.setVisibility(View.INVISIBLE);
            else buy.setVisibility(View.VISIBLE);
    }

    private int getCoins(int id)
    {
        return gvm.getElementById(id).price;
    }

    private void setFullscreen(){
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_garage);
        gvm = new GameViewModel(getApplication());

        setFullscreen();
        InitializeImageSwitchers();
        initializeButtons();

        coinsInfo = findViewById(R.id.coin_num);
        coin_menu = new CoinFragment();

        getInformation(choose_level[level]);
        coinsInfo.setText("" + getCoins(-1));

    }
    @Override
    public void buyEvent(boolean bool){
        if(can_buy)
        {
            int id = chooseroad.isEnabled() ? choose_level[level]:choose_vhcl[level][position_vhcl];
            int cash = getCoins(-1);
            cash -= getCoins(id);
            gvm.upgradePrice(cash);
            gvm.upgradeInfo(id,true);
            coinsInfo.setText("" + cash);
            getInformation(id);
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Недостаточно средств для покупки",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View makeView() {
        return new ImageView(this);
    }
}
