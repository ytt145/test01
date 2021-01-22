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
	static int count = 0; // wlist�� �ӽ� �ּ� �ߺ� ���ִ� �뵵
	
	static ArrayList<DataOutputStream> AllchatDos = new ArrayList<>(); //��ü ä���� ����鿡�� ��� ä���� �ϰڴٴ� ��ȣ �߼� //1��
//	static ArrayList<BufferedWriter> chatList = new ArrayList<BufferedWriter>(); //��ü ä���̿��ڵ��� �������
//	static HashMap<String, BufferedWriter> chatWlist = new HashMap<>(); // �ӼӸ�<�ּ�(�г���),����> 
	static String nickname = "�͸�"; //Ŭ���̾�Ʈ�� �����Ҷ� ������ �����ϴ� �г��� ����
	
	
	//nickDos�� ���
	//ä�� : �ӼӸ��� ��󿡰� �ӼӸ��� �ϰڴٴ� ��ȣ �߼�
	//���� : ������ ��󿡰� ��ȣ �߼�
	static HashMap<String, DataOutputStream> nickDos = new HashMap<>(); 
	static HashMap<String,String> myNickname = new HashMap<String, String>();
	
//	static HashMap<String, ObjectOutputStream> gameList = new HashMap<>(); // ������ ��뿡�� �� �ڵ带 �����ϴ� ����
	static HashMap<String, String> myCounterpart = new HashMap<String, String>(); // ������ ��븦 ����. Ű���� �� ip, val���� ��� �г��� ����. gameList����� �� myCounterpart.get(ip)�� ��븦 String���� �޾� gameList.<���>�� ObjectOutputStream ���� ��� ���� 
	

	static class Chat{
		
		//1�� ��üä��
		static void allUser(String ip, String msg) throws Exception{
			for(int i = 0; i < AllchatDos.size(); i++) { // list : bw���� �ѵ� �𿩼�
				DataOutputStream dos = AllchatDos.get(i);
//				BufferedWriter bw = chatList.get(i);
				try {
					int send = 1;
					dos.writeInt(send); 

					dos.writeUTF(myNickname.get(ip) + ">>" + msg); // ��� bw�� ä���� ���ִ� ��
					dos.flush();
				}catch(IOException e) {
					AllchatDos.remove(dos);
//					chatList.remove(bw);
				}
			}
			
		}
		
		static boolean whisperBoo = false; //���濡�� �ӼӸ��� ����.(�� : �ӼӸ� ����� ���ų� �߸��Է� �Ǽ� wlist.get(key) = null;)
		
		//2�� �ӼӸ�
		static void whisper(String address, String ip, String msg2) throws Exception{
			try {
				DataOutputStream dos = nickDos.get(address);
//				BufferedWriter bw = chatWlist.get(address); 
				//�г��� ġ�� �ش� �г����� ������ΰ� ���� 
				//�ӼӸ� ������ ����� �޴� ������� ���̴� ä�� ����
				
				int send = 2;
				dos.writeInt(send);
				
				String whisperchat = new String();
				if(address.equals(ip)) { 
					whisperchat += "(�ӼӸ��� ����)" + nickname + ">>" + msg2;
				}else {
					whisperchat += "(�ӼӸ�)" + nickname + ">>" + msg2; //���濡�� ������ ��
				}
				
				//�ӼӸ� ����� ���� ��� ������ �õ��ߴ� ����� ä��â�� �˸�
				
				if(address.equals(ip) && whisperBoo == true) { 
					whisperchat += "\n�ӼӸ� ����� �����ϴ�";
					whisperBoo = false;
				}
				dos.writeUTF(whisperchat);
				dos.flush();    //  /w �߸��Է��� ä��
			} catch(NullPointerException e) {
				whisperBoo = true;
				System.out.println(ip + "�� �������� �ʴ� �ӼӸ� ����� �����Ͽ����ϴ�");
			} catch(IOException e) {
				nickDos.remove(address);
			}
		}
		
	}


	static class Game{
		//101��
		static void codeSend(String myCounter, String ip, String utf) {
			try {
				DataOutputStream dos = nickDos.get(myCounter);
				
				int send = 101;
				dos.writeInt(send);
				
				dos.writeUTF(utf); 
				dos.flush();  
				
			} catch(NullPointerException e) {
				System.out.println(ip + "�� �������� �ʴ� ������ �����Ͽ����ϴ�");
			} catch(IOException e) {
				nickDos.remove(myCounter);
			}
		}
		 //102�� qwe
	      static void firstSignal(String myCounter, String ip) {
	         
	         try {
	            DataOutputStream dos = nickDos.get(myCounter);
	            int send = 102;
	            dos.writeInt(send);
	            dos.flush();
	            
	         } catch (IOException e) {
	            System.out.println(ip + "�� �߸��� ������ �����Ͽ����ϴ�");
	         }
	         
	      }
	      //qwe-
	      //103��
	     static void codeHit(String myCounter, String ip, String utf) {
	         
	         try {
	            DataOutputStream dos = nickDos.get(myCounter);
	            int send = 103;
	            dos.writeInt(send);
	            dos.writeUTF(utf);
	            dos.flush();
	            
	         } catch (IOException e) {
	            System.out.println(ip + "�� �߸��� ������ �����Ͽ����ϴ�");
	         }
	         
	      }
	      
	      //104��
	      static void codeWrong(String myCounter, String ip, String utf) {
	         try {
	              DataOutputStream dos = nickDos.get(myCounter);
	              int send = 104;
	              dos.writeInt(send);
	              dos.writeUTF(utf);
	              dos.flush();
	         } catch (IOException e) {
	              System.out.println(ip + "�� �߸��� ������ �����Ͽ����ϴ�");
	         }
	      }
	      
	      //105��
	      static void loser(String myCounter, String ip) {
	    	  try {
	    		  DataOutputStream dos = nickDos.get(myCounter);
	    		  int send = 105;
	    		  dos.writeInt(send);
	    		  dos.flush();
	    	  } catch (IOException e) {
	    		  System.out.println(ip + "�� �߸��� ������ �����Ͽ����ϴ�");
	    	  }
	      }
	      /*
	      //106��
	      static void counterMycodeRight(String myCounter, String ip, String utf) {
	    	  try {
	    		  DataOutputStream dos = nickDos.get(myCounter);
	    		  int send = 106;
	    		  dos.writeInt(send);
	    		  dos.writeUTF(utf);
	    		  dos.flush();
	    	  } catch (IOException e) {
	    		  System.out.println(ip + "�� �߸��� ������ �����Ͽ����ϴ�");
	    	  }
	      }
	      */
	      
		//110��
		static void remainBlackCodes(String myCounter, String ip, String utf) {
			try {
				DataOutputStream dos = nickDos.get(myCounter);
				
				int send = 110;
				dos.writeInt(send);
				dos.writeUTF(utf); 
				dos.flush();  
				
			} catch(NullPointerException e) {
				System.out.println(ip + "�� �߸��� ������ �����Ͽ����ϴ�");
			} catch(IOException e) {
				nickDos.remove(myCounter);
			}
		}
		
		
		
		//111��
		static void remainWhiteCodes(String myCounter, String ip, String utf) {
			try {
				DataOutputStream dos = nickDos.get(myCounter);
				
				int send = 111;
				dos.writeInt(send);
				dos.writeUTF(utf); 
				dos.flush();  
				
			} catch(NullPointerException e) {
				System.out.println(ip + "�� �߸��� ������ �����Ͽ����ϴ�");
			} catch(IOException e) {
				nickDos.remove(myCounter);
			}
		}
		
	
	}
	
	
	@Override
	public void run() {
		InetAddress inet = sock.getInetAddress();
		String ip = null;
	
		//�⺻ Ʋ
		InputStream is = null; 
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		
		OutputStream os = null;
		BufferedOutputStream bos = null;
		DataOutputStream dos = null;
		
		int num = -1;
		
		ip = inet.getHostAddress() + count;
		System.out.println(ip + " ����");
		try {
			is = sock.getInputStream();
			System.out.println("����0");
			
			bis = new BufferedInputStream(is);
			System.out.println("����1");
			
			dis = new DataInputStream(is);
			System.out.println("����2");
			
			
			os = sock.getOutputStream(); //����� ���Ͽ� �ƿ�ǲ��Ʈ��(������ ����)
			bos = new BufferedOutputStream(os);
			dos = new DataOutputStream(bos);
			
			
			AllchatDos.add(dos);//��ü �����ڿ��� ����
			
//			wlist.put(�г���, bw);
			
			
			// 1�� ��üä��
			// 2�� �ӼӸ�
			// Ŭ���̾�Ʈ���� �̸� ä�� ĥ�� /���� /w �� �ӼӸ� ǥ�õǾ����� indexOf�� Ȯ���ϰ� -1�̸� �ӼӸ��� 2�� dataOuputStream���� ������, -1�� �ƴϸ� 1�� ����
			
			// 98�� �� �г��� ����
			// 99�� ���� ������ �г��� ���� <------------------------101���� ���� �ּҸ� �ִ� �뵵����. �׷��� �׳� �޾Ƽ� String���� ����
			// 101�� ���� �ڵ�����
			
			while(true) {
			
			// 1. ������ ���� datastream���� ���۵� Int �ϳ��� �д´�
				
				num = dis.readInt();
				System.out.println(num);	
	
			// 2. ���� Int�� �� ���� ���ϴ� �� Ȯ��	
				if(num == -1) {
					System.out.println(ip + " : �߸��� ��û");
				}else if(num < 100) {
					System.out.print(ip + " : ä�ð��� �Ǵ� �г��� ���� ��û");
			
		//				chatList.add(bw); // ��ü ä��
		//				chatWlist.put(ip,bw); // �ӼӸ� ���(ip�� �Է��� �� �ִ�)
								
						String msg = null;
						msg = dis.readUTF(); //ä�� �Ǵ� �г����� �о�� 
							System.out.println(ip + " : " + msg);
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
									nickDos.put(ip, dos);
									myNickname.put(ip, msg); //�� �г����� ���� ip�� �г��� �־��
									nickname = msg;
	//								chatWlist.put(msg,bw); // �г������� �ӼӸ��� �ǵ��� ����
								}else if(num == 99) {
									System.out.println(ip + "���ӿ��� ����� �г���" + msg +"����");
									myCounterpart.put(ip, msg);
								}
							
							//������ ���� ���� �� �ش� �̺�Ʈ�� ��ȣ�� ���ϰ�
							
							// 1.�� client �̺�Ʈ �߻�
							//  int send = �ش� �̺�Ʈ�� ��ȣ
							//  dos.writeInt(sen);
							//  dos.writeUTF(������ ��Ʈ���� ����);
							
							// 2. ������ �� client�� ���� ��ȣ�� utf��Ʈ���� ����
							//	  int send = -1 �ʱ⼳��
							//    dis.readInt(send); ���ѹ�ȣ
							// 	  dis.readUTF(��Ʈ���� ����)
							//    ���Ӱ����� �׳� ���� ���� ����
							//	  ������ ����� ���� Ŭ������ �޼��带 ���� ���̱� ���ϰ� ����
							// 	  ����) game Ŭ���� �ȿ� static void sunpick(){
							//		int send = ���� ��ȣ		
							//		dos.writeInt(send); 
							//    ������ Ŭ���̾�Ʈ�� ������ �̺�Ʈ�� �߻���Ű�� ��
							//		������ ��Ʈ���� ���� �ʿ� ������ utf ���ص���
							//      dos.writeUTF(��Ʈ��);
							// 3. Ÿ Ŭ���̾�Ʈ�� readInt��
							// 	 readInt���� else if�� �ش� ���� ��ȣ�� �߰�
							// 	 �ش� ��ȣ�� ���� �̺�Ʈ�� ����. reavalidate
							
				}else {
					System.out.println(ip + "���� ���� ��û");
					
					String myCounter = myCounterpart.get(ip); //�� ��븦 ������
					
					// 101�� �� �ڵ带 ��뿡�� ����
					if(num == 101) {
						
						String msg = null;
						myCounter = myCounterpart.get(ip); //99������ �����ص� ��� String���� ����
						
						msg = dis.readUTF();
				
			//				gameList.put(ip,oos); // ���� ��� ������Ʈ ������� ����
						Game.codeSend(myCounter, ip, msg);  // ������ ������Ʈ ������η� object ����
						System.out.println(myCounter + "���� �ڵ� ����");
						System.out.println("�ڵ� : " + msg);
					} else if(num == 102) {
						 Game.firstSignal(myCounter, ip);
						
					} else if(num == 103) {
						  //���������� ���Ӱ����ؼ��� ��ġ�Ұ� �ϳ��� ����
		                  // �ѹ��� �״�� 103�� �Ѱ��ָ� ��
		                   String msg = null;
		                  msg = dis.readUTF();
		                  Game.codeHit(myCounter, ip, msg);
					} else if(num == 104) {
						String msg = null;
						msg = dis.readUTF();
						Game.codeWrong(myCounter, ip, msg);
		                System.out.println("104 ���� �޾Ҵ��� Ȯ�ο�");
		                System.out.println("���� ������ ��ȣ : " + msg);
		                
					} else if(num == 105) {
						Game.loser(myCounter, ip);
						System.out.println("105 ���� �޾Ҵ��� Ȯ�ο�");
					/*	
					} else if(num == 106) {
						String msg = null;
						msg = dis.readUTF();
						Game.counterMycodeRight(myCounter, ip, msg);
						System.out.println("106 ���� �޾Ҵ��� Ȯ�ο�");
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
				System.out.println("ä�� ���� ���� �����");
				count++;
				Socket sock = serv.accept(); //����(Ŭ���̾�Ʈ)�� �������Ͽ� ����
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
