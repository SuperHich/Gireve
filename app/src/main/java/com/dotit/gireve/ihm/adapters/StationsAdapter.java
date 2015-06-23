package com.dotit.gireve.ihm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dotit.gireve.R;
import com.dotit.gireve.entity.ChargingPoolAddress;
import com.dotit.gireve.entity.ChargingPoolItem;
import com.dotit.gireve.entity.EVSEItem;
import com.dotit.gireve.utils.GireveFonts;
import com.dotit.gireve.utils.GireveManager;

import java.util.List;

public class StationsAdapter extends ArrayAdapter<ChargingPoolItem> {

    private static final String TAG = StationsAdapter.class.getSimpleName();
    GireveManager mManager;
    Context mContext;
    int layoutId;
    LayoutInflater inflater;
    List<ChargingPoolItem> items;

    public StationsAdapter(Context context, int resource,
                           List<ChargingPoolItem> items) {
        super(context, resource, items);

        this.mContext = context;
        this.layoutId = resource;
        this.items = items;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mManager = GireveManager.getInstance(context);
    }

    public List<ChargingPoolItem> getItems() {
        return items;
    }

    public void remove(int position) {
        this.items.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.station_item, null);
            holder = new ViewHolder();

            holder.txv_label_1          = (TextView)    convertView.findViewById(R.id.txv_label_1);
            holder.txv_label_2 		    = (TextView)    convertView.findViewById(R.id.txv_label_2);
            holder.txv_details 	        = (TextView)    convertView.findViewById(R.id.txv_details);

            holder.txv_label_1.setTypeface(GireveFonts.getClanProBold());
            holder.txv_label_2.setTypeface(GireveFonts.getClanProBook());
            holder.txv_details.setTypeface(GireveFonts.getClanProBook());

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ChargingPoolItem item = items.get(position);
        holder.txv_label_1.setText(item.getName());
        holder.txv_label_2.setText(item.getType() +" - NbEVSE="+ item.getEvseItems().size());
        holder.txv_details.setText(item.getAddress());

        return convertView;
    }

    class ViewHolder {
        TextView txv_label_1;
        TextView txv_label_2;
        TextView txv_details;
    }

}
