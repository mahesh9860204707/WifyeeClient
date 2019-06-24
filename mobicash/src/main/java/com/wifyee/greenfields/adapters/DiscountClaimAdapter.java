package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.activity.DiscountClaim;
import com.wifyee.greenfields.interfaces.OnClickListener;
import com.wifyee.greenfields.models.DiscountClaimModel;

import java.util.List;

/**
 * Created by amit on 6/10/2017.
 */
public class DiscountClaimAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DiscountClaimModel> discountClaimModels;
    public OnClickListener ClickListener;
    int selectedPosition = -1;
    private Context context;
    private double amount;

    public class CityViewHolder extends RecyclerView.ViewHolder {
        private TextView voucherName, voucherDetails, discountAmount, voucherAmount;
        private CheckBox payCheckBox;

        public CityViewHolder(View itemView) {
            super(itemView);
            voucherName =  itemView.findViewById(R.id.voucher_name);
            voucherDetails =  itemView.findViewById(R.id.voucher_details);
            discountAmount =  itemView.findViewById(R.id.discount_amount);
            voucherAmount =  itemView.findViewById(R.id.voucher_amount);
            payCheckBox =  itemView.findViewById(R.id.payuMoney);
        }
    }

    public DiscountClaimAdapter(List<DiscountClaimModel> discountClaimModels, OnClickListener setonClickListener, Context ctx,double amt) {
        this.discountClaimModels = discountClaimModels;
        this.ClickListener = setonClickListener;
        this.context = ctx;
        amount = amt;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_item, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        DiscountClaimModel object = discountClaimModels.get(position);
        String id = discountClaimModels.get(position).getId();
        ((CityViewHolder) holder).payCheckBox.setTag(position);


        // This line is important.
        /*if (position == selectedPosition) {
            ((CityViewHolder) holder).payCheckBox.setChecked(true);
        } else {
            ((CityViewHolder) holder).payCheckBox.setChecked(false);
        }*/

        ((CityViewHolder) holder).payCheckBox.setChecked(selectedPosition == position);

        /*if(selectedPosition == -1 && position ==0){
            selectedPosition = position;
            ((CityViewHolder) holder).payCheckBox.setChecked(true);
        }*/

        ((CityViewHolder) holder).payCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount>=Integer.parseInt(discountClaimModels.get(position).getDiscountAmount())) {
                    Log.e("id", discountClaimModels.get(position).getId());
                    ((CityViewHolder) holder).payCheckBox.setChecked(true);
                    ClickListener.seClickListener(discountClaimModels.get(position).getId(),
                            discountClaimModels.get(position).getVoucherNo(),
                            discountClaimModels.get(position).getVoucherName(),
                            discountClaimModels.get(position).getDiscountAmount());
                    selectedPosition = position;
                    notifyDataSetChanged();
                }else {
                    ((CityViewHolder) holder).payCheckBox.setChecked(false);
                    Toast.makeText(context,"To claim this voucher discount amount should be greater than or equal to "+
                            discountClaimModels.get(position).getDiscountAmount(),Toast.LENGTH_LONG).show();
                }
            }
        });

        //((CityViewHolder) holder).payCheckBox.setOnClickListener(onStateChangedListener(((CityViewHolder) holder).payCheckBox , position));

        if (object != null) {
            ((CityViewHolder) holder).voucherName.setText(object.getVoucherName());
            ((CityViewHolder) holder).voucherDetails.setText(object.getVoucherDetails());
            ((CityViewHolder) holder).discountAmount.setText("₹"+object.getDiscountAmount());
            ((CityViewHolder) holder).voucherAmount.setText(" ₹"+object.getVoucherAmount()+" ");
            ((CityViewHolder) holder).voucherAmount.setPaintFlags(((CityViewHolder) holder).voucherAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            /*((CityViewHolder) holder).payCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (buttonView.isChecked()) {
                        if (ClickListener != null) {
                            Log.e("planId",mList.get(position).getPlanId());
                            Log.e("getPlanCost",mList.get(position).getPlanCost());
                            Log.e("getPlanTrafficTotal",mList.get(position).getPlanTrafficTotal());
                            Log.e("getPlanName",mList.get(position).getPlanName());
                            ClickListener.seClickListener(String.valueOf(mList.get(position).getPlanId()), mList.get(position).getPlanCost(), mList.get(position).getPlanTrafficTotal(), mList.get(position).getPlanName());
                            selectedPosition = position;
                            notifyDataSetChanged();
                        }
                    } else {
                      //  Toast.makeText(context, "Unselected", Toast.LENGTH_SHORT).show();
                    }
                }
            });*/
        }
    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                } else {
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        if (discountClaimModels == null)
            return 0;
        return discountClaimModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
