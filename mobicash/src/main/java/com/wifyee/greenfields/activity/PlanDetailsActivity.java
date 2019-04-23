package com.wifyee.greenfields.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.adapters.HomePagerAdapter;

public class PlanDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String authKey="";
    private HomePagerAdapter homepagerAdapter;
    private View view;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        setTabLayout();
        setupTabIcons();
       // turnGPSOn();
    }
    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }
    //Set Tab Layout
    private void setTabLayout()
    {
        homepagerAdapter =new HomePagerAdapter(getSupportFragmentManager(),this,authKey);
        viewPager.setAdapter(homepagerAdapter);
        //Notice how the Tab Layout links with the Pager Adapter
        tabLayout.setTabsFromPagerAdapter(homepagerAdapter);
        //Notice how The Tab Layout adn View Pager object are linked
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void setupTabIcons()
    {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null);
        tabOne.setText("Plans");
        tabOne.setGravity(Gravity.CENTER);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null);
        tabTwo.setText("Offers");
        tabTwo.setGravity(Gravity.CENTER);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null);
        tabThree.setText("Promo Code");
        tabThree.setGravity(Gravity.CENTER);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
        finish();
    }


}
