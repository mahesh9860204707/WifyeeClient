package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.interfaces.OnClickListener;
import com.wifyee.greenfields.models.DiscountClaimModel;
import com.wifyee.greenfields.models.VoucherModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class VoucherListAdapter extends RecyclerView.Adapter<VoucherListAdapter.MyViewHolder> {

    private List<VoucherModel> voucherModels;
    public OnClickListener ClickListener;
    private Context context;
    int flag;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView voucherName, voucherDetails, discountAmount, voucherAmount,txtVoucherAmt,payNow,txtValidity;
        private ImageView imageView;
        private RelativeLayout rlBg;

        public MyViewHolder(View itemView) {
            super(itemView);
            voucherName =  itemView.findViewById(R.id.voucher_name);
            voucherDetails =  itemView.findViewById(R.id.voucher_details);
            discountAmount =  itemView.findViewById(R.id.discount_amount);
            voucherAmount =  itemView.findViewById(R.id.voucher_amount);
            payNow =  itemView.findViewById(R.id.pay_now);
            txtVoucherAmt =  itemView.findViewById(R.id.txt_voucher_amt);
            txtValidity =  itemView.findViewById(R.id.txt_validity);
            imageView =  itemView.findViewById(R.id.imageView);
            rlBg =  itemView.findViewById(R.id.rl_bg);
        }
    }

    public VoucherListAdapter(Context ctx,List<VoucherModel> voucherModels,int flag) {
        this.voucherModels = voucherModels;
        this.context = ctx;
        this.flag = flag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (flag==0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vouchers_item, parent, false);
        }else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vouchers_list_item, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        VoucherModel voucher = voucherModels.get(position);

        holder.voucherName.setText(voucher.getVoucherName());
        holder.voucherDetails.setText(voucher.getVoucherDetails());
        holder.discountAmount.setText("₹".concat(voucher.getDiscountAmount()));
        holder.voucherAmount.setText(" ₹".concat(voucher.getVoucherAmount()+" "));
        holder.voucherAmount.setPaintFlags(holder.voucherAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtValidity.setText(getDate(voucher.getValidFrom()).concat(" to ").concat(getDate(voucher.getValidUpto())));

        holder.voucherName.setTypeface(Fonts.getSemiBold(context));
        holder.voucherDetails.setTypeface(Fonts.getRegular(context));
        holder.discountAmount.setTypeface(Fonts.getRegular(context));
        holder.voucherAmount.setTypeface(Fonts.getRegular(context));
        holder.txtVoucherAmt.setTypeface(Fonts.getRegular(context));
        holder.payNow.setTypeface(Fonts.getSemiBold(context));
        holder.txtValidity.setTypeface(Fonts.getRegular(context));
        Glide.with(context).load(voucher.getImageUrl()).into(holder.imageView);

        int[] androidColors = context.getResources().getIntArray(R.array.credit_color);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.rlBg.setBackgroundColor(randomAndroidColor);
    }


    @Override
    public int getItemCount() {
        return voucherModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public String getDate(String date){
        String finalDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.US);

        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd yyyy",Locale.US);
        finalDate = timeFormat.format(myDate);

        return finalDate;
    }
}
