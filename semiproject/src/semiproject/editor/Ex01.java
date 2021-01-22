package semiproject.editor;

import java.util.TreeMap;

public class Ex01 {
	
	public static void main(String[] args) {
		
		Object obj1;
		
		/*
		//클라이언트 채팅침
		String string = "문자열";
		
		//채팅친걸로 서버에 오브젝트아웃풋스트림
		obj1 = string; // 고객이 포장후 우체국에 맞김
		
		//서버에서는 인풋스트림으로 받음
		Object obj2 = obj1; // 곤지암우편집중국
		
		//타 클라이언트에서 아웃풋스트림 
		Object obj3 = obj2; // 배달받음
		
		String o = (String)obj3; // 전달 받고 포장 뜯음
		
		System.out.println(o); // 내용물 확인.
		*/
		
		
		//클라이언트가 게임 정보 생김
		
		TreeMap<Double, String> tm = new TreeMap<>();
		
		tm.put(100.0, "트리맵임");
		
		//서버에 오브젝트아웃풋스트림
		Object obj4 = tm;
		
		//서버는 오브젝트인풋스트림으로 받음
		Object obj5 = obj4;
		
		//서버가 타 클라이언트에게 오브젝트아웃풋스림으로 보냄
		
		Object obj6 = obj5;
		
		//클라이언트가 오브젝트인풋스트림으로 받아서 포장 뜯어봄
		TreeMap<Double, String> tm2 = (TreeMap<Double, String>) obj6;
		
		System.out.println(tm2.get(100.0));
		
		
	}
}
