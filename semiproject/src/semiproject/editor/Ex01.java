package semiproject.editor;

import java.util.TreeMap;

public class Ex01 {
	
	public static void main(String[] args) {
		
		Object obj1;
		
		/*
		//Ŭ���̾�Ʈ ä��ħ
		String string = "���ڿ�";
		
		//ä��ģ�ɷ� ������ ������Ʈ�ƿ�ǲ��Ʈ��
		obj1 = string; // ���� ������ ��ü���� �±�
		
		//���������� ��ǲ��Ʈ������ ����
		Object obj2 = obj1; // �����Ͽ������߱�
		
		//Ÿ Ŭ���̾�Ʈ���� �ƿ�ǲ��Ʈ�� 
		Object obj3 = obj2; // ��޹���
		
		String o = (String)obj3; // ���� �ް� ���� ����
		
		System.out.println(o); // ���빰 Ȯ��.
		*/
		
		
		//Ŭ���̾�Ʈ�� ���� ���� ����
		
		TreeMap<Double, String> tm = new TreeMap<>();
		
		tm.put(100.0, "Ʈ������");
		
		//������ ������Ʈ�ƿ�ǲ��Ʈ��
		Object obj4 = tm;
		
		//������ ������Ʈ��ǲ��Ʈ������ ����
		Object obj5 = obj4;
		
		//������ Ÿ Ŭ���̾�Ʈ���� ������Ʈ�ƿ�ǲ�������� ����
		
		Object obj6 = obj5;
		
		//Ŭ���̾�Ʈ�� ������Ʈ��ǲ��Ʈ������ �޾Ƽ� ���� ��
		TreeMap<Double, String> tm2 = (TreeMap<Double, String>) obj6;
		
		System.out.println(tm2.get(100.0));
		
		
	}
}
