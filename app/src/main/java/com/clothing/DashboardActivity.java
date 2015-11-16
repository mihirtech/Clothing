package com.clothing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.clothing.data.ClothDbUtils;
import com.clothing.dialog.ClothChoiceFragment;
import com.clothing.fragment.SuggestionFragment;
import com.clothing.model.ImageSelector;
import com.clothing.utils.ImageUtils;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ClothChoiceFragment.OnClothSelectListener, View.OnClickListener {

    ImageSelector mSelector;

    SuggestionFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mSelector = ImageSelector.getInstance(this);
        initView();
    }

    void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, ImageUtils.PICK_IMAGE_ACTION);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mFragment = SuggestionFragment.getInstance(mSelector.getRandomItem());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mFragment).commit();

        ImageView likeImage = (ImageView) findViewById(R.id.like);
        ImageView disLikeImage = (ImageView) findViewById(R.id.dislike);

        likeImage.setOnClickListener(this);
        disLikeImage.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ImageUtils.PICK_IMAGE_ACTION) {
                Uri uri = data.getData();
                DialogFragment fragment = ClothChoiceFragment.getInstance(uri);
                fragment.show(getSupportFragmentManager(), ClothChoiceFragment.TAG);
            }
        }
    }

    @Override
    public void onClothSelected(int type, Uri uri) {
        if (ClothDbUtils.isClothPresent(this, uri)) {
            Toast.makeText(this, R.string.cloth_already_added, Toast.LENGTH_SHORT).show();
        }
        else if (ClothDbUtils.addCloth(this, uri, type) != null) {
            Toast.makeText(this, R.string.cloth_added_msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        mSelector.release();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.like:
                if (ClothDbUtils.addToFavorites(DashboardActivity.this, mFragment.getPair())) {
                    Toast.makeText(DashboardActivity.this, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DashboardActivity.this, R.string.error_adding_favorite, Toast.LENGTH_SHORT).show();
                }
                getNewPair();
                break;
            case R.id.dislike:
                if (ClothDbUtils.addToDisliked(DashboardActivity.this, mFragment.getPair())) {
                    Toast.makeText(DashboardActivity.this, R.string.added_to_disliked, Toast.LENGTH_SHORT).show();
                } else {

                }
                getNewPair();
                break;
        }
    }

    void getNewPair() {
        mFragment.setImages(mSelector.getRandomItem());
    }
}
