
package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.DateConvert;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.activity.CreditMerchantWise;
import com.wifyee.greenfields.activity.OrderItemDetails;
import com.wifyee.greenfields.dairyorder.DairyItemListActivity;
import com.wifyee.greenfields.foodorder.FoodOrderListActivity;
import com.wifyee.greenfields.interfaces.ItemClickListener;
import com.wifyee.greenfields.models.MyCreditModel;
import com.wifyee.greenfields.models.OrderModel;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCreditAdapter extends RecyclerView.Adapter<MyCreditAdapter.MyViewHolder> {

    private List<MyCreditModel> creditModels;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView merName,totalAmount,merTypeName;
        public CircleImageView imageView;
        public View horizView;
        private ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            merName =  view.findViewById(R.id.mer_company);
            totalAmount =  view.findViewById(R.id.total_amount);
            merTypeName =  view.findViewById(R.id.mer_type_name);
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
    public MyCreditAdapter(Context ctx, List<MyCreditModel> creditModels) {
        this.creditModels = creditModels;
        this.context=ctx;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_credit_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position)
    {
       final MyCreditModel credit = creditModels.get(position);
        String upperString = credit.getMerCompany().substring(0,1).toUpperCase() + credit.getMerCompany().substring(1).toLowerCase();
        holder.merName.setText(upperString);
        holder.totalAmount.setText("₹"+credit.getTotalAmount());
        holder.merTypeName.setText("("+credit.getMerTypeName()+")");

        holder.merName.setTypeface(Fonts.getSemiBold(context));
        holder.totalAmount.setTypeface(Fonts.getRegular(context));
        holder.merTypeName.setTypeface(Fonts.getRegular(context));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.oriz_icon)
                .error(R.drawable.oriz_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context).load(credit.getMerchantProfileImage())
                .apply(options)
                .into(holder.imageView);
        //

       /* if(credit.getMerTypeId().equals("6")){
            holder.imageView.setImageResource(R.drawable.ic_food_credit);
        }else if(credit.getMerTypeId().equals("7")){
            holder.imageView.setImageResource(R.drawable.ic_medicine_cred);
        }else if(credit.getMerTypeId().equals("10")){
            holder.imageView.setImageResource(R.drawable.ic_grocery_cred);
        }else if(credit.getMerTypeId().equals("14")){
            holder.imageView.setImageResource(R.drawable.ic_fruits_cred);
        }else if(credit.getMerTypeId().equals("15")){
            holder.imageView.setImageResource(R.drawable.ic_veg_cred);
        }else if(credit.getMerTypeId().equals("7")){
            holder.imageView.setImageResource(R.drawable.ic_meat_cred);
        }else {
            holder.imageView.setImageResource(R.drawable.ic_fav_icon);
        }*/

        int[] androidColors = context.getResources().getIntArray(R.array.credit_color);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.horizView.setBackgroundColor(randomAndroidColor);
       // holder.imageView.setColorFilter(randomAndroidColor);
        holder.imageView.setBorderColor(randomAndroidColor);


        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick){
                    if(credit.getMerTypeId().equals("6")){
                        Intent intent = new Intent(context, FoodOrderListActivity.class);
                        intent.putExtra("merchantid",credit.getMerId());
                        intent.putExtra("merchantName",credit.getMerCompany());
                        intent.putExtra("current_status","1");
                        intent.putExtra("flag","credit");
                        intent.putExtra("total_bal",credit.getTotalAmount());
                        intent.putExtra("mc_id",credit.getMerCreditId());
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context, DairyItemListActivity.class);
                        intent.putExtra("data",credit.getMerId());
                        intent.putExtra("merchantName",credit.getMerCompany());
                        intent.putExtra("current_status","1");
                        intent.putExtra("flag","credit");
                        intent.putExtra("total_bal",credit.getTotalAmount());
                        intent.putExtra("mc_id",credit.getMerCreditId());
                        context.startActivity(intent);
                    }
                    /*Intent intent = new Intent(context, CreditMerchantWise.class);
                    intent.putExtra("mer_id",credit.getMerId());
                    intent.putExtra("mer_name",credit.getMerCompany());
                    context.startActivity(intent);*/
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

