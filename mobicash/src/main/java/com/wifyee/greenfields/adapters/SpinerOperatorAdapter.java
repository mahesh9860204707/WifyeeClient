package com.wifyee.greenfields.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wifyee.greenfields.models.response.operators.OperatorDetails;

import java.util.List;

/**
 * Created by amit on 8/16/2017.
 */

public class SpinerOperatorAdapter extends ArrayAdapter<OperatorDetails>
{
    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private  List<OperatorDetails> values;

    public SpinerOperatorAdapter(Context context, int textViewResourceId,
                                 List<OperatorDetails> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
        return values.size();
    }

    public OperatorDetails getItem(int position){
        return values.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).operatorCode);

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).operatorCode);

        return label;
    }
}
