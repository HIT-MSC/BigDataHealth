package com.diagnosis;


import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class hospital {
	//�õ�ҽԺ��Ϣ
	public Map getInfo() throws UnsupportedEncodingException{
		Map hospitalInfo=new HashMap();
		Scanner s=new Scanner(System.in);
		System.out.println("������ҽԺ���ƣ�");
		String name=s.next();
		System.out.println("������ҽԺ��ַ��");
		String address=s.next();
		MapDistance mapDis=new MapDistance();
		Map<String,Double> addressMap=mapDis.getLngAndLat(address);
		System.out.print("������ҽԺ�ȼ���");
		int level=s.nextInt();
		hospitalInfo.put("name", name);
		hospitalInfo.put("longitude", addressMap.get("lng"));
		hospitalInfo.put("latitude", addressMap.get("lat"));
		hospitalInfo.put("level", level);
		return hospitalInfo;
	}
	//����ҽԺ��Ϣ
	public int inputInfo(Map hospitalInfo,Connection conn)
	{
		String sql;
		String name=(String)hospitalInfo.get("name");
		double longitude=(double)hospitalInfo.get("longitude");
		double latitude=(double)hospitalInfo.get("latitude");
		int level=(int)hospitalInfo.get("level");
		
		sql="insert into hospital_info(name,longitude,latitude,level) "
				+"values('"+name+"','"+longitude+"','"+latitude+"','"+level+"')";
		mySql ml=new mySql();
		int result=ml.modifyData(conn, sql);
		System.out.println("ҽԺ��Ϣע��ɹ���");
		return result;
	}
	
	
}
