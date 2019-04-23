package com.wifyee.greenfields.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoneyTransferFragment extends Fragment {

    // Tag for logging.
    public static final String TAG = MoneyTransferFragment.class.getSimpleName();

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.toolbar_back)
    ImageButton back;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout_money)
    TabLayout mTabLayout;


    public Toolbar getToolbar() {
        return mToolbar;
    }

    public MoneyTransferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_money_transfer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (mToolbar != null) {
            activity.setSupportActionBar(mToolbar);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });

        }

        MoneyTransferPagerAdapter pagerAdapter = new MoneyTransferPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mViewPager);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null && pagerAdapter.getTabView(i) != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }

            if (mTabLayout.getTabAt(0).getCustomView() != null) {
                mTabLayout.getTabAt(0).getCustomView().setSelected(true);
            }
        }

    }

    /**
     * money transfer pager adapter.
     */
    private class MoneyTransferPagerAdapter extends FragmentPagerAdapter {

        public MoneyTransferPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * tab text
         */
        private final String[] mTabsTitle = {getString(R.string.mobicash_code_fragment_title),
                getString(R.string.mobile_no_fragment_title),
                getString(R.string.show_code_fragment_title)};

        /**
         * tab icon
         */
        private int[] mTabsIcons = {
                R.mipmap.mobicash_code,
                R.mipmap.mobile_no,
                R.mipmap.show_code};

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new MobicashCodeFragment();
            } else if (position == 1) {
                return new MobileMoneyTransferFragment();
            } else if (position == 2) {
                return new MobicashShowCodeFragment();
            }
            return null;
        }

        /**
         * get custom tab view for money transfer
         *
         * @param position
         * @return
         */
        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View view = null;
            if (getActivity() != null && isAdded()) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.custome_tab_money_transfer, null);
                TextView title = (TextView) view.findViewById(R.id.title);
                title.setText(mTabsTitle[position]);
                ImageView icon = (ImageView) view.findViewById(R.id.icon);
                icon.setImageResource(mTabsIcons[position]);
                return view;
            }
            return view;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
