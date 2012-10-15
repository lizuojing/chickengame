package com.rjxde0.zhongjiang1.gamefb;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

public class GameStartFB extends Activity {
	public static GameStartFB instance = null;
	public GamePreview gamePreview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //设置屏幕方向为纵向
        SoundManager mSoundManager = new SoundManager(getBaseContext());	
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics); //获得屏幕分辨率
        
    	int width = metrics.widthPixels;
    	int height = metrics.heightPixels;
 	
        gamePreview = new GamePreview(this,mSoundManager,width,height); 
        setContentView(gamePreview);  
        instance = this;
    }
    
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    
    public void startGamePanel() {
    }

    @Override
    public void onStop() {
    	super.onStop();
    	finish();
    }
    
    @Override
    public void onRestart() {
    	super.onRestart();
    	finish();
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    }
    
    @Override
    public void onPause() {
    	super.onPause();  
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	finish(); 
    }
    
}