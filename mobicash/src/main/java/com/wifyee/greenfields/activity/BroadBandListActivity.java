package com.wifyee.greenfields.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.adapters.BroadBandPlansAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BroadBandListActivity extends AppCompatActivity
{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_back)
    ImageButton back;
    @BindView(R.id.broadband_list)
    RecyclerView broadBandList;
    private BroadBandPlansAdapter broadBandPlansAdapter;

    private String speedData[]={"upto 5Mbps","upto 8Mbps","upto 10Mbps","upto 15mbps","upto 20Mbps","upto 25Mbps","upto 30Mbps","upto 50Mbps"};
    private String thirtydays[]={"\u20B9"+"500","\u20B9"+"600","\u20B9"+"700","\u20B9"+"900","\u20B9"+"1050","\u20B9"+"1250",
            "\u20B9"+"1350","\u20B9"+"1700"};
    private String oneEightydays[]={"\u20B9"+"3000","\u20B9"+"3050","\u20B9"+"3850","\u20B9"+"4950","\u20B9"+"5750","\u20B9"+"6850",
            "\u20B9"+"7450","\u20B9"+"9250"};
    private String sixtyFivedays[]={"\u20B9"+"6000","\u20B9"+"6600","\u20B9"+"7000","\u20B9"+"9000","\u20B9"+"10500","\u20B9"+"12500",
            "\u20B9"+"13500","\u20B9"+"17000"};
    private String yearlyData[]={"upto 10Mbps","upto 16Mbps","upto 20mbps","upto 30Mbps","upto 40Mbps","upto 50Mbps","upto 60Mbps","upto 100Mbps"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_band_list);
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //Bind adapter data
        bindAdapter(speedData,thirtydays,oneEightydays,sixtyFivedays,yearlyData);
        if (mToolbar!= null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }
    }
    //Binding adapter of BroadBand PlanList
    private void bindAdapter(String[] speedData, String[] thirtydays, String[] oneEightydays, String[] sixtyFivedays, String[] yearlyData) {
        broadBandPlansAdapter=new BroadBandPlansAdapter(this,speedData,thirtydays,oneEightydays,sixtyFivedays,yearlyData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        broadBandList.setLayoutManager(linearLayoutManager);
        broadBandList.addItemDecoration(new DividerItemDecoration(broadBandList.getContext(), DividerItemDecoration.VERTICAL));
        broadBandList.setAdapter(broadBandPlansAdapter);
    }
}
