package semiproject.editer3.notIui;

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
//	static ArrayList<BufferedWriter> chatList = new ArrayList<BufferedWriter>(); //전체 채팅이용자들의 전송통로
//	static HashMap<String, BufferedWriter> chatWlist = new HashMap<>(); // 귓속말<주소(닉네임),유저> 
	static String nickname = "익명"; //클라이언트가 접속할때 서버에 전송하는 닉네임 받음
	
	
	//nickDos의 기능
	//채팅 : 귓속말할 대상에게 귓속말을 하겠다는 신호 발송
	//게임 : 게임할 대상에게 신호 발송
	static HashMap<String, DataOutputStream> nickDos = new HashMap<>(); 
	static HashMap<String,String> myNickname = new HashMap<String, String>();
	
//	static HashMap<String, ObjectOutputStream> gameList = new HashMap<>(); // 게임할 상대에게 내 코드를 전송하는 역할
	static HashMap<String, String> myCounterpart = new HashMap<String, String>(); // 게임할 상대를 저장. 키값은 내 ip, val값은 상대 닉네임 저장. gameList사용할 시 myCounterpart.get(ip)로 상대를 String으로 받아 gameList.<상대>로 ObjectOutputStream 전송 통로 연결 
	

	static class Chat{
		
		//1번 전체채팅
		static void allUser(String ip, String msg) throws Exception{
			for(int i = 0; i < AllchatDos.size(); i++) { // list : bw들을 한데 모여서
				DataOutputStream dos = AllchatDos.get(i);
//				BufferedWriter bw = chatList.get(i);
				try {
					int send = 1;
					dos.writeInt(send); 

					dos.writeUTF(myNickname.get(ip) + ">>" + msg); // 모든 bw에 채팅을 써주는 것
					dos.flush();
				}catch(IOException e) {
					AllchatDos.remove(dos);
//					chatList.remove(bw);
				}
			}
			
		}
		
		static boolean whisperBoo = false; //상대방에게 귓속말이 실패.(예 : 귓속말 대상이 없거나 잘못입력 되서 wlist.get(key) = null;)
		
		//2번 귓속말
		static void whisper(String address, String ip, String msg2) throws Exception{
			try {
				DataOutputStream dos = nickDos.get(address);
//				BufferedWriter bw = chatWlist.get(address); 
				//닉네임 치면 해당 닉네임의 연결통로가 나옴 
				//귓속말 보내는 사람과 받는 사람에게 보이는 채팅 차이
				
				int send = 2;
				dos.writeInt(send);
				
				String whisperchat = new String();
				if(address.equals(ip)) { 
					whisperchat += "(귓속말을 보냄)" + nickname + ">>" + msg2;
				}else {
					whisperchat += "(귓속말)" + nickname + ">>" + msg2; //상대방에게 보내는 것
				}
				
				//귓속말 대상이 없는 경우 보낼려 시도했던 사람의 채팅창에 알림
				
				if(address.equals(ip) && whisperBoo == true) { 
					whisperchat += "\n귓속말 대상이 없습니다";
					whisperBoo = false;
				}
				dos.writeUTF(whisperchat);
				dos.flush();    //  /w 잘못입력함 채팅
			} catch(NullPointerException e) {
				whisperBoo = true;
				System.out.println(ip + "가 존재하지 않는 귓속말 대상을 기입하였습니다");
			} catch(IOException e) {
				nickDos.remove(address);
			}
		}
		
	}


	static class Game{
		//101번
		static void codeSend(String myCounter, String ip, String utf) {
			try {
				DataOutputStream dos = nickDos.get(myCounter);
				
				int send = 101;
				dos.writeInt(send);
				
				dos.writeUTF(utf); 
				dos.flush();  
				
			} catch(NullPointerException e) {
				System.out.println(ip + "가 존재하지 않는 정보를 기입하였습니다");
			} catch(IOException e) {
				nickDos.remove(myCounter);
			}
		}
		 //102번 qwe
	      static void firstSignal(String myCounter, String ip) {
	         
	         try {
	            DataOutputStream dos = nickDos.get(myCounter);
	            int send = 102;
	            dos.writeInt(send);
	            dos.flush();
	            
	         } catch (IOException e) {
	            System.out.println(ip + "가 잘못된 정보를 기입하였습니다");
	         }
	         
	      }
	      //qwe-
	      //103번
	     static void codeHit(String myCounter, String ip, String utf) {
	         
	         try {
	            DataOutputStream dos = nickDos.get(myCounter);
	            int send = 103;
	            dos.writeInt(send);
	            dos.writeUTF(utf);
	            dos.flush();
	            
	         } catch (IOException e) {
	            System.out.println(ip + "가 잘못된 정보를 기입하였습니다");
	         }
	         
	      }
	      
	      //104번
	      static void codeWrong(String myCounter, String ip, String utf) {
	         try {
	              DataOutputStream dos = nickDos.get(myCounter);
	              int send = 104;
	              dos.writeInt(send);
	              dos.writeUTF(utf);
	              dos.flush();
	         } catch (IOException e) {
	              System.out.println(ip + "가 잘못된 정보를 기입하였습니다");
	         }
	      }
	      
	      //105번
	      static void loser(String myCounter, String ip) {
	    	  try {
	    		  DataOutputStream dos = nickDos.get(myCounter);
	    		  int send = 105;
	    		  dos.writeInt(send);
	    		  dos.flush();
	    	  } catch (IOException e) {
	    		  System.out.println(ip + "가 잘못된 정보를 기입하였습니다");
	    	  }
	      }
	      /*
	      //106번
	      static void counterMycodeRight(String myCounter, String ip, String utf) {
	    	  try {
	    		  DataOutputStream dos = nickDos.get(myCounter);
	    		  int send = 106;
	    		  dos.writeInt(send);
	    		  dos.writeUTF(utf);
	    		  dos.flush();
	    	  } catch (IOException e) {
	    		  System.out.println(ip + "가 잘못된 정보를 기입하였습니다");
	    	  }
	      }
	      */
	      
		//110번
		static void remainBlackCodes(String myCounter, String ip, String utf) {
			try {
				DataOutputStream dos = nickDos.get(myCounter);
				
				int send = 110;
				dos.writeInt(send);
				dos.writeUTF(utf); 
				dos.flush();  
				
			} catch(NullPointerException e) {
				System.out.println(ip + "가 잘못된 정보를 기입하였습니다");
			} catch(IOException e) {
				nickDos.remove(myCounter);
			}
		}
		
		
		
		//111번
		static void remainWhiteCodes(String myCounter, String ip, String utf) {
			try {
				DataOutputStream dos = nickDos.get(myCounter);
				
				int send = 111;
				dos.writeInt(send);
				dos.writeUTF(utf); 
				dos.flush();  
				
			} catch(NullPointerException e) {
				System.out.println(ip + "가 잘못된 정보를 기입하였습니다");
			} catch(IOException e) {
				nickDos.remove(myCounter);
			}
		}
		
	
	}
	
	
	@Override
	public void run() {
		InetAddress inet = sock.getInetAddress();
		String ip = null;
	
		//기본 틀
		InputStream is = null; 
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		
		OutputStream os = null;
		BufferedOutputStream bos = null;
		DataOutputStream dos = null;
		
		int num = -1;
		
		ip = inet.getHostAddress() + count;
		System.out.println(ip + " 입장");
		try {
			is = sock.getInputStream();
			System.out.println("문제0");
			
			bis = new BufferedInputStream(is);
			System.out.println("문제1");
			
			dis = new DataInputStream(is);
			System.out.println("문제2");
			
			
			os = sock.getOutputStream(); //연결된 소켓에 아웃풋스트림(정보를 보냄)
			bos = new BufferedOutputStream(os);
			dos = new DataOutputStream(bos);
			
			
			AllchatDos.add(dos);//전체 접속자에게 전송
			
//			wlist.put(닉네임, bw);
			
			
			// 1번 전체채팅
			// 2번 귓속말
			// 클라이언트에서 미리 채팅 칠때 /ㅈ와 /w 로 귓속말 표시되었는지 indexOf로 확인하고 -1이면 귓속말인 2를 dataOuputStream으로 보내고, -1이 아니면 1을 보냄
			
			// 98번 내 닉네임 세팅
			// 99번 상대방 연결할 닉네임 세팅 <------------------------101번에 상대방 주소를 넣는 용도만임. 그래서 그냥 받아서 String으로 보관
			// 101번 게임 코드전송
			
			while(true) {
			
			// 1. 서버는 먼저 datastream으로 전송된 Int 하나를 읽는다
				
				num = dis.readInt();
				System.out.println(num);	
	
			// 2. 읽은 Int가 몇 번에 속하는 지 확인	
				if(num == -1) {
					System.out.println(ip + " : 잘못된 요청");
				}else if(num < 100) {
					System.out.print(ip + " : 채팅관련 또는 닉네임 설정 요청");
			
		//				chatList.add(bw); // 전체 채팅
		//				chatWlist.put(ip,bw); // 귓속말 대상(ip로 입력할 수 있는)
								
						String msg = null;
						msg = dis.readUTF(); //채팅 또는 닉네임을 읽어옴 
							System.out.println(ip + " : " + msg);
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
									nickDos.put(ip, dos);
									myNickname.put(ip, msg); //내 닉네임이 뭔지 ip에 닉네임 넣어놈
									nickname = msg;
	//								chatWlist.put(msg,bw); // 닉네임으로 귓속말이 되도록 설정
								}else if(num == 99) {
									System.out.println(ip + "게임에서 상대할 닉네임" + msg +"저장");
									myCounterpart.put(ip, msg);
								}
							
							//뭔가를 구현 했을 때 해당 이벤트의 번호를 정하고
							
							// 1.내 client 이벤트 발생
							//  int send = 해당 이벤트의 번호
							//  dos.writeInt(sen);
							//  dos.writeUTF(뭔가의 스트링을 보냄);
							
							// 2. 서버가 내 client가 보낸 번호와 utf스트링을 받음
							//	  int send = -1 초기설정
							//    dis.readInt(send); 정한번호
							// 	  dis.readUTF(스트링을 받음)
							//    게임관련은 그냥 곧이 곧대로 보냄
							//	  보내는 방법은 게임 클래스에 메서드를 만들어서 보이기 편하게 설정
							// 	  예시) game 클래스 안에 static void sunpick(){
							//		int send = 정한 번호		
							//		dos.writeInt(send); 
							//    서로의 클라이언트가 게임의 이벤트를 발생시키는 데
							//		보내는 스트링이 굳이 필요 없으면 utf 안해도됨
							//      dos.writeUTF(스트링);
							// 3. 타 클라이언트가 readInt함
							// 	 readInt에서 else if에 해당 정한 번호를 추가
							// 	 해당 번호에 대한 이벤트를 설정. reavalidate
							
				}else {
					System.out.println(ip + "게임 관련 요청");
					
					String myCounter = myCounterpart.get(ip); //내 상대를 가져옴
					
					// 101번 내 코드를 상대에게 전달
					if(num == 101) {
						
						String msg = null;
						myCounter = myCounterpart.get(ip); //99번에서 저장해둔 상대 String으로 받음
						
						msg = dis.readUTF();
				
			//				gameList.put(ip,oos); // 게임 대상에 오브젝트 전송통로 연결
						Game.codeSend(myCounter, ip, msg);  // 상대방의 오브젝트 연결통로로 object 전송
						System.out.println(myCounter + "에게 코드 전달");
						System.out.println("코드 : " + msg);
					} else if(num == 102) {
						 Game.firstSignal(myCounter, ip);
						
					} else if(num == 103) {
						  //서버에서는 게임관련해서는 터치할게 하나도 없음
		                  // 넘버를 그대로 103을 넘겨주면 됨
		                   String msg = null;
		                  msg = dis.readUTF();
		                  Game.codeHit(myCounter, ip, msg);
					} else if(num == 104) {
						String msg = null;
						msg = dis.readUTF();
						Game.codeWrong(myCounter, ip, msg);
		                System.out.println("104 전달 받았는지 확인용");
		                System.out.println("상대방 마지막 번호 : " + msg);
		                
					} else if(num == 105) {
						Game.loser(myCounter, ip);
						System.out.println("105 전달 받았는지 확인용");
					/*	
					} else if(num == 106) {
						String msg = null;
						msg = dis.readUTF();
						Game.counterMycodeRight(myCounter, ip, msg);
						System.out.println("106 전달 받았는지 확인용");
					*/	
					} else if(num == 110) {
						
						String msg = dis.readUTF();
						Game.remainBlackCodes(myCounter, ip, msg);
						
					} else if(num == 111) {
						
						String msg = dis.readUTF();
						Game.remainWhiteCodes(myCounter, ip, msg);
						
					}
				}

			
			}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				try {
					if(dos != null) {dos.close();}
					if(bos != null) {bos.close();}
					if(os != null) {os.close();}
					
					
					if(dis != null) {dis.close();}
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
			serv = new ServerSocket(5000);
			
			while(true) {
				System.out.println("채팅 서버 연결 대기중");
				count++;
				Socket sock = serv.accept(); //소켓(클라이언트)을 서버소켓에 연결
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
