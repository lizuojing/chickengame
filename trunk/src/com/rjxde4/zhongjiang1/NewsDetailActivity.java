package com.rjxde4.zhongjiang1;

import com.rjxde0.zhongjiang1.utility.Config;
import com.rjxde0.zhongjiang1.utility.Download;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewsDetailActivity extends Activity {
	private static final int UPDATE_LIST = 10011;
	private static final int NETWORK_ERROR = 10012;
	private String title, date, txtName;
	private String resultTxt = null;
	private TextView titleTV, dateTV, detailTV;
	private ProgressDialog pdTxt;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_LIST:			
				titleTV.setText(title);
				dateTV.setText("发表时间:" + date);
				detailTV.setText(resultTxt);
				pdTxt.dismiss();
				break;
			case NETWORK_ERROR:
				Toast.makeText(NewsDetailActivity.this, "连接失败，你的网速不给力哦！", Toast.LENGTH_LONG).show();
				pdTxt.dismiss();
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		
		pdTxt = ProgressDialog.show(this, "", "数据加载中", true, true);
		new LoadTxtThread().start();
		
		titleTV = (TextView)findViewById(R.id.tv_title);
		dateTV = (TextView)findViewById(R.id.tv_date);
		detailTV = (TextView)findViewById(R.id.tv_detail);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("newsData");
		if(bundle != null){
			title = bundle.getString("title");
			date = bundle.getString("date");
			txtName = bundle.getString("link");
		}

		Button backBtn = (Button)findViewById(R.id.btn_back);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
		
	private class LoadTxtThread extends Thread {
		@Override
		public void run() {
			try {
				resultTxt = Download.getNewsInfo(Config.newsPath + txtName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Message msg = new Message();
				msg.what = NETWORK_ERROR;
				NewsDetailActivity.this.mHandler.sendMessage(msg);
			}
			if(resultTxt != null){
				Message msg = new Message();
				msg.what = UPDATE_LIST;
				NewsDetailActivity.this.mHandler.sendMessage(msg);
			}
		}
	}
}
