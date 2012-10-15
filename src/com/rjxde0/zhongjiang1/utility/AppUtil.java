package com.rjxde0.zhongjiang1.utility;

import com.rjxde4.zhongjiang1.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class AppUtil {
	/**
	 * �˳�����
	 * @param context
	 */
	public static void QuitHintDialog(final Context context){
		new AlertDialog.Builder(context)
    	.setIcon(R.drawable.app_logo)
    	.setTitle("�˳���Ϸ")
    	.setMessage("ȷ���˳���")
    	.setPositiveButton("��",new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				try{
					((Activity)context).finish();
				}catch(Exception e){
					Log.e("close","+++++++++++++����+++++++++");
				}
			}
		}).setNegativeButton("��", new DialogInterface.OnClickListener() {				
		
			public void onClick(DialogInterface dialog, int which) {				
			}
		}).show();
    }
    
	/** 
	* �������汾��
	*/
	public static int GetVersionCode(final Context con) {
		int version = 1;
		PackageManager packageManager = con.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(con.getPackageName(), 0);
			version = packageInfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}
	
	/**
	 * ����������
	 * @param context
	 * @return
	 */
	public static String GetVersionName(final Context context){
		String versionName = "1.0.0";
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	  
}
