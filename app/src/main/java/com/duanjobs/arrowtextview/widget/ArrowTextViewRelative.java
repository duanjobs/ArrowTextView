package com.duanjobs.arrowtextview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duanjobs.arrowtextview.R;

/**
 * Created by duanjobs
 */
public class ArrowTextViewRelative extends RelativeLayout implements Runnable {

    protected Context mContext;
    protected float radius;
    protected int backroundColor;
    protected ArrowDirection arrowDirection;
    protected float relativePosition;
    protected RelativeLayout rel;

    protected ImageView arrowImage;
    protected TextView tvContent;
    protected TintedBitmapDrawable imgDrawable;
    protected RoundRectDrawable roundRectDrawable;


    public ArrowTextViewRelative(Context context) {
        this(context, null, 0);
    }

    public ArrowTextViewRelative(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrowTextViewRelative(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArrowTextView);
        radius = typedArray.getDimension(R.styleable.ArrowTextView_arrowCornerRadius, 0);
        backroundColor = typedArray.getColor(R.styleable.ArrowTextView_arrowBackgroundColor, Color.GRAY);
        int textColor = typedArray.getColor(R.styleable.ArrowTextView_arrowTextColor, 0);
        float textSize = typedArray.getDimension(R.styleable.ArrowTextView_arrowTextSize, 0);
        String contentText = typedArray.getString(R.styleable.ArrowTextView_arrowText);
        int direction = typedArray.getInt(R.styleable.ArrowTextView_arrowDirection, 1);
        setArrowDirection(direction);

        float position = typedArray.getFraction(R.styleable.ArrowTextView_relativePosition, 1, 1, 0.3f);
        setRelativePosition(position);
        typedArray.recycle();
        layoutContent(radius, backroundColor, textColor, textSize, contentText);

    }

    private void layoutContent(float radius, int backroundColor, int textColor, float textSize, String contentText) {
        rel = new RelativeLayout(mContext);
        rel.setId(View.generateViewId());
        LayoutParams relParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        roundRectDrawable = new RoundRectDrawable(backroundColor, radius);
        rel.setBackground(roundRectDrawable);

        //add textview
        tvContent = new TextView(mContext);
        tvContent.setId(View.generateViewId());
        tvContent.setTextColor(textColor);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tvContent.setText(contentText);
        int vpadding = dip2px(18);
        tvContent.setPaddingRelative(vpadding, vpadding, vpadding, vpadding);
        rel.addView(tvContent);

        //add arrowImage
        arrowImage = new ImageView(mContext);
        arrowImage.setId(View.generateViewId());
        LayoutParams arrowParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        int rotate =0;
        switch (arrowDirection) {
            case TOP:
                rotate = 270;
                relParams.addRule(RelativeLayout.BELOW, arrowImage.getId());
                break;
            case BOTTOM:
                rotate = 90;
                arrowParams.addRule(RelativeLayout.BELOW, rel.getId());
                break;
            case LEFT:
                rotate = 180;
                relParams.addRule(RelativeLayout.END_OF, arrowImage.getId());
                break;
            case RIGHT:
                rotate = 0;
                arrowParams.addRule(RelativeLayout.END_OF, rel.getId());
                break;

        }

        int arrowRes = R.mipmap.ic_arrow;
        Bitmap source = BitmapFactory.decodeResource(this.getResources(), arrowRes);
        Bitmap rotateBitmap = rotateBitmap(source, rotate);
        //tint the bitmap with  backroundColor
        imgDrawable = new TintedBitmapDrawable(mContext.getResources(), rotateBitmap, backroundColor);
        arrowImage.setImageDrawable(imgDrawable);

        this.addView(arrowImage, arrowParams);
        this.addView(rel, relParams);

        //to get the width and height
        post(this);
    }

    private void setRelativePosition(float position) {
        if (position <= 0.2f) {
            relativePosition = 0.2f;
        } else if (position > 0.8f) {
            relativePosition = 0.8f;
        } else {
            relativePosition = position;
        }
    }

    private void setArrowDirection(int direction) {
        switch (direction) {
            case 1: {
                arrowDirection = ArrowDirection.LEFT;
            }
            break;
            case 2: {
                arrowDirection = ArrowDirection.TOP;
            }
            break;
            case 3: {
                arrowDirection = ArrowDirection.RIGHT;
            }
            break;
            case 4: {
                arrowDirection = ArrowDirection.BOTTOM;
            }
            break;
            default:
                arrowDirection = ArrowDirection.LEFT;
        }
    }

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * dp2px
     */
    protected int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     */
    protected int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    public void run() {
        int arrowOffset;
        int conRlw = rel.getWidth();
        int conRlh = rel.getHeight();
        LayoutParams params = (LayoutParams) arrowImage.getLayoutParams();
//        Log.e("arrow width=========", arrowImage.getWidth() + "");
//        Log.e("arrow height=========", arrowImage.getHeight() + "");
        switch (arrowDirection) {
            case TOP:
                arrowOffset = (int) ((conRlw * relativePosition - arrowImage.getWidth() / 2));
                params.setMargins(arrowOffset, arrowImage.getHeight(), 0, 0);
                break;
            case BOTTOM:
                arrowOffset = (int) ((conRlw * relativePosition - arrowImage.getWidth() / 2));
                params.setMargins(arrowOffset, 0, 0, arrowImage.getHeight());
                break;
            case LEFT:
                arrowOffset = (int) ((conRlh * relativePosition - arrowImage.getWidth() / 2));
                params.setMargins(arrowImage.getWidth(), arrowOffset, 0, 0);
                break;
            case RIGHT:
                arrowOffset = (int) ((conRlh * relativePosition - arrowImage.getWidth() / 2));
                params.setMargins(0, arrowOffset, arrowImage.getWidth(), 0);
                break;
        }

    }

    public enum ArrowDirection {
        LEFT, TOP, RIGHT, BOTTOM
    }
}
