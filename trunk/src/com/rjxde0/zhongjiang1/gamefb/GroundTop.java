package com.rjxde0.zhongjiang1.gamefb;

public class GroundTop {
	private int xPos;
	private int yPos;
	private int speed;
	private int dif = 600;
	
	public GroundTop(float scale_x, float scale_y){
		xPos = 0;
		yPos = (int) (100 * scale_y);
		speed = (int) (100 * scale_x);
		dif = (int) (dif * scale_x);
	}
	
	public void setSpeed(double newspeed){
		speed = Double.valueOf(newspeed).intValue();
	}
	
	public int getX1(){
		return xPos;
	}
	
	public int getX2(int new_width){
		return xPos + new_width;
	}
	
	public int getY(){
		return yPos;
	}
	
	public void setXPos(float timeDelta){
		xPos -= timeDelta * speed;
		if(xPos < -dif)
			xPos = 0;
	}
}
