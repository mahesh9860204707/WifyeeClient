package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.Snackbar;
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
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.dairyorder.DairyItemListActivity;
import com.wifyee.greenfields.foodorder.FoodOrderListActivity;
import com.wifyee.greenfields.interfaces.ItemClickListener;
import com.wifyee.greenfields.interfaces.OnClickListener;
import com.wifyee.greenfields.models.MyVoucherModel;
import com.wifyee.greenfields.models.OtherMerchantModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class OtherMerchantAdapter extends RecyclerView.Adapter<OtherMerchantAdapter.MyViewHolder> {

    private List<OtherMerchantModel> otherMerchantModels;
    public OnClickListener ClickListener;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {
        public TextView txtView;
        public ImageView icon;
        public RelativeLayout itemHolder;
        private ItemClickListener clickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtView = (TextView) itemView.findViewById(R.id.title_below);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            itemHolder = (RelativeLayout) itemView.findViewById(R.id.list_item_view_horizontal);

            context = itemView.getContext();
            itemView.setTag(itemView);
            //itemView.setOnClickListener(this);
        }

       /* public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), false);
        }*/
    }

    public OtherMerchantAdapter(Context ctx, List<OtherMerchantModel> otherMerchantModels) {
        this.otherMerchantModels = otherMerchantModels;
        this.context = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ex_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final OtherMerchantModel other = otherMerchantModels.get(position);

        holder.txtView.setText(other.getMerchantType());
        holder.txtView.setTypeface(Fonts.getRegular(context));


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.oriz_icon)
                .error(R.drawable.oriz_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context).load(other.getImage())
                .apply(options)
                .into(holder.icon);

        holder.itemHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = IntentFactory.createDairyProductActivity(context);
                productIntent.putExtra(NetworkConstant.EXTRA_DATA, other.getId());
                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, other.getMerchantType());
                context.startActivity(productIntent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return otherMerchantModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

}
