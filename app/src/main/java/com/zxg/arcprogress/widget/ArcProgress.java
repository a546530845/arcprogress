package com.zxg.arcprogress.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.zxg.arcprogress.R;

import static android.R.attr.y;


/**
 * 首页圆圈动画
 */
public class ArcProgress extends View {
    /**
     * 圆弧画笔，已完成和未完成
     */
    private Paint paint;
    /**
     * 金额和可借额度文字的画笔
     */
    protected Paint textPaint;
    /**
     * 外圈圆环画笔
     */
    private Paint circlePaint;
    /**
     * 图片的bitmap画笔
     */
    private Paint mBitmapPaint;

    private RectF rectF = new RectF();
    /**
     * 内圆环实际的实线宽度
     */
    private float inside_arc_strokeWidth;
    /**
     * 外圆线的宽度
     */
    private float outside_circle_strokeWidth;
    /**
     * 外圆线的宽度单位是dp
     */
    private float default_outside_circle_stroke_width = 1;

    private float bottomTextSize;
    private String bottomText;
    /**
     * 金额的文字大小
     */
    private float textSize;
    /**
     * 金额的文字大小，初始化的时候得到，用于invalidate恢复使用
     */
    private float textSize_first;
    private int textColor;
    private int bottom_text_color;
    private int progress = 0;
    private int running_progress = 0;
    private double money = 0;
    /**
     * 金额能达到100份中的多少分
     */
    private int progress_max_from_outside = 0;
    private int max;
    private int finishedStrokeColor;
    private int unfinishedStrokeColor;
    /**
     * 外圆框的颜色
     */
    private int circle_stroke_color;
    /**
     * 圆环闭合度
     */
    private float arcAngle;
    /**
     * 外圆的半径
     */
    private float outside_circle_Radius;
    private float cx;
    private float cy;

    private final int default_finished_color = Color.WHITE;
    /**
     * #486AB0，默认未完成颜色
     */
//    private final int default_unfinished_color = Color.rgb(72, 106, 176);
    private final int default_unfinished_color = Color.parseColor("#486AB0");
    //外圆框默认颜色
    private final int default_circle_stroke_color = Color.parseColor("#aaaaaa");
    /**
     * #4291F1，默认金额颜色
     */
//    private final int default_text_color = Color.rgb(66, 145, 241);
    private final int default_text_color = Color.parseColor("#4291F1");
    //默认底部可借额度的文字大小
    private final float default_bottom_text_size;
    private final float default_bottom_text_size_dp = 10;
    //
    private final float default_inside_arc_stroke_width;
    private final float default_inside_arc_stroke_width_dp = 4;
    /**
     * 默认圆环分成100份
     */
    private final int default_max = 100;
    /**
     * 默认已完成程度
     */
    private final int default_progress = 50;
    /**
     * 默认圆环闭合程度
     */
    private final float default_arc_angle = 270 * 1.0f;
    /**
     * 两个圆环之间的实际距离dp
     */
    private float two_marging_width;
    private final float default_two_marging_width_dp = 20;

    /**
     * 点和圆弧之间的距离
     */
    private float point_marging_arc_width;
    private final float default_point_marging_arc_width = 3;
    /**
     * 点和金额文字之间的距离
     */
    private float point_marging_text_width;
    private final float default_point_marging_text_width = 3;

    /**
     * 默认中间金额的文字大小
     */
    private float default_text_size;
    private float default_text_size_dp = 40;
    /**
     * 是否需要小数点后两位
     */
    private boolean is_need_twopoint;
    private boolean default_is_need_twopoint = true;

    /**
     * 中间金额地段高度
     */
    private double text_height_level = 0.45;
    private double bottom_text_height_level = 0.64;
    private double bottom_bitmap_level = 0.8;


    //
    private final int min_size;
    private final int min_size_dp = 100;

    /**
     * 大树贷图片
     */
    private Bitmap bitmap;
    private int bottom_image;
    private int default_bottom_image = R.mipmap.arcprogress_debt;
    /**
     * 转圈的小圆点
     */
    private Bitmap bitmap_point;
    private int point_image;
    private int default_point_image = R.mipmap.arcprogress_fgt_point;

    private float bt_left_x;
    private float bt_right_x;
    private float bt_top_y;
    private float bt_bottom_y;

    public int getRunning_progress() {
        return running_progress;
    }

    public void setRunning_progress(int running_progress) {
        this.running_progress = running_progress;
    }

    public ArcProgress(Context context) {
        this(context, null);
    }

    public ArcProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        min_size = (int) dip2px(context, min_size_dp);
        default_text_size = sp2px(context, default_text_size_dp);
        default_bottom_text_size = sp2px(context, default_bottom_text_size_dp);
        default_inside_arc_stroke_width = dip2px(context, default_inside_arc_stroke_width_dp);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

    /**
     * 获取xml中设置的属性值
     *
     * @param attributes
     */
    protected void initByAttributes(TypedArray attributes) {
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, default_finished_color);
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, default_unfinished_color);
        circle_stroke_color = attributes.getColor(R.styleable.ArcProgress_circle_stroke_color, default_circle_stroke_color);
        textColor = attributes.getColor(R.styleable.ArcProgress_arc_text_color, default_text_color);
        bottom_text_color = attributes.getColor(R.styleable.ArcProgress_arc_bottom_text_color, default_text_color);
        textSize = attributes.getDimension(R.styleable.ArcProgress_arc_text_size, default_text_size);
        //保存最初设置的字体大小，可用于重画
        textSize_first = textSize;
        arcAngle = attributes.getFloat(R.styleable.ArcProgress_arc_angle, default_arc_angle);
        max = attributes.getInt(R.styleable.ArcProgress_arc_max, default_max);
        progress = attributes.getInt(R.styleable.ArcProgress_arc_progress, default_progress);
        inside_arc_strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_inside_arc_stroke_width, default_inside_arc_stroke_width);
        outside_circle_strokeWidth = attributes.getDimension(R.styleable.ArcProgress_circle_outside_circle_stroke_width, default_outside_circle_stroke_width);
        bottomTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_bottom_text_size, default_bottom_text_size);
        bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text);
        two_marging_width = attributes.getDimension(R.styleable.ArcProgress_two_marging_width, default_two_marging_width_dp);
        point_marging_arc_width = attributes.getDimension(R.styleable.ArcProgress_point_marging_arc_width, default_point_marging_arc_width);
        point_marging_text_width = attributes.getDimension(R.styleable.ArcProgress_point_marging_text_width, default_point_marging_text_width);
        is_need_twopoint = attributes.getBoolean(R.styleable.ArcProgress_is_need_twopoint, default_is_need_twopoint);
        bottom_image = attributes.getResourceId(R.styleable.ArcProgress_bottom_image, default_bottom_image);
        point_image = attributes.getResourceId(R.styleable.ArcProgress_point_image, default_point_image);
    }

    /**
     * 实例化画笔
     */
    protected void initPainters() {
        //实例化文字画笔
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        //实例化圆弧画笔
        paint = new Paint();
        paint.setColor(default_unfinished_color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(inside_arc_strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        //实例化圆环画笔
        circlePaint = new Paint();
        circlePaint.setColor(circle_stroke_color);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(outside_circle_strokeWidth);
        circlePaint.setStyle(Paint.Style.STROKE);

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    public float getInside_arc_strokeWidth() {
        return inside_arc_strokeWidth;
    }

    public void setInside_arc_strokeWidth(float inside_arc_strokeWidth) {
        this.inside_arc_strokeWidth = inside_arc_strokeWidth;
        this.invalidate();
    }


    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
        this.invalidate();
    }

    public int getProgress() {
        return progress;
    }

    /**
     * 运行的速度ms，默认50ms
     */
    private int ms_spleed = 50;
    private int default_ms_spleed = 50;
    /**
     * 总共运行的时间ms
     */
    private int total_second = 1000;

    /**
     * 开始绘制，将动画设置到自定义view中
     */
    public void startDraw(int progress, double money, int progress_max_from_outside) {
        this.progress = progress;
        this.money = money;
        this.progress_max_from_outside = progress_max_from_outside;
        //因为中间绘图和运算会花费时间，所以控制不了整个转圈完成时间，所以就不计算速度了
        if (getProgress() != 0) {
            ms_spleed = (int) (total_second / getProgress());
            if (ms_spleed >= default_ms_spleed) {
                ms_spleed = default_ms_spleed;
            }
        }

        mHandler.postDelayed(progressTask, ms_spleed);
    }

    public void startDraw(int progress, double money) {
        this.progress = progress;
        this.money = money;
        this.progress_max_from_outside = 0;
        if (getProgress() != 0) {
            ms_spleed = (int) (total_second / getProgress());
            if (ms_spleed >= default_ms_spleed) {
                ms_spleed = default_ms_spleed;
            }
        }

        mHandler.postDelayed(progressTask, ms_spleed);
    }

    private Runnable progressTask = new Runnable() {

        @Override
        public void run() {
            running_progress += 1;
            if (running_progress <= getProgress()) {
                mHandler.sendMessage(mHandler.obtainMessage(0, running_progress, 0));
            } else {
                running_progress = 0;
            }

        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startDrawing();
            mHandler.postDelayed(progressTask, ms_spleed);
            Log.e("ms_spleed=",ms_spleed+"");
        }
    };

    /**
     * 开始绘制
     */
    public void startDrawing() {
        caculateTextSize();
        invalidate();
    }

    /**
     * 在画出text之前计算出text可以设置的textSize
     */
    public void caculateTextSize() {
        //获取字体大小前置，先算出字体可以设置多大，然后再draw,在ondraw中，会有迟缓
        if (textSize_first > textSize) {
            textSize = textSize_first;
        }
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        String text = getTextString();
        //金额的写入大小范围
        float max_text_width = (float) (getWidth() * text_height_level - outside_circle_strokeWidth - inside_arc_strokeWidth - two_marging_width - point_marging_arc_width - point_marging_text_width) * 2;

        if (!TextUtils.isEmpty(text)) {
            Log.e("开始setProgress=", textPaint.measureText(text) + "----" + textPaint.getTextSize() + "----" + max_text_width);
            while (textPaint.measureText(text) >= max_text_width) {
                textSize--;
                textPaint.setColor(textColor);
                textPaint.setTextSize(textSize);
                Log.e("字体大小=", textSize + "");
            }
        }
    }

    /**
     * 获取需要画出来的text(金额)
     *
     * @return
     */
    public String getTextString() {

        //设置金额,当getProgress_max_from_outside()==0的时候就是未传入此值
        float devideNum = getRunning_progress() / (float) (getProgress_max_from_outside() == 0 ? getMax() : getProgress_max_from_outside());

        //科学计数法，保留两位小数（为后面的截取做准备）
        String result = DecimalUtil.getMoneyDecimal_twoPoint(getMoney() * devideNum);

        String text = "";
        if (!is_need_twopoint) {
            // 去除小数点
            int idx = result.lastIndexOf(".");//查找小数点的位置
            text = result.substring(0, idx);
        } else {
            //保留小数点
            text = result;//保留小数位
        }
        return text;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    public float getBottomTextSize() {
        return bottomTextSize;
    }

    public void setBottomTextSize(float bottomTextSize) {
        this.bottomTextSize = bottomTextSize;
        this.invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        this.invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.invalidate();
    }

    public int getFinishedStrokeColor() {
        return finishedStrokeColor;
    }

    public void setFinishedStrokeColor(int finishedStrokeColor) {
        this.finishedStrokeColor = finishedStrokeColor;
        this.invalidate();
    }

    public int getUnfinishedStrokeColor() {
        return unfinishedStrokeColor;
    }

    public void setUnfinishedStrokeColor(int unfinishedStrokeColor) {
        this.unfinishedStrokeColor = unfinishedStrokeColor;
        this.invalidate();
    }

    public float getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
        this.invalidate();
    }

    public int getProgress_max_from_outside() {
        return progress_max_from_outside;
    }

    public void setProgress_max_from_outside(int progress_max_from_outside) {
        this.progress_max_from_outside = progress_max_from_outside;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return min_size;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return min_size;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		rectF.set(inside_arc_strokeWidth / 2f, inside_arc_strokeWidth / 2f, MeasureSpec.getSize(widthMeasureSpec) - inside_arc_strokeWidth / 2f, MeasureSpec.getSize(heightMeasureSpec)
//				- inside_arc_strokeWidth / 2f);
        //新版圆弧大小
//        rectF.set(inside_arc_strokeWidth / 2f + two_marging_width, inside_arc_strokeWidth / 2f + two_marging_width, MeasureSpec.getSize(widthMeasureSpec) - inside_arc_strokeWidth / 2f - two_marging_width, MeasureSpec.getSize(heightMeasureSpec)
//                - inside_arc_strokeWidth / 2f - two_marging_width);
//        float radius = getWidth() / 2f;//内圆的半径
//        outside_circle_Radius = (float) (getWidth() / 2)-dp2px(this.getContext(),default_outside_strokeWidth);//外圆的半径
//        cx = getWidth() / 2;
//        cy = getHeight() / 2;
        // float angle = (360 - arcAngle) / 2f;
        // arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 *
        // Math.PI));
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = 270 - arcAngle / 2f;
        float finishedSweepAngle = getRunning_progress() / (float) getMax() * arcAngle;

        //显示出距离，在三星上onmeasure中显示不了,必须先设置上大小，圆弧的矩形
        rectF.set(inside_arc_strokeWidth / 2f + two_marging_width,
                inside_arc_strokeWidth / 2f + two_marging_width,
                getWidth() - inside_arc_strokeWidth / 2f - two_marging_width,
                getHeight() - inside_arc_strokeWidth / 2f - two_marging_width);

        outside_circle_Radius = (float) (getWidth() / 2) - outside_circle_strokeWidth;//外圆的半径
        cx = getWidth() / 2;
        cy = getHeight() / 2;

        if (getRunning_progress() > getMax()) {
            finishedSweepAngle = arcAngle;
        }
        float finishedStartAngle = startAngle;
        paint.setColor(unfinishedStrokeColor);
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint);
        paint.setColor(finishedStrokeColor);
        canvas.drawArc(rectF, finishedStartAngle, finishedSweepAngle, false, paint);
        canvas.drawCircle(cx, cy, outside_circle_Radius, circlePaint);

        //画出图片
        bitmap = BitmapFactory.decodeResource(getResources(), bottom_image);
        bt_left_x = (getWidth() - bitmap.getWidth()) / 2.0f;
        bt_right_x = (float) (getWidth() + bitmap.getWidth()) / 2.0f;
        bt_top_y = (float) ((getHeight() * bottom_bitmap_level) - (bitmap.getHeight()) / 2);
        bt_bottom_y = (float) ((getHeight() * bottom_bitmap_level) + (bitmap.getHeight())) / 2.0f;
        float bottombitmapBaseline = (float) ((getHeight() * bottom_bitmap_level) - (bitmap.getHeight()) / 2);
        canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2.0f, bottombitmapBaseline, mBitmapPaint);

        //画出圆点
        bitmap_point = BitmapFactory.decodeResource(getResources(), point_image);


        String text = getTextString();
        //金额的写入大小范围
        float max_text_width = (float) (getWidth() * text_height_level - outside_circle_strokeWidth - inside_arc_strokeWidth - two_marging_width - point_marging_arc_width - point_marging_text_width) * 2;

        if (!TextUtils.isEmpty(text)) {
            Log.e("范围大小=", textPaint.measureText(text) + "---" + max_text_width);
            float textHeight = textPaint.descent() + textPaint.ascent();
            float textBaseline = (float) ((getHeight() - textHeight) * text_height_level);//圆圈的中间
            canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, textBaseline, textPaint);
        }

        if (!TextUtils.isEmpty(getBottomText())) {
            textPaint.setTextSize(bottomTextSize);
            textPaint.setColor(bottom_text_color);
            float bottomTextBaseline = (float) ((getHeight() * bottom_text_height_level) - (textPaint.descent() + textPaint.ascent()) / 2);
            canvas.drawText(getBottomText(), (getWidth() - textPaint.measureText(getBottomText())) / 2.0f, bottomTextBaseline, textPaint);
        }


        //圆点运行范围
        Rect rMoon = new Rect((int) (getWidth() - bitmap_point.getWidth() / 2 - outside_circle_strokeWidth - inside_arc_strokeWidth - two_marging_width - point_marging_arc_width),
                (int) (getHeight() / 2 - bitmap_point.getHeight() / 2),
                (int) (getWidth() - outside_circle_strokeWidth - inside_arc_strokeWidth - two_marging_width - point_marging_arc_width + bitmap_point.getWidth() / 2),
                (int) (getHeight() / 2 + bitmap_point.getHeight() / 2));


        Log.d("宽度1测试=", dip2px(this.getContext(), 2) + "");
        Log.d("宽度=", outside_circle_strokeWidth + inside_arc_strokeWidth + two_marging_width + point_marging_arc_width + "");
        Log.d("left=", rMoon.left + "");
        Log.d("top=", rMoon.top + "");
        Log.d("right=", rMoon.right + "");
        Log.d("bottom=", rMoon.bottom + "");

        canvas.rotate(startAngle + finishedSweepAngle, getWidth() / 2, getHeight() / 2);
        canvas.drawBitmap(bitmap_point, null, rMoon, null);


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float Y = event.getY();
                Log.e("x= y = ", x + "-----" + y);
                if (x < bt_right_x && x > bt_left_x && y < bt_bottom_y && y > bt_top_y) {
                    Toast.makeText(this.getContext(), "我点击了大树贷跳转", Toast.LENGTH_LONG).show();
                }

//                Log.e("x= y = ",x+"-----"+y);
//                Toast.makeText(this.getContext(),"x= y="+x+"-----"+y,Toast.LENGTH_LONG).show();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 根据手机的分辨率dp 转成px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率px(像素) 转成dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static float sp2px(Context context, float sp){
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
