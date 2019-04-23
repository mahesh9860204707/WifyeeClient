package com.wifyee.greenfields.foodorder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.constants.NetworkConstant;


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
        ImageView imag_foodimage;

        public ViewHolder(View v){
            super(v);
            tv_fooddescrp = (TextView) v.findViewById(R.id.tv_descprition);
            imag_foodimage=(ImageView)v.findViewById(R.id.imag_food);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.merchantbylocation,parent,false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }
    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position){
        final Restaurant FoodOderItem = mFoodOderItemCollection.get(position);
        //  holder.mDateValue.setText(logItem.date);
        Vholder.tv_fooddescrp.setText(FoodOderItem.restaurant_name );

        // imageLoader.DisplayImage(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+FoodOderItem.foodImage, Vholder.imag_foodimage);
        Picasso.with(mContext).load(FoodOderItem.logo).into(Vholder.imag_foodimage);
      //  Vholder.imag_foodimage.setBackgroundResource(R.drawable.no_images);
    }
    @Override
    public int getItemCount() {
        return (mFoodOderItemCollection != null) ? mFoodOderItemCollection.size() : 0;
    }
}

