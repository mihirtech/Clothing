package com.clothing;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by mihir.shah on 11/8/2015.
 */
public class ClothingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
    }
}
