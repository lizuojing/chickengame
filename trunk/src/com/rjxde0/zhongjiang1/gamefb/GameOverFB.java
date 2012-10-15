package com.rjxde0.zhongjiang1.gamefb;

import com.rjxde0.zhongjiang1.utility.NetHelper;
import com.rjxde0.zhongjiang1.utility.UpdateManager;
import com.rjxde4.zhongjiang1.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class GameOverFB extends Activity {
	UpdateManager updateManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.download_data);
		Button downLoadBtn = (Button)findViewById(R.id.download);
		downLoadBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initNetwork();
			}
		});
		
		Button cancelBtn = (Button)findViewById(R.id.cancel);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	public void initNetwork() {
  		if(!NetHelper.networkIsAvailable(getApplicationContext())){
  			networkCloseDialog();
  			return;
  		}else{
  			if(!isSDCardReady()){
        		Toast.makeText(GameOverFB.this, "存储卡不可用", Toast.LENGTH_LONG).show();
        	}
        	updateManager = new UpdateManager(GameOverFB.this);
            updateManager.checkUpdateInfo();
  		}
	}
	
	private void networkCloseDialog() {
		
		AlertDialog alertDialog = new AlertDialog.Builder(this)
        		.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("提示信息")
                .setMessage("未打开网络连接！\n请检查你的网络设置。")
                .setPositiveButton("马上去设置", new DialogInterface.OnClickListener() {
                    
                	public void onClick(DialogInterface dialog, int which) {                   
                		startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }).create();  
        alertDialog.show();
	}
	
	/**
     * 获取当前存储卡状态    
     * @return
     */
	public static boolean isSDCardReady(){
        String STATE = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(STATE) ;      
	}
	
}



