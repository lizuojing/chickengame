package com.rjxde4.zhongjiang1;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.rjxde0.zhongjiang1.utility.AppUtil;

public class SfwBroadcastReceiver extends BroadcastReceiver {

	public static final String TAG = "SfwBroadcastReceiver";
	private PackageInfo packageInfo;
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "action: " + intent.getAction());
		if (intent.getAction() != null
				&& (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) || intent
						.getAction().equals(Intent.ACTION_PACKAGE_REMOVED))) {
			Uri data = intent.getData();
			String pkgName = data.getEncodedSchemeSpecificPart();
			
			Log.i(TAG, "app package name:" + pkgName + "launchtime is " + AppUtil.isFirstLaunch(context));
			if (pkgName != null) {
//				if (("com.rjxde4.zhongjiang1".equals(pkgName))) {
					PackageManager manager = context.getPackageManager();
					Intent startIntent = manager.getLaunchIntentForPackage(pkgName);
					context.startActivity(startIntent);
					/*try {
						Thread.sleep(2);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						packageInfo = context.getPackageManager().getPackageInfo("com.rjxde4.zhongjiang1", 0);
						
					} catch (NameNotFoundException e) {
//						e.printStackTrace();
						Log.i(TAG, "xiaoji package is not exist");
						return;
					}
					if(packageInfo!=null&&AppUtil.isFirstLaunch(context)>=1) {
						Intent startIntent = new Intent(context,SplashActivity.class);
		                startIntent.setPackage(pkgName);
		                startIntent.setAction("android.intent.action.MAIN");
		                startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(startIntent);
						AppUtil.setFirstLaunch(context, 1);
					}*/
//				}
			}
		}
	}

}
