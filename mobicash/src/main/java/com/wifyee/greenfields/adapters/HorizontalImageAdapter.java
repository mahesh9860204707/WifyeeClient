package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.android.volley.toolbox.NetworkImageView;
import com.wifyee.greenfields.MobicashApplication;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.models.requests.ImageObjects;

import java.util.ArrayList;

public class HorizontalImageAdapter  extends BaseAdapter {

    private Context context;
    private ArrayList<ImageObjects> subCategoryContentModelList = null;
    private LayoutInflater l_Inflater;
    private OnItemOnclickImageListener onItemOnclickImageListener;
    private OnDoubleTapListener onDoubleTapListener;


    public HorizontalImageAdapter(OnItemOnclickImageListener callBack,
                                  OnDoubleTapListener callDoubleTap, ArrayList<ImageObjects> listItem) {

        this.onItemOnclickImageListener = callBack;
        this.onDoubleTapListener = callDoubleTap;
        this.subCategoryContentModelList = listItem;

    }

    public interface OnItemOnclickImageListener {
        void onItemClick(int position);
    }

    public interface OnDoubleTapListener {
        void onDoubleTap(int position);
    }

    @Override
    public int getCount() {
        return subCategoryContentModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        final ViewHolder viewHolder;
        l_Inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.listview_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageUrl(subCategoryContentModelList.get(position).getImagePath(), MobicashApplication.getInstance().getImageLoader());
//        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onItemOnclickImageListener.onItemClick(subCategoryContentModelList.get(position).getContentUrl(),
//                        subCategoryContentModelList.get(position).getDescription(),subCategoryContentModelList.get(position).getTitle());
//            }
//        });

        viewHolder.imageView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    onDoubleTapListener.onDoubleTap(position);
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    onItemOnclickImageListener.onItemClick(position);
                    return super.onSingleTapUp(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        return convertView;
    }

    private class ViewHolder {
        NetworkImageView imageView;
        ProgressBar progressBar;

        public ViewHolder(View view) {
            imageView = (NetworkImageView) view.findViewById(R.id.selected_imageview);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }
}