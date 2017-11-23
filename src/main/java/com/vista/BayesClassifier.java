package com.vista;

import com.vista.ChineseSpliter;
import com.vista.ClassConditionalProbability;
import com.vista.PriorProbability;
import com.vista.TrainingDataManager;

import database.DBHelper;
import database.DataBaseConnection;

import com.vista.StopWordsHandler;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * ���ر�Ҷ˹������
 */
public class BayesClassifier {
	private TrainingDataManager tdm;// ѵ����������
	private String trainnigDataPath;// ѵ����·��
	private static double zoomFactor = 10.0f;
	static DBHelper db1 = null;
	static ResultSet ret = null;
	static String backline;
	static String jaja;

	static String ufname = null;
	static String ulname = null;
	static String udate = null;

	static String diseaseId = null;
	static String diseaseT = null;
	static String diseaseC = null;
	static String responseId = null;

	static ArrayList disease_id = new ArrayList();;
	static ArrayList disease_Title = new ArrayList();;
	static ArrayList disease_Content = new ArrayList();;

	// static String[] diseaseTitle = null;
	// static String[] diseaseContent = null;

	/**
	 * Ĭ�ϵĹ���������ʼ��ѵ����
	 */
	public BayesClassifier() {
		tdm = new TrainingDataManager();
	}

	/**
	 * ����������ı���������X�ڸ����ķ���Cj�е����������� <code>ClassConditionalProbability</code>����ֵ
	 * 
	 * @param X
	 *            �������ı���������
	 * @param Cj
	 *            ���������
	 * @return ����������������ֵ����<br>
	 */
	double calcProd(String[] X, String Cj) {
		double ret = 1.0d;
		// ��������������
		for (int i = 0; i < X.length; i++) {
			String Xi = X[i];
			// ��Ϊ�����С�����������֮ǰ�Ŵ�10����������ս������Ӱ�죬��Ϊ����ֻ�ǱȽϸ��ʴ�С����
			ret *= ClassConditionalProbability.calculatePxc(Xi, Cj) * zoomFactor;
		}
		// �ٳ����������
		ret *= PriorProbability.calculatePc(Cj);
		return ret;
	}

	/**
	 * ȥ��ͣ�ô�
	 * 
	 * @param text
	 *            �������ı�
	 * @return ȥͣ�ôʺ���
	 */
	public String[] DropStopWords(String[] oldWords) {
		Vector<String> v1 = new Vector<String>();
		for (int i = 0; i < oldWords.length; ++i) {
			if (StopWordsHandler.IsStopWord(oldWords[i]) == false) {// ����ͣ�ô�
				v1.add(oldWords[i]);
			}
		}
		String[] newWords = new String[v1.size()];
		v1.toArray(newWords);
		return newWords;
	}

	/**
	 * �Ը������ı����з���
	 * 
	 * @param text
	 *            �������ı�
	 * @return ������
	 */
	@SuppressWarnings("unchecked")
	// responseIn �û������Ĺؼ���
	public List<ClassifyResult> classify(String text) {
		String[] terms = null;
		// terms ԭ�����Ĺؼ��ʼ���
		terms = ChineseSpliter.split(text, " ").split(" ");// ���ķִʴ���(�ִʺ������ܻ�������ͣ�ôʣ�
		terms = DropStopWords(terms);// ȥ��ͣ�ôʣ�����Ӱ�����
	/*	for (int i = 0; i < terms.length; i++) {

			System.out.println(terms[i]);
		}*/

		String[] Classes = new String[disease_id.size()];
		for (int i = 0; i < disease_id.size(); i++) {
			Classes[i] = (String) disease_Title.get(i);

		}

		double probility = 0.0d;
		List<ClassifyResult> crs = new ArrayList<ClassifyResult>();// ������
		//System.out.println("Classes.length: "+Classes.length);
		for (int i = 0; i < Classes.length; i++) {
			String Ci = Classes[i];// ��i������
			probility = calcProd(terms, Ci);// ����������ı���������terms�ڸ����ķ���Ci�еķ�����������
			// ���������
			ClassifyResult cr = new ClassifyResult();
			cr.classification = Ci;// ����
			cr.probility = probility;
			// System.out.println("In process...."+Ci);
			// System.out.println(Ci + "��" + cr.probility);
			crs.add(cr);
		}
		// �������ʽ����������
		java.util.Collections.sort(crs, new Comparator() {
			public int compare(final Object o1, final Object o2) {
				final ClassifyResult m1 = (ClassifyResult) o1;
				final ClassifyResult m2 = (ClassifyResult) o2;
				if (m1.probility < m2.probility) {
					return 1;

				} else if (m1.probility == m2.probility) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		
		List<ClassifyResult> returnResult=new ArrayList<ClassifyResult>();
		for(int i=0;i<crs.size()&&i<5;++i)
		{
			returnResult.add(crs.get(i));
		}
		return returnResult;
		// return "dasdas";
	}

	

	

	@SuppressWarnings("null")
	public static void selectDisease(String sql) {
		// TODO Auto-generated method stub

//		Long startTIme = System.currentTimeMillis();
//		System.out.println(sql);
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

			ret.close();
			db1.close();// �ر�����
//			Long endTime = System.currentTimeMillis();
//			System.err.println(endTime - startTIme);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setValue(String id, String a, String b, int num) {
		disease_id.add(id);
		disease_Title.add(a);
		disease_Content.add(b);

	}
	public int getDiseaseId(String title){
		String[] DiseaseText = new String[disease_id.size()];
		int Did = 0;
		for (int i = 0; i < disease_id.size(); i++) {
			DiseaseText[i] = (String) disease_Title.get(i);

			if (title.equals(DiseaseText[i])) {
				Did = i;
			}
		}
		
		
		return Did;
	}
	
	public String getDiseaseText(String title){
		String DiseaseText = "";
		for (int i = 0; i < disease_id.size(); i++) {
			DiseaseText = DiseaseText+(String) disease_Content.get(i);

		}
//		for (int t = 0; t < DiseaseText.length; t++) {
//			String string = DiseaseText[t];
//			System.out.println("getDiseaseText:" + string);
//
//		}
		
		return DiseaseText;
	}
	@SuppressWarnings("null")
	//public static void main(String[] args){ 
	public  List<ClassifyResult> diagnosis() {

		String collectDisease = "SELECT * FROM disease_info";
		selectDisease(collectDisease);

		
		String disease_describe;	
		Scanner s=new Scanner(System.in);
		System.out.println("������֢״������");
		disease_describe=s.next();
		DataBaseConnection dataBaseConnection = null;
		
		BayesClassifier classifier = new BayesClassifier();// ����Bayes������
		//System.out.println(disease_describe);
		List<ClassifyResult> result=new ArrayList<ClassifyResult>();
		result = classifier.classify(disease_describe);// ���з���
		System.out.println("��Ͻ����");
		for(int i=0;i<result.size();++i)
		{
			System.out.println(result.get(i).classification);
		}
		return result;

	}

}