package it.unirc.abpre.proxy.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import it.unirc.abpre.proxy.PROXY.PROXY;


import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ABPRE_PROXY_GUI {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ABPRE_PROXY_GUI window = new ABPRE_PROXY_GUI();
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
	public ABPRE_PROXY_GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblSelectRekey = new JLabel("Select ReKey");
		lblSelectRekey.setBounds(28, 26, 65, 13);
		frame.getContentPane().add(lblSelectRekey);
		
		textField = new JTextField();
		textField.setBounds(110, 23, 96, 19);
		frame.getContentPane().add(textField);
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
		btnSelect.setBounds(217, 22, 85, 21);
		frame.getContentPane().add(btnSelect);
		
		JLabel label = new JLabel("Select Chipertext");
		label.setBounds(28, 53, 65, 13);
		frame.getContentPane().add(label);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(110, 50, 96, 19);
		frame.getContentPane().add(textField_1);
		
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
		button.setBounds(217, 49, 85, 21);
		frame.getContentPane().add(button);
		
		JButton btnReEncrypt = new JButton("Re encrypt");
		btnReEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PROXY p=new PROXY();
				byte[][]ct=p.readEncryptedFile(textField_1.getText());
				byte[][]rk=p.readReKey(textField.getText());
				byte[][]reencrypted=p.ReEncrypt(rk, ct);
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Encrypt");
			    chooser.showSaveDialog(btnReEncrypt);
			    String path= chooser.getSelectedFile().toString(); 
			    p.saveReEncryptedFile(reencrypted, path);
			}
		});
		btnReEncrypt.setBounds(149, 79, 85, 21);
		frame.getContentPane().add(btnReEncrypt);
	}
}
