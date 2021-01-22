package semiproject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
public class Client extends JFrame implements WindowListener, Runnable,Serializable{
	static BufferedWriter chatBw = null; //채팅 전달
	
	static ObjectOutputStream gameOos = null;
	
	Dialog chatProgram = new Dialog(this);
	static TextArea chatTa = new TextArea();
	static String whisperUsing = "귓속말을 사용하시려면 아래 예시와 같이 사용해주시길 바랍니다.\n 예시1) /w 강아지 안녕하세요 \n 예시2) /ㅈ 127.0.0.13 반갑습니다\n";
	
	//게임 
	static TreeMap<Double, String> myCode = new TreeMap<>(); //내 코드
	static ArrayList<Label> myCodeLabel; //내 코드 세팅
	
	static TreeMap<Double, String> yourCode = new TreeMap<>();
	static ArrayList<Label> yourCodeLabel;
	
	Random ran = new Random();
	
	ArrayList<Double> black = new ArrayList<>();
	ArrayList<Double> white = new ArrayList<>();
	
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
	
	void blackSelect() {
		pickCount++;
		boolean boo = true;
		Set keys = myCode.keySet();
		Iterator<Double> ite = keys.iterator();
		double ranPick = 0.0;
		if(myCode.size() != 0) {
			while(boo) {
				ranPick = black.get(ran.nextInt(12));
				while(ite.hasNext()){
					if(ite.next() != Double.valueOf(ranPick)) {
						boo = false;
					}
				}
			}
		} else {
			ranPick = black.get(ran.nextInt(12));
		}
		myCode.put(ranPick,"블랙 " + (int)ranPick);
		System.out.println(myCode.get(ranPick));
		la2.setText("선택한 코드 수 : " + myCode.size() + "개");
		
		look();
		System.out.println(myCode);
		try {
			gameOos.writeObject(myCode);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
				ranPick = white.get(ran.nextInt(12));
				while(ite.hasNext()){
					if(ite.next() != Double.valueOf(ranPick)) {
						boo = false;
					}
				}
			}
		} else {
			ranPick = white.get(ran.nextInt(12));
		}
		myCode.put(ranPick,"화이트 " + (int)(ranPick - 0.5));
		la2.setText("선택한 코드 수 : " + myCode.size() + "개");
		
		look();
		
		try {
			System.out.println(myCode);
			gameOos.writeObject(myCode);
			gameOos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(pickCount >= 4) {
			pickmyCode.dispose();
		}
	}
	
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
		TextField tf = new TextField("추리할 코드를 입력하세요");
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
				for(int i = 0; i < yourCodeLabel.size(); i++) {
					if(tf.getText().equals(myCodeLabel.get(i).getText())) {
						boo = true;
						int last = yourCodeLabel.get(i).getText().indexOf(" ");
						String BW= yourCodeLabel.get(i).getText().substring(0,last);
						if(BW.equals("블랙")) {
							yourCodeLabel.get(i).setForeground(Color.WHITE);
						}else {
							yourCodeLabel.get(i).setForeground(Color.BLACK);
						}
					}
				}
				revalidate();
				tf.setText("");
				
			}
		});
		
		pf3.add(new Label());
		pf3.add(tf);
		pf3.add(new Label());
		
		blackPick.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				blackSelect();
				pf4.removeAll();
				pf4.setLayout(new GridLayout(1,myCodeLabel.size()));
				for(int i = 0 ; i < myCodeLabel.size(); i++) {
					pf4.add(myCodeLabel.get(i));
				}
				revalidate();
				
				
			}
		});
		
		whitePick.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				whiteSelect();
				pf4.removeAll();
				pf4.setLayout(new GridLayout(1,myCodeLabel.size()));
				for(int i = 0 ; i < myCodeLabel.size(); i++) {
					pf4.add(myCodeLabel.get(i));
				}
				revalidate();
			};
			
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
					System.out.println(counterpartTf.getText());
					TreeMap<Double, String> yourNickname = new TreeMap<>();
					yourNickname.put(100.0, counterpartTf.getText()); //상대방의 닉네임을 넣음
					try {
						gameOos.writeObject(yourNickname);
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
					//서버가 내 닉네임이라고 인식하기 위한 비밀코드(?)포함 전달
					chatBw.write(logInTf.getText() + "!@#$%닉네임nickname%$#@!");
					
					// 내 닉네임으로도 연결이 가능하도록 게임서버에 닉네임 전달
					
					HashMap<Double, String> myNickname = new HashMap<Double, String>();	
					myNickname.put(101.0, logInTf.getText()); 
					gameOos.writeObject(myNickname);
					System.out.println("보내기");
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
				try {
					chatBw.write(chatTf.getText());
					chatBw.newLine();
					chatBw.flush();
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
					gameSock = new Socket("localHost",7000);
					System.out.println(1111);
					gameOs = gameSock.getOutputStream();
					gameBos = new BufferedOutputStream(gameOs);
					gameOos = new ObjectOutputStream(gameBos);
					System.out.println(2222);
					gameIs = gameSock.getInputStream();
					gameBis = new BufferedInputStream(gameIs);
					gameOis = new ObjectInputStream(gameBis);	
					System.out.println(3333);
					while(true) {
						System.out.println(44444);
						try {
							Object obj = gameIs.read();
							System.out.println(obj);
							System.out.println("obj : null? ->" + obj.equals(null));
							yourCode = (TreeMap<Double, String>)obj;
							
						} catch (Exception e) {
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

	@Override
	public void run() {
		//채팅창 연결
				Socket chatSock = null;
				InputStream chatIs = null;
				InputStreamReader chatIsr = null;
				BufferedReader chatBr = null;
				
				OutputStream chatOs = null;
				OutputStreamWriter chatOsw = null;

				Object obj = null;
				
				
				try {
					chatSock = new Socket("localHost",5000);
					
					chatIs = chatSock.getInputStream();
					chatIsr = new InputStreamReader(chatIs);
					chatBr = new BufferedReader(chatIsr);
					
					chatOs = chatSock.getOutputStream();
					chatOsw = new OutputStreamWriter(chatOs);
					chatBw = new BufferedWriter(chatOsw);
					chatBw.flush();;
					String chat = whisperUsing;
					while(true) {
						String msg = null;
							
						msg = chatBr.readLine();
						System.out.println(">>>"+msg);
						msg += "\n";

						chat += msg;
						chatTa.setText(chat);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {

						if(chatBr != null) {chatBr.close();}
						if(chatIsr != null) {chatIsr.close();}
						if(chatIs != null) {chatIs.close();}
						
						if(chatBw != null) {chatBw.close();}
						if(chatOsw != null) {chatOsw.close();}
						if(chatOs != null) {chatOs.close();}
						if(chatSock != null){chatSock.close();}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		
	}


}
