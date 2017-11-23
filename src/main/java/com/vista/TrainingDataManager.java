package com.vista;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import database.DBHelper;

/**
 * ѵ����������
 */

public class TrainingDataManager {
	private String[] traningFileClassifications;// ѵ�����Ϸ��༯��
	private File traningTextDir;// ѵ�����ϴ��Ŀ¼
	private static String defaultPath = "D:\\disease";
	static DBHelper db1 = null;
	static ResultSet ret = null;
	static ArrayList disease_id = new ArrayList();;
	static ArrayList disease_Title = new ArrayList();;
	static ArrayList disease_Content = new ArrayList();;
	static String diseaseId = null;
	static String diseaseT = null;
	static String diseaseC = null;
	static boolean flag = true;

	public TrainingDataManager() {
		traningTextDir = new File(defaultPath);
		if (!traningTextDir.isDirectory()) {
			throw new IllegalArgumentException("ѵ�����Ͽ�����ʧ�ܣ� [" + defaultPath + "]");
		}
		this.traningFileClassifications = traningTextDir.list();

		String collectDisease = "SELECT * FROM disease_info";
		if (flag) {
			selectDisease(collectDisease);
		}
		
		// for (int i = 0; i < traningTextDir.list().length; i++) {
		//
		// System.err.println("woro:"+traningFileClassifications[i]);
		// }
	}

	/**
	 * ����ѵ���ı�������������Ŀ¼��
	 * 
	 * @return ѵ���ı����
	 */
	public String[] getTraningClassifications() {
		return this.traningFileClassifications;
	}

	/**
	 * ����ѵ���ı���𷵻��������µ�����ѵ���ı�·����full path��
	 * 
	 * @param classification
	 *            �����ķ���
	 * @return ���������������ļ���·����full path��
	 */
	public String getFilesPath(String classification) {
		File classDir = new File(traningTextDir.getAbsolutePath() + File.separator + classification);
		// System.out.println(classification);
		// String[] ret = classDir.list();
		// System.out.println(traningTextDir.getPath( ) +File.separator
		// +classification);
		// System.out.println("ret.length: "+classDir.list().length);
		String ret = traningTextDir.getAbsolutePath() + File.separator + classification;
		// for (int i = 0; i < ret.length; i++)
		// {
		// ret[i] = traningTextDir.getPath() +File.separator +ret[i];
		// }
		return ret;
	}

	/**
	 * ���ظ���·�����ı��ļ�����
	 * 
	 * @param filePath
	 *            �������ı��ļ�·��
	 * @return �ı�����
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException
	 */
	public static String getText(String filePath) throws FileNotFoundException, IOException {

		InputStreamReader isReader = new InputStreamReader(new FileInputStream(filePath), "GBK");
		BufferedReader reader = new BufferedReader(isReader);
		String aline;
		StringBuilder sb = new StringBuilder();

		while ((aline = reader.readLine()) != null) {
			sb.append(aline + " ");
		}
		isReader.close();
		reader.close();
		return sb.toString();
	}

	/**
	 * ����ѵ���ı��������е��ı���Ŀ
	 * 
	 * @return ѵ���ı��������е��ı���Ŀ
	 */
	public int getTrainingFileCount() {
		int ret = 0;
		for (int i = 0; i < traningFileClassifications.length; i++) {
			ret += getTrainingFileCountOfClassification(traningFileClassifications[i]);
		}
		return ret;
	}

	/**
	 * ����ѵ���ı������ڸ��������µ�ѵ���ı���Ŀ
	 * 
	 * @param classification
	 *            �����ķ���
	 * @return ѵ���ı������ڸ��������µ�ѵ���ı���Ŀ
	 */
	public int getTrainingFileCountOfClassification(String classification) {
		File classDir = new File(traningTextDir.getPath() + File.separator + classification);
		return 1;
	}

	/**
	 * ���ظ��������а����ؼ��֣��ʵ�ѵ���ı�����Ŀ
	 * 
	 * @param classification
	 *            �����ķ���
	 * @param key
	 *            �����Ĺؼ��֣���
	 * @return ���������а����ؼ��֣��ʵ�ѵ���ı�����Ŀ
	 */
	public int getCountContainKeyOfClassification(String classification, String key) {
		int ret = 0;
//		System.err.println("yyyy:" + classification);

		String DiseaseText = "";
		String DiseaseTit = "";
		int num = 0;
		for (int i = 1; i < disease_id.size(); i++) {
			if (classification.equals(disease_Title.get(i))) {
				num = i;
//				System.err.println("num:"+(num+1));
			}
		}
		
		DiseaseText = (String) disease_Content.get(num);
		if (DiseaseText.contains(key)) {
			ret++;
		}
		return ret;
	}

	public static void selectDisease(String sql) {
		// TODO Auto-generated method stub

		Long startTIme = System.currentTimeMillis();
		System.out.println(sql);
		int number = 0;
		db1 = new DBHelper(sql);// ����DBHelper����
		try {
			ret = db1.pst.executeQuery();// ִ����䣬�õ������
			while (ret.next()) {
				diseaseId = ret.getString("disease_id");
				diseaseT = ret.getString("title");
				diseaseC = ret.getString("content");
				// System.out.println(diseaseId + "\t" + diseaseT + "\t" +
				// diseaseC);
				number++;
				
				setValue(diseaseId, diseaseT, diseaseC, number);
			} // ��ʾ����
			System.out.println("number :" + number);
			ret.close();
			db1.close();// �ر�����
			flag = false;
			Long endTime = System.currentTimeMillis();
			System.err.println(endTime - startTIme);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setValue(String id, String a, String b, int num) {
		disease_id.add(id);
		disease_Title.add(a);
		disease_Content.add(b);

	}
}