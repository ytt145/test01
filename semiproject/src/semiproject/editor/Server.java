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
	static int count = 0; // wlist의 임시 주소 중복 없애는 용도
	
	static ArrayList<DataOutputStream> AllchatDos = new ArrayList<>(); //전체 채팅할 사람들에게 모든 채팅을 하겠다는 신호 발송 //1번
	static ArrayList<BufferedWriter> chatList = new ArrayList<BufferedWriter>(); //전체 채팅이용자들의 전송통로
	static HashMap<String, BufferedWriter> chatWlist = new HashMap<>(); // 귓속말<주소(닉네임),유저> 
	static String nickname = "익명"; //클라이언트가 접속할때 서버에 전송하는 닉네임 받음
	
	
	//nickDos의 기능
	//채팅 : 귓속말할 대상에게 귓속말을 하겠다는 신호 발송
	//게임 : 게임할 대상에게 신호 발송
	static HashMap<String, DataOutputStream> nickDos = new HashMap<>(); 
	
	static HashMap<String, ObjectOutputStream> gameList = new HashMap<>(); // 게임할 상대에게 내 코드를 전송하는 역할
	static HashMap<String, String> myCounterpart = new HashMap<String, String>(); // 게임할 상대를 저장. 키값은 내 ip, val값은 상대 닉네임 저장. gameList사용할 시 myCounterpart.get(ip)로 상대를 String으로 받아 gameList.<상대>로 ObjectOutputStream 전송 통로 연결 
	
	
	static class Chat{
		
		//1번 전체채팅
		static void allUser(String nickname, String msg) throws Exception{
			for(int i = 0; i < chatList.size(); i++) { // list : bw들을 한데 모여서
				DataOutputStream dos = AllchatDos.get(i);
				BufferedWriter bw = chatList.get(i);
				try {
					int send = 1;
					dos.writeInt(send);
					
					
					bw.write(nickname + ">>" + msg); // 모든 bw에 채팅을 써주는 것
					bw.newLine();
					bw.flush();
				}catch(IOException e) {
					AllchatDos.remove(dos);
					chatList.remove(bw);
				}
			}
			
		}
		
		
		static boolean whisperBoo = false; //상대방에게 귓속말이 실패.(예 : 귓속말 대상이 없거나 잘못입력 되서 wlist.get(key) = null;)
		
		//2번 귓속말
		static void whisper(String address, String ip, String msg2) throws Exception{
			try {
				DataOutputStream dos = nickDos.get(address);
				BufferedWriter bw = chatWlist.get(address); 
				//닉네임 치면 해당 닉네임의 연결통로가 나옴 
				//귓속말 보내는 사람과 받는 사람에게 보이는 채팅 차이
				
				int send = 2;
				dos.writeInt(send);
				
				
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
					whisperBoo = false;
				}
				bw.flush();    //  /w 잘못입력함 채팅
			} catch(NullPointerException e) {
				whisperBoo = true;
				System.out.println(ip + "가 존재하지 않는 귓속말 대상을 기입하였습니다");
			} catch(IOException e) {
				chatWlist.remove(address);
			}
		}
		
	}


	static class Game{
		//101번
		static void codeSend(String myCounter, String ip, Object obj) throws Exception{
			try {
				DataOutputStream dos = nickDos.get(myCounter);
				ObjectOutputStream oos = gameList.get(myCounter); 
				
				int send = 101;
				dos.writeInt(101);
				
				oos.writeObject(obj); 
				oos.flush();  
				
			} catch(NullPointerException e) {
				System.out.println(ip + "가 존재하지 않는 정보를 기입하였습니다");
			} catch(IOException e) {
				gameList.remove(myCounter);
			}
		}
	
	}
	
	
	@Override
	public void run() {
		InetAddress inet = sock.getInetAddress();
		String ip = null;
	
		//기본 틀
		InputStream is = null; 
		OutputStream os = null; 
		
		// 자료를 주고 받으려 할때 어떤 것으로 받을 지 구분을 위함.
		DataInputStream dis = null; 
		DataOutputStream dos = null;
		
		//채팅일 경우
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		OutputStreamWriter osw = null;
		BufferedWriter  bw = null;
		
		
		//게임일 경우
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		
		BufferedOutputStream bos = null;
		ObjectOutputStream oos = null;
		
		
		
		ip = inet.getHostAddress() + count;
		System.out.println(ip + " 입장");
		try {
			is = sock.getInputStream();
			System.out.println("문제0");
			dis = new DataInputStream(is);
			System.out.println("문제1");
			
			os = sock.getOutputStream(); //연결된 소켓에 아웃풋스트림(정보를 보냄)
			dos = new DataOutputStream(os);
			System.out.println("문제2");
			
			
			AllchatDos.add(dos);//전체 접속자에게 전송
			
//			wlist.put(닉네임, bw);
			
			
			// 1번 전체채팅
			// 2번 귓속말
			// 클라이언트에서 미리 채팅 칠때 /ㅈ와 /w 로 귓속말 표시되었는지 indexOf로 확인하고 -1이면 귓속말인 2를 dataOuputStream으로 보내고, -1이 아니면 1을 보냄
			
			// 98번 내 닉네임 세팅
			// 99번 상대방 연결할 닉네임 세팅 <------------------------101번에 상대방 주소를 넣는 용도만임. 그래서 그냥 받아서 String으로 보관
			// 101번 게임 코드전송
			
			int num = -1;
			num = dis.readInt();
			System.out.println(num);	
			
//			System.out.println("num = " + num);
			
			
			
			if(num == -1) {
				System.out.println(ip + " : 잘못된 요청");
			}else if(num < 100) {
				System.out.print(ip + " : 채팅관련 또는 닉네임 설정 요청");
				
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				
				osw = new OutputStreamWriter(os);
				bw = new BufferedWriter(osw);

				chatList.add(bw); // 전체 채팅
				chatWlist.put(ip,bw); // 귓속말 대상(ip로 입력할 수 있는)
				
				
				String msg = null;
				while((msg = br.readLine()) != null) {
					
				// 1번 전체채팅 메서드 실행
					if(num == 1) {
						Chat.allUser(ip, msg);
						
				// 2번 귓속말 메서드 실행
					}else if(num == 2) {
						 //귓속말 표시로 서브스트링 구분
//						int isWhisper = msg.indexOf("/w ");
//						int isWhisper2 = msg.indexOf("/ㅈ ");
					
						int last = msg.indexOf(' ', 4);
							
						String address = new String();
						try {
							address = msg.substring(3,last); 
						}catch(StringIndexOutOfBoundsException e) {
							System.out.println(ip + "가 귓속말 형식을 잘못 입력했습니다");
						}
							
	//					System.out.println(address); 서브스트링 잘 잘랐는지 확인함
						String msg2 = msg.substring(last+1,msg.length()); //채팅
						Chat.whisper(address, ip, msg2); //상대방 채팅에 보임
						Chat.whisper(ip,ip,msg2); //내가 귓말 보낸 내용 내 채팅에 보임
				// 내 닉네임으로 정보 전달 통로 연결
					}else if(num == 98) {
						System.out.println(ip + "본인 닉네임 관련 설정 요청");
						nickDos.put(msg,dos); // 채팅 귓속말이라는 데이터 보냄
						chatWlist.put(msg,bw); // 닉네임으로 귓속말이 되도록 설정
					}else if(num == 99) {
						System.out.println(ip + "게임에서 상대할 닉네임" + msg +"저장");
						myCounterpart.put(ip, msg);
					}
				
				}
		
			}else { // 101번 내 코드를 상대에게 전달
				System.out.println(ip + "게임 관련 요청");
				
				bis = new BufferedInputStream(is);
				ois = new ObjectInputStream(bis);
				
				gameList.put(ip,oos); // 게임 대상에 오브젝트 전송통로 연결
				
				String myCounter = myCounterpart.get(ip); //99번에서 저장해둔 상대 String으로 받음
				Object obj = null;
				while((obj = ois.readObject()) != null) {
					Game.codeSend(myCounter, ip, obj);  // 상대방의 오브젝트 연결통로로 object 전송
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
				System.out.println("채팅 서버 연결 대기중");
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
