package semiproject.finalend;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.DataBufferDouble;
import java.nio.Buffer;
import java.util.*;

import javax.swing.plaf.metal.MetalIconFactory.PaletteCloseIcon;

import semiproject.editor2.read;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
public class Uc extends JFrame implements WindowListener,ActionListener{
   
   static DataOutputStream dos = null; //ä������ �������� ���� ��ȣ ����

   static Dialog first; //�����ϱ� ��ư
   static boolean firstBoo = true;
   
    static Dialog last; //Next Time�޼���â
    
    static Dialog victory; //�¸��� â
  
    static Dialog loser; //�й��� â
    
    static JFrame trying; //����Ͻðڽ��ϱ� â
    
   Dialog chatProgram = new Dialog(this);
   static TextArea chatTa = new TextArea();
   static String whisperUsing = "�ӼӸ��� ����Ͻ÷��� �Ʒ� ���ÿ� ���� ������ֽñ� �ٶ��ϴ�.\n ����1) /w ������ �ȳ��ϼ��� \n ����2) /�� 127.0.0.13 �ݰ����ϴ�\n/clear : ä�� ���� �����\n";
   //��üä�ð� �ӼӸ��� �ö����� ä��â�� ���� �߰��ϴ� �뵵
   static String chat = whisperUsing; 
   
   //���� 
   static TreeMap<Double, String> myCode = new TreeMap<>(); //�� �ڵ�
   static ArrayList<JLabel> myCodeLabel; //�� �ڵ� ����
   
   static TreeMap<Double, String> yourCode; //���� �ڵ�
   static ArrayList<JLabel> yourCodeLabel; //��� �ڵ� ����
   
   Random ran = new Random();
   
   static ArrayList<Double> black = new ArrayList<>(); //���ڵ带 ��� ����
   static ArrayList<Double> white = new ArrayList<>(); //ȭ��Ʈ�ڵ带 ��� ����
   
   Double blackVal = 0.0; //���ڵ带 ������ �ֱ����� ȭ��Ʈ�ڵ尪�� 0.5�� ��
   Double whiteVal = 0.5;

   Dialog pickmyCode = new Dialog(this); // ���� �� �ڵ�
   Font font = new Font("���ʷҵ���",Font.BOLD,20); // ��Ʈ �⺻ ���� 

   
   JPanel p1 = new JPanel();
   JLabel la1 = new JLabel("�߸��� �ڵ带 �Է��ϼ���");
   
   
   static Label la2 = new Label("������ �ڵ� �� : 0��");
   
   static int pickCount = 0;
   
   static JButton blackPick = new JButton("��");
   static JButton whitePick = new JButton("ȭ��Ʈ");
   
   static JButton yes=new JButton("��");
   static JButton no=new JButton("�ƴϿ�");
   
   static JButton VicExitButton = new JButton("���� ����");
   static JButton LosExitButton = new JButton("���� ����");
   
   JPanel pf1 = new JPanel();
   static JPanel pf2 = new JPanel(); // ��� �ڵ�
   JPanel pf3 = new JPanel();
   static JPanel pf4 = new JPanel();
   
   int myCodeCount = 0;
   
   static TextField tf = new TextField("������ �ڵ带 �߸��غ�����");
   
   static int myHit = 0; // ���� ������ �ڵ带 ���� ����
   
   static ArrayList<Integer> myCounterCodeRightNum = new ArrayList<>(); 
   //���� ������ �ڵ带 ���� �� ��ȣ 0, 1 �� ��� ������ �ڵ带 �޾ƿ� �� �� ���� �� ���� ���� ������ �ڵ���� ��Ʈ����
   //���̰� �����ϴ� ��. 410��° �� ������ ��ȣ ������ ��.
   
   
   static ArrayList<Integer> counterMyCodeRightNum = new ArrayList<>();
   //103���� �޾����� �׿� �ش�Ǵ� �� ��ȣ�� ã������ �� ��ȣ�� ���⿡ �����ϰ� �ڵ�101�� ���� �� ������ �ٽ� �����Ұ���
   
   static String lastPickNum = "-1"; //�������� ���� �� �ڵ��� ��ġ�� ���� 
 
   static Label myNameLabel = new Label(); // �� �̸� �ִ� ��â
   static Label counterNameLabel = new Label(); //���� �ִ� ��â
  
   ArrayList<String> beforeCountermyCodeHitList = new ArrayList<String>(); //�������� �ڵ带 �����ϴ� ����
   
   void iconSetting() { //������ �󺧿� �������� �־��ִ� �޼���
      pf4.removeAll();
      pf4.setLayout(new GridLayout(1,myCodeLabel.size()));
      
      for(int i = 0 ; i < myCodeLabel.size(); i++) {
         pf4.add(myCodeLabel.get(i));
         Dimension d = pf4.getSize();
         
         int index = myCodeLabel.get(i).getText().indexOf(" ");
         String color = myCodeLabel.get(i).getText().substring(0,index);
         String num = myCodeLabel.get(i).getText().substring(index+1,myCodeLabel.get(i).getText().length());
         myCodeLabel.get(i).setBackground(Color.GRAY);         
         if(color.equals("��")) {
            boolean rightBoo = false;
            
            for(int j = 0; j < beforeCountermyCodeHitList.size(); j++) {
               if(myCodeLabel.get(i).getText().equals(beforeCountermyCodeHitList.get(j))) {
                  rightBoo = true;
               }
            }
            
            if(rightBoo) {
               ImageIcon icon = new ImageIcon("black\\br" + num + ".png");
               Image img = icon.getImage().getScaledInstance(d.width/(myCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
               icon = new ImageIcon(img);
               myCodeLabel.get(i).setIcon(icon);
            }else {
               ImageIcon icon = new ImageIcon("black\\b" + num + ".png");
               Image img = icon.getImage().getScaledInstance(d.width/(myCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
               icon = new ImageIcon(img);
               myCodeLabel.get(i).setIcon(icon);
            }

         }else {
            boolean rightBoo = false;
            for(int j = 0; j < beforeCountermyCodeHitList.size(); j++) {
                  if(myCodeLabel.get(i).getText().equals(beforeCountermyCodeHitList.get(j))) {
                     rightBoo = true;
                  }
               }
            if(rightBoo) {
               ImageIcon icon = new ImageIcon("white\\wr" + num + ".png");
               Image img = icon.getImage().getScaledInstance(d.width/(myCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
               icon = new ImageIcon(img);
               myCodeLabel.get(i).setIcon(icon);
            }else {
               ImageIcon icon = new ImageIcon("white\\w" + num + ".png");
               Image img = icon.getImage().getScaledInstance(d.width/(myCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
               icon = new ImageIcon(img);
               myCodeLabel.get(i).setIcon(icon);
            }
         }
         for(int j = 0; j < 50; j++) {
            pf4.revalidate();
         }
      }
   }
   
   void look() { // �ڵ带 �߰��� ���� �����Ͽ� ���� ������Ͽ� �ؽ�Ʈ �� �� �÷��� �־��ִ� �޼���
      myCodeLabel = new ArrayList<JLabel>();
      Set keys2 = myCode.keySet();
      Iterator<Double> ite2 = keys2.iterator();
      
      int count = 0;
      while(ite2.hasNext()) {
         double su = ite2.next();
         int index = myCode.get(su).indexOf(" ");
         if(index == -1) {
            continue;
         }else {
            JLabel la = new JLabel("            ");
            Font font = new Font(Font.SANS_SERIF,Font.BOLD,0); 
            la.setFont(font);
            la.setHorizontalAlignment(JLabel.CENTER);
            la.setOpaque(true);
            myCodeLabel.add(la);
            myCodeLabel.get(count).setText(myCode.get(su));
            
            for(int i = 0; i < 100; i++) {
            myCodeLabel.get(count).revalidate();
            myCodeLabel.get(count).validate();
            }
            count++;
         }   
      }

   }
   
   void myCodeSend() { //���ڵ带 ���濡�� �����ִ� �޼���
      Set set = myCode.keySet();
      Iterator<Double> ite = set.iterator();
      String totalMyCOde = new String();
      while(ite.hasNext()) {
         double key = ite.next();
         totalMyCOde += myCode.get(key) + "/";
      }

      try {
         int send = 101;
         dos.writeInt(101);
         dos.writeUTF(totalMyCOde);
         dos.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   //110�� ����
   void remainingBalackCodes() {//���� ���ڵ带 �����ϰ� ���� ���ڵ���� ���濡�� �����Ͽ� ���� ���� ������ �����ϴ� �޼���
      String rmbcodes = new String(); //���� ���ڵ尡 ������ �������� �����ϴ� ���
      if(black.size() > 0) {
         for(int i = 0; i < black.size(); i++) {
            if(i == 0) {
               rmbcodes += black.get(i);
            }else {
               rmbcodes += "/" + black.get(i);
            }
         }
      }else {
         rmbcodes = "�������ڵ����";
      }
         try {
            int send = 110; 
            dos.writeInt(send);
            dos.writeUTF(rmbcodes);
            dos.flush();
         } catch (IOException e) {
            e.printStackTrace();
         }
      
   }
   
   //111�� ����
   void reaminingWhiteCodes() { //���� ȭ��Ʈ�ڵ带 �����ϰ� ���� ���ڵ���� ���濡�� �����Ͽ� ���� ���� ȭ��Ʈ���� �����ϴ� �޼���
      String rmwcodes = new String(); //���� ȭ��Ʈ�ڵ尡 ������ �������� �����ϴ� ���
      if(white.size() > 0) {
         for(int i = 0; i < white.size(); i++) {
            if(i == 0) {
               rmwcodes += white.get(i);
            }else {
               rmwcodes += "/" + white.get(i);
            }
         }
      }else {
         rmwcodes = "����ȭ��Ʈ�ڵ����";
      }
      try {
         int send = 111;
         dos.writeInt(send);
         dos.writeUTF(rmwcodes);
         dos.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   void blackSelect() { //���ڵ带 �߰��Ͽ��� �� �̸� ������â�� ���������� �����ϴ� �޼��� 
      
      pickCount++;
      boolean boo = true;
      Set keys = myCode.keySet();
      Iterator<Double> ite = keys.iterator();
      double ranPick = 0.0;
      if(myCode.size() != 0) {
         while(boo) {
            int su;
            try {
               su = ran.nextInt(12);
               ranPick = black.get(su);
            }catch(IndexOutOfBoundsException e) {
               continue;
            }
            black.remove(su);
            while(ite.hasNext()){
               if(ite.next() != Double.valueOf(ranPick)) {
                  boo = false;
               }
            }
         }
      } else {
         while(boo) {
            int su;
            try {
               su = ran.nextInt(12);
               ranPick = black.get(su);
            }catch(IndexOutOfBoundsException e) {
               continue;
            }
            boo = false;
            black.remove(su);
         }
      }
      myCode.put(ranPick,"�� " + (int)ranPick);
      la2.setText("������ �ڵ� �� : " + myCode.size() + "��");
      look();
      myCodeSend();
      
      ite = keys.iterator();
      int num = 0;
      while(ite.hasNext()) {
         if(ranPick == ite.next()) {
            break;
         }
         num++;
      }
      lastPickNum = String.valueOf(num);
      
      try {
         Thread.sleep(100);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      
      remainingBalackCodes();
      
      if(pickCount == 4 && firstBoo == true) {
         first.setVisible(true);
         firstBoo = false;
         pickmyCode.dispose();
      }else if(pickCount > 4) {
         pickmyCode.dispose();
         tf.setEnabled(true);
      }
      
      if(black.size() == 0) {
         blackPick.setEnabled(false);
      }
   }
   
   void whiteSelect() { //ȭ��Ʈ�ڵ带 �߰��Ͽ��� �� �̸� ������â�� ���������� �����ϴ� �޼��� 
      pickCount++;
      boolean boo = true;
      Set keys = myCode.keySet();
      Iterator<Double> ite = keys.iterator();
      double ranPick = 0.0;
      if(myCode.size() != 0) {
         while(boo) {
            int su;
            try {
            su = ran.nextInt(12);
            ranPick = white.get(su);
            }catch(IndexOutOfBoundsException e) {
               continue;
            }
            white.remove(su);
            while(ite.hasNext()){
               if(ite.next() != Double.valueOf(ranPick)) {
                  boo = false;
               }
            }
         }
      } else {
         while(boo) {
            int su;
            try {
               su = ran.nextInt(12);
               ranPick = white.get(su);
            }catch(IndexOutOfBoundsException e) {
               continue;
            }
            boo = false;
            white.remove(su);
         }
      }
      myCode.put(ranPick,"ȭ��Ʈ " + (int)(ranPick - 0.5));
      la2.setText("������ �ڵ� �� : " + myCode.size() + "��");
      look();
      myCodeSend();
      
      ite = keys.iterator();
      int num = 0;
      while(ite.hasNext()) {
         if(ranPick == ite.next()) {
            break;
         }
         num++;
      }
      lastPickNum = String.valueOf(num);
      
      try {
         Thread.sleep(100);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      reaminingWhiteCodes();
      
      if(pickCount >= 4) {
         pickmyCode.dispose();
      }
      if(pickCount == 4 && firstBoo == true) {
         first.setVisible(true);
         pickmyCode.dispose();
      }else if(pickCount > 4) {
         pickmyCode.dispose();
         tf.setEnabled(true);
      }
      if(white.size() == 0) {
         whitePick.setEnabled(false);
      }
   }
   
   public Uc() { //Ŭ���̾�Ʈ â ���� �޼���
      setLayout(new GridLayout(4,1));
      
      for(; black.size() < 12;) { 
         black.add(blackVal);
         blackVal += 1;
      }
      
      for(; white.size() < 12;) { 
         white.add(whiteVal);
         whiteVal += 1;
      }
      
      pickmyCode.setBounds(100 + 800/2 - 300/2, 100 + 800/2 - 200/2,300 ,200);
      pickmyCode.setTitle("�ٺ�ġ�ڵ�");
     
      pickmyCode.setLayout(new GridLayout(3,5));
      
      la1.setFont(font); //�ڵ� ���� ���̾˷α� ��
      p1.add(la1);
      
      JLabel laHeader = new JLabel("�ٺ�ġ�ڵ� ������Ӣ�");
  
      laHeader.setFont(font); 
      laHeader.setAlignmentX(Label.CENTER);
      pf1.add(laHeader);
      Font font = new Font("���ʷҵ���",Font.BOLD,30);
      laHeader.setFont(font);

      JPanel p2 = new JPanel();
      
      la2.setFont(font);
      p2.add(la2);
      
      JPanel p3 = new JPanel();
      p3.setLayout(new GridLayout(1,2));
      
      blackPick.setFont(font);
      blackPick.setForeground(Color.WHITE);
      blackPick.setBackground(Color.BLACK);
      
      whitePick.setFont(font);
      whitePick.setBackground(Color.WHITE);
      p3.add(blackPick);
      p3.add(whitePick);
      
      pickmyCode.add(p1);
      pickmyCode.add(p2);
      pickmyCode.add(p3);
      
      pickmyCode.setResizable(false);
      pickmyCode.setVisible(false);
      
      pf3.setLayout(new GridLayout(3,1));
      tf.setEnabled(false);
      tf.setFont(font);
      
      tf.addFocusListener(new FocusListener() {
      

         @Override
         public void focusGained(FocusEvent e) {// ��Ŀ�� ���϶��߸��� �ڵ带 �Է��ϼ��� �޼����� ������
            if(tf.getText().equals("�߸��� �ڵ带 �Է��ϼ���")) {
               tf.setText("");
            }
            
         }
         @Override
         public void focusLost(FocusEvent e) {// ��Ŀ�� �ƿ� �� �߸��� �ڵ带 �Է��ϼ��� �޼����� ������
            tf.setText("�߸��� �ڵ带 �Է��ϼ���");
         }
      });
       tf.addActionListener(new ActionListener() {// ���� �߸��� �ڵ尡 �´��� Ʋ�Ǵ����� ���� ��������

            @Override
            public void actionPerformed(ActionEvent e) {
               boolean boo = false;
               boolean boo2 = false; // 103, 104 ����
               System.out.println("------------------------------------");
               System.out.println("���� �߸��� �ڵ� �̸� :" + tf.getText());
               
               for(int i = 0; i < yourCodeLabel.size(); i++) {
                  if(tf.getText().equals(yourCodeLabel.get(i).getText())) {
                     System.out.println("���� �� " + tf.getText() + " �� ������ �ڵ� �߿� �ִ� �� : " + yourCodeLabel.get(i).getText());
                     boo = true;
                     boo2 = true;
                     Dimension d = pf2.getSize();
                     int last = yourCodeLabel.get(i).getText().indexOf(" ");
                     String BW= yourCodeLabel.get(i).getText().substring(0,last);
                     String BWnum = yourCodeLabel.get(i).getText().substring(last+1, yourCodeLabel.get(i).getText().length());
//                     yourCodeLabel.get(i).setBackground(Color.WHITE);
//                     yourCodeLabel.get(i).setForeground(Color.WHITE);
                     if(BW.equals("��")) {
                        ImageIcon icon = new ImageIcon("black\\b" + BWnum + ".png");
                       Image img = icon.getImage().getScaledInstance(d.width/(yourCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                       icon = new ImageIcon(img);
                       yourCodeLabel.get(i).setIcon(icon);
                    
                        System.out.println("���� ���� �������� : " + BW + BWnum);
                    
                     }else {
                        ImageIcon icon = new ImageIcon("white\\w" + BWnum + ".png");
                       Image img = icon.getImage().getScaledInstance(d.width/(yourCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                       icon = new ImageIcon(img);
                       yourCodeLabel.get(i).setIcon(icon);
                    
                       System.out.println("���� ȭ��Ʈ�� �������� : " + BW + BWnum);
                    
                     }
                     
                     for(int j = 0; j < 50; j++) {
                        pf2.revalidate();
                     }
                     
                     System.out.print("������ ���� ������ �ڵ带 ���� ��ġ�� : ");
                     for(int j = 0; j < myCounterCodeRightNum.size(); j++) {
                        System.out.print(myCounterCodeRightNum.get(j) + " ");
                     }
                     System.out.println();
                     
                     boolean boo3 = false;
                     for(int j = 0; j < myCounterCodeRightNum.size(); j++) {
                        if(i == myCounterCodeRightNum.get(j)) {
                           boo3 = true;
                        }
                     }
                     if(!boo3) {
                     myHit++;
                     
                     System.out.println("���� ���� ���� ");
                     System.out.println("������� ���� ���� ���� : " + myHit);
                     
                     trying.setVisible(true);
                     tf.setEnabled(false);//����
                     myCounterCodeRightNum.add(i);
                     }
                     
                     break;//��
                  }
               }      
       
             if(boo2) { //�߸��� �¾��� ��� UI���� �� ����
                 try {
                    //�¾����� �������� 103���̶� ���� �ڵ� �̸��� ������, ������ 103���� �ڵ� �̸��� �޾Ƽ�
                    //Ŭ���̾�Ʈ���� 103���� �ڵ� �̸��� ����.-> Ŭ���̾�Ʈ�� readInt if(num=103) 103���� �ڵ� �̸��� �޾��� ��
                    int send = 103;// ���� �ڵ带 ���߸� 103�ڵ带 ����
                    dos.writeInt(send);
                    dos.writeUTF(tf.getText());
                    dos.flush();
                    Thread.sleep(100);
                 } catch (IOException e1) {
                    e1.printStackTrace();
                 } catch (InterruptedException e1) {
                    e1.printStackTrace();
                 }
                 
                 if(myHit == yourCode.size()) {
                    victory.setVisible(true);
                    trying.setVisible(false);
                    int send1 = 105;// �����̱�� ���â�� �й�â�� ���� �ڵ�
                    try {
                  dos.writeInt(send1);
                  dos.flush();
               } catch (IOException e1) {
                  e1.printStackTrace();
               }
                 }
                 
           }else { //�߸��� Ʋ���� ��� UI���� �� ����
              String myLastPick = myCodeLabel.get(Integer.parseInt(lastPickNum)).getText();
              int index = myLastPick.indexOf(" ");
              String color = myLastPick.substring(0,index);
              String num = myLastPick.substring(index+1,myLastPick.length());
              Dimension d = pf4.getSize();
              System.out.println("���� Ʋ���� �������� ���� ���� ������ �ڵ� : " + myLastPick);
              System.out.println("==========  " + color + num+"==============");
              if(color.equals("��")) {
                 ImageIcon icon = new ImageIcon("black\\br" + num + ".png");
              Image img = icon.getImage().getScaledInstance(d.width/(myCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
              icon = new ImageIcon(img);
              myCodeLabel.get(Integer.parseInt(lastPickNum)).setIcon(icon);
              
              System.out.println("�׷��� ���� �������� ���� ���� ������ �� �ڵ� : " + myLastPick);
              
              }else {
                 ImageIcon icon = new ImageIcon("white\\wr" + num + ".png");
              Image img = icon.getImage().getScaledInstance(d.width/(myCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
              icon = new ImageIcon(img);
              myCodeLabel.get(Integer.parseInt(lastPickNum)).setIcon(icon);
              
              System.out.println("�׷��� ���� �������� ���� ���� ������ ȭ��Ʈ �ڵ� : " + myLastPick);
              
              }
              
              counterMyCodeRightNum.add(Integer.parseInt(lastPickNum));
              
              System.out.println("lastPickNum : "+ lastPickNum + "==================" );
              
              for(int i = 0; i < 50; i++) {
                  pf4.revalidate();
               }
              tf.setEnabled(false);
              last.setVisible(true);
              try {
                   int send = 104; //�����ڵ带 �� ������ ��� 104�ڵ带 ����
                   dos.writeInt(send);
                   dos.writeUTF(lastPickNum);
                   dos.flush();
                   System.out.println("-------104 ����------");
                   System.out.println("������ ��ȣ : " + lastPickNum);
                } catch (IOException e1) {
                   e1.printStackTrace();
             }
           }
               for(int i = 0; i < 1000; i++) {
                   revalidate();
                }
               tf.setText("");
         }});

      pf3.add(new Label());
      pf3.add(tf);
      pf3.add(new Label());
      
       yes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               trying.setVisible(false);
               tf.setEnabled(true);
            }
         });
         
         no.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               trying.setVisible(false);
               tf.setEnabled(false);
               try {
                  int send=120; //�߸��� �̾������ʰڴٰ� "�ƴϿ�" ��ư�� ������� 120��ȣ�� ����
                  last.setVisible(true);
               dos.writeInt(send);
               dos.flush();
            } catch (IOException e1) {
               e1.printStackTrace();
            }
               
            }
         });
         
      blackPick.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) { //������ ��ư
           beforeCountermyCodeHitList = new ArrayList<String>();
            for(int i = 0; i < counterMyCodeRightNum.size(); i++) {
               beforeCountermyCodeHitList.add(myCodeLabel.get(counterMyCodeRightNum.get(i)).getText());
            }
            blackSelect();
            iconSetting();
            for(int i = 0; i < 1000; i++) {
               revalidate();
            }
          //�ٽ� ���Ӱ� ����
            counterMyCodeRightNum = new ArrayList<Integer>(); 
          //���� ������ ���� �����ڵ� ����Ʈ�� ������ ���Ӱ� �ٲ� ���� �ڵ�� ��ġ�� ���� �� ����
            for(int i = 0; i < myCodeLabel.size(); i++) {
               for(int j = 0; j < beforeCountermyCodeHitList.size(); j++) {
                  if(myCodeLabel.get(i).getText().equals(beforeCountermyCodeHitList.get(j))) {
                     counterMyCodeRightNum.add(i);
                  }
               }
            }
         }
      });
         
      whitePick.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {//ȭ��Ʈ ���� ��ư
            beforeCountermyCodeHitList = new ArrayList<String>();
              for(int i = 0; i < counterMyCodeRightNum.size(); i++) {
                 beforeCountermyCodeHitList.add(myCodeLabel.get(counterMyCodeRightNum.get(i)).getText());
             }
            whiteSelect();
            iconSetting();
            for(int i = 0; i < 1000; i++) {
               revalidate();
            }
          //�ٽ� ���Ӱ� ����
            counterMyCodeRightNum = new ArrayList<Integer>(); 
          //���� ������ ���� �����ڵ� ����Ʈ�� ������ ���Ӱ� �ٲ� ���� �ڵ�� ��ġ�� ���� �� ����
            for(int i = 0; i < myCodeLabel.size(); i++) {
               for(int j = 0; j < beforeCountermyCodeHitList.size(); j++) {
                  if(myCodeLabel.get(i).getText().equals(beforeCountermyCodeHitList.get(j))) {
                     counterMyCodeRightNum.add(i);
                  }
               }
            }
         }
      });
      
      addWindowListener(this);
      
      pf4.setLayout(new GridLayout(1,1));
      pf1.setBackground(Color.GRAY);
      pf2.setBackground(Color.GRAY);
      pf3.setBackground(Color.GRAY);
      pf4.setBackground(Color.GRAY);

      add(pf1);
      add(pf2);
      add(pf3);
      add(pf4);
      //���� ������â ���� ��
      
      // 1:1�� ������ �Է��϶�� ���̾˷α�â ����
      Dialog counterpart = new Dialog(this);
      counterpart.setTitle("Davinci");
      counterpart.setLayout(new GridLayout(3,1));
      counterpart.setResizable(false);
      Panel counterpartPanel = new Panel();
      Label counterpartLabel = new Label("��� �г��� : ");
      counterpartLabel.setAlignment(Label.CENTER);
      font = new Font("���ʷҵ���",Font.BOLD,23);
      counterpartLabel.setFont(font);
      TextField counterpartTf = new TextField("               ");
      counterpartTf.setFont(font);
      counterpartTf.setText("");
      counterpartPanel.add(counterpartLabel);
      counterpartPanel.add(counterpartTf);
      
      JButton counterpartBtn = new JButton("Game Start!");
      font = new Font("���ʷҵ���",Font.BOLD,28);
      counterpartBtn.setBackground(Color.GRAY);
      counterpartBtn.setFont(font);
      counterpartBtn.setEnabled(false);
      
      counterpartTf.addKeyListener(new KeyAdapter() {//�г��ӿ� ���� �� ���� �ϰ�� �α��ι�ư ��Ȱ��ȭ
         @Override
         public void keyReleased(KeyEvent e) { 
            String counterBox =counterpartTf.getText();
               int isSpace = counterBox.indexOf(" ");
               if(isSpace != -1) {
                  counterpartBtn.setEnabled(false);
              }else if(counterBox.length()==0){
                 counterpartBtn.setEnabled(false);
              }else {
                 counterpartBtn.setEnabled(true);
              }
         }
      });
      
      counterpartTf.addFocusListener(new FocusAdapter() { //�г��ӿ� ���� �� ���� �ϰ�� �α��ι�ư ��Ȱ��ȭ
         @Override
         public void focusLost(FocusEvent e) {
            String counterBox =counterpartTf.getText();
               int isSpace = counterBox.indexOf(" ");
               if(isSpace != -1) {
                  counterpartBtn.setEnabled(false);
              }else if(counterBox.length()==0){
                 counterpartBtn.setEnabled(false);
              }else {
                 counterpartBtn.setEnabled(true);
              }
         }
      });
      counterpartBtn.addActionListener(new ActionListener() {//���� �г����� ������ ������
         @Override
         public void actionPerformed(ActionEvent arg0) {
            try {
               int send = 99; // 99:���� �г��� ����
               dos.writeInt(send);
               dos.writeUTF(counterpartTf.getText());
               dos.flush();
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }

            setEnabled(true);
            counterpart.setVisible(false);
            pickmyCode.setVisible(true);
            String counterData = counterpartTf.getText();
            counterNameLabel.setText(counterData);
         }
      });
      
      counterpart.add(counterpartPanel);
      counterpart.add(new Panel());
      counterpart.add(counterpartBtn);
      
      counterpart.addWindowListener(this);
      counterpart.setBounds(100 + 800/2 - 500/2, 100 + 800/2 - 200/2,500 ,200);
      counterpart.setVisible(false);
      //1:1�� ���̾˷α�â ���� �Ϸ�
      
      //ó�� ������ �� �г����� �Է��ϴ� �α���â ����
      font = new Font("���ʷҵ���",Font.BOLD,30);
      
      Dialog logIn = new Dialog(this);
      logIn.setTitle("Login");
      logIn.setLayout(new GridLayout(3,1));
      
      JPanel logInPanel = new JPanel();
      JLabel logInLabel = new JLabel("�г��� : ");
      
      font = new Font("���ʷҵ���",Font.BOLD,20);
      logInLabel.setAlignmentX(Label.CENTER);
      logInLabel.setFont(font);
      
      TextField logInTf = new TextField("             ");
      logInTf.setFont(font);
      logInTf.setText("");
      logInPanel.add(logInLabel);
      logInPanel.add(logInTf);
      
      JButton logInBtn = new JButton("Login");
      font = new Font("���ʷҵ���",Font.BOLD,28);
      logInBtn.setBackground(Color.GRAY);
      logInBtn.setFont(font);
       logInBtn.setEnabled(false);
      logInTf.addKeyListener(new KeyAdapter() { // �г����� ���̰� ������� �α��� ��ư false
         
         @Override
         public void keyReleased(KeyEvent e) {
            String logInBox =logInTf.getText();
               int isSpace = logInBox.indexOf(" ");
               if(isSpace != -1) {
                  logInBtn.setEnabled(false);
              }else if(logInBox.length()==0){
                 logInBtn.setEnabled(false);
              }else {
                 logInBtn.setEnabled(true);
              }
         }
      });
      
      logInTf.addFocusListener(new FocusAdapter() {// �ؽ�Ʈâ ����
         @Override
         public void focusLost(FocusEvent e) {
            String logInBox =logInTf.getText();
               int isSpace = logInBox.indexOf(" ");
               if(isSpace != -1) {
                  logInBtn.setEnabled(false);
              }else if(logInBox.length()==0){
                 logInBtn.setEnabled(false);
              }else {
                 logInBtn.setEnabled(true);
              }
         }
      });
      
         font = new Font("���ʷҵ���",Font.BOLD,28); //�α��ι�ư ����
         logInBtn.setFont(font);
         logInBtn.addActionListener(new ActionListener() { 
            
            @Override 
            public void actionPerformed(ActionEvent arg0) {

               try {
                  //�� �г��� ������ ����
                  int send = 98;
                  dos.writeInt(send);
                  dos.flush();
                  dos.writeUTF(logInTf.getText());
                  dos.flush();
                  
                  chatProgram.setVisible(true);
                  logIn.setVisible(false);
                  counterpart.setVisible(true);
               } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
               String myNameData =logInTf.getText();
               myNameLabel.setText(myNameData);
            }
         });
      
      logIn.add(logInPanel);
      logIn.add(new Panel());
      logIn.add(logInBtn);
      
      logIn.addWindowListener(this);
      logIn.setBounds(100 + 800/2 - 500/2, 100 + 800/2 - 160/2,500 ,160);
      logIn.setVisible(true);
      //�α���â ���� �Ϸ�
      
      //�����ϱ� ��ư ����
        first=new Dialog(this);
        first.setTitle("�ٺ�ġ�ڵ�");
        first.setLocation(450,450);
        first.setResizable(false);
        
        JPanel fp=new JPanel();
        first.setSize(300,200);
        
        Label fl=new Label("�����ϱ�"); 
        fl.setFont(font);
        
        JButton firstBtn=new JButton("�����ϱ�");
        font = new Font("���ʷҵ���",Font.BOLD,30);
        firstBtn.setFont(font);
        firstBtn.setPreferredSize(new Dimension(20,30));
        firstBtn.setBackground(Color.LIGHT_GRAY);

        first.add(firstBtn);
        firstBtn.addActionListener(new ActionListener() {
              
              @Override 
              public void actionPerformed(ActionEvent arg0) {
                 pickmyCode.setVisible(true);
                 
                 try {
                    int send = 102;//�����ϱ��ư�� ������ 102��ȣ�� ����
                    dos.writeInt(send);
                    dos.flush();                      
                    first.setVisible(false);
                    last.setVisible(false);
                 } catch (IOException e) {
                    e.printStackTrace();
                 }
              }
        });   
        
        //Next Timeâ ����
        last=new JDialog(this);
        font= new Font(font.SANS_SERIF,Font.BOLD,30);
        last.setResizable(false);
        
        Panel lp=new Panel();
        Label pl=new Label();
        
        last.setTitle("Next");
        pl.setText("Next Time..");
        pl.setFont(font);
        lp.add(pl);
        last.add(lp);
        last.setSize(300,200);
        last.setLocation(200,200);
        last.setVisible(false);
        last.setLocation(450, 450);
        
        //�¸��� â ����
        victory=new Dialog(this);
        victory.setResizable(false);
        victory.setBackground(Color.gray);
        font= new Font(font.SANS_SERIF,Font.BOLD,30);
        Panel lp1=new Panel();
        Label pl1=new Label();
        victory.setLayout(new GridLayout(2,0));
        victory.setLocation(450, 450);
        VicExitButton.setVisible(true);
        VicExitButton.setText("���� ����");
        victory.add(lp1);
        victory.add(VicExitButton);        
        

        pl1.setText("Victory!!!!");
        pl1.setFont(font);
        lp1.add(pl1);
        victory.setSize(300,200);
        victory.setVisible(false);
        victory.setResizable(false); 
        
        //�й��� â ����
        loser=new Dialog(this);
        font= new Font(font.SANS_SERIF,Font.BOLD,30);
        loser.setLayout(new GridLayout(2,0));

        Panel lp2=new Panel();
        Label pl2=new Label();
        
        pl2.setText("Loser!!!!!!");
        pl2.setFont(font);
        lp2.add(pl2);
        loser.setSize(300,200);
        loser.add(lp2);
        loser.setVisible(false);
        loser.setResizable(false); 
        loser.setLocation(450, 450);
        LosExitButton.setVisible(true);
        LosExitButton.setText("���� ����");
        loser.add(LosExitButton);

        trying = new JFrame("�ٺ�ġ�ڵ�");
        font = new Font(Font.SANS_SERIF,Font.BOLD,20);
        Label logInLabel1 = new Label("��� �߸� �Ͻðڽ��ϱ�?");
       
        logInLabel1.setBounds(50, 10, 300, 40);
        logInLabel1.setFont(font);
        trying.setSize(350, 140);
        trying.setLocationRelativeTo(null);
        trying.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        trying.setResizable(false);
       
        trying.getContentPane().setLayout(null);
        
        yes.setBounds(30, 50,122, 30);
        no.setBounds(182, 50,122, 30);
        trying.getContentPane().add(logInLabel1);

        trying.getContentPane().add(yes);
        trying.getContentPane().add(no);
        trying.getModalExclusionType();
        
        VicExitButton.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
               System.exit(0);
            }
         });
         LosExitButton.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
               System.exit(0);
            }
         });
        
        //ä��â ����
        // ���̵�, ���� ���̵� ���� ��ܿ� ����
        Panel name = new Panel();
        name.setBackground(Color.gray);
        name.setLayout(new GridLayout(2,2));
        name.add(new Label("�÷��̾�     �� �� ��         :      ", 2));name.add(myNameLabel);
        name.add(new Label("��        ��     �� �� ��         :      ", 2));name.add(counterNameLabel);
        name.setSize(10,10);
        add(name);
    
      chatProgram.setTitle("ä��â");
      chatProgram.setLayout(new BorderLayout());
      Panel chatP1 = new Panel();
      TextField chatTf = new TextField("                                                 ");
      chatTf.setText("ä���� �Է��ϼ���");
      chatTf.addFocusListener(new FocusListener() {   
         @Override
         public void focusLost(FocusEvent arg0) {
         }
         @Override
         public void focusGained(FocusEvent arg0) {
            chatTf.setText("");   
         }
      });
      chatTf.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent arg0) {
            if(chatTf.getText().equals("/clear")) { // ä�ÿ� "/clear" �Է½� ���� �ʱ�ȭ
               chat = "";
            }
            try {
               int isWhisper = chatTf.getText().indexOf("/w ");
               int isWhisper2 = chatTf.getText().indexOf("/�� ");
               
               
               if(isWhisper == -1 && isWhisper2 == -1) {
                  int send = 1;
                  dos.writeInt(send);
                  dos.writeUTF(chatTf.getText());
                  dos.flush();
                  
               }else {
                  int send = 2;
                  dos.writeInt(send);      
                  dos.writeUTF(chatTf.getText());
                  dos.flush();
               }
            } catch (IOException e) {
               e.printStackTrace();
            }
            chatTf.setText("");
         }   
      });
      
      chatP1.add(chatTf);
      
      chatTa.setText(whisperUsing);
      chatProgram.add(name,BorderLayout.NORTH);
      chatProgram.add(chatTa,BorderLayout.CENTER);
      chatProgram.add(chatP1,BorderLayout.SOUTH);
      chatProgram.setBounds(1200,0,400,1000);
      chatProgram.setVisible(false);
      chatProgram.addWindowListener(this);
      //ä��â ���� ��
      
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      
      setTitle("�ٺ�ġ�ڵ�");
      setBounds(0,0,1200,1000);
      setVisible(true);
      setEnabled(false);
   }
   

   @Override
   public void windowActivated(WindowEvent arg0) {
   }
   @Override
   public void windowClosed(WindowEvent arg0) {
   }
   @Override
   public void windowClosing(WindowEvent arg0) {
      int a = JOptionPane.showConfirmDialog(this, "���α׷��� �����Ͻðڽ��ϱ�?");
      System.out.println(a);
       if(a == JOptionPane.YES_OPTION) {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         }

      
   }
   @Override
   public void windowDeactivated(WindowEvent arg0) {
   }
   @Override
   public void windowDeiconified(WindowEvent arg0) {
   }
   @Override
   public void windowIconified(WindowEvent arg0) {   
   }
   @Override
   public void windowOpened(WindowEvent arg0) {
   }
   

   public static void main(String[] args) {
      Uc se = new Uc();

      //���� ����
      Socket sock = null;
      
      //�⺻ Ʋ
      InputStream is = null; 
      OutputStream os = null; 
      
      // �ڷḦ �ְ� ������ �Ҷ� � ������ ���� �� ������ ����.
      DataInputStream dis = null; 
      
      //������ ���
      BufferedInputStream bis = null;
      BufferedOutputStream bos = null;
      
      try {
         sock = new Socket("localHost",5000); //������ ���� ip,port ���� ���
         
         //�⺻ Ʋ
         is = sock.getInputStream();
         os = sock.getOutputStream();
         bis = new BufferedInputStream(is);
         bos = new BufferedOutputStream(os);
         
         //��밡 ������ ������ ������ Ÿ���� �������� �ľ�
         dis = new DataInputStream(bis);
         //��뿡�� ������ ������ Ÿ���� �������� ����
         dos = new DataOutputStream(bos);
         
         while(true) {
            
            int num = -1;
            num = dis.readInt();//���⼭ ��ȣ�ڵ带 ����
            System.out.println(num);
            
            if(num == -1) {
               System.out.println("�߸��� �����Ͱ� ���۵Ǿ����ϴ�.");
            }else if(num < 100) { 
               // 100 ���� ���� ��� ä�ùۿ� ���� �ȿ����� �״�� ����
               
               String msg = null;
               msg = dis.readUTF();
               msg += "\n";

               chat += msg;
               chatTa.setText(chat);

            }else if(num == 101){
               // 101�� ���� �����̴� Ʈ�������� ����
               //������ �ִ� ���� �ڵ�鿡�� ���� ���� ���� �ڵ���� �̸��� ����
               ArrayList<String> beforeMyHitList = new ArrayList<>();
               for(int i = 0; i < myCounterCodeRightNum.size(); i++ ) {
                  beforeMyHitList.add(yourCodeLabel.get(myCounterCodeRightNum.get(i)).getText());
               }
               
               String yourCodeSetting = dis.readUTF();
               String[] yourCodeArray = yourCodeSetting.split("/");
               yourCode = new TreeMap<Double, String>();
               
               for(int i = 0; i < yourCodeArray.length; i++) {
                  String yCode = yourCodeArray[i];
                  int colorIndex = yCode.indexOf(" ");
                  String codeColor = yCode.substring(0,colorIndex);
                  double codeNum = Integer.parseInt(yCode.substring(colorIndex+1,yCode.length()));
                  
                  if(codeColor.equals("ȭ��Ʈ")) {
                     codeNum += 0.5;
                  }
                  yourCode.put(codeNum, yCode);
               }

               yourCodeLabel = new ArrayList<JLabel>();
               Set keys = yourCode.keySet();
               Iterator<Double> ite = keys.iterator();
               
               int count = 0;
               while(ite.hasNext()) {
                  double su = ite.next();
                  
                  int index = yourCode.get(su).indexOf(" ");
                  
                  JLabel la = new JLabel("            ");
                  Font font = new Font(Font.SANS_SERIF,Font.BOLD,0);
                  la.setFont(font);
                  la.setAlignmentX(Label.CENTER);
                  la.setAlignmentY(Label.CENTER);
                  yourCodeLabel.add(la);
                  yourCodeLabel.get(count).setOpaque(true);
                  yourCodeLabel.get(count).setText(yourCode.get(su));
                  count++;
               }

               pf2.removeAll();
               pf2.setLayout(new GridLayout(1,yourCodeLabel.size()));
               for(int i = 0 ; i < yourCodeLabel.size(); i++) {
                  pf2.add(yourCodeLabel.get(i));
                  
                  Dimension d = pf2.getSize();
                  yourCodeLabel.get(i).setBackground(Color.GRAY);
                  int index = yourCodeLabel.get(i).getText().indexOf(" ");
                  String color = yourCodeLabel.get(i).getText().substring(0,index);
                  String WBnum = yourCodeLabel.get(i).getText().substring(index+1,yourCodeLabel.get(i).getText().length());
                  
                  if(color.equals("��")) {
                     boolean rightBoo = false;
                     
                     for(int j = 0; j < beforeMyHitList.size(); j++) {
                        if(yourCodeLabel.get(i).getText().equals(beforeMyHitList.get(j))) {
                           rightBoo = true;
                        }
                     }
                     
                     if(rightBoo) {
                        ImageIcon icon = new ImageIcon("black\\b" + WBnum + ".png");
                        Image img = icon.getImage().getScaledInstance(d.width/(yourCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                        icon = new ImageIcon(img);
                        yourCodeLabel.get(i).setIcon(icon);
                     }else {
                        ImageIcon icon = new ImageIcon("black\\bb.png");
                        Image img = icon.getImage().getScaledInstance(d.width/(yourCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                        icon = new ImageIcon(img);
                        yourCodeLabel.get(i).setIcon(icon);
                     }

                  }else {
                     boolean rightBoo = false;
                     
                     for(int j = 0; j < beforeMyHitList.size(); j++) {
                        if(yourCodeLabel.get(i).getText().equals(beforeMyHitList.get(j))) {
                           rightBoo = true;
                        }
                     }
                     
                     if(rightBoo) {
                        ImageIcon icon = new ImageIcon("white\\w" + WBnum + ".png");
                        Image img = icon.getImage().getScaledInstance(d.width/(yourCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                        icon = new ImageIcon(img);
                        yourCodeLabel.get(i).setIcon(icon);
                     }else {
                        ImageIcon icon = new ImageIcon("white\\wb.png");
                        Image img = icon.getImage().getScaledInstance(d.width/(yourCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                        icon = new ImageIcon(img);
                        yourCodeLabel.get(i).setIcon(icon);
                     }
                  }
                  for(int j = 0; j < 100; j++){
                    yourCodeLabel.get(i).revalidate();
                     pf4.revalidate();
                  }
               }
             //�ٽ� ���Ӱ� ���� ���� ������ ���� �����ڵ� ����Ʈ�� ������ ���Ӱ� �ٲ� ���� �ڵ�� ��ġ�� ���� �� ����
               myCounterCodeRightNum = new ArrayList<Integer>(); 
               for(int i = 0; i < yourCodeLabel.size(); i++) {
                  for(int j = 0; j < beforeMyHitList.size(); j++) {
                     if(yourCodeLabel.get(i).getText().equals(beforeMyHitList.get(j))) {
                        myCounterCodeRightNum.add(i);
                     }
                  }
               }
               for(int i = 0; i < 1000; i++) {
                  se.revalidate();
               }
               }else if(num == 102) { //102:�����ϱ�
                  firstBoo = false;
                  first.setVisible(false);
                  last.setVisible(true);
               }else if(num == 103) { //103:���� �ڵ带 ����
                  tf.setEnabled(false);
                  String msg = null;
                  msg = dis.readUTF();
                  System.out.println("������ ���� �� �ڵ� : " + msg);
                  for(int i = 0; i < myCodeLabel.size(); i++) {
                     if(msg.equals(myCodeLabel.get(i).getText())) {
                        int last = myCodeLabel.get(i).getText().indexOf(" ");
                        String BW= myCodeLabel.get(i).getText().substring(0,last);
                        String BWnum = myCodeLabel.get(i).getText().substring(last+1, myCodeLabel.get(i).getText().length());
                        
                        Dimension d = pf4.getSize();
                        if(BW.equals("��")) {
                          ImageIcon icon = new ImageIcon("black\\br" + BWnum + ".png");
                          Image img = icon.getImage().getScaledInstance(d.width/(myCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                          icon = new ImageIcon(img);
                          myCodeLabel.get(i).setIcon(icon);
                          
                          System.out.println("�׷���  BW�� " + BW + "�� BWnum��" + BWnum);
                          
                          
                        }else {
                          ImageIcon icon = new ImageIcon("white\\wr" + BWnum + ".png");
                          Image img = icon.getImage().getScaledInstance(d.width/(myCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                          icon = new ImageIcon(img);
                          myCodeLabel.get(i).setIcon(icon);
                          System.out.println("�׷���  BW�� " + BW + "�� BWnum��" + BWnum);
                        }
                        
                        for(int j = 0; j < 100; j++){
                           pf4.revalidate();
                           myCodeLabel.get(i).revalidate();
                        }
                        counterMyCodeRightNum.add(i);
                        System.out.println("������� ������ ���� ���� : " + counterMyCodeRightNum.size());
                        break;
                     }
                  }  
                  for(int i = 0; i < counterMyCodeRightNum.size(); i++) {
                     System.out.println("counterMyCodeRightNum : " + counterMyCodeRightNum.get(i));
                  }
                  
               }else if(num == 104 ) { //���� �ڵ带 ������
                  String counterLastPickNum = null;
                  counterLastPickNum = dis.readUTF();
                  myCounterCodeRightNum.add(Integer.parseInt(counterLastPickNum));
                  
                  int counterLastlabelNum = myCounterCodeRightNum.get(myCounterCodeRightNum.size()-1);
                  int colorIndex = yourCodeLabel.get(counterLastlabelNum).getText().indexOf(" ");
                  String counterlastColor = yourCodeLabel.get(counterLastlabelNum).getText().substring(0,colorIndex);
                  String counterlastNum = yourCodeLabel.get(counterLastlabelNum).getText().substring(colorIndex+1, yourCodeLabel.get(counterLastlabelNum).getText().length());
                  System.out.println(counterlastColor + counterlastNum);
                  Dimension d = pf4.getSize();
                  if(counterlastColor.equals("��")){
                    ImageIcon icon = new ImageIcon("black\\b" + counterlastNum + ".png");
                    Image img = icon.getImage().getScaledInstance(d.width/(yourCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                    icon = new ImageIcon(img);
                    yourCodeLabel.get(counterLastlabelNum).setIcon(icon);
                  }else {
                    ImageIcon icon = new ImageIcon("white\\w" + counterlastNum + ".png");
                    Image img = icon.getImage().getScaledInstance(d.width/(yourCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                    icon = new ImageIcon(img);
                    yourCodeLabel.get(counterLastlabelNum).setIcon(icon);
                  }
                  
                  for(int j = 0; j < 50; j++){
                  yourCodeLabel.get(counterLastlabelNum).revalidate();
                  pf4.revalidate();
               }
                  myHit++;
                  System.out.println("������ Ʋ���� ������ �ڵ� ���� ������� ���� ���� ���� : " + myHit);
                  
                  last.setVisible(false);
                  firstBoo = false;

                  if(black.size() < 1) {
                  blackPick.setVisible(false);
                  blackPick.revalidate();
               }
                  
               if(white.size() < 1) {
                  whitePick.setVisible(false);
                  whitePick.revalidate();
               }
                  
               if(!(black.size() < 1 && white.size() < 1)) {
                  se.pickmyCode.setVisible(true);
                  
               }else {
                  tf.setEnabled(true);
               }
               
               se.revalidate();
               }else if(num == 105 ) { // 105:���� �̰������ 
                  last.setVisible(false);
                  loser.setVisible(true);
                  se.revalidate();
            }else if(num == 110) { // 110:���濡�� ���� ���ڵ带 ����
               String remainBlackCodes = dis.readUTF();
               if(remainBlackCodes.equals("�������ڵ����")) {
                  black.remove(0);
               }else {
                  String[] rbc = remainBlackCodes.split("/");
                  black = new ArrayList<Double>();
                  for(int i = 0; i < rbc.length; i++) {
                     black.add(Double.parseDouble(rbc[i]));
                  }
               }
            }else if(num == 111) { //111:���濡�� ���� ȭ��Ʈ�ڵ带 ����
               String remainWhiteCodes = dis.readUTF();
               if(remainWhiteCodes.equals("����ȭ��Ʈ�ڵ����")) {
                  white.remove(0);
               }else {
                  String[] rbc = remainWhiteCodes.split("/");
                  
                  white = new ArrayList<Double>();
                  for(int i = 0; i < rbc.length; i++) {
                     white.add(Double.parseDouble(rbc[i]));
                  }
               }
            }else if(num == 120) { // 120:����Ͻðڽ��ϱ� â �ƴϿ��ư ��ȣ
                    last.setVisible(false);
                    
                    if(black.size() == 0) {
                    blackPick.setEnabled(false);
                 }
                 
                 if(white.size() == 0) {
                    whitePick.setEnabled(false);
                 }
                    
                 if(!(black.size() == 0 && white.size() == 0)) {
                    se.pickmyCode.setVisible(true);
                    
                 }else {
                    tf.setEnabled(true);
                 }
                    se.revalidate();
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         try {
            if(dis != null) {dis.close();}
            if(bis != null) {bis.close();}
            if(is != null) {is.close();}
            
            if(dos != null) {dos.close();}
            if(bos != null) {bos.close();}
            if(os != null) {os.close();}
            
            if(sock != null){sock.close();}
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   
   }
   @Override
   public void actionPerformed(ActionEvent e) {
   }
}