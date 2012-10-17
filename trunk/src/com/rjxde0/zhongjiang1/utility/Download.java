package com.rjxde0.zhongjiang1.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class Download {
	private static final int CONNECTCOUNT = 5;
	private URL url = null;
	
	/**
	 * HTML�ķ�ʽ�����ļ�
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String getNewsInfo(String url) {
		Log.i("download", "url is " + url);
        StringBuilder sb = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();  
        HttpConnectionParams.setConnectionTimeout(httpParams, 10*1000); //�������ӳ�ʱ
        HttpConnectionParams.setSoTimeout(httpParams, 10*1000); //���õȴ����ݳ�ʱʱ�� 
        
        int connectCount = 1;
		do{
        	 try {
     			HttpResponse response = client.execute(new HttpGet(url));
     			HttpEntity entity = response.getEntity();
     			if (entity != null) {
     			    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192); 
     			    String line = null;
     			    while ((line = reader.readLine())!= null){
     			        sb.append(line + "\n");
     			    }
     			    reader.close();
     			}
     			break;
     		} catch (Exception e) {
     			connectCount++;
     			e.printStackTrace();
     		}
       }while(connectCount <=CONNECTCOUNT);
        
        return sb.toString();
    }
	
	/**
	 * �ú����������� -1�����������ļ����� 0�����������ļ��ɹ� 1�������ļ��Ѿ�����
	 */
	public int downFile(String urlStr, String path, String fileName) {
		InputStream inputStream = null;
		try {
			FileUtils fileUtils = new FileUtils();
			
			if (fileUtils.isFileExist(path + fileName)) {
				return 1;
			} else {
				inputStream = getInputStreamFromUrl(urlStr);
				File resultFile = fileUtils.write2SDFromInput(path,fileName, inputStream);
				if (resultFile == null) {
					return -1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	/**
	 * ����URL�õ�������
	 * 
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStreamFromUrl(String urlStr)
			throws MalformedURLException, IOException {
		url = new URL(urlStr);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		InputStream inputStream = urlConn.getInputStream();
		return inputStream;
	}
	
}
