package com.example.smarthome;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Lamp extends Activity{
	long exitTime = 0; 
	Button button;
	TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lamp);
		textView = (TextView)findViewById(R.id.lamp_state_text);
		button =(Button)findViewById(R.id.lamp_button);
		init();
	}
	
	public void init(){
		if(Config.ROOM_LIGHT_STATUS == true){
			textView.setText("开");
			button.setBackgroundResource(R.drawable.room_lamp_on);
		}
		else{
			textView.setText("关");
			button.setBackgroundResource(R.drawable.room_lamp_on);
		}
	}
	
	public void backClick(View v) {
		finish();
	}
	
	public void lampButtonClick(View v) {
		if(Config.ROOM_LIGHT_STATUS == false){
			textView.setText("开");
			Config.ROOM_LIGHT_STATUS = true;
			button.setBackgroundResource(R.drawable.room_lamp_on);
			
		}
		else{
			textView.setText("关");
			Config.ROOM_LIGHT_STATUS = false;
			button.setBackgroundResource(R.drawable.room_lamp_off);
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