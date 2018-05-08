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
 * �ͷ�����ͨ�ŵ���
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
			@SuppressWarnings("static-access") //����ע�������Ǹ�������һ��ָ��������Ա���ע�Ĵ���Ԫ���ڲ���ĳЩ���汣�־�Ĭ��
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BufferedReader mBufferedReader = null;
				/*����intent , �����ܹ�֪ͨ�ϲ�Ӧ��*/
				Intent intent = new Intent();
				//intent.setAction("android.intent.action.")
				
				//����������ݵ�buffer,������
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
				//��ȡ���롢�����
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
				 * ��ȡ��ǰ������״̬���ͱ���  #SERVERSIGN#A#B#B#C#D#E#F
				 * �״ν��ձ�����������: #SERVERSIGN#A1#B1#C0#D0#E1#F0
				 */
			}
		}).start();
	}

	
}
