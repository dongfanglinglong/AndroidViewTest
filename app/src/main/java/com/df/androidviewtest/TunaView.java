package com.df.androidviewtest;

import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Property;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * @author Tunasashimi
 * @date Jun 25, 2015 10:56:04 AM
 * @Copyright 2015 TunaSashimi. All rights reserved.
 * @Description
 */

public class TunaView extends View {
    public static String getMemoryInfo() {
        String str1 = "/proc/meminfo";
        String str2 = "";
        StringBuffer stringBuffer = new StringBuffer(28);
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                stringBuffer.append(str2 + "\n");
            }
            localBufferedReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    //getRAM
    public static long getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    public static long getTotalInternalStorgeSize() {
        File path = Environment.getDataDirectory();
        StatFs mStatFs = new StatFs(path.getPath());
        long blockSize = mStatFs.getBlockSize();
        long totalBlocks = mStatFs.getBlockCount();
        return totalBlocks * blockSize;
    }

    public static long getAvailableInternalStorgeSize() {
        File path = Environment.getDataDirectory();
        StatFs mStatFs = new StatFs(path.getPath());
        long blockSize = mStatFs.getBlockSize();
        long availableBlocks = mStatFs.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static long getTotalExternalStorgeSize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs mStatFs = new StatFs(path.getPath());
            long blockSize = mStatFs.getBlockSize();
            long totalBlocks = mStatFs.getBlockCount();
            return totalBlocks * blockSize;
        }
        return 0;
    }

    public static long getAvailableExternalStorgeSize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs mStatFs = new StatFs(path.getPath());
            long blockSize = mStatFs.getBlockSize();
            long availableBlocks = mStatFs.getAvailableBlocks();
            return availableBlocks * blockSize;
        }
        return 0;
    }

    /**
     * These are public static methods
     */
    public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        switch (unit) {
        case TypedValue.COMPLEX_UNIT_PX:
            return value;
        case TypedValue.COMPLEX_UNIT_DIP:
            return value * metrics.density;
        case TypedValue.COMPLEX_UNIT_SP:
            return value * metrics.scaledDensity;
        case TypedValue.COMPLEX_UNIT_PT:
            return value * metrics.xdpi * (1.0f / 72);
        case TypedValue.COMPLEX_UNIT_IN:
            return value * metrics.xdpi;
        case TypedValue.COMPLEX_UNIT_MM:
            return value * metrics.xdpi * (1.0f / 25.4f);
        }
        return 0;
    }

    //
    public static Resources getViewResources(View view) {
        Resources resources;
        Context context = view.getContext();
        if (context == null) {
            resources = Resources.getSystem();
        }
        else {
            resources = context.getResources();
        }
        return resources;
    }

    //
    public static DisplayMetrics getViewDisplayMetrics(View view) {
        return getViewResources(view).getDisplayMetrics();
    }

    //
    public static void setViewMargins(View view, int left, int top, int right, int bottom) {
        setViewMargins(view, TypedValue.COMPLEX_UNIT_DIP, left, top, right, bottom);
    }

    public static void setViewMargins(View view, int unit, int left, int top, int right, int bottom) {
        DisplayMetrics displayMetrics = getViewDisplayMetrics(view);

        setViewMarginsRaw(view, (int) applyDimension(unit, left, displayMetrics), (int) applyDimension(unit, top, displayMetrics), (int) applyDimension(unit, right, displayMetrics),
                (int) applyDimension(unit, bottom, displayMetrics));
    }

    //
    private static void setViewMarginsRaw(View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

    public static int getScreenWidth(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getWidth();
    }

    public static int getScreenHeight(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getHeight();
    }

    public static float getScreenDensity(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.density;
    }

    // The upper left corner of the view coordinate into an array, in hiding the
    // status bar / title bar case, their height calculated by 0.
    public static int getLocationOnScreenX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0];
    }

    public static int getLocationOnScreenY(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }

    // The common activity, y coordinates as the visible state bar height +
    // visible on the upper left corner of the title bar of view height to the
    // title bar at the bottom of the distance.
    public static int getLocationInWindowX(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location[0];
    }

    public static int getLocationInWindowY(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location[1];
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public static int getTitleBarHeight(Activity activity) {
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentTop - getStatusBarHeight(activity);
    }

    //
    public static void setLayoutByWidth(View view, float width) {
        setLayoutByWidth(view, TypedValue.COMPLEX_UNIT_DIP, width);
    }

    public static void setLayoutByWidth(View view, int unit, float width) {
        setLayoutByWidthRaw(view, applyDimension(unit, width, getViewDisplayMetrics(view)));
    }

    public static void setLayoutByWidthRaw(View view, float width) {
        LayoutParams params = view.getLayoutParams();
        params.width = (int) (width);
        view.setLayoutParams(params);
    }

    //
    public static void setLayoutByHeight(View view, float height) {
        setLayoutByHeight(view, TypedValue.COMPLEX_UNIT_DIP, height);
    }

    public static void setLayoutByHeight(View view, int unit, float height) {
        setLayoutByHeightRaw(view, applyDimension(unit, height, getViewDisplayMetrics(view)));
    }

    public static void setLayoutByHeightRaw(View view, float height) {
        LayoutParams params = view.getLayoutParams();
        params.height = (int) (height);
        view.setLayoutParams(params);
    }

    //
    public static void setLayout(View view, float width, float height) {
        setLayout(view, TypedValue.COMPLEX_UNIT_DIP, width, height);
    }

    public static void setLayout(View view, int unit, float width, float height) {
        DisplayMetrics displayMetrics = getViewDisplayMetrics(view);
        setLayoutRaw(view, applyDimension(unit, width, displayMetrics), applyDimension(unit, height, displayMetrics));
    }

    private static void setLayoutRaw(View view, float width, float height) {
        LayoutParams params = view.getLayoutParams();
        params.width = (int) (width);
        params.height = (int) (height);
        view.setLayoutParams(params);
    }

    public static void adjustViewHeightByWidth(final View view, final float ratio) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //

                LayoutParams params = view.getLayoutParams();
                int width = view.getWidth();
                params.height = (int) (width / ratio);
                view.setLayoutParams(params);
            }
        });
    }

    public static void adjustViewWidthByHeight(final View view, final float ratio) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //

                LayoutParams params = view.getLayoutParams();
                int height = view.getHeight();
                params.width = (int) (height * ratio);
                view.setLayoutParams(params);
            }
        });
    }

    public static void adaptViewAutomatic(View view, int screenWidth, int screenHeight, float ratio) {
        LayoutParams params = view.getLayoutParams();
        if (screenWidth * 1f / screenHeight >= ratio) {
            params.height = screenHeight;
            params.width = (int) (screenHeight * ratio);
        }
        else {
            params.width = screenWidth;
            params.height = (int) (screenWidth / ratio);
        }
        view.setLayoutParams(params);
    }

    public static void fillViewAutomatic(View view, int screenWidth, int screenHeight, float ratio) {
        LayoutParams params = view.getLayoutParams();
        if (screenWidth * 1f / screenHeight >= ratio) {
            params.width = screenWidth;
            params.height = (int) (screenWidth / ratio);
        }
        else {
            params.height = screenHeight;
            params.width = (int) (screenHeight * ratio);
        }
        view.setLayoutParams(params);
    }

    public static void keepListViewLocation(ListView listView) {
        int index = listView.getFirstVisiblePosition();
        View view = listView.getChildAt(0);
        int top = (view == null) ? 0 : view.getTop();
        listView.setSelectionFromTop(index + 1, top);
    }


    public static LinearGradient getLinearGradient(int width, int height, int angle, int gradientStart, int gradientEnd) {
        if (angle % 45 != 0) {
            throw new IllegalArgumentException("Angle value must be a multiple of 45");
        }
        LinearGradient linearGradient = null;
        int quotient = angle / 45;
        int remainder = quotient % 8;
        if (remainder < 0) {
            remainder += 8;
        }
        switch (remainder) {
        case 0:
            linearGradient = new LinearGradient(0, 0, width, 0, gradientStart, gradientEnd, Shader.TileMode.CLAMP);
            break;
        case 1:
            linearGradient = new LinearGradient(0, height, width, 0, gradientStart, gradientEnd, Shader.TileMode.CLAMP);
            break;
        case 2:
            linearGradient = new LinearGradient(0, height, 0, 0, gradientStart, gradientEnd, Shader.TileMode.CLAMP);
            break;
        case 3:
            linearGradient = new LinearGradient(width, height, 0, 0, gradientStart, gradientEnd, Shader.TileMode.CLAMP);
            break;
        case 4:
            linearGradient = new LinearGradient(width, 0, 0, 0, gradientStart, gradientEnd, Shader.TileMode.CLAMP);
            break;
        case 5:
            linearGradient = new LinearGradient(width, 0, 0, height, gradientStart, gradientEnd, Shader.TileMode.CLAMP);
            break;
        case 6:
            linearGradient = new LinearGradient(0, 0, 0, height, gradientStart, gradientEnd, Shader.TileMode.CLAMP);
            break;
        case 7:
            linearGradient = new LinearGradient(0, 0, width, height, gradientStart, gradientEnd, Shader.TileMode.CLAMP);
            break;
        default:
            break;
        }
        return linearGradient;
    }

    public static Bitmap getCustomRoundBitmap(Bitmap sourceBitmap, float radiusLeftTop, float radiusLeftBottom, float radiusRightTop, float radiusRightBottom) {
        Bitmap roundCustomBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(roundCustomBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Rect rect = new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        RectF rectF = new RectF(rect);

        Path path = new Path();
        float[] radii = {radiusLeftTop, radiusLeftTop, radiusRightTop, radiusRightTop, radiusRightBottom, radiusRightBottom, radiusLeftBottom, radiusLeftBottom};
        path.addRoundRect(rectF, radii, Path.Direction.CW);

        canvas.drawPath(path, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(sourceBitmap, rect, rect, paint);
        return roundCustomBitmap;
    }

    public static Bitmap getClassicRoundBitmap(Bitmap sourceBitmap, float radius) {
        Bitmap classicRoundBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(classicRoundBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Rect rect = new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        RectF rectF = new RectF(rect);

        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(sourceBitmap, rect, rect, paint);
        return classicRoundBitmap;
    }

    public static Bitmap getScaleBitmap(Bitmap sourceBitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        Bitmap bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, false);
        return bitmap;
    }

    public static Bitmap getAlphaBitmap(Bitmap sourceBitmap, float fraction) {
        int[] argb = new int[sourceBitmap.getWidth() * sourceBitmap.getHeight()];

        sourceBitmap.getPixels(argb, 0, sourceBitmap.getWidth(), 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());

        int number = (int) (fraction * 255);
        for (int i = 0; i < argb.length; i++) {
            if (argb[i] != 0) {
                argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
            }
            else {
                argb[i] = 0x00000000;
            }
        }
        sourceBitmap = Bitmap.createBitmap(argb, sourceBitmap.getWidth(), sourceBitmap.getHeight(), Config.ARGB_8888);
        return sourceBitmap;
    }

    //
    public static final String GRAPHICSTYPE_UNKNOWN = "Unknown";
    public static final String GRAPHICSTYPE_GIF = "GIF";
    public static final String GRAPHICSTYPE_PNG = "PNG";
    public static final String GRAPHICSTYPE_JPG = "JPG";

    public static String getGraphicsType(File file) {
        String type = GRAPHICSTYPE_UNKNOWN;
        FileInputStream graphicsFile = null;
        byte[] b = new byte[10];
        int l = -1;
        try {
            graphicsFile = new FileInputStream(file);
            l = graphicsFile.read(b);
            graphicsFile.close();
        }
        catch (Exception e) {
            return type;
        }
        if (l == 10) {
            byte b0 = b[0];
            byte b1 = b[1];
            byte b2 = b[2];
            byte b3 = b[3];
            byte b6 = b[6];
            byte b7 = b[7];
            byte b8 = b[8];
            byte b9 = b[9];
            // GIF
            if (b0 == (byte) 'G' && b1 == (byte) 'I' && b2 == (byte) 'F') {
                type = GRAPHICSTYPE_GIF;
                // PNG
            }
            else if (b1 == (byte) 'P' && b2 == (byte) 'N' && b3 == (byte) 'G') {
                type = GRAPHICSTYPE_PNG;
                // JPG
            }
            else if (b6 == (byte) 'J' && b7 == (byte) 'F' && b8 == (byte) 'I' && b9 == (byte) 'F') {
                type = GRAPHICSTYPE_JPG;
            }
        }
        return type;
    }

    public static String getGraphicsType(String path) {
        String type = GRAPHICSTYPE_UNKNOWN;
        FileInputStream graphicsFile = null;
        byte[] b = new byte[10];
        int l = -1;
        try {
            graphicsFile = new FileInputStream(path);
            l = graphicsFile.read(b);
            graphicsFile.close();
        }
        catch (Exception e) {
            return type;
        }
        if (l == 10) {
            byte b0 = b[0];
            byte b1 = b[1];
            byte b2 = b[2];
            byte b3 = b[3];
            byte b6 = b[6];
            byte b7 = b[7];
            byte b8 = b[8];
            byte b9 = b[9];
            // GIF

            if (b0 == (byte) 'G' && b1 == (byte) 'I' && b2 == (byte) 'F') {
                type = GRAPHICSTYPE_GIF;
                // PNG
            }
            else if (b1 == (byte) 'P' && b2 == (byte) 'N' && b3 == (byte) 'G') {
                type = GRAPHICSTYPE_PNG;
                // JPG
            }
            else if (b6 == (byte) 'J' && b7 == (byte) 'F' && b8 == (byte) 'I' && b9 == (byte) 'F') {
                type = GRAPHICSTYPE_JPG;
            }
        }
        return type;
    }

    public Bitmap decodeBitmapResource(int id) {
        return decodeBitmapResource(id, 1);
    }

    //
    public Bitmap decodeBitmapResource(int id, int inSampleSize) {
        String stringId = String.valueOf(id);
        if (tunaGraphicsMap.containsKey(stringId)) {
            Object object = tunaGraphicsMap.get(stringId);
            if (object != null && object instanceof Bitmap) {
                return (Bitmap) object;
            }
        }
        Bitmap bitmap;
        if (inSampleSize > 1) {
            BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
            bitmapFactoryOptions.inSampleSize = inSampleSize;
            bitmap = BitmapFactory.decodeResource(getResources(), id, bitmapFactoryOptions);
        }
        else {
            bitmap = BitmapFactory.decodeResource(getResources(), id);
        }
        tunaGraphicsMap.put(stringId, bitmap);
        return bitmap;
    }

    //
    public Movie decodeGifResource(int id) {
        String stringId = String.valueOf(id);
        if (tunaGraphicsMap.containsKey(stringId)) {
            Object object = tunaGraphicsMap.get(stringId);
            if (object != null && object instanceof Movie) {
                return (Movie) object;
            }
        }
        return Movie.decodeStream(getResources().openRawResource(id));
    }


    //
    public Object decodeGraphicsResource(int id) {
        return decodeGraphicsResource(id, 1);
    }

    //
    public Object decodeGraphicsResource(int id, int inSampleSize) {
        String stringId = String.valueOf(id);
        if (tunaGraphicsMap.containsKey(stringId)) {
            Object object = tunaGraphicsMap.get(stringId);
            if (object != null) {
                return object;
            }
        }
        Movie movie = decodeGifResource(id);
        if (movie != null) {
            return movie;
        }
        else {
            return decodeBitmapResource(id, inSampleSize);
        }
    }

    //
    public Bitmap decodeBitmapFile(String path) {
        return decodeBitmapFile(path, 0, 0);
    }

    //
    public Bitmap decodeBitmapFile(String path, int reqWidth, int reqHeight) {
        if (tunaGraphicsMap.containsKey(path)) {
            Object object = tunaGraphicsMap.get(path);
            if (object != null && object instanceof Bitmap) {
                return (Bitmap) object;
            }
        }
        Bitmap bitmap;
        if (reqWidth == 0 || reqHeight == 0) {
            bitmap = BitmapFactory.decodeFile(path);
        }
        else {
            BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
            bitmapFactoryOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, bitmapFactoryOptions);
            bitmapFactoryOptions.inSampleSize = computeSampleSize(bitmapFactoryOptions, -1, reqWidth * reqHeight);
            bitmapFactoryOptions.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(path, bitmapFactoryOptions);
        }
        tunaGraphicsMap.put(path, bitmap);
        return bitmap;
    }

    //
    public Movie decodeGifFile(String path) {
        if (tunaGraphicsMap.containsKey(path)) {
            Object object = tunaGraphicsMap.get(path);
            if (object != null && object instanceof Movie) {
                return (Movie) object;
            }
        }
        return Movie.decodeFile(path);
    }

    //
    public Object decodeGraphicsFile(String path) {
        if (tunaGraphicsMap.containsKey(path)) {
            Object object = tunaGraphicsMap.get(path);
            if (object != null) {
                return object;
            }
        }
        Movie movie = Movie.decodeFile(path);
        if (movie != null) {
            return movie;
        }
        else {
            return decodeBitmapFile(path);
        }
    }

    //
    public Object decodeGraphicsFile(String path, int reqWidth, int reqHeight) {
        if (tunaGraphicsMap.containsKey(path)) {
            Object object = tunaGraphicsMap.get(path);
            if (object != null) {
                return object;
            }
        }

        Movie movie = Movie.decodeFile(path);
        if (movie != null) {
            return movie;
        }
        else {
            return decodeBitmapFile(path, reqWidth, reqHeight);
        }
    }

    //
    public int computeSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int initialSize = computeInitialSampleSize(options, reqWidth, reqHeight);
        int inSampleSize;
        if (initialSize <= 8) {
            inSampleSize = 1;
            while (inSampleSize < initialSize) {
                inSampleSize <<= 1;
            }
        }
        else {
            inSampleSize = (initialSize + 7) / 8 * 8;
        }
        return inSampleSize;
    }

    private int computeInitialSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (reqHeight == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / reqHeight));
        int upperBound = (reqWidth == -1) ? 128 : (int) Math.min(Math.floor(w / reqWidth), Math.floor(h / reqWidth));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((reqHeight == -1) && (reqWidth == -1)) {
            return 1;
        }
        else if (reqWidth == -1) {
            return lowerBound;
        }
        else {
            return upperBound;
        }
    }

    public Bitmap createImageThumbnail(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
        opts.inJustDecodeBounds = false;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * The following fields and methods of the parent class and subclass can always use
     */

    // as the mark of tunaView species
    protected String tunaTag = TunaView.class.getSimpleName();

    // the width and height of the tunaView(put together to save the number of rows)
    protected int tunaWidth, tunaHeight;

    //
    protected int tunaLayer;
    protected int tunaTotal;

    protected Bitmap tunaSrcBitmap;
    protected HashMap<String, Object> tunaGraphicsMap = new HashMap<String, Object>();

    protected float tunaSrcWidthScale, tunaSrcHeightScale;

    protected float tunaCenterX, tunaCenterY;
    protected float tunaDx, tunaDy;
    protected float tunaScale, tunaScaleSx, tunaScaleSy;

    protected float tunaPercent;
    protected float tunaSurplus, tunaShare;

    //
    protected StringBuffer tunaStringBuffer;
    protected static final int tunaStringBufferCapacity = 288;

    protected float[] tunaFloatArray;
    protected String[] tunaStringArray;

    protected Paint tunaPaint;

    public Paint getTunaPaint() {
        return tunaPaint;
    }

    public void setTunaPaint(Paint tunaPaint) {
        this.tunaPaint = tunaPaint;
    }

    // 0
    protected Paint initTunaPaint() {
        if (tunaPaint == null) {
            return tunaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        else {
            tunaPaint.reset();

            tunaPaint.setShader(null);
            tunaPaint.clearShadowLayer();

            tunaPaint.setAntiAlias(true);
        }
        return tunaPaint;
    }

    // 1
    protected Paint initTunaPaint(int alpha) {
        return initTunaPaint(null, 0, Color.TRANSPARENT, null, 0, Color.TRANSPARENT, 0, 0, alpha);
    }

    // 2
    protected Paint initTunaPaint(Style style, int colorValue) {
        return initTunaPaint(style, 0, colorValue, null, 0, Color.TRANSPARENT, 0, 0, -1);

    }

    // 3
    protected Paint initTunaPaint(Style style, int colorValue, int alpha) {
        return initTunaPaint(style, 0, colorValue, null, 0, Color.TRANSPARENT, 0, 0, alpha);
    }

    // 4
    protected Paint initTunaPaint(Style style, int colorValue, Shader shader, int alpha) {
        return initTunaPaint(style, 0, colorValue, shader, 0, Color.TRANSPARENT, 0, 0, alpha);
    }

    // 3
    protected Paint initTunaPaint(Style style, int colorValue, Shader shader) {
        return initTunaPaint(style, 0, colorValue, shader, 0, Color.TRANSPARENT, 0, 0, -1);
    }

    // 2
    protected Paint initTunaPaint(Style style, Shader shader) {
        return initTunaPaint(style, 0, Color.TRANSPARENT, shader, 0, Color.TRANSPARENT, 0, 0, -1);
    }

    // 3
    protected Paint initTunaPaint(Style style, Shader shader, int alpha) {
        return initTunaPaint(style, 0, Color.TRANSPARENT, shader, 0, Color.TRANSPARENT, 0, 0, alpha);
    }


    // 3
    protected Paint initTunaPaint(Style style, int colorValue, float strokeWidth) {
        return initTunaPaint(style, strokeWidth, colorValue, null, 0, Color.TRANSPARENT, 0, 0, -1);
    }

    // 4
    protected Paint initTunaPaint(Style style, int colorValue, float strokeWidth, int alpha) {
        return initTunaPaint(style, strokeWidth, colorValue, null, 0, Color.TRANSPARENT, 0, 0, alpha);
    }

    // 6
    protected Paint initTunaPaint(Style style, Shader shader, float shadowRadius, int shadowColor, float shadowDx, float shadowDy) {
        return initTunaPaint(style, 0, Color.TRANSPARENT, shader, shadowRadius, shadowColor, shadowDx, shadowDy, -1);
    }

    // 6
    protected Paint initTunaPaint(Style style, int colorValue, float shadowRadius, int shadowColor, float shadowDx, float shadowDy) {
        return initTunaPaint(style, 0, colorValue, null, shadowRadius, shadowColor, shadowDx, shadowDy, -1);
    }

    // 9
    protected Paint initTunaPaint(Style style, float strokeWidth, int colorValue, Shader shader, float shadowRadius, int shadowColor, float shadowDx, float shadowDy, int alpha) {
        //
        initTunaPaint();
        //
        if (style != null) {
            tunaPaint.setStyle(style);
        }
        if (strokeWidth != 0) {
            tunaPaint.setStrokeWidth(strokeWidth);
        }

        // When the shadow color can not be set to transparent, but can not set
        if (shader == null) {
            tunaPaint.setColor(colorValue);
        }
        else {
            tunaPaint.setShader(shader);
        }

        if (shadowRadius != 0) {
            tunaPaint.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
        }
        if (alpha != -1) {
            tunaPaint.setAlpha(alpha);
        }
        return tunaPaint;
    }

    // 1
    protected Paint initTunaPaint(float textSize) {
        return initTunaPaint(null, Color.TRANSPARENT, textSize, 0, Color.TRANSPARENT, 0, 0, null);
    }

    // 4
    protected Paint initTunaPaint(Style style, int colorValue, float textSize, Align align) {
        return initTunaPaint(style, colorValue, textSize, 0, Color.TRANSPARENT, 0, 0, align);
    }

    // 8
    protected Paint initTunaPaint(Style style, int colorValue, float textSize, float shadowRadius, int shadowColor, float shadowDx, float shadowDy, Align align) {
        //
        initTunaPaint();
        //
        if (style != null) {
            tunaPaint.setStyle(style);
        }

        tunaPaint.setColor(colorValue);

        if (textSize != 0) {
            tunaPaint.setTextSize(textSize);
        }
        if (shadowRadius != 0) {
            tunaPaint.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
        }
        if (align != null) {
            tunaPaint.setTextAlign(align);
        }
        return tunaPaint;
    }

    // 4
    protected Paint initTunaPaint(Paint paint, float shadowRadius, float shadowDx, float shadowDy) {
        paint.clearShadowLayer();
        if (shadowRadius > 0) {
            paint.setShadowLayer(shadowRadius, shadowDx, shadowDy, Color.WHITE);
        }
        return paint;
    }

    //
    protected Path tunaPath;

    public Path getTunaPath() {
        return tunaPath;
    }

    public void setTunaPath(Path tunaPath) {
        this.tunaPath = tunaPath;
    }

    protected Path initTunaPath() {
        if (tunaPath == null) {
            tunaPath = new Path();
        }
        else {
            tunaPath.reset();
        }
        return tunaPath;
    }

    protected Path initTunaPathMoveTo(float x, float y) {
        if (tunaPath == null) {
            tunaPath = new Path();
        }
        else {
            tunaPath.reset();
        }
        tunaPath.moveTo(x, y);
        return tunaPath;
    }

    protected Path initTunaPathLineTo(float x, float y) {
        if (tunaPath == null) {
            tunaPath = new Path();
        }
        else {
            tunaPath.reset();
        }
        tunaPath.lineTo(x, y);
        return tunaPath;
    }

    protected Path initTunaPathArc(RectF oval, float startAngle, float sweepAngle) {
        if (tunaPath == null) {
            tunaPath = new Path();
        }
        else {
            tunaPath.reset();
        }
        tunaPath.addArc(oval, startAngle, sweepAngle);
        return tunaPath;
    }

    protected Path initTunaPathRoundRect(RectF rect, float[] radii, Direction dir) {
        if (tunaPath == null) {
            tunaPath = new Path();
        }
        else {
            tunaPath.reset();
        }
        // This setting will cause the following message appears reading xml
        // The graphics preview in the layout editor may not be accurate:
        // Different corner sizes are not supported in Path.addRoundRect.
        // (Ignore for this session)
        tunaPath.addRoundRect(rect, radii, dir);
        return tunaPath;
    }

    protected Rect tunaRect;

    public Rect getTunaRect() {
        return tunaRect;
    }

    public void setTunaRect(Rect tunaRect) {
        this.tunaRect = tunaRect;
    }

    protected Rect initTunaRect(int left, int top, int right, int bottom) {
        if (tunaRect == null) {
            tunaRect = new Rect(left, top, right, bottom);
        }
        tunaRect.set(left, top, right, bottom);
        return tunaRect;
    }

    protected RectF tunaRectF;

    public RectF getTunaRectF() {
        return tunaRectF;
    }

    public void setTunaRectF(RectF tunaRectF) {
        this.tunaRectF = tunaRectF;
    }

    //
    protected RectF initTunaRectF(float left, float top, float right, float bottom) {
        if (tunaRectF == null) {
            tunaRectF = new RectF(left, top, right, bottom);
        }
        tunaRectF.set(left, top, right, bottom);
        return tunaRectF;
    }

    //
    protected Matrix tunaMatrix;

    public Matrix getTunaMatrix() {
        return tunaMatrix;
    }

    public void setTunaMatrix(Matrix tunaMatrix) {
        this.tunaMatrix = tunaMatrix;
    }

    protected Matrix initTunaMatrix(float sx, float sy) {
        if (tunaMatrix == null) {
            tunaMatrix = new Matrix();
        }
        tunaMatrix.reset();
        tunaMatrix.setScale(sx, sy);
        return tunaMatrix;
    }

    public static Matrix initTunaMatrix(Matrix matrix, float sx, float sy) {
        if (matrix == null) {
            matrix = new Matrix();
        }
        matrix.reset();
        matrix.setScale(sx, sy);
        return matrix;
    }

    protected DisplayMetrics displayMetrics;
    protected int displayWidth, displayHeight;
    protected float displayDensity, displayScaledDensity;
    protected float displayXdpi, displayYdpi;

    protected LayoutInflater initTunaLayoutInflater() {
        if (tunaLayoutInflater == null) {
            tunaLayoutInflater = LayoutInflater.from(getContext());
        }
        return tunaLayoutInflater;
    }

    // Parameter need to float for example tunaStroke is float convert the value
    // to dip or dp px values​​, to ensure constant size
    protected float pxToDp(float pxValue) {
        return pxValue / displayDensity;
    }

    // Parameter need to float for example tunaStroke is float convert the value
    // to dip or dp px values​​, to ensure constant size
    protected float pxToSp(float pxValue) {
        return pxValue / displayScaledDensity;
    }

    // convert the value px sp values​​, to ensure constant size
    protected int spToPx(float spValue) {
        return (int) (spValue * displayScaledDensity + 0.5f);
    }

    // convert the value to px dp values​​, to ensure constant size
    protected int dpToPx(float dpValue) {
        return (int) (dpValue * displayDensity + 0.5f);
    }

    protected void setTunaLayout(int width, int height) {
        if (tunaLayoutParams == null) {
            tunaLayoutParams = getLayoutParams();
        }
        tunaLayoutParams.width = width;
        tunaLayoutParams.height = height;
        setLayoutParams(tunaLayoutParams);
        invalidate();
    }

    protected void initDisplayMetrics() {
        // Android from 3.0 (February 2011 API Level 11) started, when drawing
        // View supports hardware acceleration,
        // full use of the GPU, which makes the draw more smooth, but it will
        // consume some memory.
        if (displayMetrics == null) {
            displayMetrics = getResources().getDisplayMetrics();
            displayWidth = displayMetrics.widthPixels;
            displayHeight = displayMetrics.heightPixels;
            displayDensity = displayMetrics.density;
            displayScaledDensity = displayMetrics.scaledDensity;
            displayXdpi = displayMetrics.xdpi;
            displayYdpi = displayMetrics.ydpi;

            tunaStringBuffer = new StringBuffer(tunaStringBufferCapacity);
            tunaStringBuffer.append("设备型号 : ");
            tunaStringBuffer.append(Build.MODEL);
            tunaStringBuffer.append(" , ");
            tunaStringBuffer.append("系统版本 : ");
            tunaStringBuffer.append(Build.VERSION.RELEASE);
            tunaStringBuffer.append(" , ");
            tunaStringBuffer.append("API等级 : ");
            tunaStringBuffer.append(Build.VERSION.SDK);
            tunaStringBuffer.append(" , ");
            tunaStringBuffer.append("屏幕分辨率 : ");
            tunaStringBuffer.append(displayWidth);
            tunaStringBuffer.append(" * ");
            tunaStringBuffer.append(displayHeight);
            tunaStringBuffer.append(" ( ");
            tunaStringBuffer.append(displayWidth / displayDensity);
            tunaStringBuffer.append("dp * ");
            tunaStringBuffer.append(displayHeight / displayDensity);
            tunaStringBuffer.append("dp ) ");
            tunaStringBuffer.append(" , 屏幕密度 : ");
            tunaStringBuffer.append(displayDensity);
            tunaStringBuffer.append(" , 伸缩密度 : ");
            tunaStringBuffer.append(displayScaledDensity);
            tunaStringBuffer.append(" , ");
            tunaStringBuffer.append(" , X维 : ");
            tunaStringBuffer.append(displayXdpi);
            tunaStringBuffer.append(" 像素点 / 英寸 , Y维 : ");
            tunaStringBuffer.append(displayYdpi);
            tunaStringBuffer.append(" 像素点 / 英寸");
        }
    }

    //
    protected void initTunaCanvas(Canvas canvas) {
        if (canvas.getDrawFilter() == null) {
            canvas.setDrawFilter(tunaPaintFlagsDrawFilter);
            tunaCanvasHardwareAccelerated = canvas.isHardwareAccelerated();
        }
    }

    // 10
    protected void drawTunaRectCustom(Canvas canvas, int width, int height, int fillColor, float strokeWidth, int strokeColor, float radiusLeftTop, float radiusLeftBottom,
                                      float radiusRightTop, float radiusRightBottom) {

        drawTunaRectCustom(canvas, 0, 0, width, height, fillColor, null, 0, Color.TRANSPARENT, 0, 0, strokeWidth, strokeColor, radiusLeftTop, radiusLeftBottom, radiusRightTop,
                radiusRightBottom);
    }

    // 10
    protected void drawTunaRectCustom(Canvas canvas, int width, int height, Shader shader, float strokeWidth, int strokeColor, float radiusLeftTop, float radiusLeftBottom,
                                      float radiusRightTop, float radiusRightBottom) {

        drawTunaRectCustom(canvas, 0, 0, width, height, Color.TRANSPARENT, shader, 0, Color.TRANSPARENT, 0, 0, strokeWidth, strokeColor, radiusLeftTop, radiusLeftBottom,
                radiusRightTop, radiusRightBottom);
    }

    // 14
    protected void drawTunaRectCustom(Canvas canvas, int width, int height, int fillColor, float shadowRadius, int shadowColor, float shadowDx, float shadowDy, float strokeWidth,
                                      int strokeColor, float radiusLeftTop, float radiusLeftBottom, float radiusRightTop, float radiusRightBottom) {

        drawTunaRectCustom(canvas, 0, 0, width, height, fillColor, null, shadowRadius, shadowColor, shadowDx, shadowDy, strokeWidth, strokeColor, radiusLeftTop, radiusLeftBottom,
                radiusRightTop, radiusRightBottom);
    }

    // 14
    protected void drawTunaRectCustom(Canvas canvas, int width, int height, Shader shader, float shadowRadius, int shadowColor, float shadowDx, float shadowDy, float strokeWidth,
                                      int strokeColor, float radiusLeftTop, float radiusLeftBottom, float radiusRightTop, float radiusRightBottom) {

        drawTunaRectCustom(canvas, 0, 0, width, height, Color.TRANSPARENT, shader, shadowRadius, shadowColor, shadowDx, shadowDy, strokeWidth, strokeColor, radiusLeftTop,
                radiusLeftBottom, radiusRightTop, radiusRightBottom);
    }

    // 15
    protected void drawTunaRectCustom(Canvas canvas, float left, float top, float right, float bottom, int fillColor, Shader shader, float shadowRadius, int shadowColor,
                                      float shadowDx, float shadowDy, float strokeWidth, int strokeColor, float radiusLeftTop, float radiusLeftBottom, float radiusRightTop, float radiusRightBottom) {

        float[] radii = {radiusLeftTop, radiusLeftTop, radiusRightTop, radiusRightTop, radiusRightBottom, radiusRightBottom, radiusLeftBottom, radiusLeftBottom};
        if (strokeWidth > 0) {
            canvas.drawPath(
                    initTunaPathRoundRect(initTunaRectF(left + strokeWidth * 0.5f, top + strokeWidth * 0.5f, right - strokeWidth * 0.5f, bottom - strokeWidth * 0.5f), radii,
                            Path.Direction.CW), initTunaPaint(Paint.Style.STROKE, strokeColor, strokeWidth));
        }

        int radiiLength = radii.length;
        for (int i = 0; i < radiiLength; i++) {
            radii[i] -= strokeWidth;
            radii[i] = radii[i] >= 0 ? radii[i] : 0;
        }

        canvas.drawPath(
                initTunaPathRoundRect(initTunaRectF(left + strokeWidth, top + strokeWidth, right - strokeWidth, bottom - strokeWidth), radii, Path.Direction.CW),
                shader == null ? shadowRadius == 0 ? initTunaPaint(Paint.Style.FILL, fillColor) : initTunaPaint(Paint.Style.FILL, fillColor, shadowRadius, shadowColor, shadowDx,
                        shadowDy) : shadowRadius == 0 ? initTunaPaint(Paint.Style.FILL, shader) : initTunaPaint(Paint.Style.FILL, shader, shadowRadius, shadowColor, shadowDx,
                        shadowDy));
    }

    // 5
    protected void drawTunaRectClassic(Canvas canvas, int width, int height, int fillColor, float radius) {

        drawTunaRectClassic(canvas, 0, 0, width, height, fillColor, null, 0, Color.TRANSPARENT, 0, 0, 0, Color.TRANSPARENT, radius);
    }

    // 7
    protected void drawTunaRectClassic(Canvas canvas, int width, int height, int fillColor, float strokeWidth, int strokeColor, float radius) {

        drawTunaRectClassic(canvas, 0, 0, width, height, fillColor, null, 0, Color.TRANSPARENT, 0, 0, strokeWidth, strokeColor, radius);
    }

    // 7
    protected void drawTunaRectClassic(Canvas canvas, int width, int height, Shader shader, float strokeWidth, int strokeColor, float radius) {

        drawTunaRectClassic(canvas, 0, 0, width, height, Color.TRANSPARENT, shader, 0, Color.TRANSPARENT, 0, 0, strokeWidth, strokeColor, radius);
    }

    // 11
    protected void drawTunaRectClassic(Canvas canvas, int width, int height, int fillColor, float shadowRadius, int shadowColor, float shadowDx, float shadowDy, float strokeWidth,
                                       int strokeColor, float radius) {

        drawTunaRectClassic(canvas, 0, 0, width, height, fillColor, null, shadowRadius, shadowColor, shadowDx, shadowDy, strokeWidth, strokeColor, radius);
    }

    // 11
    protected void drawTunaRectClassic(Canvas canvas, int width, int height, Shader shader, float shadowRadius, int shadowColor, float shadowDx, float shadowDy, float strokeWidth,
                                       int strokeColor, float radius) {

        drawTunaRectClassic(canvas, 0, 0, width, height, Color.TRANSPARENT, shader, shadowRadius, shadowColor, shadowDx, shadowDy, strokeWidth, strokeColor, radius);
    }

    // 12
    protected void drawTunaRectClassic(Canvas canvas, float left, float top, float right, float bottom, int fillColor, Shader shader, float shadowRadius, int shadowColor,
                                       float shadowDx, float shadowDy, float strokeWidth, int strokeColor, float radius) {

        if (strokeWidth > 0) {
            canvas.drawRoundRect(initTunaRectF(left + strokeWidth * 0.5f, top + strokeWidth * 0.5f, right - strokeWidth * 0.5f, bottom - strokeWidth * 0.5f), radius, radius,
                    initTunaPaint(Paint.Style.STROKE, strokeColor, strokeWidth));
        }

        float rx = radius - strokeWidth;
        float ry = radius - strokeWidth;
        rx = rx >= 0 ? rx : 0;
        ry = ry >= 0 ? ry : 0;
        canvas.drawRoundRect(
                initTunaRectF(left + strokeWidth, top + strokeWidth, right - strokeWidth, bottom - strokeWidth),
                rx,
                ry,
                shader == null ? shadowRadius == 0 ? initTunaPaint(Paint.Style.FILL, fillColor) : initTunaPaint(Paint.Style.FILL, fillColor, shadowRadius, shadowColor, shadowDx,
                        shadowDy) : shadowRadius == 0 ? initTunaPaint(Paint.Style.FILL, shader) : initTunaPaint(Paint.Style.FILL, shader, shadowRadius, shadowColor, shadowDx,
                        shadowDy));
    }

    // 8
    protected float[] drawTunaText(Canvas canvas, String string, float width, float centerX, float centerY, float paddingLeft, float paddingRight, Paint tunaPaint) {
        return drawTunaText(canvas, string, width, centerX, centerY, paddingLeft, paddingRight, tunaPaint, tunaTextGravityArray[0]);
    }

    // 9
    protected float[] drawTunaText(Canvas canvas, String string, float width, float centerX, float centerY, float paddingLeft, float paddingRight, Paint tunaPaint,
                                   TunaTextGravity tunaTextGravity) {
        FontMetricsInt fontMetrics = tunaPaint.getFontMetricsInt();

        // float baseline = (targetRectBottom + targetRectTop -
        // fontMetrics.bottom - fontMetrics.top) * 0.5f;
        float baseline = centerY - fontMetrics.bottom * 0.5f - fontMetrics.top * 0.5f;
        int halfwordHeight = (fontMetrics.descent - fontMetrics.ascent) >> 1;

        int stringTotalLength = string.length();
        float measureTotalLength = tunaPaint.measureText(string);
        float availableWidth = width - paddingLeft - paddingRight;

        int textTotalRow = (int) (measureTotalLength / availableWidth);
        textTotalRow = measureTotalLength > availableWidth * textTotalRow ? textTotalRow + 1 : textTotalRow;

        float textMiddleRow = (textTotalRow + 1) * 0.5f;

        String drawString;
        float measureLength;
        float drawLineY;

        for (int drawStartIndex = 0, drawCurrentIndex = 1, drawCurrentRow = 1; drawCurrentIndex <= stringTotalLength; drawCurrentIndex++) {

            drawString = string.substring(drawStartIndex, drawCurrentIndex);
            measureLength = tunaPaint.measureText(drawString);
            drawLineY = baseline + (drawCurrentRow - textMiddleRow) * tunaPaint.getTextSize();

            if (measureLength > availableWidth) {

                canvas.drawText(string.substring(drawStartIndex, drawCurrentIndex - 1), centerX + (paddingLeft - paddingRight) * 0.5f, drawLineY, tunaPaint);

                drawStartIndex = drawCurrentIndex - 1;
                drawCurrentRow = drawCurrentRow + 1;
            }
            else if (drawCurrentIndex == stringTotalLength) {
                switch (tunaTextGravity) {
                case CENTER:
                    // The default character position x is left of the
                    // screen,
                    // if you set paint.setTextAlign (Paint.Align.CENTER);
                    // that is the central character of the position, y is
                    // the character of the baseline at the specified
                    // position on the screen.
                    canvas.drawText(drawString, centerX, drawLineY, tunaPaint);
                    // drawLineY-centerY,To offset the final output of the
                    // baseline of the text line
                    return new float[]{measureLength, measureLength * 0.5f, drawLineY - centerY - halfwordHeight};
                case LEFT:
                    canvas.drawText(drawString, paddingLeft + measureLength * 0.5f, drawLineY, tunaPaint);
                    return new float[]{availableWidth, measureLength + paddingLeft - width * 0.5f, drawLineY - centerY - halfwordHeight};
                default:
                    break;
                }
            }
        }
        return new float[]{width, 0, 0};
    }

    //
    protected float[] drawTunaContent(Canvas canvas, String string, float width, float centerX, float centerY, float paddingLeft, float paddingRight, Paint tunaPaint,
                                      TunaContentGravity tunaContentGravity) {
        FontMetricsInt fontMetrics = tunaPaint.getFontMetricsInt();

        // float baseline = (targetRectBottom + targetRectTop -
        // fontMetrics.bottom - fontMetrics.top) * 0.5f;
        float baseline = centerY - fontMetrics.bottom * 0.5f - fontMetrics.top * 0.5f;
        int halfwordHeight = (fontMetrics.descent - fontMetrics.ascent) >> 1;

        int stringTotalLength = string.length();
        float measureTotalLength = tunaPaint.measureText(string);
        float availableWidth = width - paddingLeft - paddingRight;

        int textTotalRow = (int) (measureTotalLength / availableWidth);
        textTotalRow = measureTotalLength > availableWidth * textTotalRow ? textTotalRow + 1 : textTotalRow;

        float textMiddleRow = (textTotalRow + 1) * 0.5f;

        String drawString;
        float measureLength;
        float drawLineY;

        for (int drawStartIndex = 0, drawCurrentIndex = 1, drawCurrentRow = 1; drawCurrentIndex <= stringTotalLength; drawCurrentIndex++) {

            drawString = string.substring(drawStartIndex, drawCurrentIndex);
            measureLength = tunaPaint.measureText(drawString);
            drawLineY = baseline + (drawCurrentRow - textMiddleRow) * tunaPaint.getTextSize();

            if (measureLength > availableWidth) {

                canvas.drawText(string.substring(drawStartIndex, drawCurrentIndex - 1), centerX + (paddingLeft - paddingRight) * 0.5f, drawLineY, tunaPaint);

                drawStartIndex = drawCurrentIndex - 1;
                drawCurrentRow = drawCurrentRow + 1;
            }
            else if (drawCurrentIndex == stringTotalLength) {
                switch (tunaContentGravity) {
                case CENTER:
                    canvas.drawText(drawString, centerX, drawLineY, tunaPaint);
                    // drawLineY-centerY,To offset the final output of the
                    // baseline of the text line
                    return new float[]{measureLength, measureLength * 0.5f, drawLineY - centerY - halfwordHeight};
                case LEFT:
                    canvas.drawText(drawString, paddingLeft + measureLength * 0.5f, drawLineY, tunaPaint);
                    return new float[]{availableWidth, measureLength + paddingLeft - width * 0.5f, drawLineY - centerY - halfwordHeight};
                default:
                    break;
                }
            }
        }
        return new float[]{width, 0, 0};
    }

    //
    protected void drawTunaTextMark(Canvas canvas, float radius, Paint tunaPaint, float offsetX, float offsetY, String markText) {
        float cx = (tunaWidth >> 1) + offsetX + tunaTextMarkRadius + tunaTextMarkDx;
        float cy = (tunaHeight >> 1) + offsetY + tunaTextMarkDy;

        canvas.drawCircle(cx, cy, radius, tunaPaint);
        if (markText != null) {
            drawTunaText(canvas, markText, tunaWidth, cx, cy, 0, 0, initTunaPaint(Paint.Style.FILL, tunaTextMarkTextColor, tunaTextMarkTextSize, Paint.Align.CENTER));
        }
    }

    //
    public void setTunaStatius(boolean tunaPress, boolean tunaSelect, boolean tunaTextMark) {
        if (this.tunaPress != tunaPress || this.tunaSelect != tunaSelect || this.tunaTextMark != tunaTextMark) {
            this.tunaPress = tunaPress;
            this.tunaSelect = tunaSelect;
            this.tunaTextMark = tunaTextMark;
            invalidate();
        }
    }

    //
    public void setTunaStatius(boolean tunaPress, boolean tunaSelect, boolean tunaTextMark, boolean tunaMaterial) {
        if (this.tunaPress != tunaPress || this.tunaSelect != tunaSelect || this.tunaTextMark != tunaTextMark) {
            this.tunaPress = tunaPress;
            this.tunaSelect = tunaSelect;
            this.tunaTextMark = tunaTextMark;
            if (!tunaMaterial && tunaMaterialAnimatorSet != null) {
                this.tunaMaterialAnimatorSet.cancel();
            }
            invalidate();
        }
    }

    //
    public static void tunaAssociate(final TunaView[] tunaViewArray) {
        if (tunaViewArray == null) {
            return;
        }
        final int arraySize = tunaViewArray.length;
        for (int i = 0; i < arraySize; i++) {
            final int finalI = i;
            tunaViewArray[i].setTunaAssociateListener(new TunaAssociateListener() {
                @Override
                public void tunaAssociate(View v) {
                    for (int j = 0; j < arraySize; j++) {
                        if (j != finalI) {
                            tunaViewArray[j].setTunaStatius(false, false, false);
                        }
                    }
                }
            });
            tunaViewArray[i].setTunaTouchCancelListener(new TunaTouchCancelListener() {
                @Override
                public void tunaTouchCancel(View v) {
                    for (int j = 0; j < arraySize; j++) {
                        switch (finalI) {
                        case 0:
                            if (j == finalI + 1) {
                                tunaViewArray[j].setTunaStatius(false, true, false);
                            }
                            else if (j > finalI + 1) {
                                tunaViewArray[j].setTunaStatius(false, false, false);
                            }
                            break;
                        default:
                            if (j == finalI - 1) {
                                tunaViewArray[j].setTunaStatius(false, true, false);
                            }
                            else if (j < finalI - 1) {
                                tunaViewArray[j].setTunaStatius(false, false, false);
                            }
                            break;
                        }
                    }
                }
            });
        }
    }

    //
    public static void tunaAssociate(final List<TunaView> tunaViewList) {
        if (tunaViewList == null) {
            return;
        }
        final int listSize = tunaViewList.size();
        for (int i = 0; i < listSize; i++) {
            final int finalI = i;
            tunaViewList.get(i).setTunaAssociateListener(new TunaAssociateListener() {
                @Override
                public void tunaAssociate(View v) {
                    for (int j = 0; j < listSize; j++) {
                        if (j != finalI) {
                            tunaViewList.get(j).setTunaStatius(false, false, false, false);
                        }
                    }
                }
            });
            tunaViewList.get(i).setTunaTouchCancelListener(new TunaTouchCancelListener() {
                @Override
                public void tunaTouchCancel(View v) {
                    for (int j = 0; j < listSize; j++) {
                        switch (finalI) {
                        case 0:
                            if (j == finalI + 1) {
                                tunaViewList.get(j).setTunaStatius(false, true, false, false);
                            }
                            else if (j > finalI + 1) {
                                tunaViewList.get(j).setTunaStatius(false, false, false, false);
                            }
                            break;
                        default:
                            if (j == finalI - 1) {
                                tunaViewList.get(j).setTunaStatius(false, true, false, false);
                            }
                            else if (j < finalI - 1) {
                                tunaViewList.get(j).setTunaStatius(false, false, false, false);
                            }
                            break;
                        }
                    }
                }
            });
        }
    }

    //
    public static void tunaDynamic(String[] titleArray, String string, TunaTouchUpListener tunaTouchUpListener, LinearLayout linearLayout, int width, int leftStyle,
                                   int rightStyle, int horizontalStyle, int wholeStyle) {

        tunaDynamic(titleArray, string, tunaTouchUpListener, linearLayout, TypedValue.COMPLEX_UNIT_DIP, width, leftStyle, rightStyle, horizontalStyle, wholeStyle);
    }

    //
    public static void tunaDynamic(String[] titleArray, String string, TunaTouchUpListener tunaTouchUpListener, LinearLayout linearLayout, int unitWidth, int width, int leftStyle,
                                   int rightStyle, int horizontalStyle, int wholeStyle) {
        int index = 0;
        for (int i = 0; i < titleArray.length; i++) {
            if (titleArray[i].equals(string)) {
                index = i;
                break;
            }
        }
        tunaDynamic(titleArray, index, tunaTouchUpListener, linearLayout, unitWidth, width, leftStyle, rightStyle, horizontalStyle, wholeStyle);
    }

    //
    public static void tunaDynamic(String[] titleArray, TunaTouchUpListener tunaTouchUpListener, LinearLayout linearLayout, int width, int leftStyle, int rightStyle,
                                   int horizontalStyle, int wholeStyle) {

        tunaDynamic(titleArray, 0, tunaTouchUpListener, linearLayout, TypedValue.COMPLEX_UNIT_DIP, width, leftStyle, rightStyle, horizontalStyle, wholeStyle);
    }

    //
    public static void tunaDynamic(String[] titleArray, TunaTouchUpListener tunaTouchUpListener, LinearLayout linearLayout, int unitWidth, int width, int leftStyle,
                                   int rightStyle, int horizontalStyle, int wholeStyle) {

        tunaDynamic(titleArray, 0, tunaTouchUpListener, linearLayout, unitWidth, width, leftStyle, rightStyle, horizontalStyle, wholeStyle);
    }

    //
    public static void tunaDynamic(String[] titleArray, int index, TunaTouchUpListener tunaTouchUpListener, LinearLayout linearLayout, int width, int leftStyle, int rightStyle,
                                   int horizontalStyle, int wholeStyle) {

        tunaDynamic(titleArray, index, tunaTouchUpListener, linearLayout, TypedValue.COMPLEX_UNIT_DIP, width, leftStyle, rightStyle, horizontalStyle, wholeStyle);
    }

    //
    public static void tunaDynamic(String[] titleArray, int index, TunaTouchUpListener tunaTouchUpListener, LinearLayout linearLayout, int widthUnit, int width, int leftStyle,
                                   int rightStyle, int horizontalStyle, int wholeStyle) {

    }


    //
    protected View tunaPropertiesView;
    protected LayoutParams tunaLayoutParams;
    protected LayoutInflater tunaLayoutInflater;


    // Hardware accelerated this
    protected boolean tunaIsHardwareAccelerated;
    // Hardware accelerated canvas
    protected boolean tunaCanvasHardwareAccelerated;

    // default edge
    private TunaTouchType tunaTouchType;

    public enum TunaTouchType {
        EDGE(0), ALWAYS(1), NONE(2),;
        final int nativeInt;

        TunaTouchType(int ni) {
            nativeInt = ni;
        }
    }

    private static final TunaTouchType[] tunaTouchTypeArray = {TunaTouchType.EDGE, TunaTouchType.ALWAYS, TunaTouchType.NONE,};

    public TunaTouchType getTunaTouchType() {
        return tunaTouchType;
    }

    public void setTunaTouchType(TunaTouchType tunaTouchType) {
        this.tunaTouchType = tunaTouchType;
    }

    private boolean tunaSuper;

    public boolean isTunaSuper() {
        return tunaSuper;
    }

    public void setTunaSuper(boolean tunaSuper) {
        this.tunaSuper = tunaSuper;
    }

    private boolean tunaClassic;

    public boolean isTunaClassic() {
        return tunaClassic;
    }

    public void setTunaClassic(boolean tunaClassic) {
        this.tunaClassic = tunaClassic;
    }

    // can modify the properties when tunaDebug is true
    protected boolean tunaDebugable;

    public boolean isTunaDebugable() {
        return tunaDebugable;
    }

    public void setTunaDebugable(boolean tunaDebugable) {
        this.tunaDebugable = tunaDebugable;
    }


    // tunaPress default false
    protected boolean tunaPress;

    public boolean isTunaPress() {
        return tunaPress;
    }

    public void setTunaPress(boolean tunaPress) {
        this.tunaPress = tunaPress;
    }

    // tunaSelect default false
    protected boolean tunaSelect;

    public boolean isTunaSelect() {
        return tunaSelect;
    }

    public void setTunaSelect(boolean tunaSelect) {
        this.tunaSelect = tunaSelect;
    }

    //
    protected boolean tunaSelectRaw;

    // default none
    private TunaSelectType tunaSelectType;

    public enum TunaSelectType {
        NONE(0), SAME(1), REVERSE(2),;
        final int nativeInt;

        TunaSelectType(int ni) {
            nativeInt = ni;
        }
    }

    private static final TunaSelectType[] tunaSelectTypeArray = {TunaSelectType.NONE, TunaSelectType.SAME, TunaSelectType.REVERSE,};

    public TunaSelectType getTunaSelectType() {
        return tunaSelectType;
    }

    public void setTunaSelectType(TunaSelectType tunaSelectType) {
        this.tunaSelectType = tunaSelectType;
    }

    // tunaRotate default 0
    private int tunaRotate;

    public int getTunaRotate() {
        return tunaRotate;
    }

    public void setTunaRotate(int tunaRotate) {
        this.tunaRotate = tunaRotate;
    }

    // default false
    protected boolean tunaAnimationable;

    public boolean isTunaAnimationable() {
        return tunaAnimationable;
    }

    public void setTunaAnimationable(boolean tunaAnimationable) {
        this.tunaAnimationable = tunaAnimationable;
        if (tunaAnimationable) {
            invalidate();
        }
    }

    //
    protected float tunaTouchEventX;

    public float getTunaTouchEventX() {
        return tunaTouchEventX;
    }

    public void setTunaTouchEventX(float tunaTouchEventEventX) {
        this.tunaTouchEventX = tunaTouchEventEventX;
    }

    //
    protected float tunaTouchEventY;

    public float getTunaTouchEventY() {
        return tunaTouchEventY;
    }

    public void setTunaTouchEventY(float tunaTouchEventEventY) {
        this.tunaTouchEventY = tunaTouchEventEventY;
    }

    // default false
    protected boolean tunaTouchOutBounds;

    public boolean isTunaTouchOutBounds() {
        return tunaTouchOutBounds;
    }

    public void setTunaTouchOutBounds(boolean tunaTouchOutBounds) {
        this.tunaTouchOutBounds = tunaTouchOutBounds;
    }

    //
    protected TunaLayoutListener tunaLayoutListener;

    public interface TunaLayoutListener {
        void tunaLayout(View v);
    }

    public TunaLayoutListener getTunaLayoutListener() {
        return tunaLayoutListener;
    }

    public void setTunaLayoutListener(TunaLayoutListener tunaLayoutListener) {
        this.tunaLayoutListener = tunaLayoutListener;
    }

    //
    protected TunaDrawListener tunaDrawListener;

    public interface TunaDrawListener {
        void tunaDraw(View v);
    }

    public TunaDrawListener getTunaDrawListener() {
        return tunaDrawListener;
    }

    public void setTunaDrawListener(TunaDrawListener tunaDrawListener) {
        this.tunaDrawListener = tunaDrawListener;
    }

    //
    protected TunaAnimateEndListener tunaAnimateEndListener;

    public interface TunaAnimateEndListener {
        void tunaAnimateEnd(View v);
    }

    public TunaAnimateEndListener getTunaAnimateEndListener() {
        return tunaAnimateEndListener;
    }

    public void setTunaAnimateEndListener(TunaAnimateEndListener tunaAnimateEndListener) {
        this.tunaAnimateEndListener = tunaAnimateEndListener;
    }

    //
    protected TunaTouchListener tunaTouchListener;

    public interface TunaTouchListener {
        void tunaTouch(View v);
    }

    public TunaTouchListener getTunaTouchListener() {
        return tunaTouchListener;
    }

    public void setTunaTouchListener(TunaTouchListener tunaTouchListener) {
        this.tunaTouchListener = tunaTouchListener;
    }

    //
    protected TunaTouchDownListener tunaTouchDownListener;

    public interface TunaTouchDownListener {
        void tunaTouchDown(View v);
    }

    public TunaTouchDownListener getTunaTouchDownListener() {
        return tunaTouchDownListener;
    }

    public void setTunaTouchDownListener(TunaTouchDownListener tunaTouchDownListener) {
        this.tunaTouchDownListener = tunaTouchDownListener;
    }

    //
    protected TunaTouchMoveListener tunaTouchMoveListener;

    public interface TunaTouchMoveListener {
        void tunaTouchMove(View v);
    }

    public TunaTouchMoveListener getTunaTouchMoveListener() {
        return tunaTouchMoveListener;
    }

    public void setTunaTouchMoveListener(TunaTouchMoveListener tunaTouchMoveListener) {
        this.tunaTouchMoveListener = tunaTouchMoveListener;
    }

    //
    protected TunaTouchUpListener tunaTouchUpListener;

    public interface TunaTouchUpListener {
        void tunaTouchUp(View v);
    }

    public TunaTouchUpListener getTunaTouchUpListener() {
        return tunaTouchUpListener;
    }

    public void setTunaTouchUpListener(TunaTouchUpListener tunaTouchUpListener) {
        this.tunaTouchUpListener = tunaTouchUpListener;
    }

    //
    protected TunaTouchCancelListener tunaTouchCancelListener;

    public interface TunaTouchCancelListener {
        void tunaTouchCancel(View v);
    }

    public TunaTouchCancelListener getTunaTouchCancelListener() {
        return tunaTouchCancelListener;
    }

    public void setTunaTouchCancelListener(TunaTouchCancelListener tunaTouchCancelListener) {
        this.tunaTouchCancelListener = tunaTouchCancelListener;
    }

    //
    protected TunaTouchOutListener tunaTouchOutListener;

    public interface TunaTouchOutListener {
        void tunaTouchOut(View v);
    }

    public TunaTouchOutListener getTunaTouchOutListener() {
        return tunaTouchOutListener;
    }

    public void setTunaTouchOutListener(TunaTouchOutListener tunaTouchOutListener) {
        this.tunaTouchOutListener = tunaTouchOutListener;
    }

    //
    protected TunaTouchInListener tunaTouchInListener;

    public interface TunaTouchInListener {
        void tunaTouchIn(View v);
    }

    public TunaTouchInListener getTunaTouchInListener() {
        return tunaTouchInListener;
    }

    public void setTunaTouchInListener(TunaTouchInListener tunaTouchInListener) {
        this.tunaTouchInListener = tunaTouchInListener;
    }

    // TunaAssociateListener is written in onTouchUp
    protected TunaAssociateListener tunaAssociateListener;

    public interface TunaAssociateListener {
        void tunaAssociate(View v);
    }

    public TunaAssociateListener getTunaAssociateListener() {
        return tunaAssociateListener;
    }

    public void setTunaAssociateListener(TunaAssociateListener tunaAssociateListener) {
        this.tunaAssociateListener = tunaAssociateListener;
    }

    //
    protected TunaSubLayoutListener tunaSubLayoutListener;

    public interface TunaSubLayoutListener {
        void tunaSubLayout(View v);
    }

    public TunaSubLayoutListener getTunaSubLayoutListener() {
        return tunaSubLayoutListener;
    }

    public void setTunaSubLayoutListener(TunaSubLayoutListener tunaSubLayoutListener) {
        this.tunaSubLayoutListener = tunaSubLayoutListener;
    }

    //
    protected TunaSubDrawListener tunaSubDrawListener;

    public interface TunaSubDrawListener {
        void tunaSubDraw(View v);
    }

    public TunaSubDrawListener getTunaSubDrawListener() {
        return tunaSubDrawListener;
    }

    public void setTunaSubDrawListener(TunaSubDrawListener tunaSubDrawListener) {
        this.tunaSubDrawListener = tunaSubDrawListener;
    }

    //
    protected TunaSubAnimateEndListener tunaSubAnimateEndListener;

    public interface TunaSubAnimateEndListener {
        void tunaSubAnimateEnd(View v);
    }

    public TunaSubAnimateEndListener getTunaSubAnimateEndListener() {
        return tunaSubAnimateEndListener;
    }

    public void setTunaSubAnimateEndListener(TunaSubAnimateEndListener tunaSubAnimateEndListener) {
        this.tunaSubAnimateEndListener = tunaSubAnimateEndListener;
    }

    /**
     * The following fields and methods can be used only in the superclass
     */

    // tunaBackgroundNormal in onLayout if tunaBackgroundNormal transparent and have drawn the case of default white shadow
    private int tunaBackgroundNormal;

    public int getTunaBackgroundNormal() {
        return tunaBackgroundNormal;
    }

    public void setTunaBackgroundNormal(int tunaBackgroundNormal) {
        this.tunaBackgroundNormal = tunaBackgroundNormal;
    }

    // tunaBackgroundPress default tunaBackgroundNormal
    private int tunaBackgroundPress;

    public int getTunaBackgroundPress() {
        return tunaBackgroundPress;
    }

    public void setTunaBackgroundPress(int tunaBackgroundPress) {
        this.tunaBackgroundPress = tunaBackgroundPress;
    }

    // tunaBackgroundSelect default tunaBackgroundNormal
    private int tunaBackgroundSelect;

    public int getTunaBackgroundSelect() {
        return tunaBackgroundSelect;
    }

    public void setTunaBackgroundSelect(int tunaBackgroundSelect) {
        this.tunaBackgroundSelect = tunaBackgroundSelect;
    }

    // tunaForegroundNormal default transparent
    private int tunaForegroundNormal;

    public int getTunaForegroundNormal() {
        return tunaForegroundNormal;
    }

    public void setTunaForegroundNormal(int tunaForegroundNormal) {
        this.tunaForegroundNormal = tunaForegroundNormal;
    }

    // tunaForegroundPress default tunaForegroundNormal
    private int tunaForegroundPress;

    public int getTunaForegroundPress() {
        return tunaForegroundPress;
    }

    public void setTunaForegroundPress(int tunaForegroundPress) {
        this.tunaForegroundPress = tunaForegroundPress;
    }

    // tunaForegroundSelect default tunaForegroundNormal
    private int tunaForegroundSelect;

    public int getTunaForegroundSelect() {
        return tunaForegroundSelect;
    }

    public void setTunaForegroundSelect(int tunaForegroundSelect) {
        this.tunaForegroundSelect = tunaForegroundSelect;
    }

    public static final int DIRECTION_LEFT = 0x00000001;
    public static final int DIRECTION_RIGHT = DIRECTION_LEFT << 1;
    public static final int DIRECTION_TOP = DIRECTION_RIGHT << 1;
    public static final int DIRECTION_BOTTOM = DIRECTION_TOP << 1;
    public static final int DIRECTION_MASK = DIRECTION_LEFT | DIRECTION_RIGHT | DIRECTION_TOP | DIRECTION_BOTTOM;

    private int tunaBackgroundNormalAngle;

    public int getTunaBackgroundNormalAngle() {
        return tunaBackgroundNormalAngle;
    }

    public void setTunaBackgroundNormalAngle(int tunaBackgroundNormalAngle) {
        this.tunaBackgroundNormalAngle = tunaBackgroundNormalAngle;
    }

    private int tunaBackgroundPressAngle;

    public int getTunaBackgroundPressAngle() {
        return tunaBackgroundPressAngle;
    }

    public void setTunaBackgroundPressAngle(int tunaBackgroundPressAngle) {
        this.tunaBackgroundPressAngle = tunaBackgroundPressAngle;
    }

    private int tunaBackgroundSelectAngle;

    public int getTunaBackgroundSelectAngle() {
        return tunaBackgroundSelectAngle;
    }

    public void setTunaBackgroundSelectAngle(int tunaBackgroundSelectAngle) {
        this.tunaBackgroundSelectAngle = tunaBackgroundSelectAngle;
    }

    // tunaBackgroundNormalGradientStart default tunaBackgroundNormal
    private int tunaBackgroundNormalGradientStart;

    public int getTunaBackgroundNormalGradientStart() {
        return tunaBackgroundNormalGradientStart;
    }

    public void setTunaBackgroundNormalGradientStart(int tunaBackgroundNormalGradientStart) {
        this.tunaBackgroundNormalGradientStart = tunaBackgroundNormalGradientStart;
    }

    // tunaBackgroundNormalGradientEnd default tunaBackgroundNormal
    private int tunaBackgroundNormalGradientEnd;

    public int getTunaBackgroundNormalGradientEnd() {
        return tunaBackgroundNormalGradientEnd;
    }

    public void setTunaBackgroundNormalGradientEnd(int tunaBackgroundNormalGradientEnd) {
        this.tunaBackgroundNormalGradientEnd = tunaBackgroundNormalGradientEnd;
    }

    // tunaBackgroundPressGradientStart default tunaBackgroundPress
    private int tunaBackgroundPressGradientStart;

    public int getTunaBackgroundPressGradientStart() {
        return tunaBackgroundPressGradientStart;
    }

    public void setTunaBackgroundPressGradientStart(int tunaBackgroundPressGradientStart) {
        this.tunaBackgroundPressGradientStart = tunaBackgroundPressGradientStart;
    }

    // tunaBackgroundPressGradientEnd default tunaBackgroundPress
    private int tunaBackgroundPressGradientEnd;

    public int getTunaBackgroundPressGradientEnd() {
        return tunaBackgroundPressGradientEnd;
    }

    public void setTunaBackgroundPressGradientEnd(int tunaBackgroundPressGradientEnd) {
        this.tunaBackgroundPressGradientEnd = tunaBackgroundPressGradientEnd;
    }

    // tunaBackgroundSelectGradientStart default tunaBackgroundSelect
    private int tunaBackgroundSelectGradientStart;

    public int getTunaBackgroundSelectGradientStart() {
        return tunaBackgroundSelectGradientStart;
    }

    public void setTunaBackgroundSelectGradientStart(int tunaBackgroundSelectGradientStart) {
        this.tunaBackgroundSelectGradientStart = tunaBackgroundSelectGradientStart;
    }

    // tunaBackgroundSelectGradientEnd default tunaBackgroundSelect
    private int tunaBackgroundSelectGradientEnd;

    public int getTunaBackgroundSelectGradientEnd() {
        return tunaBackgroundSelectGradientEnd;
    }

    public void setTunaBackgroundSelectGradientEnd(int tunaBackgroundSelectGradientEnd) {
        this.tunaBackgroundSelectGradientEnd = tunaBackgroundSelectGradientEnd;
    }

    // tunaBackgroundNormalShader default null
    private Shader tunaBackgroundNormalShader;

    public Shader getTunaBackgroundNormalShader() {
        return tunaBackgroundNormalShader;
    }

    public void setTunaBackgroundNormalShader(Shader tunaBackgroundNormalShader) {
        this.tunaBackgroundNormalShader = tunaBackgroundNormalShader;
    }

    // tunaBackgroundPressShader default null
    private Shader tunaBackgroundPressShader;

    public Shader getTunaBackgroundPressShader() {
        return tunaBackgroundPressShader;
    }

    public void setTunaBackgroundPressShader(Shader tunaBackgroundPressShader) {
        this.tunaBackgroundPressShader = tunaBackgroundPressShader;
    }

    // tunaBackgroundSelectShader default null
    private Shader tunaBackgroundSelectShader;

    public Shader getTunaBackgroundSelectShader() {
        return tunaBackgroundSelectShader;
    }

    public void setTunaBackgroundSelectShader(Shader tunaBackgroundSelectShader) {
        this.tunaBackgroundSelectShader = tunaBackgroundSelectShader;
    }

    //
    private float tunaBackgroundNormalShadowRadius;

    public float getTunaBackgroundNormalShadowRadius() {
        return tunaBackgroundNormalShadowRadius;
    }

    public void setTunaBackgroundNormalShadowRadius(float tunaBackgroundNormalShadowRadius) {
        setTunaBackgroundNormalShadowRadius(TypedValue.COMPLEX_UNIT_DIP, tunaBackgroundNormalShadowRadius);
    }

    public void setTunaBackgroundNormalShadowRadius(int unit, float tunaBackgroundNormalShadowRadius) {
        setTunaBackgroundNormalShadowRadiusRaw(applyDimension(unit, tunaBackgroundNormalShadowRadius, getViewDisplayMetrics(this)));
    }

    private void setTunaBackgroundNormalShadowRadiusRaw(float tunaBackgroundNormalShadowRadius) {
        if (this.tunaBackgroundNormalShadowRadius != tunaBackgroundNormalShadowRadius) {
            this.tunaBackgroundNormalShadowRadius = tunaBackgroundNormalShadowRadius;
            invalidate();
        }
    }

    //
    private int tunaBackgroundNormalShadowColor;

    public int getTunaBackgroundNormalShadowColor() {
        return tunaBackgroundNormalShadowColor;
    }

    public void setTunaBackgroundNormalShadowColor(int tunaBackgroundNormalShadowColor) {
        this.tunaBackgroundNormalShadowColor = tunaBackgroundNormalShadowColor;
    }

    //
    private float tunaBackgroundNormalShadowDx;

    public float getTunaBackgroundNormalShadowDx() {
        return tunaBackgroundNormalShadowDx;
    }

    public void setTunaBackgroundNormalShadowDx(float tunaBackgroundNormalShadowDx) {
        setTunaBackgroundNormalShadowDx(TypedValue.COMPLEX_UNIT_DIP, tunaBackgroundNormalShadowDx);
    }

    public void setTunaBackgroundNormalShadowDx(int unit, float tunaBackgroundNormalShadowDx) {
        setTunaBackgroundNormalShadowDxRaw(applyDimension(unit, tunaBackgroundNormalShadowDx, getViewDisplayMetrics(this)));
    }

    private void setTunaBackgroundNormalShadowDxRaw(float tunaBackgroundNormalShadowDx) {
        if (this.tunaBackgroundNormalShadowDx != tunaBackgroundNormalShadowDx) {
            this.tunaBackgroundNormalShadowDx = tunaBackgroundNormalShadowDx;
            invalidate();
        }
    }

    //
    private float tunaBackgroundNormalShadowDy;

    public float getTunaBackgroundNormalShadowDy() {
        return tunaBackgroundNormalShadowDy;
    }

    public void setTunaBackgroundNormalShadowDy(float tunaBackgroundNormalShadowDy) {
        setTunaBackgroundNormalShadowDy(TypedValue.COMPLEX_UNIT_DIP, tunaBackgroundNormalShadowDy);
    }

    public void setTunaBackgroundNormalShadowDy(int unit, float tunaBackgroundNormalShadowDy) {
        setTunaBackgroundNormalShadowDyRaw(applyDimension(unit, tunaBackgroundNormalShadowDy, getViewDisplayMetrics(this)));
    }

    private void setTunaBackgroundNormalShadowDyRaw(float tunaBackgroundNormalShadowDy) {
        if (this.tunaBackgroundNormalShadowDy != tunaBackgroundNormalShadowDy) {
            this.tunaBackgroundNormalShadowDy = tunaBackgroundNormalShadowDy;
            invalidate();
        }
    }

    // default tunaBackgroundNormalShadowRadius
    private float tunaBackgroundPressShadowRadius;

    public float getTunaBackgroundPressShadowRadius() {
        return tunaBackgroundPressShadowRadius;
    }

    public void setTunaBackgroundPressShadowRadius(float tunaBackgroundPressShadowRadius) {
        setTunaBackgroundPressShadowRadius(TypedValue.COMPLEX_UNIT_DIP, tunaBackgroundPressShadowRadius);
    }

    public void setTunaBackgroundPressShadowRadius(int unit, float tunaBackgroundPressShadowRadius) {
        setTunaBackgroundPressShadowRadiusRaw(applyDimension(unit, tunaBackgroundPressShadowRadius, getViewDisplayMetrics(this)));
    }

    private void setTunaBackgroundPressShadowRadiusRaw(float tunaBackgroundPressShadowRadius) {
        if (this.tunaBackgroundPressShadowRadius != tunaBackgroundPressShadowRadius) {
            this.tunaBackgroundPressShadowRadius = tunaBackgroundPressShadowRadius;
            invalidate();
        }
    }

    // default tunaBackgroundNormalShadowColor
    private int tunaBackgroundPressShadowColor;

    public int getTunaBackgroundPressShadowColor() {
        return tunaBackgroundPressShadowColor;
    }

    public void setTunaBackgroundPressShadowColor(int tunaBackgroundPressShadowColor) {
        this.tunaBackgroundPressShadowColor = tunaBackgroundPressShadowColor;
    }

    // default tunaBackgroundNormalShadowDx
    private float tunaBackgroundPressShadowDx;

    public float getTunaBackgroundPressShadowDx() {
        return tunaBackgroundPressShadowDx;
    }

    public void setTunaBackgroundPressShadowDx(float tunaBackgroundPressShadowDx) {
        setTunaBackgroundPressShadowDx(TypedValue.COMPLEX_UNIT_DIP, tunaBackgroundPressShadowDx);
    }

    public void setTunaBackgroundPressShadowDx(int unit, float tunaBackgroundPressShadowDx) {
        setTunaBackgroundPressShadowDxRaw(applyDimension(unit, tunaBackgroundPressShadowDx, getViewDisplayMetrics(this)));
    }

    private void setTunaBackgroundPressShadowDxRaw(float tunaBackgroundPressShadowDx) {
        if (this.tunaBackgroundPressShadowDx != tunaBackgroundPressShadowDx) {
            this.tunaBackgroundPressShadowDx = tunaBackgroundPressShadowDx;
            invalidate();
        }
    }

    // default tunaBackgroundNormalShadowDy
    private float tunaBackgroundPressShadowDy;

    public float getTunaBackgroundPressShadowDy() {
        return tunaBackgroundPressShadowDy;
    }

    public void setTunaBackgroundPressShadowDy(float tunaBackgroundPressShadowDy) {
        setTunaBackgroundPressShadowDy(TypedValue.COMPLEX_UNIT_DIP, tunaBackgroundPressShadowDy);
    }

    public void setTunaBackgroundPressShadowDy(int unit, float tunaBackgroundPressShadowDy) {
        setTunaBackgroundPressShadowDyRaw(applyDimension(unit, tunaBackgroundPressShadowDy, getViewDisplayMetrics(this)));
    }

    private void setTunaBackgroundPressShadowDyRaw(float tunaBackgroundPressShadowDy) {
        if (this.tunaBackgroundPressShadowDy != tunaBackgroundPressShadowDy) {
            this.tunaBackgroundPressShadowDy = tunaBackgroundPressShadowDy;
            invalidate();
        }
    }

    // default tunaBackgroundNormalShadowRadius
    private float tunaBackgroundSelectShadowRadius;

    public float getTunaBackgroundSelectShadowRadius() {
        return tunaBackgroundSelectShadowRadius;
    }

    public void setTunaBackgroundSelectShadowRadius(float tunaBackgroundSelectShadowRadius) {
        setTunaBackgroundSelectShadowRadius(TypedValue.COMPLEX_UNIT_DIP, tunaBackgroundSelectShadowRadius);
    }

    public void setTunaBackgroundSelectShadowRadius(int unit, float tunaBackgroundSelectShadowRadius) {
        setTunaBackgroundSelectShadowRadiusRaw(applyDimension(unit, tunaBackgroundSelectShadowRadius, getViewDisplayMetrics(this)));
    }

    private void setTunaBackgroundSelectShadowRadiusRaw(float tunaBackgroundSelectShadowRadius) {
        if (this.tunaBackgroundSelectShadowRadius != tunaBackgroundSelectShadowRadius) {
            this.tunaBackgroundSelectShadowRadius = tunaBackgroundSelectShadowRadius;
            invalidate();
        }
    }

    // default tunaBackgroundNormalShadowColor
    private int tunaBackgroundSelectShadowColor;

    public int getTunaBackgroundSelectShadowColor() {
        return tunaBackgroundSelectShadowColor;
    }

    public void setTunaBackgroundSelectShadowColor(int tunaBackgroundSelectShadowColor) {
        this.tunaBackgroundSelectShadowColor = tunaBackgroundSelectShadowColor;
    }

    // default tunaBackgroundNormalShadowDx
    private float tunaBackgroundSelectShadowDx;

    public float getTunaBackgroundSelectShadowDx() {
        return tunaBackgroundSelectShadowDx;
    }

    public void setTunaBackgroundSelectShadowDx(float tunaBackgroundSelectShadowDx) {
        setTunaBackgroundSelectShadowDx(TypedValue.COMPLEX_UNIT_DIP, tunaBackgroundSelectShadowDx);
    }

    public void setTunaBackgroundSelectShadowDx(int unit, float tunaBackgroundSelectShadowDx) {
        setTunaBackgroundSelectShadowDxRaw(applyDimension(unit, tunaBackgroundSelectShadowDx, getViewDisplayMetrics(this)));
    }

    private void setTunaBackgroundSelectShadowDxRaw(float tunaBackgroundSelectShadowDx) {
        if (this.tunaBackgroundSelectShadowDx != tunaBackgroundSelectShadowDx) {
            this.tunaBackgroundSelectShadowDx = tunaBackgroundSelectShadowDx;
            invalidate();
        }
    }

    // default tunaBackgroundNormalShadowDy
    private float tunaBackgroundSelectShadowDy;

    public float getTunaBackgroundSelectShadowDy() {
        return tunaBackgroundSelectShadowDy;
    }

    public void setTunaBackgroundSelectShadowDy(float tunaBackgroundSelectShadowDy) {
        setTunaBackgroundSelectShadowDy(TypedValue.COMPLEX_UNIT_DIP, tunaBackgroundSelectShadowDy);
    }

    public void setTunaBackgroundSelectShadowDy(int unit, float tunaBackgroundSelectShadowDy) {
        setTunaBackgroundSelectShadowDyRaw(applyDimension(unit, tunaBackgroundSelectShadowDy, getViewDisplayMetrics(this)));
    }

    private void setTunaBackgroundSelectShadowDyRaw(float tunaBackgroundSelectShadowDy) {
        if (this.tunaBackgroundSelectShadowDy != tunaBackgroundSelectShadowDy) {
            this.tunaBackgroundSelectShadowDy = tunaBackgroundSelectShadowDy;
            invalidate();
        }
    }

    //
    private Bitmap tunaSrcNormal;

    public Bitmap getTunaBitmapSrcNormal() {
        return tunaSrcNormal;
    }

    public void setTunaBitmapSrcNormal(Bitmap tunaSrcNormal) {
        this.tunaSrcNormal = tunaSrcNormal;
    }

    //
    private Bitmap tunaSrcPress;

    public Bitmap getTunaBitmapSrcPress() {
        return tunaSrcPress;
    }

    public void setTunaBitmapSrcPress(Bitmap tunaSrcPress) {
        this.tunaSrcPress = tunaSrcPress;
    }

    //
    private Bitmap tunaSrcSelect;

    public Bitmap getTunaBitmapSrcSelect() {
        return tunaSrcSelect;
    }

    public void setTunaBitmapSrcSelect(Bitmap tunaSrcSelect) {
        this.tunaSrcSelect = tunaSrcSelect;
    }

    //
    private float tunaSrcNormalShadowRadius;

    public float getTunaBitmapSrcNormalShadowRadius() {
        return tunaSrcNormalShadowRadius;
    }

    public void setTunaBitmapSrcNormalShadowRadius(float tunaSrcNormalShadowRadius) {
        setTunaBitmapSrcNormalShadowRadius(TypedValue.COMPLEX_UNIT_DIP, tunaSrcNormalShadowRadius);
    }

    public void setTunaBitmapSrcNormalShadowRadius(int unit, float tunaSrcNormalShadowRadius) {
        setTunaBitmapSrcNormalShadowRadiusRaw(applyDimension(unit, tunaSrcNormalShadowRadius, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcNormalShadowRadiusRaw(float tunaSrcNormalShadowRadius) {
        if (this.tunaSrcNormalShadowRadius != tunaSrcNormalShadowRadius) {
            this.tunaSrcNormalShadowRadius = tunaSrcNormalShadowRadius;
            invalidate();
        }
    }

    //
    private float tunaSrcNormalShadowDx;

    public float getTunaBitmapSrcNormalShadowDx() {
        return tunaSrcNormalShadowDx;
    }

    public void setTunaBitmapSrcNormalShadowDx(float tunaSrcNormalShadowDx) {
        setTunaBitmapSrcNormalShadowDx(TypedValue.COMPLEX_UNIT_DIP, tunaSrcNormalShadowDx);
    }

    public void setTunaBitmapSrcNormalShadowDx(int unit, float tunaSrcNormalShadowDx) {
        setTunaBitmapSrcNormalShadowDxRaw(applyDimension(unit, tunaSrcNormalShadowDx, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcNormalShadowDxRaw(float tunaSrcNormalShadowDx) {
        if (this.tunaSrcNormalShadowDx != tunaSrcNormalShadowDx) {
            this.tunaSrcNormalShadowDx = tunaSrcNormalShadowDx;
            invalidate();
        }
    }

    //
    private float tunaSrcNormalShadowDy;

    public float getTunaBitmapSrcNormalShadowDy() {
        return tunaSrcNormalShadowDy;
    }

    public void setTunaBitmapSrcNormalShadowDy(float tunaSrcNormalShadowDy) {
        setTunaBitmapSrcNormalShadowDy(TypedValue.COMPLEX_UNIT_DIP, tunaSrcNormalShadowDy);
    }

    public void setTunaBitmapSrcNormalShadowDy(int unit, float tunaSrcNormalShadowDy) {
        setTunaBitmapSrcNormalShadowDyRaw(applyDimension(unit, tunaSrcNormalShadowDy, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcNormalShadowDyRaw(float tunaSrcNormalShadowDy) {
        if (this.tunaSrcNormalShadowDy != tunaSrcNormalShadowDy) {
            this.tunaSrcNormalShadowDy = tunaSrcNormalShadowDy;
            invalidate();
        }
    }

    // default tunaSrcNormalShadowRadius
    private float tunaSrcPressShadowRadius;

    public float getTunaBitmapSrcPressShadowRadius() {
        return tunaSrcPressShadowRadius;
    }

    public void setTunaBitmapSrcPressShadowRadius(float tunaSrcPressShadowRadius) {
        setTunaBitmapSrcPressShadowRadius(TypedValue.COMPLEX_UNIT_DIP, tunaSrcPressShadowRadius);
    }

    public void setTunaBitmapSrcPressShadowRadius(int unit, float tunaSrcPressShadowRadius) {
        setTunaBitmapSrcPressShadowRadiusRaw(applyDimension(unit, tunaSrcPressShadowRadius, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcPressShadowRadiusRaw(float tunaSrcPressShadowRadius) {
        if (this.tunaSrcPressShadowRadius != tunaSrcPressShadowRadius) {
            this.tunaSrcPressShadowRadius = tunaSrcPressShadowRadius;
            invalidate();
        }
    }

    // default tunaSrcNormalShadowDx
    private float tunaSrcPressShadowDx;

    public float getTunaBitmapSrcPressShadowDx() {
        return tunaSrcPressShadowDx;
    }

    public void setTunaBitmapSrcPressShadowDx(float tunaSrcPressShadowDx) {
        setTunaBitmapSrcPressShadowDx(TypedValue.COMPLEX_UNIT_DIP, tunaSrcPressShadowDx);
    }

    public void setTunaBitmapSrcPressShadowDx(int unit, float tunaSrcPressShadowDx) {
        setTunaBitmapSrcPressShadowDxRaw(applyDimension(unit, tunaSrcPressShadowDx, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcPressShadowDxRaw(float tunaSrcPressShadowDx) {
        if (this.tunaSrcPressShadowDx != tunaSrcPressShadowDx) {
            this.tunaSrcPressShadowDx = tunaSrcPressShadowDx;
            invalidate();
        }
    }

    // default tunaSrcNormalShadowDy
    private float tunaSrcPressShadowDy;

    public float getTunaBitmapSrcPressShadowDy() {
        return tunaSrcPressShadowDy;
    }

    public void setTunaBitmapSrcPressShadowDy(float tunaSrcPressShadowDy) {
        setTunaBitmapSrcPressShadowDy(TypedValue.COMPLEX_UNIT_DIP, tunaSrcPressShadowDy);
    }

    public void setTunaBitmapSrcPressShadowDy(int unit, float tunaSrcPressShadowDy) {
        setTunaBitmapSrcPressShadowDyRaw(applyDimension(unit, tunaSrcPressShadowDy, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcPressShadowDyRaw(float tunaSrcPressShadowDy) {
        if (this.tunaSrcPressShadowDy != tunaSrcPressShadowDy) {
            this.tunaSrcPressShadowDy = tunaSrcPressShadowDy;
            invalidate();
        }
    }

    // default tunaSrcNormalShadowRadius
    private float tunaSrcSelectShadowRadius;

    public float getTunaBitmapSrcSelectShadowRadius() {
        return tunaSrcSelectShadowRadius;
    }

    public void setTunaBitmapSrcSelectShadowRadius(float tunaSrcSelectShadowRadius) {
        setTunaBitmapSrcSelectShadowRadius(TypedValue.COMPLEX_UNIT_DIP, tunaSrcSelectShadowRadius);
    }

    public void setTunaBitmapSrcSelectShadowRadius(int unit, float tunaSrcSelectShadowRadius) {
        setTunaBitmapSrcSelectShadowRadiusRaw(applyDimension(unit, tunaSrcSelectShadowRadius, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcSelectShadowRadiusRaw(float tunaSrcSelectShadowRadius) {
        if (this.tunaSrcSelectShadowRadius != tunaSrcSelectShadowRadius) {
            this.tunaSrcSelectShadowRadius = tunaSrcSelectShadowRadius;
            invalidate();
        }
    }

    // default tunaSrcNormalShadowDx
    private float tunaSrcSelectShadowDx;

    public float getTunaBitmapSrcSelectShadowDx() {
        return tunaSrcSelectShadowDx;
    }

    public void setTunaBitmapSrcSelectShadowDx(float tunaSrcSelectShadowDx) {
        setTunaBitmapSrcSelectShadowDx(TypedValue.COMPLEX_UNIT_DIP, tunaSrcSelectShadowDx);
    }

    public void setTunaBitmapSrcSelectShadowDx(int unit, float tunaSrcSelectShadowDx) {
        setTunaBitmapSrcSelectShadowDxRaw(applyDimension(unit, tunaSrcSelectShadowDx, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcSelectShadowDxRaw(float tunaSrcSelectShadowDx) {
        if (this.tunaSrcSelectShadowDx != tunaSrcSelectShadowDx) {
            this.tunaSrcSelectShadowDx = tunaSrcSelectShadowDx;
            invalidate();
        }
    }

    // default tunaSrcNormalShadowDy
    private float tunaSrcSelectShadowDy;

    public float getTunaBitmapSrcSelectShadowDy() {
        return tunaSrcSelectShadowDy;
    }

    public void setTunaBitmapSrcSelectShadowDy(float tunaSrcSelectShadowDy) {
        setTunaBitmapSrcSelectShadowDy(TypedValue.COMPLEX_UNIT_DIP, tunaSrcSelectShadowDy);
    }

    public void setTunaBitmapSrcSelectShadowDy(int unit, float tunaSrcSelectShadowDy) {
        setTunaBitmapSrcSelectShadowDyRaw(applyDimension(unit, tunaSrcSelectShadowDy, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcSelectShadowDyRaw(float tunaSrcSelectShadowDy) {
        if (this.tunaSrcSelectShadowDy != tunaSrcSelectShadowDy) {
            this.tunaSrcSelectShadowDy = tunaSrcSelectShadowDy;
            invalidate();
        }
    }

    public static final int LEFT = 0x00000000;
    public static final int CENTER_HORIZONTAL = 0x00000001;
    public static final int RIGHT = CENTER_HORIZONTAL << 1;
    public static final int TOP = 0x00000000;
    public static final int CENTER_VERTICAL = RIGHT << 1;
    public static final int BOTTOM = CENTER_VERTICAL << 1;
    public static final int CENTER = CENTER_HORIZONTAL | CENTER_VERTICAL;
    public static final int GRAVITY_MASK = CENTER_HORIZONTAL | RIGHT | CENTER_VERTICAL | BOTTOM;

    //
    private int tunaSrcAnchorGravity;

    public int getTunaSrcAnchorGravity() {
        return tunaSrcAnchorGravity;
    }

    public void setSrcAnchorGravity(int tunaSrcAnchorGravity) {
        this.tunaSrcAnchorGravity = tunaSrcAnchorGravity;
    }

    // anchor Normal,Press,Select use one Matrix
    protected Matrix tunaAnchorMatrix;

    public Matrix getTunaAnchorMatrix() {
        return tunaAnchorMatrix;
    }

    public void setTunaAnchorMatrix(Matrix tunaAnchorMatrix) {
        this.tunaAnchorMatrix = tunaAnchorMatrix;
    }

    protected Matrix initTunaAnchorMatrix(float sx, float sy) {
        if (tunaAnchorMatrix == null) {
            tunaAnchorMatrix = new Matrix();
        }
        tunaAnchorMatrix.reset();
        tunaAnchorMatrix.setScale(sx, sy);
        return tunaAnchorMatrix;
    }

    //
    private Bitmap tunaSrcAnchorNormal;

    public Bitmap getTunaBitmapSrcAnchorNormal() {
        return tunaSrcAnchorNormal;
    }

    public void setTunaBitmapSrcAnchorNormal(Bitmap tunaSrcAnchorNormal) {
        this.tunaSrcAnchorNormal = tunaSrcAnchorNormal;
    }

    //
    private float tunaSrcAnchorNormalWidth;

    public float getTunaBitmapSrcAnchorNormalWidth() {
        return tunaSrcAnchorNormalWidth;
    }

    public void setTunaBitmapSrcAnchorNormalWidth(float tunaSrcAnchorNormalWidth) {
        setTunaBitmapSrcAnchorNormalWidth(TypedValue.COMPLEX_UNIT_DIP, tunaSrcAnchorNormalWidth);
    }

    public void setTunaBitmapSrcAnchorNormalWidth(int unit, float tunaSrcAnchorNormalWidth) {
        setTunaBitmapSrcAnchorNormalWidthRaw(applyDimension(unit, tunaSrcAnchorNormalWidth, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcAnchorNormalWidthRaw(float tunaSrcAnchorNormalWidth) {
        if (this.tunaSrcAnchorNormalWidth != tunaSrcAnchorNormalWidth) {
            this.tunaSrcAnchorNormalWidth = tunaSrcAnchorNormalWidth;
            invalidate();
        }
    }

    //
    private float tunaSrcAnchorNormalHeight;

    public float getTunaBitmapSrcAnchorNormalHeight() {
        return tunaSrcAnchorNormalHeight;
    }

    public void setTunaBitmapSrcAnchorNormalHeight(float tunaSrcAnchorNormalHeight) {
        setTunaBitmapSrcAnchorNormalHeight(TypedValue.COMPLEX_UNIT_DIP, tunaSrcAnchorNormalHeight);
    }

    public void setTunaBitmapSrcAnchorNormalHeight(int unit, float tunaSrcAnchorNormalHeight) {
        setTunaBitmapSrcAnchorNormalHeightRaw(applyDimension(unit, tunaSrcAnchorNormalHeight, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcAnchorNormalHeightRaw(float tunaSrcAnchorNormalHeight) {
        if (this.tunaSrcAnchorNormalHeight != tunaSrcAnchorNormalHeight) {
            this.tunaSrcAnchorNormalHeight = tunaSrcAnchorNormalHeight;
            invalidate();
        }
    }

    //
    private float tunaSrcAnchorNormalDx;

    public float getTunaBitmapSrcAnchorNormalDx() {
        return tunaSrcAnchorNormalDx;
    }

    public void setTunaBitmapSrcAnchorNormalDx(float tunaSrcAnchorNormalDx) {
        setTunaBitmapSrcAnchorNormalDx(TypedValue.COMPLEX_UNIT_DIP, tunaSrcAnchorNormalDx);
    }

    public void setTunaBitmapSrcAnchorNormalDx(int unit, float tunaSrcAnchorNormalDx) {
        setTunaBitmapSrcAnchorNormalDxRaw(applyDimension(unit, tunaSrcAnchorNormalDx, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcAnchorNormalDxRaw(float tunaSrcAnchorNormalDx) {
        if (this.tunaSrcAnchorNormalDx != tunaSrcAnchorNormalDx) {
            this.tunaSrcAnchorNormalDx = tunaSrcAnchorNormalDx;
            invalidate();
        }
    }

    //
    private float tunaSrcAnchorNormalDy;

    public float getTunaBitmapSrcAnchorNormalDy() {
        return tunaSrcAnchorNormalDy;
    }

    public void setTunaBitmapSrcAnchorNormalDy(float tunaSrcAnchorNormalDy) {
        setTunaBitmapSrcAnchorNormalDy(TypedValue.COMPLEX_UNIT_DIP, tunaSrcAnchorNormalDy);
    }

    public void setTunaBitmapSrcAnchorNormalDy(int unit, float tunaSrcAnchorNormalDy) {
        setTunaBitmapSrcAnchorNormalDyRaw(applyDimension(unit, tunaSrcAnchorNormalDy, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcAnchorNormalDyRaw(float tunaSrcAnchorNormalDy) {
        if (this.tunaSrcAnchorNormalDy != tunaSrcAnchorNormalDy) {
            this.tunaSrcAnchorNormalDy = tunaSrcAnchorNormalDy;
            invalidate();
        }
    }

    //
    private Bitmap tunaSrcAnchorPress;

    public Bitmap getTunaBitmapSrcAnchorPress() {
        return tunaSrcAnchorPress;
    }

    public void setTunaBitmapSrcAnchorPress(Bitmap tunaSrcAnchorPress) {
        this.tunaSrcAnchorPress = tunaSrcAnchorPress;
    }

    //
    private float tunaSrcAnchorPressWidth;

    public float getTunaBitmapSrcAnchorPressWidth() {
        return tunaSrcAnchorPressWidth;
    }

    public void setTunaBitmapSrcAnchorPressWidth(float tunaSrcAnchorPressWidth) {
        setTunaBitmapSrcAnchorPressWidth(TypedValue.COMPLEX_UNIT_DIP, tunaSrcAnchorPressWidth);
    }

    public void setTunaBitmapSrcAnchorPressWidth(int unit, float tunaSrcAnchorPressWidth) {
        setTunaBitmapSrcAnchorPressWidthRaw(applyDimension(unit, tunaSrcAnchorPressWidth, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcAnchorPressWidthRaw(float tunaSrcAnchorPressWidth) {
        if (this.tunaSrcAnchorPressWidth != tunaSrcAnchorPressWidth) {
            this.tunaSrcAnchorPressWidth = tunaSrcAnchorPressWidth;
            invalidate();
        }
    }

    //
    private float tunaSrcAnchorPressHeight;

    public float getTunaBitmapSrcAnchorPressHeight() {
        return tunaSrcAnchorPressHeight;
    }

    public void setTunaBitmapSrcAnchorPressHeight(float tunaSrcAnchorPressHeight) {
        setTunaBitmapSrcAnchorPressHeight(TypedValue.COMPLEX_UNIT_DIP, tunaSrcAnchorPressHeight);
    }

    public void setTunaBitmapSrcAnchorPressHeight(int unit, float tunaSrcAnchorPressHeight) {
        setTunaBitmapSrcAnchorPressHeightRaw(applyDimension(unit, tunaSrcAnchorPressHeight, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcAnchorPressHeightRaw(float tunaSrcAnchorPressHeight) {
        if (this.tunaSrcAnchorPressHeight != tunaSrcAnchorPressHeight) {
            this.tunaSrcAnchorPressHeight = tunaSrcAnchorPressHeight;
            invalidate();
        }
    }

    //
    private float tunaSrcAnchorPressDx;

    public float getTunaBitmapSrcAnchorPressDx() {
        return tunaSrcAnchorPressDx;
    }

    public void setTunaBitmapSrcAnchorPressDx(float tunaSrcAnchorPressDx) {
        setTunaBitmapSrcAnchorPressDx(TypedValue.COMPLEX_UNIT_DIP, tunaSrcAnchorPressDx);
    }

    public void setTunaBitmapSrcAnchorPressDx(int unit, float tunaSrcAnchorPressDx) {
        setTunaBitmapSrcAnchorPressDxRaw(applyDimension(unit, tunaSrcAnchorPressDx, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcAnchorPressDxRaw(float tunaSrcAnchorPressDx) {
        if (this.tunaSrcAnchorPressDx != tunaSrcAnchorPressDx) {
            this.tunaSrcAnchorPressDx = tunaSrcAnchorPressDx;
            invalidate();
        }
    }

    //
    private float tunaSrcAnchorPressDy;

    public float getTunaBitmapSrcAnchorPressDy() {
        return tunaSrcAnchorPressDy;
    }

    public void setTunaBitmapSrcAnchorPressDy(float tunaSrcAnchorPressDy) {
        setTunaBitmapSrcAnchorPressDy(TypedValue.COMPLEX_UNIT_DIP, tunaSrcAnchorPressDy);
    }

    public void setTunaBitmapSrcAnchorPressDy(int unit, float tunaSrcAnchorPressDy) {
        setTunaBitmapSrcAnchorPressDyRaw(applyDimension(unit, tunaSrcAnchorPressDy, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcAnchorPressDyRaw(float tunaSrcAnchorPressDy) {
        if (this.tunaSrcAnchorPressDy != tunaSrcAnchorPressDy) {
            this.tunaSrcAnchorPressDy = tunaSrcAnchorPressDy;
            invalidate();
        }
    }

    //
    private Bitmap tunaSrcAnchorSelect;

    public Bitmap getTunaBitmapSrcAnchorSelect() {
        return tunaSrcAnchorSelect;
    }

    public void setTunaBitmapSrcAnchorSelect(Bitmap tunaSrcAnchorSelect) {
        this.tunaSrcAnchorSelect = tunaSrcAnchorSelect;
    }

    //
    private float tunaSrcAnchorSelectWidth;

    public float getTunaBitmapSrcAnchorSelectWidth() {
        return tunaSrcAnchorSelectWidth;
    }

    public void setTunaBitmapSrcAnchorSelectWidth(float tunaSrcAnchorSelectWidth) {
        setTunaBitmapSrcAnchorSelectWidth(TypedValue.COMPLEX_UNIT_DIP, tunaSrcAnchorSelectWidth);
    }

    public void setTunaBitmapSrcAnchorSelectWidth(int unit, float tunaSrcAnchorSelectWidth) {
        setTunaBitmapSrcAnchorSelectWidthRaw(applyDimension(unit, tunaSrcAnchorSelectWidth, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcAnchorSelectWidthRaw(float tunaSrcAnchorSelectWidth) {
        if (this.tunaSrcAnchorSelectWidth != tunaSrcAnchorSelectWidth) {
            this.tunaSrcAnchorSelectWidth = tunaSrcAnchorSelectWidth;
            invalidate();
        }
    }

    //
    private float tunaSrcAnchorSelectHeight;

    public float getTunaBitmapSrcAnchorSelectHeight() {
        return tunaSrcAnchorSelectHeight;
    }

    public void setTunaBitmapSrcAnchorSelectHeight(float tunaSrcAnchorSelectHeight) {
        setTunaBitmapSrcAnchorSelectHeight(TypedValue.COMPLEX_UNIT_DIP, tunaSrcAnchorSelectHeight);
    }

    public void setTunaBitmapSrcAnchorSelectHeight(int unit, float tunaSrcAnchorSelectHeight) {
        setTunaBitmapSrcAnchorSelectHeightRaw(applyDimension(unit, tunaSrcAnchorSelectHeight, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcAnchorSelectHeightRaw(float tunaSrcAnchorSelectHeight) {
        if (this.tunaSrcAnchorSelectHeight != tunaSrcAnchorSelectHeight) {
            this.tunaSrcAnchorSelectHeight = tunaSrcAnchorSelectHeight;
            invalidate();
        }
    }

    //
    private float tunaSrcAnchorSelectDx;

    public float getTunaBitmapSrcAnchorSelectDx() {
        return tunaSrcAnchorSelectDx;
    }

    public void setTunaBitmapSrcAnchorSelectDx(float tunaSrcAnchorSelectDx) {
        setTunaBitmapSrcAnchorSelectDx(TypedValue.COMPLEX_UNIT_DIP, tunaSrcAnchorSelectDx);
    }

    public void setTunaBitmapSrcAnchorSelectDx(int unit, float tunaSrcAnchorSelectDx) {
        setTunaBitmapSrcAnchorSelectDxRaw(applyDimension(unit, tunaSrcAnchorSelectDx, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcAnchorSelectDxRaw(float tunaSrcAnchorSelectDx) {
        if (this.tunaSrcAnchorSelectDx != tunaSrcAnchorSelectDx) {
            this.tunaSrcAnchorSelectDx = tunaSrcAnchorSelectDx;
            invalidate();
        }
    }

    //
    private float tunaSrcAnchorSelectDy;

    public float getTunaBitmapSrcAnchorSelectDy() {
        return tunaSrcAnchorSelectDy;
    }

    public void setTunaBitmapSrcAnchorSelectDy(float tunaSrcAnchorSelectDy) {
        setTunaBitmapSrcAnchorSelectDy(TypedValue.COMPLEX_UNIT_DIP, tunaSrcAnchorSelectDy);
    }

    public void setTunaBitmapSrcAnchorSelectDy(int unit, float tunaSrcAnchorSelectDy) {
        setTunaBitmapSrcAnchorSelectDyRaw(applyDimension(unit, tunaSrcAnchorSelectDy, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcAnchorSelectDyRaw(float tunaSrcAnchorSelectDy) {
        if (this.tunaSrcAnchorSelectDy != tunaSrcAnchorSelectDy) {
            this.tunaSrcAnchorSelectDy = tunaSrcAnchorSelectDy;
            invalidate();
        }
    }

    // tunaStrokeWidthNormal default 0
    private float tunaStrokeWidthNormal;

    public float getTunaStrokeWidthNormal() {
        return tunaStrokeWidthNormal;
    }

    public void setTunaStrokeWidthNormal(float tunaStrokeWidthNormal) {
        setTunaStrokeWidthNormal(TypedValue.COMPLEX_UNIT_DIP, tunaStrokeWidthNormal);
    }

    public void setTunaStrokeWidthNormal(int unit, float tunaStrokeWidthNormal) {
        setTunaStrokeWidthNormalRaw(applyDimension(unit, tunaStrokeWidthNormal, getViewDisplayMetrics(this)));
    }

    private void setTunaStrokeWidthNormalRaw(float tunaStrokeWidthNormal) {
        if (this.tunaStrokeWidthNormal != tunaStrokeWidthNormal) {
            this.tunaStrokeWidthNormal = tunaStrokeWidthNormal;
            invalidate();
        }
    }

    // tunaStrokeColorNormal default transparent
    private int tunaStrokeColorNormal;

    public int getTunaStrokeColorNormal() {
        return tunaStrokeColorNormal;
    }

    public void setTunaStrokeColorNormal(int tunaStrokeColorNormal) {
        this.tunaStrokeColorNormal = tunaStrokeColorNormal;
    }

    // tunaStrokeWidthPress default tunaStrokeWidthNormal
    private float tunaStrokeWidthPress;

    public float getTunaStrokeWidthPress() {
        return tunaStrokeWidthPress;
    }

    public void setTunaStrokeWidthPress(float tunaStrokeWidthPress) {
        setTunaStrokeWidthPress(TypedValue.COMPLEX_UNIT_DIP, tunaStrokeWidthPress);
    }

    public void setTunaStrokeWidthPress(int unit, float tunaStrokeWidthPress) {
        setTunaStrokeWidthPressRaw(applyDimension(unit, tunaStrokeWidthPress, getViewDisplayMetrics(this)));
    }

    private void setTunaStrokeWidthPressRaw(float tunaStrokeWidthPress) {
        if (this.tunaStrokeWidthPress != tunaStrokeWidthPress) {
            this.tunaStrokeWidthPress = tunaStrokeWidthPress;
            invalidate();
        }
    }

    // tunaStrokeColorPress default tunaStrokeColorNormal
    private int tunaStrokeColorPress;

    public int getTunaStrokeColorPress() {
        return tunaStrokeColorPress;
    }

    public void setTunaStrokeColorPress(int tunaStrokeColorPress) {
        this.tunaStrokeColorPress = tunaStrokeColorPress;
    }

    // tunaStrokeWidthSelect default tunaStrokeWidthNormal
    private float tunaStrokeWidthSelect;

    public float getTunaStrokeWidthSelect() {
        return tunaStrokeWidthSelect;
    }

    public void setTunaStrokeWidthSelect(float tunaStrokeWidthSelect) {
        setTunaStrokeWidthSelect(TypedValue.COMPLEX_UNIT_DIP, tunaStrokeWidthSelect);
    }

    public void setTunaStrokeWidthSelect(int unit, float tunaStrokeWidthSelect) {
        setTunaStrokeWidthSelectRaw(applyDimension(unit, tunaStrokeWidthSelect, getViewDisplayMetrics(this)));
    }

    private void setTunaStrokeWidthSelectRaw(float tunaStrokeWidthSelect) {
        if (this.tunaStrokeWidthSelect != tunaStrokeWidthSelect) {
            this.tunaStrokeWidthSelect = tunaStrokeWidthSelect;
            invalidate();
        }
    }

    // tunaStrokeColorSelect default tunaStrokeColorNormal
    private int tunaStrokeColorSelect;

    public int getTunaStrokeColorSelect() {
        return tunaStrokeColorSelect;
    }

    public void setTunaStrokeColorSelect(int tunaStrokeColorSelect) {
        this.tunaStrokeColorSelect = tunaStrokeColorSelect;
    }

    // tunaRadius default 0
    private float tunaRadius;

    public float getTunaRadius() {
        return tunaRadius;
    }

    public void setTunaRadius(float tunaRadius) {
        setTunaRadius(TypedValue.COMPLEX_UNIT_DIP, tunaRadius);
    }

    public void setTunaRadius(int unit, float tunaRadius) {
        setTunaRadiusRaw(applyDimension(unit, tunaRadius, getViewDisplayMetrics(this)));
    }

    private void setTunaRadiusRaw(float tunaRadius) {
        if (this.tunaRadius != tunaRadius) {
            this.tunaRadius = tunaRadius;
            invalidate();
        }
    }

    // radius of draw custom roundRect
    // tunaRadiusLeftTop,tunaRadiusLeftBottom,tunaRadiusRightTop,tunaRadiusRightBottom default 0
    private float tunaRadiusLeftTop;

    public float getTunaRadiusLeftTop() {
        return tunaRadiusLeftTop;
    }

    public void setTunaRadiusLeftTop(float tunaRadiusLeftTop) {
        setTunaRadiusLeftTop(TypedValue.COMPLEX_UNIT_DIP, tunaRadiusLeftTop);
    }

    public void setTunaRadiusLeftTop(int unit, float tunaRadiusLeftTop) {
        setTunaRadiusLeftTopRaw(applyDimension(unit, tunaRadiusLeftTop, getViewDisplayMetrics(this)));
    }

    private void setTunaRadiusLeftTopRaw(float tunaRadiusLeftTop) {
        if (this.tunaRadiusLeftTop != tunaRadiusLeftTop) {
            this.tunaRadiusLeftTop = tunaRadiusLeftTop;
            invalidate();
        }
    }

    //
    private float tunaRadiusLeftBottom;

    public float getTunaRadiusLeftBottom() {
        return tunaRadiusLeftBottom;
    }

    public void setTunaRadiusLeftBottom(float tunaRadiusLeftBottom) {
        setTunaRadiusLeftBottom(TypedValue.COMPLEX_UNIT_DIP, tunaRadiusLeftBottom);
    }

    public void setTunaRadiusLeftBottom(int unit, float tunaRadiusLeftBottom) {
        setTunaRadiusLeftBottomRaw(applyDimension(unit, tunaRadiusLeftBottom, getViewDisplayMetrics(this)));
    }

    private void setTunaRadiusLeftBottomRaw(float tunaRadiusLeftBottom) {
        if (this.tunaRadiusLeftBottom != tunaRadiusLeftBottom) {
            this.tunaRadiusLeftBottom = tunaRadiusLeftBottom;
            invalidate();
        }
    }

    //
    private float tunaRadiusRightTop;

    public float getTunaRadiusRightTop() {
        return tunaRadiusRightTop;
    }

    public void setTunaRadiusRightTop(float tunaRadiusRightTop) {
        setTunaRadiusRightTop(TypedValue.COMPLEX_UNIT_DIP, tunaRadiusRightTop);
    }

    public void setTunaRadiusRightTop(int unit, float tunaRadiusRightTop) {
        setTunaRadiusRightTopRaw(applyDimension(unit, tunaRadiusRightTop, getViewDisplayMetrics(this)));
    }

    private void setTunaRadiusRightTopRaw(float tunaRadiusRightTop) {
        if (this.tunaRadiusRightTop != tunaRadiusRightTop) {
            this.tunaRadiusRightTop = tunaRadiusRightTop;
            invalidate();
        }
    }

    //
    private float tunaRadiusRightBottom;

    public float getTunaRadiusRightBottom() {
        return tunaRadiusRightBottom;
    }

    public void setTunaRadiusRightBottom(float tunaRadiusRightBottom) {
        setTunaRadiusRightBottom(TypedValue.COMPLEX_UNIT_DIP, tunaRadiusRightBottom);
    }

    public void setTunaRadiusRightBottom(int unit, float tunaRadiusRightBottom) {
        setTunaRadiusRightBottomRaw(applyDimension(unit, tunaRadiusRightBottom, getViewDisplayMetrics(this)));
    }

    private void setTunaRadiusRightBottomRaw(float tunaRadiusRightBottom) {
        if (this.tunaRadiusRightBottom != tunaRadiusRightBottom) {
            this.tunaRadiusRightBottom = tunaRadiusRightBottom;
            invalidate();
        }
    }

    // tunaTextMark default false
    protected boolean tunaTextMark;

    public boolean isTunaTextMark() {
        return tunaTextMark;
    }

    public void setTunaTextMark(boolean tunaTextMark) {
        this.tunaTextMark = tunaTextMark;
        invalidate();
    }

    public void setTunaTextMark(String tunaTextMarkTextValue) {
        this.tunaTextMarkTextValue = tunaTextMarkTextValue;
        invalidate();
    }

    public void setTunaTextMark(float tunaTextMarkRadius, int tunaTextMarkColor, String tunaTextMarkTextValue, float tunaTextMarkTextSize, int tunaTextMarkTextColor,
                                float tunaTextMarkDx, float tunaTextMarkDy) {
        setTunaTextMark(TypedValue.COMPLEX_UNIT_DIP, tunaTextMarkRadius, tunaTextMarkColor, tunaTextMarkTextValue, TypedValue.COMPLEX_UNIT_DIP, tunaTextMarkTextSize,
                tunaTextMarkTextColor, TypedValue.COMPLEX_UNIT_DIP, tunaTextMarkDx, TypedValue.COMPLEX_UNIT_DIP, tunaTextMarkDy);
    }

    public void setTunaTextMark(int tunaTextMarkRadiusUnit, float tunaTextMarkRadius, int tunaTextMarkColor, String tunaTextMarkTextValue, int tunaTextMarkTextSizeUnit,
                                float tunaTextMarkTextSize, int tunaTextMarkTextColor, int tunaTextMarkDxUnit, float tunaTextMarkDx, int tunaTextMarkDyUnit, float tunaTextMarkDy) {

        DisplayMetrics displayMetrics = getViewDisplayMetrics(this);

        setTunaTextMarkRaw(applyDimension(tunaTextMarkRadiusUnit, tunaTextMarkRadius, displayMetrics), tunaTextMarkColor, tunaTextMarkTextValue,
                applyDimension(tunaTextMarkTextSizeUnit, tunaTextMarkTextSize, displayMetrics), tunaTextMarkTextColor,
                applyDimension(tunaTextMarkDxUnit, tunaTextMarkDx, displayMetrics), applyDimension(tunaTextMarkDyUnit, tunaTextMarkDy, displayMetrics));
    }

    private void setTunaTextMarkRaw(float tunaTextMarkRadius, int tunaTextMarkColor, String tunaTextMarkTextValue, float tunaTextMarkTextSize, int tunaTextMarkTextColor,
                                    float tunaTextMarkDx, float tunaTextMarkDy) {
        if (this.tunaTextMarkRadius != tunaTextMarkRadius || this.tunaTextMarkColor != tunaTextMarkColor || this.tunaTextMarkTextValue != tunaTextMarkTextValue
                || this.tunaTextMarkTextSize != tunaTextMarkTextSize || this.tunaTextMarkTextColor != tunaTextMarkTextColor || this.tunaTextMarkDx != tunaTextMarkDx
                || this.tunaTextMarkDy != tunaTextMarkDy) {
            this.tunaTextMarkRadius = tunaTextMarkRadius;
            this.tunaTextMarkColor = tunaTextMarkColor;
            this.tunaTextMarkTextValue = tunaTextMarkTextValue;
            this.tunaTextMarkTextSize = tunaTextMarkTextSize;
            this.tunaTextMarkTextColor = tunaTextMarkTextColor;
            this.tunaTextMarkDx = tunaTextMarkDx;
            this.tunaTextMarkDy = tunaTextMarkDy;
            this.tunaTextMark = true;
            invalidate();
        }
    }

    // tunaTextMarkTouchable default false
    private boolean tunaTextMarkTouchable;

    public boolean isTunaTextMarkTouchable() {
        return tunaTextMarkTouchable;
    }

    public void setTunaTextMarkTouchable(boolean tunaTextMarkTouchable) {
        this.tunaTextMarkTouchable = tunaTextMarkTouchable;
    }

    // tunaTextMarkRadius default 0
    private float tunaTextMarkRadius;

    public float getTunaTextMarkRadius() {
        return tunaTextMarkRadius;
    }

    public void setTunaTextMarkRadius(float tunaTextMarkRadius) {
        setTunaTextMarkRadius(TypedValue.COMPLEX_UNIT_DIP, tunaTextMarkRadius);
    }

    public void setTunaTextMarkRadius(int unit, float tunaTextMarkRadius) {
        setTunaTextMarkRadiusRaw(applyDimension(unit, tunaTextMarkRadius, getViewDisplayMetrics(this)));
    }

    private void setTunaTextMarkRadiusRaw(float tunaTextMarkRadius) {
        if (this.tunaTextMarkRadius != tunaTextMarkRadius) {
            this.tunaTextMarkRadius = tunaTextMarkRadius;
            invalidate();
        }
    }

    // tunaTextMarkColor default transparent
    private int tunaTextMarkColor;

    public int getTunaTextMarkColor() {
        return tunaTextMarkColor;
    }

    public void setTunaTextMarkColor(int tunaTextMarkColor) {
        this.tunaTextMarkColor = tunaTextMarkColor;
    }

    //
    private String tunaTextMarkTextValue;

    public String getTunaTextMarkTextValue() {
        return tunaTextMarkTextValue;
    }

    public void setTunaTextMarkTextValue(String tunaTextMarkTextValue) {
        this.tunaTextMarkTextValue = tunaTextMarkTextValue;
    }

    //
    private float tunaTextMarkTextSize;

    public float getTunaTextMarkTextSize() {
        return tunaTextMarkTextSize;
    }

    public void setTunaTextMarkTextSize(float tunaTextMarkTextSize) {
        setTunaTextMarkTextSize(TypedValue.COMPLEX_UNIT_DIP, tunaTextMarkTextSize);
    }

    public void setTunaTextMarkTextSize(int unit, float tunaTextMarkTextSize) {
        setTunaTextMarkTextSizeRaw(applyDimension(unit, tunaTextMarkTextSize, getViewDisplayMetrics(this)));
    }

    private void setTunaTextMarkTextSizeRaw(float tunaTextMarkTextSize) {
        if (this.tunaTextMarkTextSize != tunaTextMarkTextSize) {
            this.tunaTextMarkTextSize = tunaTextMarkTextSize;
            invalidate();
        }
    }

    //
    private int tunaTextMarkTextColor;

    public int getTunaTextMarkTextColor() {
        return tunaTextMarkTextColor;
    }

    public void setTunaTextMarkTextColor(int tunaTextMarkTextColor) {
        this.tunaTextMarkTextColor = tunaTextMarkTextColor;
    }

    //
    private float tunaTextMarkDx;

    public float getTunaTextMarkDx() {
        return tunaTextMarkDx;
    }

    public void setTunaTextMarkDx(float tunaTextMarkDx) {
        setTunaTextMarkDx(TypedValue.COMPLEX_UNIT_DIP, tunaTextMarkDx);
    }

    public void setTunaTextMarkDx(int unit, float tunaTextMarkDx) {
        setTunaTextMarkDxRaw(applyDimension(unit, tunaTextMarkDx, getViewDisplayMetrics(this)));
    }

    private void setTunaTextMarkDxRaw(float tunaTextMarkDx) {
        if (this.tunaTextMarkDx != tunaTextMarkDx) {
            this.tunaTextMarkDx = tunaTextMarkDx;
            invalidate();
        }
    }

    //
    private float tunaTextMarkDy;

    public float getTunaTextMarkDy() {
        return tunaTextMarkDy;
    }

    public void setTunaTextMarkDy(float tunaTextMarkDy) {
        setTunaTextMarkDy(TypedValue.COMPLEX_UNIT_DIP, tunaTextMarkDy);
    }

    public void setTunaTextMarkDy(int unit, float tunaTextMarkDy) {
        setTunaTextMarkDyRaw(applyDimension(unit, tunaTextMarkDy, getViewDisplayMetrics(this)));
    }

    private void setTunaTextMarkDyRaw(float tunaTextMarkDy) {
        if (this.tunaTextMarkDy != tunaTextMarkDy) {
            this.tunaTextMarkDy = tunaTextMarkDy;
            invalidate();
        }
    }

    //
    private float tunaTextMarkFractionDx;

    public float getTunaTextMarkFractionDx() {
        return tunaTextMarkFractionDx;
    }

    public void setTunaTextMarkFractionDx(float tunaTextMarkFractionDx) {
        this.tunaTextMarkFractionDx = tunaTextMarkFractionDx;
    }

    //
    private float tunaTextMarkFractionDy;

    public float getTunaTextMarkFractionDy() {
        return tunaTextMarkFractionDy;
    }

    public void setTunaTextMarkFractionDy(float tunaTextMarkFractionDy) {
        this.tunaTextMarkFractionDy = tunaTextMarkFractionDy;
    }

    // tunaTextValue default null
    private String tunaTextValue;

    public String getTunaTextValue() {
        return tunaTextValue;
    }

    public void setTunaTextValue(String tunaTextValue) {
        this.tunaTextValue = tunaTextValue;
        // invalidate();
    }

    // tunaTextSize default 0
    private float tunaTextSize;

    public float getTunaTextSize() {
        return tunaTextSize;
    }

    public void setTunaTextSize(float tunaTextSize) {
        setTunaTextSize(TypedValue.COMPLEX_UNIT_DIP, tunaTextSize);
    }

    public void setTunaTextSize(int unit, float tunaTextSize) {
        setTunaTextSizeRaw(applyDimension(unit, tunaTextSize, getViewDisplayMetrics(this)));
    }

    private void setTunaTextSizeRaw(float tunaTextSize) {
        if (this.tunaTextSize != tunaTextSize) {
            this.tunaTextSize = tunaTextSize;
            invalidate();
        }
    }

    // tunaTextColorNormal default transparent
    private int tunaTextColorNormal;

    public int getTunaTextColorNormal() {
        return tunaTextColorNormal;
    }

    public void setTunaTextColorNormal(int tunaTextColorNormal) {
        this.tunaTextColorNormal = tunaTextColorNormal;
    }

    // tunaTextColorPress default tunaTextColorNormal
    private int tunaTextColorPress;

    public int getTunaTextColorPress() {
        return tunaTextColorPress;
    }

    public void setTunaTextColorPress(int tunaTextColorPress) {
        this.tunaTextColorPress = tunaTextColorPress;
    }

    // tunaTextColorSelect default tunaTextColorNormal
    private int tunaTextColorSelect;

    public int getTunaTextColorSelect() {
        return tunaTextColorSelect;
    }

    public void setTunaTextColorSelect(int tunaTextColorSelect) {
        this.tunaTextColorSelect = tunaTextColorSelect;
    }

    // tunaTextPaddingLeft means distance between tunaSrcLeft and The
    // leftmost,note about the tunaSrcLeftPadding
    private float tunaTextPaddingLeft;

    public float getTunaTextPaddingLeft() {
        return tunaTextPaddingLeft;
    }

    public void setTunaTextPaddingLeft(float tunaTextPaddingLeft) {
        setTunaTextPaddingLeft(TypedValue.COMPLEX_UNIT_DIP, tunaTextPaddingLeft);
    }

    public void setTunaTextPaddingLeft(int unit, float tunaTextPaddingLeft) {
        setTunaTextPaddingLeftRaw(applyDimension(unit, tunaTextPaddingLeft, getViewDisplayMetrics(this)));
    }

    private void setTunaTextPaddingLeftRaw(float tunaTextPaddingLeft) {
        if (this.tunaTextPaddingLeft != tunaTextPaddingLeft) {
            this.tunaTextPaddingLeft = tunaTextPaddingLeft;
            invalidate();
        }
    }

    // tunaTextPaddingRight means distance between tunaSrcRight and The
    // rightmost,note about the tunaSrcRightPadding
    private float tunaTextPaddingRight;

    public float getTunaTextPaddingRight() {
        return tunaTextPaddingRight;
    }

    public void setTunaTextPaddingRight(float tunaTextPaddingRight) {
        setTunaTextPaddingRight(TypedValue.COMPLEX_UNIT_DIP, tunaTextPaddingRight);
    }

    public void setTunaTextPaddingRight(int unit, float tunaTextPaddingRight) {
        setTunaTextPaddingRightRaw(applyDimension(unit, tunaTextPaddingRight, getViewDisplayMetrics(this)));
    }

    private void setTunaTextPaddingRightRaw(float tunaTextPaddingRight) {
        if (this.tunaTextPaddingRight != tunaTextPaddingRight) {
            this.tunaTextPaddingRight = tunaTextPaddingRight;
            invalidate();
        }
    }

    //
    private TunaTextGravity tunaTextGravity;

    public enum TunaTextGravity {
        CENTER(0), LEFT(1),;
        final int nativeInt;

        TunaTextGravity(int ni) {
            nativeInt = ni;
        }
    }

    private static final TunaTextGravity[] tunaTextGravityArray = {TunaTextGravity.CENTER, TunaTextGravity.LEFT,};

    public TunaTextGravity getTunaTextGravity() {
        return tunaTextGravity;
    }

    public void setTunaTextGravity(TunaTextGravity tunaTextGravity) {
        this.tunaTextGravity = tunaTextGravity;
    }

    // attention that tunaTextDx is the width of the base , tunaTextDy is the
    // height of the base
    private float tunaTextDx;

    public float getTunaTextDx() {
        return tunaTextDx;
    }

    public void setTunaTextDx(float tunaTextDx) {
        setTunaTextDx(TypedValue.COMPLEX_UNIT_DIP, tunaTextDx);
    }

    public void setTunaTextDx(int unit, float tunaTextDx) {
        setTunaTextDxRaw(applyDimension(unit, tunaTextDx, getViewDisplayMetrics(this)));
    }

    private void setTunaTextDxRaw(float tunaTextDx) {
        if (this.tunaTextDx != tunaTextDx) {
            this.tunaTextDx = tunaTextDx;
            invalidate();
        }
    }

    //
    private float tunaTextDy;

    public float getTunaTextDy() {
        return tunaTextDy;
    }

    public void setTunaTextDy(float tunaTextDy) {
        setTunaTextDy(TypedValue.COMPLEX_UNIT_DIP, tunaTextDy);
    }

    public void setTunaTextDy(int unit, float tunaTextDy) {
        setTunaTextDyRaw(applyDimension(unit, tunaTextDy, getViewDisplayMetrics(this)));
    }

    private void setTunaTextDyRaw(float tunaTextDy) {
        if (this.tunaTextDy != tunaTextDy) {
            this.tunaTextDy = tunaTextDy;
            invalidate();
        }
    }

    //
    private float tunaTextFractionDx;

    public float getTunaTextFractionDx() {
        return tunaTextFractionDx;
    }

    public void setTunaTextFractionDx(float tunaTextFractionDx) {
        this.tunaTextFractionDx = tunaTextFractionDx;
    }

    //
    private float tunaTextFractionDy;

    public float getTunaTextFractionDy() {
        return tunaTextFractionDy;
    }

    public void setTunaTextFractionDy(float tunaTextFractionDy) {
        this.tunaTextFractionDy = tunaTextFractionDy;
    }

    //
    private float tunaTextDrawWidth;
    private float tunaTextEndOffsetCenterX;
    private float tunaTextEndOffsetCenterY;

    //
    private float tunaTextShadowRadius;

    public float getTunaTextShadowRadius() {
        return tunaTextShadowRadius;
    }

    public void setTunaTextShadowRadius(float tunaTextShadowRadius) {
        setTunaTextShadowRadius(TypedValue.COMPLEX_UNIT_DIP, tunaTextShadowRadius);
    }

    public void setTunaTextShadowRadius(int unit, float tunaTextShadowRadius) {
        setTunaTextShadowRadiusRaw(applyDimension(unit, tunaTextShadowRadius, getViewDisplayMetrics(this)));
    }

    private void setTunaTextShadowRadiusRaw(float tunaTextShadowRadius) {
        if (this.tunaTextShadowRadius != tunaTextShadowRadius) {
            this.tunaTextShadowRadius = tunaTextShadowRadius;
            invalidate();
        }
    }

    //
    private int tunaTextShadowColor;

    public int getTunaTextShadowColor() {
        return tunaTextShadowColor;
    }

    public void setTunaTextShadowColor(int tunaTextShadowColor) {
        this.tunaTextShadowColor = tunaTextShadowColor;
    }

    //
    private float tunaTextShadowDx;

    public float getTunaTextShadowDx() {
        return tunaTextShadowDx;
    }

    public void setTunaTextShadowDx(float tunaTextShadowDx) {
        setTunaTextShadowDx(TypedValue.COMPLEX_UNIT_DIP, tunaTextShadowDx);
    }

    public void setTunaTextShadowDx(int unit, float tunaTextShadowDx) {
        setTunaTextShadowDxRaw(applyDimension(unit, tunaTextShadowDx, getViewDisplayMetrics(this)));
    }

    private void setTunaTextShadowDxRaw(float tunaTextShadowDx) {
        if (this.tunaTextShadowDx != tunaTextShadowDx) {
            this.tunaTextShadowDx = tunaTextShadowDx;
            invalidate();
        }
    }

    //
    private float tunaTextShadowDy;

    public float getTunaTextShadowDy() {
        return tunaTextShadowDy;
    }

    public void setTunaTextShadowDy(float tunaTextShadowDy) {
        setTunaTextShadowDy(TypedValue.COMPLEX_UNIT_DIP, tunaTextShadowDy);
    }

    public void setTunaTextShadowDy(int unit, float tunaTextShadowDy) {
        setTunaTextShadowDyRaw(applyDimension(unit, tunaTextShadowDy, getViewDisplayMetrics(this)));
    }

    private void setTunaTextShadowDyRaw(float tunaTextShadowDy) {
        if (this.tunaTextShadowDy != tunaTextShadowDy) {
            this.tunaTextShadowDy = tunaTextShadowDy;
            invalidate();
        }
    }

    // tunaContentValue default null
    private String tunaContentValue;

    public String getTunaContentValue() {
        return tunaContentValue;
    }

    public void setTunaContentValue(String tunaContentValue) {
        this.tunaContentValue = tunaContentValue;
        // invalidate();
    }

    // tunaContentSize default 0
    private float tunaContentSize;

    public float getTunaContentSize() {
        return tunaContentSize;
    }

    public void setTunaContentSize(float tunaContentSize) {
        setTunaContentSize(TypedValue.COMPLEX_UNIT_DIP, tunaContentSize);
    }

    public void setTunaContentSize(int unit, float tunaContentSize) {
        setTunaContentSizeRaw(applyDimension(unit, tunaContentSize, getViewDisplayMetrics(this)));
    }

    private void setTunaContentSizeRaw(float tunaContentSize) {
        if (this.tunaContentSize != tunaContentSize) {
            this.tunaContentSize = tunaContentSize;
            invalidate();
        }
    }

    //
    private float tunaContentShadowRadius;

    public float getTunaContentShadowRadius() {
        return tunaContentShadowRadius;
    }

    public void setTunaContentShadowRadius(float tunaContentShadowRadius) {
        setTunaContentShadowRadius(TypedValue.COMPLEX_UNIT_DIP, tunaContentShadowRadius);
    }

    public void setTunaContentShadowRadius(int unit, float tunaContentShadowRadius) {
        setTunaContentShadowRadiusRaw(applyDimension(unit, tunaContentShadowRadius, getViewDisplayMetrics(this)));
    }

    private void setTunaContentShadowRadiusRaw(float tunaContentShadowRadius) {
        if (this.tunaContentShadowRadius != tunaContentShadowRadius) {
            this.tunaContentShadowRadius = tunaContentShadowRadius;
            invalidate();
        }
    }

    //
    private int tunaContentShadowColor;

    public int getTunaContentShadowColor() {
        return tunaContentShadowColor;
    }

    public void setTunaContentShadowColor(int tunaContentShadowColor) {
        this.tunaContentShadowColor = tunaContentShadowColor;
    }

    //
    private float tunaContentShadowDx;

    public float getTunaContentShadowDx() {
        return tunaContentShadowDx;
    }

    public void setTunaContentShadowDx(float tunaContentShadowDx) {
        setTunaContentShadowDx(TypedValue.COMPLEX_UNIT_DIP, tunaContentShadowDx);
    }

    public void setTunaContentShadowDx(int unit, float tunaContentShadowDx) {
        setTunaContentShadowDxRaw(applyDimension(unit, tunaContentShadowDx, getViewDisplayMetrics(this)));
    }

    private void setTunaContentShadowDxRaw(float tunaContentShadowDx) {
        if (this.tunaContentShadowDx != tunaContentShadowDx) {
            this.tunaContentShadowDx = tunaContentShadowDx;
            invalidate();
        }
    }

    //
    private float tunaContentShadowDy;

    public float getTunaContentShadowDy() {
        return tunaContentShadowDy;
    }

    public void setTunaContentShadowDy(float tunaContentShadowDy) {
        setTunaContentShadowDy(TypedValue.COMPLEX_UNIT_DIP, tunaContentShadowDy);
    }

    public void setTunaContentShadowDy(int unit, float tunaContentShadowDy) {
        setTunaContentShadowDyRaw(applyDimension(unit, tunaContentShadowDy, getViewDisplayMetrics(this)));
    }

    private void setTunaContentShadowDyRaw(float tunaContentShadowDy) {
        if (this.tunaContentShadowDy != tunaContentShadowDy) {
            this.tunaContentShadowDy = tunaContentShadowDy;
            invalidate();
        }
    }

    // tunaContentColorNormal default transparent
    private int tunaContentColorNormal;

    public int getTunaContentColorNormal() {
        return tunaContentColorNormal;
    }

    public void setTunaContentColorNormal(int tunaContentColorNormal) {
        this.tunaContentColorNormal = tunaContentColorNormal;
    }

    // tunaContentColorPress default tunaContentColorNormal
    private int tunaContentColorPress;

    public int getTunaContentColorPress() {
        return tunaContentColorPress;
    }

    public void setTunaContentColorPress(int tunaContentColorPress) {
        this.tunaContentColorPress = tunaContentColorPress;
    }

    // tunaContentColorSelect default tunaContentColorNormal
    private int tunaContentColorSelect;

    public int getTunaContentColorSelect() {
        return tunaContentColorSelect;
    }

    public void setTunaContentColorSelect(int tunaContentColorSelect) {
        this.tunaContentColorSelect = tunaContentColorSelect;
    }

    // tunaContentPaddingLeft means distance between tunaSrcLeft and The
    // leftmost,note about the tunaSrcLeftPadding
    private float tunaContentPaddingLeft;

    public float getTunaContentPaddingLeft() {
        return tunaContentPaddingLeft;
    }

    public void setTunaContentPaddingLeft(float tunaContentPaddingLeft) {
        setTunaContentPaddingLeft(TypedValue.COMPLEX_UNIT_DIP, tunaContentPaddingLeft);
    }

    public void setTunaContentPaddingLeft(int unit, float tunaContentPaddingLeft) {
        setTunaContentPaddingLeftRaw(applyDimension(unit, tunaContentPaddingLeft, getViewDisplayMetrics(this)));
    }

    private void setTunaContentPaddingLeftRaw(float tunaContentPaddingLeft) {
        if (this.tunaContentPaddingLeft != tunaContentPaddingLeft) {
            this.tunaContentPaddingLeft = tunaContentPaddingLeft;
            invalidate();
        }
    }

    // tunaContentPaddingRight means distance between tunaSrcRight and The
    // rightmost,note about the tunaSrcRightPadding
    private float tunaContentPaddingRight;

    public float getTunaContentPaddingRight() {
        return tunaContentPaddingRight;
    }

    public void setTunaContentPaddingRight(float tunaContentPaddingRight) {
        setTunaContentPaddingRight(TypedValue.COMPLEX_UNIT_DIP, tunaContentPaddingRight);
    }

    public void setTunaContentPaddingRight(int unit, float tunaContentPaddingRight) {
        setTunaContentPaddingRightRaw(applyDimension(unit, tunaContentPaddingRight, getViewDisplayMetrics(this)));
    }

    private void setTunaContentPaddingRightRaw(float tunaContentPaddingRight) {
        if (this.tunaContentPaddingRight != tunaContentPaddingRight) {
            this.tunaContentPaddingRight = tunaContentPaddingRight;
            invalidate();
        }
    }

    //
    private TunaContentGravity tunaContentGravity;

    public enum TunaContentGravity {
        CENTER(0), LEFT(1),;
        final int nativeInt;

        TunaContentGravity(int ni) {
            nativeInt = ni;
        }
    }

    private static final TunaContentGravity[] tunaContentGravityArray = {TunaContentGravity.CENTER, TunaContentGravity.LEFT,};

    public TunaContentGravity getTunaContentGravity() {
        return tunaContentGravity;
    }

    public void setTunaContentGravity(TunaContentGravity tunaContentGravity) {
        this.tunaContentGravity = tunaContentGravity;
    }

    // attention that tunaContentDx is the width of the base , tunaContentDy is
    // the height of the base
    private float tunaContentDx;

    public float getTunaContenttDx() {
        return tunaContentDx;
    }

    public void setTunaContentDx(float tunaContentDx) {
        setTunaTextDx(TypedValue.COMPLEX_UNIT_DIP, tunaContentDx);
    }

    public void setTunaContentDx(int unit, float tunaContentDx) {
        setTunaContentDxRaw(applyDimension(unit, tunaContentDx, getViewDisplayMetrics(this)));
    }

    private void setTunaContentDxRaw(float tunaContentDx) {
        if (this.tunaContentDx != tunaContentDx) {
            this.tunaContentDx = tunaContentDx;
            invalidate();
        }
    }

    //
    private float tunaContentDy;

    public float getTunaContentDy() {
        return tunaContentDy;
    }

    public void setTunaContentDy(float tunaContentDy) {
        setTunaContentDy(TypedValue.COMPLEX_UNIT_DIP, tunaContentDy);
    }

    public void setTunaContentDy(int unit, float tunaContentDy) {
        setTunaContentDyRaw(applyDimension(unit, tunaContentDy, getViewDisplayMetrics(this)));
    }

    private void setTunaContentDyRaw(float tunaContentDy) {
        if (this.tunaContentDy != tunaContentDy) {
            this.tunaContentDy = tunaContentDy;
            invalidate();
        }
    }

    //
    private float tunaContentFractionDx;

    public float getTunaContentFractionDx() {
        return tunaContentFractionDx;
    }

    public void setTunaContentFractionDx(float tunaContentFractionDx) {
        this.tunaContentFractionDx = tunaContentFractionDx;
    }

    //
    private float tunaContentFractionDy;

    public float getTunaContentFractionDy() {
        return tunaContentFractionDy;
    }

    public void setTunaContentFractionDy(float tunaContentFractionDy) {
        this.tunaContentFractionDy = tunaContentFractionDy;
    }

    // tunaContentMark default false
    protected boolean tunaContentMark;

    public boolean isTunaContentMark() {
        return tunaContentMark;
    }

    public void setTunaContentMark(boolean tunaContentMark) {
        this.tunaContentMark = tunaContentMark;
        invalidate();
    }

    public void setTunaContentMark(String tunaContentMarkTextValue) {
        this.tunaContentMarkTextValue = tunaContentMarkTextValue;
        invalidate();
    }

    public void setTunaContentMark(float tunaContentMarkRadius, int tunaContentMarkColor, String tunaContentMarkTextValue, float tunaContentMarkTextSize,
                                   int tunaContentMarkTextColor, float tunaContentMarkDx, float tunaContentMarkDy) {
        setTunaContentMark(TypedValue.COMPLEX_UNIT_DIP, tunaContentMarkRadius, tunaContentMarkColor, tunaContentMarkTextValue, TypedValue.COMPLEX_UNIT_DIP,
                tunaContentMarkTextSize, tunaContentMarkTextColor, TypedValue.COMPLEX_UNIT_DIP, tunaContentMarkDx, TypedValue.COMPLEX_UNIT_DIP, tunaContentMarkDy);
    }

    public void setTunaContentMark(int tunaContentMarkRadiusUnit, float tunaContentMarkRadius, int tunaContentMarkColor, String tunaContentMarkTextValue,
                                   int tunaContentMarkTextSizeUnit, float tunaContentMarkTextSize, int tunaContentMarkTextColor, int tunaContentMarkDxUnit, float tunaContentMarkDx,
                                   int tunaContentMarkDyUnit, float tunaContentMarkDy) {

        DisplayMetrics displayMetrics = getViewDisplayMetrics(this);

        setTunaContentMarkRaw(applyDimension(tunaContentMarkRadiusUnit, tunaContentMarkRadius, displayMetrics), tunaContentMarkColor, tunaContentMarkTextValue,
                applyDimension(tunaContentMarkTextSizeUnit, tunaContentMarkTextSize, displayMetrics), tunaContentMarkTextColor,
                applyDimension(tunaContentMarkDxUnit, tunaContentMarkDx, displayMetrics), applyDimension(tunaContentMarkDyUnit, tunaContentMarkDy, displayMetrics));
    }

    private void setTunaContentMarkRaw(float tunaContentMarkRadius, int tunaContentMarkColor, String tunaContentMarkTextValue, float tunaContentMarkTextSize,
                                       int tunaContentMarkTextColor, float tunaContentMarkDx, float tunaContentMarkDy) {
        if (this.tunaContentMarkRadius != tunaContentMarkRadius || this.tunaContentMarkColor != tunaContentMarkColor || this.tunaContentMarkTextValue != tunaContentMarkTextValue
                || this.tunaContentMarkTextSize != tunaContentMarkTextSize || this.tunaContentMarkTextColor != tunaContentMarkTextColor
                || this.tunaContentMarkDx != tunaContentMarkDx || this.tunaContentMarkDy != tunaContentMarkDy) {
            this.tunaContentMarkRadius = tunaContentMarkRadius;
            this.tunaContentMarkColor = tunaContentMarkColor;
            this.tunaContentMarkTextValue = tunaContentMarkTextValue;
            this.tunaContentMarkTextSize = tunaContentMarkTextSize;
            this.tunaContentMarkTextColor = tunaContentMarkTextColor;
            this.tunaContentMarkDx = tunaContentMarkDx;
            this.tunaContentMarkDy = tunaContentMarkDy;
            this.tunaContentMark = true;
            invalidate();
        }
    }

    // tunaContentMarkTouchable default false
    private boolean tunaContentMarkTouchable;

    public boolean isTunaContentMarkTouchable() {
        return tunaContentMarkTouchable;
    }

    public void setTunaContentMarkTouchable(boolean tunaContentMarkTouchable) {
        this.tunaContentMarkTouchable = tunaContentMarkTouchable;
    }

    // tunaContentMarkRadius default 0
    private float tunaContentMarkRadius;

    public float getTunaContentMarkRadius() {
        return tunaContentMarkRadius;
    }

    public void setTunaContentMarkRadius(float tunaContentMarkRadius) {
        setTunaContentMarkRadius(TypedValue.COMPLEX_UNIT_DIP, tunaContentMarkRadius);
    }

    public void setTunaContentMarkRadius(int unit, float tunaContentMarkRadius) {
        setTunaContentMarkRadiusRaw(applyDimension(unit, tunaContentMarkRadius, getViewDisplayMetrics(this)));
    }

    private void setTunaContentMarkRadiusRaw(float tunaContentMarkRadius) {
        if (this.tunaContentMarkRadius != tunaContentMarkRadius) {
            this.tunaContentMarkRadius = tunaContentMarkRadius;
            invalidate();
        }
    }

    // tunaContentMarkColor default transparent
    private int tunaContentMarkColor;

    public int getTunaContentMarkColor() {
        return tunaContentMarkColor;
    }

    public void setTunaContentMarkColor(int tunaContentMarkColor) {
        this.tunaContentMarkColor = tunaContentMarkColor;
    }

    //
    private String tunaContentMarkTextValue;

    public String getTunaContentMarkTextValue() {
        return tunaContentMarkTextValue;
    }

    public void setTunaContentMarkTextValue(String tunaContentMarkTextValue) {
        this.tunaContentMarkTextValue = tunaContentMarkTextValue;
    }

    //
    private float tunaContentMarkTextSize;

    public float getTunaContentMarkTextSize() {
        return tunaContentMarkTextSize;
    }

    public void setTunaContentMarkTextSize(float tunaContentMarkTextSize) {
        setTunaContentMarkTextSize(TypedValue.COMPLEX_UNIT_DIP, tunaContentMarkTextSize);
    }

    public void setTunaContentMarkTextSize(int unit, float tunaContentMarkTextSize) {
        setTunaContentMarkTextSizeRaw(applyDimension(unit, tunaContentMarkTextSize, getViewDisplayMetrics(this)));
    }

    private void setTunaContentMarkTextSizeRaw(float tunaContentMarkTextSize) {
        if (this.tunaContentMarkTextSize != tunaContentMarkTextSize) {
            this.tunaContentMarkTextSize = tunaContentMarkTextSize;
            invalidate();
        }
    }

    //
    private int tunaContentMarkTextColor;

    public int getTunaContentMarkTextColor() {
        return tunaContentMarkTextColor;
    }

    public void setTunaContentMarkTextColor(int tunaContentMarkTextColor) {
        this.tunaContentMarkTextColor = tunaContentMarkTextColor;
    }

    //
    private float tunaContentMarkDx;

    public float getTunaContentMarkDx() {
        return tunaContentMarkDx;
    }

    public void setTunaContentMarkDx(float tunaContentMarkDx) {
        setTunaContentMarkDx(TypedValue.COMPLEX_UNIT_DIP, tunaContentMarkDx);
    }

    public void setTunaContentMarkDx(int unit, float tunaContentMarkDx) {
        setTunaContentMarkDxRaw(applyDimension(unit, tunaContentMarkDx, getViewDisplayMetrics(this)));
    }

    private void setTunaContentMarkDxRaw(float tunaContentMarkDx) {
        if (this.tunaContentMarkDx != tunaContentMarkDx) {
            this.tunaContentMarkDx = tunaContentMarkDx;
            invalidate();
        }
    }

    //
    private float tunaContentMarkDy;

    public float getTunaContentMarkDy() {
        return tunaContentMarkDy;
    }

    public void setTunaContentMarkDy(float tunaContentMarkDy) {
        setTunaContentMarkDy(TypedValue.COMPLEX_UNIT_DIP, tunaContentMarkDy);
    }

    public void setTunaContentMarkDy(int unit, float tunaContentMarkDy) {
        setTunaContentMarkDyRaw(applyDimension(unit, tunaContentMarkDy, getViewDisplayMetrics(this)));
    }

    private void setTunaContentMarkDyRaw(float tunaContentMarkDy) {
        if (this.tunaContentMarkDy != tunaContentMarkDy) {
            this.tunaContentMarkDy = tunaContentMarkDy;
            invalidate();
        }
    }

    //
    private float tunaContentMarkFractionDx;

    public float getTunaContentMarkFractionDx() {
        return tunaContentMarkFractionDx;
    }

    public void setTunaContentMarkFractionDx(float tunaContentMarkFractionDx) {
        this.tunaContentMarkFractionDx = tunaContentMarkFractionDx;
    }

    //
    private float tunaContentMarkFractionDy;

    public float getTunaContentMarkFractionDy() {
        return tunaContentMarkFractionDy;
    }

    public void setTunaContentMarkFractionDy(float tunaContentMarkFractionDy) {
        this.tunaContentMarkFractionDy = tunaContentMarkFractionDy;
    }

    //
    private float tunaContentDrawWidth;
    private float tunaContentEndOffsetCenterX;
    private float tunaContentEndOffsetCenterY;

    //
    private Bitmap tunaSrcLeft;

    public Bitmap getTunaBitmapSrcLeft() {
        return tunaSrcLeft;
    }

    public void setTunaBitmapSrcLeft(Bitmap tunaSrcLeft) {
        this.tunaSrcLeft = tunaSrcLeft;
    }

    //
    private float tunaSrcLeftWidth;

    public float getTunaBitmapSrcLeftWidth() {
        return tunaSrcLeftWidth;
    }

    public void setTunaBitmapSrcLeftWidth(float tunaSrcLeftWidth) {
        setTunaBitmapSrcLeftWidth(TypedValue.COMPLEX_UNIT_DIP, tunaSrcLeftWidth);
    }

    public void setTunaBitmapSrcLeftWidth(int unit, float tunaSrcLeftWidth) {
        setTunaBitmapSrcLeftWidthRaw(applyDimension(unit, tunaSrcLeftWidth, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcLeftWidthRaw(float tunaSrcLeftWidth) {
        if (this.tunaSrcLeftWidth != tunaSrcLeftWidth) {
            this.tunaSrcLeftWidth = tunaSrcLeftWidth;
            invalidate();
        }
    }

    //
    private float tunaSrcLeftHeight;

    public float getTunaBitmapSrcLeftHeight() {
        return tunaSrcLeftHeight;
    }

    public void setTunaBitmapSrcLeftHeight(float tunaSrcLeftHeight) {
        setTunaBitmapSrcLeftHeight(TypedValue.COMPLEX_UNIT_DIP, tunaSrcLeftHeight);
    }

    public void setTunaBitmapSrcLeftHeight(int unit, float tunaSrcLeftHeight) {
        setTunaBitmapSrcLeftHeightRaw(applyDimension(unit, tunaSrcLeftHeight, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcLeftHeightRaw(float tunaSrcLeftHeight) {
        if (this.tunaSrcLeftHeight != tunaSrcLeftHeight) {
            this.tunaSrcLeftHeight = tunaSrcLeftHeight;
            invalidate();
        }
    }

    //
    protected Matrix tunaLeftMatrix;

    public Matrix getTunaLeftMatrix() {
        return tunaLeftMatrix;
    }

    public void setTunaLeftMatrix(Matrix tunaLeftMatrix) {
        this.tunaLeftMatrix = tunaLeftMatrix;
    }

    protected Matrix initTunaLeftMatrix(float sx, float sy) {
        if (tunaLeftMatrix == null) {
            tunaLeftMatrix = new Matrix();
        }
        tunaLeftMatrix.reset();
        tunaLeftMatrix.setScale(sx, sy);
        return tunaLeftMatrix;
    }

    // tunaSrcLeftPadding means distance between tunaSrcLeft and textview,note
    // about the tunaTextPaddingLeft
    private float tunaSrcLeftPadding;

    public float getTunaBitmapSrcLeftPadding() {
        return tunaSrcLeftPadding;
    }

    public void setTunaBitmapSrcLeftPadding(float tunaSrcLeftPadding) {
        setTunaBitmapSrcLeftPadding(TypedValue.COMPLEX_UNIT_DIP, tunaSrcLeftPadding);
    }

    public void setTunaBitmapSrcLeftPadding(int unit, float tunaSrcLeftPadding) {
        setTunaBitmapSrcLeftPaddingRaw(applyDimension(unit, tunaSrcLeftPadding, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcLeftPaddingRaw(float tunaSrcLeftPadding) {
        if (this.tunaSrcLeftPadding != tunaSrcLeftPadding) {
            this.tunaSrcLeftPadding = tunaSrcLeftPadding;
            invalidate();
        }
    }

    //
    private float tunaSrcLeftDx;

    public float getTunaBitmapSrcLeftDx() {
        return tunaSrcLeftDx;
    }

    public void setTunaBitmapSrcLeftDx(float tunaSrcLeftDx) {
        setTunaBitmapSrcLeftDx(TypedValue.COMPLEX_UNIT_DIP, tunaSrcLeftDx);
    }

    public void setTunaBitmapSrcLeftDx(int unit, float tunaSrcLeftDx) {
        setTunaBitmapSrcLeftDxRaw(applyDimension(unit, tunaSrcLeftDx, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcLeftDxRaw(float tunaSrcLeftDx) {
        if (this.tunaSrcLeftDx != tunaSrcLeftDx) {
            this.tunaSrcLeftDx = tunaSrcLeftDx;
            invalidate();
        }
    }

    //
    private float tunaSrcLeftDy;

    public float getTunaBitmapSrcLeftDy() {
        return tunaSrcLeftDy;
    }

    public void setTunaBitmapSrcLeftDy(float tunaSrcLeftDy) {
        setTunaBitmapSrcLeftDy(TypedValue.COMPLEX_UNIT_DIP, tunaSrcLeftDy);
    }

    public void setTunaBitmapSrcLeftDy(int unit, float tunaSrcLeftDy) {
        setTunaBitmapSrcLeftDyRaw(applyDimension(unit, tunaSrcLeftDy, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcLeftDyRaw(float tunaSrcLeftDy) {
        if (this.tunaSrcLeftDy != tunaSrcLeftDy) {
            this.tunaSrcLeftDy = tunaSrcLeftDy;
            invalidate();
        }
    }

    //
    private Bitmap tunaSrcRight;

    public Bitmap getTunaBitmapSrcRight() {
        return tunaSrcRight;
    }

    public void setTunaBitmapSrcRight(Bitmap tunaSrcRight) {
        this.tunaSrcRight = tunaSrcRight;
    }

    //
    private float tunaSrcRightWidth;

    public float getTunaBitmapSrcRightWidth() {
        return tunaSrcRightWidth;
    }

    public void setTunaBitmapSrcRightWidth(float tunaSrcRightWidth) {
        setTunaBitmapSrcRightWidth(TypedValue.COMPLEX_UNIT_DIP, tunaSrcRightWidth);
    }

    public void setTunaBitmapSrcRightWidth(int unit, float tunaSrcRightWidth) {
        setTunaBitmapSrcRightWidthRaw(applyDimension(unit, tunaSrcRightWidth, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcRightWidthRaw(float tunaSrcRightWidth) {
        if (this.tunaSrcRightWidth != tunaSrcRightWidth) {
            this.tunaSrcRightWidth = tunaSrcRightWidth;
            invalidate();
        }
    }

    //
    private float tunaSrcRightHeight;

    public float getTunaBitmapSrcRightHeight() {
        return tunaSrcRightHeight;
    }

    public void setTunaBitmapSrcRightHeight(float tunaSrcRightHeight) {
        setTunaBitmapSrcRightHeight(TypedValue.COMPLEX_UNIT_DIP, tunaSrcRightHeight);
    }

    public void setTunaBitmapSrcRightHeight(int unit, float tunaSrcRightHeight) {
        setTunaBitmapSrcRightHeightRaw(applyDimension(unit, tunaSrcRightHeight, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcRightHeightRaw(float tunaSrcRightHeight) {
        if (this.tunaSrcRightHeight != tunaSrcRightHeight) {
            this.tunaSrcRightHeight = tunaSrcRightHeight;
            invalidate();
        }
    }

    //
    protected Matrix tunaRightMatrix;

    public Matrix getTunaRightMatrix() {
        return tunaRightMatrix;
    }

    public void setTunaRightMatrix(Matrix tunaRightMatrix) {
        this.tunaRightMatrix = tunaRightMatrix;
    }

    protected Matrix initTunaRightMatrix(float sx, float sy) {
        if (tunaRightMatrix == null) {
            tunaRightMatrix = new Matrix();
        }
        tunaRightMatrix.reset();
        tunaRightMatrix.setScale(sx, sy);
        return tunaRightMatrix;
    }

    // tunaSrcRightPadding means distance between tunaSrcRight and textview,note
    // about the tunaTextPaddingRight
    private float tunaSrcRightPadding;

    public float getTunaBitmapSrcRightPadding() {
        return tunaSrcRightPadding;
    }

    public void setTunaBitmapSrcRightPadding(float tunaSrcRightPadding) {
        setTunaBitmapSrcRightPadding(TypedValue.COMPLEX_UNIT_DIP, tunaSrcRightPadding);
    }

    public void setTunaBitmapSrcRightPadding(int unit, float tunaSrcRightPadding) {
        setTunaBitmapSrcRightPaddingRaw(applyDimension(unit, tunaSrcRightPadding, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcRightPaddingRaw(float tunaSrcRightPadding) {
        if (this.tunaSrcRightPadding != tunaSrcRightPadding) {
            this.tunaSrcRightPadding = tunaSrcRightPadding;
            invalidate();
        }
    }

    //
    private float tunaSrcRightDx;

    public float getTunaBitmapSrcRightDx() {
        return tunaSrcRightDx;
    }

    public void setTunaBitmapSrcRightDx(float tunaSrcRightDx) {
        setTunaBitmapSrcRightDx(TypedValue.COMPLEX_UNIT_DIP, tunaSrcRightDx);
    }

    public void setTunaBitmapSrcRightDx(int unit, float tunaSrcRightDx) {
        setTunaBitmapSrcRightDxRaw(applyDimension(unit, tunaSrcRightDx, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcRightDxRaw(float tunaSrcRightDx) {
        if (this.tunaSrcRightDx != tunaSrcRightDx) {
            this.tunaSrcRightDx = tunaSrcRightDx;
            invalidate();
        }
    }

    //
    private float tunaSrcRightDy;

    public float getTunaBitmapSrcRightDy() {
        return tunaSrcRightDy;
    }

    public void setTunaBitmapSrcRightDy(float tunaSrcRightDy) {
        setTunaBitmapSrcRightDy(TypedValue.COMPLEX_UNIT_DIP, tunaSrcRightDy);
    }

    public void setTunaBitmapSrcRightDy(int unit, float tunaSrcRightDy) {
        setTunaBitmapSrcRightDyRaw(applyDimension(unit, tunaSrcRightDy, getViewDisplayMetrics(this)));
    }

    private void setTunaBitmapSrcRightDyRaw(float tunaSrcRightDy) {
        if (this.tunaSrcRightDy != tunaSrcRightDy) {
            this.tunaSrcRightDy = tunaSrcRightDy;
            invalidate();
        }
    }

    // attention tunaPorterDuffXfermode default 0 instead of -1!
    protected PorterDuffXfermode tunaPorterDuffXfermode;

    public enum TunaPorterDuffXfermode {
        SRC_IN(0), SRC_OUT(1),;
        final int nativeInt;

        TunaPorterDuffXfermode(int ni) {
            nativeInt = ni;
        }
    }

    private static final Mode[] tunaPorterDuffXfermodeArray = {PorterDuff.Mode.SRC_IN, PorterDuff.Mode.SRC_OUT,};

    public PorterDuffXfermode getTunaPorterDuffXfermode() {
        return tunaPorterDuffXfermode;
    }

    public void setTunaPorterDuffXfermode(TunaPorterDuffXfermode tunaXfermode) {
        this.tunaPorterDuffXfermode = new PorterDuffXfermode(tunaPorterDuffXfermodeArray[tunaXfermode.nativeInt]);
    }

    //
    private boolean tunaMaterial;

    public boolean isTunaMaterial() {
        return tunaMaterial;
    }

    public void setTunaMaterial(boolean tunaMaterial) {
        this.tunaMaterial = tunaMaterial;
    }

    //
    private boolean tunaMaterialSpread;

    public boolean isTunaMaterialSpread() {
        return tunaMaterialSpread;
    }

    public void setTunaMaterialSpread(boolean tunaMaterialSpread) {
        this.tunaMaterialSpread = tunaMaterialSpread;
    }

    //
    private float tunaMaterialRadius;

    public float getTunaMaterialRadius() {
        return tunaMaterialRadius;
    }

    public void setTunaMaterialRadius(float tunaMaterialRadius) {
        this.tunaMaterialRadius = tunaMaterialRadius;
    }

    //
    private int tunaMaterialDuraction = 500;

    public int getTunaMaterialDuraction() {
        return tunaMaterialDuraction;
    }

    public void setTunaMaterialDuraction(int tunaMaterialDuraction) {
        this.tunaMaterialDuraction = tunaMaterialDuraction;
    }

    //
    private boolean tunaMaterialPlay;

    public boolean isTunaMaterialPlay() {
        return tunaMaterialPlay;
    }

    public void setTunaMaterialPlay(boolean tunaMaterialPlay) {
        this.tunaMaterialPlay = tunaMaterialPlay;
    }

    //
    private TimeInterpolator tunaMaterialTimeInterpolator;

    public TimeInterpolator getTunaMaterialTimeInterpolator() {
        return tunaMaterialTimeInterpolator;
    }

    public void setTunaMaterialTimeInterpolator(TimeInterpolator tunaMaterialTimeInterpolator) {
        this.tunaMaterialTimeInterpolator = tunaMaterialTimeInterpolator;
    }

    //
    public enum TunaMaterialTimeInterpolator {
        ACCELERATEDECELERATEINTERPOLATOR(0), ACCELERATEINTERPOLATOR(1), ANTICIPATEINTERPOLATOR(2), ANTICIPATEOVERSHOOTINTERPOLATOR(3), BOUNCEINTERPOLATOR(4), CYCLEINTERPOLATOR(5), DECELERATEINTERPOLATOR(
                6), LINEARINTERPOLATOR(7), OVERSHOOTINTERPOLATOR(8),;
        final int nativeInt;

        TunaMaterialTimeInterpolator(int ni) {
            nativeInt = ni;
        }
    }

    //
    private static final TimeInterpolator[] tunaMaterialTimeInterpolatorArray = {new AccelerateDecelerateInterpolator(), new AccelerateInterpolator(),
            new AnticipateInterpolator(), new AnticipateOvershootInterpolator(), new BounceInterpolator(), new CycleInterpolator(0), new DecelerateInterpolator(),
            new LinearInterpolator(), new OvershootInterpolator(),};

    //
    private AnimatorSet tunaMaterialAnimatorSet;

    public AnimatorSet getTunaMaterialAnimatorSet() {
        return tunaMaterialAnimatorSet;
    }

    public void setTunaMaterialAnimatorSet(AnimatorSet tunaMaterialAnimatorSet) {
        this.tunaMaterialAnimatorSet = tunaMaterialAnimatorSet;
    }

    //
    private Property<TunaView, Float> tunaMaterialRadiusProperty = new Property<TunaView, Float>(Float.class, "tunaMaterialRadius") {
        @Override
        public Float get(TunaView object) {
            return object.tunaMaterialRadius;
        }

        @Override
        public void set(TunaView object, Float value) {
            object.tunaMaterialRadius = value;
            invalidate();
        }
    };

    //
    private Property<TunaView, Float> tunaMaterialPaintXProperty = new Property<TunaView, Float>(Float.class, "tunaMaterialPaintX") {
        @Override
        public Float get(TunaView object) {
            return object.tunaTouchDownEventX;
        }

        @Override
        public void set(TunaView object, Float value) {
            object.tunaTouchDownEventX = value;
        }
    };

    //
    private Property<TunaView, Float> tunaMaterialPaintYProperty = new Property<TunaView, Float>(Float.class, "tunaMaterialPaintY") {
        @Override
        public Float get(TunaView object) {
            return object.tunaTouchDownEventY;
        }

        @Override
        public void set(TunaView object, Float value) {
            object.tunaTouchDownEventY = value;
        }
    };

    // This setting will cause the following message appears reading xml
    // The graphics preview in the layout editor may not be accurate: Paint
    // Flags Draw Filters are not supported. (Ignore for this session)
    private PaintFlagsDrawFilter tunaPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public PaintFlagsDrawFilter getTunaPaintFlagsDrawFilter() {
        return tunaPaintFlagsDrawFilter;
    }

    public void setTunaPaintFlagsDrawFilter(PaintFlagsDrawFilter tunaPaintFlagsDrawFilter) {
        this.tunaPaintFlagsDrawFilter = tunaPaintFlagsDrawFilter;
    }

    //
    private static int touchDownCount;
    private static long touchDownTimeStart, touchDownTimeEnd;
    private static float tunaTouchDownEventX, tunaTouchDownEventY;
    private static final int TOUCH_DOWN_TIMES = 3;
    private static final int SHOW_PROPERTY_MAX_DISTANCE_DP = 10;
    // in response to the dispathtouch event: need touch TOUCH_DOWN_TIMES
    // consecutive times within SHOW_PROPERTY_MAX_TIME_MILLIS ,
    // and the touch location can not exceed SHOW_PROPERTY_MAX_DISTANCE_DIP,
    // tunaTouchDownEventX and tunaTouchDownEventY position refresh when pressed
    // eachtimes
    private static final int SHOW_PROPERTY_MAX_TIME_MILLIS = 200 * TOUCH_DOWN_TIMES;

    public TunaView(Context context) {
        this(context, null);
    }

    public TunaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TunaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //
        tunaDebugable = (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));

        //
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TunaView, 0, defStyle);
//
//        //
//
//        // tunaTouchType default edge
//        int tunaTouchTypeIndex = typedArray.getInt(R.styleable.TunaView_tunaTouchType, 0);
//        tunaTouchType = tunaTouchTypeArray[tunaTouchTypeIndex];
//
//        tunaPress = typedArray.getBoolean(R.styleable.TunaView_tunaPress, false);
//        tunaSelect = typedArray.getBoolean(R.styleable.TunaView_tunaSelect, false);
//
//        // tunaSelectType default none
//        int tunaSelectTypeIndex = typedArray.getInt(R.styleable.TunaView_tunaSelectType, 0);
//        tunaSelectType = tunaSelectTypeArray[tunaSelectTypeIndex];
//
//        tunaAnimationable = typedArray.getBoolean(R.styleable.TunaView_tunaAnimationable, false);
//
//        tunaRotate = typedArray.getInt(R.styleable.TunaView_tunaRotate, 0);

        tunaSuper = TunaView.class == this.getClass();

//        if (tunaSuper) {
//            // note that the use of default values ​​can be
//            // defined,tunaBackgroundNormal to the default white to achieve clip
//            // tunaBitmap results!
//
//            tunaBackgroundNormal = typedArray.getColor(R.styleable.TunaView_tunaBackgroundNormal, Color.TRANSPARENT);
//            tunaBackgroundPress = typedArray.getColor(R.styleable.TunaView_tunaBackgroundPress, tunaBackgroundNormal);
//            tunaBackgroundSelect = typedArray.getColor(R.styleable.TunaView_tunaBackgroundSelect, tunaBackgroundNormal);
//
//            tunaForegroundNormal = typedArray.getColor(R.styleable.TunaView_tunaForegroundNormal, Color.TRANSPARENT);
//            tunaForegroundPress = typedArray.getColor(R.styleable.TunaView_tunaForegroundPress, tunaForegroundNormal);
//            tunaForegroundSelect = typedArray.getColor(R.styleable.TunaView_tunaForegroundSelect, tunaForegroundNormal);
//
//            //
//            tunaBackgroundNormalAngle = typedArray.getInt(R.styleable.TunaView_tunaBackgroundNormalAngle, Integer.MAX_VALUE);
//            if (tunaBackgroundNormalAngle != Integer.MAX_VALUE) {
//                tunaBackgroundNormalGradientStart = typedArray.getColor(R.styleable.TunaView_tunaBackgroundNormalGradientStart, tunaBackgroundNormal);
//                tunaBackgroundNormalGradientEnd = typedArray.getColor(R.styleable.TunaView_tunaBackgroundNormalGradientEnd, tunaBackgroundNormal);
//            }
//
//            tunaBackgroundPressAngle = typedArray.getInt(R.styleable.TunaView_tunaBackgroundPressAngle, Integer.MAX_VALUE);
//            if (tunaBackgroundPressAngle != Integer.MAX_VALUE) {
//                tunaBackgroundPressGradientStart = typedArray.getColor(R.styleable.TunaView_tunaBackgroundPressGradientStart, tunaBackgroundPress);
//                tunaBackgroundPressGradientEnd = typedArray.getColor(R.styleable.TunaView_tunaBackgroundPressGradientEnd, tunaBackgroundPress);
//            }
//
//            tunaBackgroundSelectAngle = typedArray.getInt(R.styleable.TunaView_tunaBackgroundSelectAngle, Integer.MAX_VALUE);
//            if (tunaBackgroundSelectAngle != Integer.MAX_VALUE) {
//                tunaBackgroundSelectGradientStart = typedArray.getColor(R.styleable.TunaView_tunaBackgroundSelectGradientStart, tunaBackgroundSelect);
//                tunaBackgroundSelectGradientEnd = typedArray.getColor(R.styleable.TunaView_tunaBackgroundSelectGradientEnd, tunaBackgroundSelect);
//            }
//
//            //
//            tunaBackgroundNormalShadowRadius = typedArray.getDimension(R.styleable.TunaView_tunaBackgroundNormalShadowRadius, 0);
//            if (tunaBackgroundNormalShadowRadius > 0) {
//                tunaBackgroundNormalShadowColor = typedArray.getColor(R.styleable.TunaView_tunaBackgroundNormalShadowColor, Color.TRANSPARENT);
//                tunaBackgroundNormalShadowDx = typedArray.getDimension(R.styleable.TunaView_tunaBackgroundNormalShadowDx, 0);
//                tunaBackgroundNormalShadowDy = typedArray.getDimension(R.styleable.TunaView_tunaBackgroundNormalShadowDy, 0);
//            }
//
//            //
//            tunaBackgroundPressShadowRadius = typedArray.getDimension(R.styleable.TunaView_tunaBackgroundPressShadowRadius, tunaBackgroundNormalShadowRadius);
//            if (tunaBackgroundPressShadowRadius > 0) {
//                tunaBackgroundPressShadowColor = typedArray.getColor(R.styleable.TunaView_tunaBackgroundPressShadowColor, tunaBackgroundNormalShadowColor);
//                tunaBackgroundPressShadowDx = typedArray.getDimension(R.styleable.TunaView_tunaBackgroundPressShadowDx, tunaBackgroundNormalShadowDx);
//                tunaBackgroundPressShadowDy = typedArray.getDimension(R.styleable.TunaView_tunaBackgroundPressShadowDy, tunaBackgroundNormalShadowDy);
//            }
//
//            //
//            tunaBackgroundSelectShadowRadius = typedArray.getDimension(R.styleable.TunaView_tunaBackgroundSelectShadowRadius, tunaBackgroundNormalShadowRadius);
//            if (tunaBackgroundSelectShadowRadius > 0) {
//                tunaBackgroundSelectShadowColor = typedArray.getColor(R.styleable.TunaView_tunaBackgroundSelectShadowColor, tunaBackgroundNormalShadowColor);
//                tunaBackgroundSelectShadowDx = typedArray.getDimension(R.styleable.TunaView_tunaBackgroundSelectShadowDx, tunaBackgroundNormalShadowDx);
//                tunaBackgroundSelectShadowDy = typedArray.getDimension(R.styleable.TunaView_tunaBackgroundSelectShadowDy, tunaBackgroundNormalShadowDy);
//            }
//
//            //
//            int tunaSrcNormalId = typedArray.getResourceId(R.styleable.TunaView_tunaSrcNormal, -1);
//            if (tunaSrcNormalId != -1) {
//
//                // tunaXfermodeIndex default PorterDuff.Mode.SRC_IN
//                int tunaXfermodeIndex = typedArray.getInt(R.styleable.TunaView_tunaPorterDuffXfermode, 0);
//                tunaPorterDuffXfermode = new PorterDuffXfermode(tunaPorterDuffXfermodeArray[tunaXfermodeIndex]);
//
//                //
//                tunaSrcNormal = BitmapFactory.decodeResource(getResources(), tunaSrcNormalId);
//
//                //
//                tunaSrcNormalShadowRadius = typedArray.getDimension(R.styleable.TunaView_tunaSrcNormalShadowRadius, 0);
//                if (tunaSrcNormalShadowRadius > 0) {
//                    tunaSrcNormalShadowDx = typedArray.getDimension(R.styleable.TunaView_tunaSrcNormalShadowDx, 0);
//                    tunaSrcNormalShadowDy = typedArray.getDimension(R.styleable.TunaView_tunaSrcNormalShadowDy, 0);
//                }
//                //
//                tunaSrcPressShadowRadius = typedArray.getDimension(R.styleable.TunaView_tunaSrcPressShadowRadius, tunaSrcNormalShadowRadius);
//                if (tunaSrcPressShadowRadius > 0) {
//                    tunaSrcPressShadowDx = typedArray.getDimension(R.styleable.TunaView_tunaSrcPressShadowDx, tunaSrcNormalShadowDx);
//                    tunaSrcPressShadowDy = typedArray.getDimension(R.styleable.TunaView_tunaSrcPressShadowDy, tunaSrcNormalShadowDy);
//                }
//                //
//                tunaSrcSelectShadowRadius = typedArray.getDimension(R.styleable.TunaView_tunaSrcSelectShadowRadius, tunaSrcNormalShadowRadius);
//                if (tunaSrcSelectShadowRadius > 0) {
//                    tunaSrcSelectShadowDx = typedArray.getDimension(R.styleable.TunaView_tunaSrcSelectShadowDx, tunaSrcNormalShadowDx);
//                    tunaSrcSelectShadowDy = typedArray.getDimension(R.styleable.TunaView_tunaSrcSelectShadowDy, tunaSrcNormalShadowDy);
//                }
//
//                //
//                int tunaSrcPressId = typedArray.getResourceId(R.styleable.TunaView_tunaSrcPress, -1);
//                if (tunaSrcPressId != -1) {
//                    tunaSrcPress = BitmapFactory.decodeResource(getResources(), tunaSrcPressId);
//                }
//                else {
//                    tunaSrcPress = tunaSrcNormal;
//                }
//                //
//                int tunaSrcSelectId = typedArray.getResourceId(R.styleable.TunaView_tunaSrcPress, -1);
//                if (tunaSrcSelectId != -1) {
//                    tunaSrcSelect = BitmapFactory.decodeResource(getResources(), tunaSrcSelectId);
//                }
//                else {
//                    tunaSrcSelect = tunaSrcNormal;
//                }
//            }
//
//            //
//            int tunaSrcAnchorNormalId = typedArray.getResourceId(R.styleable.TunaView_tunaSrcAnchorNormal, -1);
//            if (tunaSrcAnchorNormalId != -1) {
//
//                //
//                tunaSrcAnchorNormal = BitmapFactory.decodeResource(getResources(), tunaSrcAnchorNormalId);
//                tunaSrcAnchorNormalWidth = typedArray.getDimension(R.styleable.TunaView_tunaSrcAnchorNormalWidth, 0);
//                tunaSrcAnchorNormalHeight = typedArray.getDimension(R.styleable.TunaView_tunaSrcAnchorNormalHeight, 0);
//                tunaSrcAnchorNormalDx = typedArray.getDimension(R.styleable.TunaView_tunaSrcAnchorNormalDx, 0);
//                tunaSrcAnchorNormalDy = typedArray.getDimension(R.styleable.TunaView_tunaSrcAnchorNormalDy, 0);
//                //
//                tunaSrcAnchorPressWidth = typedArray.getDimension(R.styleable.TunaView_tunaSrcAnchorPressWidth, tunaSrcAnchorNormalWidth);
//                tunaSrcAnchorPressHeight = typedArray.getDimension(R.styleable.TunaView_tunaSrcAnchorPressHeight, tunaSrcAnchorNormalHeight);
//                tunaSrcAnchorPressDx = typedArray.getDimension(R.styleable.TunaView_tunaSrcAnchorPressDx, tunaSrcAnchorNormalDx);
//                tunaSrcAnchorPressDy = typedArray.getDimension(R.styleable.TunaView_tunaSrcAnchorPressDy, tunaSrcAnchorNormalDy);
//                //
//                tunaSrcAnchorSelectWidth = typedArray.getDimension(R.styleable.TunaView_tunaSrcAnchorSelectWidth, tunaSrcAnchorNormalWidth);
//                tunaSrcAnchorSelectHeight = typedArray.getDimension(R.styleable.TunaView_tunaSrcAnchorSelectHeight, tunaSrcAnchorNormalHeight);
//                tunaSrcAnchorSelectDx = typedArray.getDimension(R.styleable.TunaView_tunaSrcAnchorSelectDx, tunaSrcAnchorNormalDx);
//                tunaSrcAnchorSelectDy = typedArray.getDimension(R.styleable.TunaView_tunaSrcAnchorSelectDy, tunaSrcAnchorNormalDy);
//
//                //
//                int tunaSrcAnchorPressId = typedArray.getResourceId(R.styleable.TunaView_tunaSrcAnchorPress, -1);
//                if (tunaSrcAnchorPressId != -1) {
//                    tunaSrcAnchorPress = BitmapFactory.decodeResource(getResources(), tunaSrcAnchorPressId);
//                }
//                else {
//                    tunaSrcAnchorPress = tunaSrcAnchorNormal;
//                }
//                //
//                int tunaSrcAnchorSelectId = typedArray.getResourceId(R.styleable.TunaView_tunaSrcAnchorSelect, -1);
//                if (tunaSrcAnchorSelectId != -1) {
//                    tunaSrcAnchorSelect = BitmapFactory.decodeResource(getResources(), tunaSrcAnchorSelectId);
//                }
//                else {
//                    tunaSrcAnchorSelect = tunaSrcAnchorNormal;
//                }
//
//                //
//                tunaSrcAnchorGravity = typedArray.getInt(R.styleable.TunaView_tunaSrcAnchorGravity, 0);
//            }
//
//            //
//            int tunaSrcLeftId = typedArray.getResourceId(R.styleable.TunaView_tunaSrcLeft, -1);
//            if (tunaSrcLeftId != -1) {
//                tunaSrcLeft = BitmapFactory.decodeResource(getResources(), tunaSrcLeftId);
//                tunaSrcLeftWidth = typedArray.getDimension(R.styleable.TunaView_tunaSrcLeftWidth, 0);
//                tunaSrcLeftHeight = typedArray.getDimension(R.styleable.TunaView_tunaSrcLeftHeight, 0);
//                tunaSrcLeftPadding = typedArray.getDimension(R.styleable.TunaView_tunaSrcLeftPadding, 0);
//                tunaSrcLeftDx = typedArray.getDimension(R.styleable.TunaView_tunaSrcLeftDx, 0);
//                tunaSrcLeftDy = typedArray.getDimension(R.styleable.TunaView_tunaSrcLeftDy, 0);
//
//                if (tunaSrcLeftWidth == 0 || tunaSrcLeftHeight == 0) {
//                    throw new IllegalArgumentException("The content attribute require property named tunaSrcLeftWidth and tunaSrcLeftHeight");
//                }
//            }
//
//            //
//            int tunaSrcRightId = typedArray.getResourceId(R.styleable.TunaView_tunaSrcRight, -1);
//            if (tunaSrcRightId != -1) {
//                tunaSrcRight = BitmapFactory.decodeResource(getResources(), tunaSrcRightId);
//                tunaSrcRightWidth = typedArray.getDimension(R.styleable.TunaView_tunaSrcRightWidth, 0);
//                tunaSrcRightHeight = typedArray.getDimension(R.styleable.TunaView_tunaSrcRightHeight, 0);
//                tunaSrcRightPadding = typedArray.getDimension(R.styleable.TunaView_tunaSrcRightPadding, 0);
//                tunaSrcRightDx = typedArray.getDimension(R.styleable.TunaView_tunaSrcRightDx, 0);
//                tunaSrcRightDy = typedArray.getDimension(R.styleable.TunaView_tunaSrcRightDy, 0);
//
//                if (tunaSrcRightWidth == 0 || tunaSrcRightHeight == 0) {
//                    throw new IllegalArgumentException("The content attribute require property named tunaSrcRightWidth and tunaSrcRightHeight");
//                }
//            }
//
//            //
//            tunaTextMark = typedArray.getBoolean(R.styleable.TunaView_tunaTextMark, false);
//            tunaTextMarkTouchable = typedArray.getBoolean(R.styleable.TunaView_tunaTextMarkTouchable, false);
//            tunaTextMarkRadius = typedArray.getDimension(R.styleable.TunaView_tunaTextMarkRadius, 0);
//            tunaTextMarkColor = typedArray.getColor(R.styleable.TunaView_tunaTextMarkColor, Color.TRANSPARENT);
//            tunaTextMarkTextValue = typedArray.getString(R.styleable.TunaView_tunaTextMarkTextValue);
//            tunaTextMarkTextSize = typedArray.getDimension(R.styleable.TunaView_tunaTextMarkTextSize, 0);
//            tunaTextMarkTextColor = typedArray.getColor(R.styleable.TunaView_tunaTextMarkTextColor, Color.TRANSPARENT);
//
//            //
//            tunaContentMark = typedArray.getBoolean(R.styleable.TunaView_tunaContentMark, false);
//            tunaContentMarkTouchable = typedArray.getBoolean(R.styleable.TunaView_tunaContentMarkTouchable, false);
//            tunaContentMarkRadius = typedArray.getDimension(R.styleable.TunaView_tunaContentMarkRadius, 0);
//            tunaContentMarkColor = typedArray.getColor(R.styleable.TunaView_tunaContentMarkColor, Color.TRANSPARENT);
//            tunaContentMarkTextValue = typedArray.getString(R.styleable.TunaView_tunaContentMarkTextValue);
//            tunaContentMarkTextSize = typedArray.getDimension(R.styleable.TunaView_tunaContentMarkTextSize, 0);
//            tunaContentMarkTextColor = typedArray.getColor(R.styleable.TunaView_tunaContentMarkTextColor, Color.TRANSPARENT);
//
//            //
//            tunaStrokeWidthNormal = typedArray.getDimension(R.styleable.TunaView_tunaStrokeWidthNormal, 0);
//            tunaStrokeColorNormal = typedArray.getColor(R.styleable.TunaView_tunaStrokeColorNormal, Color.TRANSPARENT);
//            tunaStrokeWidthPress = typedArray.getDimension(R.styleable.TunaView_tunaStrokeWidthPress, tunaStrokeWidthNormal);
//            tunaStrokeColorPress = typedArray.getColor(R.styleable.TunaView_tunaStrokeColorPress, tunaStrokeColorNormal);
//            tunaStrokeWidthSelect = typedArray.getDimension(R.styleable.TunaView_tunaStrokeWidthSelect, tunaStrokeWidthNormal);
//            tunaStrokeColorSelect = typedArray.getColor(R.styleable.TunaView_tunaStrokeColorSelect, tunaStrokeColorNormal);
//
//            //
//            tunaRadius = typedArray.getDimension(R.styleable.TunaView_tunaRadius, 0);
//            tunaRadiusLeftTop = typedArray.getDimension(R.styleable.TunaView_tunaRadiusLeftTop, tunaRadius);
//            tunaRadiusLeftBottom = typedArray.getDimension(R.styleable.TunaView_tunaRadiusLeftBottom, tunaRadius);
//            tunaRadiusRightTop = typedArray.getDimension(R.styleable.TunaView_tunaRadiusRightTop, tunaRadius);
//            tunaRadiusRightBottom = typedArray.getDimension(R.styleable.TunaView_tunaRadiusRightBottom, tunaRadius);
//
//            tunaClassic = (tunaRadius == tunaRadiusLeftTop && tunaRadiusLeftTop == tunaRadiusLeftBottom && tunaRadiusLeftBottom == tunaRadiusRightTop && tunaRadiusRightTop == tunaRadiusRightBottom);
//
//            //
//            tunaTextValue = typedArray.getString(R.styleable.TunaView_tunaTextValue);
//            tunaTextSize = typedArray.getDimension(R.styleable.TunaView_tunaTextSize, 0);
//
//            tunaTextColorNormal = typedArray.getColor(R.styleable.TunaView_tunaTextColorNormal, Color.TRANSPARENT);
//            tunaTextColorPress = typedArray.getColor(R.styleable.TunaView_tunaTextColorPress, tunaTextColorNormal);
//            tunaTextColorSelect = typedArray.getColor(R.styleable.TunaView_tunaTextColorSelect, tunaTextColorNormal);
//
//            tunaTextPaddingLeft = typedArray.getDimension(R.styleable.TunaView_tunaTextPaddingLeft, 0);
//            tunaTextPaddingRight = typedArray.getDimension(R.styleable.TunaView_tunaTextPaddingRight, 0);
//
//            //
//            int tunaTextGravityIndex = typedArray.getInt(R.styleable.TunaView_tunaTextGravity, 0);
//            if (tunaTextGravityIndex >= 0) {
//                tunaTextGravity = tunaTextGravityArray[tunaTextGravityIndex];
//            }
//
//            tunaTextDx = typedArray.getDimension(R.styleable.TunaView_tunaTextDx, 0);
//            tunaTextDy = typedArray.getDimension(R.styleable.TunaView_tunaTextDy, 0);
//            tunaTextFractionDx = typedArray.getFraction(R.styleable.TunaView_tunaTextFractionDx, 1, 1, 0);
//            tunaTextFractionDy = typedArray.getFraction(R.styleable.TunaView_tunaTextFractionDy, 1, 1, 0);
//
//            //
//            tunaTextShadowRadius = typedArray.getDimension(R.styleable.TunaView_tunaTextShadowRadius, 0);
//            if (tunaTextShadowRadius > 0) {
//                tunaTextShadowColor = typedArray.getColor(R.styleable.TunaView_tunaTextShadowColor, Color.TRANSPARENT);
//                tunaTextShadowDx = typedArray.getDimension(R.styleable.TunaView_tunaTextShadowDx, 0);
//                tunaTextShadowDy = typedArray.getDimension(R.styleable.TunaView_tunaTextShadowDy, 0);
//            }
//
//            //
//            tunaContentValue = typedArray.getString(R.styleable.TunaView_tunaContentValue);
//            tunaContentSize = typedArray.getDimension(R.styleable.TunaView_tunaContentSize, 0);
//
//            tunaContentColorNormal = typedArray.getColor(R.styleable.TunaView_tunaContentColorNormal, Color.TRANSPARENT);
//            tunaContentColorPress = typedArray.getColor(R.styleable.TunaView_tunaContentColorPress, tunaContentColorNormal);
//            tunaContentColorSelect = typedArray.getColor(R.styleable.TunaView_tunaContentColorSelect, tunaContentColorNormal);
//
//            tunaContentPaddingLeft = typedArray.getDimension(R.styleable.TunaView_tunaContentPaddingLeft, 0);
//            tunaContentPaddingRight = typedArray.getDimension(R.styleable.TunaView_tunaContentPaddingRight, 0);
//
//            //
//            int tunaContentGravityIndex = typedArray.getInt(R.styleable.TunaView_tunaContentGravity, 0);
//            if (tunaContentGravityIndex >= 0) {
//                tunaContentGravity = tunaContentGravityArray[tunaContentGravityIndex];
//            }
//
//            tunaContentDx = typedArray.getDimension(R.styleable.TunaView_tunaContentDx, 0);
//            tunaContentDy = typedArray.getDimension(R.styleable.TunaView_tunaContentDy, 0);
//            tunaContentFractionDx = typedArray.getFraction(R.styleable.TunaView_tunaContentFractionDx, 1, 1, 0);
//            tunaContentFractionDy = typedArray.getFraction(R.styleable.TunaView_tunaContentFractionDy, 1, 1, 0);
//
//            //
//            tunaContentShadowRadius = typedArray.getDimension(R.styleable.TunaView_tunaContentShadowRadius, 0);
//            if (tunaContentShadowRadius > 0) {
//                tunaContentShadowColor = typedArray.getColor(R.styleable.TunaView_tunaContentShadowColor, Color.TRANSPARENT);
//                tunaContentShadowDx = typedArray.getDimension(R.styleable.TunaView_tunaContentShadowDx, 0);
//                tunaContentShadowDy = typedArray.getDimension(R.styleable.TunaView_tunaContentShadowDy, 0);
//            }
//
//            //
//            tunaTextMarkDx = typedArray.getDimension(R.styleable.TunaView_tunaTextMarkDx, 0);
//            tunaTextMarkDy = typedArray.getDimension(R.styleable.TunaView_tunaTextMarkDy, 0);
//            tunaTextMarkFractionDx = typedArray.getFraction(R.styleable.TunaView_tunaTextMarkFractionDx, 1, 1, 0);
//            tunaTextMarkFractionDy = typedArray.getFraction(R.styleable.TunaView_tunaTextMarkFractionDy, 1, 1, 0);
//
//            tunaMaterial = typedArray.getBoolean(R.styleable.TunaView_tunaMaterial, false);
//
//            if (tunaMaterial) {
//                tunaMaterialSpread = typedArray.getBoolean(R.styleable.TunaView_tunaMaterialSpread, false);
//                tunaMaterialDuraction = typedArray.getInt(R.styleable.TunaView_tunaMaterialDuraction, tunaMaterialDuraction);
//
//                //
//                int tunaMaterialInterpolatorIndex = typedArray.getInt(R.styleable.TunaView_tunaMaterialTimeInterpolator, -1);
//                if (tunaMaterialInterpolatorIndex > -1) {
//                    tunaMaterialTimeInterpolator = tunaMaterialTimeInterpolatorArray[tunaMaterialInterpolatorIndex];
//                }
//            }
//        }

//        typedArray.recycle();
        initDisplayMetrics();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//
//        if (tunaTouchType == TunaTouchType.NONE) {
//            return super.dispatchTouchEvent(event);
//        }
//
//        tunaTouchEventX = event.getX();
//        tunaTouchEventY = event.getY();
//
//        if (tunaTouchListener != null && (tunaTouchType == TunaTouchType.ALWAYS || !tunaTouchOutBounds)) {
//            tunaTouchListener.tunaTouch(this);
//        }
//
//        switch (event.getAction()) {
//        case MotionEvent.ACTION_DOWN:
//
//            //
//            if (tunaDebugable) {
//                if (touchDownCount == 0) {
//                    touchDownCount = 1;
//                    touchDownTimeStart = System.currentTimeMillis();
//                }
//                else if (touchDownCount == TOUCH_DOWN_TIMES - 1) {
//                    touchDownTimeEnd = System.currentTimeMillis();
//                    if ((touchDownTimeEnd - touchDownTimeStart) < SHOW_PROPERTY_MAX_TIME_MILLIS) {
////                        showTunaProperties();
//                    }
//                    touchDownCount = 0;
//                }
//                else {
//                    float touchDownTimeDistanceX = Math.abs(tunaTouchDownEventX - event.getX());
//                    float touchDownTimeDistanceY = Math.abs(tunaTouchDownEventY - event.getY());
//                    if (touchDownTimeDistanceX < SHOW_PROPERTY_MAX_DISTANCE_DP * displayDensity && touchDownTimeDistanceY < SHOW_PROPERTY_MAX_DISTANCE_DP * displayDensity) {
//                        touchDownCount++;
//                    }
//                }
//                tunaTouchDownEventX = event.getX();
//                tunaTouchDownEventY = event.getY();
//                if ((System.currentTimeMillis() - touchDownTimeStart) >= SHOW_PROPERTY_MAX_TIME_MILLIS) {
//                    touchDownCount = 0;
//                }
//            }
//
//            tunaPress = true;
//            tunaSelect = false;
//
//            if (!tunaTextMarkTouchable) {
//                tunaTextMark = false;
//            }
//
//            if (!tunaContentMarkTouchable) {
//                tunaContentMark = false;
//            }
//
//            if (!tunaTouchOutBounds && tunaAssociateListener != null) {
//                tunaAssociateListener.tunaAssociate(this);
//            }
//
//            if (!tunaTouchOutBounds && tunaTouchDownListener != null) {
//                tunaTouchDownListener.tunaTouchDown(this);
//            }
//
//            if (tunaMaterial) {
//                float startRadius, endRadius;
//                if (tunaWidth >= tunaHeight) {
//                    startRadius = (tunaTouchDownEventY >= tunaHeight - tunaTouchDownEventY ? tunaTouchDownEventY : tunaHeight - tunaTouchDownEventY);
//                    endRadius = tunaWidth << 1;
//                }
//                else {
//                    startRadius = (tunaTouchDownEventX >= tunaWidth - tunaTouchDownEventX ? tunaTouchDownEventX : tunaWidth - tunaTouchDownEventX);
//                    endRadius = tunaHeight << 1;
//                }
//
//                tunaMaterialAnimatorSet = new AnimatorSet();
//                if (tunaMaterialSpread) {
//                    tunaMaterialAnimatorSet.playTogether(ObjectAnimator.ofFloat(this, tunaMaterialRadiusProperty, startRadius, endRadius),
//
//                            ObjectAnimator.ofFloat(this, tunaMaterialPaintXProperty, tunaTouchDownEventX, tunaWidth >> 1),
//                            ObjectAnimator.ofFloat(this, tunaMaterialPaintYProperty, tunaTouchDownEventY, tunaHeight >> 1));
//
//                }
//                else {
//                    tunaMaterialAnimatorSet.playTogether(ObjectAnimator.ofFloat(this, tunaMaterialRadiusProperty, startRadius, endRadius));
//                }
//
//                tunaMaterialAnimatorSet.setDuration(tunaMaterialDuraction);
//
//                if (tunaMaterialTimeInterpolator != null) {
//                    tunaMaterialAnimatorSet.setInterpolator(tunaMaterialTimeInterpolator);
//                }
//
//                tunaMaterialAnimatorSet.addListener(new AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        tunaMaterialPlay = true;
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        tunaMaterialPlay = false;
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//                        tunaMaterialPlay = false;
//                    }
//                });
//                tunaMaterialAnimatorSet.start();
//            }
//
//            break;
//        case MotionEvent.ACTION_MOVE:
//            //
//            if (tunaTouchType == TunaTouchType.ALWAYS) {
//
//                tunaPress = true;
//                tunaSelect = false;
//
//            }
//            else if (!tunaTouchOutBounds && (tunaTouchEventX < 0 || tunaTouchEventX > tunaWidth || tunaTouchEventY < 0 || tunaTouchEventY > tunaHeight)) {
//
//                tunaPress = false;
//                tunaSelect = false;
//
//                if (!tunaTextMarkTouchable) {
//                    tunaTextMark = false;
//                }
//                if (!tunaContentMarkTouchable) {
//                    tunaContentMark = false;
//                }
//
//                invalidate();
//
//                tunaTouchOutBounds = true;
//                if (tunaTouchOutListener != null) {
//                    tunaTouchOutListener.tunaTouchOut(this);
//                }
//            }
//            else if (tunaTouchOutBounds && (tunaTouchEventX >= 0 && tunaTouchEventX <= tunaWidth && tunaTouchEventY >= 0 && tunaTouchEventY <= tunaHeight)) {
//
//                tunaPress = true;
//                tunaSelect = false;
//
//                if (!tunaTextMarkTouchable) {
//                    tunaTextMark = false;
//                }
//
//                if (!tunaContentMarkTouchable) {
//                    tunaContentMark = false;
//                }
//
//                tunaTouchOutBounds = false;
//                if (tunaTouchInListener != null) {
//                    tunaTouchInListener.tunaTouchIn(this);
//                }
//            }
//
//            //
//            if (!tunaTouchOutBounds && tunaTouchMoveListener != null) {
//                tunaTouchMoveListener.tunaTouchMove(this);
//            }
//
//            break;
//        case MotionEvent.ACTION_UP:
//            //
//            tunaPress = false;
//            switch (tunaSelectType) {
//            case NONE:
//                tunaSelect = false;
//                break;
//            case SAME:
//                tunaSelect = true;
//                break;
//            case REVERSE:
//                tunaSelectRaw = !tunaSelectRaw;
//                tunaSelect = tunaSelectRaw;
//                break;
//            default:
//                break;
//            }
//
//            if (!tunaTextMarkTouchable) {
//                tunaTextMark = false;
//            }
//            if (!tunaContentMarkTouchable) {
//                tunaContentMark = false;
//            }
//
//            if (!tunaTouchOutBounds && tunaAssociateListener != null) {
//                tunaAssociateListener.tunaAssociate(this);
//            }
//
//            if (!tunaTouchOutBounds && tunaTouchUpListener != null) {
//                tunaTouchUpListener.tunaTouchUp(this);
//            }
//
//            tunaTouchOutBounds = false;
//
//            break;
//        case MotionEvent.ACTION_CANCEL:
//            tunaPress = false;
//            tunaSelect = false;
//
//            if (!tunaTextMarkTouchable) {
//                tunaTextMark = false;
//            }
//            if (!tunaContentMarkTouchable) {
//                tunaContentMark = false;
//            }
//
//            if (!tunaTouchOutBounds && tunaTouchCancelListener != null) {
//                tunaTouchCancelListener.tunaTouchCancel(this);
//            }
//            break;
//        default:
//            break;
//        }
//
//        if (!tunaTouchOutBounds) {
//            invalidate();
//        }
//        return true;
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveTunaSize((int) getMinimumTunaWidth(), widthMeasureSpec), resolveTunaSize((int) getMinimumTunaHeight(), heightMeasureSpec));
    }

    private int resolveTunaSize(int suggestSize, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        // Default size if no limits are specified.
        int result = 0;
        // wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
            // System.out.println("AT_MOST");
            result = Math.min(suggestSize, specSize);
            // match_parent
        }
        else if (specMode == MeasureSpec.EXACTLY) {
            // System.out.println("EXACTLY");
            // If your control can fit within these bounds return that value.
            result = specSize;
        }
        else if (specMode == MeasureSpec.UNSPECIFIED) {
            // System.out.println("UNSPECIFIED");
            result = specSize;
        }
        return result;
    }

    private float getMinimumTunaWidth() {
        if (tunaTextValue != null) {
            initTunaPaint(tunaTextSize);
            return tunaPaint.measureText(tunaTextValue, 0, tunaTextValue.length());
        }
        else {
            return applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getViewDisplayMetrics(this));
        }
    }

    private float getMinimumTunaHeight() {
        if (tunaTextValue != null) {
            // first use getMinimumTunaWidth!
            FontMetricsInt fontMetrics = tunaPaint.getFontMetricsInt();
            return fontMetrics.descent - fontMetrics.ascent;
        }
        else {
            return applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getViewDisplayMetrics(this));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        tunaWidth = getWidth();
        tunaHeight = getHeight();

        if (tunaForegroundNormal != Color.TRANSPARENT || tunaForegroundPress != Color.TRANSPARENT || tunaForegroundSelect != Color.TRANSPARENT || tunaSrcNormal != null
                || tunaSrcPress != null || tunaSrcSelect != null || tunaSrcNormalShadowRadius > 0 || tunaSrcPressShadowRadius > 0 || tunaSrcSelectShadowRadius > 0
                || tunaBackgroundNormalShadowRadius > 0 || tunaBackgroundPressShadowRadius > 0 || tunaBackgroundSelectShadowRadius > 0 || tunaBackgroundNormalAngle != Integer.MAX_VALUE
                || tunaBackgroundPressAngle != Integer.MAX_VALUE || tunaBackgroundSelectAngle != Integer.MAX_VALUE || tunaSrcAnchorNormal != null || tunaSrcAnchorPress != null
                || tunaSrcAnchorSelect != null

                ) {
            // setShadowLayer() is only supported on text when hardware
            // acceleration is on.
            // Hardware acceleration is on by default when targetSdk=14 or
            // higher.
            // An easy workaround is to put your View in a software layer:
            // myView.setLayerType(View.LAYER_TYPE_SOFTWARE, null).
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        if ((tunaSrcNormal != null || tunaSrcPress != null || tunaSrcSelect != null || tunaBackgroundNormalShadowRadius > 0 || tunaBackgroundPressShadowRadius > 0 || tunaBackgroundSelectShadowRadius > 0)
                && tunaBackgroundNormal == Color.TRANSPARENT) {
            tunaBackgroundNormal = Color.WHITE;
        }

        // tunaText
        tunaTextDx += tunaWidth * tunaTextFractionDx;
        tunaTextDy += tunaHeight * tunaTextFractionDy;

        // tunaTextMark
        tunaTextMarkDx += tunaWidth * tunaTextMarkFractionDx;
        tunaTextMarkDy += tunaHeight * tunaTextMarkFractionDy;

        //
        if (tunaBackgroundNormalAngle != Integer.MAX_VALUE) {
            tunaBackgroundNormalShader = getLinearGradient(tunaWidth, tunaHeight, tunaBackgroundNormalAngle, tunaBackgroundNormalGradientStart, tunaBackgroundNormalGradientEnd);
        }
        if (tunaBackgroundPressAngle != Integer.MAX_VALUE) {
            tunaBackgroundPressShader = getLinearGradient(tunaWidth, tunaHeight, tunaBackgroundPressAngle, tunaBackgroundPressGradientStart, tunaBackgroundPressGradientEnd);
        }
        if (tunaBackgroundSelectAngle != Integer.MAX_VALUE) {
            tunaBackgroundSelectShader = getLinearGradient(tunaWidth, tunaHeight, tunaBackgroundSelectAngle, tunaBackgroundSelectGradientStart, tunaBackgroundSelectGradientEnd);
        }

        //
        int tunaSrcNormalWidthRaw = 0, tunaSrcNormalHeightRaw = 0, tunaSrcPressWidthRaw = 0, tunaSrcPressHeightRaw = 0, tunaSrcSelectWidthRaw = 0, tunaSrcSelectHeightRaw = 0;
        if (tunaSrcNormal != null) {
            tunaSrcNormalWidthRaw = tunaSrcNormal.getWidth();
            tunaSrcNormalHeightRaw = tunaSrcNormal.getHeight();

            initTunaMatrix((tunaWidth - tunaSrcNormalShadowRadius * 2f - tunaBackgroundNormalShadowRadius * 2f - tunaBackgroundNormalShadowDx * 2f) / tunaSrcNormalWidthRaw,
                    (tunaHeight - tunaSrcNormalShadowRadius * 2f - tunaBackgroundNormalShadowRadius * 2f - tunaBackgroundNormalShadowDy * 2f) / tunaSrcNormalHeightRaw);
        }

        if (tunaSrcPress != null) {
            tunaSrcPressWidthRaw = tunaSrcPress.getWidth();
            tunaSrcPressHeightRaw = tunaSrcPress.getHeight();
        }

        if (tunaSrcSelect != null) {
            tunaSrcSelectWidthRaw = tunaSrcSelect.getWidth();
            tunaSrcSelectHeightRaw = tunaSrcSelect.getHeight();
        }

        if (tunaSrcNormalWidthRaw != tunaSrcPressWidthRaw || tunaSrcNormalHeightRaw != tunaSrcPressHeightRaw || tunaSrcPressWidthRaw != tunaSrcSelectWidthRaw
                || tunaSrcPressHeightRaw != tunaSrcSelectHeightRaw) {
            throw new IllegalArgumentException("Both the width and height of the attribute tunaSrcNormal ,tunaSrcPress and tunaSrcSelect needed equal");
        }

        //
        int tunaSrcAnchorNormalWidthRaw = 0, tunaSrcAnchorNormalHeightRaw = 0, tunaSrcAnchorPressWidthRaw = 0, tunaSrcAnchorPressHeightRaw = 0, tunaSrcAnchorSelectWidthRaw = 0, tunaSrcAnchorSelectHeightRaw = 0;

        if (tunaSrcAnchorNormal != null) {
            tunaSrcAnchorNormalWidthRaw = tunaSrcAnchorNormal.getWidth();
            tunaSrcAnchorNormalHeightRaw = tunaSrcAnchorNormal.getHeight();

            initTunaAnchorMatrix(tunaSrcAnchorNormalWidth / tunaSrcAnchorNormalWidthRaw, tunaSrcAnchorNormalHeight / tunaSrcAnchorNormalHeightRaw);
        }

        if (tunaSrcAnchorPress != null) {
            tunaSrcAnchorPressWidthRaw = tunaSrcAnchorPress.getWidth();
            tunaSrcAnchorPressHeightRaw = tunaSrcAnchorPress.getHeight();
        }

        if (tunaSrcAnchorSelect != null) {
            tunaSrcAnchorSelectWidthRaw = tunaSrcAnchorSelect.getWidth();
            tunaSrcAnchorSelectHeightRaw = tunaSrcAnchorSelect.getHeight();
        }

        if (tunaSrcAnchorNormalWidthRaw != tunaSrcAnchorPressWidthRaw || tunaSrcAnchorNormalHeightRaw != tunaSrcAnchorPressHeightRaw
                || tunaSrcAnchorPressWidthRaw != tunaSrcAnchorSelectWidthRaw || tunaSrcAnchorPressHeightRaw != tunaSrcAnchorSelectHeightRaw) {
            throw new IllegalArgumentException("Both the width and height of the attribute tunaSrcAnchorNormal ,tunaSrcAnchorPress and tunaSrcAnchorSelect needed equal");
        }

        //
        if (tunaSrcLeft != null) {
            int tunaSrcLeftWidthRaw = tunaSrcLeft.getWidth();
            int tunaSrcLeftHeightRaw = tunaSrcLeft.getHeight();
            initTunaLeftMatrix(tunaSrcLeftWidth / tunaSrcLeftWidthRaw, tunaSrcLeftHeight / tunaSrcLeftHeightRaw);
        }

        if (tunaSrcRight != null) {
            int tunaSrcRightWidthRaw = tunaSrcRight.getWidth();
            int tunaSrcRightHeightRaw = tunaSrcRight.getHeight();
            initTunaRightMatrix(tunaSrcRightWidth / tunaSrcRightWidthRaw, tunaSrcRightHeight / tunaSrcRightHeightRaw);
        }

        if (tunaLayoutListener != null) {
            tunaLayoutListener.tunaLayout(this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        initTunaCanvas(canvas);

        //
        if (tunaRotate != 0) {
            canvas.rotate(tunaRotate, tunaWidth >> 1, tunaHeight >> 1);
        }
        if (!tunaSuper) {
            return;
        }

        //
        boolean needSaveLayer = (tunaSrcNormal != null || tunaSrcPress != null || tunaSrcSelect != null);
        if (needSaveLayer) {
            // draw the src/dst example into our offscreen bitmap
            tunaLayer = canvas.saveLayer(0, 0, tunaWidth, tunaHeight, null, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                    | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        }

        // dst

        // Note that when tunaSrcSelectShadowRadius > 0, the parent background color must take incoming as tunaBackgroundNormal!

        if (tunaClassic) {

            if (tunaMaterialPlay) {
                drawTunaRectClassic(canvas, tunaBackgroundNormalShadowRadius + tunaBackgroundNormalShadowDx, tunaBackgroundNormalShadowRadius + tunaBackgroundNormalShadowDy,
                        tunaWidth - tunaBackgroundNormalShadowRadius - tunaBackgroundNormalShadowDx, tunaHeight - tunaBackgroundNormalShadowRadius
                                - tunaBackgroundNormalShadowDy, tunaBackgroundNormal, tunaBackgroundNormalShader, tunaBackgroundNormalShadowRadius,
                        tunaBackgroundNormalShadowColor, tunaBackgroundNormalShadowDx, tunaBackgroundNormalShadowDy, tunaStrokeWidthNormal, tunaStrokeColorNormal, tunaRadius);

                canvas.drawCircle(tunaTouchDownEventX, tunaTouchDownEventY, tunaMaterialRadius, initTunaPaint(Paint.Style.FILL, tunaBackgroundPress));

            }
            else {
                drawTunaRectClassic(canvas, tunaBackgroundNormalShadowRadius + tunaBackgroundNormalShadowDx, tunaBackgroundNormalShadowRadius + tunaBackgroundNormalShadowDy,
                        tunaWidth - tunaBackgroundNormalShadowRadius - tunaBackgroundNormalShadowDx, tunaHeight - tunaBackgroundNormalShadowRadius
                                - tunaBackgroundNormalShadowDy, tunaSelect ? tunaBackgroundSelect : tunaPress ? tunaBackgroundPress : tunaBackgroundNormal,
                        tunaSelect ? tunaBackgroundSelectShader : tunaPress ? tunaBackgroundPressShader : tunaBackgroundNormalShader,
                        tunaSelect ? tunaBackgroundSelectShadowRadius : tunaPress ? tunaBackgroundPressShadowRadius : tunaBackgroundNormalShadowRadius,
                        tunaSelect ? tunaBackgroundSelectShadowColor : tunaPress ? tunaBackgroundPressShadowColor : tunaBackgroundNormalShadowColor,
                        tunaSelect ? tunaBackgroundSelectShadowDx : tunaPress ? tunaBackgroundPressShadowDx : tunaBackgroundNormalShadowDx,
                        tunaSelect ? tunaBackgroundSelectShadowDy : tunaPress ? tunaBackgroundPressShadowDy : tunaBackgroundNormalShadowDy, tunaSelect ? tunaStrokeWidthSelect
                                : tunaPress ? tunaStrokeWidthPress : tunaStrokeWidthNormal, tunaSelect ? tunaStrokeColorSelect : tunaPress ? tunaStrokeColorPress
                                : tunaStrokeColorNormal, tunaRadius);
            }

        }
        else {

            // draw MaterialDesign Effect
            if (tunaMaterialPlay) {
                drawTunaRectCustom(canvas, tunaBackgroundNormalShadowRadius + tunaBackgroundNormalShadowDx, tunaBackgroundNormalShadowRadius + tunaBackgroundNormalShadowDy,
                        tunaWidth - tunaBackgroundNormalShadowRadius - tunaBackgroundNormalShadowDx, tunaHeight - tunaBackgroundNormalShadowRadius
                                - tunaBackgroundNormalShadowDy, tunaBackgroundNormal, tunaBackgroundNormalShader, tunaBackgroundNormalShadowRadius,
                        tunaBackgroundNormalShadowColor, tunaBackgroundNormalShadowDx, tunaBackgroundNormalShadowDy, tunaStrokeWidthNormal, tunaStrokeColorNormal,
                        tunaRadiusLeftTop, tunaRadiusLeftBottom, tunaRadiusRightTop, tunaRadiusRightBottom);

                canvas.drawCircle(tunaTouchDownEventX, tunaTouchDownEventY, tunaMaterialRadius, initTunaPaint(Paint.Style.FILL, tunaBackgroundPress));

            }
            else {
                drawTunaRectCustom(canvas, tunaBackgroundNormalShadowRadius + tunaBackgroundNormalShadowDx, tunaBackgroundNormalShadowRadius + tunaBackgroundNormalShadowDy,
                        tunaWidth - tunaBackgroundNormalShadowRadius - tunaBackgroundNormalShadowDx, tunaHeight - tunaBackgroundNormalShadowRadius
                                - tunaBackgroundNormalShadowDy, tunaSelect ? tunaBackgroundSelect : tunaPress ? tunaBackgroundPress : tunaBackgroundNormal,
                        tunaSelect ? tunaBackgroundSelectShader : tunaPress ? tunaBackgroundPressShader : tunaBackgroundNormalShader,
                        tunaSelect ? tunaBackgroundSelectShadowRadius : tunaPress ? tunaBackgroundPressShadowRadius : tunaBackgroundNormalShadowRadius,
                        tunaSelect ? tunaBackgroundSelectShadowColor : tunaPress ? tunaBackgroundPressShadowColor : tunaBackgroundNormalShadowColor,
                        tunaSelect ? tunaBackgroundSelectShadowDx : tunaPress ? tunaBackgroundPressShadowDx : tunaBackgroundNormalShadowDx,
                        tunaSelect ? tunaBackgroundSelectShadowDy : tunaPress ? tunaBackgroundPressShadowDy : tunaBackgroundNormalShadowDy, tunaSelect ? tunaStrokeWidthSelect
                                : tunaPress ? tunaStrokeWidthPress : tunaStrokeWidthNormal, tunaSelect ? tunaStrokeColorSelect : tunaPress ? tunaStrokeColorPress
                                : tunaStrokeColorNormal, tunaRadiusLeftTop, tunaRadiusLeftBottom, tunaRadiusRightTop, tunaRadiusRightBottom);
            }
        }


        // draw tunaBitmap
        if (needSaveLayer) {
            tunaPaint.setXfermode(tunaPorterDuffXfermode);

            // If they are offset tunaBackgroundShadow, mobile, is to draw on
            // the background shadow,
            // without moving the bigger picture and the need to set the width
            // and height

            canvas.translate(tunaSelect ? tunaBackgroundSelectShadowDx * 2f + tunaSrcSelectShadowRadius - tunaSrcSelectShadowDx : tunaPress ? tunaBackgroundPressShadowDx * 2f
                            + tunaSrcPressShadowRadius - tunaSrcPressShadowDx : tunaBackgroundNormalShadowDx * 2f + tunaSrcNormalShadowRadius - tunaSrcNormalShadowDx,
                    tunaSelect ? tunaBackgroundSelectShadowDy * 2f + tunaSrcSelectShadowRadius - tunaSrcSelectShadowDy : tunaPress ? tunaBackgroundPressShadowDy * 2f
                            + tunaSrcPressShadowRadius - tunaSrcPressShadowDy : tunaBackgroundNormalShadowDy * 2f + tunaSrcNormalShadowRadius - tunaSrcNormalShadowDy);
            canvas.drawBitmap(
                    tunaSelect ? tunaSrcSelect : tunaPress ? tunaSrcPress : tunaSrcNormal,
                    tunaMatrix,
                    initTunaPaint(tunaPaint, tunaSelect ? tunaSrcSelectShadowRadius : tunaPress ? tunaSrcPressShadowRadius : tunaSrcNormalShadowRadius,
                            tunaSelect ? tunaSrcSelectShadowDx : tunaPress ? tunaSrcPressShadowDx : tunaSrcNormalShadowDx, tunaSelect ? tunaSrcSelectShadowDy
                                    : tunaPress ? tunaSrcPressShadowDy : tunaSrcNormalShadowDy));
            canvas.translate(tunaSelect ? -tunaBackgroundSelectShadowDx * 2f - tunaSrcSelectShadowRadius + tunaSrcSelectShadowDx : tunaPress ? -tunaBackgroundPressShadowDx * 2f
                            - tunaSrcPressShadowRadius + tunaSrcPressShadowDx : -tunaBackgroundNormalShadowDx * 2f - tunaSrcNormalShadowRadius + tunaSrcNormalShadowDx,
                    tunaSelect ? -tunaBackgroundSelectShadowDy * 2f - tunaSrcSelectShadowRadius + tunaSrcSelectShadowDy : tunaPress ? -tunaBackgroundPressShadowDy * 2f
                            - tunaSrcPressShadowRadius + tunaSrcPressShadowDy : -tunaBackgroundNormalShadowDy * 2f - tunaSrcNormalShadowRadius + tunaSrcNormalShadowDy);

            tunaPaint.setXfermode(null);

            // Uncomment will cause a null pointer with tunaPaint in xml preview
            // canvas.restoreToCount(tunaLayer);
        }

        float anchorDx = 0, anchorDy = 0;

        switch (tunaSrcAnchorGravity & GRAVITY_MASK) {
        case LEFT:
            break;
        case CENTER_HORIZONTAL:
            anchorDx = (tunaWidth >> 1) - tunaSrcAnchorNormalWidth * 0.5f;
            break;
        case RIGHT:
            anchorDx = tunaWidth - tunaSrcAnchorNormalWidth;
            break;
        case CENTER_VERTICAL:
            anchorDy = (tunaHeight >> 1) - tunaSrcAnchorNormalHeight * 0.5f;
            break;
        case CENTER:
            anchorDx = (tunaWidth >> 1) - tunaSrcAnchorNormalWidth * 0.5f;
            anchorDy = (tunaHeight >> 1) - tunaSrcAnchorNormalHeight * 0.5f;
            break;
        case RIGHT | CENTER_VERTICAL:
            anchorDx = tunaWidth - tunaSrcAnchorNormalWidth;
            anchorDy = (tunaHeight >> 1) - tunaSrcAnchorNormalHeight * 0.5f;
            break;
        case BOTTOM:
            anchorDy = tunaHeight - tunaSrcAnchorNormalHeight;
            break;
        case BOTTOM | CENTER_HORIZONTAL:
            anchorDx = (tunaWidth >> 1) - tunaSrcAnchorNormalWidth * 0.5f;
            anchorDy = tunaHeight - tunaSrcAnchorNormalHeight;
            break;
        case BOTTOM | RIGHT:
            anchorDx = tunaWidth - tunaSrcAnchorNormalWidth;
            anchorDy = tunaHeight - tunaSrcAnchorNormalHeight;
            break;
        default:
            break;
        }

        // draw tunaAnchor
        if (tunaSrcAnchorNormal != null) {
            canvas.translate(anchorDx + (tunaSelect ? tunaSrcAnchorSelectDx : tunaPress ? tunaSrcAnchorPressDx : tunaSrcAnchorNormalDx), anchorDy
                    + (tunaSelect ? tunaSrcAnchorSelectDy : tunaPress ? tunaSrcAnchorPressDy : tunaSrcAnchorNormalDy));

            canvas.drawBitmap(tunaSelect ? tunaSrcAnchorSelect : tunaPress ? tunaSrcAnchorPress : tunaSrcAnchorNormal, tunaAnchorMatrix, tunaPaint);

            canvas.translate(-anchorDx + (tunaSelect ? -tunaSrcAnchorSelectDx : tunaPress ? -tunaSrcAnchorPressDx : -tunaSrcAnchorNormalDx), -anchorDy
                    + (tunaSelect ? -tunaSrcAnchorSelectDy : tunaPress ? -tunaSrcAnchorPressDy : -tunaSrcAnchorNormalDy));
        }

        // draw tunaText
        if (tunaTextValue != null) {

            float f[] = drawTunaText(
                    canvas,
                    tunaTextValue,
                    tunaWidth,
                    (tunaWidth >> 1) + tunaTextDx + tunaSrcLeftWidth * 0.5f + tunaSrcLeftPadding * 0.5f - tunaSrcRightWidth * 0.5f - tunaSrcRightPadding * 0.5f,
                    (tunaHeight >> 1) + tunaTextDy,
                    tunaTextPaddingLeft + tunaSrcLeftWidth,
                    tunaTextPaddingRight + tunaSrcRightWidth,
                    initTunaPaint(Paint.Style.FILL,
                            tunaMaterialPlay ? tunaTextColorPress : tunaSelect ? tunaTextColorSelect : tunaPress ? tunaTextColorPress : tunaTextColorNormal, tunaTextSize,
                            tunaTextShadowRadius, tunaTextShadowColor, tunaTextShadowDx, tunaTextShadowDy, Paint.Align.CENTER), tunaTextGravity);

            tunaTextDrawWidth = f[0];
            tunaTextEndOffsetCenterX = f[1];
            tunaTextEndOffsetCenterY = f[2];
        }

        // draw tunaContent
        if (tunaContentValue != null) {
            float f[] = drawTunaContent(
                    canvas,
                    tunaContentValue,
                    tunaWidth,
                    (tunaWidth >> 1) + tunaContentDx + tunaSrcLeftWidth * 0.5f + tunaSrcLeftPadding * 0.5f - tunaSrcRightWidth * 0.5f - tunaSrcRightPadding * 0.5f,
                    (tunaHeight >> 1) + tunaContentDy,
                    tunaContentPaddingLeft + tunaSrcLeftWidth,
                    tunaContentPaddingRight + tunaSrcRightWidth,
                    initTunaPaint(Paint.Style.FILL, tunaMaterialPlay ? tunaContentColorPress : tunaSelect ? tunaContentColorSelect : tunaPress ? tunaContentColorPress
                                    : tunaContentColorNormal, tunaContentSize, tunaContentShadowRadius, tunaContentShadowColor, tunaContentShadowDx, tunaContentShadowDy,
                            Paint.Align.CENTER), tunaContentGravity);

            tunaContentDrawWidth = f[0];
            tunaContentEndOffsetCenterX = f[1];
            tunaContentEndOffsetCenterY = f[2];
        }

        // draw tunaBitmapLeft,the draw position is half of the tunaWidth minus
        // the tunaSrcLeftPadding and tunaTextActualDrawWidth*0.5f
        if (tunaSrcLeft != null) {
            float dx = (tunaWidth >> 1) - tunaSrcLeftWidth * 0.5f - tunaTextDrawWidth * 0.5f - tunaSrcLeftPadding * 0.5f + tunaSrcLeftDx;
            float dy = (tunaHeight >> 1) - tunaSrcLeftHeight * 0.5f + tunaSrcLeftDy;

            canvas.translate(dx, dy);
            canvas.drawBitmap(tunaSrcLeft, tunaLeftMatrix, tunaPaint);
            canvas.translate(-dx, -dy);
        }

        if (tunaSrcRight != null) {

            float dx = (tunaWidth >> 1) - tunaSrcRightWidth * 0.5f + tunaTextDrawWidth * 0.5f + tunaSrcRightPadding * 0.5f + tunaSrcRightDx;
            float dy = (tunaHeight >> 1) - tunaSrcRightHeight * 0.5f + tunaSrcRightDy;

            canvas.translate(dx, dy);
            canvas.drawBitmap(tunaSrcRight, tunaRightMatrix, tunaPaint);
            canvas.translate(-dx, -dy);
        }

        // draw tunaTextMark
        if (tunaTextMark) {
            drawTunaTextMark(canvas, tunaTextMarkRadius, initTunaPaint(Paint.Style.FILL, tunaTextMarkColor), tunaTextDx + tunaTextEndOffsetCenterX + tunaTextMarkDx, tunaTextDy
                    + tunaTextEndOffsetCenterY + tunaTextMarkDy, tunaTextMarkTextValue);
        }

        // draw tunaContentMark
        if (tunaContentMark) {
            drawTunaTextMark(canvas, tunaContentMarkRadius, initTunaPaint(Paint.Style.FILL, tunaContentMarkColor), tunaContentDx + tunaContentEndOffsetCenterX + tunaContentMarkDx,
                    tunaContentDy + tunaContentEndOffsetCenterY + tunaContentMarkDy, tunaContentMarkTextValue);
        }

        // draw tunaForeground
        if (tunaSelect && tunaForegroundSelect != Color.TRANSPARENT) {
            if (tunaClassic) {
                drawTunaRectClassic(canvas, tunaWidth, tunaHeight, tunaForegroundSelect, tunaRadius);
            }
            else {
                drawTunaRectCustom(canvas, tunaWidth, tunaHeight, tunaForegroundSelect, 0, Color.TRANSPARENT, tunaRadiusLeftTop, tunaRadiusLeftBottom, tunaRadiusRightTop, tunaRadiusRightBottom);
            }
        }
        else if (tunaPress && tunaForegroundPress != Color.TRANSPARENT) {
            if (tunaClassic) {
                drawTunaRectClassic(canvas, tunaWidth, tunaHeight, tunaForegroundPress, tunaRadius);
            }
            else {
                drawTunaRectCustom(canvas, tunaWidth, tunaHeight, tunaForegroundPress, 0, Color.TRANSPARENT, tunaRadiusLeftTop, tunaRadiusLeftBottom, tunaRadiusRightTop, tunaRadiusRightBottom);
            }
        }
        else if (tunaForegroundNormal != Color.TRANSPARENT) {
            if (tunaClassic) {
                drawTunaRectClassic(canvas, tunaWidth, tunaHeight, tunaForegroundNormal, tunaRadius);
            }
            else {
                drawTunaRectCustom(canvas, tunaWidth, tunaHeight, tunaForegroundNormal, 0, Color.TRANSPARENT, tunaRadiusLeftTop, tunaRadiusLeftBottom, tunaRadiusRightTop, tunaRadiusRightBottom);
            }
        }
        if (tunaRotate != 0) {
            canvas.rotate(-tunaRotate, tunaWidth >> 1, tunaHeight >> 1);
        }
        if (tunaDrawListener != null) {
            tunaDrawListener.tunaDraw(this);
        }
    }

}