package com.dotit.gireve.ihm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dotit.gireve.R;
import com.dotit.gireve.utils.GireveFonts;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Hichem Laroussi @SH on 08/04/2015.
 */
public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    Context mContext;

    public MyInfoWindowAdapter(Context context){
        mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        LayoutInflater li = LayoutInflater.from(mContext);
        View root = li.inflate(R.layout.info_window_layout, null);

        TextView txv_title = (TextView) root.findViewById(R.id.txv_title);
        TextView txv_details = (TextView) root.findViewById(R.id.txv_details);

        txv_title.setTypeface(GireveFonts.getClanProBold());
        txv_details.setTypeface(GireveFonts.getClanProBook());

        if(marker.getTitle() == null)
            return null;

        String[] labels = marker.getTitle().split("@");

        txv_title.setText(labels[0]);
        txv_details.setText(labels[1]);

        return root;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
