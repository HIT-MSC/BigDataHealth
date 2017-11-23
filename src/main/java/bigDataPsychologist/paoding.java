package bigDataPsychologist;

/*
 * �ӿڣ�setString(String s)�����ַ���s��s�ǽ�Ҫ�ִʵ��ַ��� ������ֵΪ��
 * paodingOperate()���зִʴ�������ֵΪList<String>�����д洢�����еĴʡ�
 */


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import net.paoding.analysis.analyzer.PaodingAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;


public class paoding{
	
	private List<String> words;
	private String s;
	
	public paoding(){
		words = new ArrayList<String>();
	}
	
	
	public void setString(String s){
		this.s = s;
	}
	
	public List<String> paodingOperate() throws Exception{
		Analyzer analyzer = new PaodingAnalyzer(); 
		String  indexStr = s;
        StringReader reader = new StringReader(indexStr); 
        TokenStream ts = analyzer.tokenStream(indexStr, reader); 
        Token t = ts.next(); 
        while (t != null) {
        	words.add(t.termText());
            //System.out.println(t.termText()+"/"); 
            t = ts.next(); 
        } 
        return words;
	}
	
	
	
	public static void main(String[] args) throws Exception { 
		String st = new String();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(new File("d://paodingData.txt")));
			while (bReader.ready()){
				st = bReader.readLine();
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<String> s;
		paoding p = new paoding();
		p.setString(st);
		s = p.paodingOperate();
		System.out.println(s);
    } 
	
}