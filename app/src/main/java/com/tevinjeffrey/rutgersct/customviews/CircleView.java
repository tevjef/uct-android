package com.tevinjeffrey.rutgersct.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.tevinjeffrey.rutgersct.R;

import icepick.Icepick;
import icepick.Icicle;

public class CircleView extends View {

    final int DEFAULT_TITLE_COLOR = Color.CYAN;

    final String DEFAULT_TITLE = "Title";

    final boolean DEFAULT_SHOW_TITLE = true;

    final float DEFAULT_TITLE_SIZE = 25f;

    final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    final int DEFAULT_VIEW_SIZE = 96;

    @Icicle
    int mTitleColor = DEFAULT_TITLE_COLOR;

    @Icicle
    int mBackgroundColor = DEFAULT_BACKGROUND_COLOR;

    @Icicle
    String mTitleText = DEFAULT_TITLE;

    @Icicle
    float mTitleSize = DEFAULT_TITLE_SIZE;

    @Icicle
    boolean mShowTitle = DEFAULT_SHOW_TITLE;

    TextPaint mTitleTextPaint;

    Paint mBackgroundPaint;

    @Icicle
    RectF mInnerRectF;

    @Icicle
    int mViewSize;

    public CircleView(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CircleView, defStyle, 0);

        if(a.hasValue(R.styleable.CircleView_titleText)){
            mTitleText = a.getString(R.styleable.CircleView_titleText);
        }

        mTitleColor = a.getColor(R.styleable.CircleView_titleColor,DEFAULT_TITLE_COLOR);
        mBackgroundColor = a.getColor(R.styleable.CircleView_backgroundColorValue,DEFAULT_BACKGROUND_COLOR);

        mTitleSize = a.getDimension(R.styleable.CircleView_titleSize,DEFAULT_TITLE_SIZE);


        a.recycle();

        mTitleTextPaint = new TextPaint();
        mBackgroundPaint = new Paint();
        mInnerRectF = new RectF();

        //Title TextPaint
        mTitleTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTitleTextPaint.setAntiAlias(true);
        mTitleTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mTitleTextPaint.setTextAlign(Paint.Align.CENTER);
        mTitleTextPaint.setLinearText(true);
        mTitleTextPaint.setColor(mTitleColor);
        mTitleTextPaint.setTextSize(mTitleSize);


        //Background Paint
        mBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBackgroundPaint.setColor(mBackgroundColor);

    }


    private void invalidatePaints(){
        mBackgroundPaint.setColor(mBackgroundColor);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = resolveSize(DEFAULT_VIEW_SIZE, widthMeasureSpec);
        int height = resolveSize(DEFAULT_VIEW_SIZE, heightMeasureSpec);
        mViewSize = Math.min(width, height);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mInnerRectF.set(0, 0, mViewSize, mViewSize);

        final float centerX = mInnerRectF.centerX();
        final float centerY = mInnerRectF.centerY();

        canvas.drawCircle(centerX, centerY, mViewSize/2 ,mBackgroundPaint);

        final int xPos = (int) centerX;
        final int yPos = (int) (centerY - (mTitleTextPaint.descent() + mTitleTextPaint.ascent()) / 2);

        if (mShowTitle) {
            canvas.drawText(mTitleText,
                    xPos,
                    yPos,
                    mTitleTextPaint);
        }


    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    /**
     * Sets whether the view's title string will be shown.
     * @param flag The boolean value.
     */
    public void setShowTitle(boolean flag){
        this.mShowTitle = flag;
        invalidate();
    }

    /**
     * Sets the view's title string attribute value.
     * @param title The example string attribute value to use.
     */
    public void setTitleText(String title) {
        mTitleText = title;
        invalidate();
    }

    public String getTitleText() {
       return mTitleText;
    }

    /**
     * Sets the view's background color attribute value.
     * @param backgroundColor The background color attribute value to use.
     */
    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        invalidatePaints();
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
    }
}