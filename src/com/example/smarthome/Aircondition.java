package com.example.smarthome;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class Aircondition extends Activity{
	long exitTime = 0; 
	TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aircondition);
		
		textView = (TextView)findViewById(R.id.air_text);
		init();
	}
	
	public void init(){
		if(Config.AIRCONDITION_STATUS == true){
			textView.setText("��");
		}
		else{
			textView.setText("��");
		}
	}
	
	public void backClick(View v) {
		finish();
	}
	
	public void airButtonClick(View v) {
		if(Config.AIRCONDITION_STATUS == false){
			textView.setText("��");
			Config.AIRCONDITION_STATUS = true;
		}
		else{
			textView.setText("��");
			Config.AIRCONDITION_STATUS = false;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if((System.currentTimeMillis() - exitTime) > 2000)
			{
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
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
