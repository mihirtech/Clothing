package com.clothing.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.clothing.R;
import com.squareup.picasso.Picasso;

/**
 * Created by mihir.shah on 11/9/2015.
 */
public class SuggestionFragment extends Fragment {

    View mRootView;

    Uri mShirtUri, mPantUri;

    public static final String SHIRT = "shirt", PANT = "pant", TAG = SuggestionFragment.class.getSimpleName();

    ImageView mShirtImage, mPantImage;

    public static SuggestionFragment getInstance(Uri shirtUri, Uri pantUri) {
        SuggestionFragment fragment = new SuggestionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SHIRT, shirtUri);
        bundle.putParcelable(PANT, pantUri);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.suggestion_fragment, container, false);
        initView();
        return mRootView;
    }

    void initView() {
        mShirtImage = (ImageView) mRootView.findViewById(R.id.shirtImage);
        mPantImage = (ImageView) mRootView.findViewById(R.id.pantImage);
    }

    void updateImages() {
        if (mShirtUri != null) {
            /*try {
                 mShirtImage.setImageBitmap(ImageUtils.scaledBitmap(ImageUtils.getImage(getActivity(), mShirtUri), ImageUtils.SCALED_SIZE, ImageUtils.SCALED_SIZE));

            } catch (IOException ex) {
                ex.printStackTrace();
            }*/
            Picasso.with(getActivity()).load(mShirtUri).into(mShirtImage);
        }
    }

    public void setImages(Uri shirtUri, Uri pantUri) {
        mShirtUri = shirtUri;
        mPantUri = pantUri;
        notifyDataChanged();
    }

    void notifyDataChanged() {
        updateImages();
    }
}
