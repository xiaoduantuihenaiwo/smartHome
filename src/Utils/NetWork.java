package Utils;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 
 * @author Administrator
 * 存放socket通讯相关数据
 */
public class NetWork {

	public static Socket socket = null;
	public static PrintWriter out = null;//发送数据
	public static BufferedReader br = null;//接收数据
}
