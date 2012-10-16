package com.rjxde4.zhongjiang1;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.rjxde0.zhongjiang1.entity.NewsInfo;
import com.rjxde0.zhongjiang1.utility.Config;
import com.rjxde0.zhongjiang1.utility.Download;
import com.rjxde0.zhongjiang1.utility.NetHelper;
import com.rjxde0.zhongjiang1.utility.StringUtils;
import com.rjxde0.zhongjiang1.xml.NewsContentHandler;

public class NewsActivity extends ListActivity implements View.OnClickListener {
	private static final String TAG = "NewsActivity";
	private List<NewsInfo> newsInfos = null;
	ProgressDialog pd;
	private LoadDataTask task;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);

		loadNewsList();

		Button home = (Button) findViewById(R.id.btn_back);
		Button refresh = (Button) findViewById(R.id.news_refresh);

		home.setOnClickListener(this);
		refresh.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.news_refresh:
			loadNewsList();
			break;
		default:
			break;
		}

	}

	private void loadNewsList() {
		// 判断网络是否可用
		if (!NetHelper.networkIsAvailable(getApplicationContext())) {
			networkCloseDialog();
			return;
		} else {
			if (task != null) {
				task.cancel(true);
				task = null;
			}
			task = new LoadDataTask();
			task.execute();
		}
	}

	class LoadDataTask extends AsyncTask<Void, Integer, Long> {

		private String newsResult;
		private boolean neterror = false;

		@Override
		protected Long doInBackground(Void... params) {
			try {
				newsResult = Download.getNewsInfo(Config.newsXML);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				neterror = true;
			}
			if (!StringUtils.isNullOrEmpty(newsResult)) {
				newsInfos = parse(newsResult); // 对xml文件进行解析
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			if (neterror) {
				Toast.makeText(NewsActivity.this, "连接失败，你的网速不给力哦！",Toast.LENGTH_LONG).show();
				pd.dismiss();
			} else {
				if (newsResult != null) {
					SimpleAdapter simpleAdapter = buildSimpleAdapter(newsInfos);
					setListAdapter(simpleAdapter); // 将解析结果设置到List当中
					pd.dismiss();
				}
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(NewsActivity.this, "", "数据加载中", true, true);
			super.onPreExecute();
		}

	}

	private SimpleAdapter buildSimpleAdapter(List<NewsInfo> newsInfos) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (Iterator iterator = newsInfos.iterator(); iterator.hasNext();) {
			NewsInfo mp3Info = (NewsInfo) iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("news_title", mp3Info.getNewsTitle());
			map.put("news_date", mp3Info.getNewsDate());
			list.add(map);
		}
		// 创建一个SimpleAdapter对象
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
				R.layout.newslist_item, new String[] { "news_title",
						"news_date" }, new int[] { R.id.list_item_title,
						R.id.list_item_date });
		return simpleAdapter;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		NewsInfo newsInfo = newsInfos.get(position);
		Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("title", newsInfo.getNewsTitle());
		bundle.putString("date", newsInfo.getNewsDate());
		bundle.putString("link", newsInfo.getLink());
		intent.putExtra("newsData", bundle);

		startActivity(intent);
		super.onListItemClick(l, v, position, id);
	}

	private List<NewsInfo> parse(String xmlStr) {
		Log.i(TAG, "xmlStr is " + xmlStr);
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<NewsInfo> infos = new ArrayList<NewsInfo>();
		try {
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();
			NewsContentHandler newsListContentHandler = new NewsContentHandler(
					infos);
			xmlReader.setContentHandler(newsListContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			for (Iterator iterator = infos.iterator(); iterator.hasNext();) {
				NewsInfo mp3Info = (NewsInfo) iterator.next();
			}
		}catch (OutOfMemoryError error) {
			error.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	private void networkCloseDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setIcon(
				android.R.drawable.ic_dialog_alert).setTitle(
				R.string.dialog_network_error_title).setMessage(
				R.string.dialog_network_error_message).setPositiveButton(
				R.string.dialog_network_error_btn,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(
								android.provider.Settings.ACTION_WIRELESS_SETTINGS));
					}
				}).create();
		alertDialog.show();
	}

}
