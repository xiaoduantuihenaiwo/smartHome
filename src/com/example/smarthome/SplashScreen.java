package com.example.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashScreen extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);
		Thread timer = new Thread(){
			public void run(){
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				}finally{
					Intent openLoginIntent = new Intent("com.example.smarthome.LOGIN");
					startActivity(openLoginIntent);
					finish();
				}
			}
		};
		timer.start();
	}
}
