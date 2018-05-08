package com.example.smarthome;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import Utils.NetWork;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	BroadcastReceiver receiver; 
	long exitTime = 0; 
	
	TextView tvMainDoor,tvMainAir,tvMainCurtain,tvMainLamp;
	String conIP,conPort;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        initView();
    }
    
    public void initView(){
    	tvMainDoor = (TextView)findViewById(R.id.tvMainDoor);
    	tvMainAir = (TextView)findViewById(R.id.tvMainAir);
    	tvMainCurtain = (TextView)findViewById(R.id.tvMainCurtain);
    	tvMainLamp = (TextView)findViewById(R.id.tvMainLamp);
    	conIP = getResources().getString(R.string.ip_value);
    	conPort = getResources().getString(R.string.port_value);
    	
    }
    void connectToServer(){
    	new Thread() {
            public void run() {
            	/*启动后台service服务, 接受网络数据*/
            	MainActivity.this.startService(new Intent(MainActivity.this, ServiceSocket.class));     	
            	/*设置接收后台的广播信息*/
        		receiver = new BrdcstReceiver();  
                IntentFilter filter = new IntentFilter();  
                filter.addAction("android.intent.action.MY_RECEIVER");                   
                registerReceiver(receiver, filter); //注册  
            	
                /*连接服务器*/
            	int  port = Integer.parseInt(conPort);        	    
        		try {
        			Socket socket = new Socket(conIP, port);
        			/**
        			 * 设置网络,输入流,输出流
        			 */
        			NetWork.socket = socket;
        			NetWork.out = new PrintWriter(
        					new BufferedWriter(
        							new OutputStreamWriter(socket.getOutputStream(),"UTF-8")), true);
        			NetWork.br = new BufferedReader(
        			new InputStreamReader(socket.getInputStream(), "UTF-8"));
        		} catch (UnsupportedEncodingException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		                
            }
		}.start();
    }
    
    
    
    
    public void doorClick(View v) {
        //Toast.makeText(MainActivity.this, "门点击事件", 0).show();
    	openChild("Door");
	}
    
    public void airconditionClick(View v){
    	openChild("Aircondition");
    }
    
    public void curtainClick(View v){
    	openChild("Curtain");
    }
    
    public void lampClick(View v){
    	openChild("Lamp");
    }
    
    public void settingClick(View v){
    	/**
		 * 在实际开发中LayoutInflater这个类还是非常有用的，
		 * 它的作用类似于findViewById()。
		 * 不同点是LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化；
		 * 而findViewById()是找xml布局文件下的具体widget控件(如 Button、TextView等)。
		 * 具体作用：
		 * 1、对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入；
		 * 2、对于一个已经载入的界面，就可以使用Activiyt.findViewById()方法来获得其中的界面元素。
		 */
		LayoutInflater factory = LayoutInflater.from(MainActivity.this);
		final View v1=factory.inflate(R.layout.config_input_layout,null);
		AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
		
		dialog.setTitle("网络连接属性");
		dialog.setView(v1);
		final EditText editTextIp = (EditText)v1.findViewById(R.id.connectionurl);
		final EditText editTextPort = (EditText)v1.findViewById(R.id.controlurl);
		editTextIp.setText(conIP);		//初始值
		editTextPort.setText(conPort);	//初始值
    	
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	conIP   = editTextIp.getText().toString();
            	conPort = editTextPort.getText().toString();
            	Toast.makeText(MainActivity.this, "设置成功！", Toast.LENGTH_SHORT).show(); 
            }
        });
        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
        dialog.show();
    }
    
    public void exitClick(View v){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this)
    		.setTitle("退出对话框")
    		.setIcon(R.drawable.ic_launcher)
    		.setMessage("确定要退出吗")
    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					finish();
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			});
    	builder.show();
    }
    
    
    public void openChild(String str)
    {
    	try {
    		Class ourClass = Class.forName("com.example.smarthome."+str);
    		Intent outIntent = new Intent(MainActivity.this,ourClass);
    		startActivity(outIntent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    } 

    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateUI();
	}
    
    
    
    @Override
	protected void onDestroy() {
		unregisterReceiver(receiver); //  注销
		this.stopService(new Intent(this, ServiceSocket.class));// 停止service  
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void updateUI(){
    	//InfoShower.showInfo("main", getApplicationContext());
		// 更新门禁状态
		if (Config.DOOR_STATUAS) {
			tvMainDoor.setText("状态：开");
		} else {
			tvMainDoor.setText("状态：关");
		}
		
		if (Config.AIRCONDITION_STATUS) {
			tvMainAir.setText("状态：开");
		} else {
			tvMainAir.setText("状态：关");
		}
		// 更新窗帘状态
		if (Config.CURTAIN_STATUS) {
			tvMainCurtain.setText("状态：开");
		} else {
			tvMainCurtain.setText("状态：关");
		}
		// 更新房间灯状态
		if (Config.ROOM_LIGHT_STATUS) {
			tvMainLamp.setText("房：亮");
		} else {
			tvMainLamp.setText("房：灭");
		}
		// 更新客厅灯状态
		if (Config.CUSTOMERRIGHT_STATUS) {
			//mainLLamp.setText("厅：亮");
		} else {
			//mainLLamp.setText("厅：灭");
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

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    
    /**************广播:接收后台的service发送的广播******************/
    private class BrdcstReceiver extends BroadcastReceiver {  		  
        @Override  
        public void onReceive(Context context, Intent intent) {  
//            Bundle bundle = intent.getExtras();
            String stringValue=intent.getStringExtra("strRecvMsg");            
        	//Log.v("mainactivity", "onReceive" + stringValue);
            //   发送的测试码(windows平台) , 如果是linux平台请去掉前面四个点
        	//....#SERVERSDATA#12C#35%#
        	//....#SERVERSIGN#A1#B1#C0#D0#E1#F0#
        	//....#SERVERSIGN#A0#B0#C0#D0#E1#F0#
            //....#SERVERSIGN#A1#B1#C1#D1#E1#F0#
            //....#SERVERSDATA#42C#41%#
            //....#SERVERSIGN#A0#B0#C0#D0#E0#F0#
            //....#SERVERSIGN#B#ON#
        	if(stringValue.startsWith("#SERVERSDATA#")){// 温湿度
	    		String sub = stringValue.substring(stringValue.indexOf('#'), stringValue.lastIndexOf('#') + 1);
				String[] strs = sub.split("#");
    			//mainAirTemper.setText("温：" + strs[2]);
    			//mainAirHum.setText("湿：" + strs[3]);
        	}else{        		
        		updateUI();        		     		
        	}
        	
        }
    } 
    
    
    
}
