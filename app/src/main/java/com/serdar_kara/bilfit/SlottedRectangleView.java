package com.serdar_kara.bilfit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class SlottedRectangleView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rect = new RectF(); // Used for drawing rounded rectangle

    private final String[] DAYS = {"M", "T", "W", "T", "F", "S", "S"};


    public SlottedRectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Background rounded rectangle
        paint.setColor(Color.parseColor("#D6D7D7")); // Background color
        rect.set(16, 0, width - 16, height);
        canvas.drawRoundRect(rect, 50, 50, paint); // Adjust corner radius as needed

        // Drawing slots and day letters
        paint.setColor(Color.BLACK); // Line color
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size)); // Set text size
        float textHeight = paint.getTextSize(); // Get text size height for vertical alignment
        Paint.Align originalAlign = paint.getTextAlign();
        paint.setTextAlign(Paint.Align.CENTER); // Center text

        paint.setTypeface(Typeface.DEFAULT_BOLD); // Set text style (bold)

        for (int i = 0; i < 7; i++) { // Draw 6 lines to create 7 slots and text for each slot
            if (i > 0) {
                float xLine = width * i / 7.0f;
                canvas.drawLine(xLine, 0, xLine, height, paint);
            }
            float xText = width * (i / 7.0f) + width / 14.0f; // Center of each slot
            LocalDate today = LocalDate.now();
            DayOfWeek dayOfWeek = today.getDayOfWeek();
            if (dayOfWeek.getValue() == i + 1) {
                paint.setColor(Color.parseColor("#FF0000")); // Highlight today
            } else {
                paint.setColor(Color.BLACK);
            }
            canvas.drawText(DAYS[i], xText, (float) (height / (2.5)) + textHeight / 2, paint);

            boolean[] exerciseDays = getExerciseDays();
            boolean[] completedExerciseDays = getCompletedExerciseDays();


            if (exerciseDays[i]) {
                if (completedExerciseDays[i]) {
                    paint.setColor(Color.parseColor("#00FF00"));
                    paint.setTypeface(Typeface.DEFAULT_BOLD); // Set text style (bold
                    System.out.println("ticccccc");
                    paint.setTextSize(60);
                    canvas.drawText("+", xText, (float) (height / 1.35) + textHeight / 2, paint);
                } else {
                    paint.setColor(Color.RED);
                    System.out.println("minuss");
                    paint.setTextSize(70);
                    paint.setTypeface(Typeface.DEFAULT_BOLD); // Set text style (bold
                    canvas.drawText("-", xText, (float) (height / 1.35) + textHeight / 2, paint);
                }
            }else{
                System.out.println(exerciseDays.length);
                System.out.println("nooo");
            }
            paint.setTextSize(textHeight);



        }

        paint.setTextAlign(originalAlign); // Reset text align
    }

    public boolean[] getExerciseDays(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ExerciseDays", Context.MODE_PRIVATE);
        boolean[] days = new boolean[7];
        for (int i = 0; i < 7; i++) {
            days[i] = sharedPreferences.getBoolean("day_" + i, false);
        }
        return days;
    }

    public boolean[] getCompletedExerciseDays(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CompletedExerciseDays", Context.MODE_PRIVATE);
        boolean[] days = new boolean[7];
        for (int i = 0; i < 7; i++) {
            days[i] = sharedPreferences.getBoolean("day_" + i, false);
        }
        return days;
    }



}
