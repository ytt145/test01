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
	
	static ArrayList<BufferedWriter> list = new ArrayList<BufferedWriter>(); //전체 채팅
	
	//bw : 연결된 소켓에 정보를 전달 하는것

	static HashMap<String, BufferedWriter> wlist = new HashMap<>(); // 귓속말<주소(닉네임),유저> 
	
	static int count = 0; // wlist의 임시 주소 중복 없애는 용도 
	
	boolean whisperBoo = false; //상대방에게 귓속말이 실패.(예 : 귓속말 대상이 없거나 잘못입력 되서 wlist.get(key) = null;)
	
	public ChatServer(Socket sock) {
		this.sock = sock;
	}
	
	String nickname = "익명"; //클라이언트가 접속할때 서버에 전송하는 닉네임 받음
	
	void allUser(String nickname, String msg) throws Exception{
		for(int i = 0; i < list.size(); i++) { // list : bw들을 한데 모여서
			BufferedWriter bw = list.get(i);
			try {
				bw.write(nickname + ">>" + msg); // 모든 bw에 채팅을 써주는 것
				bw.newLine();
				bw.flush();
			}catch(IOException e) {
				list.remove(bw);
			}
		}
		
	}
	/*
	유저1 유저2
	wlist.(유저1,연결통로); ip,닉네임
	wlist.(유저2,연결통로); ip,닉네임
	유저1이 유저2 한테 귓속말 보냄
	(유저2,유저1,msg2)
	(유저1,유저1,msg2)
	*/
	
	void whisper(String address, String ip, String msg2) throws Exception{
		try {
			BufferedWriter bw = wlist.get(address); 
			
			//닉네임 치면 해당 닉네임의 연결통로가 나옴 
			//귓속말 보내는 사람과 받는 사람에게 보이는 채팅 차이
			if(address.equals(ip)) { 
				bw.write("(귓속말을 보냄)" + nickname + ">>" + msg2);
			}else {
				bw.write("(귓속말)" + nickname + ">>" + msg2); //상대방에게 보내는 것
			}
			
			bw.newLine();
			//귓속말 대상이 없는 경우 보낼려 시도했던 사람의 채팅창에 알림
			
			if(address.equals(ip) && whisperBoo == true) { 
				bw.write("귓속말 대상이 없습니다");
				bw.newLine();
				bw.flush();
				whisperBoo = false;
			}
			bw.flush();    //  /w 잘못입력함 채팅
		} catch(NullPointerException e) {
			whisperBoo = true;
			System.out.println(ip + "가 존재하지 않는 귓속말 대상을 기입하였습니다");
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
		System.out.println(ip + " 입장");
		try {
			is = sock.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			os = sock.getOutputStream(); //연결된 소켓에 아웃풋스트림(정보를 보냄)
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
	
			list.add(bw);
			wlist.put(ip,bw); 
//			wlist.put(닉네임, bw);
			
			System.out.println("현재 연결 갯수 : " + wlist.size());
			String msg = null;
			while((msg = br.readLine()) != null) {
				int isnickname = msg.indexOf("!@#$%닉네임nickname%$#@!");
				if(isnickname != -1) {
					nickname = msg.substring(0,isnickname);
					wlist.put(nickname,bw); 
				}else {
					int isWhisper = msg.indexOf("/w "); //이 글자 있는 위치 없으면 -1
					int isWhisper2 = msg.indexOf("/ㅈ ");
					
					if(isWhisper != -1 || isWhisper2 != -1) {
	//					System.out.println("귓속말 구분 기능 작동합니다"); 구분 되는지 확인함										
						int last = msg.indexOf(' ', 4);
						
						String address = new String();
						try {
							address = msg.substring(3,last); 
						}catch(StringIndexOutOfBoundsException e) {
							System.out.println(ip + "가 귓속말 형식을 잘못 입력했습니다");
						}
							
	//					System.out.println(address); 서브스트링 잘 잘랐는지 확인함
						String msg2 = msg.substring(last+1,msg.length()); //채팅
						whisper(address, ip, msg2); //상대방 채팅에 보임
						whisper(ip,ip,msg2); //내가 귓말 보낸 내용 내 채팅에 보임
					}else {
						allUser(nickname, msg); // 전체채팅
					} 
				}
				
			}
			
			
		} catch (IOException e) {
			System.out.println(ip + "연결 해제");
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
				System.out.println("채팅 서버 연결 대기중");
//				new Server(serv.accept()).start();
				count++;
				Socket sock = serv.accept(); //소켓(클라이언트)을 서버소켓에 연결
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
