package com.rjxde0.zhongjiang1.utility;

public class StringUtils {

	public static boolean isNullOrEmpty(String str) {
		if(str==null||"".equals(str)) {
			return true;
		}
		return false;
	}

}
