package com.dotit.gireve.ihm;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dotit.gireve.R;
import com.dotit.gireve.entity.ChargingPoolItem;
import com.dotit.gireve.entity.EVSEItem;
import com.dotit.gireve.ihm.adapters.StationsAdapter;
import com.dotit.gireve.utils.GireveFonts;
import com.dotit.gireve.utils.GireveManager;

import java.util.ArrayList;

public class StationsFragment extends Fragment {

	private StationsAdapter adapter;
	private ArrayList<ChargingPoolItem> items = new ArrayList<>();

	private ListView listView;
	private TextView txv_emptyList, txv_title;
    private ImageButton btn_back;
	
	private GireveManager mManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

        mManager = GireveManager.getInstance(getActivity());

	}
	
	@Override
	public void onDetach() {
		super.onDetach();

	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.stations_list_layout, container, false);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

		listView = (ListView) rootView.findViewById(android.R.id.list);

        txv_title       = (TextView) rootView.findViewById(R.id.txv_title);
		txv_emptyList 	= (TextView) rootView.findViewById(R.id.txv_emptyList);

        txv_title	    .setTypeface(GireveFonts.getClanProBold());
        txv_emptyList	.setTypeface(GireveFonts.getClanProBold());

        btn_back        = (ImageButton) rootView.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				((MapsActivity) getActivity()).gotoFragment(MapsActivity.STATION_DETAILS_FRAGMENT, items.get(position).getEvseItems(), true);
				
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

        items = new ArrayList<>(mManager.getFoundChargingPools());
		adapter = new StationsAdapter(getActivity(), R.layout.station_item, items);
		listView.setAdapter(adapter);

        toggleEmptyMessage();

        txv_title.setText(items.size() + " " + getString(R.string.found_stations));

	}
	
	private void toggleEmptyMessage() {
		if(items.size() == 0)
			txv_emptyList.setVisibility(View.VISIBLE);
		else
			txv_emptyList.setVisibility(View.GONE);
	}

}
