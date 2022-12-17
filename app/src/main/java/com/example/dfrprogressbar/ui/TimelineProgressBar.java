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

    private static final int DEFAULT_SIZE = 40;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#D9D9D9");
    private static final int DEFAULT_PROGRESS_COLOR = Color.parseColor("#7B7B7C");
    private static final int DEFAULT_PROGRESS = 50;

    private int backgroundColor = Color.BLACK;
    private int progressColor =  Color.GREEN;
    private int progress = 0;
    private int separatorColor = Color.BLACK;

    private final Rect bodyRect = new Rect();
    private final Rect separatorRect = new Rect();
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

        int initWidthSize = resolveDefaultSize(widthMeasureSpec);
        int initHeightSize = resolveDefaultSize(heightMeasureSpec);
        setMeasuredDimension(initWidthSize, initHeightSize);
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
//        progressRect.left = 0;
//        progressRect.top = 50;
//        progressRect.right = (int) ((w / 100.0) * progress);
//        progressRect.bottom = h;

        separatorRect.left = 0;
        separatorRect.top = 0;
        separatorRect.right = 0;
        separatorRect.bottom = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBody(canvas);
        invalidate();
    }

    protected int resolveDefaultSize(int spec) {
        if (MeasureSpec.getMode(spec) == MeasureSpec.UNSPECIFIED)
            return (int) (getContext().getResources().getDisplayMetrics().density * DEFAULT_SIZE);
        else
            return MeasureSpec.getSize(spec);
    }

    protected void drawBody(Canvas canvas) {
        backgroundPaint.setColor(backgroundColor);
        separatorPaint.setColor(separatorColor);
        canvas.drawRoundRect(new RectF(bodyRect), 15F, 15F, backgroundPaint);
        for (int i = 0; i <= 4; i++ ) {
            separatorRect.left += bodyRect.width() / 5 + 10;
            separatorRect.right += bodyRect.width() / 5;
            canvas.drawRect(separatorRect, separatorPaint);
        }
//        canvas.drawRect(separatorRect, separatorPaint);

    }
}
