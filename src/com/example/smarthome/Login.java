package com.example.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity{
	long exitTime = 0; 
	public static String filenameString = "MySharedString";
	SharedPreferences someData;
	EditText userName,passWord;
	Button loginButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);		
		initVars();
		initSharedPreferences();
	}
	
	public void initSharedPreferences(){
		SharedPreferences.Editor editor = someData.edit();
		editor.clear();
		editor.putString("username", "admin");
		editor.putString("password", "admin");
		editor.commit();
	}
	
	public void initVars(){
		userName = (EditText)findViewById(R.id.etUser);
		passWord = (EditText)findViewById(R.id.etPass);	
		loginButton = (Button)findViewById(R.id.login_button);
		loginButton.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					loginButton.setBackgroundColor(Color.GREEN);
				}

				return false;
			}
		});
		someData = getSharedPreferences("MySharedString",MODE_PRIVATE);
	}
	
	public void onBtnLogin(View v){
		String inputuser = userName.getText().toString();
		String inputpass = passWord.getText().toString();
		
		someData = getSharedPreferences(filenameString, 0);
		String user = someData.getString("username", "Couldn't load data!");
		String pass = someData.getString("password", "nothing");
		
		//if(user.contentEquals(inputuser) && pass.contentEquals(inputpass)){
		if(true){
			try {
				Class ourClass = Class.forName("com.example.smarthome.MainActivity");
				Intent ourIntent = new Intent(Login.this,ourClass);
				startActivity(ourIntent);
			} catch (ClassNotFoundException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		else {
			Toast toast = Toast.makeText(Login.this,
				     "用户名或密码错误，请重新输入。", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	public void onBtnCancel(View v){
		finish();
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
