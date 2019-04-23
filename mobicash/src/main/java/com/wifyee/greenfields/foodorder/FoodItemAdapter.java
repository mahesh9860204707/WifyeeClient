package com.wifyee.greenfields.foodorder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wifyee.greenfields.R;

import java.util.List;

/**
 * Created by user on 12/21/2017.
 */

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder>{

    private List<ItemsDeatils> mFoodOderItemCollection;
    Context mContext;
    private RecyclerView mRecyclerView;
    private final LayoutInflater mLayoutInflater;


    public FoodItemAdapter(Context context, RecyclerView recyclerView, List<ItemsDeatils> foodItemCollection) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mFoodOderItemCollection = foodItemCollection;
        mRecyclerView = recyclerView;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_name,tv_quantity,tv_amount;
        public ViewHolder(View v){
            super(v);
            tv_name = (TextView) v.findViewById(R.id.tv_Itemname);
            tv_quantity = (TextView) v.findViewById(R.id.item_quantity);
            tv_amount = (TextView) v.findViewById(R.id.item_amount);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view1 = LayoutInflater.from(mContext).inflate(R.layout.itemdisplay_layout,parent,false);

        ViewHolder viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }


    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position){
        final ItemsDeatils FoodOderItem = mFoodOderItemCollection.get(position);

        //  holder.mDateValue.setText(logItem.date);
        Vholder.tv_amount.setText(FoodOderItem.amount);
        Vholder.tv_name.setText(FoodOderItem.Name );
        Vholder.tv_quantity.setText(FoodOderItem.quantity);


        // imageLoader.DisplayImage(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+FoodOderItem.foodImage, Vholder.imag_foodimage);
        //  Picasso.with(mContext).load(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+FoodOderItem.foodImage).into(Vholder.imag_foodimage);


    }


    @Override
    public int getItemCount() {
        return (mFoodOderItemCollection != null) ? mFoodOderItemCollection.size() : 0;
    }

}