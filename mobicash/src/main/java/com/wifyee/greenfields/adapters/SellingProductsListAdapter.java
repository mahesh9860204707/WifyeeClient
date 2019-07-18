package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.MobicashApplication;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.CircularNetworkImageView;
import com.wifyee.greenfields.models.requests.SellingResponse;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Amit on 21/05/18.
 */
public class SellingProductsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean loading = true;
    private Context context;
    private List<SellingResponse> sellingList;
    private OnItemClickListener onItemClickListener;

    public SellingProductsListAdapter(Context mcontext)
    {
        this.context=mcontext;
        sellingList = new ArrayList<>();
    }

    private void add(SellingResponse item) {
        sellingList.add(item);
        notifyItemInserted(sellingList.size());
    }

    public void addAll(List<SellingResponse> productList) {
        for (SellingResponse products : productList) {
            add(products);
        }
    }

    public void remove(SellingResponse item) {
        int position = sellingList.indexOf(item);
        if (position > -1) {
            sellingList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public SellingResponse getItem(int position){
        return sellingList.get(position);
    }

    @Override
    public int getItemViewType (int position) {
        if(isPositionFooter (position)) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_ITEM;
    }

    private boolean isPositionFooter (int position) {
        return position == sellingList.size();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_member, parent, false);
            return new MemberViewHolder(view, onItemClickListener);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_loading, parent, false);
            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MemberViewHolder) {
            MemberViewHolder memberViewHolder = (MemberViewHolder) holder;
            final SellingResponse sellProducts = sellingList.get(position);
            if(sellProducts!= null) {
                if (sellProducts.getImageObj() != null) {
                    if (sellProducts.getImageObj().get(0).getImagePath()!= null) {
                        memberViewHolder.productImage.setImageUrl(sellProducts.getImageObj().get(0).getImagePath(), MobicashApplication.getInstance().getImageLoader());
                        // Picasso.with(context).load(sellProducts.getImageObj().get(0).getImagePath()).into(memberViewHolder.productImage);
                    }
                } else {
                    memberViewHolder.productImage.setImageResource(R.mipmap.ic_oriz);
                }
                memberViewHolder.itemName.setText(sellProducts.getItemName());
                memberViewHolder.itemPrice.setText(sellProducts.getItemPrice());
                memberViewHolder.discount.setText(sellProducts.getDiscount());
                memberViewHolder.discountType.setText(sellProducts.getDiscountType());
            }
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
               loadingViewHolder.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        }

    }

    public void setLoading(boolean loading){

        this.loading = loading;
    }

    @Override
    public int getItemCount() {
        return sellingList == null ? 0 : sellingList.size() + 1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircularNetworkImageView productImage;
        TextView itemName;
        TextView itemPrice;
        TextView discountType;
        TextView discount;


        OnItemClickListener onItemClickListener;

        public MemberViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            productImage = (CircularNetworkImageView) itemView.findViewById(R.id.product_image);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemPrice = (TextView) itemView.findViewById(R.id.item_price);
            discountType = (TextView) itemView.findViewById(R.id.discoutn_type);
            discount = (TextView) itemView.findViewById(R.id.discount);

            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.loading);
        }
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }
}
