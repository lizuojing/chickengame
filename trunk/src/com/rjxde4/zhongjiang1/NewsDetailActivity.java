package com.rjxde4.zhongjiang1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rjxde0.zhongjiang1.utility.Config;
import com.rjxde0.zhongjiang1.utility.Download;

public class NewsDetailActivity extends Activity implements View.OnClickListener{
	private static final String TAG = "NewsDetailActivity";
	private String title, date, txtName;
	private String resultTxt = null;
	private TextView titleTV, dateTV, detailTV;
	private ProgressDialog pdTxt;
	private LoadTxtTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);

		pdTxt = ProgressDialog.show(this, "", "数据加载中", true, true);

		titleTV = (TextView) findViewById(R.id.tv_title);
		dateTV = (TextView) findViewById(R.id.tv_date);
		detailTV = (TextView) findViewById(R.id.tv_detail);

		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		date = intent.getStringExtra("date");
		txtName = intent.getStringExtra("link");

		Button backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);
		
		if(task!=null) {
			task.cancel(true);
			task = null;
		}
		task = new LoadTxtTask();
		task.execute(txtName);
	}

	private class LoadTxtTask extends AsyncTask<String, Integer, Long> {

		private boolean hasError = false;

		@Override
		protected Long doInBackground(String... params) {
			try {
				resultTxt = Download.getNewsInfo(Config.newsPath + txtName);
				Log.i(TAG, "url is " + Config.newsPath + txtName);
			} catch (Exception e) {
				e.printStackTrace();
				hasError = true;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			if(hasError ) {
				Toast.makeText(NewsDetailActivity.this, "连接失败，你的网速不给力哦！",Toast.LENGTH_LONG).show();
				pdTxt.dismiss();
			}else {
				if(resultTxt!= null) {
					titleTV.setText(title);
					dateTV.setText("发表时间:" + date);
					detailTV.setText(resultTxt);
					pdTxt.dismiss();
				}
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}
}
