package com.example.project1;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    public static int ROWS = 5;
    public static int COLS = 3;

    private int lives = 3;
    private int score =0;

    public GameManager() {

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void reduceLives() {
        lives--;
    }


    public boolean isDead() {
        return lives <= 0;
    }


}