package semiproject.editor;

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
public class Client extends JFrame implements WindowListener{
	static DataOutputStream dos = null; //ä������ �������� ���� ��ȣ ����
	static BufferedWriter bw = null; //ä�� ����
	static ObjectOutputStream oos = null; //���� ���� ����
	
	Dialog chatProgram = new Dialog(this);
	static TextArea chatTa = new TextArea();
	static String whisperUsing = "�ӼӸ��� ����Ͻ÷��� �Ʒ� ���ÿ� ���� ������ֽñ� �ٶ��ϴ�.\n ����1) /w ������ �ȳ��ϼ��� \n ����2) /�� 127.0.0.13 �ݰ����ϴ�\n";
	
	//���� 
	static TreeMap<Double, String> myCode = new TreeMap<>(); //�� �ڵ�
	static ArrayList<Label> myCodeLabel; //�� �ڵ� ����
	
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
	Label la1 = new Label("�ڵ带 �����ϼ���");
	
	static Label la2 = new Label("������ �ڵ� �� : 0��");
	
	static int pickCount = 0;
	
	Panel pf1 = new Panel();
	static Panel pf2 = new Panel(); // ��� �ڵ�
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
		myCode.put(ranPick,"�� " + (int)ranPick);
		System.out.println(myCode.get(ranPick));
		la2.setText("������ �ڵ� �� : " + myCode.size() + "��");
		
		look();
		
		try {
			int send = 101;
			dos.writeInt(send);
			
			oos.writeObject(myCode);
			oos.flush();
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
		myCode.put(ranPick,"ȭ��Ʈ " + (int)(ranPick - 0.5));
		la2.setText("������ �ڵ� �� : " + myCode.size() + "��");
		
		look();
		
		try {
			int send = 101;
			dos.writeInt(101);
			oos.writeObject(myCode);
			oos.flush();
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
			System.out.println(myCode.get(su) + " ���° : " + myCode.get(su).indexOf(" "));
			if(index == -1) {
				continue;
			}else {

				Label la = new Label("            ");
				Font font = new Font(Font.SANS_SERIF,Font.BOLD,30);
				la.setFont(font);
				la.setAlignment(Label.CENTER);
				if(myCode.get(su).substring(0, index).equals("��")) {
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
		//���� ������ â ����
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
		JButton blackPick = new JButton("�� ����");
		blackPick.setFont(font);
		blackPick.setForeground(Color.WHITE);
		blackPick.setBackground(Color.BLACK);
		JButton whitePick = new JButton("ȭ��Ʈ ����");
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
		TextField tf = new TextField("�߸��� �ڵ带 �Է��ϼ���");
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
				for(int i = 0; i < yourCodeLabel.size(); i++) {
					if(tf.getText().equals(myCodeLabel.get(i).getText())) {
						boo = true;
						int last = yourCodeLabel.get(i).getText().indexOf(" ");
						String BW= yourCodeLabel.get(i).getText().substring(0,last);
						if(BW.equals("��")) {
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
		
		counterpartBtn.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("���� �г��� :" + counterpartTf.getText());
				
				try {
					int send = 99;
					dos.writeInt(send);
					
					bw.write(counterpartTf.getText());
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
		logInBtn.addActionListener(new ActionListener() {
			
			@Override 
			public void actionPerformed(ActionEvent arg0) {
				try {
					//�� �г��� ������ ����
					int send = 98;
					dos.writeInt(send);
					
					bw.write(logInTf.getText());
					bw.flush();
					
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
				try {
					int isWhisper = chatTf.getText().indexOf("/w ");
					int isWhisper2 = chatTf.getText().indexOf("/�� ");
					
					if(isWhisper != -1 || isWhisper2 != -2) {
						
						int send = 1;
						dos.writeInt(send);
						
						
						bw.write(chatTf.getText());
						bw.newLine();
						bw.flush();
						
					}else {
						
						int send = 2;
						dos.writeInt(send);
						
						
						bw.write(chatTf.getText());
						bw.newLine();
						bw.flush();
						
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
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		OutputStreamWriter osw = null;
		
		
		//������ ���
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		
		BufferedOutputStream bos = null;
		
		
		try {
			sock = new Socket("localHost",5000);
			
			//�⺻ Ʋ
			is = sock.getInputStream();
			os = sock.getOutputStream();
			
			//��밡 ������ ������ ������ Ÿ���� �������� �ľ�
			dis = new DataInputStream(is);
			//��뿡�� ������ ������ Ÿ���� �������� ����
			dos = new DataOutputStream(os);
			
			
			
			//�� ä�ð��� �� �г���, ���� ����̸� ���� ����
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			
			
			//���� ���� �ڵ� ����
			bos = new BufferedOutputStream(os);
			oos = new ObjectOutputStream(bos);
			
			
			//��üä�� �ӼӸ��� �ö����� ä��â�� ���� �߰��ϴ� �뵵
			String chat = whisperUsing; 
			
			while(true) {
				
				int num = -1;
				num = dis.readInt();
				System.out.println(num);
				
				if(num == -1) {
					System.out.println("�߸��� �����Ͱ� ���۵Ǿ����ϴ�.");
				}else if(num < 100) { 
					// 100 ���� ���� ��� ä�ùۿ� ���� �ȿ����� �״�� ����
					
					//��� ��ü ä��, �ӼӸ� ����
					isr = new InputStreamReader(is);
					br = new BufferedReader(isr);
		
					String msg = null;
					msg = br.readLine();
					msg += "\n";

					chat += msg;
					chatTa.setText(chat);

				}else {
					// 101�� ���� �����̴� Ʈ�������� ����
					
					//��� ���� �ڵ� ����
					bis = new BufferedInputStream(is);
					ois = new ObjectInputStream(bis);

					
					TreeMap<Double, String> yourCode = (TreeMap<Double, String>) ois.readObject();
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

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				if(br != null) {br.close();}
				if(isr != null) {isr.close();}
				if(is != null) {is.close();}
				
				if(bw != null) {bw.close();}
				if(osw != null) {osw.close();}
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
