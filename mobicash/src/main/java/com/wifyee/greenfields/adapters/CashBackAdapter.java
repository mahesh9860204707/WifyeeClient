
package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.activity.OrderItemDetails;
import com.wifyee.greenfields.activity.PhotoFullPopupWindow;
import com.wifyee.greenfields.interfaces.ItemClickListener;
import com.wifyee.greenfields.models.CashbackModel;
import com.wifyee.greenfields.models.OrderModel;

import java.util.List;
import java.util.Random;

public class CashBackAdapter extends RecyclerView.Adapter<CashBackAdapter.MyViewHolder> {

    private List<CashbackModel> cashbackModels;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView orderid,orderAmount;
        public ImageView imageView;
        public CardView cardView;
        private ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            orderAmount = (TextView) view.findViewById(R.id.amount);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            cardView = (CardView) view.findViewById(R.id.card1);

            context = view.getContext();
            view.setTag(view);
            view.setOnClickListener(this);

        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }


        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), false);

        }

    }
    public CashBackAdapter(Context ctx, List<CashbackModel> cashbackModels) {
        this.cashbackModels = cashbackModels;
        this.context=ctx;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cashback_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position)
    {
       final CashbackModel cash = cashbackModels.get(position);
        holder.orderAmount.setText("₹"+cash.getCashbackAmt());

        if (cash.getCashbackStatus().equals("1")){
            holder.imageView.setBackgroundResource(R.drawable.ic_cashback_pending);
        }else if (cash.getCashbackStatus().equals("2")){
            holder.imageView.setBackgroundResource(R.drawable.ic_cashback_credit);
        }



       /* holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(context, view, cash.getCashbackStatus(), cash.getCashbackAmt(),cash.getOrderId(),cash.getOrderOn());
            }
        });*/

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick){
                    new PhotoFullPopupWindow(context, view, cash.getCashbackStatus(), cash.getCashbackAmt(),cash.getOrderId(),cash.getOrderOn());
                    //Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return cashbackModels.size();
    }

    @Override
    public long getItemId (int position)
    {
        setHasStableIds(true);
        return position;
    }

}

