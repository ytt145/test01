

	package server;

	import java.io.IOException;
	import java.io.ObjectInputStream;
	import java.io.ObjectOutputStream;
	import java.net.Socket;
	import java.util.HashMap;

	public class ChatServerThread implements Runnable{
	   Socket child;
	   ObjectInputStream ois;
	   ObjectOutputStream oos;
	   
	   String user_id;
	   HashMap<String, ObjectOutputStream>hm; //스레드간의 정보를 공유할 것
	   //key 아이디, val은 출력 스트림
	   
	   public ChatServerThread(Socket s, HashMap hm) {
	      child = s;
	      this.hm = hm;
	      try {
	         System.out.println(child.getInetAddress()+"로부터 연결 요청 받음");
	         ois = new ObjectInputStream(child.getInputStream());
	         oos = new ObjectOutputStream(child.getOutputStream());
	         
	         user_id = (String)ois.readObject();
	         broadcast(user_id +"님이 접속하셨습니다.");
	         System.out.println("접속한 클라이언트의 아이디는 "+user_id +"입니다.");
	         
	         synchronized(hm) {
	            hm.put(user_id, oos);
	         }
	      }catch(ClassNotFoundException e) {
	         e.printStackTrace();
	      }catch(Exception e) {
	         e.printStackTrace();
	      }
	   }
	   @Override
	   public void run() {
	      String receiveData;
	      try {
	         while(true) {
	            receiveData = (String)ois.readObject();
	            if(receiveData.equals("/quit"))
	               break;
	            else if(receiveData.indexOf("/to")> -1)
	               sendMsg(receiveData);
	            else
	               broadcast(user_id +":" +receiveData);
	         }
	      }catch(Exception e) {
	         System.out.println("클라이언트가 강제 종료");
	      }
	      finally {
	         synchronized(hm) {
	            hm.remove(user_id);
	         }
	         broadcast(user_id +"님이 나가셨습니다.");
	         System.out.println(user_id +"님이 나가셨습니다.");
	         try {
	            if(child !=null)
	               child.close();
	         }catch(Exception e) {}   
	         }
	      }
	         public void broadcast(String message) {
	            synchronized(hm) {
	               try {
	                  for(ObjectOutputStream oos : hm.values()) {
	                     oos.writeObject(message);
	                     oos.flush();
	                  }
	               }catch(IOException e) {}
	            }
	      }
	      public void sendMsg(String message) {
	         int begin =message.indexOf("") +1;
	         int end =message.indexOf("",begin);
	         
	         if(end != -1) {
	            String id = message.substring(begin,end);
	            String msg = message.substring(end +1);
	            ObjectOutputStream oos=hm.get(id);
	            try {
	               if(oos !=null) {
	                  oos.writeObject(id + "님이 다음과 같은 귀속말을 보내셨습니다.:" +msg);
	                  oos.flush();
	               }
	            }catch(IOException e) {}
	         }
	      }
	   }

