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
import com.clothing.model.PairInfo;
import com.clothing.utils.ImageUtils;

import java.io.IOException;

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

    public static SuggestionFragment getInstance(PairInfo clothInfo) {
        SuggestionFragment fragment = new SuggestionFragment();
        Bundle bundle = new Bundle();
        if (clothInfo != null) {
            bundle.putParcelable(SHIRT, clothInfo.getShirtUri());
            bundle.putParcelable(PANT, clothInfo.getPantUri());
        }
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mShirtUri = getArguments().getParcelable(SHIRT);
        mPantUri = getArguments().getParcelable(PANT);
        updateImages();
    }

    void initView() {
        mShirtImage = (ImageView) mRootView.findViewById(R.id.shirtImage);
        mPantImage = (ImageView) mRootView.findViewById(R.id.pantImage);
    }

    void updateImages() {
        if (mShirtUri != null) {
            try {
                mShirtImage.setImageBitmap(ImageUtils.scaledBitmap(ImageUtils.getImage(getActivity(), mShirtUri), ImageUtils.SCALED_SIZE, ImageUtils.SCALED_SIZE));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (mPantUri != null) {
            try {
                mPantImage.setImageBitmap(ImageUtils.scaledBitmap(ImageUtils.getImage(getActivity(), mPantUri), ImageUtils.SCALED_SIZE, ImageUtils.SCALED_SIZE));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setImages(Uri shirtUri, Uri pantUri) {
        mShirtUri = shirtUri;
        mPantUri = pantUri;
        notifyDataChanged();
    }

    public void setImages(PairInfo info) {
        if (!info.isValid()) return;
        mShirtUri = info.getShirtUri();
        mPantUri = info.getPantUri();
        notifyDataChanged();
    }

    public PairInfo getPair() {
        return new PairInfo(mShirtUri, mPantUri);
    }

    void notifyDataChanged() {
        updateImages();
    }
}
