package com.clothing;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class SplashActivity extends AppCompatActivity {

    CallbackManager mCallbackManager;

    LoginButton mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (AccessToken.getCurrentAccessToken() != null) {
            launchDashboard();
        } else {
            initView();
        }
    }

    void initView() {
        mLoginButton = (LoginButton) findViewById(R.id.fb_login);
        mCallbackManager = CallbackManager.Factory.create();

        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                launchDashboard();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    void launchDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        float fbIconScale = 1.5F;
        Drawable drawable = getResources().getDrawable(com.facebook.R.drawable.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*fbIconScale),
                (int)(drawable.getIntrinsicHeight()*fbIconScale));
        mLoginButton.setCompoundDrawables(drawable, null, null, null);
        mLoginButton.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
        mLoginButton.setPadding(getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr), getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_top),
                0, getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_bottom));
    }
}
