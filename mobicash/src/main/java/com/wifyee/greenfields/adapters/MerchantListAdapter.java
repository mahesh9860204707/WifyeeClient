
package com.wifyee.greenfields.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.models.response.MerchantListBean;

import java.util.List;


public class MerchantListAdapter extends RecyclerView.Adapter<MerchantListAdapter.MyViewHolder> {

    private int i=1;
    private List<MerchantListBean> merchantListBeanList;
    private Context context;
    private String imageUrl= NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mer_type,mer_name,mer_mobile_number,mer_company,mer_email,mer_address,mer_city;
        private ImageView mer_profile_image;
        public MyViewHolder(View view) {
            super(view);
            mer_type = (TextView) view.findViewById(R.id.text_merchant_type);
            mer_name = (TextView) view.findViewById(R.id.text_merchant_name);
            mer_mobile_number = (TextView) view.findViewById(R.id.text_mobile_number);
            mer_company=(TextView) view.findViewById(R.id.text_merchant_company);
            mer_email=(TextView) view.findViewById(R.id.text_merchant_email);
            mer_address=(TextView) view.findViewById(R.id.text_merchant_address);
            mer_city=(TextView) view.findViewById(R.id.text_merchant_city);
            mer_profile_image=(ImageView) view.findViewById(R.id.merchant_image);
        }
    }
    public MerchantListAdapter(Context ctx, List<MerchantListBean> medicineModelList) {
        this.merchantListBeanList = medicineModelList;
        this.context=ctx;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position)
    {
       final MerchantListBean merchantListBean = merchantListBeanList.get(position);
        holder.mer_type.setText(merchantListBean.getMer_type());
        holder.mer_mobile_number.setText(merchantListBean.getIdt_contactphone());
        holder.mer_name.setText(merchantListBean.getMer_name());
        holder.mer_company.setText(merchantListBean.getMer_company());
        holder.mer_email.setText(merchantListBean.getIdt_email());
        holder.mer_address.setText(merchantListBean.getIdt_address());
        holder.mer_city.setText(merchantListBean.getIdt_city());
        Picasso.with(context).load(imageUrl+merchantListBean.getMerchant_profile_image()).into(holder.mer_profile_image);
    }
    @Override
    public int getItemCount() {
        return merchantListBeanList.size();
    }

    @Override
    public long getItemId (int position)
    {
        setHasStableIds(true);
        return position;
    }
}

