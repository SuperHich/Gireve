package com.dotit.gireve.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.dotit.gireve.R;
import com.dotit.gireve.entity.ChargingPoolAddress;
import com.dotit.gireve.entity.ChargingPoolItem;
import com.dotit.gireve.entity.EVSEItem;
import com.dotit.gireve.entity.EVSEOperator;
import com.dotit.gireve.entity.EVSEProperty;
import com.dotit.gireve.entity.OpenTime;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Hichem Laroussi @SH on 03/04/2015.
 */
public class GireveManager {

    private static GireveManager mInstance;

    public static GireveManager getInstance(Context context){
        if(mInstance == null)
            mInstance = new GireveManager(context);

        return mInstance;
    }

    private ArrayList<EVSEProperty> evseProperties;
    private ArrayList<EVSEOperator> evseOperators;
    private LinkedList<EVSEItem> foundStations;
    private LinkedList<ChargingPoolItem> foundChargingPools;

    public GireveManager(Context context) {

        // INIT EVSE PARAMETERS
        evseProperties = new ArrayList<>();
        evseProperties.add(new EVSEProperty(3002, EVSEItem.SOAP_EVSEIdType)); //3002 ?
        evseProperties.add(new EVSEProperty(3003, EVSEItem.SOAP_EVSEId));
        evseProperties.add(new EVSEProperty(2002, EVSEItem.SOAP_chargingStationIdType)); //2002 ?
        evseProperties.add(new EVSEProperty(2003, EVSEItem.SOAP_chargingStationId));
        evseProperties.add(new EVSEProperty(1002, EVSEItem.SOAP_chargingPoolIdType)); //1002 ?
        evseProperties.add(new EVSEProperty(1003, EVSEItem.SOAP_chargingPoolId));
        evseProperties.add(new EVSEProperty(1005, EVSEItem.SOAP_chargingPoolBrandName));
        evseProperties.add(new EVSEProperty(1006, EVSEItem.SOAP_chargingPoolName));
        evseProperties.add(new EVSEProperty(1062, EVSEItem.SOAP_entrancelatitude));
        evseProperties.add(new EVSEProperty(1063, EVSEItem.SOAP_entrancelongitude));
        evseProperties.add(new EVSEProperty(2041, EVSEItem.SOAP_chargingStationlatitude));
        evseProperties.add(new EVSEProperty(2042, EVSEItem.SOAP_chargingStationlongitude));
        evseProperties.add(new EVSEProperty(1065, EVSEItem.SOAP_phoneNumber));
        evseProperties.add(new EVSEProperty(4021, EVSEItem.SOAP_connectorType));
        evseProperties.add(new EVSEProperty(3044, EVSEItem.SOAP_power));
        evseProperties.add(new EVSEProperty(3111, EVSEItem.SOAP_speed));
        evseProperties.add(new EVSEProperty(2063, EVSEItem.SOAP_authorisationMode));
        evseProperties.add(new EVSEProperty(2062, EVSEItem.SOAP_authorisationInformation));
        evseProperties.add(new EVSEProperty(2064, EVSEItem.SOAP_paymentMode));
        evseProperties.add(new EVSEProperty(2065, EVSEItem.SOAP_paymentInformation));
        evseProperties.add(new EVSEProperty(2061, EVSEItem.SOAP_accessibility));
        evseProperties.add(new EVSEProperty(1101, EVSEItem.SOAP_isOpen24_7));
        evseProperties.add(new EVSEProperty(2066, EVSEItem.SOAP_bookable));
        evseProperties.add(new EVSEProperty(3046, EVSEItem.SOAP_availabilityStatus));
        evseProperties.add(new EVSEProperty(3047, EVSEItem.SOAP_availabilityStatusUntil));
        evseProperties.add(new EVSEProperty(3041, EVSEItem.SOAP_busyStatus));
        evseProperties.add(new EVSEProperty(3042, EVSEItem.SOAP_busyStatusUntil));
        evseProperties.add(new EVSEProperty(3101, EVSEItem.SOAP_useabilityStatus));
        evseProperties.add(new EVSEProperty(3102, EVSEItem.SOAP_useabilityStatusUntil));
        evseProperties.add(new EVSEProperty(1044, ChargingPoolAddress.SOAP_StreetNumber));
        evseProperties.add(new EVSEProperty(1045, ChargingPoolAddress.SOAP_StreetName));
        evseProperties.add(new EVSEProperty(1042, ChargingPoolAddress.SOAP_PostCode));
        evseProperties.add(new EVSEProperty(1043, ChargingPoolAddress.SOAP_City));
        evseProperties.add(new EVSEProperty(1046, ChargingPoolAddress.SOAP_Province));
        evseProperties.add(new EVSEProperty(1041, ChargingPoolAddress.SOAP_Country));
        evseProperties.add(new EVSEProperty(1103, OpenTime.SOAP_WeekDay)); // ?
        evseProperties.add(new EVSEProperty(1103, OpenTime.SOAP_StartTime)); // ?
        evseProperties.add(new EVSEProperty(1103, OpenTime.SOAP_EndTime)); // ?

        Collections.sort(evseProperties);


        // INIT EVSE OPERATORS
        evseOperators = new ArrayList<>();
        evseOperators.add(new EVSEOperator("=", "="));
        evseOperators.add(new EVSEOperator(">", ">")); // &gt;
        evseOperators.add(new EVSEOperator("<", "<")); // &lt;
        evseOperators.add(new EVSEOperator(">=", ">=")); // &gt;=
        evseOperators.add(new EVSEOperator("<=", "<=")); // &lt;=
        evseOperators.add(new EVSEOperator("!=", "!=")); // &#33;=

    }

    public String getAddress(ChargingPoolAddress address){
        StringBuilder result = new StringBuilder();
        try {
            result.append(avoidNull(address.getStreetNumber()));
            result.append(", " + avoidNull(address.getStreetName()));
            result.append(" " + avoidNull(address.getPostCode()));
            result.append(" " + avoidNull(address.getCity()));
        }catch(Exception e){
            e.printStackTrace();
        }

        return result.toString();
    }

    public String getConnectorsAsString(ArrayList<Integer> connectors){
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < connectors.size() ; i++) {
            if(i < connectors.size() - 1)
                result.append(connectors.get(i)+", ");
            else
                result.append(connectors.get(i)+"");
        }

        return result.toString();
    }


    public ArrayList<EVSEProperty> getEVSEProperties() {
        return evseProperties;
    }

    public void setEVSEProperties(ArrayList<EVSEProperty> evseProperties) {
        this.evseProperties = evseProperties;
    }

    public ArrayList<EVSEOperator> getEVSEOperators() {
        return evseOperators;
    }

    public void setEVSEOperators(ArrayList<EVSEOperator> evseOperators) {
        this.evseOperators = evseOperators;
    }

    public LinkedList<EVSEItem> getFoundStations() {
        return foundStations;
    }

    public void setFoundStations(LinkedList<EVSEItem> foundStations) {
        this.foundStations = foundStations;
    }

    public LinkedList<ChargingPoolItem> getFoundChargingPools() {
        return foundChargingPools;
    }

    public void setFoundChargingPools(LinkedList<ChargingPoolItem> foundChargingPools) {
        this.foundChargingPools = foundChargingPools;
    }

    public String getIPAddress(){
        try {
            Document doc = Jsoup.connect("http://www.checkip.org").get();
            return doc.getElementById("yourip").select("h1").first().select("span").text();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public void getMyIP(final Context context){
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... arg0) {

                return getIPAddress();
            }

            @Override
            protected void onPostExecute(String result) {
                Log.e("", ">>>>> My IP " + result);
                String ip;
                if(result != null) {
                    ip = result;
                }else
                    ip = context.getString(R.string.pref_default_ip_address);

                setStringPreference(context, context.getString(R.string.pref_key_ip_address), ip);
                setStringPreference(context, context.getString(R.string.pref_key_fault_response), context.getString(R.string.pref_default_fault_response));
            }


        }.execute();
    }


    // Set String Preference
    public void setStringPreference(Context context, String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    // Get String Preference
    public String getStringPreference(Context context, String key, String defValue){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, defValue);
    }

    // Get String Preference
    public int getIntPreference(Context context, String key, int defValue){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, defValue);
    }

    public String avoidNull(String value){
        return value!=null?value:"";
    }
}
