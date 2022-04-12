package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private ImageView[] game_IMG_hearts;
    private Button[] game_BTN_arrows;
    private ImageView [][]board;
    private TextView game_LBL_score;
    private GameManager gameManager;
    private int[] characters;
    private Player wolf;
    private Player redHood;
    private boolean restart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        findViews();

        game_BTN_arrows[0].setOnClickListener(view -> changeLocation(Player.UP));
        game_BTN_arrows[1].setOnClickListener(view -> changeLocation(Player.DOWN));
        game_BTN_arrows[2].setOnClickListener(view -> changeLocation(Player.RIGHT));
        game_BTN_arrows[3].setOnClickListener(view -> changeLocation(Player.LEFT));

        gameManager = new GameManager();
        wolf = new Player(gameManager.ROWS-1, gameManager.COLS-2, Player.STATIC , false);
        redHood = new Player(0, gameManager.COLS-1, Player.STATIC , true);
        updateUI();

    }


    private void moveInSameDir() {
        if (wolf.inSamePlace(redHood)) {
            board[wolf.indexX][wolf.indexY].setImageResource(characters[2]);
            board[wolf.indexX][wolf.indexY].setVisibility(View.VISIBLE);
            gameManager.reduceLives();
            updateUI();
            restart=true;

        } else {
            board[wolf.indexX][wolf.indexY].setImageResource(characters[0]);
            board[wolf.indexX][wolf.indexY].setVisibility(View.VISIBLE);
            board[redHood.indexX][redHood.indexY].setImageResource(characters[1]);
            board[redHood.indexX][redHood.indexY].setVisibility(View.VISIBLE);
        }
    }
    private void changeLocation(int direction) {
        if (gameManager.getLives() > 0) {
            if (restart) {
                board[wolf.indexX][wolf.indexY].setVisibility(View.INVISIBLE);
                wolf.direction = 4;
                wolf.indexX = gameManager.ROWS - 1;
                wolf.indexY = gameManager.COLS - 2;
                redHood.indexX = 0;
                redHood.indexY = gameManager.COLS - 1;
                restart = false;
            } else {
                board[wolf.indexX][wolf.indexY].setVisibility(View.INVISIBLE);
                board[redHood.indexX][redHood.indexY].setVisibility(View.INVISIBLE);
                wolf.direction = direction;
                wolf.changeDirection();
                redHood.changeDirection();
            }
            moveInSameDir();
        }
    }

    private void updateUI() {
        for (int i = 0; i < game_IMG_hearts.length; i++) {
            game_IMG_hearts[i].setVisibility(gameManager.getLives() > i ? View.VISIBLE : View.INVISIBLE);
        }
        done();
    }

    private void done() {
        if (gameManager.isDead()) {
            Toast.makeText(this, "your score is:" + gameManager.getScore(), Toast.LENGTH_LONG).show();
            findViewById(R.id.game_IMG_game_over).setVisibility(View.VISIBLE);
            board[wolf.indexX][wolf.indexY].setVisibility(View.INVISIBLE);
            board[redHood.indexX][redHood.indexY].setVisibility(View.INVISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishGame();
                }
            }, 1500);


        }

    }


    private void finishGame() {
        finish();
    }


    private Timer timer = new Timer();

    private final int DELAY = 1000;
    private enum TIMER_STATUS {
        OFF,
        RUNNING,
        PAUSE
    }

    private TIMER_STATUS timerStatus = TIMER_STATUS.OFF;
    private int counter = 0;

    private void tick() {
        ++counter;
        gameManager.setScore(counter);
        Log.d("pttt" , "Tick" + counter);
        game_LBL_score.setText("" + counter);
        changeLocation(wolf.direction);

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
        timerStatus = TIMER_STATUS.RUNNING;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tick();
                    }
                });

            }
        }, 0, DELAY);
    }

    private void stopTimer() {
        timer.cancel();
    }


    private void findViews() {
        game_LBL_score=findViewById(R.id.game_LBL_score);

        game_IMG_hearts = new ImageView[] {
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };

        characters = new int[] {
                R.drawable.ic_wolf,
                R.drawable.ic_redhood,
                R.drawable.ic_dead
        };
        game_BTN_arrows = new Button[]{
                findViewById(R.id.game_BTN_up),
                findViewById(R.id.game_BTN_down),
                findViewById(R.id.game_BTN_right),
                findViewById(R.id.game_BTN_left),

        };

        board = new ImageView[][]{
                {findViewById(R.id.game_IMG_cell00),findViewById(R.id.game_IMG_cell01),findViewById(R.id.game_IMG_cell02)},
                {findViewById(R.id.game_IMG_cell10),findViewById(R.id.game_IMG_cell11),findViewById(R.id.game_IMG_cell12)},
                {findViewById(R.id.game_IMG_cell20),findViewById(R.id.game_IMG_cell21),findViewById(R.id.game_IMG_cell22)},
                {findViewById(R.id.game_IMG_cell30),findViewById(R.id.game_IMG_cell31), findViewById(R.id.game_IMG_cell32)},
                {findViewById(R.id.game_IMG_cell40),findViewById(R.id.game_IMG_cell41),findViewById(R.id.game_IMG_cell42)},
        };


}
}