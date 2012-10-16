package com.rjxde4.zhongjiang1;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rjxde0.zhongjiang1.utility.Config;
import com.rjxde0.zhongjiang1.utility.Download;
import com.rjxde0.zhongjiang1.utility.FileUtils;
import com.rjxde0.zhongjiang1.utility.NetHelper;

public class ImageActivity extends Activity implements View.OnClickListener{
	protected static final String TAG = "ImageActivity";
	private String[] imageURL = new String[] { Config.IMAGEDIR + "m1.jpg",
			Config.IMAGEDIR + "m2.jpg", Config.IMAGEDIR + "m3.jpg",
			Config.IMAGEDIR + "m4.jpg", Config.IMAGEDIR + "m5.jpg",
			Config.IMAGEDIR + "m6.jpg", Config.IMAGEDIR + "m7.jpg",
			Config.IMAGEDIR + "m8.jpg", Config.IMAGEDIR + "m9.jpg",
			Config.IMAGEDIR + "m10.jpg", };
	// 保存文件名
	private String[] fileName = new String[] { "m1.jpg", "m2.jpg", "m3.jpg",
			"m4.jpg", "m5.jpg", "m6.jpg", "m7.jpg", "m8.jpg", "m9.jpg",
			"m10.jpg" };
	private ImageView imageView;
	private Button pageupBtn, pagedownBtn, downloadBtn;
	private int position = 0;
	private Bitmap bm;
	ProgressBar pb;
	ProgressDialog pd;

	private HashMap<String, SoftReference<Bitmap>> bitmaps = new HashMap<String, SoftReference<Bitmap>>();
	private ImageLoaderTask task;
	private ImageDownloadTask download;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);

		if (!NetHelper.networkIsAvailable(getApplicationContext())) {
			networkCloseDialog();
			return;
		} else {
			imageView = (ImageView) findViewById(R.id.club_image);
			pageupBtn = (Button) findViewById(R.id.pageup);
			pagedownBtn = (Button) findViewById(R.id.pagedown);
			downloadBtn = (Button) findViewById(R.id.download);
			pb = (ProgressBar) findViewById(R.id.load_image_progress);
			if(task!=null) {
				task.cancel(true);
				task = null;
			}
			task = new ImageLoaderTask();
			task.execute();
		}

		pageupBtn.setOnClickListener(this);
		pagedownBtn.setOnClickListener(this);
		downloadBtn.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		bitmaps = null;
//		deleteFile();//TODO 删除缓存文件
		super.onDestroy();
	}

	private void deleteFile() {
		FileUtils utils = new FileUtils();
		utils.deleteCacheFile();
	}

	private void loadImage(int position) {
		String urlpath = imageURL[position];
		Log.i(TAG, "position is " + position + " urlpath is " + urlpath);
		try {
			// 从内存缓存中取图片
			if (bitmaps.containsKey(urlpath)) {
				SoftReference<Bitmap> reference = bitmaps.get(urlpath);
				if (reference != null) {
					Bitmap bitmap = reference.get();
					if (bitmap != null) {
						bm = bitmap;
						return;
					}
				}
			}

			// http://apk.4c27.com/xiaoji/image/m1.jpg
			// 从sd卡缓存中取图片
			int i = urlpath.lastIndexOf("/");
			String filename = urlpath.substring(i + 1);
			FileUtils fileUtils = new FileUtils();
			File cachePath = fileUtils.getCachePath();
			String localPath = cachePath + "/" + filename;
			Log.i(TAG, "localPath is " + localPath);
			if (localPath != null) {
				File file = new File(localPath);
				if (file.exists()) {
					Bitmap bitmap = BitmapFactory.decodeFile(localPath);
					if (bitmap != null) {
						bitmaps.put(urlpath, new SoftReference<Bitmap>(bitmap));
						bm = bitmap;
						return;
					}
				}
			}

			// 下载网络图片
			URL url = new URL(urlpath); // 生成一个URL对象
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 打开连接
			conn.setConnectTimeout(5000);
			conn.connect();
			InputStream inputstream = conn.getInputStream(); // 得到数据流
			// bm = BitmapFactory.decodeByteArray(data, 0, data.length);
			// //从字节数组流里创建一个Bitmap对象

			Log.i(TAG, "filepath is " + cachePath.getAbsolutePath() + "/"
					+ filename);
			File file = fileUtils.saveInputStreamToFile(inputstream, cachePath
					.getAbsolutePath()+ "/" + filename);
			Log.i(TAG, "file is " + file.exists());
			if (file == null) {
				return;
			}
			bm = BitmapFactory.decodeFile(file.getAbsolutePath());
			if (bm != null) {
				bitmaps.put(urlpath, new SoftReference<Bitmap>(bm));
				return;
			}

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	class ImageLoaderTask extends AsyncTask<String, Integer, Long> {

		@Override
		protected Long doInBackground(String... params) {
			loadImage(position);
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			imageView.setImageBitmap(bm);
			pb.setVisibility(View.GONE);
			task = null;
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			pb.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		
		
		
	}
	
	class ImageDownloadTask extends AsyncTask<String, Integer, Long> {

		private int downloadResult;

		@Override
		protected Long doInBackground(String... params) {
			Download download = new Download();
			downloadResult = download.downFile(imageURL[position], "zhuanqian/",
					fileName[position]);

			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			if (downloadResult == 0) {
				Toast.makeText(getApplicationContext(), "下载成功",
						Toast.LENGTH_LONG).show();
				pd.dismiss();
			} else if (downloadResult == 1) {
				Toast.makeText(getApplicationContext(), "同命名文件以存在",
						Toast.LENGTH_LONG).show();
				pd.dismiss();
			} else if (downloadResult == -1) {
				Toast.makeText(getApplicationContext(), "下载失败",
						Toast.LENGTH_LONG).show();
				pd.dismiss();
			}
			download = null;
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(ImageActivity.this, "请稍后", "图片下载中",
					true, true);
			super.onPreExecute();
		}
		
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

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pageup:
			position--;
			if (position <= 0) {
				Toast.makeText(getApplicationContext(), "这是第一张",
						Toast.LENGTH_LONG).show();
				position = 0;
			} else {
				if(task!=null) {
					task.cancel(true);
					task = null;
				}
				task = new ImageLoaderTask();
				task.execute();
			}
			break;
	
		case R.id.pagedown:
			position++;
			if (position >= imageURL.length - 1) {
				Toast.makeText(getApplicationContext(), "这是最后一张",
						Toast.LENGTH_LONG).show();
				position = imageURL.length - 1;
			} else {
				if(task!=null) {
					task.cancel(true);
					task = null;
				}
				task = new ImageLoaderTask();
				task.execute();
			}
			break;
			
		case R.id.download:
			if(download!=null) {
				download.cancel(true);
				download = null;
			}
			download = new ImageDownloadTask();
			download.execute();
			break;
			
		default:
			break;
		}
	}

}
