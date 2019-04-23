package com.wifyee.greenfields.foodorder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;

import java.util.List;

/**
 * Created by user on 12/20/2017.
 */

public class FoodOrderStautsAdapter extends RecyclerView.Adapter<FoodOrderStautsAdapter.ViewHolder>{

    private List<OrdersList> mFoodOderItemCollection;
    Context mContext;
    private RecyclerView mRecyclerView;
    private final LayoutInflater mLayoutInflater;


    public FoodOrderStautsAdapter(Context context, RecyclerView recyclerView, List<OrdersList> foodItemCollection) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mFoodOderItemCollection = foodItemCollection;
        mRecyclerView = recyclerView;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_orderID,tv_Order_STATUS,tv_Order_Amount,tv_Order_on,tv_Pay_mode,tv_updated;
        LinearLayout updated;
        public ViewHolder(View v){
            super(v);
            tv_Order_STATUS = (TextView) v.findViewById(R.id.Order_Status);
            tv_Order_Amount = (TextView) v.findViewById(R.id.Order_amount);
            tv_orderID = (TextView) v.findViewById(R.id.tv_order_id);
            tv_Order_on=(TextView) v.findViewById(R.id.Order_On);
            tv_Pay_mode=(TextView) v.findViewById(R.id.pay_mode);
            tv_updated=(TextView) v.findViewById(R.id.updated_on);
            updated=(LinearLayout)v.findViewById(R.id.updated_lin);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view1 = LayoutInflater.from(mContext).inflate(R.layout.foodorderstatus,parent,false);

        ViewHolder viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }


    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position){
        final OrdersList FoodOderItem = mFoodOderItemCollection.get(position);

        //  holder.mDateValue.setText(logItem.date);
        Vholder.tv_Order_Amount.setText(FoodOderItem.order_amount);
        Vholder.tv_Order_on.setText(FoodOderItem.order_on );
        Vholder.tv_Order_STATUS.setText(FoodOderItem.order_status);
        Vholder.tv_orderID.setText(FoodOderItem.order_id);
        Vholder.tv_Pay_mode.setText(FoodOderItem.payment_mode);
        if (!FoodOderItem.updated_on.equalsIgnoreCase("null")) {
            Vholder.updated.setVisibility(View.VISIBLE);
            Vholder.tv_updated.setText(FoodOderItem.updated_on);
        } else {
            Vholder.updated.setVisibility(View.GONE);
        }
        // imageLoader.DisplayImage(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+FoodOderItem.foodImage, Vholder.imag_foodimage);
       //Picasso.with(mContext).load(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+FoodOderItem.foodImage).into(Vholder.imag_foodimage);
    }
    @Override
    public int getItemCount() {
        return (mFoodOderItemCollection != null) ? mFoodOderItemCollection.size() : 0;
    }
}