package com.wifyee.greenfields.adapters;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.foodorder.CategoryList;
import com.wifyee.greenfields.foodorder.FoodOrderListActivity;
import com.wifyee.greenfields.foodorder.SharedPrefenceList;
import com.wifyee.greenfields.models.CountriesList;

import java.util.List;

/**
 * Created by user on 12/9/2017.
 */

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> {
    private List<CountriesList> mCountriesLists;
    private String id="";
    private Context context;
    private SharedPrefenceList productItem = new SharedPrefenceList();
    private Double actual_foodAmount = 0.00;
    private CountriesAdapter.CountriesListener mListener;

    public CountriesAdapter(Context ctx, List<CountriesList> list,CountriesAdapter.CountriesListener mListener) {
        this.mCountriesLists = list;
        this.context = ctx;
        this.mListener = mListener;
    }

    @Override
    public CountriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_foodorder_view_layout, parent, false);
     */
        View view1 = LayoutInflater.from(context).inflate(R.layout.countries_list,parent,false);
        return new CountriesAdapter.ViewHolder(view1);

    }

    @Override
    public void onBindViewHolder(final CountriesAdapter.ViewHolder holder, final int position) {
        final CountriesList object = mCountriesLists.get(position);
        if (object != null) {

            String upperString = object.getName().substring(0,1).toUpperCase() + object.getName().substring(1);

            holder.tv_countryName.setText(upperString+" (+"+object.getPhoneCode()+")");

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_action_phone)
                    .error(R.drawable.ic_action_phone)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(NetworkConstant.MOBICASH_BASE_URL_TESTING+object.getFlag128())
                    .apply(options)
                    .into(holder.imag_countries);

            holder.tv_countryName.setTypeface(Fonts.getSemiBold(context));

            holder.tv_countryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCountriesClecked(object);
                }
            });

            

        }
    }



    @Override
    public int getItemCount() {

        return mCountriesLists.size();
    }

    /*@Override
    public int getItemViewType(int position) {
        return mCountriesLists.get(position);
    }*/


    public  class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_countryName;
        private ImageView imag_countries;
        private LinearLayout ll,llAdd,llIncrDecr;

        public ViewHolder(View v) {
            super(v);
            tv_countryName =  v.findViewById(R.id.tv_countryName);
            imag_countries = v.findViewById(R.id.imag_countries);
           
        }
    }

    public interface CountriesListener {
        void onCountriesClecked(CountriesList item);

    }

  
   
   
   

}
