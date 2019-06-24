package com.wifyee.greenfields.activity;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.adapters.CashBackAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.foodorder.ModelMapper;
import com.wifyee.greenfields.models.CashbackModel;
import com.wifyee.greenfields.models.OrderModel;
import com.wifyee.greenfields.models.response.FailureResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MyCashback extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CashBackAdapter adapter;
    private List<CashbackModel> cashbackModel = new ArrayList<>();
    private ProgressDialog progressDialog;
    CashbackModel[] cash;
    TextView totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashback);

        Toolbar toolbar = findViewById(R.id.toolbar);
        totalAmount = findViewById(R.id.total_amount);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Cashback");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);

        initCollapsingToolbar();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new CashBackAdapter(this, cashbackModel);
        recyclerView.setAdapter(adapter);

        dataLoad();

    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("My Cashback");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(MyCashback.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIcon(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    private void dataLoad() {
        if (cashbackModel != null) {
            cashbackModel.clear();
        }

        showProgressDialog();

        AndroidNetworking.get(NetworkConstant.MOBICASH_BASE_URL_TESTING +
                NetworkConstant.PARAM_MY_CASHBACK + LocalPreferenceUtility.getUserCode(MyCashback.this))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject object) {
                        Log.e("--RESPONSE--", object.toString());
                        Timber.e("called onResponse of All Order.");
                        cancelProgressDialog();
                        try {
                            if (object.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                JSONArray jsonArray = object.getJSONArray("cashbackList");
                                cash = new CashbackModel[jsonArray.length()];

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    cash[i] = new CashbackModel(
                                            jsonObject.getString("order_id"),
                                            jsonObject.getString("order_amt"),
                                            jsonObject.getString("cashback_amt"),
                                            jsonObject.getString("order_on"),
                                            jsonObject.getString("cashback_status"),
                                            jsonObject.getString("cashback_credited_date")
                                    );
                                    cashbackModel.add(cash[i]);
                                }
                                recyclerView.getRecycledViewPool().clear();
                                adapter.notifyDataSetChanged();
                                double amount = 0.0;
                                if(cashbackModel.size()>0) {
                                    int size = cashbackModel.size();
                                    for (int i = 0; i < size; i++) {
                                        double quantityAmount = Double.parseDouble(cashbackModel.get(i).getCashbackAmt());
                                        amount = amount + quantityAmount;
                                    }
                                }

                                totalAmount.setText(String.valueOf(amount));
                            } else {
                                Toast.makeText(MyCashback.this,
                                        "Something went wrong. PLease try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            cancelProgressDialog();
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        cancelProgressDialog();
                        // handle error
                        Timber.e("called onError of User Invoice API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("UserInvoice = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                    }
                });
    }
}
