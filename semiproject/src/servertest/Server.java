package servertest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.*;


public class Server extends Thread{
	
	Socket sock;
	
	static ArrayList<BufferedWriter> list = new ArrayList<BufferedWriter>();
	static HashMap<String, BufferedWriter> wlist = new HashMap<>();
	
	static int count = 0;
	
	public Server(Socket sock) {
		this.sock = sock;
	}
	
	void allUser(String ip, String msg) throws Exception{
		for(int i = 0; i < list.size(); i++) {
			BufferedWriter bw = list.get(i);
			try {
				bw.write(ip + ">>" + msg);
				bw.newLine();
				bw.flush();
			}catch(IOException e) {
				list.remove(bw);
			}
		}
		
	}
	
	void whisper(String address,String ip, String msg2) throws Exception{
		BufferedWriter bw = wlist.get(address);
		try {
			bw.write("(�ӼӸ�)" + ip + ">>" + msg2);
			bw.newLine();
			bw.flush();
		} catch(IOException e) {
			wlist.remove(address);
		}
	}
	
	
	@Override
	public void run() {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		InetAddress inet = sock.getInetAddress();
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter  bw = null;
		String ip = null;
		
		
		ip = inet.getHostAddress() + count;
		
		
		System.out.println(ip + " ����");
		try {
			is = sock.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			os = sock.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
	
			list.add(bw);
			wlist.put(ip,bw);
			System.out.println("���� ������ �� : " + wlist.size());
			String msg = null;
			while((msg = br.readLine()) != null) {
				int isWhisper = msg.indexOf("/w ");
				
				if(isWhisper != -1) {
					int last = msg.indexOf(' ', 3);
					String address = msg.substring(isWhisper+3,last);
					System.out.println(address);
//					System.out.println("�ӼӸ� ��� �۵��մϴ�"); ���Ȯ����
					String msg2 = msg.substring(last+1,msg.length());
					whisper(address, ip, msg2); //���� ä�ÿ� ����
					whisper(ip,ip,msg2); //���� �Ӹ� ���� ���� �� ä�ÿ� ����
				}else {
					allUser(ip, msg);
				}
				
			}
			
			
		} catch (IOException e) {
			System.out.println(ip + "���� ����");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(bw != null) {bw.close();}
				if(osw != null) {osw.close();}
				if(os != null) {os.close();}
				if(br != null) {br.close();}
				if(isr != null) {isr.close();}
				if(is != null) {is.close();}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public static void main(String[] args) {
		ServerSocket serv = null;
		
		try {
			serv = new ServerSocket(5000);
			
			while(true) {
				System.out.println("�����");
//				new Server(serv.accept()).start();
				count++;
				Socket sock = serv.accept();
				Server clinet = new Server(sock);
				clinet.start();
				
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				
				if(serv != null) {serv.close();}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
}
