package com.dotit.gireve.com;

import android.app.LauncherActivity;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Property;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import com.dotit.gireve.R;
import com.dotit.gireve.entity.ChargingPoolAddress;
import com.dotit.gireve.entity.EVSECriteria;
import com.dotit.gireve.entity.EVSEItem;
import com.dotit.gireve.entity.OpenTime;
import com.dotit.gireve.utils.GireveManager;
import com.dotit.gireve.utils.IOUtil;
import com.google.android.gms.maps.model.LatLng;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.SoapFault12;
import org.ksoap2.serialization.AttributeInfo;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;


/**
 * client-side interface to the back-end application.
 */
public class Api {

    private static final String TAG = Api.class.getSimpleName();

    // BASE URL
    public static final String BASE_URL = "https://api-pp-iop.gireve.com/services/csfV1";

    // REQUESTS
    private static final String SearchEVSERequest = "eMIP_ToIOP_SearchEVSERequest";

    private SSLContext sslContext;
    private int lastResponseCode;
    private MySSLSocketFactory mSSLSocketFactory;

    public Api(Context context, String clientCertFileName, String clientCertificatePassword) throws Exception {

        File clientCertFile = getClientCertFile(context, clientCertFileName);
        mSSLSocketFactory = SSLContextFactory.getInstance().makeSSLSocketFactory(clientCertFile,  clientCertificatePassword);
        CookieHandler.setDefault(new CookieManager());
    }

    public Api(AuthenticationParameters authParams) throws Exception {

        File clientCertFile = authParams.getClientCertificate();

//        sslContext = SSLContextFactory.getInstance().makeContext(clientCertFile, authParams.getClientCertificatePassword(), authParams.getCaCertificate());
        mSSLSocketFactory = SSLContextFactory.getInstance().makeSSLSocketFactory(clientCertFile, authParams.getClientCertificatePassword());
        CookieHandler.setDefault(new CookieManager());
    }

    private File getClientCertFile(Context context, String fileName) {
        File externalStorageDir = Environment.getExternalStorageDirectory();
        File file = new File(externalStorageDir, fileName);
        if(!file.exists())
            IOUtil.CopyAssetsFileToSDCard(context, fileName);

        return file;
    }

    public String doGet(String url)  throws Exception {
        String result = null;

        HttpURLConnection urlConnection = null;
        try {
            URL requestedUrl = new URL(url);
            urlConnection = (HttpURLConnection) requestedUrl.openConnection();
            if(urlConnection instanceof HttpsURLConnection) {
                ((HttpsURLConnection)urlConnection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(1500);
            urlConnection.setReadTimeout(1500);

            lastResponseCode = urlConnection.getResponseCode();
            result = IOUtil.readFully(urlConnection.getInputStream());

        } catch(Exception ex) {
            result = ex.toString();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    public String doPost(String url)  throws Exception {
        String result = null;

        HttpURLConnection urlConnection = null;
        try {
            URL requestedUrl = new URL(url);
            urlConnection = (HttpURLConnection) requestedUrl.openConnection();
            if(urlConnection instanceof HttpsURLConnection) {
                ((HttpsURLConnection)urlConnection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(1500);
            urlConnection.setReadTimeout(1500);

            lastResponseCode = urlConnection.getResponseCode();
            result = IOUtil.readFully(urlConnection.getInputStream());

        } catch(Exception ex) {
            result = ex.toString();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    public NtlmTransport httpTransport;
    //Generic soap request
    public SoapObject sendGenericSoapRequest(String WS_NAMESPACE, SoapObject request, String url, String WS_METHOD_NAME,
                                             List<HeaderProperty> headerList){

        // 1. Create SOAP Envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        // 2. Set the request parameters
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.setAddAdornments(false);
        envelope.implicitTypes = true;
        //Log.i(TAG, "site login and pwd: "+login+" and "+pwd);

        try {
            // 3. Create an HTTP Transport object to send the web service request
            httpTransport = new NtlmTransport(url, 443, "", mSSLSocketFactory);
            httpTransport.setCredentials(url, "", "", "", "");
//          httpTransport.setIgnoreBack(ignoreBack);
            httpTransport.debug = true; // allows capture of raw request/response in Logcat
            String soap_action = WS_NAMESPACE + WS_METHOD_NAME;
            //new MarshalBase64().register(envelope);
            // 4. Make the web service invocation
            httpTransport.call(soap_action, envelope, headerList);

        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        SoapObject soapObject = null;
        // 5. Process the web service response
        if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
            soapObject = (SoapObject) envelope.bodyIn;
            // ... do whatever you want with this object now
            Log.e("", "SoapObject ============> ");
        } else if (envelope.bodyIn instanceof SoapFault12) { // SoapFault = FAILURE
            SoapFault12 soapFault = (SoapFault12) envelope.bodyIn;
            Log.e("", "SoapFault ============> " + soapFault.faultstring);
        }else if(envelope.bodyIn instanceof SoapPrimitive)
            Log.e("", "Primitive ============>");

//        ignoreBack = false;

        return soapObject;
    }

    //GetListItems for search
    public LinkedList<EVSEItem> searchEVSEItems(Context ctx, LatLng position, int radius, int maxCount, String language, String search_algo, HashMap<Integer, ArrayList<EVSECriteria>> criteriaMap){

//        Log.i(TAG, "request-url: "+url);
        final String METHOD_NAME = SearchEVSERequest;
        SoapObject request = new SoapObject("" , METHOD_NAME);

        String newID = "DEMO-" + IOUtil.todayAsString();

        request.addProperty("transactionId", newID);
        request.addProperty("partnerIdType", "eMI3");
        request.addProperty("partnerId", "G_Apps");
        request.addProperty("operatorIdType", "eMI3");
        request.addProperty("operatorId", "FR*107");
        request.addProperty("latitude", IOUtil.formatDouble(position.latitude));
        request.addProperty("longitude", IOUtil.formatDouble(position.longitude));
        request.addProperty("radius", radius);
        request.addProperty("searchAlgo", search_algo);
        request.addProperty("maxCount", maxCount);
        request.addProperty("language", language);

        for(int key : criteriaMap.keySet()){
            SoapObject andCombinedCriteria = new SoapObject("", "andCombinedCriteria");
            andCombinedCriteria.addProperty("attributeId", key);

            ArrayList<EVSECriteria> list = criteriaMap.get(key);

            for (EVSECriteria criteria : list) {
                SoapObject attributeCondition = new SoapObject("", "attributeCondition");
                attributeCondition.addProperty("conditionOperator", criteria.getOperator().getValue());
                attributeCondition.addProperty("attributeValue", criteria.getValue());

                andCombinedCriteria.addProperty("attributeCondition", attributeCondition);
            }

            request.addProperty("andCombinedCriteria", andCombinedCriteria);
        }

//        ignoreBack = true;
        SoapObject result = sendGenericSoapRequest("", request, BASE_URL, METHOD_NAME, null);
        if (result != null) { // SoapObject = SUCCESS
            return parseListItems(ctx, result);
        }
        //Log.i(TAG, "list items size after request: "+list_items.size());
        return null;
    }

    public static LinkedList<EVSEItem> parseListItems(final Context ctx, SoapObject soapObject){

        Log.i(TAG, ">>> soapObject " + soapObject);

        if(soapObject.hasProperty("faultstring"))
        {
            String faultCode = soapObject.getPropertyAsString("faultcode");
            String faultMessage = soapObject.getPropertyAsString("faultstring");
            final String msg = faultCode + " : " + faultMessage;

            GireveManager.getInstance(ctx).setStringPreference(ctx, ctx.getString(R.string.pref_key_fault_response), msg);

            ((FragmentActivity) ctx).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    IOUtil.showInfoPopup(ctx, ctx.getString(R.string.warning), msg);
                }
            });
            return new LinkedList<>();
        }
        else
            GireveManager.getInstance(ctx).setStringPreference(ctx,
                    ctx.getString(R.string.pref_key_fault_response),
                    ctx.getString(R.string.pref_default_fault_response));

        LinkedList<EVSEItem> listItems = new LinkedList<>();
//        Log.i(TAG, ">>> eMIP_ToIOP_SearchEVSEResponse " + soapObject.getPropertyCount());

        for(int i = 0; i < soapObject.getPropertyCount(); i++) {
            if(soapObject.getProperty(i) instanceof SoapObject){
                SoapObject so = (SoapObject) soapObject.getProperty(i);
//                Log.i(TAG, ">>> EVSEDescrip " + so.getPropertyCount());

                EVSEItem item = new EVSEItem();

                for (int j = 0; j < so.getPropertyCount(); j++) {
                    PropertyInfo pi = new PropertyInfo();
                    so.getPropertyInfo(j, pi);

//                    Log.i(TAG, ">>> "+pi.getName()+" ... " + pi.getValue());
                    if(!String.valueOf(pi.getValue()).equals("anyType{}")) {

                        if (pi.getName().equals(EVSEItem.SOAP_EVSEIdType))
                            item.setEVSEIdType(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_EVSEId))
                            item.setEVSEId(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_chargingStationIdType))
                            item.setChargingStationIdType(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_chargingStationId))
                            item.setChargingStationId(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_chargingPoolIdType))
                            item.setChargingPoolIdType(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_chargingPoolId))
                            item.setChargingPoolId(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_chargingPoolBrandName))
                            item.setChargingPoolBrandName(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_chargingPoolName))
                            item.setChargingPoolName(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_chargingPoolAddress))
                            item.setChargingPoolAddress(getChargingPoolAddress((SoapObject) pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_entrancelatitude))
                            item.setEntrancelatitude(stringToDouble(String.valueOf(pi.getValue())));

                        else if (pi.getName().equals(EVSEItem.SOAP_entrancelongitude))
                            item.setEntrancelongitude(stringToDouble(String.valueOf(pi.getValue())));

                        else if (pi.getName().equals(EVSEItem.SOAP_chargingStationlatitude))
                            item.setChargingStationlatitude(stringToDouble(String.valueOf(pi.getValue())));

                        else if (pi.getName().equals(EVSEItem.SOAP_chargingStationlongitude))
                            item.setChargingStationlongitude(stringToDouble(String.valueOf(pi.getValue())));

                        else if (pi.getName().equals(EVSEItem.SOAP_phoneNumber))
                            item.setPhoneNumber(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_connectorType)) {
                            Integer cInt = stringToInteger(String.valueOf(pi.getValue()));
                            if (cInt != null)
                                item.getConnectorTypes().add(cInt);
                        } else if (pi.getName().equals(EVSEItem.SOAP_power))
                            item.setPower(stringToDouble(String.valueOf(pi.getValue())));

                        else if (pi.getName().equals(EVSEItem.SOAP_speed))
                            item.setSpeed(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_authorisationInformation))
                            item.setAuthorisationInformation(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_paymentInformation))
                            item.setPaymentInformation(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_authorisationMode))
                            item.setAuthorisationMode(stringToInteger(String.valueOf(pi.getValue())));

                        else if (pi.getName().equals(EVSEItem.SOAP_paymentMode))
                            item.setPaymentMode(stringToInteger(String.valueOf(pi.getValue())));

                        else if (pi.getName().equals(EVSEItem.SOAP_accessibility))
                            item.setAccessibility(stringToInteger(String.valueOf(pi.getValue())));

                        else if (pi.getName().equals(EVSEItem.SOAP_isOpen24_7))
                            item.setOpen24_7(stringToBoolean(String.valueOf(pi.getValue())));

                            //TODO: TO check in real case
//                    else if(pi.getName().equals(EVSEItem.SOAP_openTimesListList))
//                        item.getOpenTimesListList().add(getOpenTime((SoapObject)pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_bookable))
                            item.setBookable(stringToBoolean(String.valueOf(pi.getValue())));

                        else if (pi.getName().equals(EVSEItem.SOAP_availabilityStatus))
                            item.setAvailabilityStatus(stringToInteger(String.valueOf(pi.getValue())));

                        else if (pi.getName().equals(EVSEItem.SOAP_availabilityStatusUntil))
                            item.setAvailabilityStatusUntil(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_busyStatus))
                            item.setBusyStatus(stringToInteger(String.valueOf(pi.getValue())));

                        else if (pi.getName().equals(EVSEItem.SOAP_busyStatusUntil))
                            item.setBusyStatusUntil(String.valueOf(pi.getValue()));

                        else if (pi.getName().equals(EVSEItem.SOAP_useabilityStatus))
                            item.setUseabilityStatus(stringToInteger(String.valueOf(pi.getValue())));

                        else if (pi.getName().equals(EVSEItem.SOAP_useabilityStatusUntil))
                            item.setUseabilityStatusUntil(String.valueOf(pi.getValue()));
                    }
                }
                listItems.add(item);
            }
        }

        return listItems;
    }

    public static double stringToDouble(String value){
        try{
            return Double.valueOf(value);
        }catch(NumberFormatException e){
            e.printStackTrace();
        };

        return 0.0;
    }

    public static Integer stringToInteger(String value){
        try{
            return Integer.valueOf(value);
        }catch(NumberFormatException e){
            e.printStackTrace();
        };

        return null;
    }

    public static Boolean stringToBoolean(String value){
        try{
            return Integer.valueOf(value) == 1;
        }catch(NumberFormatException e){
            e.printStackTrace();
        };

        return false;
    }

    private static ChargingPoolAddress getChargingPoolAddress(SoapObject so){
        ChargingPoolAddress c = new ChargingPoolAddress();

        for (int j = 0; j < so.getPropertyCount(); j++) {
            PropertyInfo pi = new PropertyInfo();
            so.getPropertyInfo(j, pi);

            if(!String.valueOf(pi.getValue()).equals("anyType{}")) {
                if (pi.getName().equals(ChargingPoolAddress.SOAP_StreetNumber))
                    c.setStreetNumber(String.valueOf(pi.getValue()));

                else if (pi.getName().equals(ChargingPoolAddress.SOAP_StreetName))
                    c.setStreetName(String.valueOf(pi.getValue()));

                else if (pi.getName().equals(ChargingPoolAddress.SOAP_PostCode))
                    c.setPostCode(String.valueOf(pi.getValue()));

                else if (pi.getName().equals(ChargingPoolAddress.SOAP_City))
                    c.setCity(String.valueOf(pi.getValue()));

                else if (pi.getName().equals(ChargingPoolAddress.SOAP_Province))
                    c.setProvince(String.valueOf(pi.getValue()));

                else if (pi.getName().equals(ChargingPoolAddress.SOAP_Country))
                    c.setCountry(String.valueOf(pi.getValue()));
            }
        }

        return c;
    }

    private static OpenTime getOpenTime(SoapObject so){
        OpenTime ot = new OpenTime();

        for (int j = 0; j < so.getPropertyCount(); j++) {
            PropertyInfo pi = new PropertyInfo();
            so.getPropertyInfo(j, pi);

            if (pi.getName().equals(OpenTime.SOAP_WeekDay))
                ot.setWeekDay(stringToInteger(String.valueOf(pi.getValue())));

            else if (pi.getName().equals(OpenTime.SOAP_StartTime))
                ot.setStartTime(String.valueOf(pi.getValue()));

            else if (pi.getName().equals(OpenTime.SOAP_EndTime))
                ot.setEndTime(String.valueOf(pi.getValue()));
        }

        return ot;
    }
}
