package semiproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.*;


public class ChatServer extends Thread{
	
	Socket sock;
	
	static ArrayList<BufferedWriter> list = new ArrayList<BufferedWriter>(); //��ü ä��
	
	//bw : ����� ���Ͽ� ������ ���� �ϴ°�

	static HashMap<String, BufferedWriter> wlist = new HashMap<>(); // �ӼӸ�<�ּ�(�г���),����> 
	
	static int count = 0; // wlist�� �ӽ� �ּ� �ߺ� ���ִ� �뵵 
	
	boolean whisperBoo = false; //���濡�� �ӼӸ��� ����.(�� : �ӼӸ� ����� ���ų� �߸��Է� �Ǽ� wlist.get(key) = null;)
	
	public ChatServer(Socket sock) {
		this.sock = sock;
	}
	
	String nickname = "�͸�"; //Ŭ���̾�Ʈ�� �����Ҷ� ������ �����ϴ� �г��� ����
	
	void allUser(String nickname, String msg) throws Exception{
		for(int i = 0; i < list.size(); i++) { // list : bw���� �ѵ� �𿩼�
			BufferedWriter bw = list.get(i);
			try {
				bw.write(nickname + ">>" + msg); // ��� bw�� ä���� ���ִ� ��
				bw.newLine();
				bw.flush();
			}catch(IOException e) {
				list.remove(bw);
			}
		}
		
	}
	/*
	����1 ����2
	wlist.(����1,�������); ip,�г���
	wlist.(����2,�������); ip,�г���
	����1�� ����2 ���� �ӼӸ� ����
	(����2,����1,msg2)
	(����1,����1,msg2)
	*/
	
	void whisper(String address, String ip, String msg2) throws Exception{
		try {
			BufferedWriter bw = wlist.get(address); 
			
			//�г��� ġ�� �ش� �г����� ������ΰ� ���� 
			//�ӼӸ� ������ ����� �޴� ������� ���̴� ä�� ����
			if(address.equals(ip)) { 
				bw.write("(�ӼӸ��� ����)" + nickname + ">>" + msg2);
			}else {
				bw.write("(�ӼӸ�)" + nickname + ">>" + msg2); //���濡�� ������ ��
			}
			
			bw.newLine();
			//�ӼӸ� ����� ���� ��� ������ �õ��ߴ� ����� ä��â�� �˸�
			
			if(address.equals(ip) && whisperBoo == true) { 
				bw.write("�ӼӸ� ����� �����ϴ�");
				bw.newLine();
				bw.flush();
				whisperBoo = false;
			}
			bw.flush();    //  /w �߸��Է��� ä��
		} catch(NullPointerException e) {
			whisperBoo = true;
			System.out.println(ip + "�� �������� �ʴ� �ӼӸ� ����� �����Ͽ����ϴ�");
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
			
			os = sock.getOutputStream(); //����� ���Ͽ� �ƿ�ǲ��Ʈ��(������ ����)
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
	
			list.add(bw);
			wlist.put(ip,bw); 
//			wlist.put(�г���, bw);
			
			System.out.println("���� ���� ���� : " + wlist.size());
			String msg = null;
			while((msg = br.readLine()) != null) {
				int isnickname = msg.indexOf("!@#$%�г���nickname%$#@!");
				if(isnickname != -1) {
					nickname = msg.substring(0,isnickname);
					wlist.put(nickname,bw); 
				}else {
					int isWhisper = msg.indexOf("/w "); //�� ���� �ִ� ��ġ ������ -1
					int isWhisper2 = msg.indexOf("/�� ");
					
					if(isWhisper != -1 || isWhisper2 != -1) {
	//					System.out.println("�ӼӸ� ���� ��� �۵��մϴ�"); ���� �Ǵ��� Ȯ����										
						int last = msg.indexOf(' ', 4);
						
						String address = new String();
						try {
							address = msg.substring(3,last); 
						}catch(StringIndexOutOfBoundsException e) {
							System.out.println(ip + "�� �ӼӸ� ������ �߸� �Է��߽��ϴ�");
						}
							
	//					System.out.println(address); ���꽺Ʈ�� �� �߶����� Ȯ����
						String msg2 = msg.substring(last+1,msg.length()); //ä��
						whisper(address, ip, msg2); //���� ä�ÿ� ����
						whisper(ip,ip,msg2); //���� �Ӹ� ���� ���� �� ä�ÿ� ����
					}else {
						allUser(nickname, msg); // ��üä��
					} 
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
				System.out.println("ä�� ���� ���� �����");
//				new Server(serv.accept()).start();
				count++;
				Socket sock = serv.accept(); //����(Ŭ���̾�Ʈ)�� �������Ͽ� ����
				ChatServer clinet = new ChatServer(sock);
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
