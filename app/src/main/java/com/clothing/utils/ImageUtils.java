package com.clothing.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import com.clothing.widget.BitmapCache;

import java.io.IOException;

/**
 * Created by mihir.shah on 11/8/2015.
 */
public class ImageUtils {

    public static final int PICK_IMAGE_ACTION = 0x100, SCALED_SIZE = 320;

    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                null, null, null);

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            } else {
                return -1;
            }
        } finally {
            cursor.close();
        }
    }

    public static Bitmap getImage(Context context, Uri photoUri) throws IOException {
        // Handle rotation
        int rotation = getOrientation(context, photoUri);
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
        if (rotation != -1) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                    matrix, true);
        }
        return bitmap;
    }

    public static Bitmap scaledBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap.getWidth() < width && bitmap.getHeight() < height) {
            return bitmap;
        }

        float ratio = Math.min(
                (float) width / bitmap.getWidth(),
                (float) height / bitmap.getHeight());
        int newWidth = Math.round((float) ratio * bitmap.getWidth());
        int newHeight = Math.round((float) ratio * bitmap.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, newWidth,
                newHeight, true);
        return newBitmap;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Bitmap getBitmap(Context context, Uri uri, int width, int height) throws
            IOException {
        Bitmap bitmap = BitmapCache.getBitmap(uri, width, height);
        if (bitmap != null) {
            return bitmap;
        }

        bitmap = scaledBitmap(getImage(context, uri), SCALED_SIZE, SCALED_SIZE);
        BitmapCache.putBitmap(uri, bitmap);
        return bitmap;
    }
}
