package com.wifyee.greenfields.dairyorder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DairyMainListAdapter extends RecyclerView.Adapter  {

    List mValues;
    Context mContext;
    protected ItemListener mListener;

    public DairyMainListAdapter(Context context, List values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        DairyMerchantListModel item;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            textView =  v.findViewById(R.id.textView);
            imageView =   v.findViewById(R.id.imageView);
            relativeLayout =  v.findViewById(R.id.relativeLayout);

        }

        public void setData(DairyMerchantListModel item) {
            this.item = item;
            String upperString = item.getCompany().substring(0,1).toUpperCase() + item.getCompany().substring(1);
            textView.setText(upperString);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.oriz_icon)
                    .error(R.drawable.oriz_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(mContext)
                    .load(item.getImage())
                    .apply(options)
                    .into(imageView);

            textView.setTypeface(Fonts.getSemiBold(mContext));

            /*Picasso.with(mContext).load(item.getImage()).noFade().into(imageView);*/

            if(item.getCurrentStatus().equals("0")) {
                relativeLayout.setAlpha(0.3f);
            }else {
                relativeLayout.setAlpha(1);
            }
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public DairyMainListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.dairy_main_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, int position) {
        ViewHolder myViewHolder = (ViewHolder)vHolder;
        myViewHolder.setData((DairyMerchantListModel)mValues.get(position));
    }


    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(DairyMerchantListModel item);
    }
}
