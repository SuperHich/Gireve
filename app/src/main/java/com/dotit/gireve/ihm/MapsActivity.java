package com.dotit.gireve.ihm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.dotit.gireve.R;
import com.dotit.gireve.com.Api;
import com.dotit.gireve.com.AuthenticationParameters;
import com.dotit.gireve.entity.ChargingPoolItem;
import com.dotit.gireve.entity.EVSECriteria;
import com.dotit.gireve.entity.EVSEItem;
import com.dotit.gireve.ihm.adapters.CriteriaAdapter;
import com.dotit.gireve.ihm.adapters.MyInfoWindowAdapter;
import com.dotit.gireve.ihm.views.ICriteriaItemListener;
import com.dotit.gireve.ihm.views.NumberPickerPreference;
import com.dotit.gireve.utils.GireveManager;
import com.dotit.gireve.utils.IOUtil;
import com.dotit.gireve.utils.MapUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//@ContentView(R.layout.activity_maps)
public class MapsActivity extends FragmentActivity {

    public static final String STATION_LIST_FRAGMENT = "station_list";
    public static final String STATION_DETAILS_FRAGMENT = "station_details";

    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final int    DEFAULT_ZOOM        = 5;
    private static final double DEFAULT_LATITUDE    = 47.055588;
    private static final double DEFAULT_LONGITUDE   = 2.376331;
    private static final int    MAX_CRITERIA        = 5;

    //    @InjectView(R.id.btn_search)
    ImageButton btn_search, btn_add, btn_list, btn_settings;

//    @InjectView(R.id.list_view)
    ListView listView;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private CriteriaAdapter criteriaAdapter;
    private int criteriaId = 0;
    private int criteriaCounter = 0;

    private LatLng lastSelectedPosition;
    private Api mApi;
    private GireveManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mManager = GireveManager.getInstance(this);
        String defaultIP = getString(R.string.pref_default_ip_address);
        if(mManager.getStringPreference(this, getString(R.string.pref_key_ip_address), defaultIP).equals(defaultIP))
            mManager.getMyIP(this);

        btn_search      = (ImageButton) findViewById(R.id.btn_search);
        btn_add         = (ImageButton) findViewById(R.id.btn_add);
        btn_list        = (ImageButton) findViewById(R.id.btn_list);
        btn_settings    = (ImageButton) findViewById(R.id.btn_settings);
        listView        = (ListView) findViewById(R.id.list_view);

        listView.setItemsCanFocus(true);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(criteriaCounter < MAX_CRITERIA) {
                    clearFocusOnList();

                    criteriaCounter++;
                    criteriaAdapter.add(new EVSECriteria(criteriaId++));
                }else
                    Toast.makeText(MapsActivity.this, getString(R.string.max_criteria), Toast.LENGTH_SHORT).show();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEVSE();
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFragment(STATION_LIST_FRAGMENT);
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, SettingsActivity.class));
            }
        });

//        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        LatLng coordinate = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        CameraUpdate defaultLocation = CameraUpdateFactory.newLatLngZoom(coordinate, DEFAULT_ZOOM);
        mMap.animateCamera(defaultLocation);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();

                lastSelectedPosition = latLng;
//                Log.i(TAG, ">>> SELECTED POSITION = " + lastSelectedPosition);

                drawMarkerWithCircle(latLng);
                startSearchMode();
            }
        });

        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ChargingPoolItem clickedStation = getSelectedStation(marker);
                if(clickedStation != null)
                    gotoFragment(STATION_DETAILS_FRAGMENT, clickedStation.getEvseItems(), false);
            }
        });
    }

    private void drawMarkerWithCircle(LatLng position){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(MapsActivity.this);

        int radius = Integer.valueOf(sharedPreferences.getString(getString(R.string.pref_key_radius), "5"));

        double radiusInMeters = radius * 1000.0;
        int strokeColor = getResources().getColor(R.color.blue); //blue outline
        int shadeColor = getResources().getColor(R.color.blue_very_light); //opaque blue fill

//        CameraUpdate defaultLocation = CameraUpdateFactory.newLatLngZoom(position, getZoomByRadius(radius));
//        mMap.animateCamera(defaultLocation);

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(4);
        mMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMap.addMarker(markerOptions);

        LatLngBounds bounds = MapUtils.boundsWithCenterAndLatLngDistance(position, radius*1000, radius*2000);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
    }

    private void startSearchMode(){

        criteriaId = 0;
        criteriaCounter = 0;

        criteriaAdapter = new CriteriaAdapter(this, R.layout.criteria_item, new ArrayList<EVSECriteria>(), new ICriteriaItemListener() {

            @Override
            public void onMinusClicked(int id) {
                for (int i = 0; i < criteriaAdapter.getCount(); i++) {
                    if(criteriaAdapter.getItem(i).getId() == id) {
                        clearFocusOnList();
                        criteriaCounter--;
                        criteriaAdapter.remove(i);
                        break;
                    }
                }
            }
        });
        criteriaAdapter.add(new EVSECriteria(Integer.MAX_VALUE));
        criteriaAdapter.setNotifyOnChange(true);

        listView.setAdapter(criteriaAdapter);

        btn_search.setVisibility(View.VISIBLE);
        btn_add.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);

        btn_list.setVisibility(View.GONE);

    }

    private void searchEVSE() {

        if(criteriaAdapter.getSelectedEditText() != null)
            IOUtil.hideKeyBoardFromWindow(this, criteriaAdapter.getSelectedEditText());

        mMap.clear();
        drawMarkerWithCircle(lastSelectedPosition);

        new AsyncTask<Void, Void, LinkedList<ChargingPoolItem>>() {

            ProgressDialog progress;

            @Override
            protected void onPreExecute() {
                clearFocusOnList();

                progress = new ProgressDialog(MapsActivity.this);
                progress.setMessage(getString(R.string.searching));
                progress.setCancelable(false);
                progress.show();
            }

            @Override
            protected LinkedList<ChargingPoolItem> doInBackground(Void... params) {
                if(IOUtil.isOnline(MapsActivity.this)) {
                    try{
                        mApi = new Api(MapsActivity.this, getString(R.string.client_cert_file_name), getString(R.string.client_cert_password));

                        int radius = Integer.valueOf(mManager.getStringPreference(MapsActivity.this, getString(R.string.pref_key_radius), getString(R.string.pref_default_radius)));
                        int max_count = Integer.valueOf(mManager.getStringPreference(MapsActivity.this, getString(R.string.pref_key_max_count), "100"));
                        String language = mManager.getStringPreference(MapsActivity.this, getString(R.string.pref_key_language), getString(R.string.pref_default_language));
                        String search_algo = mManager.getStringPreference(MapsActivity.this, getString(R.string.pref_key_search_algo), getString(R.string.pref_default_search_algo));

                        LinkedList<EVSEItem> result = mApi.searchEVSEItems(MapsActivity.this,
                                lastSelectedPosition,
                                radius,
                                max_count,
                                language,
                                search_algo,
                                filterSimilarCriteria(criteriaAdapter.getItems()));
                        return getChargingPoolItems(result);
                    } catch (Exception ex) {
                        Log.e(TAG, "failed to create timeApi", ex);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(LinkedList<ChargingPoolItem> cpItems) {
                progress.dismiss();

                if(cpItems != null){
//                        Log.i(TAG, ">>> evseItems Size= " + evseItems.size());
//                        Log.i(TAG, ">>> evseItems= " + evseItems);
                    displayChargingPools(cpItems);
                    mManager.setFoundChargingPools(cpItems);
                    btn_list.setVisibility(View.VISIBLE);

                    String msg;
                    if(cpItems.isEmpty()) {
                        msg = getString(R.string.no_stations_found);
//                            btn_list.setVisibility(View.GONE);
                    }
                    else {
                        msg = cpItems.size() + " " + getString(R.string.found_stations);
//                            btn_list.setVisibility(View.VISIBLE);
                    }

                    Toast.makeText(MapsActivity.this, msg, Toast.LENGTH_LONG).show();

                }else{
                    if(IOUtil.isOnline(MapsActivity.this))
                        IOUtil.showInfoPopup(MapsActivity.this, getString(R.string.warning), getString(R.string.error_data));
                    else
                        IOUtil.showInfoPopup(MapsActivity.this, getString(R.string.warning), getString(R.string.no_internet));

                }

            }
        }.execute();


    }

    private void displayChargingPools(LinkedList<ChargingPoolItem> cpItems){
        for(ChargingPoolItem item : cpItems){
            LatLng position = new LatLng(item.getLatitude(), item.getLongitude());
//            LatLng position = new LatLng(item.getEntrancelatitude(), item.getEntrancelongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .title(item.getType() +" - "+ item.getEvseItems().size() + "@" + item.getAddress())
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon));
            mMap.addMarker(markerOptions);
        }
    }

    private LinkedList<EVSEItem> filterSimilarStations(LinkedList<EVSEItem> evseItems){
        if(evseItems != null) {
            HashMap<String, EVSEItem> hashFilter = new HashMap<>();
            for (EVSEItem item : evseItems) {
                String id = item.getChargingStationlatitude() + "" + item.getChargingStationlongitude();
//                String id = item.getEntrancelatitude() + "" + item.getEntrancelongitude();

                EVSEItem old = hashFilter.get(id);
                int nbEVSE = 0;
                if(old != null)
                    nbEVSE = old.getNbEVSE();

                item.setNbEVSE(nbEVSE+1);

                hashFilter.put(id, item);
            }
            return new LinkedList<>(hashFilter.values());
        }

        return evseItems;
    }

    private LinkedList<ChargingPoolItem> getChargingPoolItems(LinkedList<EVSEItem> evseItems){
        if(evseItems != null) {
            HashMap<String, ChargingPoolItem> hashFilter = new HashMap<>();
            for (EVSEItem item : evseItems) {
                String id = item.getChargingStationlatitude() + "" + item.getChargingStationlongitude();
//                String id = item.getEntrancelatitude() + "" + item.getEntrancelongitude();

                ChargingPoolItem old = hashFilter.get(id);
                if(old == null)
                {
                    old = new ChargingPoolItem(id,
                            item.getChargingPoolBrandName() +" - "+item.getChargingPoolName(),
                            item.getEVSEIdType(),
                            mManager.getAddress(item.getChargingPoolAddress()),
                            item.getChargingStationlatitude(),
                            item.getChargingStationlongitude());
                }

                old.getEvseItems().add(item);
                hashFilter.put(id, old);
            }
            return new LinkedList<>(hashFilter.values());
        }

        return null;
    }

    private HashMap<Integer, ArrayList<EVSECriteria>> filterSimilarCriteria(List<EVSECriteria> criteriaList){
        HashMap<Integer, ArrayList<EVSECriteria>> result = new HashMap<>();

        for(EVSECriteria item : criteriaList){
            if(item.getId() != Integer.MAX_VALUE) {
                int key = item.getProperty().getId();
                ArrayList<EVSECriteria> list = result.get(key);
                if (list == null) {
                    list = new ArrayList<>();
                }

                list.add(item);
                result.put(key, list);
            }
        }

//        Log.i(TAG, ">>> filterSimilarCriteria: " + result.size() );

        return result;
    }


    private void clearFocusOnList(){
        criteriaAdapter.setJustClearFocus(true);
        criteriaAdapter.notifyDataSetChanged();
        criteriaAdapter.setJustClearFocus(false);
    }

    public void gotoFragment(String fragTag, Object... item){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);

        Fragment fragment = null;
        if(fragTag.equals(STATION_LIST_FRAGMENT))
            fragment = new StationsFragment();
        else if(fragTag.equals(STATION_DETAILS_FRAGMENT))
            fragment = EVSEPagerFragment.newInstance((LinkedList<EVSEItem>)item[0], (Boolean)item[1]);

        if(fragment != null) {
            transaction.replace(R.id.fragment_view, fragment, fragTag);
            transaction.addToBackStack(fragTag);

            transaction.commit();
        }
    }

    private ChargingPoolItem getSelectedStation(Marker marker){
        for(ChargingPoolItem item : mManager.getFoundChargingPools()){
            LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
//            LatLng latLng = new LatLng(item.getEntrancelatitude(), item.getEntrancelongitude());
            if(latLng.equals(marker.getPosition()))
                return item;
        }

        return null;
    }

}
