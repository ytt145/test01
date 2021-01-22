package semiproject.editor2;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class read {
	public static void main(String[] args) {
		
File file = new File("sa.txt");
		
		InputStream is = null;
		DataInputStream dis = null;
		ObjectInputStream ois = null;
		
		int i  = -1;

			try {
				is = new FileInputStream(file);
				dis = new DataInputStream(is);	
				i = dis.readInt();
				
				if(i == 1) {
					String a = dis.readUTF();
					System.out.println(a);
					String b = dis.readUTF();
					System.out.println(b);
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

				
			
			/*
			System.out.println("이상없음");
			System.out.println(i);
			if(i == 1) {
				try {
		
					ois = new ObjectInputStream(is);
					HashMap<Integer, Integer> s = (HashMap<Integer, Integer>) ois.readObject();
					System.out.println(s.get(1));
					System.out.println("이상없음");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						ois.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			try {
				if(is != null) {is.close();}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		*/
	}
}
