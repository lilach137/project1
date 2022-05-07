package com.example.project1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project1.objects.Actor;
import com.example.project1.objects.GameManager;
import com.example.project1.R;
import com.example.project1.objects.GpsTracker;
import com.example.project1.objects.MSPV;
import com.example.project1.objects.MyDB;
import com.example.project1.objects.MySensors;
import com.example.project1.objects.Record;
import com.example.project1.objects.SoundsUtils;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Main_Activity extends AppCompatActivity {


    private ImageView[] game_IMG_hearts;
    private Button[] game_BTN_arrows;
    private ImageView [][]board;
    private TextView game_LBL_score;
    private GameManager gameManager;
    private MySensors mySensors;
    private ImageView game_IMG_background;

    private int[] icons;
    private boolean restart = true;

    //actors
    private Actor wolf;
    private Actor redHood;
    private Actor bomb;

    //sensors

    private SensorManager sensorManager;
    private Sensor accSensor;
    private SensorEventListener sensorEventListener;


    //timer
    private enum TIMER_STATUS {
        OFF,
        RUNNING,
        PAUSE
    }
    private Timer timer = new Timer();
    private int delay=1000;
    private TIMER_STATUS timerStatus = TIMER_STATUS.OFF;
    private int counter = 0;

    //game info from login
    public static final String GAME_TYPE = "GAME_TYPE";
    public static final String GAME_PLAYER = "GAME_PLAYER";
    private int speed;
    private String playerName;
    private int gameType;

    private GpsTracker gpsService;
    private final MyDB myDB = MyDB.initMyDB();
    private SoundsUtils soundsUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        soundsUtils = new SoundsUtils();
        soundsUtils.setMpAndPlay((ContextWrapper) getApplicationContext(),R.raw.background_music);
        findViews();
        getDataFromLogin();

        gameManager = new GameManager();
        wolf = new Actor(gameManager.ROWS-1, gameManager.COLS-2, Actor.STATIC , false);
        redHood = new Actor(0, gameManager.COLS-1, Actor.STATIC , true);
        bomb = new Actor(2,1, Actor.STATIC,true);
        updateUI();
        startGame();

    }

    private void updateUI() {
        for (int i = 0; i < game_IMG_hearts.length; i++) {
            game_IMG_hearts[i].setVisibility(gameManager.getLives() > i ? View.VISIBLE : View.INVISIBLE);
        }
        if(gameManager.isDead())
            gameOver();
    }

    private void findViews() {
        game_LBL_score=findViewById(R.id.game_LBL_score);
        game_IMG_background = findViewById(R.id.game_IMG_background);

        game_IMG_hearts = new ImageView[] {
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };

        icons = new int[] {
                R.drawable.ic_wolf,
                R.drawable.ic_redhood,
                R.drawable.ic_dead,
                R.drawable.ic_bomb
        };


        board = new ImageView[][]{
                {findViewById(R.id.game_IMG_cell00),findViewById(R.id.game_IMG_cell01),findViewById(R.id.game_IMG_cell02),findViewById(R.id.game_IMG_cell03),findViewById(R.id.game_IMG_cell04)},
                {findViewById(R.id.game_IMG_cell10),findViewById(R.id.game_IMG_cell11),findViewById(R.id.game_IMG_cell12),findViewById(R.id.game_IMG_cell13),findViewById(R.id.game_IMG_cell14)},
                {findViewById(R.id.game_IMG_cell20),findViewById(R.id.game_IMG_cell21),findViewById(R.id.game_IMG_cell22),findViewById(R.id.game_IMG_cell23),findViewById(R.id.game_IMG_cell24)},
                {findViewById(R.id.game_IMG_cell30),findViewById(R.id.game_IMG_cell31), findViewById(R.id.game_IMG_cell32),findViewById(R.id.game_IMG_cell33),findViewById(R.id.game_IMG_cell34)},
                {findViewById(R.id.game_IMG_cell40),findViewById(R.id.game_IMG_cell41),findViewById(R.id.game_IMG_cell42),findViewById(R.id.game_IMG_cell43),findViewById(R.id.game_IMG_cell44)},
                {findViewById(R.id.game_IMG_cell50),findViewById(R.id.game_IMG_cell51), findViewById(R.id.game_IMG_cell52),findViewById(R.id.game_IMG_cell53),findViewById(R.id.game_IMG_cell54)},
                {findViewById(R.id.game_IMG_cell60),findViewById(R.id.game_IMG_cell61),findViewById(R.id.game_IMG_cell62),findViewById(R.id.game_IMG_cell63),findViewById(R.id.game_IMG_cell64)}
        };

        Glide
                .with(this)
                .load(R.drawable.yard)
                .centerCrop()
                .into(game_IMG_background);


    }

    private void initButtons(){

        game_BTN_arrows = new Button[]{
                findViewById(R.id.game_BTN_up),
                findViewById(R.id.game_BTN_down),
                findViewById(R.id.game_BTN_right),
                findViewById(R.id.game_BTN_left),
        };
        for (int i=0; i<4 ; i++){
            game_BTN_arrows[i].setVisibility(View.VISIBLE);
        }

        game_BTN_arrows[0].setOnClickListener(view -> changeLocation(Actor.UP));
        game_BTN_arrows[1].setOnClickListener(view -> changeLocation(Actor.DOWN));
        game_BTN_arrows[2].setOnClickListener(view -> changeLocation(Actor.RIGHT));
        game_BTN_arrows[3].setOnClickListener(view -> changeLocation(Actor.LEFT));

}

    private void initSensors() {
        mySensors = new MySensors();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySensors.setSensorManager(sensorManager);
        mySensors.initSensor();

    }

    private void show(Actor actor, int index){
        board[actor.indexX][actor.indexY].setImageResource(icons[index]);
        board[actor.indexX][actor.indexY].setVisibility(View.VISIBLE);
    }

    private void hidden(Actor actor){
        board[actor.indexX][actor.indexY].setVisibility(View.INVISIBLE);
    }

    private void moveInSameDir() {
        if (wolf.inSamePlace(redHood)) {
            soundsUtils.setMpAndPlay((ContextWrapper) getApplicationContext(),R.raw.wolf);
            show(wolf, 2);
            gameManager.reduceLives();
            updateUI();
            restart=true;

        } else {
            show(wolf, 0);
            show(redHood, 1);
        }
    }

    private void changeLocation(int direction) {
        if (gameManager.getLives() > 0) {
            if (restart) {
                hidden(wolf);
                wolf.direction = 4;
                wolf.indexX = gameManager.ROWS - 1;
                wolf.indexY = gameManager.COLS - 2;
                redHood.indexX = 0;
                redHood.indexY = gameManager.COLS - 1;
                restart = false;
            } else {
                if(redHood.inSamePlace(bomb))
                {
                    moveBomb();
                }
                hidden(wolf);
                hidden(redHood);
                wolf.direction = direction;
                wolf.changeDirection();
                redHood.changeDirection();
            }
            moveInSameDir();
        }
    }

    private void moveBomb(){
        int temp1 = new Random().nextInt(4);
        bomb.indexX = temp1;
        int temp2 = new Random().nextInt(5);
        bomb.indexY = temp2;
        show(bomb, 3);
    }

    private void getDataFromLogin() {
        Bundle bundle = getIntent().getExtras();
        gameType = bundle.getInt(GAME_TYPE);
        playerName = bundle.getString(GAME_PLAYER);
    }

    private void startGame(){
        if (gameType == 1) {
            initSensors();
        }
        else
            initButtons();
    }

    private SensorEventListener accSensorEventListener =new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];

            if (x < -5) {// move right
                wolf.setDirection(Actor.RIGHT);
            } else if (x > 5) {// move left
                wolf.setDirection(Actor.LEFT);
            } else if (y < -3) {// move up
                wolf.setDirection(Actor.UP);
            } else if (y > 5) {// move down
                wolf.setDirection(Actor.DOWN);
            }
        }



        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            Log.d("pttt", "onAccuracyChanged");
        }
    };

    private void gameOver() {
            soundsUtils.stopMp();
            soundsUtils.setMpAndPlay((ContextWrapper) getApplicationContext(),R.raw.gameover);
            hidden(wolf);
            hidden(redHood);
            hidden(bomb);
            Toast.makeText(this, "your score is:" + gameManager.getScore(), Toast.LENGTH_LONG).show();
            findViewById(R.id.game_IMG_game_over).setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveData();
                }
            }, 1500);



    }

    private void saveData() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        double latitude = 0.0;
        double longitude = 0.0;
        gpsService = new GpsTracker(Main_Activity.this);
        if (gpsService.canGetLocation()) {
            latitude = gpsService.getLatitude();
            longitude = gpsService.getLongitude();
        } else {
            gpsService.showSettingsAlert();
        }


        Record winner = new Record();
        winner.setName(playerName);
        winner.setScore(gameManager.getScore());
        winner.setLon(longitude);
        winner.setLat(latitude);
        myDB.addRecord(winner);

        String json = new Gson().toJson(myDB);
        MSPV.getMe().putString("MY_DB", json);
        Collections.sort(myDB.getRecords(), new MyDB.SortByScore());
        stopTimer();
        Intent top_ten = new Intent(this, TopTen_Activity.class);
        startActivity(top_ten);

        this.finish();
    }

    private void tick() {
        ++counter;

        if(bomb.inSamePlace(wolf)){
            soundsUtils.setMpAndPlay((ContextWrapper) getApplicationContext(),R.raw.bomb);
            moveBomb();
            gameManager.setScore(gameManager.getScore()+10);
        }
        else if(counter%5==0 || bomb.inSamePlace(redHood)){
            hidden(bomb);
            moveBomb();
        }


        gameManager.setScore(gameManager.getScore()+ 1);
        Log.d("pttt" , "Tick" + counter);
        game_LBL_score.setText("" + gameManager.getScore());


    }

    protected void onStop() {
        super.onStop();
        if (timerStatus == TIMER_STATUS.RUNNING) {
            stopTimer();
            timerStatus = TIMER_STATUS.PAUSE;
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        if(timerStatus == TIMER_STATUS.OFF){
            startTimer();
        } else if(timerStatus == TIMER_STATUS.RUNNING ){
            stopTimer();
        }else{
            startTimer();
        }
    }

    private void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(()-> {
                        tick();
                        changeLocation(wolf.direction);

                });

            }
        }, 0, delay);
    }

    private void stopTimer() {
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameType == 1)
            mySensors.getSensorManager().registerListener(accSensorEventListener, mySensors.getAccSensor(), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameType == 1)
            mySensors.getSensorManager().unregisterListener(accSensorEventListener);
    }
}