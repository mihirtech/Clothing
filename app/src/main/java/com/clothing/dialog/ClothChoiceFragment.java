package com.clothing.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.clothing.R;
import com.clothing.data.ClothingProvider;

/**
 * Created by mihir.shah on 11/8/2015.
 */
public class ClothChoiceFragment extends DialogFragment {

    Uri mUri;

    public static final String URI = "uri", TAG = ClothChoiceFragment.class.getSimpleName();

    public static final int SHIRT = 0, PANT = 1;

    OnClothSelectListener mClothSelectListener;

    public static ClothChoiceFragment getInstance(Uri uri) {
        ClothChoiceFragment fragment = new ClothChoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(URI, uri);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mClothSelectListener = (OnClothSelectListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mUri = getArguments().getParcelable(URI);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.cloth_type);
        builder.setSingleChoiceItems(R.array.clothType, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mClothSelectListener != null) {
                    mClothSelectListener.onClothSelected(which == SHIRT ? ClothingProvider.AddedImagesColumns.TYPE_SHIRT : ClothingProvider.AddedImagesColumns.TYPE_PANT, mUri);
                }
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onDetach() {
        mClothSelectListener = null;
        super.onDetach();
    }

    public interface OnClothSelectListener {
        void onClothSelected(int type, Uri uri);
    }
}
