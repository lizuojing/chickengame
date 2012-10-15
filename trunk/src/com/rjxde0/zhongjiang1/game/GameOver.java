package com.rjxde0.zhongjiang1.game;

import com.rjxde4.zhongjiang1.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class GameOver extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.over);
		
		Button againBtn = (Button)findViewById(R.id.again);
		againBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				GameStart.instance.finish();
				Intent intent = new Intent(GameOver.this, GameStart.class);
				startActivity(intent);
			}
		});
		
		Button endBtn = (Button)findViewById(R.id.end);
		endBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GameStart.instance.finish();
				finish();
			}
		});
	}
	
}



