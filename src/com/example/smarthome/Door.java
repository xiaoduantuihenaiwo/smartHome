package com.example.smarthome;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class Door extends Activity{
	long exitTime = 0; 
	TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.door);
		textView = (TextView)findViewById(R.id.door_state_text);
		init();
	}
	
	public void init(){
		if(Config.DOOR_STATUAS == true){
			textView.setText("开");
		}
		else{
			textView.setText("关");
		}
	}
	
	public void backClick(View v) {
		finish();
	}
	
	public void doorButtonClick(View v) {
		if(Config.DOOR_STATUAS == false){
			textView.setText("开");
			Config.DOOR_STATUAS = true;
		}
		else{
			textView.setText("关");
			Config.DOOR_STATUAS = false;
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if((System.currentTimeMillis() - exitTime) > 2000)
			{
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			}else{
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
