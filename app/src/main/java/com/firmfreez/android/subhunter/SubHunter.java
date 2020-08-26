package com.firmfreez.android.subhunter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.Random;

public class SubHunter extends Activity {
    //Переменные
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
    boolean debugging = false;
    //Объекты для рисования
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private ImageView gameView;
    private Paint mPaint;

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

        //Init drawing
        mBitmap = Bitmap.createBitmap(numberHorizontalPixels, numberVerticalPixels, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        gameView = new ImageView(this);
        mPaint = new Paint();


        setContentView(gameView);

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
        gameView.setImageBitmap(mBitmap);

        //Заполняем экран белым цветом
        mCanvas.drawColor(Color.argb(255,255,255,255));

        //Подготовка и рисование линий
        mPaint.setColor(Color.argb(255,0,0,0));
        //Вертикальная линия
        for (int i = 0; i < gridWidth; i++) {
            mCanvas.drawLine(blockSize * i, 0, blockSize * i, numberVerticalPixels - 1, mPaint);
        }

        //Горизонтальная линия
        for (int i = 0; i < gridHeight + 1; i++) {
            mCanvas.drawLine(0,blockSize * i, numberHorizontalPixels - 1, blockSize * i, mPaint);
        }

        //Рисуем квадрат попадания
        mCanvas.drawRect(horizontalTouched * blockSize,
                verticalTouched * blockSize,
                (horizontalTouched * blockSize) + blockSize,
                (verticalTouched * blockSize) + blockSize, mPaint);

        //Рисуем отладочный текст
        mPaint.setTextSize(blockSize * 2);
        mPaint.setColor(Color.argb(255,0,0,255));
        mCanvas.drawText("Shots taken: " + shotsTaken + " Distance: " + distanceFromSub, blockSize, blockSize * 1.75f,mPaint);

        Log.d("Debugging", "In Draw");
        printDebugText();
    }

    /**
     * Событие касания экрана
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("Debugging", "In Touch Event");
        if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
            takeShot(event.getX(),event.getY());
        }
        return super.onTouchEvent(event);
    }


    /**
     * Совершение выстрела
     */
    void takeShot(float x, float y) {
        Log.d("Debugging", "In Take Shoot");

        shotsTaken++;
        horizontalTouched = (int) x / blockSize;
        verticalTouched   = (int) y / blockSize;

        hit = horizontalTouched == subHorizontalPosition && verticalTouched == subVerticalPosition;

        int horizontalGap = (int) horizontalTouched - subHorizontalPosition;
        int verticalGap   = (int) verticalTouched   - subVerticalPosition;

        distanceFromSub = (int) Math.sqrt(Math.pow(horizontalGap,2)+ Math.pow(verticalGap,2));

        if(hit) {
            boom();
        } else {
            draw();
        }
    }

    /**
     * Метод отображает "Бум"
     */
    void boom() {
        //Устанавливаем BitMap на экран
        gameView.setImageBitmap(mBitmap);

        //Задаём сплошной цвет экрана
        mCanvas.drawColor(Color.argb(255,255,0,0));

        //Задаём размеры и цвет текста
        mPaint.setTextSize(blockSize * 10);
        mPaint.setColor(Color.argb(255,255,255,255));

        //Рисуем текст БУМ!
        mCanvas.drawText("BOOM!", blockSize * 4, blockSize * 14, mPaint);

        //Задаём размеры второго текста
        mPaint.setTextSize(blockSize * 2);

        //Рисуем второй текст
        mCanvas.drawText("Take a shoot to start again!", blockSize * 8, blockSize * 18, mPaint);

        //Начинаем новую игру
        newGame();
    }

    /**
     * Выводит отладочный текст
     */
    void printDebugText() {
        if(debugging) {
            mPaint.setTextSize(blockSize);
            mCanvas.drawText("numberHorizontalPixels: " + numberHorizontalPixels, 50, blockSize * 3, mPaint);
            mCanvas.drawText("numberVerticalPixels: " + numberVerticalPixels, 50, blockSize * 4, mPaint);
            mCanvas.drawText("blockSize: " + blockSize, 50, blockSize * 5, mPaint);
            mCanvas.drawText("gridWidth: " + gridWidth, 50, blockSize * 6, mPaint);
            mCanvas.drawText("gridHeight: " + gridHeight, 50, blockSize * 7, mPaint);
            mCanvas.drawText("horizontalTouched: " + horizontalTouched, 50, blockSize * 8, mPaint);
            mCanvas.drawText("verticalTouched: " + verticalTouched, 50, blockSize * 9, mPaint);
            mCanvas.drawText("subHorizontalPosition: " + subHorizontalPosition, 50, blockSize * 10, mPaint);
            mCanvas.drawText("subVerticalPosition: " + subVerticalPosition, 50, blockSize * 11, mPaint);
            mCanvas.drawText("hit: " + hit, 50, blockSize * 12, mPaint);
            mCanvas.drawText("shotsTaken: " + shotsTaken, 50, blockSize * 13, mPaint);
            mCanvas.drawText("debugging: " + debugging, 50, blockSize * 14, mPaint);
        }
    }
}