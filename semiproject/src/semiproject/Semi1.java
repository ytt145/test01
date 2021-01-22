package semiproject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;


import javax.swing.JButton;

public class Semi1 extends Frame {
	
	static Map<Double, String> code = new TreeMap<>();
	
	Random ran = new Random();
	
	
	ArrayList<Double> black = new ArrayList<>();
	ArrayList<Double> white = new ArrayList<>();
	
	Double blackVal = 0.0;
	Double whiteVal = 0.5;

	Dialog pickCode = new Dialog(this);
	
	Font font = new Font(Font.SANS_SERIF,Font.BOLD,30);
	
	Panel p1 = new Panel();
	Label la1 = new Label("�ڵ带 �����ϼ���");
	
	static Label la2 = new Label("������ �ڵ� �� : 0��");
	
	static int pickCount = 0;
	
	Panel pf1 = new Panel();
	Panel pf2 = new Panel();
	Panel pf3 = new Panel();
	
	static ArrayList<Label> code2;
	
	int codeCount = 0;
	
	void blackSelect() {
		pickCount++;
		boolean boo = true;
		Set keys = code.keySet();
		Iterator<Double> ite = keys.iterator();
		double ranPick = 0.0;
		if(code.size() != 0) {
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
		code.put(ranPick,"�� " + (int)ranPick);
		la2.setText("������ �ڵ� �� : " + code.size() + "��");

		
		look();
		
		if(pickCount >= 4) {
			pickCode.dispose();
		}
	}
	

	void whiteSelect() {
		pickCount++;
		boolean boo = true;
		Set keys = code.keySet();
		Iterator<Double> ite = keys.iterator();
		double ranPick = 0.0;
		if(code.size() != 0) {
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
				
		code.put(ranPick,"ȭ��Ʈ " + (int)(ranPick - 0.5));
		la2.setText("������ �ڵ� �� : " + code.size() + "��");
				
		look();

		if(pickCount >= 4) {
			pickCode.dispose();
		}
	}
	
	void look() {
		code2 = new ArrayList<Label>();
		Set keys2 = code.keySet();
		Iterator<Double> ite2 = keys2.iterator();
		
		int count = 0;
		while(ite2.hasNext()) {
			double su = ite2.next();
			
			int index = code.get(su).indexOf(" ");
			
			Label la = new Label("            ");
			Font font = new Font(Font.SANS_SERIF,Font.BOLD,30);
			la.setFont(font);
			la.setAlignment(Label.CENTER);
			if(code.get(su).substring(0, index).equals("��")) {
				code2.add(la);
				code2.get(count).setBackground(Color.BLACK);
				code2.get(count).setForeground(Color.BLACK);
				code2.get(count).setText(code.get(su));
			}else {
				code2.add(la);
				code2.get(count).setBackground(Color.WHITE);
				code2.get(count).setForeground(Color.WHITE);
				code2.get(count).setText(code.get(su));
			}
			count++;
		}
	}
	
	
	public Semi1() {
		
		setLayout(new GridLayout(3,1));
		
		for(; black.size() < 12;) { 
			blackVal += 1;
			black.add(blackVal);
		}
		
		for(; white.size() < 12;) { 
			whiteVal += 1;
			white.add(whiteVal);
		}
		
		pickCode.setBounds(100 + 800/2 - 300/2, 100 + 800/2 - 200/2,300 ,200);
		
		pickCode.setTitle("�ڵ� ����");
		
		pickCode.setLayout(new GridLayout(3,1));
		
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
		
		pickCode.add(p1);
		pickCode.add(p2);
		pickCode.add(p3);
		
		pickCode.setResizable(false);
		pickCode.setVisible(true);
		
		pf2.setLayout(new GridLayout(3,1));
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
				for(int i = 0; i < code2.size(); i++) {
					if(tf.getText().equals(code2.get(i).getText())) {
						boo = true;
						int last = code2.get(i).getText().indexOf(" ");
						String BW= code2.get(i).getText().substring(0,last);
						if(BW.equals("��")) {
							code2.get(i).setForeground(Color.WHITE);
						}else {
							code2.get(i).setForeground(Color.BLACK);
						}
					}
				}
				revalidate();
				tf.setText("");
				
			}
		});
		
		pf2.add(new Label());
		pf2.add(tf);
		pf2.add(new Label());
		
		
		blackPick.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				blackSelect();
				pf3.removeAll();
				pf3.setLayout(new GridLayout(1,code2.size()));
				for(int i = 0 ; i < code2.size(); i++) {
					pf3.add(code2.get(i));
				}
				revalidate();
			}
		});
		
		whitePick.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				whiteSelect();
				pf3.removeAll();
				pf3.setLayout(new GridLayout(1,code2.size()));
				for(int i = 0 ; i < code2.size(); i++) {
					pf3.add(code2.get(i));
				}
				revalidate();
			};
			
		});
		
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		
		pf3.setLayout(new GridLayout(1,1));
		
		
		
		
		add(pf1);
		add(pf2);
		add(pf3);
		
		setTitle("�ٺ�ġ�ڵ�");
		setBounds(100,100,800,800);
		setVisible(true);
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		Semi1 se = new Semi1();
		
		
	}



	
	
	
	
	
}
