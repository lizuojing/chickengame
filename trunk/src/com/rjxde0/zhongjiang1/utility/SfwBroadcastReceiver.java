package com.rjxde0.zhongjiang1.utility;

import com.rjxde4.zhongjiang1.SplashActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class SfwBroadcastReceiver extends BroadcastReceiver {

	public static final String TAG = "SfwBroadcastReceiver";

	public static final String PKG_ADD_ACTION = "android.intent.action.PACKAGE_ADDED";
//	public static final String PKG_REM_ACTION = "android.intent.action.PACKAGE_REMOVED";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction() != null
				&& (intent.getAction().equals(PKG_ADD_ACTION) /*|| intent
						.getAction().equals(PKG_REM_ACTION)*/)) {
			Uri data = intent.getData();
			String pkgName = data.getEncodedSchemeSpecificPart();
			Log.i(TAG, "app package name:" + pkgName);
			if (pkgName != null) {
//				if (!("com.rjxde4.zhongjiang1".equals(pkgName))) {
					Intent startIntent = new Intent(context,SplashActivity.class);
	                startIntent.setPackage(pkgName);
	                startIntent.setAction("android.intent.action.MAIN");
	                startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(startIntent);
					
					
//				}
			}
		}
	}

}
