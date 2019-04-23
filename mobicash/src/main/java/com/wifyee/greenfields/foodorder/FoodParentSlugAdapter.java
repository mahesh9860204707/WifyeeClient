package com.wifyee.greenfields.foodorder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifyee.greenfields.R;


import java.util.List;

/**
 * Created by user on 12/9/2017.
 */

public class FoodParentSlugAdapter extends RecyclerView.Adapter<FoodParentSlugAdapter.ViewHolder>{

    private List<CategoryList> mCategoryListItemCollection;
    Context mContext;
    private RecyclerView mRecyclerView;
    private final LayoutInflater mLayoutInflater;


    public FoodParentSlugAdapter(Context context, RecyclerView recyclerView, List<CategoryList> CategoryListItemCollection) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCategoryListItemCollection = CategoryListItemCollection;
        mRecyclerView = recyclerView;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_CategoryName;

        public ViewHolder(View v){
            super(v);
            tv_CategoryName = (TextView) v.findViewById(R.id.tv_categoryName);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view1 = LayoutInflater.from(mContext).inflate(R.layout.category_parentby_slug,parent,false);

        ViewHolder viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }


    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position){
        final CategoryList categoryListItem = mCategoryListItemCollection.get(position);

        //  holder.mDateValue.setText(logItem.date);
        Vholder.tv_CategoryName.setText(categoryListItem.categoryName);




        // imageLoader.DisplayImage(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+FoodOderItem.foodImage, Vholder.imag_foodimage);
        //  Picasso.with(mContext).load(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+FoodOderItem.foodImage).into(Vholder.imag_foodimage);


    }


    @Override
    public int getItemCount() {
        return (mCategoryListItemCollection != null) ? mCategoryListItemCollection.size() : 0;
    }

}
