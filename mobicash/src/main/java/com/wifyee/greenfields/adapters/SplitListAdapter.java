package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.activity.SplitParticipientActivity;
import com.wifyee.greenfields.models.response.SplitListResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by admin on 27-12-2016.
 */

public class SplitListAdapter extends RecyclerView.Adapter<SplitListAdapter.LogItemViewHolder> {



    private List<SplitListResponse> mSplitListResponse;
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    final String inputFormat = "yyyy-MM-dd HH:mm:ss";
    final String outputFormat = "dd-MM-yyyy";

    public SplitListAdapter(Context context, List<SplitListResponse> splitListResponses) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSplitListResponse = splitListResponses;

    }

    @Override
    public LogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.split_list_inflate, parent, false);
        return new LogItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogItemViewHolder holder,final int position) {
        final SplitListResponse splitItem = mSplitListResponse.get(position);

        holder.mSplitDate.setText(splitItem.getSplitMoneyCreatedDate());
        try {
            holder.mSplitDate.setText(timeStampConverter(inputFormat, splitItem.getSplitMoneyCreatedDate(), outputFormat));
        } catch (ParseException e) {
            Timber.e("ParseException, Message : " + e.getMessage());
        }
        holder.mSplitRef.setText(splitItem.getSplitMoneyReferenceNumber());
        holder.mSplitAmount.setText(splitItem.getSplitMoneyAmount());
        holder.mSplitStats.setText(splitItem.getSplitMoneyStatus());
        holder.msplitPaidUsers.setText(splitItem.getSplitMoneyNumberOfParticipents());
        holder.mSplitSerailNumber.setText(String.valueOf(position+1));
        holder.mainlayLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SplitParticipientActivity.class).putExtra("splitId",mSplitListResponse.get(position).getSplitMoneyId()));
            }
        });


    }

    private static String timeStampConverter(final String inputFormat,
                                             String inputTimeStamp, final String outputFormat)
            throws ParseException {

       /* SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat Targetformatter = new SimpleDateFormat("dd-MM-yyyy");

        String dateInString = "2016-07-15 14:13:33";

        try {

            Date date = formatter.parse(dateInString);
            System.out.println(date);
            System.out.println(Targetformatter.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }*/


        return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(
                inputFormat).parse(inputTimeStamp));
    }

    @Override
    public int getItemCount() {
        return (mSplitListResponse != null) ? mSplitListResponse.size() : 0;
    }

    public void cleartLogItemCollection() {
        mSplitListResponse.clear();
    }

    public void setLogItemCollection(List<SplitListResponse> logItemCollection) {
        for (SplitListResponse logItem : logItemCollection) {
            mSplitListResponse.add(0, logItem);
        }
        notifyDataSetChanged();
    }

    public class LogItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.split_date)
        public TextView mSplitDate;

        @BindView(R.id.split_ref)
        public TextView mSplitRef;

        @BindView(R.id.split_balance)
        public TextView mSplitAmount;

        @BindView(R.id.status)
        public TextView mSplitStats;

        @BindView(R.id.paid_users)
        public TextView msplitPaidUsers;

        @BindView(R.id.serial_number)
        public TextView mSplitSerailNumber;

        @BindView(R.id.main_layout)
        public LinearLayout mainlayLinearLayout;


        public LogItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


