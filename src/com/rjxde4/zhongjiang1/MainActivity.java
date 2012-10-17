package com.rjxde4.zhongjiang1;

import org.json.JSONArray;
import org.json.JSONObject;

import com.rjxde0.zhongjiang1.game.GameStart;
import com.rjxde0.zhongjiang1.gamefb.GameStartFB;
import com.rjxde0.zhongjiang1.utility.AppUtil;
import com.rjxde0.zhongjiang1.utility.Config;
import com.rjxde0.zhongjiang1.utility.NetHelper;
import com.rjxde0.zhongjiang1.utility.StringUtils;
import com.rjxde0.zhongjiang1.utility.UpdateManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements 
	View.OnClickListener {
	
	private static final int START_GAME = 10011;
	private static final int START_GAME_FB = 10012;
	private static final int GAME_SETTING = 10021;
	private static final int AID_SETTING = 10022;
	private static final String TAG = "Update";
	private int newVerCode = 0;
	private RelativeLayout homeLayout;
	private Button startGameBtn, helpBtn, experienceBtn, moreBtn;
	UpdateManager updateManager;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Intent intent;
			switch (msg.what) {						
			case START_GAME:		
				intent = new Intent(MainActivity.this, GameStart.class);			
				startActivity(intent);					
				break;	
			case START_GAME_FB:			
				intent = new Intent(MainActivity.this, GameStartFB.class);				
				startActivity(intent);				
				break;		
			case GAME_SETTING:		
				intent = new Intent(MainActivity.this, AidActivity.class);				
				startActivity(intent);		
				break;	
			case AID_SETTING:			
				intent = new Intent(MainActivity.this, AidActivity.class);				
				startActivity(intent);				
				break;	
			}	
		}		
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);       
        
		initControls();
        loadAnimation();
    }
      
    private void initControls(){
    	homeLayout = (RelativeLayout)findViewById(R.id.home_layout);  
    	startGameBtn = (Button)findViewById(R.id.start_game);
    	helpBtn = (Button)findViewById(R.id.help);
    	experienceBtn = (Button)findViewById(R.id.experience);
    	moreBtn = (Button)findViewById(R.id.more);
        
    	startGameBtn.setOnClickListener(this);
    	helpBtn.setOnClickListener(this);
    	experienceBtn.setOnClickListener(this);
    	moreBtn.setOnClickListener(this);
    }
    
    private void loadAnimation(){
    	AlphaAnimation alpha = new AlphaAnimation(0.0f,1.0f);
 		alpha.setDuration(1000);
 	    alpha.setStartOffset(100); //设置动画延时执行
 	    homeLayout.startAnimation(alpha);
 	    homeLayout.setVisibility(View.VISIBLE);
    }
    
    public void onClick(View v) {
    	if (v instanceof Button) {
    		int id = ((Button) v).getId();
    		Intent intent;
    		switch (id) {  
    		case R.id.start_game:
    			Toast.makeText(this, "服务器连接中...", Toast.LENGTH_SHORT).show();
    			if(!NetHelper.networkIsAvailable(getApplicationContext())){
    	  			networkCloseDialog();
    	  			return;
    	  		}else{
    	  			new LoadJsonThread().start();
    	  		}
    			break;
    		case R.id.help:
    			intent = new Intent(MainActivity.this, PayActivity.class);
      			startActivity(intent);
    			break;
    		case R.id.experience:
    			Toast.makeText(this, "服务器连接中...", Toast.LENGTH_SHORT).show();
    			if(!NetHelper.networkIsAvailable(getApplicationContext())){
    	 			networkCloseDialog();
    	 			return;
    	 		}else{
    	 			new mLoadJsonThread().start();
    	 		}
    			break;
    		case R.id.more:
    			intent = new Intent(MainActivity.this, MoreActivity.class);
      			startActivity(intent);
      			overridePendingTransition(R.anim.load_scale_expand, 0);
    			break;
    		}
    	}
    }

	/**
	 * 开始游戏Activity切换
	 * @author Administrator
	 *
	 */
	private class LoadJsonThread extends Thread {
		@Override
		public void run() {
			if (getServerVerCode()) {
	        	int vercode = AppUtil.GetVersionCode(MainActivity.this); 
	        	Message msg = new Message();
	        	if (newVerCode > vercode) {                        
	    			msg.what = START_GAME_FB;
	    			MainActivity.this.mHandler.sendMessage(msg);              
	        	}else{
	        		msg.what = START_GAME;
	        		MainActivity.this.mHandler.sendMessage(msg); 
	        	}       
	        }		
		}
	}
	
	/**
	 * 游戏辅助Activity切换
	 * @author Administrator
	 *
	 */
	private class mLoadJsonThread extends Thread {
		@Override
		public void run() {
			if (getServerVerCode()) {
		       	int vercode = AppUtil.GetVersionCode(MainActivity.this);
		       	Message msg = new Message();
		       	if (newVerCode > vercode) {                        
		   			msg.what = AID_SETTING;
		   			MainActivity.this.mHandler.sendMessage(msg);              
		       	}else{
		       		msg.what = GAME_SETTING;
		       		MainActivity.this.mHandler.sendMessage(msg);
		       	}  
			}		
		}
	}
	
	/**
	 * 下载Json数据
	 * @return
	 */
	private boolean getServerVerCode() {           
    	try {                    
    		String verjson = NetHelper.getContent(Config.UPDATE_SERVER + Config.UPDATE_VERJSON);                      
    		JSONArray array = new JSONArray(verjson); 
    		if(!StringUtils.isNullOrEmpty(verjson)) {
    			if (array.length() > 0) {                                
        			JSONObject obj = array.getJSONObject(0);                              
        			try {                                     
        				newVerCode = Integer.parseInt(!StringUtils.isNullOrEmpty(obj.getString("verCode"))?obj.getString("verCode"):"0");                                   
        			}catch(OutOfMemoryError error){
        				error.printStackTrace();
        				newVerCode = -1;      
        				return false;        
        			} catch (Exception e) {                                    
        				newVerCode = -1;                                    
        				return false;                              
        			}                      
        		}            
    		}
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
	
	/**
	 * 网络未打开提示对话框
	 */
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
	 * 按下back键退出对话框
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AppUtil.QuitHintDialog(this);
			return true;
		}else {		
			return super.onKeyDown(keyCode, event);
		}
	}
	
}






