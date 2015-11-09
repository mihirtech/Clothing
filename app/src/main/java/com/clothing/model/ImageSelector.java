package com.clothing.model;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.clothing.data.ClothDbUtils;
import com.clothing.data.ClothingProvider;

import java.util.ArrayList;

/**
 * Created by mihir.shah on 11/9/2015.
 */
public class ImageSelector {

    static ImageSelector sImageSelector;

    Context mContext;

    ClothObserver mClothObserver;

    ArrayList<Uri> mShirtUri, mPantUri;

    public static ImageSelector getInstance(Context context) {
        if (sImageSelector == null) {
            sImageSelector = new ImageSelector(context);
            sImageSelector.init();
        }
        return sImageSelector;
    }

    ImageSelector(Context context) {
        mContext = context;
    }

    public void init() {
        mContext.getContentResolver().registerContentObserver(ClothingProvider.AddedImagesColumns.CONTENT_URI, true, mClothObserver);
        updateList();
    }

    public void release() {
        mContext.getContentResolver().unregisterContentObserver(mClothObserver);
    }

    void updateList() {
        mShirtUri = ClothDbUtils.getClothList(mContext, ClothingProvider.AddedImagesColumns.TYPE_SHIRT);
        mPantUri = ClothDbUtils.getClothList(mContext, ClothingProvider.AddedImagesColumns.TYPE_PANT);
    }

    class ClothObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public ClothObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            updateList();
        }
    }
}
