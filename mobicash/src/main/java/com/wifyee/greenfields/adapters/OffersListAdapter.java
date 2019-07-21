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
import com.wifyee.greenfields.models.response.OffersItem;
import com.wifyee.greenfields.models.response.PlanDetails;

import java.util.List;

/**
 * Created by amit on 6/10/2017.
 */
public class OffersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OffersItem> mList;
    public OnClickListener ClickListener;
    int selectedPosition = -1;
    private String id="";
    private Context context;
    private Spinner spinnerPlan;

    public OffersListAdapter(List<OffersItem> list, Context ctx) {
        this.mList = list;
        this.context=ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_child_offers, parent, false);
                return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {
        OffersItem object = mList.get(position);
        id=mList.get(position).getOffer_id();
        if (object != null) {
            ((CityViewHolder) holder).tvOfferName.setText(object.getOffer_name());
            ((CityViewHolder) holder).tvOfferType.setText(object.getOffer_type());
            ((CityViewHolder) holder).tvOfferDiscount.setText(object.getOffer_discount());
            ((CityViewHolder) holder).tvOfferValidUpto.setText(object.getValid_upto());
            /*((CityViewHolder) holder).payCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (buttonView.isChecked())
                    {
                        if (ClickListener != null)
                        {

                        }
                    }
                    else {
                       Toast.makeText(context,"Unselected",Toast.LENGTH_SHORT).show();
                    }

                }
            });*/
          /*  ((CityViewHolder) holder).payRadio.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                        if (ClickListener != null)
                        {
                            ClickListener.seClickListener(String.valueOf(mList.get(position).getPlanId()), mList.get(position).getPlanCost(), mList.get(position).getPlanTrafficTotal(), mList.get(position).getPlanName());
                    }
                }
            });*/
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

        private TextView tvOfferName,tvOfferType,tvOfferDiscount,tvOfferValidUpto,tvMerchantId;
        private TextView id;
        //private CheckBox payCheckBox;

        public CityViewHolder(View itemView) {
            super(itemView);
            tvOfferName = (TextView) itemView.findViewById(R.id.offer_name);
            tvOfferType = (TextView) itemView.findViewById(R.id.offer_type);
            tvOfferDiscount = (TextView) itemView.findViewById(R.id.offer_discount);
            tvOfferValidUpto = (TextView) itemView.findViewById(R.id.valid_upto);
           // payCheckBox= (CheckBox) itemView.findViewById(R.id.payuMoney);
         //   tvHeadPlan= (TextView) itemView.findViewById(R.id.header_plan);


        }
    }
}
