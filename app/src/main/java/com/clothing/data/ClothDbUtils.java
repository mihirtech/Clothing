package com.clothing.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by mihir.shah on 11/8/2015.
 */
public class ClothDbUtils {

    public static boolean isClothPresent(Context context, Uri uri) {
        return context.getContentResolver().query(ClothingProvider.AddedImagesColumns.CONTENT_URI, null, ClothingProvider.AddedImagesColumns.URI + "=?", new String[]{uri.toString()}, null).getCount() != 0;
    }

    public static Uri addCloth(Context context, Uri uri, int type) {
        ContentValues values = new ContentValues();
        values.put(ClothingProvider.AddedImagesColumns.URI, uri.toString());
        values.put(ClothingProvider.AddedImagesColumns.CLOTH_TYPE, type);
        return context.getContentResolver().insert(ClothingProvider.AddedImagesColumns.CONTENT_URI, values);
    }

    public static ArrayList<Uri> getClothList(Context context, int type) {
        Cursor cursor = context.getContentResolver().query(ClothingProvider.AddedImagesColumns.CONTENT_URI, null, ClothingProvider.AddedImagesColumns.CLOTH_TYPE + "=?", new String[]{String.valueOf(type)}, null);
        ArrayList<Uri> uriList = new ArrayList<Uri>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int uriColumn = cursor.getColumnIndex(ClothingProvider.AddedImagesColumns.URI);
                do {
                    uriList.add(Uri.parse(cursor.getString(uriColumn)));
                } while (cursor.moveToNext());
            }
        }
        return uriList;
    }
}
