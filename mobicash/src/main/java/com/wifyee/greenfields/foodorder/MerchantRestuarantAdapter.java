package com.wifyee.greenfields.foodorder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.activity.UploadPrescription;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.dairyorder.DairyNetworkConstant;


import java.util.List;

/**
 * Created by user on 12/19/2017.
 */

public class MerchantRestuarantAdapter extends RecyclerView.Adapter<MerchantRestuarantAdapter.ViewHolder>{

    private List<Restaurant> mFoodOderItemCollection;
    Context mContext;
    private RecyclerView mRecyclerView;
    private final LayoutInflater mLayoutInflater;

    public MerchantRestuarantAdapter(Context context, RecyclerView recyclerView, List<Restaurant> foodItemCollection) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFoodOderItemCollection = foodItemCollection;
        mRecyclerView = recyclerView;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_fooddescrp;
        public ImageView imag_foodimage;
        public RelativeLayout ll;

        public ViewHolder(View v){
            super(v);
            tv_fooddescrp =  v.findViewById(R.id.tv_descprition);
            imag_foodimage = v.findViewById(R.id.imag_food);
            ll =  v.findViewById(R.id.ll);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.merchantbylocation,parent,false);
        return new ViewHolder(view1);
    }
    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position){
        final Restaurant FoodOderItem = mFoodOderItemCollection.get(position);

        String upperString = FoodOderItem.restaurant_name.substring(0,1).toUpperCase() + FoodOderItem.restaurant_name.substring(1);
        Vholder.tv_fooddescrp.setText(upperString);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.food_bg4)
                .error(R.drawable.food_bg4)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(mContext).load(FoodOderItem.logo)
                .apply(options)
                .into(Vholder.imag_foodimage);


        //Picasso.with(mContext).load(FoodOderItem.logo).into(Vholder.imag_foodimage);

        Vholder.tv_fooddescrp.setTypeface(Fonts.getSemiBold(mContext));

        if(FoodOderItem.status.equals("0")) {
            Vholder.ll.setAlpha(0.3f);
        }else {
            Vholder.ll.setAlpha(1f);
        }
    }
    @Override
    public int getItemCount() {
        return (mFoodOderItemCollection != null) ? mFoodOderItemCollection.size() : 0;
    }
}

