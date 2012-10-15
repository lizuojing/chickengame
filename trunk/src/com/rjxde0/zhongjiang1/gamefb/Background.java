package com.rjxde0.zhongjiang1.gamefb;

import com.rjxde4.zhongjiang1.R;


public class Background {
	private int xPos = 0;
	private int dif = 0;
	private int speed;
	private int temp;
	
	private boolean x1first = true;
	
	public Background(float scale_x){
		speed = (int) (23 * scale_x);
	}
	
	public int getImage(){
		return R.drawable.zt_bg;
	}
	
	
	public int getX1(int width){		
		dif = width;
		return xPos;
	}
		
	public boolean getFirst(){
		return x1first;
	}
	
	public void setXPos(float timeDelta){
		temp = Double.valueOf(timeDelta * speed).intValue();
		xPos -= temp;
		
		if(xPos <= -dif) {
			xPos = 0;
			if (x1first)
				x1first = false;
			else x1first = true;
		}
	}
}
