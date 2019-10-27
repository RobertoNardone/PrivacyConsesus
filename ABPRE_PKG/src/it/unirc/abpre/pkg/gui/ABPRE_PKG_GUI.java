package it.unirc.abpre.pkg.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import it.unirc.abpre.pkg.PKG.PKG;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;


public class ABPRE_PKG_GUI {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ABPRE_PKG_GUI window = new ABPRE_PKG_GUI();
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
	public ABPRE_PKG_GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.MAGENTA);
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		
		JLabel label = new JLabel("Select directory of PK and MSK");
		panel_1.add(label);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		panel_1.add(textField_2);
		
		JButton button = new JButton("Open");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select Folder");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    chooser.showOpenDialog(button);
			    String path= chooser.getSelectedFile().toString(); 
			    textField_2.setText(path);
			}
		});
		panel_1.add(button);
		
		JLabel lblNewLabel = new JLabel("Select file with the list of attributes");
		panel_1.add(lblNewLabel);
		
		textField = new JTextField();
		panel_1.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Open");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select file");
			   
			    chooser.showOpenDialog(btnNewButton);
			    String path= chooser.getSelectedFile().toString(); 
			    textField.setText(path);
			}
		});
		panel_1.add(btnNewButton);
		
		JButton btnNewButton_2 = new JButton("KeyGen");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				PKG pkg= new PKG();
				byte[][]dk=pkg.KeyGen(pkg.readAttributesFromFile(textField.getText()), textField_2.getText());
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Save key");
			    chooser.showSaveDialog(btnNewButton_2);
			    String path= chooser.getSelectedFile().toString(); 
                pkg.saveDkey(dk, path);
				
			}
		});
		panel_1.add(btnNewButton_2);
		
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.CYAN);
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnSetup = new JButton("Setup");
		panel.add(btnSetup);
		btnSetup.setBackground(Color.RED);
		btnSetup.setForeground(Color.BLACK);
		btnSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select Folder");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    chooser.showOpenDialog(btnSetup);
			    String path= chooser.getSelectedFile().toString(); 
			    PKG pkg= new PKG();
			    pkg.Setup();
			    pkg.saveKeys(path);
			    
			    
			}
		});

	}

}
