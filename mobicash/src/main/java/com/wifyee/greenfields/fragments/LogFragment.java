package com.wifyee.greenfields.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.CustomSwipeRefreshLayout;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.adapters.LogsAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.models.requests.ClientLogRequest;
import com.wifyee.greenfields.models.response.ClientLogResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.LogItem;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by sumanta on 12/23/16.
 */

public class LogFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_USER_CLIENT_LOG_SUCCESS,
            NetworkConstant.STATUS_USER_CLIENT_LOG_FAIL
    };


    public interface LogFragmentListener {
        void showProgress();

        void cancelProgress();

        void overridePendingTransition();

        void showError(String msg);

    }

    //swipe refresh layout
    private CustomSwipeRefreshLayout swipeLayout;

    @BindView(R.id.log_list_rv)
    RecyclerView mLogListRecyclerView;

    @BindView(R.id.empty_view_container)
    LinearLayout emptyView;

    private LogsAdapter mLogAdapter;
    private Context mContext = null;

    /**
     * listener for handling request progress, transition and error message
     */
    LogFragmentListener mLogFragmentListener;
    public LogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    private void resetSwipeView() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        swipeLayout.setAcceptEvents(false);
    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(true);
        //refresh data
        requestData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.log_fragment, container, false);
        ButterKnife.bind(this, view);
        mLogFragmentListener = (LogFragmentListener) getActivity();
        swipeLayout = (CustomSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright);

        setupUI();
        return view;
    }

    private void setupUI()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mLogListRecyclerView.setLayoutManager(linearLayoutManager);
        mLogAdapter = new LogsAdapter(getActivity(), mLogListRecyclerView, new ArrayList<LogItem>());
        mLogListRecyclerView.setAdapter(mLogAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(clientLogsReceiver, new IntentFilter(action));
        }
        requestData();
    }

    /**
     * start request for log
     */
    private void requestData() {
        ClientLogRequest mClientLogRequest = new ClientLogRequest();
        try {
            mClientLogRequest.clientId = LocalPreferenceUtility.getUserCode(mContext);
            mClientLogRequest.pincode = LocalPreferenceUtility.getUserPassCode(mContext);
            mClientLogRequest.hash = MobicashUtils.getSha1(mClientLogRequest.clientId + MobicashUtils.md5(mClientLogRequest.pincode));
        } catch (NoSuchAlgorithmException e) {
            Timber.e("NoSuchAlgorithmException message " + e.getMessage());
        }
        MobicashIntentService.startActionClientLog(mContext, mClientLogRequest);
    }


    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(clientLogsReceiver);
    }

    /**
     * Handling broadcast event for user log .
     */
    private BroadcastReceiver clientLogsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            mLogFragmentListener.cancelProgress();
            if (action != null && action.equals(NetworkConstant.STATUS_USER_CLIENT_LOG_SUCCESS)) {
                ClientLogResponse mClientLogResponse = (ClientLogResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (mClientLogResponse != null && mClientLogResponse.logList != null
                        && mClientLogResponse.logList.mobicashClientLogItem != null) {
                    emptyView.setVisibility(View.GONE);
                    mLogAdapter.setLogItemCollection(mClientLogResponse.logList.mobicashClientLogItem);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
                resetSwipeView();
            } else if (action != null && action.equals(NetworkConstant.STATUS_USER_CLIENT_LOG_FAIL)) {
                emptyView.setVisibility(View.VISIBLE);
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null) {
                    mLogFragmentListener.showError(failureResponse.msg);
                } else {
                    String errorMessage = getString(R.string.error_message);
                    mLogFragmentListener.showError(errorMessage);
                }
                resetSwipeView();
            }
        }
    };

}