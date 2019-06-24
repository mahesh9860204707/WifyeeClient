package com.wifyee.greenfields.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.constants.Constants;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PhotoPopupWindow extends PopupWindow {

    View view;
    Context mContext;
    ImageView photoView;
    ProgressBar loading;
    ViewGroup parent;
    private static PhotoPopupWindow instance = null;

    public PhotoPopupWindow(Context ctx, View v, String imageUrl, Bitmap bitmap) {
        super(((LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_photo_full, null), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        if (Build.VERSION.SDK_INT >= 21) {
            setElevation(5.0f);
        }
        this.mContext = ctx;
        this.view = getContentView();
        ImageButton closeButton = (ImageButton) this.view.findViewById(R.id.ib_close);
        setOutsideTouchable(true);

        setFocusable(true);
        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                dismiss();
            }
        });
        //---------Begin customising this popup--------------------

        photoView =view.findViewById(R.id.image);
        loading = view.findViewById(R.id.loading);
        if (bitmap != null) {
            loading.setVisibility(View.GONE);
            /*if (Build.VERSION.SDK_INT >= 16) {
                parent.setBackground(
                        new BitmapDrawable(mContext.getResources(),
                                Constants.fastblur(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));// ));
            } else {
                onPalette(Palette.from(bitmap).generate());

            }*/
            photoView.setImageBitmap(bitmap);
        }
        showAtLocation(v, Gravity.CENTER, 0, 0);
    }
}