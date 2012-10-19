package com.rjxde4.zhongjiang1;

import com.rjxde0.zhongjiang1.utility.AppUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends Activity {
	
	private static final String TAG = "SplashActivity";
	protected int mSplashTime = 10000;
	protected Thread mSplashThread;
	private boolean mNextActivityStarted;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		AppUtil.setFirstLaunch(this, 1);
		Log.i(TAG,"firstlaunch is "+ AppUtil.isFirstLaunch(this));
	//	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 设置屏幕方向为纵向 
        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < mSplashTime) {
                        sleep(50);
                        waited += 200; 
                    }
                } catch(InterruptedException e) {
                    // do nothing
                } finally {
                	if (mNextActivityStarted == false) {
             			mNextActivityStarted = true;
             			startActivity(new Intent(SplashActivity.this, MainActivity.class));	
             			finish();
                	}
                }
            }
        };
        mSplashThread.start();   
	}
}
