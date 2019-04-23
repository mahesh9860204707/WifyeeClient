package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.models.response.BankListResponseModel;
import com.wifyee.greenfields.models.response.CityResponse;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 8/16/2017.
 */

public class SpinerCityAdapter extends ArrayAdapter<CityResponse>
{
    Context context;
    int resource, textViewResourceId;
    List<CityResponse> items, tempItems, suggestions;

    public SpinerCityAdapter(Context context, int resource, int textViewResourceId, List<CityResponse> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<CityResponse>(items); // this makes the difference.
        suggestions = new ArrayList<CityResponse>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.city_list, parent, false);
        }
        CityResponse people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people.getName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((CityResponse) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CityResponse people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<CityResponse> filterList = (ArrayList<CityResponse>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CityResponse people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
