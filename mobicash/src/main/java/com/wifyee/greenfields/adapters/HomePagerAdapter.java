package com.wifyee.greenfields.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.wifyee.greenfields.fragments.OffersFragment;
import com.wifyee.greenfields.fragments.PromoCodeFragment;
import com.wifyee.greenfields.fragments.RetailPlanFragment;


/**
 * Created by Amit on 18/01/16.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter
{
     Activity activity;
     private String authValue="";

    public HomePagerAdapter(FragmentManager fm, Activity act, String auth) {
        super(fm);
        activity=act;
        authValue=auth;
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0: return RetailPlanFragment.getinstance();
            case 1: return OffersFragment.getinstance();
            case 2: return PromoCodeFragment.getinstance();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Plans";
            case 1:
                return "Offers";
            case 2:
                return "Promo Code";
        }
        return "";
    }

}
