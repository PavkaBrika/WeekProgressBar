package com.example.dfrprogressbar.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.dfrprogressbar.R;

public class WeekProgressBar extends View {

    private static final int DEFAULT_SIZE = 40;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#D9D9D9");
    private static final int DEFAULT_PROGRESS_COLOR = Color.parseColor("#3F8C27");
    private static final int DEFAULT_PROGRESS_TEXT_COLOR = Color.WHITE;
    private static final float DEFAULT_PROGRESS = 50.0F;

    private int backgroundColor = Color.GRAY;
    private int progressColor =  Color.GREEN;
    private int progressTextColor = Color.WHITE;
    private float progress = 0.0F;
    private int amountRO = 0;
    private float hoursCompleted = 0.0F;
    private float hoursLeft = 0.0F;

    //Rect
    private final Rect bodyRect = new Rect();
    private final Rect progressRect = new Rect();
    //Rect Paint
    private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //Text Paint
    private final Paint progressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint hoursLeftTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint hoursCompletedTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public WeekProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WeekProgressBar);
            progress = ta.getFloat(R.styleable.WeekProgressBar_progress, DEFAULT_PROGRESS);
            progressColor = ta.getColor(R.styleable.WeekProgressBar_wpb_progresscolor, DEFAULT_PROGRESS_COLOR);
            backgroundColor = ta.getColor(R.styleable.WeekProgressBar_wpb_backgroundcolor, DEFAULT_BACKGROUND_COLOR);
            progressTextColor =  ta.getColor(R.styleable.WeekProgressBar_wpb_progresstextcolor, DEFAULT_PROGRESS_TEXT_COLOR);
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
        bodyRect.top = 50;
        bodyRect.right = w;
        bodyRect.bottom = h;
        //completed
        progressRect.left = 0;
        progressRect.top = 50;
        progressRect.right = (int) ((w / 100.0) * (progress % 100));
        progressRect.bottom = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBody(canvas);
        drawProgress(canvas);
        drawText(canvas);
        invalidate();
    }

    protected int resolveDefaultSize(int spec) {
        if (MeasureSpec.getMode(spec) == MeasureSpec.UNSPECIFIED)
            return (int) (getContext().getResources().getDisplayMetrics().density * DEFAULT_SIZE);
        else
            return MeasureSpec.getSize(spec);
    }

    protected void drawBody(Canvas canvas) {
        if (progress > 100) {
            backgroundPaint.setColor(progressColor);
            canvas.drawRoundRect(new RectF(bodyRect), 5F, 5F, backgroundPaint);
        } else {
            backgroundPaint.setColor(backgroundColor);
            canvas.drawRoundRect(new RectF(bodyRect), 5F, 5F, backgroundPaint);
        }
    }

    protected void drawProgress(Canvas canvas) {
        if (progress > 100) {
            progressPaint.setColor(Color.parseColor("#00521d"));
            canvas.drawRoundRect(new RectF(progressRect), 5F, 5F, progressPaint);
        } else {
            progressPaint.setColor(progressColor);
            canvas.drawRoundRect(new RectF(progressRect), 5F, 5F, progressPaint);
        }
    }

    protected void drawText(Canvas canvas) {
        //ProgressText
        progressTextPaint.setColor(progressTextColor);
        progressTextPaint.setTextAlign(Paint.Align.CENTER);
        progressTextPaint.setTextSize(getHeight() * 0.2F);
        progressTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //HoursLeftText
        hoursLeftTextPaint.setColor(progressTextColor);
        hoursLeftTextPaint.setTextAlign(Paint.Align.CENTER);
        hoursLeftTextPaint.setTextSize(getHeight() * 0.2F);
        hoursLeftTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //HoursCompletedText
        hoursCompletedTextPaint.setColor(Color.WHITE);
        hoursCompletedTextPaint.setTextAlign(Paint.Align.CENTER);
        hoursCompletedTextPaint.setTextSize(getHeight() * 0.2F);
        hoursCompletedTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        float offsetY = (progressTextPaint.descent() + progressTextPaint.ascent()) / 2;
        canvas.drawText(progress + "%", progressRect.right, bodyRect.top + offsetY, progressTextPaint);

        String hoursCompletedString = hoursCompleted + " hrs";
        String amountROString = "(RO x" + amountRO + ")";
        if ((hoursCompletedTextPaint.measureText(hoursCompletedString) >= progressRect.width()) || (hoursCompletedTextPaint.measureText(amountROString) >= progressRect.width())) {
            canvas.drawText(hoursCompletedString, progressRect.right + 50, progressRect.top + 28 , hoursCompletedTextPaint);
            canvas.drawText(amountROString, progressRect.right + 50, progressRect.bottom - 12, hoursCompletedTextPaint);
        } else {
            canvas.drawText(hoursCompletedString, progressRect.exactCenterX(), progressRect.top + 28 , hoursCompletedTextPaint);
            canvas.drawText(amountROString, progressRect.exactCenterX(), progressRect.bottom - 12, hoursCompletedTextPaint);
        }
        if (progress < 100) {
            String hoursLeftString = hoursLeft + " hrs";
            if ((hoursLeftTextPaint.measureText(hoursLeftString) >= bodyRect.width() - progressRect.width()) || (hoursLeftTextPaint.measureText("left") >= bodyRect.width() - progressRect.width())) {
                hoursLeftTextPaint.setColor(backgroundColor);
                canvas.drawText(hoursLeftString, progressRect.right - 50, bodyRect.top + 28, hoursLeftTextPaint);
                canvas.drawText("left", progressRect.right - 50, bodyRect.bottom - 12, hoursLeftTextPaint);
            } else {
                hoursLeftTextPaint.setColor(progressColor);
                canvas.drawText(hoursLeftString, bodyRect.exactCenterX() + progressRect.right / 2, bodyRect.top + 28, hoursLeftTextPaint);
                canvas.drawText("left", bodyRect.exactCenterX() + progressRect.right / 2, bodyRect.bottom - 12, hoursLeftTextPaint);
            }
        } else {
            String hoursLeftString = "ACHIEVED";
            hoursLeftTextPaint.setColor(backgroundColor);
            if (hoursLeftTextPaint.measureText(hoursLeftString) >= bodyRect.width() - progressRect.width()) {
                canvas.drawText(hoursLeftString, progressRect.right - 65, bodyRect.exactCenterY() - offsetY, hoursLeftTextPaint);
            } else {
                canvas.drawText(hoursLeftString, bodyRect.right - 65, bodyRect.exactCenterY() - offsetY, hoursLeftTextPaint);
            }
        }

    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
        requestLayout();
    }

    public float getProgress() {
        return progress;
    }

    public void setAmountRO(int amountRO) {
        this.amountRO = amountRO;
        invalidate();
        requestLayout();
    }

    public void setHoursCompleted(float hoursCompleted) {
        this.hoursCompleted = hoursCompleted;
        invalidate();
        requestLayout();
    }

    public void setHoursLeft(float hoursLeft) {
        this.hoursLeft = hoursLeft;
        invalidate();
        requestLayout();
    }
}
