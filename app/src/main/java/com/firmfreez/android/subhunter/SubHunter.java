package com.firmfreez.android.subhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;

import java.util.Random;

public class SubHunter extends AppCompatActivity {
    int numberHorizontalPixels;
    int numberVerticalPixels;
    int blockSize;
    int gridWidth = 40;
    int gridHeight;
    float horizontalTouched = -100f;
    float verticalTouched = -100f;
    int subHorizontalPosition;
    int subVerticalPosition;
    boolean hit = false;
    int shotsTaken;
    int distanceFromSub;
    boolean debugging = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size);
        } else {
            display.getSize(size);
        }
        numberHorizontalPixels = size.x;
        numberVerticalPixels   = size.y;
        blockSize              = numberHorizontalPixels / gridWidth;
        gridHeight             = numberVerticalPixels   / blockSize;


        Log.d("Debugging", "In Create");
        newGame();
        draw();
    }

    /**
     * Когда пользователь закончил предыдущую партию и начинает новую
     */
    void newGame() {
        Random random = new Random();
        subVerticalPosition = random.nextInt(gridHeight);
        subHorizontalPosition = random.nextInt(gridWidth);
        shotsTaken = 0;
        
        Log.d("Debugging", "In new game");
    }

    /**
     * Прорисовка таблицы
     * Поля обнаружения
     * Экран "БУМ"
     */
    void draw() {
        Log.d("Debugging", "In Draw");
        printDebugText();
    }

    /**
     * Событие касания экрана
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("Debugging", "In Touch Event");
        takeShot();
        return super.onTouchEvent(event);
    }


    /**
     * Совершение выстрела
     */
    void takeShot() {
        Log.d("Debugging", "In Take Shoot");
        draw();
    }

    /**
     * Метод отображает "Бум"
     */
    void boom() {

    }

    /**
     * Выводит отладочный текст
     */
    void printDebugText() {
        Log.d("numberHorizontalPixels" , "" + numberHorizontalPixels);
        Log.d("numberVerticalPixels", "" + numberVerticalPixels);
        Log.d("blockSize", "" + blockSize);
        Log.d("gridWidth", "" + gridWidth);
        Log.d("gridHeight", "" + gridHeight);
        Log.d("horizontalTouched", "" + horizontalTouched);
        Log.d("verticalTouched", "" + verticalTouched);
        Log.d("subHorizontalPosition", "" + subHorizontalPosition);
        Log.d("subVerticalPosition", "" + subVerticalPosition);
        Log.d("hit", "" + hit);
        Log.d("shotsTaken", "" + shotsTaken);
        Log.d("debugging", "" + debugging);
        Log.d("distanceFromSub", "" + distanceFromSub);
    }
}