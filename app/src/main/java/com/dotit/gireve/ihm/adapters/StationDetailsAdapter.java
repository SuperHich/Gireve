package com.dotit.gireve.ihm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dotit.gireve.R;
import com.dotit.gireve.entity.EVSEItem;
import com.dotit.gireve.entity.StationDetail;
import com.dotit.gireve.utils.GireveFonts;
import com.dotit.gireve.utils.GireveManager;

import java.util.List;

public class StationDetailsAdapter extends ArrayAdapter<StationDetail> {

    private static final String TAG = StationDetailsAdapter.class.getSimpleName();
    GireveManager mManager;
    Context mContext;
    int layoutId;
    LayoutInflater inflater;
    List<StationDetail> items;

    public StationDetailsAdapter(Context context, int resource,
                                 List<StationDetail> items) {
        super(context, resource, items);

        this.mContext = context;
        this.layoutId = resource;
        this.items = items;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mManager = GireveManager.getInstance(context);
    }

    public List<StationDetail> getItems() {
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

            convertView = inflater.inflate(R.layout.detail_item, null);
            holder = new ViewHolder();

            holder.txv_name         = (TextView)    convertView.findViewById(R.id.txv_name);
            holder.txv_value 		= (TextView)    convertView.findViewById(R.id.txv_value);
            holder.img_tick 		= (ImageView)   convertView.findViewById(R.id.img_tick);

            holder.txv_name.setTypeface(GireveFonts.getClanProBold());
            holder.txv_value.setTypeface(GireveFonts.getClanProBook());

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final StationDetail item = items.get(position);
        holder.txv_name.setText(item.getName());

        switch (item.getType())
        {
            case TEXT:
                holder.txv_value.setVisibility(View.VISIBLE);
                holder.img_tick.setVisibility(View.GONE);

                holder.txv_value.setText(item.getValue());
                break;
            case BOOLEAN:
                holder.txv_value.setVisibility(View.GONE);
                holder.img_tick.setVisibility(View.VISIBLE);

                holder.img_tick.setImageResource(item.isBoolValue()?R.drawable.tick_on:R.drawable.tick_off);
                break;
            case LIST:
                holder.txv_value.setVisibility(View.VISIBLE);
                holder.img_tick.setVisibility(View.GONE);

                holder.txv_value.setText(item.getValue());
            default:
                break;
        }

        return convertView;
    }

    class ViewHolder {
        TextView txv_name;
        TextView txv_value;
        ImageView img_tick;
    }

}
