package bigDataPsychologist;

/*
 * ԭ�򣺹�������У���ͥ������
 * 
 * ���ݽṹ��HashMap<String, List<String>> trainData�洢��ѵ����������
 * HashMap<String, List<Boolean>> TrainDataReason�洢��ÿһ��ѵ��������ԭ��һ��List<String>��Ӧһ��List<Boolean>��List<Boolean>����4��ֵ����ԭ���Ӧ��ֵΪtrue��������ԭ��
 * List<String> testData�洢��Ҫ���Ե�����
 *  
 * �ӿڣ�setTrainData(HashMap<String, List<String>> trainData, HashMap<String, List<Boolean>> TrainDataReason) ����ѵ������   ����Ϊ��
 * setTestData(HashMap<String, List<String>> testData)���ò�������     ����Ϊ��
 * getResult()�õ����������ֵΪList<Double>
 * 
 * 
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Collections;  
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class bayesianForReason {
	
	private HashMap<String, List<String>> trainData;
	private HashMap<String, List<Boolean>> trainDataReason;
	private List<String> keyWord;
	private List<Integer> numOfWordEachTrainData;
	private int[][] keyWordNum;
	private HashSet<String> trainDataSet; 
	private int sumOfAllData;
	
	
	public bayesianForReason(){
		trainData = new HashMap<String, List<String>>();
		numOfWordEachTrainData = new ArrayList<Integer>();
		trainDataReason = new HashMap<String, List<Boolean>>();
		keyWord = new ArrayList<String>();
		keyWordNum = new int[1000][1000];
		sumOfAllData = 1;
		trainDataSet = new HashSet<String>();
	}
	
	public void setTrainData(HashMap<String, List<String>> trainData, HashMap<String, List<Boolean>> trainDataReason){
		this.trainData = trainData;
		this.trainDataReason = trainDataReason;
	}
	
	public void setTestData(List<String> keyWord){
		this.keyWord = keyWord;
	}
	
	
	public List<Double> getResult(){
		return bayesianModel();
	}
	
	private void calculateData(){    //��������
		for(int i = 0; i < trainData.size(); i++){
			numOfWordEachTrainData.add(trainData.get(i+"").size());
			sumOfAllData += trainData.get((i + "")).size();
			for(int j = 0; j < trainData.get(i+"").size(); j++){
				trainDataSet.add(trainData.get(i+"").get(j));
			}
			for(int j = 0; j < keyWord.size(); j++){
				keyWordNum[i][j] = Collections.frequency(trainData.get(i+""), keyWord.get(j));
			}
		}
	}
	
	
	private int getMAX(List<Double> num){
		if(num.get(0) >= num.get(1) && num.get(0) >= num.get(2) && num.get(0) >= num.get(3)){
			return 0;
		}
		if(num.get(1) >= num.get(0) && num.get(1) >= num.get(2) && num.get(1) >= num.get(3)){
			return 1;
		}
		if(num.get(2) >= num.get(1) && num.get(2) >= num.get(0) && num.get(2) >= num.get(3)){
			return 2;
		}
		return 3;
	}
	
	private List<Double> bayesianModel(){   //����ģ�ͣ������ؽ��
		//set();
		calculateData();
		List<Boolean> result = new ArrayList<Boolean>();
		List<Double> num = new ArrayList<Double>();
		int position;
		double probability_1, probability_2, probability_3, probability_4;
		double probability1_1, probability1_2, probability1_3, probability1_4;
		double probability2_1 = 1;
		double probability2_2 = 1;
		double probability2_3 = 1;
		double probability2_4 = 1;
		double down_1, down_2, down_3, down_4;
		double numOf_1 = 0.1;
		double numOf_2 = 0.1;
		double numOf_3 = 0.1;
		double numOf_4 = 0.1;

		for(int i = 0; i < trainDataReason.size(); i++){
			if(trainDataReason.get(i+"").get(0)){
				numOf_1 += numOfWordEachTrainData.get(i);
			}
			if(trainDataReason.get(i+"").get(1)){
				numOf_2 += numOfWordEachTrainData.get(i);
			}
			if(trainDataReason.get(i+"").get(2)){
				numOf_3 += numOfWordEachTrainData.get(i);
			}
			if(trainDataReason.get(i+"").get(3)){
				numOf_4 += numOfWordEachTrainData.get(i);
			}
		}
		down_1 = numOf_1 + trainDataSet.size();
		down_2 = numOf_2 + trainDataSet.size();
		down_3 = numOf_3 + trainDataSet.size();
		down_4 = numOf_4 + trainDataSet.size();
		probability1_1 = (double)(numOf_1 / sumOfAllData);
		probability1_2 = (double)(numOf_2 / sumOfAllData);
		probability1_3 = (double)(numOf_3 / sumOfAllData);
		probability1_4 = (double)(numOf_4 / sumOfAllData);
		for(int i = 0; i < keyWord.size(); i++){
			int sumOfKeyWord_1 = 1;
			int sumOfKeyWord_2 = 1;
			int sumOfKeyWord_3 = 1;
			int sumOfKeyWord_4 = 1;
//System.out.println(trainDataReason);
			for(int j = 0; j < trainDataReason.size(); j++){
//System.out.println(trainDataReason.get(j).get(0));
				if(trainDataReason.get(j+"").get(0)){
					sumOfKeyWord_1 += keyWordNum[j][i];
				}
				if(trainDataReason.get(j+"").get(1)){
					sumOfKeyWord_2 += keyWordNum[j][i];
				}
				if(trainDataReason.get(j+"").get(2)){
					sumOfKeyWord_3 += keyWordNum[j][i];
				}
				if(trainDataReason.get(j+"").get(3)){
					sumOfKeyWord_4 += keyWordNum[j][i];
				}
			}
			probability2_1 *= (sumOfKeyWord_1 / down_1);
			probability2_2 *= (sumOfKeyWord_2 / down_2);
			probability2_3 *= (sumOfKeyWord_3 / down_3);
			probability2_4 *= (sumOfKeyWord_4 / down_4);
			
			probability2_2 *= ( 1 / probability2_1);
			probability2_3 *= ( 1 / probability2_1);
			probability2_4 *= ( 1 / probability2_1);
			probability2_1 *= ( 1 / probability2_1);
			
//			System.out.println(probability2_1);
//			System.out.println(probability2_2);
//			System.out.println(probability2_3);
//			System.out.println(probability2_4);
			
		}
		
		probability_1 = probability1_1 * probability2_1;
		probability_2 = probability1_2 * probability2_2;
		probability_3 = probability1_3 * probability2_3;
		probability_4 = probability1_4 * probability2_4;
//		probability_1 *= ( 1 / probability_1 *1.2);
//		probability_2 *= ( 1 / probability_1 *1.2);
//		probability_3 *= ( 1 / probability_1 *1.2);
//		probability_4 *= ( 1 / probability_1 *1.2);
//System.out.println(probability_1 + " " + probability_2 + " " + probability_3 + " " + probability_4);
		num.add(probability_1);
		num.add(probability_2);
		num.add(probability_3);
		num.add(probability_4);
		System.out.println(num);
		return num;
//		position = getMAX(num);
//		switch(position){
//			case(0):{
//				System.out.println("0");
//				result.add(true);
//				result.add(false);
//				result.add(false);
//				result.add(false);
//			}
//			case(1):{
//				System.out.println("1");
//				result.add(false);
//				result.add(true);
//				result.add(false);
//				result.add(false);
//			}
//			case(2):{
//				System.out.println("2");
//				result.add(false);
//				result.add(false);
//				result.add(true);
//				result.add(false);
//			}
//			default:{
//				System.out.println("3");
//				result.add(false);
//				result.add(false);
//				result.add(false);
//				result.add(true);
//			}
//		}
//		return result;
	}
	
	
	public void tempSet(){
		int num = 0; 
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://ReasonBayesian.txt")));
			while (bReader.ready()){
				List<String> tempList = new ArrayList<String>();
				String st;
				String stList[];
				st = bReader.readLine();
				stList = st.split(" ");
				for(int i = 0; i < stList.length; i++)
					tempList.add(stList[i]);
//System.out.println(tempList);
				trainData.put(num + "", tempList);
				num++;
				//System.out.println(inputCheckString);
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Boolean> tempBoo = new ArrayList<Boolean>();
		tempBoo.add(true);
		tempBoo.add(false);
		tempBoo.add(false);
		tempBoo.add(false);
		for(int i = 0; i < 3; i++){
			trainDataReason.put(i+"", tempBoo);
		}
		tempBoo = new ArrayList<Boolean>();
		tempBoo.add(false);
		tempBoo.add(true);
		tempBoo.add(false);
		tempBoo.add(false);
		for(int i = 3; i < 6; i++){
			trainDataReason.put(i+"", tempBoo);
		}
		tempBoo = new ArrayList<Boolean>();
		tempBoo.add(false);
		tempBoo.add(false);
		tempBoo.add(true);
		tempBoo.add(false);
		for(int i = 6; i < 9; i++){
			trainDataReason.put(i+"", tempBoo);
		}
		tempBoo = new ArrayList<Boolean>();
		tempBoo.add(false);
		tempBoo.add(false);
		tempBoo.add(false);
		tempBoo.add(true);
		for(int i = 9; i < 12; i++){
			trainDataReason.put(i+"", tempBoo);
		}
		
		
//		keyWord.add("����");
//		keyWord.add("����");
//		keyWord.add("����");
//		keyWord.add("˯��");
//		keyWord.add("˯��");
//		keyWord.add("����");
//		keyWord.add("ƣ��");
//		keyWord.add("̰˯ ");
//		keyWord.add("˵��");
//		keyWord.add("����");
//		keyWord.add("����");
//		keyWord.add("ʧ��");
//		keyWord.add("ʧ��");
//		keyWord.add("����");
//		keyWord.add("���� ");
		
		
//		keyWord.add("����");
//		keyWord.add("��ϧ");
//		keyWord.add("���");
//		keyWord.add("��į");
//		keyWord.add("����");
//		keyWord.add("����");
//		keyWord.add("����");
//		keyWord.add("ʹ�� ");
//		keyWord.add("����");
//		keyWord.add("���");
//		keyWord.add("����");
//		keyWord.add("����");
//		keyWord.add("��");
//		keyWord.add("��");
//		keyWord.add("�� ");
//		
//		keyWord.add("�Ұ�");
//		keyWord.add("Ƣ��");
//		keyWord.add("��߶");
//		keyWord.add("����");
//		keyWord.add("����");
//		keyWord.add("ά��");
//		keyWord.add("���");
//		keyWord.add("���� ");
//		keyWord.add("��");
//		keyWord.add("����");
//		keyWord.add("��Ȼ");
//		keyWord.add("Ƣ��");
//		keyWord.add("Сʱ��");
//		keyWord.add("��");
//		keyWord.add("�Ұ� ");
//		
//		keyWord.add("ծ");
//		keyWord.add("����");
//		keyWord.add("����");
//		keyWord.add("����");
//		keyWord.add("׬Ǯ");
//		keyWord.add("ʣ��");
//		keyWord.add("����");
//		keyWord.add("Ǯ ");
//		keyWord.add("��");
//		keyWord.add("ÿ��");
//		keyWord.add("����");
//		keyWord.add("���ÿ�");
//		keyWord.add("����");
//		keyWord.add("����");
//		keyWord.add("Ƣ�� ");
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		bayesianForReason bfr = new bayesianForReason();
		bfr.tempSet();
		bfr.getResult();

	}

}
