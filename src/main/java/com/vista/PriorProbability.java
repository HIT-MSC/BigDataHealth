package com.vista;

/**
* ������ʼ���
* <h3>������ʼ���</h3>
* P(c<sub>j</sub>)=N(C=c<sub>j</sub>)<b>/</b>N <br>
* ���У�N(C=c<sub>j</sub>)��ʾ���c<sub>j</sub>�е�ѵ���ı�������
* N��ʾѵ���ı�����������
*/

public class PriorProbability 
{
	private static TrainingDataManager tdm =new TrainingDataManager();

	/**
	* �������
	* @param c �����ķ���
	* @return ���������µ��������
	*/
	public static double calculatePc(String c)
	{
		double ret = 0d;
//		float Nc = tdm.getTrainingFileCountOfClassification(c);
		float Nc = 1;
		float N = tdm.getTrainingFileCount();
		ret = Nc / N;
		return ret;
	}
}