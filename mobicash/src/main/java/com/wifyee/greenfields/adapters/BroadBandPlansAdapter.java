package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifyee.greenfields.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amit on 6/1/2018.
 */
public class BroadBandPlansAdapter extends RecyclerView.Adapter<BroadBandPlansAdapter.LogItemViewHolder> {

    private String speedData[] ;
    private String thirtiesData[];
    private String oneEightyDays[];
    private String threeSixtyDays[];
    private String yearlyData[];
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public BroadBandPlansAdapter(Context context,String speedDatas[],String thirtyDaysData[],String[] oneEightyDaysData,String[] threeSixtyData,String [] yearsData) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.speedData = speedDatas;
        this.thirtiesData = thirtyDaysData;
        this.oneEightyDays = oneEightyDaysData;
        this.threeSixtyDays = threeSixtyData;
        this.yearlyData = yearsData;
    }
    @Override
    public BroadBandPlansAdapter.LogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.inflate_broadband_plans_list, parent, false);
        return new BroadBandPlansAdapter.LogItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(BroadBandPlansAdapter.LogItemViewHolder holder, final int position)
    {
        holder.speedLimitData.setText(speedData[position]);
        holder.thirtyDayData.setText(thirtiesData[position]);
        holder.oneEightyDayData.setText(oneEightyDays[position]);
        holder.threeSixtyDayData.setText(threeSixtyDays[position]);
        holder.yearlydata.setText(yearlyData[position]);
    }

    @Override
    public int getItemCount() {
        return speedData.length;
    }

    public class LogItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.speed_limit)
        public TextView speedLimitData;

        @BindView(R.id.thirty_days)
        public TextView thirtyDayData;

        @BindView(R.id.one_eighty_days)
        public TextView oneEightyDayData;

        @BindView(R.id.three_sixty_days)
        public TextView threeSixtyDayData;

        @BindView(R.id.yearly)
        public TextView yearlydata;

        public LogItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}