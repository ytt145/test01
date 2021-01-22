package semiproject.editor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.*;


public class Server extends Thread{
	
	Socket sock;
	
	public Server(Socket sock) {
		this.sock = sock;
	}
	static int count = 0; // wlist�� �ӽ� �ּ� �ߺ� ���ִ� �뵵
	
	static ArrayList<DataOutputStream> AllchatDos = new ArrayList<>(); //��ü ä���� ����鿡�� ��� ä���� �ϰڴٴ� ��ȣ �߼� //1��
	static ArrayList<BufferedWriter> chatList = new ArrayList<BufferedWriter>(); //��ü ä���̿��ڵ��� �������
	static HashMap<String, BufferedWriter> chatWlist = new HashMap<>(); // �ӼӸ�<�ּ�(�г���),����> 
	static String nickname = "�͸�"; //Ŭ���̾�Ʈ�� �����Ҷ� ������ �����ϴ� �г��� ����
	
	
	//nickDos�� ���
	//ä�� : �ӼӸ��� ��󿡰� �ӼӸ��� �ϰڴٴ� ��ȣ �߼�
	//���� : ������ ��󿡰� ��ȣ �߼�
	static HashMap<String, DataOutputStream> nickDos = new HashMap<>(); 
	
	static HashMap<String, ObjectOutputStream> gameList = new HashMap<>(); // ������ ��뿡�� �� �ڵ带 �����ϴ� ����
	static HashMap<String, String> myCounterpart = new HashMap<String, String>(); // ������ ��븦 ����. Ű���� �� ip, val���� ��� �г��� ����. gameList����� �� myCounterpart.get(ip)�� ��븦 String���� �޾� gameList.<���>�� ObjectOutputStream ���� ��� ���� 
	
	
	static class Chat{
		
		//1�� ��üä��
		static void allUser(String nickname, String msg) throws Exception{
			for(int i = 0; i < chatList.size(); i++) { // list : bw���� �ѵ� �𿩼�
				DataOutputStream dos = AllchatDos.get(i);
				BufferedWriter bw = chatList.get(i);
				try {
					int send = 1;
					dos.writeInt(send);
					
					
					bw.write(nickname + ">>" + msg); // ��� bw�� ä���� ���ִ� ��
					bw.newLine();
					bw.flush();
				}catch(IOException e) {
					AllchatDos.remove(dos);
					chatList.remove(bw);
				}
			}
			
		}
		
		
		static boolean whisperBoo = false; //���濡�� �ӼӸ��� ����.(�� : �ӼӸ� ����� ���ų� �߸��Է� �Ǽ� wlist.get(key) = null;)
		
		//2�� �ӼӸ�
		static void whisper(String address, String ip, String msg2) throws Exception{
			try {
				DataOutputStream dos = nickDos.get(address);
				BufferedWriter bw = chatWlist.get(address); 
				//�г��� ġ�� �ش� �г����� ������ΰ� ���� 
				//�ӼӸ� ������ ����� �޴� ������� ���̴� ä�� ����
				
				int send = 2;
				dos.writeInt(send);
				
				
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
					whisperBoo = false;
				}
				bw.flush();    //  /w �߸��Է��� ä��
			} catch(NullPointerException e) {
				whisperBoo = true;
				System.out.println(ip + "�� �������� �ʴ� �ӼӸ� ����� �����Ͽ����ϴ�");
			} catch(IOException e) {
				chatWlist.remove(address);
			}
		}
		
	}


	static class Game{
		//101��
		static void codeSend(String myCounter, String ip, Object obj) throws Exception{
			try {
				DataOutputStream dos = nickDos.get(myCounter);
				ObjectOutputStream oos = gameList.get(myCounter); 
				
				int send = 101;
				dos.writeInt(101);
				
				oos.writeObject(obj); 
				oos.flush();  
				
			} catch(NullPointerException e) {
				System.out.println(ip + "�� �������� �ʴ� ������ �����Ͽ����ϴ�");
			} catch(IOException e) {
				gameList.remove(myCounter);
			}
		}
	
	}
	
	
	@Override
	public void run() {
		InetAddress inet = sock.getInetAddress();
		String ip = null;
	
		//�⺻ Ʋ
		InputStream is = null; 
		OutputStream os = null; 
		
		// �ڷḦ �ְ� ������ �Ҷ� � ������ ���� �� ������ ����.
		DataInputStream dis = null; 
		DataOutputStream dos = null;
		
		//ä���� ���
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		OutputStreamWriter osw = null;
		BufferedWriter  bw = null;
		
		
		//������ ���
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		
		BufferedOutputStream bos = null;
		ObjectOutputStream oos = null;
		
		
		
		ip = inet.getHostAddress() + count;
		System.out.println(ip + " ����");
		try {
			is = sock.getInputStream();
			System.out.println("����0");
			dis = new DataInputStream(is);
			System.out.println("����1");
			
			os = sock.getOutputStream(); //����� ���Ͽ� �ƿ�ǲ��Ʈ��(������ ����)
			dos = new DataOutputStream(os);
			System.out.println("����2");
			
			
			AllchatDos.add(dos);//��ü �����ڿ��� ����
			
//			wlist.put(�г���, bw);
			
			
			// 1�� ��üä��
			// 2�� �ӼӸ�
			// Ŭ���̾�Ʈ���� �̸� ä�� ĥ�� /���� /w �� �ӼӸ� ǥ�õǾ����� indexOf�� Ȯ���ϰ� -1�̸� �ӼӸ��� 2�� dataOuputStream���� ������, -1�� �ƴϸ� 1�� ����
			
			// 98�� �� �г��� ����
			// 99�� ���� ������ �г��� ���� <------------------------101���� ���� �ּҸ� �ִ� �뵵����. �׷��� �׳� �޾Ƽ� String���� ����
			// 101�� ���� �ڵ�����
			
			int num = -1;
			num = dis.readInt();
			System.out.println(num);	
			
//			System.out.println("num = " + num);
			
			
			
			if(num == -1) {
				System.out.println(ip + " : �߸��� ��û");
			}else if(num < 100) {
				System.out.print(ip + " : ä�ð��� �Ǵ� �г��� ���� ��û");
				
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				
				osw = new OutputStreamWriter(os);
				bw = new BufferedWriter(osw);

				chatList.add(bw); // ��ü ä��
				chatWlist.put(ip,bw); // �ӼӸ� ���(ip�� �Է��� �� �ִ�)
				
				
				String msg = null;
				while((msg = br.readLine()) != null) {
					
				// 1�� ��üä�� �޼��� ����
					if(num == 1) {
						Chat.allUser(ip, msg);
						
				// 2�� �ӼӸ� �޼��� ����
					}else if(num == 2) {
						 //�ӼӸ� ǥ�÷� ���꽺Ʈ�� ����
//						int isWhisper = msg.indexOf("/w ");
//						int isWhisper2 = msg.indexOf("/�� ");
					
						int last = msg.indexOf(' ', 4);
							
						String address = new String();
						try {
							address = msg.substring(3,last); 
						}catch(StringIndexOutOfBoundsException e) {
							System.out.println(ip + "�� �ӼӸ� ������ �߸� �Է��߽��ϴ�");
						}
							
	//					System.out.println(address); ���꽺Ʈ�� �� �߶����� Ȯ����
						String msg2 = msg.substring(last+1,msg.length()); //ä��
						Chat.whisper(address, ip, msg2); //���� ä�ÿ� ����
						Chat.whisper(ip,ip,msg2); //���� �Ӹ� ���� ���� �� ä�ÿ� ����
				// �� �г������� ���� ���� ��� ����
					}else if(num == 98) {
						System.out.println(ip + "���� �г��� ���� ���� ��û");
						nickDos.put(msg,dos); // ä�� �ӼӸ��̶�� ������ ����
						chatWlist.put(msg,bw); // �г������� �ӼӸ��� �ǵ��� ����
					}else if(num == 99) {
						System.out.println(ip + "���ӿ��� ����� �г���" + msg +"����");
						myCounterpart.put(ip, msg);
					}
				
				}
		
			}else { // 101�� �� �ڵ带 ��뿡�� ����
				System.out.println(ip + "���� ���� ��û");
				
				bis = new BufferedInputStream(is);
				ois = new ObjectInputStream(bis);
				
				gameList.put(ip,oos); // ���� ��� ������Ʈ ������� ����
				
				String myCounter = myCounterpart.get(ip); //99������ �����ص� ��� String���� ����
				Object obj = null;
				while((obj = ois.readObject()) != null) {
					Game.codeSend(myCounter, ip, obj);  // ������ ������Ʈ ������η� object ����
				}
				
			}
			
		} catch (IOException e) {
			System.out.println(ip + "���� ����");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(oos != null) {oos.close();}
				if(bw != null) {bw.close();}
				if(bos != null) {bos.close();}
				if(osw != null) {osw.close();}
				if(dos != null) {dos.close();}
				if(os != null) {os.close();}
				
				if(ois != null) {ois.close();}
				if(bis != null) {bis.close();}
				if(br != null) {br.close();}
				if(isr != null) {isr.close();}
				if(dis != null) {dis.close();}
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
