package it.unirc.abpre.user.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.SerializationUtils;

import it.unirc.abpre.structures.Attribute;
import it.unirc.abpre.user.USER.USER;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.awt.event.ActionEvent;

public class ABPRE_USER_GUI {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ABPRE_USER_GUI window = new ABPRE_USER_GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ABPRE_USER_GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 340);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 436, 90);
		panel.setPreferredSize(new Dimension(20, 90));
	
	
		panel.setBackground(Color.YELLOW);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblSelectFileTo = new JLabel("Select File To Encrypt");
		lblSelectFileTo.setBounds(22, 14, 99, 13);
		panel.add(lblSelectFileTo);
		
		textField = new JTextField();
		textField.setBounds(126, 11, 96, 19);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select file");
			   
			    chooser.showOpenDialog(btnSelect);
			    String path= chooser.getSelectedFile().toString(); 
			    textField.setText(path);
			}
		});
		btnSelect.setBounds(227, 10, 85, 21);
		panel.add(btnSelect);
		
		JLabel label = new JLabel("Select Public Key ");
		label.setBounds(22, 41, 99, 13);
		panel.add(label);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(126, 38, 96, 19);
		panel.add(textField_1);
		
		JButton button = new JButton("Select");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select file");
			   
			    chooser.showOpenDialog(button);
			    String path= chooser.getSelectedFile().toString(); 
			    textField_1.setText(path);
			}
		});
		button.setBounds(227, 37, 85, 21);
		panel.add(button);
		
		JLabel label_1 = new JLabel("Select Policy ");
		label_1.setBounds(22, 68, 99, 13);
		panel.add(label_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(126, 65, 96, 19);
		panel.add(textField_2);
		
		JButton button_1 = new JButton("Select");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select file");
			   
			    chooser.showOpenDialog(button_1);
			    String path= chooser.getSelectedFile().toString(); 
			    textField_2.setText(path);
			}
		});
		button_1.setBounds(227, 64, 85, 21);
		panel.add(button_1);
		
		JButton btnNewButton = new JButton("Encrypt");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byte[]message=null;
				try {
					message=Files.readAllBytes(Paths.get(textField.getText()));
					
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
				
			USER u=new USER();
		
				
			byte[][] enc=u.encrypt(message, u.readPolicyFromFile(textField_2.getText()), u.readPK(textField_1.getText()));
			JFileChooser chooser = new JFileChooser(); 
		    chooser.setCurrentDirectory(new java.io.File("."));
		    chooser.setDialogTitle("Encrypt");
		    chooser.showSaveDialog(btnNewButton);
		    String path= chooser.getSelectedFile().toString(); 
			
		    u.saveEncryptedFile(enc, path);
			
			
			}
		});
		btnNewButton.setBounds(325, 64, 85, 21);
		panel.add(btnNewButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.CYAN);
		panel_1.setBounds(0, 91, 436, 65);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel label_2 = new JLabel("Select Encrypted File To Decrypt");
		label_2.setBounds(22, 14, 99, 13);
		panel_1.add(label_2);
		
		textField_3 = new JTextField();
		textField_3.setBounds(126, 11, 96, 19);
		textField_3.setColumns(10);
		panel_1.add(textField_3);
		
		JButton button_2 = new JButton("Select");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select file");
			   
			    chooser.showOpenDialog(button_2);
			    String path= chooser.getSelectedFile().toString(); 
			    textField_3.setText(path);
			}
		});
		button_2.setBounds(227, 10, 83, 21);
		panel_1.add(button_2);
		
		JLabel label_3 = new JLabel("Select Secret Key ");
		label_3.setBounds(22, 41, 99, 13);
		panel_1.add(label_3);
		
		textField_4 = new JTextField();
		textField_4.setBounds(126, 38, 96, 19);
		panel_1.add(textField_4);
		textField_4.setColumns(10);
		
		JButton button_3 = new JButton("Select");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select file");
			   
			    chooser.showOpenDialog(button_3);
			    String path= chooser.getSelectedFile().toString(); 
			    textField_4.setText(path);
			}
		});
		button_3.setBounds(227, 37, 83, 21);
		panel_1.add(button_3);
		
		JButton button_4 = new JButton("Decrypt");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				USER u= new USER();
				byte[][] Enc=u.readEncryptedFile(textField_3.getText());
				byte[][] DKey=u.readDkey(textField_4.getText());
			    byte[] m=u.decrypt(Enc, DKey);	
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Decrypt");
			    chooser.showSaveDialog(button_4);
			    String path= chooser.getSelectedFile().toString(); 
			    try {
					Files.write(Paths.get(path), m);
				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_4.setBounds(328, 37, 85, 21);
		panel_1.add(button_4);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setPreferredSize(new Dimension(20, 90));
		panel_2.setBackground(Color.GREEN);
		panel_2.setBounds(0, 151, 436, 90);
		frame.getContentPane().add(panel_2);
		
		JLabel label_4 = new JLabel("Select Secret key");
		label_4.setBounds(22, 14, 99, 13);
		panel_2.add(label_4);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(126, 11, 96, 19);
		panel_2.add(textField_5);
		
		JButton button_5 = new JButton("Select");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select file");
			   
			    chooser.showOpenDialog(button_5);
			    String path= chooser.getSelectedFile().toString(); 
			    textField_5.setText(path);
			  

			    
			    
			    
			}
		});
		button_5.setBounds(227, 10, 76, 21);
		panel_2.add(button_5);
		
		JLabel label_5 = new JLabel("Select Public Key ");
		label_5.setBounds(22, 41, 99, 13);
		panel_2.add(label_5);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(126, 38, 96, 19);
		panel_2.add(textField_6);
		
		JButton button_6 = new JButton("Select");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select file");
			   
			    chooser.showOpenDialog(button_6);
			    String path= chooser.getSelectedFile().toString(); 
			    textField_6.setText(path);
			}
		});
		button_6.setBounds(227, 37, 76, 21);
		panel_2.add(button_6);
		
		JLabel label_6 = new JLabel("Select new Policy ");
		label_6.setBounds(22, 68, 99, 13);
		panel_2.add(label_6);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(126, 65, 96, 19);
		panel_2.add(textField_7);
		
		JButton button_7 = new JButton("Select");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select file");
			   
			    chooser.showOpenDialog(button_7);
			    String path= chooser.getSelectedFile().toString(); 
			    textField_7.setText(path);
			}
		});
		button_7.setBounds(227, 64, 76, 21);
		panel_2.add(button_7);
		
		JButton button_8 = new JButton("ReKeyGen");
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				USER u=new USER();
				byte[][]rekey=u.RKGen(u.readDkey(textField_5.getText()), u.readPolicyFromFile(textField_7.getText()), u.readPK(textField_6.getText()));
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select file");
			    chooser.showSaveDialog(button_8);
			    String path= chooser.getSelectedFile().toString(); 			
				u.saveReKey(rekey, path);
						
			}
		});
		button_8.setBounds(330, 64, 85, 21);
		panel_2.add(button_8);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBackground(Color.RED);
		panel_3.setBounds(0, 241, 436, 65);
		frame.getContentPane().add(panel_3);
		
		JLabel label_7 = new JLabel("Select Re-Encrypted File To Decrypt");
		label_7.setBounds(22, 14, 99, 13);
		panel_3.add(label_7);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(126, 11, 96, 19);
		panel_3.add(textField_8);
		
		JButton button_9 = new JButton("Select");
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select file");
			   
			    chooser.showOpenDialog(button_9);
			    String path= chooser.getSelectedFile().toString(); 
			    textField_8.setText(path);
			    
	
			
			    
			    
			    
			    
			}
		});
		button_9.setBounds(227, 10, 76, 21);
		panel_3.add(button_9);
		
		JLabel label_8 = new JLabel("Select Secret Key ");
		label_8.setBounds(22, 41, 99, 13);
		panel_3.add(label_8);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(126, 38, 96, 19);
		panel_3.add(textField_9);
		
		JButton button_10 = new JButton("Select");
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select file");
			   
			    chooser.showOpenDialog(button_10);
			    String path= chooser.getSelectedFile().toString(); 
			    textField_9.setText(path);
			    
			}
		});
		button_10.setBounds(227, 37, 76, 21);
		panel_3.add(button_10);
		
		JButton button_11 = new JButton("Decrypt");
		button_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				USER u= new USER();
				byte[][] Enc=u.readRE_EncryptedFile(textField_8.getText());
				byte[][] DKey=u.readDkey(textField_9.getText());
			    byte[] m=u.decryptRC(Enc, DKey);	
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Decrypt");
			    chooser.showSaveDialog(button_11);
			    String path= chooser.getSelectedFile().toString(); 
			    try {
					Files.write(Paths.get(path), m);
				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_11.setBounds(328, 37, 85, 21);
		panel_3.add(button_11);
	}
}
