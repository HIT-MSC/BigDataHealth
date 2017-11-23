package com.diagnosis;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import kdTree.EuclideanDistance;
import kdTree.KDTree;
import kdTree.KeyDuplicateException;
import kdTree.KeySizeException;

import com.vista.BayesClassifier;
import com.vista.ClassifyResult;



public class TestMain {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws KeyDuplicateException 
	 * @throws KeySizeException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws KeySizeException, KeyDuplicateException, SQLException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		mySql ml=new mySql();
		Connection conn=ml.getConnection();
		
		//�û�ע��
		patient pt=new patient();
		System.out.println("ע�����û���");
		pt.inputPatientInfo(pt.getPatientInfo(), conn);
		//getPatientInfo()�����ǵ������Ƕ�ȡ�û���ע����Ϣ��
		//����ֵ��Map���洢�����û���ĳ������������ֵ�����ļ�ֵ�ԣ�����<"name",����>
		//inputPatientInfo�����������ǽ�ע����Ϣ�浽���ݿ���
		
		
		//ҽԺע��
		hospital h=new hospital();
		System.out.println("ע��ҽԺ��Ϣ��");
		h.inputInfo(h.getInfo(), conn);
		//getInfo()�����������Ƕ�ȡҽԺ��ע����Ϣ������ֵҲ��Map�����Ҳ��ĳ������������ֵ�����ļ�ֵ��
		//inputInfo�����������ǽ�ע����Ϣ�浽���ݿ���
		
		
		//�������
		System.out.println("������ϣ�");
		Diagnosis diagnosis=new Diagnosis();
		System.out.println("����������֢״��Ϣ��");
		Scanner s=new Scanner(System.in);
		String symptom=s.next();
		ArrayList<Map.Entry<String, Double>>diagnosis_result=diagnosis.diagnosis(symptom, conn);
		//diagnosis������������ǲ��˵�֢״����symptom�����ݿ�Ľ������conn������symptom������Ͻ��������û������
		//����ֵ����Ͻ����Ҳ���ǿ��ܵļ�������������ƶȣ�������ArrayList<Map.Entry<String, Double>>������<"��ð��ICD���"��89.1>
		
		
		
		//�Ż����
		System.out.println("������ϣ�");
		diagnosis.optimizeDiagnosis(diagnosis_result, symptom, conn);
		//optimizeDiagnosis�����������Ǹ����û���ǰ����Ͻ���ķ��������ж������
		//������diagnosis_result-ǰ����Ͻ����Ҳ����diagnosis�ķ���ֵ��symptom-�����֢״��Ϣ��conn-���ݿ�������
		//��optimizeDiagnosis�е�����mark_related_disease����������û��ķ�����Ϣ��Ҳ���ǵõ����ܵļ��������
		//����ֵ���Ż���Ͻ����Ҳ���ǿ��ܵļ�������������ƶȣ�������ArrayList<Map.Entry<String, Double>>������<"��ð��ICD���"��89.1>
		
		
		
		//ҽԺ�Ƽ�
		System.out.println("ҽԺ�Ƽ���");
		int patient_id=3000;
		int disease_id=5;
		double range=15;
		evaluater ev=new evaluater();
		ArrayList<Map.Entry<Integer, Double>>  CFScoreMap=new ArrayList<>();
		CFScoreMap=ev.CFEvaluator(patient_id,disease_id, range, conn);
		
		for(int index=0;index<CFScoreMap.size();++index){
			System.out.println("ҽԺ���:"+CFScoreMap.get(index).getKey()+";����:"+CFScoreMap.get(index).getValue());
		}
		//CFEvaluator�����������Ǹ���ǰ�����Ͻ��Ϊ�û��Ƽ�һЩҽԺ
		//�������patient_id��ʾ�û���id���ò��������ڵ�¼�����ȡ
		//�������disease_id��ʾ�û�����Ͻ��������ѡ��ļ�����ţ��ò�����������Ͻ�������ȡ
		//�������range��ʾ���Ƽ���ҽԺ�����û��������룬�ò���ĿǰĬ��15
		//�������conn��ʾ���ݿ�������
		//����ֵΪArrayList<Map.Entry<Integer, Double>> ���͵ģ���ʾ�Ƽ���ҽԺ�����ۺ����֣�����<"��������ҵ��ѧУҽԺ"��4.3>
		
		
		//�������Ƽ�¼
		System.out.println("�������Ƽ�¼��");
		Map<Integer,Map> m=new HashMap<Integer,Map>();
		Map diagnosis_info=new HashMap();
		diagnosis_info.put("disease_id", disease_id);
		diagnosis_info.put("patient_id", patient_id);
		int hospital_id=17;
		//hospital_id���Դ��Ƽ������л�ȡ
		pt.inputMedicalRecords(pt.getMedicalRecords(diagnosis_info, hospital_id), conn);
		//inputMedicalRecords����ص���ϼ�¼�������ݿ���
		
		
		//����ҽԺ
		System.out.println("����ҽԺ��");
		ev.inputEvaluate_turnover(ev.getEvaluate(diagnosis_info, hospital_id), conn);
		//inputEvaluate_turnover���û���������Ϣ�������ݿ�
		//getEvaluate���������ǻ�ȡ�û���������Ϣ
	}

}
