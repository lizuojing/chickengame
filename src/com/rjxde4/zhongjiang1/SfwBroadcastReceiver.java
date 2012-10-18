package com.rjxde4.zhongjiang1;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class SfwBroadcastReceiver extends BroadcastReceiver {

	public static final String TAG = "SfwBroadcastReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "action: " + intent.getAction());
		if (intent.getAction() != null
				&& (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) || intent
						.getAction().equals(Intent.ACTION_PACKAGE_REPLACED))) {
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
