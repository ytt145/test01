package servertest;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.net.Socket;

public class Client extends Frame {
	static BufferedWriter bw = null;
	static TextArea ta = new TextArea();
	public Client(){
		setLayout(new BorderLayout());
		
		Panel p1 = new Panel();
		TextField tf = new TextField("                                   ");
		tf.setText("채팅을 입력하세요");
		tf.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				tf.setText("채팅을 입력하세요");
				
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				tf.setText("");
			}
		});

		
		tf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					bw.write(tf.getText());
					bw.newLine();
					bw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tf.setText("");
			}
		});
		
		
		p1.add(tf);
		
		add(ta,BorderLayout.CENTER);
		add(p1,BorderLayout.SOUTH);
		
		setBounds(100,100,300,300);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Client cl = new Client();
		
		Socket sock = null;
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		OutputStream os = null;
		OutputStreamWriter osw = null;
		
		try {
			sock = new Socket("localHost",5000);
			
			is = sock.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			os = sock.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			
			String chat = new String();
			while(true) {
				String msg = null;
					
					msg = br.readLine();
					msg += "\n";

				chat += msg;
				ta.setText(chat);
			}
			
			
		} catch (IOException e) {
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
		
		
		
		
	}

}
