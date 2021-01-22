package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Ex15_07_ChatServerEx {
 public static void main(String[] args) throws IOException {
  Ex15_07_ChatServerEx c = new Ex15_07_ChatServerEx();
  c.server = new ServerSocket(port);
  System.out.println("===== ä�� ���� ���� =====");
  c.hm = new HashMap<String, ObjectOutputStream>();
  while (true) {
   System.out.println("Ŭ���̾�Ʈ ���� ��� ��");
   Socket client = c.server.accept();
   ChatServerThread chatThread = new ChatServerThread(client, c.hm);

   Thread t = new Thread(chatThread);
   t.start();
  }
 }

 ServerSocket server;
 final static int port = 1507;
 HashMap<String, ObjectOutputStream> hm;

 static class ChatServerThread implements Runnable {

  public ChatServerThread(Socket s, HashMap<String, ObjectOutputStream> hm) {
   this.client = s;
   this.hm = hm;
   try {
    System.out.println(client.getInetAddress() + " �����");
    ois = new ObjectInputStream(client.getInputStream());
    oos = new ObjectOutputStream(client.getOutputStream());

    userId = (String) ois.readObject();
    System.out.println("userId : " + userId);
    broadcast(userId + "�� ���ӵ�");
    synchronized (hm) {
     hm.put(userId, oos);
    }
   } catch (Exception e) {
    e.printStackTrace();
   }
  }

  Socket client;
  ObjectInputStream ois;
  ObjectOutputStream oos;
  String userId;
  HashMap<String, ObjectOutputStream> hm;

  @Override
  public void run() {
   String rcvData;
   try {
    while (true) {
     rcvData = (String) ois.readObject();
     if (rcvData.equals("/bye"))
      break;
     else if (rcvData.startsWith("\to"))
      sendMsg(rcvData);     
     else
      broadcast(userId + " : " + rcvData);
    }
   } catch (Exception e) {
    System.out.println("Ŭ���̾�Ʈ ���� ����");
   } finally {
    synchronized (hm) {
     hm.remove(userId);
    }

    broadcast(userId + "�� ����!");
    try {
     if (client != null)
      client.close();
    } catch (Exception e) {
    }
   }
  }

  public void sendMsg(String rcvData) {
   int begin = rcvData.indexOf(" ") + 1;
   int end = rcvData.indexOf(" ", begin);
   if (end != -1) {
    String id = rcvData.substring(begin, end);
    String msg = rcvData.substring(end + 1);
    ObjectOutputStream oos = hm.get(id);
    try {
     if (oos != null) {
      oos.writeObject(userId + "���� �ӼӸ� : " + msg);
      oos.flush();
     }
    } catch (Exception e) {
    }
   }
  }

  public void broadcast(String message) {
   synchronized (hm) {
    try {
     for (ObjectOutputStream oos : hm.values()) {
      oos.writeObject(message);
      oos.flush();
     }
    } catch (IOException e) {
     e.printStackTrace();
    }
   }
  }

 }

 }