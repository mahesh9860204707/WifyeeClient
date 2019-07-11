
package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.interfaces.ItemClickListener;
import com.wifyee.greenfields.models.CreditMerchantWiseModel;
import com.wifyee.greenfields.models.MyCreditModel;

import java.util.List;
import java.util.Random;

public class CreditMerchantWiseAdapter extends RecyclerView.Adapter<CreditMerchantWiseAdapter.MyViewHolder> {

    private List<CreditMerchantWiseModel> creditModels;
    private Context context;
    private Bitmap bitmap;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView totalAmount;
        public Button pay;
        public ImageView imageView;
        public View horizView;
        private ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            totalAmount =  view.findViewById(R.id.amount);
            pay =  view.findViewById(R.id.pay);
            imageView =  view.findViewById(R.id.imageView);
            horizView =  view.findViewById(R.id.horizview);

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
    public CreditMerchantWiseAdapter(Context ctx, List<CreditMerchantWiseModel> creditModels, Bitmap bitmap) {
        this.creditModels = creditModels;
        this.context = ctx;
        this.bitmap = bitmap;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.credit_merchant_wise_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position)
    {
       final CreditMerchantWiseModel credit = creditModels.get(position);
        holder.totalAmount.setText("₹"+credit.getReceivedCredit());
        holder.imageView.setImageBitmap(bitmap);

        holder.totalAmount.setTypeface(Fonts.getSemiBold(context));
        holder.pay.setTypeface(Fonts.getSemiBold(context));

        int[] androidColors = context.getResources().getIntArray(R.array.credit_color);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.horizView.setBackgroundColor(randomAndroidColor);
        holder.imageView.setColorFilter(randomAndroidColor);

        GradientDrawable drawable = (GradientDrawable)holder.pay.getBackground();
        drawable.setColor(randomAndroidColor);

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick){
                    Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return creditModels.size();
    }

    @Override
    public long getItemId (int position)
    {
        setHasStableIds(true);
        return position;
    }

}

