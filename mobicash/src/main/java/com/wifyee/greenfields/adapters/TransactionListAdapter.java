package com.wifyee.greenfields.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.interfaces.OnClickListenerUpdateKYC;
import com.wifyee.greenfields.models.requests.KYCDocumentsBean;
import com.wifyee.greenfields.models.requests.TransactionBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amit Kumar on 27-12-2016.
 */
public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.LogItemViewHolder> {

    private List<TransactionBean> mTransactionBeanList;
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    public OnClickListenerUpdateKYC ClickListener;

    public TransactionListAdapter(Context context, List<TransactionBean> mTransactionList) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mTransactionBeanList = mTransactionList;
    }

    @Override
    public LogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.transaction_list_item, parent, false);
        return new LogItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogItemViewHolder holder, final int position)
    {
        final TransactionBean transactionItem = mTransactionBeanList.get(position);
        holder.mTransactionid.setText(transactionItem.getTXN_ID());
        holder.mTransactionDate.setText(transactionItem.getTRANSACTION_DATE());
        holder.mStatus.setText(transactionItem.getSTATUS());
        holder.mBenMobile.setText(transactionItem.getBENEFICIARY_MOBILE());
        holder.mAmount.setText(transactionItem.getAMOUNT());
        holder.mAccountNumber.setText(transactionItem.getACCOUNT_NUMBER());
        holder.mIfscCode.setText(transactionItem.getIFSC_CODE());
        holder.mReferenceNumber.setText(transactionItem.getREFERENCE_NO());
    }

    @Override
    public int getItemCount() {
        return (mTransactionBeanList != null) ? mTransactionBeanList.size() : 0;
    }

    public void cleartLogItemCollection() {
        mTransactionBeanList.clear();
    }

    public void setLogItemCollection(List<TransactionBean> transItemCollection) {
        for (TransactionBean transactionItem : transItemCollection) {
            mTransactionBeanList.add(0, transactionItem);
        }
        notifyDataSetChanged();
    }

    public class LogItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.transaction_id)
        public TextView mTransactionid;

        @BindView(R.id.text_transaction_date)
        public TextView mTransactionDate;

        @BindView(R.id.text_transaction_status)
        public TextView mStatus;


        @BindView(R.id.text_transaction_amount)
        public TextView mAmount;

        @BindView(R.id.text_transaction_ben_mobile)
        public TextView mBenMobile;

        @BindView(R.id.text_transaction_acc_no)
        public TextView mAccountNumber;

        @BindView(R.id.text_transaction_ifsc)
        public TextView mIfscCode;

        @BindView(R.id.text_transaction_refrence_number)
        public TextView mReferenceNumber;

        public LogItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


