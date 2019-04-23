package com.wifyee.greenfields.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.activity.KYCActivity;
import com.wifyee.greenfields.interfaces.OnClickTravelPackage;
import com.wifyee.greenfields.models.requests.KYCDocumentsBean;
import com.wifyee.greenfields.services.MobicashIntentService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amit on 6/1/2018.
 */

public class BookExperienceAdapter extends  RecyclerView.Adapter<BookExperienceAdapter.LogItemViewHolder> {
    private int brandImages[] ;
    private String brandNames[];
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    private OnClickTravelPackage onClickTravelPackage;

    public BookExperienceAdapter(Context context, int brandImage[], String brandName[],OnClickTravelPackage onClickTravelPackages) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.brandImages = brandImage;
        this.brandNames = brandName;
        this.onClickTravelPackage= onClickTravelPackages;
    }
    @Override
    public BookExperienceAdapter.LogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.book_experience_inflate, parent, false);
        return new BookExperienceAdapter.LogItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookExperienceAdapter.LogItemViewHolder holder, final int position)
    {
        holder.brandName.setText(brandNames[position]);
        holder.brandImage.setImageResource(brandImages[position]);
        holder.brandImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               onClickTravelPackage.onClickTravels(mContext);
            }
        });
    }



    @Override
    public int getItemCount() {
        return brandImages.length;
    }

    public class LogItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.brand_name)
        public TextView brandName;

        @BindView(R.id.brand_image)
        public ImageView brandImage;

        public LogItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}