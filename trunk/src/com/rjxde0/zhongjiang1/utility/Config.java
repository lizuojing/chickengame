package com.rjxde0.zhongjiang1.utility;

import android.os.Environment;

/**
 * ≈‰÷√ƒ⁄–≈œ¢
 * 
 * @author walkingp
 * 
 */
public class Config {	
	 public static final String UPDATE_SERVER = "http://apk.4c27.com/xiaoji/";
	 public static final String UPDATE_APKNAME = "xiaojigame.apk";
	 public static final String UPDATE_VERJSON = "anzhuo/check_version.json"; 
	 
	 public static final String downloadPath = Environment.getExternalStorageDirectory().getPath() + "/zhuanqian"; 
	 public static final String saveFileName = downloadPath + "/xiaojigame.apk";
	 
	 public static final String newsXML = UPDATE_SERVER + "news.xml";
	 public static final String newsPath = UPDATE_SERVER + "NewsDetail/";
	 public static final String IMAGEDIR = UPDATE_SERVER + "image/";

}
