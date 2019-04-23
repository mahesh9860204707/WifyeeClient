package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.models.response.LogItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by admin on 27-12-2016.
 */

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.LogItemViewHolder> {


    private RecyclerView mRecyclerView;
    private List<LogItem> mLogItemCollection;
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    final String inputFormat = "yyyy-MM-dd HH:mm:ss";
    final String outputFormat = "dd-MM-yyyy";

    public LogsAdapter(Context context, RecyclerView recyclerView, List<LogItem> logItemCollection)
    {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLogItemCollection = logItemCollection;
        mRecyclerView = recyclerView;
    }

    @Override
    public LogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.log_list_item, parent, false);
        return new LogItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogItemViewHolder holder, int position) {
        final LogItem logItem = mLogItemCollection.get(position);

        holder.mLogDate.setText(logItem.logDate);
        try {
            holder.mLogDate.setText(timeStampConverter(inputFormat, logItem.logDate, outputFormat));
        } catch (ParseException e) {
            Timber.e("ParseException, Message : " + e.getMessage());
        }
        holder.mLogRef.setText(logItem.logId);
        holder.mLogBalance.setText(logItem.logBalance);

        holder.mLogCreditOrDebit.setText(logItem.logCredit + "/" + logItem.logDebit);
        holder.mLogFees.setText(logItem.logFees);
        holder.mLogDescription.setText(logItem.logDescription);
    }

    private static String timeStampConverter(final String inputFormat, String inputTimeStamp, final String outputFormat)
            throws ParseException {

        return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(
                inputFormat).parse(inputTimeStamp));
    }

    @Override
    public int getItemCount() {
        return (mLogItemCollection != null) ? mLogItemCollection.size() : 0;
    }

    public void cleartLogItemCollection() {
        mLogItemCollection.clear();
    }

    public void setLogItemCollection(List<LogItem> logItemCollection) {
        for (LogItem logItem : logItemCollection) {
            mLogItemCollection.add(0, logItem);
        }
        notifyDataSetChanged();
    }

    public class LogItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.log_date)
        public TextView mLogDate;

        @BindView(R.id.log_ref)
        public TextView mLogRef;

        @BindView(R.id.log_balance)
        public TextView mLogBalance;

        @BindView(R.id.log_credit_debit)
        public TextView mLogCreditOrDebit;

        @BindView(R.id.log_fees)
        public TextView mLogFees;

        @BindView(R.id.log_description)
        public TextView mLogDescription;


        public LogItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


