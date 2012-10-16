package com.rjxde0.zhongjiang1.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;
import android.util.Log;

public class FileUtils {
	private String SDPATH;

	public String getSDPATH() {
		return SDPATH;
	}
	public FileUtils() {
		//�õ���ǰ�ⲿ�洢�豸��Ŀ¼
		// /SDCARD
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}
	/**
	 * ��SD���ϴ����ļ�
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}
	
	/**
	 * ��SD���ϴ���Ŀ¼
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}
	
	public File getCachePath() {
		File dir = new File(SDPATH + "zhuanqian/cache");
		dir.mkdirs();
		return dir;
	}
	public File getAppPath() {
		File dir = new File(SDPATH + "zhuanqian/");
		dir.mkdirs();
		return dir;
	}

	/**
	 * �ж�SD���ϵ��ļ����Ƿ����
	 */
	public boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}
	
	
	public File saveInputStreamToFile(InputStream inStream, String filePath) throws Exception
	{
		File fl = new File(filePath);
        try
        {  
        	fl.createNewFile();  
        }
        catch (IOException e) 
        {  
            e.printStackTrace();
        }  
        
	    FileOutputStream fos = new FileOutputStream(fl);
	    byte[] buffer = new byte[1024];
	    int len = -1;
	    while( (len = inStream.read(buffer)) != -1 )
	    {
	    	fos.write(buffer, 0, len);
	    }
	    fos.close();
	    return fl;
	}
	
	/**
	 * ��һ��InputStream���������д�뵽SD����
	 */
	public File write2SDFromInput(String path,String fileName,InputStream input){
		File file = null;
		OutputStream output = null;
		try{
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer [] = new byte[4 * 1024];
			int length = 0;
			while((length = input.read(buffer)) != -1){
				output.write(buffer,0,length);
			}
			output.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				output.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}
	public void deleteCacheFile() {
		File file = new File(SDPATH + "zhuanqian/cache");
		if(file!=null&&file.exists()) {
			File[] files = file.listFiles();
			for(File f :files) {
				if(f.isFile()) {
					f.delete();
					Log.i("delete", f.getAbsolutePath());
				}
			}
		}
		file.delete();
		
	}

}