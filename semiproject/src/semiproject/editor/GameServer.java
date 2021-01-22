package semiproject.editor;

import java.io.*;
import java.net.*;
import java.time.chrono.ChronoZonedDateTime;
import java.util.*;

import javax.swing.text.html.HTMLDocument.Iterator;
import javax.xml.crypto.Data;

// �ڵ� ��ü�� ������ �� ���� ��, �ڵ� �߰�, �����ɶ����� ������ �� ���� ��...

// ���⿣ �ڵ� ��ü�� ������ �� ���� �� ����

// ������ ���� ������ �ڵ带 ���Ӱ� ���ٹް� gridlayout���� �ǽð� ����!

public class GameServer extends Thread implements Serializable{
	Socket sock;
	
	String address;
	String myNickname = new String(); // �� �г������ε� ����ǵ�����
	static HashMap<String, ObjectOutputStream> glist = new HashMap<>(); // �ӼӸ�<�ּ�(�г���),����>
	
	static int count = 0; 
	
	public GameServer(Socket sock) {
		this.sock = sock;
	}
	
	void gameConnect(String address, String ip, Object hm) throws Exception{
		try {
			ObjectOutputStream oos = glist.get(address); 
			oos.writeObject(hm); 
			oos.flush();  
		} catch(NullPointerException e) {
			System.out.println(ip + "�� �������� �ʴ� ������ �����Ͽ����ϴ�");
		} catch(IOException e) {
			glist.remove(address);
		}
	}
	
	@Override
	public void run() {
		InetAddress inet = sock.getInetAddress();
		String ip = null;
		
		
		InputStream is = null;
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		
		OutputStream os = null;
		BufferedOutputStream bos = null;
		ObjectOutputStream oos = null;
		
		ip = inet.getHostAddress() + count;
		System.out.println(ip + " ����");
		try {
			os = sock.getOutputStream(); 
			bos = new BufferedOutputStream(os);
			oos = new ObjectOutputStream(bos);
			
			is = sock.getInputStream();
			bis = new BufferedInputStream(is);
			ois = new ObjectInputStream(bis);
			
			glist.put(ip,oos);
			
			System.out.println("���� ���� ���� : " + glist.size());
			
			Object obj = null;
			while((obj = ois.readObject()) != null) {	
				TreeMap<Double,String>  hm = (TreeMap)obj;
				Set set = hm.keySet();
				java.util.Iterator<String> ite = set.iterator();
				while(ite.hasNext()) {
					String s = ite.next();
					System.out.print(hm.get(s) + " �� "); // �� �������� Ȯ�ο�
				}
				//101.0 Ű�� ���� ���� �г������ε� ���� �ǵ��� ��.(�̴� ������ �г������ε� ���� �ǵ��� �ϴ� �Ű��� ��)
				if(hm.get(101.0) != null) {
					myNickname = hm.get(101.0); 
					glist.put(myNickname, oos);
				}else {
				address = hm.get(100.0); //100.0 Ű�� ���� ����г���, �ּ�
				System.out.println(address);
				hm.remove(100.0); //������� �ڵ�鸸 ����
				gameConnect(address, ip, hm); 
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
				if(bos != null) {bos.close();}
				if(os != null) {os.close();}
				
				if(ois != null) {ois.close();}
				if(bis != null) {bis.close();}
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
			serv = new ServerSocket(7000);
			while(true) {
				System.out.println("���� ���� ���� �����");
				count++;
				System.out.println("33333333333333333");
				Socket sock = serv.accept(); 
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
