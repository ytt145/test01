

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
	   HashMap<String, ObjectOutputStream>hm; //�����尣�� ������ ������ ��
	   //key ���̵�, val�� ��� ��Ʈ��
	   
	   public ChatServerThread(Socket s, HashMap hm) {
	      child = s;
	      this.hm = hm;
	      try {
	         System.out.println(child.getInetAddress()+"�κ��� ���� ��û ����");
	         ois = new ObjectInputStream(child.getInputStream());
	         oos = new ObjectOutputStream(child.getOutputStream());
	         
	         user_id = (String)ois.readObject();
	         broadcast(user_id +"���� �����ϼ̽��ϴ�.");
	         System.out.println("������ Ŭ���̾�Ʈ�� ���̵�� "+user_id +"�Դϴ�.");
	         
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
	         System.out.println("Ŭ���̾�Ʈ�� ���� ����");
	      }
	      finally {
	         synchronized(hm) {
	            hm.remove(user_id);
	         }
	         broadcast(user_id +"���� �����̽��ϴ�.");
	         System.out.println(user_id +"���� �����̽��ϴ�.");
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
	                  oos.writeObject(id + "���� ������ ���� �ͼӸ��� �����̽��ϴ�.:" +msg);
	                  oos.flush();
	               }
	            }catch(IOException e) {}
	         }
	      }
	   }

