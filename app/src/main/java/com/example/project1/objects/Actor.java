package com.example.project1.objects;

import java.util.Random;

public class Actor {

    public static int UP = 0;
    public static int DOWN = 1;
    public static int RIGHT = 2;
    public static int LEFT = 3;
    public static int STATIC =4;

    public int indexX;
    public int indexY;
    public int direction;
    public boolean randomDir = false;

    public Actor(int indexX, int indexY, int direction, boolean randomDir) {
        this.indexX = indexX;
        this.indexY = indexY;
        this.direction = direction;
        this.randomDir = randomDir;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void changeDirection() {
        if (randomDir) {
            int temp = new Random().nextInt(4);
            direction = temp;
        }
        if (direction == UP) {
            if (indexX == 0) {
                indexX = GameManager.ROWS - 1;
            } else indexX = indexX -1;
        } else if (direction == DOWN) {
            if (indexX == GameManager.ROWS - 1) {
                indexX = 0;
            } else indexX = indexX + 1;
        } else if (direction == RIGHT) {
            if (indexY == GameManager.COLS - 1) {
                indexY = 0;
            } else indexY = indexY + 1;
        } else if (direction == LEFT) {
            if (indexY == 0) {
                indexY = GameManager.COLS - 1;
            } else indexY = indexY - 1;
        }

    }



    public boolean inSamePlace(Actor actor) {
        return indexX == actor.indexX && indexY == actor.indexY;
    }
}