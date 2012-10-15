package com.rjxde0.zhongjiang1.gamefb;

import java.util.HashMap;
import java.util.Map;

import com.rjxde4.zhongjiang1.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePreview extends SurfaceView implements SurfaceHolder.Callback, java.io.Serializable{
	public static final long serialVersionUID = 1L;
	private GamePreviewThread thread;
	private Map<Integer, Bitmap> mBitMapCache = new HashMap<Integer, Bitmap>();
	private Activity mActivity;
	private GameModel mGameModel;
	private SoundManager mSoundManager;	
	public Context context;
	
	float hero_width = 320;
	float hero_height = 480;
	int width = 0;
	int height = 0;
	float scale_x = 0;
	float scale_y = 0;
	float aspectratio = 0;
	
	private int textposx = 0; 
	private int textposy = 0;
	private int textposx2 = 0;
	private int textposy2 = 0;
	private int handposx = 0;
	private int touchx = 0;
	private int touchy = 0;
	
	public GamePreview(Context context, SoundManager sm, int width, int height) {
		super(context);
		
		this.width = width;
		this.height = height;

		scale_y = this.height / hero_width;
		scale_x = this.width / hero_height;
		
		float hero = hero_width / hero_height;
		float you = (float) (this.height) / (float) (this.width);
		aspectratio = you/hero;
		
		textposx = (int) (240 * scale_x); 
		textposy = (int) (20 * scale_y);
		textposx2 = (int) (240 * scale_x);
		textposy2 = (int) (180 * scale_y);
		handposx = (int) (-12 * scale_y);
		touchx = (int) (480 * scale_y);
		touchy = (int) (160 * scale_y);
		
		this.context = context;
		mSoundManager = sm;			
		mGameModel = new GameModel(mSoundManager, scale_x, scale_y);
		mGameModel.initialize(context);
		fillBitmapCache();	
		mActivity = (Activity) context;		
		getHolder().addCallback(this);
		thread = new GamePreviewThread(this,mGameModel);
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
	}
	
	private void fillBitmapCache() {
		mBitMapCache = new HashMap<Integer, Bitmap>();
		mBitMapCache.put(R.drawable.d1, BitmapFactory.decodeResource(getResources(), R.drawable.d1));
		mBitMapCache.put(R.drawable.d2, BitmapFactory.decodeResource(getResources(), R.drawable.d2));
		mBitMapCache.put(R.drawable.d3, BitmapFactory.decodeResource(getResources(), R.drawable.d3));
		mBitMapCache.put(R.drawable.d4, BitmapFactory.decodeResource(getResources(), R.drawable.d4));
		mBitMapCache.put(R.drawable.d5, BitmapFactory.decodeResource(getResources(), R.drawable.d5));
		mBitMapCache.put(R.drawable.d6, BitmapFactory.decodeResource(getResources(), R.drawable.d6));
		mBitMapCache.put(R.drawable.d7, BitmapFactory.decodeResource(getResources(), R.drawable.d7));
		mBitMapCache.put(R.drawable.d8, BitmapFactory.decodeResource(getResources(), R.drawable.d8));
		mBitMapCache.put(R.drawable.zt_bg, BitmapFactory.decodeResource(getResources(), R.drawable.zt_bg));
		mBitMapCache.put(R.drawable.girl, BitmapFactory.decodeResource(getResources(), R.drawable.girl));
	}
	
	public void onDraw(Canvas canvas) {
		int new_width = (int) (mBitMapCache.get(mGameModel.getBackgroun().getImage()).getWidth());
		Rect dst = new Rect(mGameModel.getBackgroun().getX1(new_width), 0, mGameModel.getBackgroun().getX1(new_width)+ new_width, height);

		if (mGameModel.getBackgroun().getFirst()) {
		    canvas.drawBitmap(mBitMapCache.get(mGameModel.getBackgroun().getImage()), null, dst, null);
		    dst = new Rect(mGameModel.getBackgroun().getX1(new_width) + new_width, 0,mGameModel.getBackgroun().getX1(new_width) + new_width + new_width, height);
		    canvas.drawBitmap(mBitMapCache.get(mGameModel.getBackgroun().getImage()), null, dst, null);
	    }
	    else {
			dst = new Rect(mGameModel.getBackgroun().getX1(new_width) + new_width, 0, mGameModel.getBackgroun().getX1(new_width)+ new_width + new_width, height);
		    canvas.drawBitmap(mBitMapCache.get(mGameModel.getBackgroun().getImage()), null, dst, null);
		    dst = new Rect(mGameModel.getBackgroun().getX1(new_width), 0,mGameModel.getBackgroun().getX1(new_width) + new_width, height);
		    canvas.drawBitmap(mBitMapCache.get(mGameModel.getBackgroun().getImage()), null, dst, null);
	    }

		int new_height = (int) (mBitMapCache.get(R.drawable.d1).getHeight());
		//绘制显示的英雄
		new_width = (int) (mBitMapCache.get(mGameModel.getDront().getDrontImage()).getWidth());
		new_height = (int) (mBitMapCache.get(mGameModel.getDront().getDrontImage()).getHeight());
		dst = new Rect(mGameModel.getDront().getX(), mGameModel.getDront().getY(), mGameModel.getDront().getX() + new_width, mGameModel.getDront().getY() + new_height);
	    canvas.drawBitmap(mBitMapCache.get(mGameModel.getDront().getDrontImage()), null, dst, null);			
	
		//绘制提示使者
		if(mGameModel.getShowGirl()){
			new_width = (int) (mBitMapCache.get(R.drawable.girl).getWidth());
			new_height = (int) (mBitMapCache.get(R.drawable.girl).getHeight());
			dst = new Rect((int) (330 * scale_x), (int) (40 * scale_y), (int) (330 * scale_x) + new_width, (int) (40 * scale_y) + new_height);
		    canvas.drawBitmap(mBitMapCache.get(R.drawable.girl), null, dst, null);	
		}
		
		//当游戏失败后显示的图片
		if(mGameModel.getLost()){
	//		canvas.drawBitmap(mBitMapCache.get(R.drawable.gameover),110 * scale_x,60 * scale_y,null);
			thread.setRunning(false);
			mBitMapCache = null;
			getHolder().removeCallback(this);
			
			Activity parent = (Activity) getContext();
			Intent intent = new Intent(parent, GameOverFB.class);
			parent.startActivity(intent);
		} 

	} 
	
	/**
	 * 实现这个方法来处理触摸屏移动事件。
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		synchronized (getHolder()) {
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(!mGameModel.getLost()){
					if (event.getY() > 0 && event.getY() < touchy) {
						if (mGameModel.getDront().moveDrontDOWN())
							mSoundManager.playSound(0);									
					}
					else if (event.getY() > touchy) {
						if (mGameModel.getDront().moveDrontUP())
							mSoundManager.playSound(0);							
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				break; 
			case MotionEvent.ACTION_UP:	
				break;
			}		
		}
		return true;
	}
	
	/**
	 * 在surface的大小发生改变时触发
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	} 
	
	/**
	 * 在创建时激发，一般在这里调用画图的线程
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		if (!thread.isAlive()) {
			thread = new GamePreviewThread(this,mGameModel);			
		}
		thread.setRunning(true);
		thread.start();		
	}
	/**
	 * 销毁时激发，一般在这里将画图的线程停止、释放。
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// we will try it again and again...
			}
		}	
		mBitMapCache = new HashMap<Integer, Bitmap>();
	}


}
