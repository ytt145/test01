package serverr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {

	Socket socket = null;
	String msg = null;
	PrintWriter pw = null;
	BufferedReader br = null;
	
	public SocketClient() {
		try {
			socket = new Socket("localHost",5000);
			new SocketThread().start();
			pw = new PrintWriter(socket.getOutputStream(),true);
			br = new BufferedReader(new InputStreamReader(System.in));
			
			//���ν����� ���� - ���� ��
			while((msg = br.readLine()) !=null) {
				System.out.println(msg);
				pw.println(msg);
				pw.flush();
			}
			
			pw.close();
			br.close();
			socket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	//Ÿ�� - �޼����� �޴� ������
	class SocketThread extends Thread{
		
		String myMsg = null;
		BufferedReader br = null;
		@Override
		public void run() {
			try {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				while((myMsg = br.readLine()) !=null){
					System.out.println(myMsg);
				}
				
				br.close();				
				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
	public static void main(String[] args) {
		new SocketClient();
		

	}

}

