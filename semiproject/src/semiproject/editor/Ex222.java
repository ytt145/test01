package semiproject.editor;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class Ex222 {
	public static void main(String[] args) {
		File file = new File("my.txt");
		
		InputStream is = null;
		ObjectInputStream ois = null;

		try {
			is = new FileInputStream(file);
			ois = new ObjectInputStream(is);
			
			int a = (int) ois.readObject();
			System.out.println(a);
			
			if(a == 1) {
				HashMap<Integer, Integer> b = (HashMap<Integer, Integer>) ois.readObject();
				b.get(1);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
