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
	Label la1 = new Label("코드를 선택하세요");
	
	static Label la2 = new Label("선택한 코드 수 : 0개");
	
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
		code.put(ranPick,"블랙 " + (int)ranPick);
		la2.setText("선택한 코드 수 : " + code.size() + "개");

		
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
				
		code.put(ranPick,"화이트 " + (int)(ranPick - 0.5));
		la2.setText("선택한 코드 수 : " + code.size() + "개");
				
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
			if(code.get(su).substring(0, index).equals("블랙")) {
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
		
		pickCode.setTitle("코드 선택");
		
		pickCode.setLayout(new GridLayout(3,1));
		
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
		
		pickCode.add(p1);
		pickCode.add(p2);
		pickCode.add(p3);
		
		pickCode.setResizable(false);
		pickCode.setVisible(true);
		
		pf2.setLayout(new GridLayout(3,1));
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
				for(int i = 0; i < code2.size(); i++) {
					if(tf.getText().equals(code2.get(i).getText())) {
						boo = true;
						int last = code2.get(i).getText().indexOf(" ");
						String BW= code2.get(i).getText().substring(0,last);
						if(BW.equals("블랙")) {
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
		
		setTitle("다빈치코드");
		setBounds(100,100,800,800);
		setVisible(true);
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		Semi1 se = new Semi1();
		
		
	}



	
	
	
	
	
}
