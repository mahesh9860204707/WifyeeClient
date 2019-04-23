package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.interfaces.OnClickListener;
import com.wifyee.greenfields.models.response.PlanDetails;
import com.wifyee.greenfields.models.response.PromoCodeBean;

import java.util.List;

/**
 * Created by amit on 6/10/2017.
 */
public class PromoCodeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PromoCodeBean> mList;
    public OnClickListener ClickListener;
    int selectedPosition = -1;
    private String id="";
    private Context context;
    private Spinner spinnerPlan;

    public PromoCodeListAdapter(List<PromoCodeBean> list, Context ctx) {
        this.mList = list;
        this.context=ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promocode_inflate, parent, false);
                return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {
        PromoCodeBean object = mList.get(position);
        id= mList.get(position).getPlanId();
        if (object != null) {
            ((CityViewHolder) holder).tvPromoName.setText(object.getPromoName());
            ((CityViewHolder) holder).tvPromoCode.setText(object.getPromoCode());
            ((CityViewHolder) holder).tvPromoDesc.setText(object.getPromoDesc());
           /* ((CityViewHolder) holder).tvtype.setText(object.getType());
            ((CityViewHolder) holder).tvdiscountType.setText(object.getDiscountType());
            ((CityViewHolder) holder).tvdiscount.setText(object.getDiscount());
            ((CityViewHolder) holder).maxDiscount.setText(object.getMaxDiscount());
            ((CityViewHolder) holder).startDate.setText(object.getStartDate());*/
            ((CityViewHolder) holder).endDate.setText(object.getEndDate());
            //((CityViewHolder) holder).userType.setText(object.getUserType());
           // ((CityViewHolder) holder).createdDate.setText(object.getCreatedDate());

        }
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    public  class CityViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPromoName,tvPromoCode,tvPromoDesc,tvtype,tvdiscountType,tvdiscount,maxDiscount,startDate,endDate,userType,createdDate;

        public CityViewHolder(View itemView) {
            super(itemView);
            tvPromoName = (TextView) itemView.findViewById(R.id.txt_promo_name);
            tvPromoCode = (TextView) itemView.findViewById(R.id.txt_promo_code);
            tvPromoDesc = (TextView) itemView.findViewById(R.id.txt_promo_desc);
          /*  tvtype = (TextView) itemView.findViewById(R.id.txt_promo_type);
            tvdiscountType= (TextView) itemView.findViewById(R.id.txt_discount_type);
            tvdiscount= (TextView) itemView.findViewById(R.id.txt_discount);
            maxDiscount= (TextView) itemView.findViewById(R.id.txt_max_discount);
            startDate= (TextView) itemView.findViewById(R.id.txt_start_date);*/
            endDate= (TextView) itemView.findViewById(R.id.txt_end_date);
         //   userType = (TextView) itemView.findViewById(R.id.txt_user_type);
         //   createdDate= (TextView) itemView.findViewById(R.id.txt_created_date);

        }
    }
}
