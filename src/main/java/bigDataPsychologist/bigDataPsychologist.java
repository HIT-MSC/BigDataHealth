package bigDataPsychologist;


/*
 * setTrainData(HashMap<String, List<String>> trainData, List<Boolean> whetherIll)����ѵ��������
 * setTestData(HashMap<String, List<String>> testData, List<Boolean> testWhether)����adaboost�Ĳ�������
 * setKeyWord(List<String> keyWord)����logical�Ĺؼ���
 * setCheckString(String inputCheckString)����Ҫ�����ַ���
 * setTrainDataForReason(HashMap<String, List<String>> trainDataForReason, HashMap<String, List<Boolean>> trainDataForReasonReason)����ԭ���ѵ������
 * run�������г���
 */



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class bigDataPsychologist {
	
	
	
	private HashMap<String, List<String>> trainData;
	private HashMap<String, List<String>> testData;
	private List<Boolean> testWhether;
	private List<Boolean> whetherIll;
	private List<String> keyWord;
	private List<String> checkData;
	private double resultNum;
	private String inputCheckString;
	private String reason;
	private List<Double> resultForReason;
	private HashMap<String, List<String>> trainDataForReason;
	private HashMap<String, List<Boolean>> trainDataForReasonReason;
	
	public bigDataPsychologist(){
		trainData = new HashMap<String, List<String>>();
		whetherIll = new ArrayList<Boolean>();
		testData = new HashMap<String, List<String>>();
		testWhether = new ArrayList<Boolean>();
		keyWord = new ArrayList<String>();
		checkData = new ArrayList<String>();
		trainDataForReason = new HashMap<String,List<String>>();
		trainDataForReasonReason = new HashMap<String, List<Boolean>>();
		
	}
	
	public void setTrainData(HashMap<String, List<String>> trainData, List<Boolean> whetherIll){
		this.trainData = trainData;
		this.whetherIll = whetherIll;
	}
	
	public void setTestData(HashMap<String, List<String>> testData, List<Boolean> testWhether){
		this.testData = testData;
		this.testWhether = testWhether;
	}
	
	public void setKeyWord(List<String> keyWord){
		this.keyWord = keyWord;
	}
	
	public void setCheckString(String inputCheckString){
		this.inputCheckString = inputCheckString;
	}
	
	public void setTrainDataForReason(HashMap<String, List<String>> trainDataForReason, HashMap<String, List<Boolean>> trainDataForReasonReason){
		this.trainDataForReason = trainDataForReason;
		this.trainDataForReasonReason = trainDataForReasonReason;
	}
	
	
	public void paoding(){
		paoding p = new paoding();
		p.setString(inputCheckString);
		try {
			this.checkData = p.paodingOperate();
//System.out.println(checkData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void adaboost(){
		double[] alph;
		adaboost ab = new adaboost();
		ab.setTestData(testData, testWhether);
		bayesian b = new bayesian();
		b.setTrainData(trainData, whetherIll);
		logical l = new logical();
		l.setTrainData(trainData, whetherIll);
		l.setKeyWord(keyWord);
		l.run();
		
		alph = ab.calculate(b, l);
		System.out.println(alph[0]+ "~" + alph[1]);
		
		b.setTestData(checkData);
		b.getResult();
		l.setTestData(checkData);
		l.getResult();
		System.out.println("b : " + b.getResultNum());
		System.out.println("l : " + l.getResultNum());
		resultNum = b.getResultNum() * (alph[0] / (alph[0] + alph[1])) + l.getResultNum() * (alph[1] / (alph[0] + alph[1]));
		System.out.println(resultNum);
	}
	
	
	public void forReason(){
		bayesianForReason bfr = new bayesianForReason();
		bfr.setTrainData(trainDataForReason, trainDataForReasonReason);
		bfr.setTestData(checkData);
		resultForReason = bfr.getResult();
	}
	
	
	private void tempSet(){
		
		int key = 0;
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://data.txt")));
			List<String> data = new ArrayList<String>();
			while (bReader.ready()){	
				paoding p = new paoding();
				String line = bReader.readLine();
				//System.out.println(line);
				p.setString(line);
				trainData.put(""+key,p.paodingOperate());
				//System.out.println(p.paodingOperate());
				key ++;
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(trainData);
		
//		try {
//			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://data.txt")));
//			int time = 0;
//			int key = 0;
//			List<String> data = new ArrayList<String>();
//			while (bReader.ready()){
//				if((time % 3 == 0 && time != 0) || time == 29){
//					trainData.put(key + "", data);
//					key ++;
//					data = new ArrayList<String>();
//				}
//				String line = bReader.readLine();
//				String[] terms = line.split(" ");
//				for(int i = 0; i < terms.length; i++){
//					data.add(terms[i]);
//				}
//				time ++;
//			}
//			bReader.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		for(int i = 0; i < 5; i++)
			whetherIll.add(true);
		for(int i = 5; i < 10; i++)
			whetherIll.add(false);
//		testData.add("�˷�");
//		testData.add("����");
//		testData.add("ϲ��");
//		testData.add("���");
//		testData.add("����");
//		testData.add("��ʹ");
//		testData.add("���� ");
//		testData.add("���� ");
		keyWord.add("�˷�");
		keyWord.add("����");
		keyWord.add("ϲ��");
		keyWord.add("���");
		keyWord.add("����");
		keyWord.add("��ʹ");
		keyWord.add("���� ");
		keyWord.add("���� ");
//		
//		testData.add("�ɴ�");
//		testData.add("�ɴ�");
//		testData.add("�ɴ�");
//		testData.add("�ɴ�");
//		testData.add("�ɴ�");
		testData = trainData;
		testWhether = whetherIll;
		
		
//		try {
//			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://data.txt")));
//			while (bReader.ready()){
//				String line = bReader.readLine();
//				String[] terms = line.split(" ");
//				for(int i = 0; i < terms.length; i++){
//					checkData.add(terms[i]);
//				}
//			}
//			bReader.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
		
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://paodingData.txt")));
			while (bReader.ready()){
				this.inputCheckString = bReader.readLine();
				//System.out.println(inputCheckString);
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
//		checkData.add("ϲ��");
//		checkData.add("���");
//		checkData.add("����");
//		checkData.add("��ʹ");
//		checkData.add("���� ");
//		checkData.add("���� ");
//		checkData.add("�˷�");
//		checkData.add("����");
//		checkData.add("ϲ��");
//		checkData.add("���");
//		checkData.add("����");
//		checkData.add("��ʹ");
//		checkData.add("���� ");
//		checkData.add("���� ");
//		
//		checkData.add("�ɴ�");
//		checkData.add("�ɴ�");
//		checkData.add("�ɴ�");
//		checkData.add("�ɴ�");
//		checkData.add("�ɴ�");
		
		
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
				this.trainDataForReason.put(num + "", tempList);
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
			trainDataForReasonReason.put(i+"", tempBoo);
		}
		tempBoo = new ArrayList<Boolean>();
		tempBoo.add(false);
		tempBoo.add(true);
		tempBoo.add(false);
		tempBoo.add(false);
		for(int i = 3; i < 6; i++){
			trainDataForReasonReason.put(i+"", tempBoo);
		}
		tempBoo = new ArrayList<Boolean>();
		tempBoo.add(false);
		tempBoo.add(false);
		tempBoo.add(true);
		tempBoo.add(false);
		for(int i = 6; i < 9; i++){
			trainDataForReasonReason.put(i+"", tempBoo);
		}
		tempBoo = new ArrayList<Boolean>();
		tempBoo.add(false);
		tempBoo.add(false);
		tempBoo.add(false);
		tempBoo.add(true);
		for(int i = 9; i < 12; i++){
			trainDataForReasonReason.put(i+"", tempBoo);
		}
	}

	
	public void run(){
		tempSet();
		paoding();
		adaboost();
		if(resultNum < 0.5){
			forReason();
			System.out.println(resultForReason);
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		bigDataPsychologist bdp = new bigDataPsychologist();
		bdp.run();

	}

}
