package com.dotit.gireve.ihm;

import android.app.Activity;
import android.graphics.Color;
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
import com.dotit.gireve.entity.ChargingPoolAddress;
import com.dotit.gireve.entity.EVSEItem;
import com.dotit.gireve.entity.EVSEProperty;
import com.dotit.gireve.entity.EVSEStatusEnum;
import com.dotit.gireve.entity.EVSETypeEnum;
import com.dotit.gireve.entity.OpenTime;
import com.dotit.gireve.entity.StationDetail;
import com.dotit.gireve.ihm.adapters.StationDetailsAdapter;
import com.dotit.gireve.ihm.adapters.StationsAdapter;
import com.dotit.gireve.utils.GireveFonts;
import com.dotit.gireve.utils.GireveManager;
import com.dotit.gireve.utils.IOUtil;

import java.util.ArrayList;

public class StationDetailsFragment extends Fragment {

    private static final String EVSE_ITEM_TAG = "evse_item";
	private StationDetailsAdapter adapter;
	private ArrayList<StationDetail> items = new ArrayList<>();

	private ListView listView;
	private TextView txv_emptyList;

	private GireveManager mManager;
    private EVSEItem item;


    public static StationDetailsFragment newInstance(EVSEItem item){

        StationDetailsFragment fragmentInstance = new StationDetailsFragment();

        Bundle argument = new Bundle();
        argument.putSerializable(EVSE_ITEM_TAG, item);
        fragmentInstance.setArguments(argument);

        return fragmentInstance;
    }
	
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
		View rootView = inflater.inflate(R.layout.stations_details_layout, container, false);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

		listView = (ListView) rootView.findViewById(android.R.id.list);
		txv_emptyList 	= (TextView) rootView.findViewById(R.id.txv_emptyList);

        txv_emptyList	.setTypeface(GireveFonts.getClanProBold());

		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

        item = (EVSEItem)getArguments().get(EVSE_ITEM_TAG);
        items = getStationDetailsList(item);
		adapter = new StationDetailsAdapter(getActivity(), R.layout.detail_item, items);
		listView.setAdapter(adapter);
		listView.setCacheColorHint(Color.TRANSPARENT);

	}


    private ArrayList<StationDetail> getStationDetailsList(EVSEItem item) {
        ArrayList<StationDetail> result = new ArrayList<>();

        result.add(new StationDetail(EVSEItem.SOAP_EVSEId, avoidNull(item.getEVSEIdType())+"/"+avoidNull(item.getEVSEId()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Charging Pool", avoidNull(item.getChargingPoolBrandName()) +" - "+ avoidNull(item.getChargingPoolName()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Charging Pool-Id", avoidNull(item.getChargingPoolIdType())+"/"+avoidNull(item.getChargingPoolId()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Address", mManager.getAddress(item.getChargingPoolAddress()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("GeoCoord", IOUtil.formatDouble(item.getChargingStationlatitude()) +"/"+IOUtil.formatDouble(item.getChargingStationlongitude()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Entrance GeoCoord", IOUtil.formatDouble(item.getEntrancelatitude()) +"/"+IOUtil.formatDouble(item.getEntrancelongitude()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Charging Station", avoidNull(item.getChargingStationIdType())+"/"+avoidNull(item.getChargingStationId()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Connectors", mManager.getConnectorsAsString(item.getConnectorTypes()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Accessibility", String.valueOf(item.getAccessibility()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Open Hours", item.isOpen24_7()?"24/7":"Specific", EVSETypeEnum.TEXT));
        result.add(new StationDetail("Availability", EVSEStatusEnum.Status.valueToStatus(item.getAvailabilityStatus()) +" - "+ avoidNull(item.getAvailabilityStatusUntil()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Busy", EVSEStatusEnum.Status.valueToStatus(item.getBusyStatus()) +" - "+ avoidNull(item.getBusyStatusUntil()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Useability", EVSEStatusEnum.Status.valueToStatus(item.getUseabilityStatus()) +" - "+ avoidNull(item.getUseabilityStatusUntil()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Phone Number", avoidNull(item.getPhoneNumber()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Power", IOUtil.formatDoubleWithSingleDecimal(item.getPower()) +"KW - "+ avoidNull(item.getSpeed()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Authorisation Infos", avoidNull(item.getAuthorisationInformation()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Authorisation Modes", String.valueOf(item.getAuthorisationMode()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Payment Infos", avoidNull(item.getPaymentInformation()), EVSETypeEnum.TEXT));
        result.add(new StationDetail("Payment Modes", String.valueOf(item.getPaymentMode()), EVSETypeEnum.TEXT));
//        result.add(new StationDetail(EVSEItem.SOAP_bookable, item.isBookable(), EVSETypeEnum.BOOLEAN));

//        evseProperties.add(new EVSEProperty(1103, OpenTime.SOAP_WeekDay)); // ?
//        evseProperties.add(new EVSEProperty(1103, OpenTime.SOAP_StartTime)); // ?
//        evseProperties.add(new EVSEProperty(1103, OpenTime.SOAP_EndTime)); // ?

        return result;
    }

    private String avoidNull(String value){
        return value!=null?value:"";
    }
}
