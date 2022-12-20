package com.example.dfrprogressbar.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.dfrprogressbar.R;

public class TimelineProgressBar extends View {

    private static final int DEFAULT_WIDTH = 150;
    private static final int DEFAULT_HEIGHT = 44;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#D9D9D9");
    private static final int DEFAULT_PROGRESS_COLOR = Color.parseColor("#7B7B7C");
    private static final int DEFAULT_PROGRESS = 50;

    private int backgroundColor = Color.BLACK;
    private int progressColor =  Color.GREEN;
    private int progress = 0;
    private int separatorColor = Color.BLACK;

    private final Rect bodyRect = new Rect();
    private final Rect firstSeparatorRect = new Rect();
    private final Rect secondSeparatorRect = new Rect();
    private final Rect thirdSeparatorRect = new Rect();
    private final Rect forthSeparatorRect = new Rect();
    private final Rect progressRect = new Rect();

    private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint separatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public TimelineProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimelineProgressBar);
            progress = ta.getInt(R.styleable.TimelineProgressBar_tpb_progress, DEFAULT_PROGRESS);
            progressColor = ta.getColor(R.styleable.TimelineProgressBar_tpb_progresscolor, DEFAULT_PROGRESS_COLOR);
            backgroundColor = ta.getColor(R.styleable.TimelineProgressBar_tpb_backgroundcolor, DEFAULT_BACKGROUND_COLOR);
            ta.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(DEFAULT_WIDTH, widthSize);
        } else {
            width = DEFAULT_WIDTH;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(DEFAULT_HEIGHT, heightSize);
        } else {
            //Be whatever you want
            height = DEFAULT_HEIGHT;
        }

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w == 0)
            return;
        //body
        bodyRect.left = 0;
        bodyRect.top = 0;
        bodyRect.right = w;
        bodyRect.bottom = h;

        //completed
        progressRect.left = 0;
        progressRect.top = 0;
        progressRect.right = (int) ((w / 100.0) * progress);
        progressRect.bottom = h;

        firstSeparatorRect.left = w / 5 - 4;
        firstSeparatorRect.top = 0;
        firstSeparatorRect.right = w / 5 + 4;
        firstSeparatorRect.bottom = h;

        secondSeparatorRect.left = w / 5 * 2 - 4;
        secondSeparatorRect.top = 0;
        secondSeparatorRect.right = w / 5 * 2 + 4;
        secondSeparatorRect.bottom = h;

        thirdSeparatorRect.left = w / 5 * 3 - 4;
        thirdSeparatorRect.top = 0;
        thirdSeparatorRect.right = w / 5 * 3 + 4;
        thirdSeparatorRect.bottom = h;

        forthSeparatorRect.left = w / 5 * 4 - 4;
        forthSeparatorRect.top = 0;
        forthSeparatorRect.right = w / 5 * 4 + 4;
        forthSeparatorRect.bottom = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBody(canvas);
        drawProgress(canvas);
        drawSeparators(canvas);
        invalidate();
    }



    protected void drawBody(Canvas canvas) {
        backgroundPaint.setColor(backgroundColor);
        canvas.drawRoundRect(new RectF(bodyRect), 15F, 15F, backgroundPaint);
    }

    protected void drawProgress(Canvas canvas) {
        progressPaint.setColor(progressColor);
        canvas.drawRoundRect(new RectF(progressRect), 10F, 10F, progressPaint);
    }

    protected void drawSeparators(Canvas canvas) {
        separatorPaint.setColor(separatorColor);
        canvas.drawRect(firstSeparatorRect, separatorPaint);
        canvas.drawRect(secondSeparatorRect, separatorPaint);
        canvas.drawRect(thirdSeparatorRect, separatorPaint);
        canvas.drawRect(forthSeparatorRect, separatorPaint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
        requestLayout();
    }

    public int getProgress() {
        return progress;
    }
}
