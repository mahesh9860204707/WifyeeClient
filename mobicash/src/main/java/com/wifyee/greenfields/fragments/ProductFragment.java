package com.wifyee.greenfields.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;

/**
 * Created by sumanta on 12/20/16.
 */

public class ProductFragment extends Fragment {

    /**
     * Tag for logging.
     */
    public static final String TAG = ProductFragment.class.getSimpleName();

    private View rootView;

    private GridView gridview;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.poduct_fragment, container, false);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridview = (GridView) getActivity().findViewById(R.id.grid_view);
        gridview.setAdapter(new ImageTextAdapter(getActivity()));
    }

    public class ImageTextAdapter extends BaseAdapter {

        Context context;
        Integer[] mThumbIds = null;
        String[] mTextIds = null;
        LayoutInflater inflater = null;

        public ImageTextAdapter(Context ctx) {
            context = ctx;
            setGridView();
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        private void setGridView() {
            mTextIds = new String[]{getString(R.string.gv_bus_ticket)
//                    , getString(R.string.gv_air_line)
                    , getString(R.string.gv_car_booking)
//                    , getString(R.string.gv_railway_ticket)
                    , getString(R.string.gv_fees_collection)
                    , getString(R.string.gv_movie_ticket)
                    , getString(R.string.gv_tour_packages)
//                    , getString(R.string.gv_hotel_booking)
                    , getString(R.string.gv_mutual_fund_payment)
                    , getString(R.string.gv_insurance_payment)};
            mThumbIds = new Integer[]{R.mipmap.bus_ticket_icon
//                    , R.mipmap.air_line_icon
                    , R.mipmap.cab_icon
//                    , R.mipmap.railway_icon
                    , R.mipmap.fees_icon
                    , R.mipmap.movie_icon
                    , R.mipmap.tour_icon
//                    , R.mipmap.hotel_icon
                    , R.mipmap.mutualfund_icon
                    , R.mipmap.insurance_icon};
        }

        @Override
        public int getCount() {
            return mTextIds.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class Holder {
            TextView imageName;
            ImageView imageView;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.product_image_text_grid_view_item, null);
            holder.imageName = (TextView) rowView.findViewById(R.id.image_text);
            holder.imageView = (ImageView) rowView.findViewById(R.id.image_view);

            holder.imageName.setText(mTextIds[position]);
            holder.imageView.setImageResource(mThumbIds[position]);

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mTextIds[position].equals(getString(R.string.gv_bus_ticket))) {
                        getActivity().startActivity(IntentFactory.createBookBusTicketActivity(getContext()));
                        getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    }
                }
            });

            return rowView;
        }
    }

}
