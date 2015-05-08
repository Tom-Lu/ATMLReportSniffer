package com.tomlu.ATMLReportSniffer.GUI;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class UIHandler {
	
	private static JTextArea messageBox;
	private static JProgressBar progressBar;
	
	public static void Init(JTextArea textArea, JProgressBar progressBar) {
		UIHandler.messageBox = textArea;
		UIHandler.progressBar = progressBar;
	}
	
	public static void Cleanup() {
		if(null != messageBox)
			messageBox.setText("");
		if(null != progressBar)
			progressBar.setValue(0);
	}
	
	public static void UpdateProgress(int progressValue) {
		if(null != progressBar) {
			progressBar.setValue(progressValue);
			progressBar.revalidate();
		}
	}
	
	public static void AppendMessage(String message) {
		if(null != messageBox) {
			messageBox.append(message + "\r\n");
			messageBox.setCaretPosition(messageBox.getDocument().getLength());
			messageBox.revalidate();
		}
	}

}
