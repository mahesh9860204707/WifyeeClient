
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

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.activity.OrderItemDetails;
import com.wifyee.greenfields.interfaces.ItemClickListener;
import com.wifyee.greenfields.models.OrderModel;

import java.util.List;
import java.util.Random;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

   private int i=1;
    private List<OrderModel> orderModels;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView orderid,orderAmount,orderTimeStamp;
        public CardView cardView;
        public Button serial_number;
        private ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            orderid = (TextView) view.findViewById(R.id.text_order_number);
            orderAmount = (TextView) view.findViewById(R.id.amount);
            orderTimeStamp = (TextView) view.findViewById(R.id.on_date);
            cardView = (CardView) view.findViewById(R.id.card_view);
            serial_number= (Button) view.findViewById(R.id.text_order_serial_number);

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
    public OrderAdapter(Context ctx, List<OrderModel> orderModels) {
        this.orderModels = orderModels;
        this.context=ctx;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position)
    {
       final OrderModel order = orderModels.get(position);
        holder.orderid.setText(order.getOrderId());
        holder.orderAmount.setText("₹"+order.getOrderAmount());
        holder.orderTimeStamp.setText(order.getOrderDate());
        holder.serial_number.setText("#"+ String.valueOf(position+1));

        GradientDrawable drawable = (GradientDrawable)holder.serial_number.getBackground();
        drawable.setColor(getRandomColor());

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, OrderItemDetails.class);
                    intent.putExtra("order_id",order.getOrderId());
                    intent.putExtra("merchantId",order.getMerchantId());
                    intent.putExtra("taskId",order.getTaskId());
                    intent.putExtra("merchantType",order.getMerchantType());

                    /*Log.e("orderid",order.getOrderId());
                    Log.e("merchantId",order.getMerchantId());
                    Log.e("taskId",order.getTaskId());*/

                    context.startActivity(intent);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return orderModels.size();
    }

    @Override
    public long getItemId (int position)
    {
        setHasStableIds(true);
        return position;
    }

    private int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

}

