package com.rjxde0.zhongjiang1.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetHelper {
    
	private static final int CONNECTCOUNT = 5;//�������ӳ����쳣�� ���Ӵ���

	/**
    * HTML�ķ�ʽ�����ļ�
    * @param url
    * @return
    * @throws Exception
    */
    public static String getContent(String url) {
    	int connectCount = 1;
        StringBuilder sb = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();  
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000); //�������ӳ�ʱ
        HttpConnectionParams.setSoTimeout(httpParams, 5000); //���õȴ����ݳ�ʱʱ�� 
        do{
        	 try {
     			HttpResponse response = client.execute(new HttpGet(url));
     			HttpEntity entity = response.getEntity();
     			if (entity != null) {
     			    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "GB2312"), 8192); 
     			    String line = null;
     			    while ((line = reader.readLine())!= null){
     			        sb.append(line + "\n");
     			    }
     			    reader.close();
     			}
     			break;//����ѭ��
     		} catch (Exception e) {
     			connectCount++;
     			e.printStackTrace();
     		} 
        }while(connectCount<=CONNECTCOUNT);
       
        return sb.toString();
    }
    
	/**
	 * ��ȡ�����Ƿ����״̬
	 * ����ӷ�������״̬��Ȩ��
	 * @return
	 */
	public static boolean networkIsAvailable(Context context) {
		ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		if (info.isConnected()) {
			return true;
		}
		return false;
	}
	
}
