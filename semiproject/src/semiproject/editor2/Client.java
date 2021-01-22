package semiproject.editor2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.DataBufferDouble;
import java.nio.Buffer;
import java.util.*;


import javax.swing.JButton;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
public class Client extends JFrame implements WindowListener{
	static DataOutputStream dos = null; //채팅인지 게임인지 구분 신호 전달
//	static BufferedWriter bw = null; //채팅 전달
//	static ObjectOutputStream oos = null; //게임 정보 전달
	
	Dialog chatProgram = new Dialog(this);
	static TextArea chatTa = new TextArea();
	static String whisperUsing = "귓속말을 사용하시려면 아래 예시와 같이 사용해주시길 바랍니다.\n 예시1) /w 강아지 안녕하세요 \n 예시2) /ㅈ 127.0.0.13 반갑습니다\n/clear : 채팅 내용 지우기\n";
	//전체채팅과 귓속말이 올때마다 채팅창에 전부 추가하는 용도
	static String chat = whisperUsing; 
	
	//게임 
	static TreeMap<Double, String> myCode = new TreeMap<>(); //내 코드
	static ArrayList<Label> myCodeLabel; //내 코드 세팅
	
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
	Label la1 = new Label("코드를 선택하세요");
	
	static Label la2 = new Label("선택한 코드 수 : 0개");
	
	static int pickCount = 0;
	
	Panel pf1 = new Panel();
	static Panel pf2 = new Panel(); // 상대 코드
	Panel pf3 = new Panel();
	Panel pf4 = new Panel();
	
	int myCodeCount = 0;
	
	static TextField tf = new TextField("추리할 코드를 입력하세요");
	
	void look() {
		myCodeLabel = new ArrayList<Label>();
		Set keys2 = myCode.keySet();
		Iterator<Double> ite2 = keys2.iterator();
		
		int count = 0;
		while(ite2.hasNext()) {
			double su = ite2.next();
			
			int index = myCode.get(su).indexOf(" ");
			System.out.println(myCode.get(su) + " 몇번째 : " + myCode.get(su).indexOf(" "));
			if(index == -1) {
				continue;
			}else {

				Label la = new Label("            ");
				Font font = new Font(Font.SANS_SERIF,Font.BOLD,30);
				la.setFont(font);
				la.setAlignment(Label.CENTER);
				if(myCode.get(su).substring(0, index).equals("블랙")) {
					myCodeLabel.add(la);
					myCodeLabel.get(count).setBackground(Color.BLACK);
					myCodeLabel.get(count).setForeground(Color.WHITE);
					myCodeLabel.get(count).setText(myCode.get(su));
				}else {
					myCodeLabel.add(la);
					myCodeLabel.get(count).setBackground(Color.WHITE);
					myCodeLabel.get(count).setForeground(Color.BLACK);
					myCodeLabel.get(count).setText(myCode.get(su));
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
	
	//110번 전송
	void remainingBalackCodes() {
		String rmbcodes = new String();
		for(int i = 0; i < black.size(); i++) {
			if(i == 0) {
				rmbcodes += black.get(i);
			}else {
				rmbcodes += "/" + black.get(i);
			}
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
	
	//111번 전송
	void reaminingWhiteCodes() {
		String rmwcodes = new String();
		for(int i = 0; i < white.size(); i++) {
			if(i == 0) {
				rmwcodes += white.get(i);
			}else {
				rmwcodes += "/" + white.get(i);
			}
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
		System.out.println(myCode.get(ranPick));
		la2.setText("선택한 코드 수 : " + myCode.size() + "개");
		
		look();
		myCodeSend();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		remainingBalackCodes();
		
		if(pickCount >= 4) {
			pickmyCode.dispose();
		}
	}
	
	void whiteSelect() {
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
	}
	
	
	
	public Client() {
		//게임 프레임 창 세팅
		setLayout(new GridLayout(4,1));
		
		for(; black.size() < 12;) { 
			blackVal += 1;
			black.add(blackVal);
		}
		
		for(; white.size() < 12;) { 
			whiteVal += 1;
			white.add(whiteVal);
		}
		
		pickmyCode.setBounds(100 + 800/2 - 300/2, 100 + 800/2 - 200/2,300 ,200);
		
		pickmyCode.setTitle("코드 선택");
		
		pickmyCode.setLayout(new GridLayout(3,1));
		
		la1.setFont(font);
		p1.add(la1);
		
		Label laHeader = new Label("다빈치코드 보드게임");
		laHeader.setFont(font);
		laHeader.setAlignment(Label.CENTER);
		pf1.add(laHeader);
		
		font = new Font(Font.SANS_SERIF,Font.BOLD,20);
		
		Panel p2 = new Panel();
		
		la2.setFont(font);
		p2.add(la2);
		
		Panel p3 = new Panel();
		p3.setLayout(new GridLayout(1,2));
		JButton blackPick = new JButton("블랙 선택");
		blackPick.setFont(font);
		blackPick.setForeground(Color.WHITE);
		blackPick.setBackground(Color.BLACK);
		JButton whitePick = new JButton("화이트 선택");
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
		
		tf.setFont(font);
		
		tf.addFocusListener(new FocusListener() {
		

			@Override
			public void focusGained(FocusEvent e) {
				if(tf.getText().equals("추리할 코드를 입력하세요")) {
					tf.setText("");
				}
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				tf.setText("추리할 코드를 입력하세요");
				
			}
		});
		
		 tf.addActionListener(new ActionListener() {

	         @Override
	         public void actionPerformed(ActionEvent e) {
	            boolean boo = false;
	            boolean boo2 = false; // 103, 104 구분
	            for(int i = 0; i < yourCodeLabel.size(); i++) {
	               if(tf.getText().equals(yourCodeLabel.get(i).getText())) {
	                  boo = true;
	                  boo2 = true;
	                  int last = yourCodeLabel.get(i).getText().indexOf(" ");
	                  String BW= yourCodeLabel.get(i).getText().substring(0,last);
	                  if(BW.equals("블랙")) {
	                     yourCodeLabel.get(i).setForeground(Color.WHITE);
	                     revalidate();
	                  }else {
	                     yourCodeLabel.get(i).setForeground(Color.BLACK);
	                     revalidate();
	                  }
	               }
	            }      
	    
	            if(boo2) {
	               tf.setEnabled(true);
	               
	              try {
	                 //맞았으면 서버한테 103번이랑 맞춘 코드 이름을 보내고, 서버는 103번과 코드 이름을 받아서
	                 //클라이언트한테 103번과 코드 이름을 보냄.-> 클라이언트가 readInt if(num=103) 103번과 코드 이름을 받았을 때
	                 int send = 103;
	                 dos.writeInt(send);
	                 dos.writeUTF(tf.getText());
	                 dos.flush();
	              } catch (IOException e1) {
	                 // TODO Auto-generated catch block
	                 e1.printStackTrace();
	              }
	            }else {
	               tf.setEnabled(false);
	               try {
	                    int send = 104;
	                    dos.writeInt(send);
	                    dos.flush();
	                    System.out.println("-------104 보냄------");
	                 } catch (IOException e1) {
	                    e1.printStackTrace();
	              }
	            }
	            
	            for(int i = 0; i < 100; i++) {
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
				for(int i = 0; i < 100; i++) {
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
				for(int i = 0; i < 100; i++) {
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
		//게임 프레임창 세팅 끝
		
		
		// 1:1할 상대방을 입력하라는 다이알로그창 세팅
		Dialog counterpart = new Dialog(this);
		counterpart.setTitle("로그인");
		counterpart.setLayout(new GridLayout(3,1));
		
		Panel counterpartPanel = new Panel();
		Label counterpartLabel = new Label("상대할 닉네임");
		counterpartLabel.setAlignment(Label.CENTER);
		font = new Font(Font.SANS_SERIF,Font.BOLD,25);
		counterpartLabel.setFont(font);
		TextField counterpartTf = new TextField("               ");
		counterpartTf.setFont(font);
		counterpartTf.setText("");
		counterpartPanel.add(counterpartLabel);
		counterpartPanel.add(counterpartTf);
		
		JButton counterpartBtn = new JButton("게임시작");
		font = new Font(Font.SANS_SERIF,Font.BOLD,28);
		counterpartBtn.setFont(font);
		
		counterpartBtn.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("상대방 닉네임 :" + counterpartTf.getText());
				
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
		
		
				
		//1:1할 다이알로그창 세팅 완료
		
		
		
		//처음 시작할 때 닉네임을 입력하는 로그인창 세팅
		
		font = new Font(Font.SANS_SERIF,Font.BOLD,30);
		
		Dialog logIn = new Dialog(this);
		logIn.setTitle("로그인");
		logIn.setLayout(new GridLayout(3,1));
		
		Panel logInPanel = new Panel();
		Label logInLabel = new Label("닉네임");
		logInLabel.setAlignment(Label.CENTER);
		logInLabel.setFont(font);
		TextField logInTf = new TextField("                 ");
		logInTf.setFont(font);
		logInTf.setText("");
		logInPanel.add(logInLabel);
		logInPanel.add(logInTf);
		
		JButton logInBtn = new JButton("로그인");
		font = new Font(Font.SANS_SERIF,Font.BOLD,28);
		logInBtn.setFont(font);
		logInBtn.addActionListener(new ActionListener() {
			
			@Override 
			public void actionPerformed(ActionEvent arg0) {
				try {
					//내 닉네임 서버에 전달
					int send = 98;
					dos.writeInt(send);
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
		
		//로그인창 세팅 완료
		

		//채팅창 세팅
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
				if(chatTf.getText().equals("/clear")) {
					chat = "";
				}
				try {
					int isWhisper = chatTf.getText().indexOf("/w ");
					int isWhisper2 = chatTf.getText().indexOf("/ㅈ ");
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
		//채팅창 세팅 끝
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setTitle("다빈치코드");
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
		Client se = new Client();

		
		//서버 연결
		Socket sock = null;
		
		
		//기본 틀
		InputStream is = null; 
		OutputStream os = null; 
		
		// 자료를 주고 받으려 할때 어떤 것으로 받을 지 구분을 위함.
		DataInputStream dis = null; 
		
		
		//채팅일 경우
//		InputStreamReader isr = null;
//		BufferedReader br = null;
		
//		OutputStreamWriter osw = null;
		
		
		//게임일 경우
		BufferedInputStream bis = null;
//		ObjectInputStream ois = null;
		
		BufferedOutputStream bos = null;
		
		
		try {
			sock = new Socket("localHost",5000);
			
			//기본 틀
			is = sock.getInputStream();
			os = sock.getOutputStream();
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(os);
			
			
			//상대가 나에게 전송한 내용의 타입이 무엇인지 파악
			dis = new DataInputStream(bis);
			//상대에게 전송할 내용의 타입이 무엇인지 전달
			dos = new DataOutputStream(bos);
			
			
			
			//내 채팅관련 및 닉네임, 게임 상대이름 정보 전송
//			osw = new OutputStreamWriter(os);
//			bw = new BufferedWriter(osw);
			
			
			//나의 게임 코드 전송
//			bos = new BufferedOutputStream(os);
//			oos = new ObjectOutputStream(bos);
			
			
			
			
			while(true) {
				
				int num = -1;
				num = dis.readInt();
				System.out.println(num);
				
				if(num == -1) {
					System.out.println("잘못된 데이터가 전송되었습니다.");
				}else if(num < 100) { 
					// 100 보다 작은 경우 채팅밖에 수신 안왔으니 그대로 받음
					
					//상대 전체 채팅, 귓속말 수신
//					isr = new InputStreamReader(is);
//					br = new BufferedReader(isr);
		
					String msg = null;
					msg = dis.readUTF();
					msg += "\n";

					chat += msg;
					chatTa.setText(chat);

				}else if(num == 101){
					// 101이 게임 수신이니 트리맵으로 받음
					
					//상대 게임 코드 수신
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
						
						if(codeColor.equals("화이트")) {
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
						if(yourCode.get(su).substring(0, index).equals("블랙")) {
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
					pf2.setLayout(new GridLayout(1,yourCodeLabel.size()));
					for(int i = 0 ; i < yourCodeLabel.size(); i++) {
						pf2.add(yourCodeLabel.get(i));
						se.revalidate();
					}
					
					for(int i = 0; i < 100; i++) {
						se.revalidate();
					}
					
	            }else if(num == 102) {
	                
	            }else if(num == 103) {
	               tf.setEnabled(false);
	               String msg = null;
	               msg = dis.readUTF();
	               System.out.println(msg+ "받음");
//	             boolean boo = false;
	               for(int i = 0; i < myCodeLabel.size(); i++) {
	                  if(msg.equals(myCodeLabel.get(i).getText())) {
//	                     boo = true;
//	                     boo2 = true;
	                     int last = myCodeLabel.get(i).getText().indexOf(" ");
	                     String BW= myCodeLabel.get(i).getText().substring(0,last);
	                     if(BW.equals("블랙")) {
	                        myCodeLabel.get(i).setBackground(Color.BLUE);
	                     }
	                  }
	               }  
	            }else if(num == 104 ) {
	               tf.setEnabled(true);
	               se.revalidate();

				}else if(num == 110) {
					String remainBlackCodes = dis.readUTF();
					
					String[] rbc = remainBlackCodes.split("/");
					
					black = new ArrayList<Double>();
					for(int i = 0; i < rbc.length; i++) {
						black.add(Double.parseDouble(rbc[i]));
					}
					
				}else if(num == 111) {
					String remainWhiteCodes = dis.readUTF();
					
					String[] rbc = remainWhiteCodes.split("/");
					
					white = new ArrayList<Double>();
					for(int i = 0; i < rbc.length; i++) {
						white.add(Double.parseDouble(rbc[i]));
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
		
		
		
		
		
		
		
		
		
		
		//실패작
	/*	
		//채팅서버 연결
		Thread chatThread = new Thread(se);
		chatThread.setDaemon(true);
		chatThread.start();
		
		
	
		//게임서버 연결
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
							if(yourCode.get(su).substring(0, index).equals("블랙")) {
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
		// 내 myCode를 보낼거임
		Map myCodeLabel = new TreeMap();
		
		ObjectOutsream object(myCodeLabel)
		
		while(true) {
		1개 마다 put put put 
		TreeMap myCode3 = ObjectInputstream ->myCodeLabel;
		myCode3 size new grid reads
		for(int i ) {
			iterator
			label만들고
			라벨에 정보 넣고
			add하고
			revalidate하면
			
		}
		실시간으로 추가될때마다 new gridlayout 및 라벨 반영 
		
		}
		*/
		
	}

	


}
