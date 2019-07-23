package com.wifyee.greenfields.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.fragments.HomeFragment;

public class MoreCategory extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView recyclerView;
    private RecycleViewPagerAdapter recycleViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_category);

        mToolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.horizontal_recycler_view);
        TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.secondaryPrimary), PorterDuff.Mode.SRC_ATOP);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }

        toolbarTitle.setTypeface(Fonts.getSemiBold(this));

        recycleViewPagerAdapter = new RecycleViewPagerAdapter();
        LinearLayoutManager horizontalLayoutManagaer =  new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.setAdapter(recycleViewPagerAdapter);


    }

    private class RecycleViewPagerAdapter extends RecyclerView.Adapter<RecycleViewPagerAdapter.MyViewHolder> {

        private String[] title = {
                getString(R.string.menu_prepaid),
                getString(R.string.menu_postpaid),
                getString(R.string.menu_landline),
                getString(R.string.menu_dth),
                getString(R.string.menu_gas),
                getString(R.string.menu_broadband),
                getString(R.string.menu_electricity),
                "Wifyee Buy Plan",
                "Transaction",
                "Split Money",
                "Request Money",
                "Send Money",
                "Pay Merchant",
                "Add Money",
                //"Request Credit",
                //"Ticketing",
                //"Book Experiences",
                "Order Broadband"
        };

        private int[] mTabsIcons = {
                R.drawable.ic_prepaid,
                R.drawable.ic_postpaid,
                R.drawable.ic_landline,
                R.drawable.ic_dth,
                R.drawable.ic_gas,
                R.drawable.ic_broadband,
                R.drawable.ic_electricity,
                R.mipmap.prepaid_icon,
                R.drawable.ic_transact,
                R.drawable.ic_split_money,
                R.drawable.ic_request_money,
                R.drawable.ic_send_money,
                R.drawable.ic_pay,
                R.drawable.ic_add_money,
               // R.drawable.ic_request_credit,
               // R.mipmap.ticketing,
                //R.drawable. book_flight,
                R.drawable.ic_broadband
        };
        
        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView txtView;
            public ImageView icon;
            public RelativeLayout itemHolder;

            public MyViewHolder(View view) {
                super(view);
                txtView = (TextView) view.findViewById(R.id.title);
                icon = (ImageView) view.findViewById(R.id.icon);
                itemHolder = (RelativeLayout) view.findViewById(R.id.list_item_view);
            }
        }
        public RecycleViewPagerAdapter() {

        }
        @Override
        public RecycleViewPagerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pay_recharge_item, parent, false);

            return new RecycleViewPagerAdapter.MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final RecycleViewPagerAdapter.MyViewHolder holder, final int position) {
            holder.txtView.setText(title[position]);
            holder.icon.setImageResource(mTabsIcons[position]);
            holder.itemHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            startActivity(IntentFactory.createPrepaidActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;
                        case 1:
                            startActivity(IntentFactory.createPostpaidActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;
                        case 2:
                            startActivity(IntentFactory.createLandLineActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;
                        case 3:
                            startActivity(IntentFactory.createDthActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;
                        case 4:
                            startActivity(IntentFactory.createGasBillPaymentActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;
                        case 5:
                            startActivity(IntentFactory.createBroadbandBillPaymentActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            break;
                        case 6:
                            startActivity(IntentFactory.createElectricityBillPaymentActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;
                        case 7:
                            startActivity(IntentFactory.createWiFiPlansActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;
                        case 8:
                            startActivity(IntentFactory.createTransactionActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;

                        case 9:
                            startActivity(IntentFactory.createSplitMoneyActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;
                        case 10:
                            startActivity(IntentFactory.createRequestMoneyActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;
                        case 11:
                            startActivity(IntentFactory.createSendMoneyActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;
                        case 12:
                            startActivity(IntentFactory.scanQRCodeActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;
                        case 13:
                            startActivity(IntentFactory.createAddMoneyActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;

                        /*case 14:
                           startActivity(IntentFactory.createRequestCreditActivity(getApplicationContext()));
                           overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            break;
                        case 15:
                            startActivity(IntentFactory.createTicketingActivity(getApplicationContext()));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            break;
                        case 16:
                            startActivity(new Intent(getApplicationContext(), BookExperinceActivity.class));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            break;*/

                        case 14:
                            startActivity(new Intent(getApplicationContext(), RequestBroadbandActivity.class));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            break;

                        default:
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return title.length;
        }
    }

}
