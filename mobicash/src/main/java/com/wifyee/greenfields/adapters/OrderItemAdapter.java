
package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.models.OrderItemModel;

import java.util.List;


public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.MyViewHolder> {

    private List<OrderItemModel> orderItemModels;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderName,orderAmount,orderQuantity;

        public MyViewHolder(View view) {
            super(view);
            orderName = (TextView) view.findViewById(R.id.text_order_number);
            orderAmount = (TextView) view.findViewById(R.id.amount);
            orderQuantity = (TextView) view.findViewById(R.id.quantity);

            context = view.getContext();

        }
    }
    public OrderItemAdapter(Context ctx, List<OrderItemModel> orderItemModels) {
        this.orderItemModels = orderItemModels;
        this.context=ctx;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderitem_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position)
    {
       final OrderItemModel order = orderItemModels.get(position);
        holder.orderName.setText(order.getItemName());
        holder.orderAmount.setText("₹"+order.getItemPrice());
        holder.orderQuantity.setText(order.getItemQuantity());
    }
    @Override
    public int getItemCount() {
        return orderItemModels.size();
    }

    @Override
    public long getItemId (int position)
    {
        setHasStableIds(true);
        return position;
    }
}

