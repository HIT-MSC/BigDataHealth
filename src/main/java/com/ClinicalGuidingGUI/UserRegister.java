package com.ClinicalGuidingGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.MouseInputAdapter;

import com.diagnosis.MapDistance;
import com.diagnosis.mySql;
import com.diagnosis.patient;
import com.mysql.jdbc.Connection;

public class UserRegister extends JFrame{
	private MyContentPane contentPane;
	private JTextField userNameField;
	private ButtonGroup sexGroup;
	private JRadioButton maleButton;
	private JRadioButton femaleButton;
	private JTextField birthDataField;
	private boolean birthDateFlag=false;
	private JTextField addressField;
	private JPasswordField userPasswordField;
	private JPasswordField confirmPasswordField;
	private TransparentButton registerButton;
	
	public static final int DEFAULT_WIDTH=700;
	public static final int DEFAULT_HEIGHT=500;
	
	private Map getPatientInfo(){
		Map patientInfo=new HashMap();
		String name=userNameField.getText();
		String address=addressField.getText();
		String birth=birthDataField.getText();
		String userPassword=String.valueOf(userPasswordField.getPassword());
		String confirPassword=String.valueOf(confirmPasswordField.getPassword());
		MapDistance mapDis=new MapDistance();
		Map<String,Double> addressMap=mapDis.getLngAndLat(address);
		
		if(name.isEmpty() || address.isEmpty() || birth.isEmpty() || userPassword.isEmpty() || confirPassword.isEmpty() || addressMap.isEmpty()){
			patientInfo.put("error", 1);//��Ϣ������
			return patientInfo;
		}
		if(!userPassword.equals(confirPassword)){
			patientInfo.put("error", 2);//���벻ƥ��
			return patientInfo;
		}
		
		
		patientInfo.put("name", name);
		
		patientInfo.put("longitude", addressMap.get("lng"));
		patientInfo.put("latitude", addressMap.get("lat"));
		patientInfo.put("passpord", userPassword);
		patientInfo.put("error", 0);//û�д���
		return patientInfo;
	}
	final class ButtonActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==registerButton){
				mySql ml=new mySql();
				Connection conn=(Connection) ml.getConnection();
				
				patient pt=new patient();
				int patient_id = 0;
				Map patientInfo=getPatientInfo();
				int error=(int)patientInfo.get("error");
				if(0==error){
					patient_id = pt.inputPatientInfo(patientInfo, conn);
					if(patient_id<=0){
						Map<String, String> textIcon=new HashMap<String,String>();
						textIcon.put("buttonText", "ȷ��");
						textIcon.put("buttonIcon", "messageButton.png");
						textIcon.put("labelText", "�Բ���ע��ʧ�ܣ�������ע�ᣡ");
						textIcon.put("labelIcon", "messageFailLabel.png");
						textIcon.put("titleText", "ע��ʧ��");
						MyMessageDialog messageDialog=new MyMessageDialog(textIcon);
					}
					else{
						Map<String, String> textIcon=new HashMap<String,String>();
						textIcon.put("buttonText", "��ס��");
						textIcon.put("buttonIcon", "messageButton.png");
						textIcon.put("labelText", "�װ���"+userNameField.getText()+",���μ������û���"+patient_id+"���Ա��´ε�¼��");
						textIcon.put("labelIcon", "messageSuccessLabel.png");
						textIcon.put("titleText", "ע��ɹ�");
						
						MyMessageDialog messageDialog=new MyMessageDialog(textIcon);
						dispose();
					}
					try {
						conn.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					};
				}
				else  if(1==error){
					Map<String, String> textIcon=new HashMap<String,String>();
					textIcon.put("buttonText", "ȷ��");
					textIcon.put("buttonIcon", "messageButton.png");
					textIcon.put("labelText", "�Բ���ע����Ϣ��д����������ע�ᣡ");
					textIcon.put("labelIcon", "messageFailLabel.png");
					textIcon.put("titleText", "ע��ʧ��");
					MyMessageDialog messageDialog=new MyMessageDialog(textIcon);
				}
				else if(2==error){
					Map<String, String> textIcon=new HashMap<String,String>();
					textIcon.put("buttonText", "ȷ��");
					textIcon.put("buttonIcon", "messageButton.png");
					textIcon.put("labelText", "�Բ���������������벻һ�£�������ע�ᣡ");
					textIcon.put("labelIcon", "messageFailLabel.png");
					textIcon.put("titleText", "ע��ʧ��");
					MyMessageDialog messageDialog=new MyMessageDialog(textIcon);
				}
				
			}
		}
	}
	
	final class TextFocusListener extends FocusAdapter{
		public void focusGained(FocusEvent e){
			if(!birthDateFlag)
			{
				JDateChooser dc=new JDateChooser();
				dc.showDateChooser();
				birthDateFlag=true;
				birthDataField.setText(dc.getDateFormat("yyyy-MM-dd"));
				addressField.requestFocusInWindow();
	
			}
		}
		public void focusLost(FocusEvent e){
			if(birthDateFlag){
				birthDateFlag=false;
			}
		}
	}
	final class ButtonMouseActionListener extends MouseInputAdapter{
		public void mouseEntered(MouseEvent e){
			if(e.getSource()==registerButton){
				((TransparentButton) e.getSource()).setForeground(new Color(0, 60, 150));
			}
		}
		public void mouseExited(MouseEvent e){
			if(e.getSource()==registerButton){
				((TransparentButton) e.getSource()).setForeground(Color.BLACK);
			}
		}
	}
	private void initCompoent(){
		contentPane=new MyContentPane();
		userNameField=new JTextField(20);
		sexGroup=new ButtonGroup();
		maleButton=new JRadioButton("��",true);
		maleButton.setContentAreaFilled(false);
		maleButton.setBorderPainted(false);
		femaleButton=new JRadioButton("Ů",false);
		femaleButton.setContentAreaFilled(false);
		sexGroup.add(maleButton);
		sexGroup.add(femaleButton);
		birthDataField=new JTextField(20);
		addressField=new JTextField(20);
		userPasswordField=new JPasswordField(20);
		confirmPasswordField=new JPasswordField(20);
		registerButton=new TransparentButton("ע��",new ImageIcon("registerButton.png"));
	}
	
	private void addComponet(){
		TransparentPane titlePanel=new TransparentPane();
		JLabel titleLabel=new JLabel("���û�������Ϣ�ǼǱ�");
		titleLabel.setFont(new Font("������κ", Font.PLAIN, 25));
		titleLabel.setForeground(new Color(0, 60, 150));
		titlePanel.add(titleLabel);
		
		TransparentPane namePanel=new TransparentPane();
		namePanel.add(new JLabel("��     ����"));
		namePanel.add(userNameField);
		
		TransparentPane sexPanel=new TransparentPane();
		sexPanel.add(new JLabel("��     ��"));
		sexPanel.add(new TransparentPane());
		sexPanel.add(maleButton);
		sexPanel.add(femaleButton);
		sexPanel.add(new TransparentPane());
		sexPanel.add(new TransparentPane());
		sexPanel.add(new TransparentPane());
		sexPanel.add(new TransparentPane());
		
		
		TransparentPane birthPanel=new TransparentPane();
		birthPanel.add(new JLabel("�������ڣ�"));
		birthPanel.add(birthDataField);
		
		TransparentPane addrPanel=new TransparentPane();
		addrPanel.add(new JLabel("��ͥסַ��"));
		addrPanel.add(addressField);
		
		
		TransparentPane loginPwdPanel=new TransparentPane();
		loginPwdPanel.add(new JLabel("��¼���룺"));
		loginPwdPanel.add(userPasswordField);
		
		TransparentPane confirmPwdPanel=new TransparentPane();
		confirmPwdPanel.add(new JLabel("ȷ�����룺"));
		confirmPwdPanel.add(confirmPasswordField);
		
		TransparentPane registerPanel=new TransparentPane();
		registerPanel.add(registerButton);
		
		this.setTitle("���ܵ���--�û�ע��");
		this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		contentPane.setLayout(new GridLayout(8,1));
		contentPane.add(titlePanel);
		contentPane.add(namePanel);
		contentPane.add(sexPanel);
		contentPane.add(birthPanel);
		contentPane.add(addrPanel);
		contentPane.add(loginPwdPanel);
		contentPane.add(confirmPwdPanel);
		contentPane.add(registerPanel);
		
		this.setContentPane(contentPane);
		this.setVisible(true);
	}
	
	public void addListener(){
		TextFocusListener tfl=new TextFocusListener();
		birthDataField.addFocusListener(tfl);
		ButtonActionListener bal=new ButtonActionListener();
		registerButton.addActionListener(bal);
		ButtonMouseActionListener bmal=new ButtonMouseActionListener();
		registerButton.addMouseListener(bmal);
	}
	public UserRegister(){
		initCompoent();
		addComponet();
		addListener();
	}
}
