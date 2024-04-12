package com.serdar_kara.bilfit;

import android.content.Context;
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
    private SlotClickListener slotClickListener;

    private final String[] DAYS = {"M", "T", "W", "T", "F", "S", "S"};

    public interface SlotClickListener {
        void onSlotClicked(int position);
    }

    public SlottedRectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
    }

    public void setSlotClickListener(SlotClickListener listener) {
        this.slotClickListener = listener;
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
        }

        paint.setTextAlign(originalAlign); // Reset text align
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && slotClickListener != null) {
            float x = event.getX();
            int position = calculateSlotPosition(x);
            slotClickListener.onSlotClicked(position);
            return true;
        }
        return super.onTouchEvent(event);
    }

    private int calculateSlotPosition(float clickX) {
        int width = getWidth();
        return (int) (clickX / (width / 7));
    }
}
