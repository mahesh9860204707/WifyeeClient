package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;


import com.wifyee.greenfields.R;
import com.wifyee.greenfields.wifimodel.CornettoPlan;

import java.util.ArrayList;

public class MyBaseAdapter extends BaseAdapter {

    ArrayList<CornettoPlan> myList = new ArrayList<CornettoPlan>();
    LayoutInflater inflater;
    Context context;


    public MyBaseAdapter(Context context, ArrayList<CornettoPlan> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    public void updateCheckboxes(int pos){
        for (int i=0;i<this.myList.size();i++){
            this.myList.get(i).isSelected = false;
        }
        this.myList.get(pos).isSelected = true;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public CornettoPlan getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_plan_tem, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        CornettoPlan currentListData = getItem(position);

        mViewHolder.desc.setText(currentListData.sDesc);
        mViewHolder.cost.setText("RS. "+currentListData.cost);
        if(currentListData.isSelected){
            mViewHolder.rb_Choice.setChecked(true);
        } else {
            mViewHolder.rb_Choice.setChecked(false);
        }
        return convertView;
    }

    private class MyViewHolder {
        TextView desc;
        TextView cost;
        RadioButton rb_Choice;

        public MyViewHolder(View item) {
            desc = (TextView) item.findViewById(R.id.desc);
            cost = (TextView) item.findViewById(R.id.cost);
            rb_Choice = (RadioButton) item.findViewById(R.id.rb_Choice);
        }
    }
}