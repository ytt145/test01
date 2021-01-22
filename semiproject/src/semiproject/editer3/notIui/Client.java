package semiproject.editer3.notIui;

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


import javax.swing.JButton;
import javax.swing.plaf.metal.MetalIconFactory.PaletteCloseIcon;

import semiproject.editor2.read;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
public class Client extends JFrame implements WindowListener{
	
	static DataOutputStream dos = null; //ä������ �������� ���� ��ȣ ����

	static Dialog first;//�����ϱ� ��ư
	static boolean firstBoo = true;
   
    static Dialog last;//Next Time�޼���â
    
    static Dialog victory;//�¸��� â
  
    static Dialog loser;//�й��� â

    
	Dialog chatProgram = new Dialog(this);
	static TextArea chatTa = new TextArea();
	static String whisperUsing = "�ӼӸ��� ����Ͻ÷��� �Ʒ� ���ÿ� ���� ������ֽñ� �ٶ��ϴ�.\n ����1) /w ������ �ȳ��ϼ��� \n ����2) /�� 127.0.0.13 �ݰ����ϴ�\n/clear : ä�� ���� �����\n";
	//��üä�ð� �ӼӸ��� �ö����� ä��â�� ���� �߰��ϴ� �뵵
	static String chat = whisperUsing; 
	
	//���� 
	static TreeMap<Double, String> myCode = new TreeMap<>(); //�� �ڵ�
	static ArrayList<Label> myCodeLabel; //�� �ڵ� ����
	
	static TreeMap<Double, String> yourCode;
	static ArrayList<Label> yourCodeLabel;
	
	Random ran = new Random();
	
	static ArrayList<Double> black = new ArrayList<>();
	static ArrayList<Double> white = new ArrayList<>();
	
	Double blackVal = 0.0;
	Double whiteVal = 0.5;

	Dialog pickmyCode = new Dialog(this);
	
	Font font = new Font(Font.SANS_SERIF,Font.BOLD,30);
	
	Panel p1 = new Panel();
	Label la1 = new Label("�ڵ带 �����ϼ���");
	
	
	static Label la2 = new Label("������ �ڵ� �� : 0��");
	
	static int pickCount = 0;
	
	static JButton blackPick = new JButton("�� ����");
	static JButton whitePick = new JButton("ȭ��Ʈ ����");
	
	Panel pf1 = new Panel();
	static Panel pf2 = new Panel(); // ��� �ڵ�
	Panel pf3 = new Panel();
	Panel pf4 = new Panel();
	
	int myCodeCount = 0;
	
	static TextField tf = new TextField("�߸��� �ڵ带 �Է��ϼ���");
	
	static int myHit = 0; // ���� ������ �ڵ带 ���� ����
	
	static ArrayList<Integer> myCounterCodeRightNum = new ArrayList<>(); 
	//���� ������ �ڵ带 ���� �� ��ȣ 0, 1 �� ��� ������ �ڵ带 �޾ƿ� �� �� ���� �� ���� ���� ������ �ڵ���� ��Ʈ����
	//���̰� �����ϴ� ��. 410��° �� ������ ��ȣ ������ ��.
	
	
	static ArrayList<Integer> counterMyCodeRightNum = new ArrayList<>();
	//103���� �޾����� �׿� �ش�Ǵ� �� ��ȣ�� ã������ �� ��ȣ�� ���⿡ �����ϰ� �ڵ�101�� ���� �� ������ �ٽ� �����Ұ���
	
	static String lastPickNum = "-1"; //�������� ���� �� �ڵ��� ��ġ�� ���� 
	
	 
	void look() {
		myCodeLabel = new ArrayList<Label>();
		Set keys2 = myCode.keySet();
		Iterator<Double> ite2 = keys2.iterator();
		
		int count = 0;
		while(ite2.hasNext()) {
			double su = ite2.next();
			int index = myCode.get(su).indexOf(" ");
			System.out.println(myCode.get(su) + " ���° : " + myCode.get(su).indexOf(" "));
			if(index == -1) {
				System.out.println("====================������ ����=========================");
				continue;
			}else {
				Label la = new Label("            ");
				Font font = new Font(Font.SANS_SERIF,Font.BOLD,30);
				la.setFont(font);
				la.setAlignment(Label.CENTER);
				if(myCode.get(su).substring(0, index).equals("��")) {
					myCodeLabel.add(la);
					boolean rightBoo = false;
					for(int i = 0; i < counterMyCodeRightNum.size(); i++) {
						if(counterMyCodeRightNum.get(i) == count) {
							rightBoo = true;
						}
					}
					if(rightBoo) {
						myCodeLabel.get(count).setBackground(Color.BLUE);
						System.out.println("====================" + (count+1) +"��° �÷��� ���� �ٲ�========================");
					}else {
						myCodeLabel.get(count).setBackground(Color.BLACK);
						System.out.println("====================" + (count+1) +"��° �÷��� �������� �ٲ�========================");
					}
					
					myCodeLabel.get(count).setForeground(Color.WHITE);
					myCodeLabel.get(count).setText(myCode.get(su));
				}else {
					myCodeLabel.add(la);
					
					boolean rightBoo = false;
					for(int i = 0; i < counterMyCodeRightNum.size(); i++) {
						if(counterMyCodeRightNum.get(i) == count) {
							rightBoo = true;
						}
					}
					if(rightBoo) {
						myCodeLabel.get(count).setBackground(Color.BLUE);
						System.out.println("====================" + (count+1) +"��° �÷��� ���� �ٲ�========================");
					}else {
						myCodeLabel.get(count).setBackground(Color.WHITE);
						System.out.println("====================" + (count+1) +"��° �÷��� ȭ��Ʈ�� �ٲ�========================");
					}
					
					myCodeLabel.get(count).setForeground(Color.BLACK);
					myCodeLabel.get(count).setText(myCode.get(su));
				}
				for(int i = 0; i < 100; i++) {
				myCodeLabel.get(count).revalidate();
				myCodeLabel.get(count).validate();
				}
				count++;
			}	
		}
		
		
	}
	
	
	
	void myCodeSend() {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//110�� ����
	void remainingBalackCodes() {
		String rmbcodes = new String();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	//111�� ����
	void reaminingWhiteCodes() {
		String rmwcodes = new String();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	void blackSelect() {
		pickCount++;
		System.out.println("pickCount : " + pickCount);
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
		System.out.println(myCode.get(ranPick));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		remainingBalackCodes();
		
		if(pickCount == 4 && firstBoo == true) {
			first.setVisible(true);
			firstBoo = false;
			pickmyCode.dispose();
		}else if(pickCount > 4) {
			pickmyCode.dispose();
		}
		
		if(black.size() == 0) {
			blackPick.setEnabled(false);
		}
	}
	
	void whiteSelect() {
		pickCount++;
		System.out.println("pickCount : " + pickCount);
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
			// TODO Auto-generated catch block
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
		}
		
		if(white.size() == 0) {
			whitePick.setEnabled(false);
		}
	}
	
	
	
	public Client() {
		//���� ������ â ����
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
		
		pickmyCode.setTitle("�ڵ� ����");
		
		pickmyCode.setLayout(new GridLayout(3,1));
		
		la1.setFont(font);
		p1.add(la1);
		
		Label laHeader = new Label("�ٺ�ġ�ڵ� �������");
		laHeader.setFont(font);
		laHeader.setAlignment(Label.CENTER);
		pf1.add(laHeader);
		
		font = new Font(Font.SANS_SERIF,Font.BOLD,20);
		
		Panel p2 = new Panel();
		
		la2.setFont(font);
		p2.add(la2);
		
		Panel p3 = new Panel();
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
			public void focusGained(FocusEvent e) {
				if(tf.getText().equals("�߸��� �ڵ带 �Է��ϼ���")) {
					tf.setText("");
				}
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				tf.setText("�߸��� �ڵ带 �Է��ϼ���");
				
			}
		});
		
		 tf.addActionListener(new ActionListener() {

	         @Override
	         public void actionPerformed(ActionEvent e) {
	            boolean boo = false;
	            boolean boo2 = false; // 103, 104 ����
	           
	            for(int i = 0; i < yourCodeLabel.size(); i++) {
	               if(tf.getText().equals(yourCodeLabel.get(i).getText())) {
	                  boo = true;
	                  boo2 = true;

	                  int last = yourCodeLabel.get(i).getText().indexOf(" ");
	                  String BW= yourCodeLabel.get(i).getText().substring(0,last);
	                  if(BW.equals("��")) {
	                     yourCodeLabel.get(i).setForeground(Color.WHITE);
	                     revalidate();
	                    // myCounterCodeRightNumColor.add("��");
	                  }else {
	                     yourCodeLabel.get(i).setForeground(Color.BLACK);
	                     revalidate();
	                    // myCounterCodeRightNumColor.add("ȭ��Ʈ");
	                  }
	                  boolean boo3 = false;
	                  for(int j = 0; j < myCounterCodeRightNum.size(); j++) {
	                	  if(i == myCounterCodeRightNum.get(j)) {
	                		  boo3 = true;
	                	  }
	                  }
	                  if(!boo3) {
		               myHit++;
	                  }
	                  myCounterCodeRightNum.add(i);
	                  break;
	               }
	            }      
	    
	            if(boo2) {
	               tf.setEnabled(true);
	              
	               
	              try {
	                 //�¾����� �������� 103���̶� ���� �ڵ� �̸��� ������, ������ 103���� �ڵ� �̸��� �޾Ƽ�
	                 //Ŭ���̾�Ʈ���� 103���� �ڵ� �̸��� ����.-> Ŭ���̾�Ʈ�� readInt if(num=103) 103���� �ڵ� �̸��� �޾��� ��
	                 int send = 103;
	                 dos.writeInt(send);
	                 dos.writeUTF(tf.getText());
	                 dos.flush();
	                 Thread.sleep(100);
	              } catch (IOException e1) {
	                 // TODO Auto-generated catch block
	                 e1.printStackTrace();
	              } catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	              
	      //        CMRN();
	              
	              if(myHit == yourCode.size()) {
	            	  victory.setVisible(true);
	            	  int send1 = 105;// �����̱�� ���â�� �й�â�� ���� �ڵ�
	            	  try {
						dos.writeInt(send1);
						dos.flush();
						System.out.println("-------105 ����------");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	              }
	              
	              
	            }else {
	               tf.setEnabled(false);
	               
	               last.setVisible(true);
	               try {
	                    int send = 104;
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
		
		blackPick.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				blackSelect();
				pf4.removeAll();
				pf4.setLayout(new GridLayout(1,myCodeLabel.size()));
				for(int i = 0 ; i < myCodeLabel.size(); i++) {
					pf4.add(myCodeLabel.get(i));
				}
				for(int i = 0; i < 1000; i++) {
					revalidate();
				}
				
				
			}
		});
		
		
		whitePick.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				whiteSelect();
				pf4.removeAll();
				pf4.setLayout(new GridLayout(1,myCodeLabel.size()));
				for(int i = 0 ; i < myCodeLabel.size(); i++) {
					pf4.add(myCodeLabel.get(i));
				}
				for(int i = 0; i < 1000; i++) {
					revalidate();
				}
			}
		});
		
		addWindowListener(this);
		
		pf4.setLayout(new GridLayout(1,1));

		add(pf1);
		add(pf2);
		add(pf3);
		add(pf4);
		//���� ������â ���� ��
		
		
		// 1:1�� ������ �Է��϶�� ���̾˷α�â ����
		Dialog counterpart = new Dialog(this);
		counterpart.setTitle("�α���");
		counterpart.setLayout(new GridLayout(3,1));
		
		Panel counterpartPanel = new Panel();
		Label counterpartLabel = new Label("����� �г���");
		counterpartLabel.setAlignment(Label.CENTER);
		font = new Font(Font.SANS_SERIF,Font.BOLD,25);
		counterpartLabel.setFont(font);
		TextField counterpartTf = new TextField("               ");
		counterpartTf.setFont(font);
		counterpartTf.setText("");
		counterpartPanel.add(counterpartLabel);
		counterpartPanel.add(counterpartTf);
		
		
		JButton counterpartBtn = new JButton("���ӽ���");
		font = new Font(Font.SANS_SERIF,Font.BOLD,28);
		counterpartBtn.setFont(font);
		counterpartBtn.setEnabled(false);
		
		counterpartTf.addKeyListener(new KeyAdapter() {
			
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
		
		counterpartTf.addFocusListener(new FocusAdapter() {
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
		
		counterpartBtn.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("���� �г��� :" + counterpartTf.getText());
				
				try {
					int send = 99;
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
		
		font = new Font(Font.SANS_SERIF,Font.BOLD,30);
		
		Dialog logIn = new Dialog(this);
		logIn.setTitle("�α���");
		logIn.setLayout(new GridLayout(3,1));
		
		Panel logInPanel = new Panel();
		Label logInLabel = new Label("�г���");
		logInLabel.setAlignment(Label.CENTER);
		logInLabel.setFont(font);
		TextField logInTf = new TextField("                 ");
		logInTf.setFont(font);
		logInTf.setText("");
		logInPanel.add(logInLabel);
		logInPanel.add(logInTf);
		
		JButton logInBtn = new JButton("�α���");
		font = new Font(Font.SANS_SERIF,Font.BOLD,28);
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
		
		logInTf.addFocusListener(new FocusAdapter() {
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
		
		
	      font = new Font(Font.SANS_SERIF,Font.BOLD,28);
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
	         }
	      });
		
		logIn.add(logInPanel);
		logIn.add(new Panel());
		logIn.add(logInBtn);
		
		logIn.addWindowListener(this);
		logIn.setBounds(100 + 800/2 - 500/2, 100 + 800/2 - 160/2,500 ,160);
		logIn.setVisible(true);
		
		//�α���â ���� �Ϸ�
		//qwe
        first=new Dialog(this);
        font = new Font(Font.SANS_SERIF,Font.BOLD,30);
        
        Panel fp=new Panel();
        first.setSize(300,200);
        
        Label fl=new Label("�����ϱ�"); 
        fl.setFont(font);
        
        JButton fristBtn=new JButton("�����ϱ�");
        fristBtn.setFont(font);
        fristBtn.setPreferredSize(new Dimension(20,30));
        first.add(fristBtn);
        fristBtn.addActionListener(new ActionListener() {
              
              @Override 
              public void actionPerformed(ActionEvent arg0) {
            	  tf.setEnabled(true);
            	  pickmyCode.setVisible(true);
                 try {
                    //�� �г��� ������ ����
                    int send = 102;
                    dos.writeInt(send);
                    dos.flush();                      
                    
                    //chatProgram.setVisible(true);
                    first.setVisible(false);
                    tf.setEnabled(true);
                    last.setVisible(false);
                   // counterpart.setVisible(true);
                 } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                 }
              }
        });   
        
        
        //�˸�â(next)
        last=new Dialog(this);
        font= new Font(font.SANS_SERIF,Font.BOLD,30);
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
        
        //�˸�â1
        victory=new Dialog(this);
        font= new Font(font.SANS_SERIF,Font.BOLD,30);
        Panel lp1=new Panel();
        Label pl1=new Label();
        
        pl1.setText("Victory!!!!!!");
        pl1.setFont(font);
        lp1.add(pl1);
        victory.setSize(300,200);
        victory.add(lp1);
        victory.setVisible(false);

        //�˸�â2
        loser=new Dialog(this);
        font= new Font(font.SANS_SERIF,Font.BOLD,30);
        Panel lp2=new Panel();
        Label pl2=new Label();
        
        pl2.setText("Loser!!!!!!");
        pl2.setFont(font);
        lp2.add(pl2);
        loser.setSize(300,200);
        loser.add(lp2);
        loser.setVisible(false);
        
		//ä��â ����
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
				if(chatTf.getText().equals("/clear")) {
					chat = "";
				}
				try {
					int isWhisper = chatTf.getText().indexOf("/w ");
					int isWhisper2 = chatTf.getText().indexOf("/�� ");
					System.out.println(chatTf.getText());
					System.out.println(isWhisper2);
					
					
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				chatTf.setText("");
			}	
		});
		
		chatP1.add(chatTf);
		
		chatTa.setText(whisperUsing);
		chatProgram.add(chatTa,BorderLayout.CENTER);
		chatProgram.add(chatP1,BorderLayout.SOUTH);
		chatProgram.setBounds(900,100,400,800);
		chatProgram.setVisible(false);
		chatProgram.addWindowListener(this);
		//ä��â ���� ��
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setTitle("�ٺ�ġ�ڵ�");
		setBounds(100,100,800,800);
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
		Client se = new Client();

		
		//���� ����
		Socket sock = null;
		
		
		//�⺻ Ʋ
		InputStream is = null; 
		OutputStream os = null; 
		
		// �ڷḦ �ְ� ������ �Ҷ� � ������ ���� �� ������ ����.
		DataInputStream dis = null; 
		
		
		//ä���� ���
//		InputStreamReader isr = null;
//		BufferedReader br = null;
		
//		OutputStreamWriter osw = null;
		
		
		//������ ���
		BufferedInputStream bis = null;
//		ObjectInputStream ois = null;
		
		BufferedOutputStream bos = null;
		
		
		try {
			sock = new Socket("localHost",5000);
			
			//�⺻ Ʋ
			is = sock.getInputStream();
			os = sock.getOutputStream();
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(os);
			
			
			//��밡 ������ ������ ������ Ÿ���� �������� �ľ�
			dis = new DataInputStream(bis);
			//��뿡�� ������ ������ Ÿ���� �������� ����
			dos = new DataOutputStream(bos);
			
			
			
			//�� ä�ð��� �� �г���, ���� ����̸� ���� ����
//			osw = new OutputStreamWriter(os);
//			bw = new BufferedWriter(osw);
			
			
			//���� ���� �ڵ� ����
//			bos = new BufferedOutputStream(os);
//			oos = new ObjectOutputStream(bos);
			
			
			
			
			while(true) {
				
				int num = -1;
				num = dis.readInt();
				System.out.println(num);
				
				if(num == -1) {
					System.out.println("�߸��� �����Ͱ� ���۵Ǿ����ϴ�.");
				}else if(num < 100) { 
					// 100 ���� ���� ��� ä�ùۿ� ���� �ȿ����� �״�� ����
					
					//��� ��ü ä��, �ӼӸ� ����
//					isr = new InputStreamReader(is);
//					br = new BufferedReader(isr);
		
					String msg = null;
					msg = dis.readUTF();
					msg += "\n";

					chat += msg;
					chatTa.setText(chat);

				}else if(num == 101){
					// 101�� ���� �����̴� Ʈ�������� ����
					
					//��� ���� �ڵ� ����
//					bis = new BufferedInputStream(is);
//					ois = new ObjectInputStream(bis);
					
					String yourCodeSetting = dis.readUTF();
					System.out.println(yourCodeSetting);
					String[] yourCodeArray = yourCodeSetting.split("/");
					System.out.println(Arrays.toString(yourCodeArray));
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
					
					
					yourCodeLabel = new ArrayList<Label>();
					Set keys = yourCode.keySet();
					Iterator<Double> ite = keys.iterator();
					
					int count = 0;
					while(ite.hasNext()) {
						double su = ite.next();
						
						int index = yourCode.get(su).indexOf(" ");
						
						Label la = new Label("            ");
						Font font = new Font(Font.SANS_SERIF,Font.BOLD,30);
						la.setFont(font);
						la.setAlignment(Label.CENTER);
						if(yourCode.get(su).substring(0, index).equals("��")) {
							yourCodeLabel.add(la);
							yourCodeLabel.get(count).setBackground(Color.BLACK);
							boolean rightBoo = false;
							for(int i = 0; i < myCounterCodeRightNum.size(); i++) {
								if(myCounterCodeRightNum.get(i) == count) {
									rightBoo = true;
								}
							}
							if(rightBoo) {
								yourCodeLabel.get(count).setForeground(Color.WHITE);
							}else {
								yourCodeLabel.get(count).setForeground(Color.BLACK);
							}

							yourCodeLabel.get(count).setText(yourCode.get(su));
						}else {
							yourCodeLabel.add(la);
							yourCodeLabel.get(count).setBackground(Color.WHITE);
							
							boolean rightBoo = false;
							for(int i = 0; i < myCounterCodeRightNum.size(); i++) {
								if(myCounterCodeRightNum.get(i) == count) {
									rightBoo = true;
								}
							}
							if(rightBoo) {
								yourCodeLabel.get(count).setForeground(Color.BLACK);
							}else {
								yourCodeLabel.get(count).setForeground(Color.WHITE);
							}
							
							yourCodeLabel.get(count).setText(yourCode.get(su));
						}
						count++;
					}

					
					pf2.removeAll();
					pf2.setLayout(new GridLayout(1,yourCodeLabel.size()));
					for(int i = 0 ; i < yourCodeLabel.size(); i++) {
						pf2.add(yourCodeLabel.get(i));
						se.revalidate();
					}
					
					
					for(int i = 0; i < 1000; i++) {
						se.revalidate();
					}
	
	            }else if(num == 102) {
	            	firstBoo = false;
	            	first.setVisible(false);
	            }else if(num == 103) {
	               tf.setEnabled(false);
	               String msg = null;
	               msg = dis.readUTF();
	               System.out.println(msg+ "����");
//	             boolean boo = false;
	               for(int i = 0; i < myCodeLabel.size(); i++) {
	                  if(msg.equals(myCodeLabel.get(i).getText())) {
//	                     boo = true;
//	                     boo2 = true;
	                     int last = myCodeLabel.get(i).getText().indexOf(" ");
	                     String BW= myCodeLabel.get(i).getText().substring(0,last);
	                     myCodeLabel.get(i).setBackground(Color.BLUE);
	                     counterMyCodeRightNum.add(i);
	                  }
	               }  
	               for(int i = 0; i < counterMyCodeRightNum.size(); i++) {
	            	   System.out.println("counterMyCodeRightNum : " + counterMyCodeRightNum.get(i));
	               }
	               
	            }else if(num == 104 ) {
	            	String counterLastPickNum = null;
	            	counterLastPickNum = dis.readUTF();
	            	myCounterCodeRightNum.add(Integer.parseInt(counterLastPickNum));
	            	
	            	int counterLastlabelNum = myCounterCodeRightNum.get(myCounterCodeRightNum.size()-1);
	            	int colorIndex = yourCodeLabel.get(counterLastlabelNum).getText().indexOf(" ");
	            	String counterlastColor = yourCodeLabel.get(counterLastlabelNum).getText().substring(0,colorIndex);
	            	if(counterlastColor.equals("��")){
	            		yourCodeLabel.get(counterLastlabelNum).setForeground(Color.WHITE);
	            	}else {
	            		yourCodeLabel.get(counterLastlabelNum).setForeground(Color.BLACK);
	            	}
	            	yourCodeLabel.get(counterLastlabelNum).revalidate();
	            	
	            	myHit++;
	            	
	               tf.setEnabled(true);
	               last.setVisible(false);
	               firstBoo = false;

	               System.out.println("���� �� ���� ���� : " + black.size());
	               
	               System.out.println("���� ȭ��Ʈ ���� ���� : " + white.size());
	               
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
					}
					
					
					se.revalidate();
	            }else if(num == 105 ) {
	            	last.setVisible(false);
	            	loser.setVisible(true);
	            	se.revalidate();
	            /*	
	            }else if(num == 106) {
	            	String counterMyCodeRightNums = dis.readUTF();
	            	
	            	String[] cmcrn = counterMyCodeRightNums.split("/");
	            	
	            	counterMyCodeRightNum = new ArrayList<>();
	            	for(int i = 0; i < cmcrn.length; i++) {
	            		counterMyCodeRightNum.add(Integer.parseInt(cmcrn[i]));
	            	}
	            */
				}else if(num == 110) {
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
					
				}else if(num == 111) {
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
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

//				if(br != null) {br.close();}
//				if(isr != null) {isr.close();}

				
//				if(bw != null) {bw.close();}
//				if(osw != null) {osw.close();}
				if(dis != null) {dis.close();}
				if(bis != null) {bis.close();}
				if(is != null) {is.close();}
				
				if(dos != null) {dos.close();}
				if(bos != null) {bos.close();}
				if(os != null) {os.close();}
				
				if(sock != null){sock.close();}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
		
		
		
		
		
		
		//������
	/*	
		//ä�ü��� ����
		Thread chatThread = new Thread(se);
		chatThread.setDaemon(true);
		chatThread.start();
		
		
	
		//���Ӽ��� ����
		Thread gameThread = new Thread(se) {
			@Override
			public void run() {
				Socket gameSock = null;
				
				InputStream gameIs = null;
				BufferedInputStream gameBis = null;
				ObjectInputStream gameOis = null;
				
				OutputStream  gameOs = null;
				BufferedOutputStream gameBos = null;
				
				try {
					gameSock = new Socket("192.168.0.111",7000);
					
					gameOs = gameSock.getOutputStream();
					gameBos = new BufferedOutputStream(gameOs);
					gameOos = new ObjectOutputStream(gameBos);
					
					gameIs = gameSock.getInputStream();
					
					
					while(true) {
						try {
							byte by;
					
							
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						yourCodeLabel = new ArrayList<Label>();
						Set keys = yourCode.keySet();
						Iterator<Double> ite = keys.iterator();
						
						int count = 0;
						while(ite.hasNext()) {
							double su = ite.next();
							
							int index = yourCode.get(su).indexOf(" ");
							
							Label la = new Label("            ");
							Font font = new Font(Font.SANS_SERIF,Font.BOLD,30);
							la.setFont(font);
							la.setAlignment(Label.CENTER);
							if(yourCode.get(su).substring(0, index).equals("��")) {
								yourCodeLabel.add(la);
								yourCodeLabel.get(count).setBackground(Color.BLACK);
								yourCodeLabel.get(count).setForeground(Color.BLACK);
								yourCodeLabel.get(count).setText(yourCode.get(su));
							}else {
								yourCodeLabel.add(la);
								yourCodeLabel.get(count).setBackground(Color.WHITE);
								yourCodeLabel.get(count).setForeground(Color.WHITE);
								yourCodeLabel.get(count).setText(yourCode.get(su));
							}
							count++;
						}
						
						pf2.removeAll();
						pf2.setLayout(new GridLayout(1,myCodeLabel.size()));
						for(int i = 0 ; i < myCodeLabel.size(); i++) {
							pf2.add(myCodeLabel.get(i));
						}
						pf2.revalidate();

					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						if(gameOos != null) {gameOos.close();}
						if(gameBos != null) {gameBos.close();}
						if(gameOs != null) {gameOs.close();}
						
						if(gameOis != null) {gameOis.close();}
						if(gameBis != null) {gameBis.close();}
						if(gameIs != null) {gameIs.close();}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		gameThread.setDaemon(true);
		gameThread.start();
	
		
		
		
		/*
		// �� myCode�� ��������
		Map myCodeLabel = new TreeMap();
		
		ObjectOutsream object(myCodeLabel)
		
		while(true) {
		1�� ���� put put put 
		TreeMap myCode3 = ObjectInputstream ->myCodeLabel;
		myCode3 size new grid reads
		for(int i ) {
			iterator
			label�����
			�󺧿� ���� �ְ�
			add�ϰ�
			revalidate�ϸ�
			
		}
		�ǽð����� �߰��ɶ����� new gridlayout �� �� �ݿ� 
		
		}
		*/
		
	}
}
