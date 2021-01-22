package semiproject.editor2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class send {
	
	public static void main(String[] args) {
		
File file = new File("sa.txt");
		
		OutputStream os = null;
		DataOutputStream dos = null;
		ObjectOutputStream oos = null;
		

		int num = 1;
		
		try {
			os = new FileOutputStream(file);

			try {
				
				dos = new DataOutputStream(os);	
				
				dos.writeInt(num);
				String a = "Àá²¿´ë";
				dos.writeUTF(a);
				String b= "¹è°íÇÄ";
				dos.writeUTF(b);
				
				
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				 try {
					 if(dos != null) {dos.close();}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			try {
				if(os != null) {os.close();}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
/*
		try {
			os = new FileOutputStream(file);
			oos = new ObjectOutputStream(os);
			HashMap<Integer, Integer> s = new HashMap<Integer, Integer>();
			s.put(1, 10);
			
			oos.writeObject(s);
			System.out.println("ÀÌ»ó¾øÀ½");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(oos != null) {oos.close();}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
*/			
		
		
		
		
		
	}
	
	
	
}
