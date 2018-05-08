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
            	/*������̨service����, ������������*/
            	MainActivity.this.startService(new Intent(MainActivity.this, ServiceSocket.class));     	
            	/*���ý��պ�̨�Ĺ㲥��Ϣ*/
        		receiver = new BrdcstReceiver();  
                IntentFilter filter = new IntentFilter();  
                filter.addAction("android.intent.action.MY_RECEIVER");                   
                registerReceiver(receiver, filter); //ע��  
            	
                /*���ӷ�����*/
            	int  port = Integer.parseInt(conPort);        	    
        		try {
        			Socket socket = new Socket(conIP, port);
        			/**
        			 * ��������,������,�����
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
        //Toast.makeText(MainActivity.this, "�ŵ���¼�", 0).show();
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
		 * ��ʵ�ʿ�����LayoutInflater����໹�Ƿǳ����õģ�
		 * ��������������findViewById()��
		 * ��ͬ����LayoutInflater��������res/layout/�µ�xml�����ļ�������ʵ������
		 * ��findViewById()����xml�����ļ��µľ���widget�ؼ�(�� Button��TextView��)��
		 * �������ã�
		 * 1������һ��û�б����������Ҫ��̬����Ľ��棬����Ҫʹ��LayoutInflater.inflate()�����룻
		 * 2������һ���Ѿ�����Ľ��棬�Ϳ���ʹ��Activiyt.findViewById()������������еĽ���Ԫ�ء�
		 */
		LayoutInflater factory = LayoutInflater.from(MainActivity.this);
		final View v1=factory.inflate(R.layout.config_input_layout,null);
		AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
		
		dialog.setTitle("������������");
		dialog.setView(v1);
		final EditText editTextIp = (EditText)v1.findViewById(R.id.connectionurl);
		final EditText editTextPort = (EditText)v1.findViewById(R.id.controlurl);
		editTextIp.setText(conIP);		//��ʼֵ
		editTextPort.setText(conPort);	//��ʼֵ
    	
        dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	conIP   = editTextIp.getText().toString();
            	conPort = editTextPort.getText().toString();
            	Toast.makeText(MainActivity.this, "���óɹ���", Toast.LENGTH_SHORT).show(); 
            }
        });
        dialog.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
        dialog.show();
    }
    
    public void exitClick(View v){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this)
    		.setTitle("�˳��Ի���")
    		.setIcon(R.drawable.ic_launcher)
    		.setMessage("ȷ��Ҫ�˳���")
    		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					finish();
				}
			})
			.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
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
		unregisterReceiver(receiver); //  ע��
		this.stopService(new Intent(this, ServiceSocket.class));// ֹͣservice  
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void updateUI(){
    	//InfoShower.showInfo("main", getApplicationContext());
		// �����Ž�״̬
		if (Config.DOOR_STATUAS) {
			tvMainDoor.setText("״̬����");
		} else {
			tvMainDoor.setText("״̬����");
		}
		
		if (Config.AIRCONDITION_STATUS) {
			tvMainAir.setText("״̬����");
		} else {
			tvMainAir.setText("״̬����");
		}
		// ���´���״̬
		if (Config.CURTAIN_STATUS) {
			tvMainCurtain.setText("״̬����");
		} else {
			tvMainCurtain.setText("״̬����");
		}
		// ���·����״̬
		if (Config.ROOM_LIGHT_STATUS) {
			tvMainLamp.setText("������");
		} else {
			tvMainLamp.setText("������");
		}
		// ���¿�����״̬
		if (Config.CUSTOMERRIGHT_STATUS) {
			//mainLLamp.setText("������");
		} else {
			//mainLLamp.setText("������");
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
    
    
    
    /**************�㲥:���պ�̨��service���͵Ĺ㲥******************/
    private class BrdcstReceiver extends BroadcastReceiver {  		  
        @Override  
        public void onReceive(Context context, Intent intent) {  
//            Bundle bundle = intent.getExtras();
            String stringValue=intent.getStringExtra("strRecvMsg");            
        	//Log.v("mainactivity", "onReceive" + stringValue);
            //   ���͵Ĳ�����(windowsƽ̨) , �����linuxƽ̨��ȥ��ǰ���ĸ���
        	//....#SERVERSDATA#12C#35%#
        	//....#SERVERSIGN#A1#B1#C0#D0#E1#F0#
        	//....#SERVERSIGN#A0#B0#C0#D0#E1#F0#
            //....#SERVERSIGN#A1#B1#C1#D1#E1#F0#
            //....#SERVERSDATA#42C#41%#
            //....#SERVERSIGN#A0#B0#C0#D0#E0#F0#
            //....#SERVERSIGN#B#ON#
        	if(stringValue.startsWith("#SERVERSDATA#")){// ��ʪ��
	    		String sub = stringValue.substring(stringValue.indexOf('#'), stringValue.lastIndexOf('#') + 1);
				String[] strs = sub.split("#");
    			//mainAirTemper.setText("�£�" + strs[2]);
    			//mainAirHum.setText("ʪ��" + strs[3]);
        	}else{        		
        		updateUI();        		     		
        	}
        	
        }
    } 
    
    
    
}
