package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.wifyee.greenfields.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amit on 6/1/2018.
 */

public class FollowBrandsAdapter extends  RecyclerView.Adapter<FollowBrandsAdapter.LogItemViewHolder> {
    private int brandImages[] ;
    private String brandNames[];
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public  FollowBrandsAdapter(Context context,int brandImage[], String brandName[]) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.brandImages = brandImage;
        this.brandNames = brandName;
    }

    @Override
    public FollowBrandsAdapter.LogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.follow_brands_inflate, parent, false);
        return new FollowBrandsAdapter.LogItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FollowBrandsAdapter.LogItemViewHolder holder, final int position)
    {
        holder.brandName.setText(brandNames[position]);
        holder.brandImage.setImageResource(brandImages[position]);
    }

    @Override
    public int getItemCount() {
        return brandImages.length;
    }

    public class LogItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.brand_name)
        public TextView brandName;

        @BindView(R.id.brand_image)
        public ImageView brandImage;

        public LogItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}