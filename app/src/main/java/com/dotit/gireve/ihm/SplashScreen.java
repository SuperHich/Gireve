package com.dotit.gireve.ihm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dotit.gireve.R;
import com.dotit.gireve.utils.GireveFonts;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

/**
 * @author Hichem LAROUSSI.
 */
@ContentView(R.layout.splash)
public class SplashScreen extends RoboActivity {

	private static String TAG = SplashScreen.class.getName();
	private static long SLEEP_TIME = 3 * 1000;    // Sleep for some time (in seconds)

    private Handler mHandler = new Handler();
    private Runnable mStartRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashScreen.this, MapsActivity.class));
            finish();
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

       	GireveFonts.Init(this);

		// Start timer and launch main activity
        mHandler.postDelayed(mStartRunnable, SLEEP_TIME);
    }

    @Override
    public void onBackPressed() {
        mHandler.removeCallbacks(mStartRunnable);
        super.onBackPressed();
    }
}


