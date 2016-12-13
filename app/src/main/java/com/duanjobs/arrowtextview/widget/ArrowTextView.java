package com.duanjobs.arrowtextview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.duanjobs.arrowtextview.R;


/**
 * Created by duanjobs on 16/12/8.
 */
public class ArrowTextView extends TextView {

    protected Context mContext;
    protected float radius;
    protected float   arrowWidth;
    protected int backroundColor;
    protected float arrowHeight;
    protected ArrowDirection arrowDirection;
    protected float relativePosition;


    public ArrowTextView(Context context) {
        this(context,null,0);
    }

    public ArrowTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ArrowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArrowTextView);
        radius = typedArray.getDimension(R.styleable.ArrowTextView_arrowCornerRadius,0);
        arrowWidth=typedArray.getDimension(R.styleable.ArrowTextView_arrowWidth, 0);
        arrowHeight=typedArray.getDimension(R.styleable.ArrowTextView_arrowHeight, 0);
        backroundColor = typedArray.getColor(R.styleable.ArrowTextView_arrowBackgroundColor, Color.GRAY);
        float position=typedArray.getFraction(R.styleable.ArrowTextView_relativePosition,1,1,0.3f);
        setRelativePosition(position);
        int direction = typedArray.getInt(R.styleable.ArrowTextView_arrowDirection,1);
        setArrowDirection(direction);

    }


    @Override
    public int getCompoundPaddingLeft() {
        return arrowDirection == ArrowDirection.LEFT ? super.getCompoundPaddingLeft()+
                (int)arrowHeight:super.getCompoundPaddingLeft();
    }
    @Override
    public int getCompoundPaddingTop() {
        return arrowDirection == ArrowDirection.TOP ? super.getCompoundPaddingTop()+
                (int)arrowHeight:super.getCompoundPaddingTop();
    }
    @Override
    public int getCompoundPaddingRight() {
        return arrowDirection == ArrowDirection.RIGHT ? super.getCompoundPaddingRight()+
                (int)arrowHeight:super.getCompoundPaddingRight();
    }
    @Override
    public int getCompoundPaddingBottom() {
        return arrowDirection == ArrowDirection.BOTTOM ? super.getCompoundPaddingBottom()+
                (int)arrowHeight:super.getCompoundPaddingBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint=new Paint();
        canvas.drawColor(Color.TRANSPARENT);
        paint.setColor(backroundColor);
        paint.setAntiAlias(true);
        int height = getHeight();
        int width = getWidth();

        if(radius==0){
            radius= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        }
        if(arrowWidth==0){
            arrowWidth= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width/90, getResources().getDisplayMetrics());
        }
        if(arrowHeight==0){
            arrowHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,width/90,getResources().getDisplayMetrics());
        }
        Path path=new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        switch (arrowDirection){
            case LEFT:
                canvas.drawRoundRect(new RectF(arrowHeight, 0, width, height), radius, radius, paint);
                float yMiddle = height*relativePosition;
                float yTop=yMiddle-(arrowWidth/2);
                float yBottom=yMiddle+(arrowWidth/2);
                path.moveTo(0, yMiddle);
                path.lineTo(arrowHeight, yTop);
                path.lineTo(arrowHeight, yBottom);
                path.lineTo(0, yMiddle);
                break;
            case TOP:
                canvas.drawRoundRect(new RectF(0, arrowHeight, width, height), radius, radius, paint);
                float xMiddle = width*relativePosition;
                float xTop = xMiddle + (arrowWidth/2);
                float xBottom = xMiddle - (arrowWidth/2);
                path.moveTo(xMiddle,0);
                path.lineTo(xBottom,arrowHeight);
                path.lineTo(xTop,arrowHeight);
                path.lineTo(xMiddle,0);
                break;
            case RIGHT:
                float yMiddler = height*relativePosition;
                float yTopr=yMiddler-(arrowWidth/2);
                float yBottomr=yMiddler+(arrowWidth/2);
                path.moveTo(width, yMiddler);
                path.lineTo(width-arrowHeight, yTopr);
                path.lineTo(width-arrowHeight, yBottomr);
                path.lineTo(width, yMiddler);
                canvas.drawRoundRect(new RectF(0, 0, width-arrowHeight, height), radius, radius, paint);
                break;
            case BOTTOM:
                canvas.drawRoundRect(new RectF(0, 0, width, height-arrowHeight), radius, radius, paint);
                float xMiddleb = width*relativePosition;
                float xTopb = xMiddleb + (arrowWidth/2);
                float xBottomb = xMiddleb - (arrowWidth/2);
                path.moveTo(xMiddleb,height);
                path.lineTo(xBottomb,height-arrowHeight);
                path.lineTo(xTopb,height-arrowHeight);
                path.lineTo(xMiddleb,height);
                break;
        }

        path.close();
        canvas.drawPath(path, paint);
        super.onDraw(canvas);

    }


    private void setRelativePosition(float position){
        if(position<=0.05f){
            relativePosition=0.5f;
        }else if(position>0.95f){
            relativePosition=0.95f;
        }else{
            relativePosition=position;
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
                arrowDirection= ArrowDirection.LEFT;
        }
    }

    /**
     * dp2px
     */
    protected  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     */
    protected  int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public enum ArrowDirection {
        LEFT, TOP, RIGHT, BOTTOM
    }

}
