package com.example.dfrprogressbar.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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
    private final Rect bodyRectDst = new Rect();
    private final Rect progressRectSrc = new Rect();
//    private final Rect progressRect = new Rect();
    //Rect Paint
    private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //Text Paint
    private final Paint progressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint hoursLeftTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint hoursCompletedTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Bitmap bitmapSrc;
    private Bitmap bitmapDsc;



    public WeekProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WeekProgressBar);
            progress = ta.getFloat(R.styleable.WeekProgressBar_wpb_progress, DEFAULT_PROGRESS);
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
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        //body
        bodyRectDst.left = 0;
        bodyRectDst.top = 50;
        bodyRectDst.right = w;
        bodyRectDst.bottom = h;
        //completed
        progressRectSrc.left = 0;
        progressRectSrc.top = 50;
        if (progress > 100)
            progressRectSrc.right = (int) ((w / 100.0) * (progress % 100));
        else
            progressRectSrc.right = (int) ((w / 100.0) * progress);
        progressRectSrc.bottom = h;

//        prepareBitmaps(w, h);
        bitmapDsc = createBitmap(bodyRectDst, backgroundPaint, w, h);

        bitmapSrc = createBitmap(progressRectSrc, progressPaint, w / 2, h);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawBody(canvas);
//        canvas.drawBitmap(maskBm, bodyRect, bodyRect, null);
//        drawProgress(canvas);

        canvas.drawBitmap(bitmapDsc, 0, 0, backgroundPaint);
        canvas.drawBitmap(bitmapSrc, 0, 0, progressPaint);
        drawText(canvas);
        invalidate();
    }

//    private void prepareBitmaps(int w, int h) {
//        maskBm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        resultBm = maskBm.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas maskCanvas = new Canvas(maskBm);
//        maskCanvas.drawRoundRect(new RectF(bodyRectDst), 15f, 15f, backgroundPaint);
//        backgroundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
//        srcBm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//
//        Canvas resultCanvas = new Canvas(resultBm);
//
//        resultCanvas.drawBitmap(maskBm, bodyRectDst, bodyRectDst, null);
//        resultCanvas.drawBitmap(srcBm, bodyRectDst, bodyRectDst, null);
//
//
////        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
//
//    }

    private Bitmap createBitmap(Rect rect, Paint paint, int w, int h) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas bitmapCanvas = new Canvas(bitmap);

        bitmapCanvas.drawRect(rect, paint);

        return bitmap;
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
            canvas.drawRoundRect(new RectF(bodyRectDst), 15F, 15F, backgroundPaint);
        } else {
            backgroundPaint.setColor(backgroundColor);
            canvas.drawRoundRect(new RectF(bodyRectDst), 15F, 15F, backgroundPaint);
        }
    }

    protected void drawProgress(Canvas canvas) {
        if (progress > 100) {
            progressPaint.setColor(Color.parseColor("#00521d"));
//            canvas.drawRoundRect(new RectF(progressRoundRect), 15F, 15F, progressPaint);
        } else {
            progressPaint.setColor(progressColor);
            canvas.drawRect(progressRectSrc, progressPaint);

//            canvas.drawRoundRect(new RectF(progressRoundRect), 15F, 15F, progressPaint);
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
        canvas.drawText(progress + "%", progressRectSrc.right, bodyRectDst.top + offsetY, progressTextPaint);

        String hoursCompletedString = hoursCompleted + " hrs";
        String amountROString = "(RO x" + amountRO + ")";
        if ((hoursCompletedTextPaint.measureText(hoursCompletedString) >= progressRectSrc.width()) || (hoursCompletedTextPaint.measureText(amountROString) >= progressRectSrc.width())) {
            canvas.drawText(hoursCompletedString, progressRectSrc.right + 50, (float) (progressRectSrc.top + progressRectSrc.height() / 2 + offsetY / 2.5) , hoursCompletedTextPaint);
            canvas.drawText(amountROString, progressRectSrc.right + 50, (float) (progressRectSrc.bottom - progressRectSrc.height() / 2 - offsetY * 2.5), hoursCompletedTextPaint);
        } else {
            canvas.drawText(hoursCompletedString, progressRectSrc.exactCenterX(), (float) (progressRectSrc.top + progressRectSrc.height() / 2 + offsetY / 2.5), hoursCompletedTextPaint);
            canvas.drawText(amountROString, progressRectSrc.exactCenterX(), (float) (progressRectSrc.bottom - progressRectSrc.height() / 2 - offsetY * 2.5), hoursCompletedTextPaint);
        }
        if (progress < 100) {
            String hoursLeftString = hoursLeft + " hrs";
            if ((hoursLeftTextPaint.measureText(hoursLeftString) >= bodyRectDst.width() - progressRectSrc.width()) || (hoursLeftTextPaint.measureText("left") >= bodyRectDst.width() - progressRectSrc.width())) {
                hoursLeftTextPaint.setColor(backgroundColor);
                canvas.drawText(hoursLeftString, progressRectSrc.right - 50, (float) (bodyRectDst.top + bodyRectDst.height() / 2 + offsetY / 2.5), hoursLeftTextPaint);
                canvas.drawText("left", progressRectSrc.right - 50, (float) (bodyRectDst.bottom - bodyRectDst.height() / 2 - offsetY * 2.5), hoursLeftTextPaint);
            } else {
                hoursLeftTextPaint.setColor(progressColor);
                canvas.drawText(hoursLeftString, bodyRectDst.exactCenterX() + progressRectSrc.right / 2, (float) (bodyRectDst.top + bodyRectDst.height() / 2 + offsetY / 2.5), hoursLeftTextPaint);
                canvas.drawText("left", bodyRectDst.exactCenterX() + progressRectSrc.right / 2, (float) (bodyRectDst.bottom - bodyRectDst.height() / 2 - offsetY * 2.5), hoursLeftTextPaint);
            }
        } else {
            String hoursLeftString = "ACHIEVED";
            hoursLeftTextPaint.setColor(backgroundColor);
            if (hoursLeftTextPaint.measureText(hoursLeftString) >= bodyRectDst.width() - progressRectSrc.width()) {
                canvas.drawText(hoursLeftString, progressRectSrc.right - progressRectSrc.right / 10, bodyRectDst.exactCenterY() - offsetY, hoursLeftTextPaint);
            } else {
                canvas.drawText(hoursLeftString, bodyRectDst.right - progressRectSrc.right / 10, bodyRectDst.exactCenterY() - offsetY, hoursLeftTextPaint);
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
