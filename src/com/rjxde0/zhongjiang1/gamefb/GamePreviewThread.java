package com.rjxde0.zhongjiang1.gamefb;

import android.graphics.Canvas;
import android.os.SystemClock;
/**
 * Thread class to perform the so called "game loop".
 * 
 */
class GamePreviewThread extends Thread {
	private GamePreview mGamePreview;
	private boolean mRunThread = false;
	private long mLastTime;
	GameModel mGameModel;

	public GamePreviewThread(GamePreview panel, GameModel model) {
		mGamePreview = panel;
		mLastTime = SystemClock.uptimeMillis();
		mGameModel = model;
	}

	public void setRunning(boolean run) {
		mRunThread = run;
	}

	public boolean isRunning() {
		return mRunThread;
	}

	@Override
	public void run() {
		Canvas c = null;
		mLastTime = SystemClock.uptimeMillis();

		while (mRunThread) {
			final long time = SystemClock.uptimeMillis();
			final long timeDelta = time - mLastTime;
			long finalDelta = timeDelta;

			if (timeDelta > 12) {
				float secondsDelta = (time - mLastTime) * 0.001f;
				if (secondsDelta > 0.1f) {
					secondsDelta = 0.1f;
				}
				mLastTime = time;
				mGameModel.updateModel(secondsDelta);
			}
			
			final long endTime = SystemClock.uptimeMillis();
            finalDelta = endTime - time;
            
			try {
				c = mGamePreview.getHolder().lockCanvas(null);
				synchronized (mGamePreview.getHolder()) {
					mGamePreview.onDraw(c);
				}
			} finally {
				if (c != null) {
					mGamePreview.getHolder().unlockCanvasAndPost(c);
				}
			}
            
            
            // If the game logic completed in less than 16ms, that means it's running
            // faster than 60fps, which is our target frame rate.  In that case we should
            // yield to the rendering thread, at least for the remaining frame.
           
            if (finalDelta < 16) {
                try {
                    Thread.sleep(16 - finalDelta);
                } catch (InterruptedException e) {
                    // Interruptions here are no big deal.
                }
            }
			
		}
	}
}