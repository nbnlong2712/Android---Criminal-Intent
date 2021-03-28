package com.LongTraiDep.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {

    public static Bitmap getScaledBitmap(String path, Activity activity)
    {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight)
    {
        //đọc, lấy kích thước của hình ảnh thật (trên đĩa)
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;   //chiều rộng của hình ảnh chụp
        float srcHeight = options.outHeight;   //chiều cao của hình ảnh chụp

        //tìm ra tỷ lệ bao nhiêu để giảm kích thước hình ảnh thật lại để vừa đủ hiển thị trong CrimeFragment
        int inSampleSize = 1;
        if(srcHeight > destHeight || srcWidth > destWidth)
        {
            float heightScale = srcHeight/destHeight;
            float widthScale = srcWidth/destWidth;

            inSampleSize = Math.round(heightScale>widthScale ? heightScale:widthScale);
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        //đọc và tạo final bitmap
        return BitmapFactory.decodeFile(path, options);
    }
}
