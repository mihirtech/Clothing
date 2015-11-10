package com.clothing.model;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.clothing.data.ClothDbUtils;
import com.clothing.data.ClothingProvider;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mihir.shah on 11/9/2015.
 */
public class ImageSelector {

    static ImageSelector sImageSelector;

    Context mContext;

    ClothObserver mClothObserver;

    ArrayList<Uri> mShirtUri, mPantUri;

    Random mRandom;

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
        mClothObserver = new ClothObserver(new Handler());
        mContext.getContentResolver().registerContentObserver(ClothingProvider.AddedImagesColumns.CONTENT_URI, true, mClothObserver);
        mRandom = new Random();
        updateList();
    }

    public void release() {
        mContext.getContentResolver().unregisterContentObserver(mClothObserver);
    }

    void updateList() {
        mShirtUri = ClothDbUtils.getClothList(mContext, ClothingProvider.AddedImagesColumns.TYPE_SHIRT);
        mPantUri = ClothDbUtils.getClothList(mContext, ClothingProvider.AddedImagesColumns.TYPE_PANT);
    }

    public PairInfo getRandomItem() {
        PairInfo info = null;
        Uri shirtUri = mShirtUri.size() == 0 ? null : mShirtUri.get(mRandom.nextInt(mShirtUri.size()));
        Uri pantUri = mPantUri.size() == 0 ? null : mPantUri.get(mRandom.nextInt(mPantUri.size()));
        return new PairInfo(shirtUri, pantUri);
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
