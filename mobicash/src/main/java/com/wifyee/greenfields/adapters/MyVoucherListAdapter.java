package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.activity.VoucherDetails;
import com.wifyee.greenfields.dairyorder.DairyItemListActivity;
import com.wifyee.greenfields.foodorder.FoodOrderListActivity;
import com.wifyee.greenfields.foodorder.MerchantActivity;
import com.wifyee.greenfields.interfaces.ItemClickListener;
import com.wifyee.greenfields.interfaces.OnClickListener;
import com.wifyee.greenfields.models.MyVoucherModel;
import com.wifyee.greenfields.models.VoucherModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class MyVoucherListAdapter extends RecyclerView.Adapter<MyVoucherListAdapter.MyViewHolder> {

    private List<MyVoucherModel> voucherModels;
    public OnClickListener ClickListener;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView voucherName, voucherDetails, voucherAmount,txtVoucherAmt,payNow,txtValidity,quantity;
        private ImageView imageView;
        private RelativeLayout rlBg;
        private LinearLayout ll;
        private ItemClickListener clickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            voucherName =  itemView.findViewById(R.id.voucher_name);
            voucherDetails =  itemView.findViewById(R.id.voucher_details);
            voucherAmount =  itemView.findViewById(R.id.voucher_amount);
            quantity =  itemView.findViewById(R.id.qty);
            payNow =  itemView.findViewById(R.id.pay_now);
            txtVoucherAmt =  itemView.findViewById(R.id.txt_voucher_amt);
            txtValidity =  itemView.findViewById(R.id.txt_validity);
            imageView =  itemView.findViewById(R.id.imageView);
            rlBg =  itemView.findViewById(R.id.rl_bg);
            ll =  itemView.findViewById(R.id.ll);

            context = itemView.getContext();
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }


        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), false);
        }

    }

    public MyVoucherListAdapter(Context ctx, List<MyVoucherModel> voucherModels) {
        this.voucherModels = voucherModels;
        this.context = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_vouchers_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MyVoucherModel voucher = voucherModels.get(position);

        holder.voucherName.setText(voucher.getVoucherName());
        holder.voucherDetails.setText(voucher.getVoucherDetails());
        holder.quantity.setText(voucher.getQuantity());
        holder.voucherAmount.setText("Bal : ₹".concat(voucher.getBalanceAmount()+" "));

        holder.voucherName.setTypeface(Fonts.getSemiBold(context));
        holder.voucherDetails.setTypeface(Fonts.getRegular(context));
        holder.quantity.setTypeface(Fonts.getSemiBold(context));
        holder.voucherAmount.setTypeface(Fonts.getRegular(context));
        holder.txtVoucherAmt.setTypeface(Fonts.getRegular(context));
        holder.payNow.setTypeface(Fonts.getSemiBold(context));
        holder.txtValidity.setTypeface(Fonts.getRegular(context));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.oriz_icon)
                .error(R.drawable.oriz_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context).load(voucher.getImageUrl())
                .apply(options)
                .into(holder.imageView);

        int[] androidColors = context.getResources().getIntArray(R.array.credit_color);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.rlBg.setBackgroundColor(randomAndroidColor);
        GradientDrawable drawable = (GradientDrawable)holder.quantity.getBackground();
        drawable.setColor(randomAndroidColor);

        if(voucher.getIsVoucherExpired().equalsIgnoreCase("Y")){
            holder.txtValidity.setText("Expired : ".concat(getDate(voucher.getValidUpto())));
        }else {
            holder.txtValidity.setText("Expires : ".concat(getDate(voucher.getValidUpto())));
        }

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(voucher.getIsVoucherExpired().equalsIgnoreCase("n")){
                    if(voucher.getMerType().equals("6")){
                        Intent intent = new Intent(context, FoodOrderListActivity.class);
                        intent.putExtra("merchantid",voucher.getMerId());
                        intent.putExtra("merchantName",voucher.getMerName());
                        intent.putExtra("current_status",voucher.getMerCurrentStatus());
                        intent.putExtra("flag","voucher");
                        intent.putExtra("v_id",voucher.getVoucherId());
                        intent.putExtra("total_bal",voucher.getBalanceAmount());
                        intent.putExtra("tuv_id",voucher.getId());
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context, DairyItemListActivity.class);
                        intent.putExtra("merchantid",voucher.getMerId());
                        intent.putExtra("merchantName",voucher.getMerName());
                        intent.putExtra("current_status",voucher.getMerCurrentStatus());
                        intent.putExtra("flag","voucher");
                        intent.putExtra("v_id",voucher.getVoucherId());
                        intent.putExtra("total_bal",voucher.getBalanceAmount());
                        intent.putExtra("tuv_id",voucher.getId());
                        context.startActivity(intent);
                    }
                }else {
                    Snackbar.make(view,voucher.getVoucherName().concat(" Voucher expired"),Snackbar.LENGTH_LONG).show();
                }
            }
        });
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
                "yyyy-MM-dd", Locale.US);

        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy",Locale.US);
        finalDate = timeFormat.format(myDate);

        return finalDate;
    }

    private int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
    }
}
