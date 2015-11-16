package com.clothing.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.clothing.model.PairInfo;

import java.util.ArrayList;

/**
 * Created by mihir.shah on 11/8/2015.
 */
public class ClothDbUtils {

    public static boolean isClothPresent(Context context, Uri uri) {
        return context.getContentResolver().query(ClothingProvider.AddedImagesColumns
                .CONTENT_URI, null, ClothingProvider.AddedImagesColumns.URI + "=?", new
                String[]{uri.toString()}, null).getCount() != 0;
    }

    public static Uri addCloth(Context context, Uri uri, int type) {
        ContentValues values = new ContentValues();
        values.put(ClothingProvider.AddedImagesColumns.URI, uri.toString());
        values.put(ClothingProvider.AddedImagesColumns.CLOTH_TYPE, type);
        return context.getContentResolver().insert(ClothingProvider.AddedImagesColumns
                .CONTENT_URI, values);
    }

    public static ArrayList<Uri> getClothList(Context context, int type) {
        Cursor cursor = context.getContentResolver().query(ClothingProvider.AddedImagesColumns
                .CONTENT_URI, null, ClothingProvider.AddedImagesColumns.CLOTH_TYPE + "=?", new
                String[]{String.valueOf(type)}, null);
        ArrayList<Uri> uriList = new ArrayList<Uri>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int uriColumn = cursor.getColumnIndex(ClothingProvider.AddedImagesColumns.URI);
                do {
                    uriList.add(Uri.parse(cursor.getString(uriColumn)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return uriList;
    }

    public static boolean addToFavorites(Context context, PairInfo info) {
        if (!info.isValid()) return false;

        ContentValues values = new ContentValues();
        values.put(ClothingProvider.FavoriteImagesColumns.URI_SHIRT, info.getShirtUri().toString());
        values.put(ClothingProvider.FavoriteImagesColumns.URI_PANT, info.getPantUri().toString());
        return context.getContentResolver().insert(ClothingProvider.FavoriteImagesColumns
                .CONTENT_URI, values) != null;
    }

    public static boolean addToDisliked(Context context, PairInfo info) {
        if (!info.isValid()) return false;

        ContentValues values = new ContentValues();
        values.put(ClothingProvider.DislikedImagesColumns.URI_SHIRT, info.getShirtUri().toString());
        values.put(ClothingProvider.DislikedImagesColumns.URI_PANT, info.getPantUri().toString());
        return context.getContentResolver().insert(ClothingProvider.DislikedImagesColumns
                .CONTENT_URI, values) != null;
    }

    public static boolean isValidRandomPair(Context context, Uri shirtUri, Uri pantUri) {
        return !(isFavoritePair(context, shirtUri, pantUri) || isDislikedPair(context, shirtUri,
                pantUri));
    }

    public static boolean isFavoritePair(Context context, Uri shirtUri, Uri pantUri) {
        boolean isFavorite = false;
        Cursor cursor = context.getContentResolver().query(ClothingProvider.FavoriteImagesColumns
                        .CONTENT_URI,
                null, ClothingProvider.FavoriteImagesColumns.URI_SHIRT + "=? AND " +
                        ClothingProvider.FavoriteImagesColumns.URI_PANT + "=?", new
                        String[]{shirtUri.toString(), pantUri.toString()}, null);
        if (cursor != null) {
            isFavorite = cursor.getCount() > 0;
            cursor.close();
        }
        return isFavorite;
    }

    public static boolean isDislikedPair(Context context, Uri shirtUri, Uri pantUri) {
        boolean isDisliked = false;
        Cursor cursor = context.getContentResolver().query(ClothingProvider.DislikedImagesColumns
                        .CONTENT_URI,
                null, ClothingProvider.DislikedImagesColumns.URI_SHIRT + "=? AND " +
                        ClothingProvider.DislikedImagesColumns.URI_PANT + "=?", new
                        String[]{shirtUri.toString(), pantUri.toString()}, null);
        if (cursor != null) {
            isDisliked = cursor.getCount() > 0;
            cursor.close();
        }
        return isDisliked;
    }

}
