package semiproject.editor;

import java.io.*;
import java.net.*;
import java.time.chrono.ChronoZonedDateTime;
import java.util.*;

import javax.swing.text.html.HTMLDocument.Iterator;
import javax.xml.crypto.Data;

// 코드 전체를 보내는 게 나을 지, 코드 추가, 삭제될때마다 보내는 게 나을 지...

// 보기엔 코드 전체를 보내는 게 나을 거 같음

// 상대방은 받을 때마다 코드를 새롭게 복붙받고 gridlayout또한 실시간 변경!

public class GameServer extends Thread implements Serializable{
	Socket sock;
	
	String address;
	String myNickname = new String(); // 내 닉네임으로도 연결되도록함
	static HashMap<String, ObjectOutputStream> glist = new HashMap<>(); // 귓속말<주소(닉네임),유저>
	
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
			System.out.println(ip + "가 존재하지 않는 정보를 기입하였습니다");
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
		System.out.println(ip + " 입장");
		try {
			os = sock.getOutputStream(); 
			bos = new BufferedOutputStream(os);
			oos = new ObjectOutputStream(bos);
			
			is = sock.getInputStream();
			bis = new BufferedInputStream(is);
			ois = new ObjectInputStream(bis);
			
			glist.put(ip,oos);
			
			System.out.println("현재 연결 갯수 : " + glist.size());
			
			Object obj = null;
			while((obj = ois.readObject()) != null) {	
				TreeMap<Double,String>  hm = (TreeMap)obj;
				Set set = hm.keySet();
				java.util.Iterator<String> ite = set.iterator();
				while(ite.hasNext()) {
					String s = ite.next();
					System.out.print(hm.get(s) + " ★ "); // 값 나오는지 확인용
				}
				//101.0 키의 값은 나의 닉네임으로도 연결 되도록 함.(이는 상대방의 닉네임으로도 연결 되도록 하는 매개가 됨)
				if(hm.get(101.0) != null) {
					myNickname = hm.get(101.0); 
					glist.put(myNickname, oos);
				}else {
				address = hm.get(100.0); //100.0 키의 값은 상대방닉네임, 주소
				System.out.println(address);
				hm.remove(100.0); //지우고나면 코드들만 존재
				gameConnect(address, ip, hm); 
				}
			}
			
		} catch (IOException e) {
			System.out.println(ip + "연결 해제");
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
				System.out.println("게임 서버 연결 대기중");
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
