package com.rjxde4.zhongjiang1;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.rjxde0.zhongjiang1.utility.Config;
import com.rjxde0.zhongjiang1.utility.Download;
import com.rjxde0.zhongjiang1.utility.NetHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ImageActivity extends Activity {
	private static final int UPDATEIMAGE = 10001;
	private static final int DOWNSUCCESS = 10002;
	private static final int DOWNFAILURE = 10003;
	private static final int DOWNALREADY = 10004;
	private String[] imageURL = new String[] { 
			Config.IMAGEDIR + "m1.jpg", 
			Config.IMAGEDIR + "m2.jpg", 
			Config.IMAGEDIR + "m3.jpg", 
			Config.IMAGEDIR + "m4.jpg", 
			Config.IMAGEDIR + "m5.jpg", 
			Config.IMAGEDIR + "m6.jpg", 
			Config.IMAGEDIR + "m7.jpg",
			Config.IMAGEDIR + "m8.jpg",
			Config.IMAGEDIR + "m9.jpg",
			Config.IMAGEDIR + "m10.jpg",};
	//保存文件名
	private String[] fileName = new String[] { "m1.jpg", "m2.jpg", "m3.jpg", "m4.jpg", "m5.jpg", "m6.jpg", "m7.jpg","m8.jpg","m9.jpg","m10.jpg"};
	private ImageView imageView;
	private Button pageupBtn, pagedownBtn, downloadBtn;
	private int position = 0;
	private Bitmap bm;
	ProgressBar pb;
	ProgressDialog pd;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATEIMAGE:
				imageView.setImageBitmap(bm);
				pb.setVisibility(View.GONE);
				break;
			case DOWNSUCCESS:
				Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_LONG).show();
				pd.dismiss();
				break;
			case DOWNALREADY:
				Toast.makeText(getApplicationContext(), "同命名文件以存在", Toast.LENGTH_LONG).show();
				pd.dismiss();
				break;
			case DOWNFAILURE:
				Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
				pd.dismiss();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);
		
		if(!NetHelper.networkIsAvailable(getApplicationContext())){
			networkCloseDialog();
  			return;
  		} else {
  			imageView  = (ImageView) findViewById(R.id.club_image);
  	        pageupBtn = (Button)findViewById(R.id.pageup);
  	        pagedownBtn = (Button)findViewById(R.id.pagedown);
  	        downloadBtn = (Button)findViewById(R.id.download);
  	        pb = (ProgressBar)findViewById(R.id.load_image_progress);
  	        loadProgress();
  			new LoadImageThread().start();
  		}
        
		pageupBtn.setOnClickListener(new Button.OnClickListener() {
            
			public void onClick(View v) {
				// TODO Auto-generated method stub
				position--;
				if(position <= 0) {
					Toast.makeText(getApplicationContext(), "这是第一张", Toast.LENGTH_LONG).show();
					position = 0;
				}
				else {
					loadProgress();
					new LoadImageThread().start();
				}
			}
		});
       
        pagedownBtn.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				position++;
				if(position >= imageURL.length - 1) {
					Toast.makeText(getApplicationContext(), "这是最后一张", Toast.LENGTH_LONG).show();
					position = imageURL.length - 1;
				}
				else {
					loadProgress();
					new LoadImageThread().start();
				}
			}
        });
        
        downloadBtn.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = ProgressDialog.show(ImageActivity.this, "请稍后", "图片下载中", true, true);
				new downloadImageThread().start();
			}	
        });      
	}
	
	private void loadImage(int position) {
		// TODO Auto-generated method stub
		String urlpath = imageURL[position];
		try {
			byte[] data = GetData.getStream(urlpath);
			bm = BitmapFactory.decodeByteArray(data, 0, data.length); //从字节数组流里创建一个Bitmap对象
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void networkCloseDialog() {
		
		AlertDialog alertDialog = new AlertDialog.Builder(this)
        		.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.dialog_network_error_title)
                .setMessage(R.string.dialog_network_error_message)
                .setPositiveButton(R.string.dialog_network_error_btn, new DialogInterface.OnClickListener() {
                    
                	public void onClick(DialogInterface dialog, int which) { 
                		startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }).create();  
        alertDialog.show();
	}
	
	private class LoadImageThread extends Thread {
		@Override
		public void run() {
			loadImage(position);
			Message msg = new Message();
			msg.what = UPDATEIMAGE;
			ImageActivity.this.handler.sendMessage(msg);  
		}
	}
	
	public class downloadImageThread extends Thread {
		@Override
		public void run() {
			Download download = new Download();
			int result = download.downFile(imageURL[position], "zhuanqian/", fileName[position]);
			Message msg = new Message();
			if(result == 0) {
				msg.what = DOWNSUCCESS;
				ImageActivity.this.handler.sendMessage(msg);
			}
			else if (result == 1) {
				msg.what = DOWNALREADY;
				ImageActivity.this.handler.sendMessage(msg);
			}
			else if(result == -1) {
				msg.what = DOWNFAILURE;
				ImageActivity.this.handler.sendMessage(msg);
			}
			
		}
	}
	
	private void loadProgress(){
		pb.setVisibility(View.VISIBLE);
	}
	
}

class GetData {
	public static byte[] getStream(String urlpath) throws Exception { //得到字节形式的流数据
		InputStream inputstream;
		URL url = new URL(urlpath); //生成一个URL对象
		HttpURLConnection conn = (HttpURLConnection)url.openConnection(); //打开连接
		conn.setConnectTimeout(5000);
		conn.connect();
		inputstream = conn.getInputStream(); //得到数据流
		ByteArrayOutputStream outputstream = new ByteArrayOutputStream(); //生成一个字节数组输出流对象
		byte[] buffer = new byte[1024];
		int len = -1;
		while((len = inputstream.read(buffer)) != -1){
			outputstream.write(buffer,0,len);
		}
		inputstream.close();
		outputstream.close();
		return outputstream.toByteArray(); //返回字节数组流
	}
	
}
