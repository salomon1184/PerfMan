package com.salomon.perfman.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class FileSelectionAdapter implements ActionListener {

	private final Component mainFrameWindow;
	private final JTextField inputTextFiled;

	public FileSelectionAdapter(JFrame mainFrame, JTextField textField) {
		this.mainFrameWindow = mainFrame;
		this.inputTextFiled = textField;
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser("./");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// FileNameExtensionFilter filter = new FileNameExtensionFilter("文本文档",
		// "txt");
		// fileChooser.setFileFilter(filter);

		int returnVal = fileChooser.showOpenDialog(this.mainFrameWindow);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.inputTextFiled.setText(fileChooser.getSelectedFile()
					.getAbsolutePath());
		}

	}

}
