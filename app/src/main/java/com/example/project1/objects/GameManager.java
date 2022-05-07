package com.example.project1.objects;

import android.os.Bundle;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    public static int ROWS = 7;
    public static int COLS = 5;

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