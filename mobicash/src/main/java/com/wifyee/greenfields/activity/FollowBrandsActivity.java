package com.wifyee.greenfields.activity;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.adapters.FollowBrandsAdapter;

public class FollowBrandsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton back;
    private FollowBrandsAdapter followBrandsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private int images[]={R.drawable.jeans,R.drawable.jeans_second,R.drawable.jeans_third,R.drawable.jeans_four,R.drawable.jeans,R.drawable.jeans_second};
    private String brandsName[]={"Levis","Allen Cooper","Killer","Mufti","Spyker","Arrow"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_brands);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        back= (ImageButton) findViewById(R.id.toolbar_back);
        recyclerView= (RecyclerView) findViewById(R.id.recycler_follow);
        if (mToolbar != null) {
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

        bindAdapter(images,brandsName);
    }

    private void bindAdapter(int[] images, String[] brandsName)
    {
        followBrandsAdapter=new FollowBrandsAdapter(this,images,brandsName);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(followBrandsAdapter);
    }
}
