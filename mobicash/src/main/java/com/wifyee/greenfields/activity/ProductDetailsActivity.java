package com.wifyee.greenfields.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.MobicashApplication;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.HorizontalView;
import com.wifyee.greenfields.Utils.TouchImageView;
import com.wifyee.greenfields.adapters.HorizontalImageAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.models.requests.SellingResponse;

public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener,
        HorizontalImageAdapter.OnItemOnclickImageListener,HorizontalImageAdapter.OnDoubleTapListener
{
    private SellingResponse sellingResponse;
    private TextView itemName,itemPrice,itemDiscount,itemDiscountType;
    private Button payButton;
    private String itemAmount="";
    private HorizontalView listView;
    private HorizontalImageAdapter imageAdapter;
    private ImageLoader _ImageLoader;
    private Toolbar mToolbar;
    private ImageButton back;
    private NetworkImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        back = (ImageButton) findViewById(R.id.toolbar_back);
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
        sellingResponse=(SellingResponse) getIntent().getSerializableExtra("ArrayValue");
        listView = (HorizontalView) findViewById(R.id.bottom_galary);
        imageView = (NetworkImageView) findViewById(R.id.img_view);
        bindUi();
        //LinearLayout layout = (LinearLayout)findViewById(R.id.linear_layout_params);
        if(sellingResponse!=null)
        {
            if(sellingResponse.getImageObj()!=null)
            {

                imageAdapter = new HorizontalImageAdapter(this,this,sellingResponse.getImageObj() );
                listView.setAdapter(imageAdapter);

//                for(int i=0;i<sellingResponse.getImageObj().size();i++) {
//                    final ImageView image = new ImageView(this);
//                    if(sellingResponse.getImageObj()!=null) {
//                        if(sellingResponse.getImageObj().get(i).getImagePath()!=null) {
//                            final int j=i;
//                            Picasso.with(this).load(sellingResponse.getImageObj().get(i).getImagePath()).into(image);
//                            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300));
//                            image.setMaxHeight(200);
//                            image.setMaxWidth(150);
//                            image.setBackgroundResource(R.drawable.rectangle_shape);
//                            image.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    String imagePath=sellingResponse.getImageObj().get(j).getImagePath();
//                                    zoomImageView(ProductDetailsActivity.this,imagePath);
//                                }
//                            });
//                            // Adds the view to the layout
//                            layout.addView(image);
//                        }
//                    }
//                }
            }
            else{
                Toast.makeText(this,"Image Size is Zero",Toast.LENGTH_SHORT).show();
            }
        }

    }

    //Zooming Image View
    private void zoomImageView(final Context activity, String prescriptionImage)
    {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_zoom_medical);
        // Set dialog title
        layout.setTitle("Zoom Image");
        Button yes = (Button) layout.findViewById(R.id.yes);
        layout.show();
        final TouchImageView imageview=(TouchImageView) layout.findViewById(R.id.zoom_imageview);
        Picasso.with(this).load(prescriptionImage).into(imageview);
        imageview.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
                PointF point = imageview.getScrollPosition();
                RectF rect = imageview.getZoomedRect();
                float currentZoom = imageview.getCurrentZoom();
                boolean isZoomed = imageview.isZoomed();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    layout.dismiss();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    //Bind UI Data
    private void bindUi() {
        itemName=(TextView)findViewById(R.id.item_name);
        itemName.setText(sellingResponse.getItemName());
        itemPrice=(TextView)findViewById(R.id.item_price);
        itemPrice.setText(sellingResponse.getItemPrice());
        itemDiscount=(TextView)findViewById(R.id.item_discount);
        itemDiscount.setText(sellingResponse.getDiscount());
        itemDiscountType=(TextView)findViewById(R.id.item_discount_type);
        itemDiscountType.setText(sellingResponse.getDiscountType());
        payButton=(Button)findViewById(R.id.payuMoney);
        payButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        itemAmount=itemPrice.getText().toString();
        Intent intent=new Intent(this,PayUBaseActivity.class);
        intent.putExtra(PaymentConstants.STRING_EXTRA, itemAmount);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        //String imagePath=sellingResponse.getImageObj().get(position).getImagePath();
       // zoomImageView(ProductDetailsActivity.this,imagePath);
         if(sellingResponse.getImageObj() != null) {
             imageView.setImageUrl(sellingResponse.getImageObj().get(position).getImagePath(), MobicashApplication.getInstance().getImageLoader());

        }
    }

    @Override
    public void onDoubleTap(int position) {
        Intent i = new Intent(this, FullImageViewActivity.class);
        i.putExtra("position", sellingResponse.getImageObj().get(position).getImagePath());
        startActivity(i);
    }
}
