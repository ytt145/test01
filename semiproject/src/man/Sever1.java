package man;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Sever1 extends Thread{
	final static int port = 5000;
	Socket sock;
	
	public Sever1(Socket sock) {
		this.sock = sock;
	}
	
	static ArrayList<BufferedWriter> list = new ArrayList<>();
	
	@Override
	public void run() {
		InetAddress inet = sock.getInetAddress();
		String ip = inet.getHostAddress();
		System.out.println(ip + "¿‘¿Â");
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		try {
			is = sock.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			os = sock.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			
			list.add(bw);
			String msg = null;
			while((msg = br.readLine()) != null)){
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public static void main(String[] args) {
		ServerSocket serv = null;
		Socket sock = null;
		
		try {
			serv = new ServerSocket(port);
			
			sock = new Socket();
			new Sever1(serv.accept()).start();
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
