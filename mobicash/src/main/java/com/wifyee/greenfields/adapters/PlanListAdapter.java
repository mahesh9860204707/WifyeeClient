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

import java.util.List;

/**
 * Created by amit on 6/10/2017.
 */
public class PlanListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PlanDetails> mList;
    public OnClickListener ClickListener;
    int selectedPosition = -1;
    private String id="";
    private Context context;
    private Spinner spinnerPlan;

    public PlanListAdapter(List<PlanDetails> list, OnClickListener setonClickListener,Context ctx) {
        this.mList = list;
        this.ClickListener=setonClickListener;
        this.context=ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_child, parent, false);
                return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {
        PlanDetails object = mList.get(position);
        id= mList.get(position).getPlanId();
        if (object != null) {
            ((CityViewHolder) holder).tvPlanName.setText(object.getPlanName());
            ((CityViewHolder) holder).tvPlanAmount.setText(object.getPlanCost());
            ((CityViewHolder) holder).tvPlanData.setText(object.getPlanTrafficTotal());
            ((CityViewHolder) holder).tvPlanValidity.setText(object.getPlanTimeBank());
            ((CityViewHolder) holder).payCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (buttonView.isChecked()) {
                        if (ClickListener != null) {
                            ClickListener.seClickListener(String.valueOf(mList.get(position).getPlanId()), mList.get(position).getPlanCost(), mList.get(position).getPlanTrafficTotal(), mList.get(position).getPlanName());
                        }
                    }
                    else {
                       Toast.makeText(context,"Unselected",Toast.LENGTH_SHORT).show();
                    }

                }
            });
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
        private TextView tvPlanName,tvPlanAmount,tvPlanData,tvPlanValidity,tvHeadPlan;
        private TextView id;
        private CheckBox payCheckBox;

        public CityViewHolder(View itemView) {
            super(itemView);
            tvPlanName = (TextView) itemView.findViewById(R.id.titleTextView);
            tvPlanAmount = (TextView) itemView.findViewById(R.id.plan_amount);
            tvPlanData = (TextView) itemView.findViewById(R.id.plan_data);
            tvPlanValidity = (TextView) itemView.findViewById(R.id.plan_validity);
            payCheckBox= (CheckBox) itemView.findViewById(R.id.payuMoney);
         //   tvHeadPlan= (TextView) itemView.findViewById(R.id.header_plan);


        }
    }
}
