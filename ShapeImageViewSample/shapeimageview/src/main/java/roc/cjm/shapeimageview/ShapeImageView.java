package roc.cjm.shapeimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Jiamei Cheng on 2017/7/6.
 */

public class ShapeImageView extends AppCompatImageView {


    private Paint mPaint;
    public static final int TYPE_CIRCLE = 1;
    public static final int TYPE_ROUND_RECT = 2;

    private static final int COLORDRAWABLE_DIMENSION = 2;
    private Bitmap mBmpPhoto;
    private Paint mBackPaint;
    private Paint mCirClePaint;
    private Paint mClearPaint;
    private int mShapeType = 1;
    private int mBottomColor = 0xffffffff;
    private float borderWidth = 0;
    private int borderColor = 0xffffffff;
    private float roundRadiusX = 0f;
    private float roundRadiusY = 0f;
    private RectF roundRectF = new RectF();

    public ShapeImageView(Context context) {
        super(context);
        init(context);
    }

    public ShapeImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShapeImageView);
        mShapeType = a.getInteger(R.styleable.ShapeImageView_siv_shape_type, 1);
        borderWidth = a.getDimensionPixelSize(R.styleable.ShapeImageView_siv_border_width, 0);
        borderColor = a.getColor(R.styleable.ShapeImageView_siv_border_color, context.getResources().getColor(R.color.white));
        mBottomColor = a.getColor(R.styleable.ShapeImageView_siv_background_color, context.getResources().getColor(R.color.white));
        roundRadiusX = a.getDimensionPixelSize(R.styleable.ShapeImageView_siv_round_radius_x, 0);
        roundRadiusY = a.getDimensionPixelSize(R.styleable.ShapeImageView_siv_round_radius_y, 0);
        a.recycle();
        init(context);
    }

    public void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setARGB(255, 255, 225, 255);

        mBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackPaint.setColor(mBottomColor);
        mBackPaint.setStyle(Paint.Style.FILL);

        mCirClePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirClePaint.setColor(borderColor);
        mCirClePaint.setStyle(Paint.Style.STROKE);
        mCirClePaint.setStrokeWidth(borderWidth);

        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    private void initializeBitmap() {

        mBmpPhoto = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    public void setup() {
        if (mBackPaint != null) {
            mBackPaint.setColor(mBottomColor);
            invalidate();
        }
    }

    public float getRoundRadiusX() {
        return roundRadiusX;
    }

    public void setRoundRadiusX(float roundRadiusX) {
        this.roundRadiusX = roundRadiusX;
        invalidate();
    }

    public float getRoundRadiusY() {
        return roundRadiusY;
    }

    public void setRoundRadiusY(float roundRadiusY) {
        this.roundRadiusY = roundRadiusY;
        invalidate();
    }

    public int getmShapeType() {
        return mShapeType;
    }

    public void setmShapeType(int mShapeType) {
        this.mShapeType = mShapeType;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (getHeight() <= 0) {
            return;
        }
        if (mBmpPhoto == null) {
            return;
        }


        Bitmap tmp = null;
        boolean isH = getHeight() > getWidth();
        int wid = (getHeight() > getWidth() ? getWidth() : getHeight());
        int rad = wid / 2;
        switch (mShapeType) {
            case TYPE_CIRCLE:
                Matrix matrix = new Matrix();
                float rate = (wid * 1.0f / (!isH ? mBmpPhoto.getWidth() : mBmpPhoto.getWidth()));
                matrix.postScale(rate, rate);
                mBmpPhoto = Bitmap.createBitmap(mBmpPhoto, 0, 0, mBmpPhoto.getWidth(), mBmpPhoto.getHeight(), matrix, true);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, rad, mBackPaint);
                tmp = getRoundBitmap(mBottomColor, rad);
                matrix.reset();
                matrix.postTranslate(getWidth() / 2 - rad, getHeight() / 2 - rad);
                canvas.drawBitmap(tmp, matrix, null);
                break;
            case TYPE_ROUND_RECT:
                drawScale(canvas);
                break;
            default:
                break;
        }

        switch (mShapeType) {
            case TYPE_CIRCLE:
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, rad, mCirClePaint);
                break;
            case TYPE_ROUND_RECT:
                canvas.drawRoundRect(roundRectF, roundRadiusX, roundRadiusY, mCirClePaint);
                break;
            default:
                break;
        }


    }

    public void drawScale(Canvas canvas) {
        boolean rateTl = (((getHeight() * 1.0f) / (getWidth() * 1.0f)) > ((mBmpPhoto.getHeight() * 1.0f) / (mBmpPhoto.getWidth() * 1.0f)));
        Matrix matrix = new Matrix();
        float rateW = 1.0f;
        float rateH = 1.0f;
        float width = getWidth() * 1.0f;
        float height = getHeight() * 1.0f;
        ScaleType scaleType = getScaleType();
        if (scaleType == ScaleType.CENTER) {

        } else if (scaleType == ScaleType.CENTER_CROP) {
            rateW = width / mBmpPhoto.getWidth();
            rateH = height / mBmpPhoto.getHeight();
            rateH = (rateW > rateH ? rateW : rateH);
            rateW = rateH;
        } else if (scaleType == ScaleType.CENTER_INSIDE) {
            if(width>mBmpPhoto.getWidth() || height>mBmpPhoto.getHeight()){
                rateW = (rateTl ? (width) / mBmpPhoto.getWidth() : (height) / mBmpPhoto.getHeight());
                rateH = rateW;
            }
        } else if (scaleType == ScaleType.FIT_CENTER || scaleType == ScaleType.FIT_END || scaleType == ScaleType.FIT_START) {
            rateW = (rateTl ? (width) / mBmpPhoto.getWidth() : (height) / mBmpPhoto.getHeight());
            rateH = rateW;
        } else if (scaleType == ScaleType.FIT_XY) {
            rateW = width / mBmpPhoto.getWidth();
            rateH = height / mBmpPhoto.getHeight();
        }
        matrix.postScale(rateW, rateH);
        mBmpPhoto = Bitmap.createBitmap(mBmpPhoto, 0, 0, mBmpPhoto.getWidth(), mBmpPhoto.getHeight(), matrix, true);
        matrix.reset();
        if (scaleType == ScaleType.FIT_CENTER || scaleType == ScaleType.FIT_XY || scaleType == ScaleType.CENTER_CROP ||
                scaleType == ScaleType.CENTER || scaleType == ScaleType.CENTER_INSIDE) {
            roundRectF.set((width - mBmpPhoto.getWidth()) / 2, (height - mBmpPhoto.getHeight()) / 2,
                    (width + mBmpPhoto.getWidth()) / 2, (height + mBmpPhoto.getHeight()) / 2);
            matrix.postTranslate((width - mBmpPhoto.getWidth()) / 2, (height - mBmpPhoto.getHeight()) / 2);
        }else  if (scaleType == ScaleType.FIT_END) {
            roundRectF.set(width - mBmpPhoto.getWidth(),height - mBmpPhoto.getHeight(),width,height);
            matrix.postTranslate((width - mBmpPhoto.getWidth()), (height - mBmpPhoto.getHeight()) );
        } else if (scaleType == ScaleType.FIT_START) {
            roundRectF.set(0,0,mBmpPhoto.getWidth(),mBmpPhoto.getHeight());
            matrix.postTranslate(0, 0);
        } else {
            roundRectF.set((width - mBmpPhoto.getWidth()) / 2, (height - mBmpPhoto.getHeight()) / 2,
                    (width + mBmpPhoto.getWidth()) / 2, (height + mBmpPhoto.getHeight()) / 2);
            matrix.postTranslate((width - mBmpPhoto.getWidth()) / 2, (height - mBmpPhoto.getHeight()) / 2);
        }

        roundRectF.left = (roundRectF.left<0?0:roundRectF.left);
        roundRectF.top = (roundRectF.top<0?0:roundRectF.top);
        roundRectF.bottom = (roundRectF.bottom>height?height:roundRectF.bottom);
        roundRectF.right = (roundRectF.right>width?width:roundRectF.right);



        canvas.drawRoundRect(roundRectF, roundRadiusX, roundRadiusY, mBackPaint);
        Bitmap tmp = getRoundRectBitmap(roundRectF, roundRadiusX, roundRadiusY);
        canvas.drawBitmap(tmp, matrix, null);
    }

    public Bitmap getRoundBitmap(int color, int rad) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);

        int wid = mBmpPhoto.getWidth();
        int hei = mBmpPhoto.getHeight();
        Bitmap bmp = Bitmap.createBitmap(rad * 2, rad * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawCircle(mBmpPhoto.getWidth() / 2, mBmpPhoto.getHeight() / 2, rad, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mBmpPhoto, new Matrix(), paint);
        return bmp;
    }

    public Bitmap getRoundRectBitmap(RectF rectF, float radiusX, float radiusY) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mBottomColor);

        Bitmap bmp = Bitmap.createBitmap(mBmpPhoto.getWidth(), mBmpPhoto.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawRoundRect(new RectF((mBmpPhoto.getWidth() - rectF.width())/2, (mBmpPhoto.getHeight() - rectF.height())/2,
                (mBmpPhoto.getWidth() - rectF.width())/2+rectF.width(), (mBmpPhoto.getHeight() - rectF.height())/2+rectF.height()),
                radiusX, radiusY, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mBmpPhoto, new Matrix(), paint);
        return bmp;
    }

}
