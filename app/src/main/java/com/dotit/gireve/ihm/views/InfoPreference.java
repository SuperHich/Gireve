package com.dotit.gireve.ihm.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dotit.gireve.R;
import com.dotit.gireve.utils.GireveManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class InfoPreference extends DialogPreference {

    private Animation rotate;
    TextView txv_info;
    ImageView btn_refresh;

    String initialValue;
    String defaultValue;

    GireveManager mManager;

    public InfoPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.fragment_info_dialog);
        setDialogIcon(null);

        mManager = GireveManager.getInstance(context);

        rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.txv_info       = (TextView)view.findViewById(R.id.txv_info);
        this.btn_refresh    = (ImageView)view.findViewById(R.id.btn_refresh);

        this.txv_info.setTextIsSelectable(true);

        if ( this.initialValue != null ) {
            refreshValue();
        }

        if(getKey().equals(getContext().getString(R.string.pref_key_ip_address))) {
            defaultValue = getContext().getString(R.string.pref_default_ip_address);
            btn_refresh.setVisibility(View.VISIBLE);
            btn_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMyIP(getContext());
                }
            });

        }
        else if(getKey().equals(getContext().getString(R.string.pref_key_fault_response)))
            defaultValue = getContext().getString(R.string.pref_default_fault_response);
    }

    private void refreshValue(){
        txv_info.setText(initialValue);
        persistString( initialValue );
        callChangeListener( initialValue );
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
                                     Object defaultValue) {
        String def = String.valueOf( (defaultValue instanceof String ) ? defaultValue
                : ( defaultValue != null ) ? defaultValue.toString() : defaultValue);
        if ( restorePersistedValue ) {
            this.initialValue = getPersistedString(def);
        }
        else this.initialValue = String.valueOf(defaultValue);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }


    public void getMyIP(final Context context){
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                btn_refresh.startAnimation(rotate);
                btn_refresh.setEnabled(false);
            }

            @Override
            protected String doInBackground(Void... arg0) {

                return mManager.getIPAddress();
            }

            @Override
            protected void onPostExecute(String result) {
                btn_refresh.clearAnimation();
                btn_refresh.setEnabled(true);

                Log.e("", ">>>>> My IP " + result);
                if(result != null) {
                    initialValue = result;
                }else
                    initialValue = context.getString(R.string.pref_default_ip_address);

                refreshValue();
             }


        }.execute();
    }
}