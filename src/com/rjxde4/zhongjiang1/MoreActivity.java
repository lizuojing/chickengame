package com.rjxde4.zhongjiang1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rjxde0.zhongjiang1.utility.AppUtil;
import com.rjxde0.zhongjiang1.utility.Config;
import com.rjxde0.zhongjiang1.utility.NetHelper;
import com.rjxde0.zhongjiang1.utility.StringUtils;
import com.rjxde0.zhongjiang1.utility.UpdateManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MoreActivity extends Activity {
	private static final int UPDATEIDENTIFIER = 10031;
	private static final int NOUPDATE = 10032;
	private static final String TAG = "Update";
	private ListView listView;
	UpdateManager updateManager;
    private int newVerCode = 0;
    private String newVerName = "";
    private String updateContent = "";
//	private static final String[]  titleItem = {"��������", "���½�ͼ����", "���������", "����°汾", "����������", "�ٷ���̳"};	
	private static final int[]  titleItem = {R.string.more_news,R.string.more_piclist,R.string.more_share,R.string.more_check,R.string.more_suggestion,R.string.more_forum};	
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATEIDENTIFIER:
				doNewVersionUpdate();
				break;
			case NOUPDATE:
				notNewVersionShow();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more); 

		listView = (ListView) findViewById(R.id.morelist);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < titleItem.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("picture", R.drawable.ic_listitem);
			map.put("title", getResources().getString(titleItem[i]));		
            list.add(map);
		}
	  
		SimpleAdapter adapter = new SimpleAdapter(this, (List<Map<String, Object>>) list, R.layout.more_list_item,
				new String[] { "picture",  "title"}, 
				new int[] {  R.id.list_item_picture, R.id.list_item_title});
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener(){ 
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  long arg3) { 
				Intent intent;
				switch(arg2){
				case 0:
					intent = new Intent(MoreActivity.this, NewsActivity.class);
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(MoreActivity.this, ImageActivity.class);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_TEXT, "����׬Ǯ�ֻ����θ����ű�!");
					startActivity(Intent.createChooser(intent, getTitle()));
					break;
				case 3:
					Toast.makeText(MoreActivity.this, "������������...", Toast.LENGTH_SHORT).show();
					if(!NetHelper.networkIsAvailable(getApplicationContext())){
			  			networkCloseDialog();
			  			return;
			  		}else{
			  			new LoadJsonThread().start();
			  		}
					break;
				case 4:
					Toast.makeText(MoreActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
					break;	
				case 5:
					bbsDialog();
					break;	
				}
				
	        }
	     });
		
		Button homeBtn = (Button)findViewById(R.id.homebtn);
		homeBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MoreActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
	}

	private void bbsDialog() {
		  
        AlertDialog alertDialog = new AlertDialog.Builder(this)
        		.setIcon(R.drawable.app_logo)
                .setTitle(R.string.bi_dialog_title)
                .setMessage("��̳ϵͳ�����Ż���...\n�������ţ�")
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    
                	public void onClick(DialogInterface dialog, int which) {                   
                		dialog.dismiss();
                    }
                }).create();  
        alertDialog.show();
    }
		
	//---------------------------------���²���------------------------------------

	public class LoadJsonThread extends Thread {
		@Override
		public void run() {
			if (getServerVerCode()) {
	        	int vercode = AppUtil.GetVersionCode(MoreActivity.this);
	        	Message msg = new Message();
	        	if (newVerCode > vercode) {                        
	    			msg.what = UPDATEIDENTIFIER;
	    			MoreActivity.this.handler.sendMessage(msg);              
	        	}else{
	        		msg.what = NOUPDATE;
	        		MoreActivity.this.handler.sendMessage(msg);
	        	}
	        }		
		}
	}
	
	private boolean getServerVerCode() {           
    	try {                    
    		String verjson = NetHelper.getContent(Config.UPDATE_SERVER + Config.UPDATE_VERJSON);                      
    		Log.i(TAG, "verjson is " + verjson);
    		if(!StringUtils.isNullOrEmpty(verjson)) {
    			JSONArray array = new JSONArray(verjson);                     
        		if (array.length() > 0) {                                
        			JSONObject obj = array.getJSONObject(0);                              
        			try {                                     
        				newVerCode = Integer.parseInt(obj.getString("verCode"));                                   
        				newVerName = obj.getString("verName"); 
        				updateContent = obj.getString("updateContent");
        			}catch (JSONException je){
        				je.printStackTrace();
        				newVerCode = -1;                                    
        				newVerName = ""; 
        				updateContent = "";
        				return false;    
        			} catch (Exception e) {                                    
        				newVerCode = -1;                                    
        				newVerName = ""; 
        				updateContent = "";
        				return false;                              
        			}                      
        		}      	
    		}
    	}catch (JSONException je){
			je.printStackTrace();
			return false;         
		}catch(OutOfMemoryError error){
			error.printStackTrace();
			return false;         
		} catch (Exception e) {   
    		e.printStackTrace();
    		Log.e(TAG, e.getMessage());                      
    		return false;               
    	}               
    	return true;   
    }
	
	private void notNewVersionShow() {
        StringBuffer sb = new StringBuffer();
        sb.append("�������°汾��������£�");
        Dialog dialog = new AlertDialog.Builder(MoreActivity.this)
        .setIcon(android.R.drawable.ic_dialog_info)
        .setTitle("��ʾ��Ϣ")
        .setMessage(sb.toString())
        .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {            
        	public void onClick(DialogInterface dialog, int which) {               	
        		dialog.cancel();                  
        	}
        }).create();
        dialog.show();
    }

	private void doNewVersionUpdate() {
        StringBuffer sb = new StringBuffer();
        sb.append(updateContent);
        Dialog dialog = new AlertDialog.Builder(MoreActivity.this)
        .setIcon(R.drawable.app_logo)
        .setTitle("�����°汾")
        .setMessage(sb.toString())
        .setPositiveButton("��������", new DialogInterface.OnClickListener() {        	
            public void onClick(DialogInterface dialog, int which) {
            	if(!isSDCardReady()){
            		Toast.makeText(MoreActivity.this, "�洢��������", Toast.LENGTH_LONG).show();
            	}
            	updateManager = new UpdateManager(MoreActivity.this);
                updateManager.checkUpdateInfo();
            }
        }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                                            
        	public void onClick(DialogInterface dialog, int whichButton) {                                   
        		dialog.dismiss();                               
        	}                          
        }).create();        
        dialog.show();
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

	/**
     * ��ȡ��ǰ�洢��״̬    
     * @return
     */
	public static boolean isSDCardReady(){
        String STATE = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(STATE) ;      
	}
	
}





