package com.rjxde4.zhongjiang1;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import com.rjxde0.zhongjiang1.utility.SfwBroadcastReceiver;

public class XiaojiApp extends Application {

	private static final String TAG = "XiaojiApp";


	public static Context appContext;


	private SfwBroadcastReceiver softwareReceiver;


	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "XiaojiApp oncreate");
		registerSoftwareReceiver();
		appContext = this.getApplicationContext();
	}
	
	private void registerSoftwareReceiver() {
		softwareReceiver = new SfwBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PACKAGE_ADDED");
		filter.addAction("android.intent.action.PACKAGE_REMOVED");
		filter.addDataScheme("package");
		registerReceiver(softwareReceiver, filter);
	}
	

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
