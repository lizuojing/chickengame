package com.rjxde0.zhongjiang1.game;

/*
 *   All sounds needs to be added to res/raw/sound.mp3 with
 *   their extension intact. They're loaded and cached within
 *   the constructor of the class.
 */

import com.rjxde4.zhongjiang1.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.SystemClock;
import android.util.Log;

public class SoundManager {

	private SoundPool mSoundPool;
	private int[] mSoundArray     = new int[64];
	private float[] mSoundPitch     = new float[64];
	private long[] mSoundDelay     = new long[64];
	private long[] mSoundDelayLast = new long[64];
	private AudioManager mAudioManager;
	private Context mContext;
	private int currIndex = 0;
    public boolean playSound = true; 
    
    final int SOUND_TOWER_SHOT1   = 0;
    final int SOUND_TOWER_SHOT2   = 1;
    final int SOUND_CREATURE_DEAD = 10;
    final int SOUND_TOWER_BUILD   = 20;

	public SoundManager(Context baseContext) {
        this.initSounds(baseContext);
        this.addSound(1.0f, 500, R.raw.jump); 
        this.addSound(1.0f, 500, R.raw.crash); 
        this.addSound(1.0f, 500, R.raw.egg); 
        this.addSound(1.0f, 500, R.raw.stop); 
        this.addSound(1.0f, 500, R.raw.time); 
	}

	/**
	 * Called by the constructor to pre-cache all sound files and save in an array.
	 */
	private void initSounds(Context context) { 
		mContext = context;
	    mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	    mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
	} 

    /**
	 * Loads a sound-file into our cache and prepares it for being played
	 * by the playSound or playLoopedSound-functions.
	 */
	public int addSound(float pitch, long delay, int soundId) {
		if (currIndex >= mSoundArray.length) {
			return -1;
		} else {
			mSoundArray[currIndex] = mSoundPool.load(mContext, soundId, 1);
			mSoundPitch[currIndex] = pitch;
			mSoundDelay[currIndex] = delay;
			currIndex++;
			return (currIndex-1);
		}
	}

	/**
	 * Plays a sound from the array of sounds cached.
	 */
	public void playSound(int index) {
		final long time = SystemClock.uptimeMillis();
		if (playSound && mSoundDelay[index] + mSoundDelayLast[index] < time) {
			mSoundDelayLast[index] = time;
			int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mSoundPool.play(mSoundArray[index], streamVolume, streamVolume, 1, 0, mSoundPitch[index]) == 0) {
			}
		}
	}

	/**
	 * Plays a sound in a loop, e.g. background music or similar.
	 */
	public void playLoopedSound(int index) {
		 int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
		if (mSoundPool.play(mSoundArray[index], streamVolume, streamVolume, 1,-1, mSoundPitch[index]) == 0) {
			Log.d("SOUNDMANAGER", "Failed to play " + index);
		}
	}

	/**
	 * Stops playing a sound or looped sound.
	 */
	public void stopSound(int index) {
		mSoundPool.stop(mSoundArray[index]);
	}

	/**
	 * Releases all cached soundfiles and returns their resources.
	 * It's a good idea to call this when done using SoundManager
	 * to preserve memory/CPU.
	 */
	public void release() {
		mSoundPool.release();
	}

}