package com.rjxde0.zhongjiang1.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.rjxde4.zhongjiang1.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class UpdateManager {
	private Context mContext;
	private Dialog downloadDialog;
	private ProgressBar mProgress;  	
    private static final int DOWN_UPDATE = 1;   
    private static final int DOWN_OVER = 2;    
    private int progress;   
    private Thread downLoadThread;    
    private boolean interceptFlag = false;

    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);   //设置进度条的值
				break;
			case DOWN_OVER:
				downloadDialog.cancel(); 
				installApk();
				break;
			default:
				break;
			}
    	};
    };
    
	public UpdateManager(Context context) {
		this.mContext = context;
	}
	//外部接口让主Activity调用
	public void checkUpdateInfo(){
		showDownloadDialog();
		/*Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		Uri downUrl = Uri.parse(Config.UPDATE_SERVER + Config.UPDATE_APKNAME);
		intent.setData(downUrl);
		try {
			mContext.startActivity(intent);
		} catch (Exception e) {
			intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
			mContext.startActivity(intent);
		}*/
	}
	
	private void showDownloadDialog(){
		AlertDialog.Builder builder = new Builder(mContext);  
		builder.setIcon(R.drawable.app_logo);
		builder.setTitle("最新数据包");
		builder.setMessage("\n正在下载最新数据包!  请稍等...");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.progress);
		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {	
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();		
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();		
		downloadApk();
	}
	
	private Runnable mdownApkRunnable = new Runnable() {
		
		public void run() {
			try {
//				URL url = new URL(Config.UPDATE_SERVER + Config.UPDATE_APKNAME);			
//				URL url = new URL("http://dig.chouti.com/download/Chouti/1.1/Android/build281/Chouti_Android_1.1_build281_for_gozap_release.apk");			
				URL url = new URL("http://www.baidupcs.com/file/XiaoJiGame3.apk?fid=2668288549-250528-485873258&time=1350550918&sign=FPDTAE-DCb740ccc5511e5e8fedcff06b081203-pdpvoLhSO6hAtuJT1Zi%2Fa9dTYfA%3D&expires=1h&digest=cb53d543b83635fb1a4af7792bee9845");			
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				//连接服务器超时时间
				conn.setConnectTimeout(5000);
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(Config.downloadPath);
				//没有该目录则创建
				if(!file.exists()){
					file.mkdir();
				}
				
				String apkFile = Config.saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				
				int count = 0;
				byte buf[] = new byte[1024*4];				
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    	    progress = (int)(((float)count / length) * 100);
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE); //更新进度
		    		if(numread <= 0){		
		    			mHandler.sendEmptyMessage(DOWN_OVER); //下载完成通知安装
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);//点击取消就停止下载
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
			
		}
	};
	
	 /**
     * 下载apk
     * @param url
     */	
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	 
	/**
     * 安装apk
     * @param url
     */
	private void installApk(){
		File apkfile = new File(Config.saveFileName);
        if (!apkfile.exists()) {
            return;
        }    
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        mContext.startActivity(i);
	}

}










