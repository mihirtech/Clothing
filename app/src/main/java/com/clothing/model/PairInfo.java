package com.clothing.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mihir.shah on 11/10/2015.
 */
public class PairInfo implements Parcelable {

    Uri mShirtUri, mPantUri;

    PairInfo(Uri shirtUri, Uri pantUri) {
        mShirtUri = shirtUri;
        mPantUri = pantUri;
    }

    public PairInfo(Parcel source) {
        mShirtUri = source.readParcelable(Uri.class.getClassLoader());
        mPantUri = source.readParcelable(Uri.class.getClassLoader());

    }

    public Uri getShirtUri() {
        return mShirtUri;
    }

    public Uri getPantUri() {
        return mPantUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mShirtUri, flags);
        dest.writeParcelable(mPantUri, flags);
    }

    public static final Creator<PairInfo> CREATOR = new Creator<PairInfo>() {

        @Override
        public PairInfo createFromParcel(Parcel source) {
            return new PairInfo(source);
        }

        @Override
        public PairInfo[] newArray(int size) {
            return new PairInfo[size];
        }
    };
}
