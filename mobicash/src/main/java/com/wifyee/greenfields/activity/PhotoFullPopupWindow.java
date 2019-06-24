package com.wifyee.greenfields.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PhotoFullPopupWindow extends PopupWindow {

    View view;
    Context mContext;
    ImageView photoView;
    TextView amount,orderId,orderOn,statusCashBack;
    ViewGroup parent;
    private static PhotoFullPopupWindow instance = null;



    public PhotoFullPopupWindow(Context ctx, View v, String status,String amt,String ordId,String ordOn) {
        super(((LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate( R.layout.popup_cashback_full, null), ViewGroup.LayoutParams.MATCH_PARENT,
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

        photoView =view.findViewById(R.id.imageview);
        amount = view.findViewById(R.id.amount);
        orderId = view.findViewById(R.id.order_id);
        orderOn = view.findViewById(R.id.order_on);
        statusCashBack = view.findViewById(R.id.status);
        //parent = (ViewGroup) photoView.getParent();

        if (status.equals("1")){
            photoView.setBackgroundResource(R.drawable.ic_cashback_pending);
            statusCashBack.setText("Status : Pending");
        }else if (status.equals("2")){
            photoView.setBackgroundResource(R.drawable.ic_cashback_credit);
            statusCashBack.setText("Status : Credited");
        }


        amount.setText("₹"+amt);
        orderId.setText("Order Id : "+ordId);
        orderOn.setText("Order on : "+ordOn);

        showAtLocation(v, Gravity.CENTER, 0, 0);

    }

    public void onPalette(Palette palette) {
        if (null != palette) {
            ViewGroup parent = (ViewGroup) photoView.getParent().getParent();
            parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
        }
    }

}