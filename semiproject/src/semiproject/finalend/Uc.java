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
   
   static DataOutputStream dos = null; //채팅인지 게임인지 구분 신호 전달

   static Dialog first; //먼저하기 버튼
   static boolean firstBoo = true;
   
    static Dialog last; //Next Time메세지창
    
    static Dialog victory; //승리의 창
  
    static Dialog loser; //패배의 창
    
    static JFrame trying; //계속하시겠습니까 창
    
   Dialog chatProgram = new Dialog(this);
   static TextArea chatTa = new TextArea();
   static String whisperUsing = "귓속말을 사용하시려면 아래 예시와 같이 사용해주시길 바랍니다.\n 예시1) /w 강아지 안녕하세요 \n 예시2) /ㅈ 127.0.0.13 반갑습니다\n/clear : 채팅 내용 지우기\n";
   //전체채팅과 귓속말이 올때마다 채팅창에 전부 추가하는 용도
   static String chat = whisperUsing; 
   
   //게임 
   static TreeMap<Double, String> myCode = new TreeMap<>(); //내 코드
   static ArrayList<JLabel> myCodeLabel; //내 코드 세팅
   
   static TreeMap<Double, String> yourCode; //상대방 코드
   static ArrayList<JLabel> yourCodeLabel; //상대 코드 세팅
   
   Random ran = new Random();
   
   static ArrayList<Double> black = new ArrayList<>(); //블랙코드를 담는 공간
   static ArrayList<Double> white = new ArrayList<>(); //화이트코드를 담는 공간
   
   Double blackVal = 0.0; //블랙코드를 앞으로 주기위해 화이트코드값을 0.5더 줌
   Double whiteVal = 0.5;

   Dialog pickmyCode = new Dialog(this); // 내가 고른 코드
   Font font = new Font("함초롬돋음",Font.BOLD,20); // 폰트 기본 셋팅 

   
   JPanel p1 = new JPanel();
   JLabel la1 = new JLabel("추리할 코드를 입력하세요");
   
   
   static Label la2 = new Label("선택한 코드 수 : 0개");
   
   static int pickCount = 0;
   
   static JButton blackPick = new JButton("블랙");
   static JButton whitePick = new JButton("화이트");
   
   static JButton yes=new JButton("예");
   static JButton no=new JButton("아니요");
   
   static JButton VicExitButton = new JButton("게임 종료");
   static JButton LosExitButton = new JButton("게임 종료");
   
   JPanel pf1 = new JPanel();
   static JPanel pf2 = new JPanel(); // 상대 코드
   JPanel pf3 = new JPanel();
   static JPanel pf4 = new JPanel();
   
   int myCodeCount = 0;
   
   static TextField tf = new TextField("상대방의 코드를 추리해보세요");
   
   static int myHit = 0; // 내가 상대방의 코드를 맞춘 개수
   
   static ArrayList<Integer> myCounterCodeRightNum = new ArrayList<>(); 
   //내가 상대방의 코드를 맞춘 라벨 번호 0, 1 등 담고 상대방의 코드를 받아올 때 라벨 셋팅 후 내가 맞춘 상대방의 코드들을 폰트색을
   //보이게 변경하는 것. 410번째 줄 정도에 번호 담으면 됨.
   
   
   static ArrayList<Integer> counterMyCodeRightNum = new ArrayList<>();
   //103번을 받았으면 그에 해당되는 라벨 번호를 찾았을때 그 번호를 여기에 저장하고 코드101번 받을 때 색상을 다시 유지할거임
   
   static String lastPickNum = "-1"; //마지막에 뽑은 내 코드의 위치를 저장 
 
   static Label myNameLabel = new Label(); // 내 이름 넣는 라벨창
   static Label counterNameLabel = new Label(); //상대방 넣는 라벨창
  
   ArrayList<String> beforeCountermyCodeHitList = new ArrayList<String>(); //내가맞춘 코드를 저장하는 공간
   
   void iconSetting() { //셋팅한 라벨에 아이콘을 넣어주는 메서드
      pf4.removeAll();
      pf4.setLayout(new GridLayout(1,myCodeLabel.size()));
      
      for(int i = 0 ; i < myCodeLabel.size(); i++) {
         pf4.add(myCodeLabel.get(i));
         Dimension d = pf4.getSize();
         
         int index = myCodeLabel.get(i).getText().indexOf(" ");
         String color = myCodeLabel.get(i).getText().substring(0,index);
         String num = myCodeLabel.get(i).getText().substring(index+1,myCodeLabel.get(i).getText().length());
         myCodeLabel.get(i).setBackground(Color.GRAY);         
         if(color.equals("블랙")) {
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
   
   void look() { // 코드를 추가한 것을 포함하여 라벨을 재셋팅하여 텍스트 및 라벨 컬러를 넣어주는 메서드
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
   
   void myCodeSend() { //내코드를 상대방에게 보여주는 메서드
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
   
   //110번 전송
   void remainingBalackCodes() {//내가 블랙코드를 선택하고 남은 블랙코드들을 상대방에게 전달하여 서로 남은 블랙들을 교류하는 메서드
      String rmbcodes = new String(); //남은 블랙코드가 없음을 상대방한테 전달하는 기능
      if(black.size() > 0) {
         for(int i = 0; i < black.size(); i++) {
            if(i == 0) {
               rmbcodes += black.get(i);
            }else {
               rmbcodes += "/" + black.get(i);
            }
         }
      }else {
         rmbcodes = "남은블랙코드없음";
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
   
   //111번 전송
   void reaminingWhiteCodes() { //내가 화이트코드를 선택하고 남은 블랙코드들을 상대방에게 전달하여 서로 남은 화이트들을 교류하는 메서드
      String rmwcodes = new String(); //남은 화이트코드가 없음을 상대방한테 전달하는 기능
      if(white.size() > 0) {
         for(int i = 0; i < white.size(); i++) {
            if(i == 0) {
               rmwcodes += white.get(i);
            }else {
               rmwcodes += "/" + white.get(i);
            }
         }
      }else {
         rmwcodes = "남은화이트코드없음";
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
   
   void blackSelect() { //블랙코드를 추가하였을 때 이를 프레임창에 종합적으로 세팅하는 메서드 
      
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
      myCode.put(ranPick,"블랙 " + (int)ranPick);
      la2.setText("선택한 코드 수 : " + myCode.size() + "개");
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
   
   void whiteSelect() { //화이트코드를 추가하였을 때 이를 프레임창에 종합적으로 세팅하는 메서드 
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
      myCode.put(ranPick,"화이트 " + (int)(ranPick - 0.5));
      la2.setText("선택한 코드 수 : " + myCode.size() + "개");
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
   
   public Uc() { //클라이언트 창 세팅 메서드
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
      pickmyCode.setTitle("다빈치코드");
     
      pickmyCode.setLayout(new GridLayout(3,5));
      
      la1.setFont(font); //코드 선택 다이알로그 라벨
      p1.add(la1);
      
      JLabel laHeader = new JLabel("다빈치코드 보드게임♠");
  
      laHeader.setFont(font); 
      laHeader.setAlignmentX(Label.CENTER);
      pf1.add(laHeader);
      Font font = new Font("함초롬돋움",Font.BOLD,30);
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
         public void focusGained(FocusEvent e) {// 포커스 인일때추리할 코드를 입력하세요 메세지를 없에줌
            if(tf.getText().equals("추리할 코드를 입력하세요")) {
               tf.setText("");
            }
            
         }
         @Override
         public void focusLost(FocusEvent e) {// 포커스 아웃 시 추리할 코드를 입력하세요 메세지를 보여줌
            tf.setText("추리할 코드를 입력하세요");
         }
      });
       tf.addActionListener(new ActionListener() {// 내가 추리한 코드가 맞는지 틀렷는지에 따른 엑션퍼폼

            @Override
            public void actionPerformed(ActionEvent e) {
               boolean boo = false;
               boolean boo2 = false; // 103, 104 구분
               System.out.println("------------------------------------");
               System.out.println("내가 추리한 코드 이름 :" + tf.getText());
               
               for(int i = 0; i < yourCodeLabel.size(); i++) {
                  if(tf.getText().equals(yourCodeLabel.get(i).getText())) {
                     System.out.println("내가 쓴 " + tf.getText() + " 가 상대방의 코드 중에 있는 것 : " + yourCodeLabel.get(i).getText());
                     boo = true;
                     boo2 = true;
                     Dimension d = pf2.getSize();
                     int last = yourCodeLabel.get(i).getText().indexOf(" ");
                     String BW= yourCodeLabel.get(i).getText().substring(0,last);
                     String BWnum = yourCodeLabel.get(i).getText().substring(last+1, yourCodeLabel.get(i).getText().length());
//                     yourCodeLabel.get(i).setBackground(Color.WHITE);
//                     yourCodeLabel.get(i).setForeground(Color.WHITE);
                     if(BW.equals("블랙")) {
                        ImageIcon icon = new ImageIcon("black\\b" + BWnum + ".png");
                       Image img = icon.getImage().getScaledInstance(d.width/(yourCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                       icon = new ImageIcon(img);
                       yourCodeLabel.get(i).setIcon(icon);
                    
                        System.out.println("내가 블랙을 맞췄을때 : " + BW + BWnum);
                    
                     }else {
                        ImageIcon icon = new ImageIcon("white\\w" + BWnum + ".png");
                       Image img = icon.getImage().getScaledInstance(d.width/(yourCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                       icon = new ImageIcon(img);
                       yourCodeLabel.get(i).setIcon(icon);
                    
                       System.out.println("내가 화이트를 맞췄을때 : " + BW + BWnum);
                    
                     }
                     
                     for(int j = 0; j < 50; j++) {
                        pf2.revalidate();
                     }
                     
                     System.out.print("기존에 내가 상대방의 코드를 맞춘 위치들 : ");
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
                     
                     System.out.println("맞춘 개수 증가 ");
                     System.out.println("현재까지 내가 맞춘 갯수 : " + myHit);
                     
                     trying.setVisible(true);
                     tf.setEnabled(false);//용훈
                     myCounterCodeRightNum.add(i);
                     }
                     
                     break;//나
                  }
               }      
       
             if(boo2) { //추리가 맞았을 경우 UI변동 및 전송
                 try {
                    //맞았으면 서버한테 103번이랑 맞춘 코드 이름을 보내고, 서버는 103번과 코드 이름을 받아서
                    //클라이언트한테 103번과 코드 이름을 보냄.-> 클라이언트가 readInt if(num=103) 103번과 코드 이름을 받았을 때
                    int send = 103;// 상대방 코드를 맞추면 103코드를 보냄
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
                    int send1 = 105;// 내가이기면 상대창에 패배창을 띄우는 코드
                    try {
                  dos.writeInt(send1);
                  dos.flush();
               } catch (IOException e1) {
                  e1.printStackTrace();
               }
                 }
                 
           }else { //추리가 틀렸을 경우 UI변동 및 전송
              String myLastPick = myCodeLabel.get(Integer.parseInt(lastPickNum)).getText();
              int index = myLastPick.indexOf(" ");
              String color = myLastPick.substring(0,index);
              String num = myLastPick.substring(index+1,myLastPick.length());
              Dimension d = pf4.getSize();
              System.out.println("내가 틀려서 마지막에 뽑은 픽을 보여줄 코드 : " + myLastPick);
              System.out.println("==========  " + color + num+"==============");
              if(color.equals("블랙")) {
                 ImageIcon icon = new ImageIcon("black\\br" + num + ".png");
              Image img = icon.getImage().getScaledInstance(d.width/(myCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
              icon = new ImageIcon(img);
              myCodeLabel.get(Integer.parseInt(lastPickNum)).setIcon(icon);
              
              System.out.println("그래서 내가 마지막에 뽑은 픽을 보여줄 블랙 코드 : " + myLastPick);
              
              }else {
                 ImageIcon icon = new ImageIcon("white\\wr" + num + ".png");
              Image img = icon.getImage().getScaledInstance(d.width/(myCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
              icon = new ImageIcon(img);
              myCodeLabel.get(Integer.parseInt(lastPickNum)).setIcon(icon);
              
              System.out.println("그래서 내가 마지막에 뽑은 픽을 보여줄 화이트 코드 : " + myLastPick);
              
              }
              
              counterMyCodeRightNum.add(Integer.parseInt(lastPickNum));
              
              System.out.println("lastPickNum : "+ lastPickNum + "==================" );
              
              for(int i = 0; i < 50; i++) {
                  pf4.revalidate();
               }
              tf.setEnabled(false);
              last.setVisible(true);
              try {
                   int send = 104; //상대방코드를 못 맞췄을 경우 104코드를 보냄
                   dos.writeInt(send);
                   dos.writeUTF(lastPickNum);
                   dos.flush();
                   System.out.println("-------104 보냄------");
                   System.out.println("마지막 번호 : " + lastPickNum);
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
                  int send=120; //추리를 이어하지않겠다고 "아니요" 버튼을 누른경우 120신호를 보냄
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
         public void mouseClicked(MouseEvent e) { //블랙선택 버튼
           beforeCountermyCodeHitList = new ArrayList<String>();
            for(int i = 0; i < counterMyCodeRightNum.size(); i++) {
               beforeCountermyCodeHitList.add(myCodeLabel.get(counterMyCodeRightNum.get(i)).getText());
            }
            blackSelect();
            iconSetting();
            for(int i = 0; i < 1000; i++) {
               revalidate();
            }
          //다시 새롭게 만들어서
            counterMyCodeRightNum = new ArrayList<Integer>(); 
          //내가 이전에 맞춘 상대방코드 리스트를 가져와 새롭게 바뀐 상대방 코드라벨 위치에 맞춰 재 셋팅
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
         public void mouseClicked(MouseEvent e) {//화이트 선택 버튼
            beforeCountermyCodeHitList = new ArrayList<String>();
              for(int i = 0; i < counterMyCodeRightNum.size(); i++) {
                 beforeCountermyCodeHitList.add(myCodeLabel.get(counterMyCodeRightNum.get(i)).getText());
             }
            whiteSelect();
            iconSetting();
            for(int i = 0; i < 1000; i++) {
               revalidate();
            }
          //다시 새롭게 만들어서
            counterMyCodeRightNum = new ArrayList<Integer>(); 
          //내가 이전에 맞춘 상대방코드 리스트를 가져와 새롭게 바뀐 상대방 코드라벨 위치에 맞춰 재 셋팅
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
      //게임 프레임창 세팅 끝
      
      // 1:1할 상대방을 입력하라는 다이알로그창 세팅
      Dialog counterpart = new Dialog(this);
      counterpart.setTitle("Davinci");
      counterpart.setLayout(new GridLayout(3,1));
      counterpart.setResizable(false);
      Panel counterpartPanel = new Panel();
      Label counterpartLabel = new Label("상대 닉네임 : ");
      counterpartLabel.setAlignment(Label.CENTER);
      font = new Font("함초롬돋음",Font.BOLD,23);
      counterpartLabel.setFont(font);
      TextField counterpartTf = new TextField("               ");
      counterpartTf.setFont(font);
      counterpartTf.setText("");
      counterpartPanel.add(counterpartLabel);
      counterpartPanel.add(counterpartTf);
      
      JButton counterpartBtn = new JButton("Game Start!");
      font = new Font("함초롬돋음",Font.BOLD,28);
      counterpartBtn.setBackground(Color.GRAY);
      counterpartBtn.setFont(font);
      counterpartBtn.setEnabled(false);
      
      counterpartTf.addKeyListener(new KeyAdapter() {//닉네임에 띄어쓰기 및 공백 일경우 로그인버튼 비활성화
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
      
      counterpartTf.addFocusListener(new FocusAdapter() { //닉네임에 띄어쓰기 및 공백 일경우 로그인버튼 비활성화
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
      counterpartBtn.addActionListener(new ActionListener() {//상대방 닉네임을 서버에 전송함
         @Override
         public void actionPerformed(ActionEvent arg0) {
            try {
               int send = 99; // 99:상대방 닉네임 전송
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
      //1:1할 다이알로그창 세팅 완료
      
      //처음 시작할 때 닉네임을 입력하는 로그인창 세팅
      font = new Font("함초롬돋음",Font.BOLD,30);
      
      Dialog logIn = new Dialog(this);
      logIn.setTitle("Login");
      logIn.setLayout(new GridLayout(3,1));
      
      JPanel logInPanel = new JPanel();
      JLabel logInLabel = new JLabel("닉네임 : ");
      
      font = new Font("함초롬돋음",Font.BOLD,20);
      logInLabel.setAlignmentX(Label.CENTER);
      logInLabel.setFont(font);
      
      TextField logInTf = new TextField("             ");
      logInTf.setFont(font);
      logInTf.setText("");
      logInPanel.add(logInLabel);
      logInPanel.add(logInTf);
      
      JButton logInBtn = new JButton("Login");
      font = new Font("함초롬돋음",Font.BOLD,28);
      logInBtn.setBackground(Color.GRAY);
      logInBtn.setFont(font);
       logInBtn.setEnabled(false);
      logInTf.addKeyListener(new KeyAdapter() { // 닉네임의 길이가 없을경우 로그인 버튼 false
         
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
      
      logInTf.addFocusListener(new FocusAdapter() {// 텍스트창 설정
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
      
         font = new Font("함초롬돋음",Font.BOLD,28); //로그인버튼 세팅
         logInBtn.setFont(font);
         logInBtn.addActionListener(new ActionListener() { 
            
            @Override 
            public void actionPerformed(ActionEvent arg0) {

               try {
                  //내 닉네임 서버에 전달
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
      //로그인창 세팅 완료
      
      //먼저하기 버튼 세팅
        first=new Dialog(this);
        first.setTitle("다빈치코드");
        first.setLocation(450,450);
        first.setResizable(false);
        
        JPanel fp=new JPanel();
        first.setSize(300,200);
        
        Label fl=new Label("선정하기"); 
        fl.setFont(font);
        
        JButton firstBtn=new JButton("먼저하기");
        font = new Font("함초롬돋음",Font.BOLD,30);
        firstBtn.setFont(font);
        firstBtn.setPreferredSize(new Dimension(20,30));
        firstBtn.setBackground(Color.LIGHT_GRAY);

        first.add(firstBtn);
        firstBtn.addActionListener(new ActionListener() {
              
              @Override 
              public void actionPerformed(ActionEvent arg0) {
                 pickmyCode.setVisible(true);
                 
                 try {
                    int send = 102;//먼저하기버튼을 누르면 102신호를 보냄
                    dos.writeInt(send);
                    dos.flush();                      
                    first.setVisible(false);
                    last.setVisible(false);
                 } catch (IOException e) {
                    e.printStackTrace();
                 }
              }
        });   
        
        //Next Time창 세팅
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
        
        //승리의 창 세팅
        victory=new Dialog(this);
        victory.setResizable(false);
        victory.setBackground(Color.gray);
        font= new Font(font.SANS_SERIF,Font.BOLD,30);
        Panel lp1=new Panel();
        Label pl1=new Label();
        victory.setLayout(new GridLayout(2,0));
        victory.setLocation(450, 450);
        VicExitButton.setVisible(true);
        VicExitButton.setText("게임 종료");
        victory.add(lp1);
        victory.add(VicExitButton);        
        

        pl1.setText("Victory!!!!");
        pl1.setFont(font);
        lp1.add(pl1);
        victory.setSize(300,200);
        victory.setVisible(false);
        victory.setResizable(false); 
        
        //패배의 창 세팅
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
        LosExitButton.setText("게임 종료");
        loser.add(LosExitButton);

        trying = new JFrame("다빈치코드");
        font = new Font(Font.SANS_SERIF,Font.BOLD,20);
        Label logInLabel1 = new Label("계속 추리 하시겠습니까?");
       
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
        
        //채팅창 세팅
        // 아이디, 상대방 아이디 우측 상단에 띄우기
        Panel name = new Panel();
        name.setBackground(Color.gray);
        name.setLayout(new GridLayout(2,2));
        name.add(new Label("플레이어     닉 네 임         :      ", 2));name.add(myNameLabel);
        name.add(new Label("상        대     닉 네 임         :      ", 2));name.add(counterNameLabel);
        name.setSize(10,10);
        add(name);
    
      chatProgram.setTitle("채팅창");
      chatProgram.setLayout(new BorderLayout());
      Panel chatP1 = new Panel();
      TextField chatTf = new TextField("                                                 ");
      chatTf.setText("채팅을 입력하세요");
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
            if(chatTf.getText().equals("/clear")) { // 채팅에 "/clear" 입력시 내용 초기화
               chat = "";
            }
            try {
               int isWhisper = chatTf.getText().indexOf("/w ");
               int isWhisper2 = chatTf.getText().indexOf("/ㅈ ");
               
               
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
      //채팅창 세팅 끝
      
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      
      setTitle("다빈치코드");
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
      int a = JOptionPane.showConfirmDialog(this, "프로그램을 종료하시겠습니까?");
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

      //서버 연결
      Socket sock = null;
      
      //기본 틀
      InputStream is = null; 
      OutputStream os = null; 
      
      // 자료를 주고 받으려 할때 어떤 것으로 받을 지 구분을 위함.
      DataInputStream dis = null; 
      
      //게임일 경우
      BufferedInputStream bis = null;
      BufferedOutputStream bos = null;
      
      try {
         sock = new Socket("localHost",5000); //연결할 서버 ip,port 연결 기능
         
         //기본 틀
         is = sock.getInputStream();
         os = sock.getOutputStream();
         bis = new BufferedInputStream(is);
         bos = new BufferedOutputStream(os);
         
         //상대가 나에게 전송한 내용의 타입이 무엇인지 파악
         dis = new DataInputStream(bis);
         //상대에게 전송할 내용의 타입이 무엇인지 전달
         dos = new DataOutputStream(bos);
         
         while(true) {
            
            int num = -1;
            num = dis.readInt();//여기서 신호코드를 받음
            System.out.println(num);
            
            if(num == -1) {
               System.out.println("잘못된 데이터가 전송되었습니다.");
            }else if(num < 100) { 
               // 100 보다 작은 경우 채팅밖에 수신 안왔으니 그대로 받음
               
               String msg = null;
               msg = dis.readUTF();
               msg += "\n";

               chat += msg;
               chatTa.setText(chat);

            }else if(num == 101){
               // 101이 게임 수신이니 트리맵으로 받음
               //기존에 있던 상대방 코드들에서 내가 맞춘 상대방 코드들의 이름을 저장
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
                  
                  if(codeColor.equals("화이트")) {
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
                  
                  if(color.equals("블랙")) {
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
             //다시 새롭게 만들어서 내가 이전에 맞춘 상대방코드 리스트를 가져와 새롭게 바뀐 상대방 코드라벨 위치에 맞춰 재 셋팅
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
               }else if(num == 102) { //102:먼저하기
                  firstBoo = false;
                  first.setVisible(false);
                  last.setVisible(true);
               }else if(num == 103) { //103:상대방 코드를 맞춤
                  tf.setEnabled(false);
                  String msg = null;
                  msg = dis.readUTF();
                  System.out.println("상대방이 맞춘 내 코드 : " + msg);
                  for(int i = 0; i < myCodeLabel.size(); i++) {
                     if(msg.equals(myCodeLabel.get(i).getText())) {
                        int last = myCodeLabel.get(i).getText().indexOf(" ");
                        String BW= myCodeLabel.get(i).getText().substring(0,last);
                        String BWnum = myCodeLabel.get(i).getText().substring(last+1, myCodeLabel.get(i).getText().length());
                        
                        Dimension d = pf4.getSize();
                        if(BW.equals("블랙")) {
                          ImageIcon icon = new ImageIcon("black\\br" + BWnum + ".png");
                          Image img = icon.getImage().getScaledInstance(d.width/(myCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                          icon = new ImageIcon(img);
                          myCodeLabel.get(i).setIcon(icon);
                          
                          System.out.println("그래서  BW의 " + BW + "와 BWnum은" + BWnum);
                          
                          
                        }else {
                          ImageIcon icon = new ImageIcon("white\\wr" + BWnum + ".png");
                          Image img = icon.getImage().getScaledInstance(d.width/(myCodeLabel.size()+1), d.height, java.awt.Image.SCALE_SMOOTH);
                          icon = new ImageIcon(img);
                          myCodeLabel.get(i).setIcon(icon);
                          System.out.println("그래서  BW의 " + BW + "와 BWnum은" + BWnum);
                        }
                        
                        for(int j = 0; j < 100; j++){
                           pf4.revalidate();
                           myCodeLabel.get(i).revalidate();
                        }
                        counterMyCodeRightNum.add(i);
                        System.out.println("현재까지 상대방이 맞춘 개수 : " + counterMyCodeRightNum.size());
                        break;
                     }
                  }  
                  for(int i = 0; i < counterMyCodeRightNum.size(); i++) {
                     System.out.println("counterMyCodeRightNum : " + counterMyCodeRightNum.get(i));
                  }
                  
               }else if(num == 104 ) { //상대방 코드를 못맞춤
                  String counterLastPickNum = null;
                  counterLastPickNum = dis.readUTF();
                  myCounterCodeRightNum.add(Integer.parseInt(counterLastPickNum));
                  
                  int counterLastlabelNum = myCounterCodeRightNum.get(myCounterCodeRightNum.size()-1);
                  int colorIndex = yourCodeLabel.get(counterLastlabelNum).getText().indexOf(" ");
                  String counterlastColor = yourCodeLabel.get(counterLastlabelNum).getText().substring(0,colorIndex);
                  String counterlastNum = yourCodeLabel.get(counterLastlabelNum).getText().substring(colorIndex+1, yourCodeLabel.get(counterLastlabelNum).getText().length());
                  System.out.println(counterlastColor + counterlastNum);
                  Dimension d = pf4.getSize();
                  if(counterlastColor.equals("블랙")){
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
                  System.out.println("상대방이 틀려서 보여준 코드 포함 현재까지 내가 맞춘 개수 : " + myHit);
                  
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
               }else if(num == 105 ) { // 105:내가 이겼을경우 
                  last.setVisible(false);
                  loser.setVisible(true);
                  se.revalidate();
            }else if(num == 110) { // 110:상대방에게 남은 블랙코드를 정송
               String remainBlackCodes = dis.readUTF();
               if(remainBlackCodes.equals("남은블랙코드없음")) {
                  black.remove(0);
               }else {
                  String[] rbc = remainBlackCodes.split("/");
                  black = new ArrayList<Double>();
                  for(int i = 0; i < rbc.length; i++) {
                     black.add(Double.parseDouble(rbc[i]));
                  }
               }
            }else if(num == 111) { //111:상대방에게 남은 화이트코드를 정송
               String remainWhiteCodes = dis.readUTF();
               if(remainWhiteCodes.equals("남은화이트코드없음")) {
                  white.remove(0);
               }else {
                  String[] rbc = remainWhiteCodes.split("/");
                  
                  white = new ArrayList<Double>();
                  for(int i = 0; i < rbc.length; i++) {
                     white.add(Double.parseDouble(rbc[i]));
                  }
               }
            }else if(num == 120) { // 120:계속하시겠습니까 창 아니요버튼 신호
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