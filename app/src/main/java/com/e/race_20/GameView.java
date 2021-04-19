package com.e.race_20;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import androidx.lifecycle.MutableLiveData;

import java.util.Random;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements Runnable{
    public static float maxX,maxY;
    public static float screenX, screenY = 0;//размер экрана
    public static boolean isLeftPressed,isRightPressed = false;
    int score = 0,
        coins = 0,
        n = 0;
    public boolean gameRunning = true;
    GameDb gc;
    private RaceActivivty race;//активити
    private Vehicle vehicle;//пользователь
    private NPC[] npc;//массив других машин
    private Coin[] coins_pic;
    private Thread thread; // поток отрисовки
    private Paint paint; // отрисовка
    private Background back1, back2;
    GameViewModel gvm;
    MutableLiveData<Boolean> isGameOver;

//Класс,описывающий отрисовку элементов в гонке
    public GameView(RaceActivivty race, int screenX, int screenY, int level_num, int vhcl_num) {
        super(race);
        this.race = race;
        gvm = new GameViewModel(race.getApplication());

        initBack( screenX, screenY, level_num,vhcl_num);
        vehicle = initVehicle(vhcl_num,level_num);
        coins_pic = initCoins();
        npc = initNpc(level_num);
        setTextParameter();

        thread = new Thread(this);
        thread.start();
        isGameOver = new MutableLiveData<>();
        isGameOver.postValue(Boolean.FALSE);


    }

   ///движок
    @Override
    public void run() {
        while (gameRunning) {
            update();
            draw();
            sleep();
        }
    }

    //Задание параметров текста
    private void setTextParameter() {
        paint = new Paint();
        paint.setTextSize(48);
        paint.setColor(Color.CYAN);
    }

    //Инициализация дороги
    private void initBack( int screenX, int screenY, int level_num, int vhcl_num) {
        GameView.screenX = screenX;
        GameView.screenY = screenY;

        maxX = 1920f / screenX;
        maxY = 1080f / screenY;

        back1 = new Background(screenX,screenY, getResources(),level_num);
        back2 = new Background(screenX, screenY, getResources(),level_num);
        back2.y = -back2.background.getHeight();
    }

    //инициализация машинки пользователя
    private Vehicle initVehicle(int vhcl_num,int level_num){
        return new Vehicle(getResources(),(int)screenX,(int)screenY,vhcl_num,level_num);
    }

    /*инициализация монеток */
    private Coin[] initCoins() {
        coins_pic = new Coin[5];
        for (int i = 0;i < 5;i++) {
            Coin coin  = new Coin(getResources(),(int)screenX);
            coins_pic[i] = coin;
        }
        return coins_pic;
    }

    /*инициализация машинок */
    private NPC[] initNpc(int level){
        npc = new NPC[4];
        for (int i = 0;i < 4;i++) {
            NPC Npc = new NPC(getResources(),level,i,(int)screenX);
            npc[i] = Npc;
        }
        return npc;
    }

     //осуществление перемещения машины
    private int  checkTurns() {
        if (isLeftPressed && vehicle.x > 20)
            vehicle.x -= vehicle.turn_speed + maxX;
        else if (isRightPressed && vehicle.x < screenX - vehicle.width - 20)
            vehicle.x += vehicle.turn_speed + maxX;
        return vehicle.x;
    }

    //обработка скорости пользователя
    private int speedUpdate() {
        if(vehicle.current_speed < vehicle.max_speed) {
            return(vehicle.power + score)/3;
        }
          return vehicle.max_speed;
    }

    //движение дороги
    private void backGroundUpdate() {
        back1.y +=  2*(vehicle.current_speed  + 5);
        back2.y +=  2*(vehicle.current_speed  + 5);

        if (back1.y + back1.background.getHeight() > 2*screenY  ) {
            back1.y = 0;
            score++;
        }

        if (back2.y + back2.background.getHeight() >  screenY) {
            back2.y = -back2.background.getHeight() ;
            score++;
        }
    }

    //Обработка встречных машинок,их координаты и скорости
    private void NPCupdate() {
        int i = 0;
        for (NPC np : npc) {
            Random r = new Random();
            np.speed = r.nextInt((int) vehicle.current_speed + 1) *r.nextInt(10);
            np.y += np.speed;
            if (np.y > screenY + np.height) {
                np.y = -(np.height + r.nextInt((int) vehicle.current_speed) + (350 - 2 * n) * (i + 1));
                int c = (int) screenX / npc.length;//Коэффициент
                np.x = ((i + n) % 4) * c + r.nextInt(c);
                checkNPCinScreen(np);
                n++;
                i++;
            }
            if(withNpcCollision(np)) {
                isGameOver.postValue(Boolean.TRUE);
                return;
            }
        }
    }

    //Отслеживает выходы за пределы экрана
    private void checkNPCinScreen(NPC np){
        if (np.x > screenX - np.width - 50) {
            np.x = (int) screenX - np.width - 20;
        }
        if (np.x < 20) {
            np.x = 20;
        }
    }

    //Отслеживает выходы за пределы экрана монеток
    private boolean withNpcCollision(NPC np) {
        return Rect.intersects(np.getCollision(), vehicle.getCollisionShape());
    }

    //Обработка массива монеток
    private void  coinsUpdate(){
        for (Coin coin:coins_pic)
        {
            coin.y += 2 * (vehicle.current_speed + 5);
            if(coin.y > screenY + coin.height){
                coinCoordinatesUpdate(coin);
            }
            if(coinCollision(coin))
            {
                coins++;
                coinCoordinatesUpdate(coin);
            }
        }
    }
    //обработка координат монеток
    private void coinCoordinatesUpdate(Coin coin)
    {
        coin.y = -(2 * (int)screenY);
        coin.x = coin.x_update();
        checkCoinInScreen(coin);
    }
    //Отслеживает выходы за пределы экрана монеток
    private void checkCoinInScreen (Coin coin)
    {
        if (coin.x > screenX - coin.width - 50) {
            coin.x = (int) screenX - coin.width - 20;
        }
        if (coin.x < 20) {
            coin.x = 20;
        }
    }

    //Отслеживает отлов монетки
    private boolean coinCollision(Coin coin)
    {
        return Rect.intersects(coin.getCollision(),vehicle.getCollisionShape());
    }

    //создание движения спрайтов
    private void update() {
        vehicle.current_speed = speedUpdate();
        backGroundUpdate();
        vehicle.x = checkTurns();
        NPCupdate();
        coinsUpdate();
    }

    //отрисвока спрайтов(background,npc,vehicle.coins)
    private void draw() {
        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(back1.background, back1.x, back1.y, paint);
            canvas.drawBitmap(back2.background, back2.x, back2.y, paint);
            canvas.drawBitmap(vehicle.getCar(), vehicle.x, vehicle.y, paint);

            for(NPC np:npc) {
                canvas.drawBitmap(np.getNpc(), np.x, np.y, paint);
            }
            for (Coin cn:coins_pic)
            {
                canvas.drawBitmap(cn.getCoin(), cn.x, cn.y, paint);
            }

            drawInfo(canvas);

            if (isGameOver.getValue() == Boolean.TRUE) {
                gameRunning = false;
                drawGameOver(canvas);
                getHolder().unlockCanvasAndPost(canvas);

                upCash();
                waitBeforeMenu ();
                race.startActivity(new Intent(race,ChooseCarAndLocationActivity.class));
                race.finish();
                return;
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //отрисовка информации о скорости,монетах и очках
    private void drawInfo(Canvas canvas){
        canvas.drawText("Score: " + score , screenX  - 250 , 50, paint);
        canvas.drawText("Coins: " + coins , screenX  - 250 , 100, paint);
        canvas.drawText(vehicle.current_speed + " км/ч",20 ,1500, paint);
    }

    //появление значка "игра окончена",отображение результатов
    //И ПРОЦЕДУРА СОХРАНЕНИЯ ТОЖК ЗДЕСЬ РЕАЛИЗУЕТСЯ У МЕНЯ
    private void drawGameOver(Canvas canvas)
    {
        Bitmap label= BitmapFactory.decodeResource(getResources(),R.drawable.lable_game_over);
        label = Bitmap.createScaledBitmap(label,(int)screenX,(int)screenY/5,false);
        canvas.drawBitmap(label,0,screenY/3,paint);

        paint.setTextSize(82);
        canvas.drawText( "Scores: " + score , screenX/2 - 200, 950, paint);
        canvas.drawText("Coins: " + coins , screenX/2 - 200 , 1050, paint);

        if(saveIfHighScore()) {
            Bitmap new_record = BitmapFactory.decodeResource(getResources(),R.drawable.new_record);
            new_record = Bitmap.createScaledBitmap(new_record,(int)screenX/6,(int)screenY/7,false);
            canvas.drawBitmap(new_record,screenX/2,screenY/2,paint);
        }
    }
    //подождать перед выходом
    private void waitBeforeMenu() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //сохранения результата,если рекорд
    private boolean saveIfHighScore() {
        gc = gvm.getElementById(vehicle.getVehicleId());
        if(gc.record < score) {
            gvm.upgradeRec(vehicle.getVehicleId(), score);
        }
        gc = gvm.getElementById(back1.getId());
        if(gc.record < score) {
            gvm.upgradeRec(back1.getId(), score);
            return true;
        }
        return false;
    }
    //Обновление денежного запаса игрока
    private void upCash()
    {
        gc = gvm.getElementById(-1);
        coins += gc.price;
        gvm.upgradePrice(coins);
    }

    //возобновление игрового процесса
    public void resume(){
        gameRunning = true;
        thread = new Thread(this);
        thread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //остановка игрового процесса
    public void pause() throws InterruptedException {
        gameRunning = false;
        thread.join();
    }

    //обработчик касаний,перемещение vehicle по оси y
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) {
                    isLeftPressed = true;
                }
                if (event.getX() > screenX / 2)
                    isRightPressed = true;
                break;
            case MotionEvent.ACTION_UP:
                isLeftPressed = false;
                isRightPressed = false;
                break;
        }
        return true;
    }
}
