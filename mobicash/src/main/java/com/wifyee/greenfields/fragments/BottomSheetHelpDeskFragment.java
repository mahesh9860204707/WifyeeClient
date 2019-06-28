package com.wifyee.greenfields.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;

import java.util.List;

public class BottomSheetHelpDeskFragment extends BottomSheetDialogFragment {
    public BottomSheetHelpDeskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_fragment, container, false);

        TextView txt = view.findViewById(R.id.txt);
        TextView txtLandlineNumber = view.findViewById(R.id.tv_land_line_number);
        TextView txtWork = view.findViewById(R.id.txt_work);
        TextView mobile_no = view.findViewById(R.id.mobile_no);
        TextView txt_mobile_no = view.findViewById(R.id.txt_mobile_no);
        TextView txt_email = view.findViewById(R.id.txt_email);

        txt.setTypeface(Fonts.getSemiBold(getContext()));
        txtLandlineNumber.setTypeface(Fonts.getRegular(getContext()));
        txtWork.setTypeface(Fonts.getRegular(getContext()));
        mobile_no.setTypeface(Fonts.getRegular(getContext()));
        txt_mobile_no.setTypeface(Fonts.getRegular(getContext()));
        txt_email.setTypeface(Fonts.getRegular(getContext()));

        return view;

    }
}
