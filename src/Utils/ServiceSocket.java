package Utils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore.PrivateKeyEntry;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * 和服务器通信的类
 * @author Administrator
 * 
 */
public class ServiceSocket extends Service{

	private boolean threadDisable = false;
	ProtocolParser parser = new ProtocolParser();
	
	Handler handler = null;
	private int i_time_out = 5000;
	private Socket mSocketClient = null;
	private PrintWriter out = null;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
	}
	
	void serviceSocket(){
		new Thread(new Runnable() {
			@SuppressWarnings("static-access") //该批注的作用是给编译器一条指令，告诉它对被批注的代码元素内部的某些警告保持静默。
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BufferedReader mBufferedReader = null;
				/*设置intent , 让其能够通知上层应用*/
				Intent intent = new Intent();
				//intent.setAction("android.intent.action.")
				
				//定义接收数据的buffer,并清零
				char[] buffer = new char[64];
				for (int i = 0; i < buffer.length; i++) {
					buffer[i] = '\0';
				}
				
				try {
					while(NetWork.socket ==  null){
						if((NetWork.socket != null) && (NetWork.socket.isConnected()))
						{
							break;
						}
						Thread.sleep(1000);
						if(threadDisable)
						{
							return;
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				mSocketClient = NetWork.socket;
				//获取输入、输出流
				if((mSocketClient != null) && (mSocketClient.isConnected())){
					InputStream is = null;
					try {
						is = mSocketClient.getInputStream();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					mBufferedReader = new BufferedReader(new InputStreamReader(is));
				}
				out = NetWork.out;
				
				/**
				 * 获取当前的所有状态发送编码  #SERVERSIGN#A#B#B#C#D#E#F
				 * 首次接收编码大概类似于: #SERVERSIGN#A1#B1#C0#D0#E1#F0
				 */
			}
		}).start();
	}

	
}
