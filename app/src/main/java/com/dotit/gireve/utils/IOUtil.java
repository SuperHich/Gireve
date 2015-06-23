package com.dotit.gireve.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.dotit.gireve.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IOUtil {
    private static final String TAG = IOUtil.class.getSimpleName();

    public static String readFully(InputStream inputStream) throws IOException {

        if(inputStream == null) {
            return "";
        }

        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            byteArrayOutputStream = new ByteArrayOutputStream();

            final byte[] buffer = new byte[1024];
            int available = 0;

            while ((available = bufferedInputStream.read(buffer)) >= 0) {
                byteArrayOutputStream.write(buffer, 0, available);
            }

            return byteArrayOutputStream.toString();

        } finally {
            if(bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        }
    }


    public static File createFileFromInputStream(InputStream inputStream, String fileName) {

        try{
            File f = new File(fileName);
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            Log.i(TAG, ">>> file: " + f);
            return f;
        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String todayAsString(){

        Date date = Calendar.getInstance(Locale.FRENCH).getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String today = sdf.format(date);

        Log.i("TAG", ">>> today " + today);

        return today;
    }

    public static String formatDouble(double value){
        String result = null;
        try {
            result = new DecimalFormat("##.######").format(value);
            result = result.replaceAll(",", ".");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static String formatDoubleWithSingleDecimal(double value){
        String result = null;
        try {
            result = new DecimalFormat("##.#").format(value);
//            result = result.replaceAll(",", ".");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    public static void showInfoPopup(final Context context, String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if(title != null)
            builder.setTitle(title);

        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });

        // Create the AlertDialog object and return it
        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnPositive = alert.getButton(Dialog.BUTTON_POSITIVE);
                btnPositive.setTypeface(GireveFonts.getClanProBook());

                int dialogTitle = context.getResources().getIdentifier( "alertTitle", "id", "android" );
                TextView txv_title = (TextView) alert.findViewById(dialogTitle);
                TextView txv_message = (TextView) alert.findViewById(android.R.id.message);

                txv_title.setTypeface(GireveFonts.getClanProBook());
                txv_message.setTypeface(GireveFonts.getClanProBook());
            }
        });

        alert.show();
    }

    public static void hideKeyBoardFromWindow(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive())
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyBoardOnWindow(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public static void hideKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static void CopyAssetsFileToSDCard(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fileName);   // if files resides inside the "Files" directory itself
            out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() +"/" + fileName);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch(Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}
