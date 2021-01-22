package serverr;

import java.io.*;
import java.net.*;
import java.util.*;


public class SocketServer {
	
	ArrayList<SocketThread> vt = null;
	
	ServerSocket serverSocket = null;
	
	public SocketServer() {
		String ip = null;
		try {
			serverSocket = new ServerSocket(5000);
			
			while(true) {
				Socket socket = serverSocket.accept();
				InetAddress ia = socket.getInetAddress();
				ip = ia.getHostAddress();
				System.out.println(ip + " ��û�� ����");
				SocketThread st = new SocketThread(socket);
				Thread t1 = new Thread(st);
				t1.start();
				vt.add(st);
			}
			
		} catch (IOException e) {
			System.out.println(ip +" ���� ����");
		}
		
	}
	
	
	
	public static void main(String[] args) {
		
		new SocketServer();
		
		
		
	}
	
	
	class SocketThread extends Thread{
		OutputStream os = null;
		OutputStreamWriter osw = null;
		PrintWriter pw = null;
		BufferedWriter bw = null;
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		String msg = null;
		Socket sock =null;
		String id = "�͸�";
		
		public SocketThread(Socket sock) {
			this.sock = sock;
		}
		
		@Override
		public void run() {
			try {
				os = sock.getOutputStream();
				osw = new OutputStreamWriter(os);
				pw = new PrintWriter(osw);
				
				pw.println("ȯ���մϴ�");
				pw.println("id�� �Է��ϼ���");
				pw.println("id�� �Է����� ������ �͸�ó���˴ϴ�");
				pw.println("ex) id:ssar");
				
				
				
				is = sock.getInputStream();
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				
				while((msg = br.readLine()) != null) {
					
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if(br != null) {br.close();}
					if(isr != null) {isr.close();}
					if(is != null) {is.close();}
					
					if(pw != null) {pw.close();}
					if(osw != null) {osw.close();}
					if(os != null) {os.close();}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		void routine(String data){
			int index = data.indexOf(':');
			String gubun = null;
			String result = null;
			if(index == -1) {  //�͸�
				gubun = data.substring(0,index);
				result = data.substring(index+1);
				
				if(gubun.equals("id")) {
					id = result;
				}
			}else {
				int index2 = data.indexOf('/');  
				
				if(index2 != -1) {  
					gubun = data.substring(0,index2); 
					result = data.substring(index2+1);
					for(int i = 0; i < vt.size(); i++) {
						if(vt.get(i).id.equals(gubun)) {
							vt.get(i).pw.println(id + ":" + result); //�̸��ް�
						} 
					}
				}else { //��üä���̸�
					for(int i = 0; i < vt.size(); i++) {
						vt.get(i).pw.println(id + ":" + msg); 
					}
				}
				
			}
			
		}
		
		
	}
	
}


