package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.activity.MerchantListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amit on 6/1/2018.
 */
public class MerchantCategoryAdapter extends  RecyclerView.Adapter<MerchantCategoryAdapter.LogItemViewHolder> {
    private int categoryImages[] ;
    private String categoryNames[];
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public MerchantCategoryAdapter(Context context, int brandImage[], String brandName[]) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.categoryImages = brandImage;
        this.categoryNames = brandName;
    }

    @Override
    public MerchantCategoryAdapter.LogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.merchant_category_inflate, parent, false);
        return new MerchantCategoryAdapter.LogItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MerchantCategoryAdapter.LogItemViewHolder holder, final int position)
    {
        holder.categoryName.setText(categoryNames[position]);
        holder.categoryImage.setImageResource(categoryImages[position]);
        holder.categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               mContext.startActivity(new Intent(mContext, MerchantListActivity.class).putExtra("category",categoryNames[position]));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryNames.length;
    }

    public class LogItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.category_name)
        public TextView categoryName;

        @BindView(R.id.category_image)
        public ImageView categoryImage;

        public LogItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}