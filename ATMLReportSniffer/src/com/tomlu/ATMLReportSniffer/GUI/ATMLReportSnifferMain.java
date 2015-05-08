package com.tomlu.ATMLReportSniffer.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import java.awt.GridBagLayout;

import javax.swing.JSplitPane;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;

import javax.swing.JCheckBox;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.dom4j.DocumentException;

import java.awt.Font;

import com.tomlu.ATMLReportSniffer.ATML.ExportHandler;
import com.tomlu.ATMLReportSniffer.ATML.ReportParser;
import com.tomlu.ATMLReportSniffer.ATML.Step;
import com.tomlu.ATMLReportSniffer.ATML.TestResult;
import com.tomlu.ATMLReportSniffer.ATML.UUT;

/**
 * @author Tom Lu
 *
 */

public class ATMLReportSnifferMain extends JFrame {
	
	private JList stepFilterList;
	private JLabel lblReportSampleFile;
	private JCheckBox chckbxActionStep;
	private JCheckBox chckbxPassfailTestStep;
	private JCheckBox chckbxNumericTestStep;
	private JCheckBox chckbxMultipleNumericLimit;
	private JCheckBox chckbxStringValueTest;
	private JCheckBox chckbxPassResult;
	private JCheckBox chckbxFailResult;
	private TestResult sampleTestResult = null;
	private List<CheckListItem> currentCheckListItems;
	private String SampleReport = "";
	private String ReportFolder = "";
	private String ExportFile = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ATMLReportSnifferMain frame = new ATMLReportSnifferMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ATMLReportSnifferMain() {
		setTitle(Messages.getString("ATMLReportSnifferMain.Title")); //$NON-NLS-1$
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JButton btnImport = new JButton(Messages.getString("ATMLReportSnifferMain.btnImport.text")); //$NON-NLS-1$

		GridBagConstraints gbc_btnImport = new GridBagConstraints();
		gbc_btnImport.insets = new Insets(5, 5, 5, 5);
		gbc_btnImport.gridx = 0;
		gbc_btnImport.gridy = 0;
		getContentPane().add(btnImport, gbc_btnImport);
		
		JButton btnExport = new JButton(Messages.getString("ATMLReportSnifferMain.btnExport.text")); //$NON-NLS-1$
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.insets = new Insets(5, 5, 5, 5);
		gbc_btnExport.gridx = 1;
		gbc_btnExport.gridy = 0;
		getContentPane().add(btnExport, gbc_btnExport);
		
		JButton btnAbout = new JButton(Messages.getString("ATMLReportSnifferMain.btnAbout.text")); //$NON-NLS-1$
		GridBagConstraints gbc_btnAbout = new GridBagConstraints();
		gbc_btnAbout.insets = new Insets(5, 5, 5, 5);
		gbc_btnAbout.gridx = 2;
		gbc_btnAbout.gridy = 0;
		getContentPane().add(btnAbout, gbc_btnAbout);
		
		lblReportSampleFile = new JLabel(Messages.getString("ATMLReportSnifferMain.lblNewLabel.text")); //$NON-NLS-1$
		GridBagConstraints gbc_lblReportSampleFile = new GridBagConstraints();
		gbc_lblReportSampleFile.anchor = GridBagConstraints.WEST;
		gbc_lblReportSampleFile.insets = new Insets(0, 0, 5, 0);
		gbc_lblReportSampleFile.gridx = 3;
		gbc_lblReportSampleFile.gridy = 0;
		getContentPane().add(lblReportSampleFile, gbc_lblReportSampleFile);
		
		JSplitPane splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridwidth = 4;
		gbc_splitPane.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
		
		getContentPane().add(splitPane, gbc_splitPane);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		panel.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JLabel lblExportToFile = new JLabel(Messages.getString("ATMLReportSnifferMain.lblExportToFile.text")); //$NON-NLS-1$
		lblExportToFile.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblExportToFile = new GridBagConstraints();
		gbc_lblExportToFile.anchor = GridBagConstraints.WEST;
		gbc_lblExportToFile.insets = new Insets(0, 10, 5, 5);
		gbc_lblExportToFile.gridx = 0;
		gbc_lblExportToFile.gridy = 1;
		panel_2.add(lblExportToFile, gbc_lblExportToFile);
		
		final JTextField textFieldExportFileName = new JTextField();
		textFieldExportFileName.setText(Messages.getString("ATMLReportSnifferMain.textField.text")); //$NON-NLS-1$
		GridBagConstraints gbc_textFieldExportFileName = new GridBagConstraints();
		gbc_textFieldExportFileName.insets = new Insets(0, 5, 5, 5);
		gbc_textFieldExportFileName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldExportFileName.gridx = 0;
		gbc_textFieldExportFileName.gridy = 2;
		panel_2.add(textFieldExportFileName, gbc_textFieldExportFileName);
		textFieldExportFileName.setColumns(10);
		
		JButton buttonBroswe = new JButton(Messages.getString("ATMLReportSnifferMain.button.text")); //$NON-NLS-1$
		GridBagConstraints gbc_buttonBroswe = new GridBagConstraints();
		gbc_buttonBroswe.insets = new Insets(0, 0, 5, 5);
		gbc_buttonBroswe.gridx = 1;
		gbc_buttonBroswe.gridy = 2;
		panel_2.add(buttonBroswe, gbc_buttonBroswe);
		
		JLabel lblExportOptions = new JLabel(Messages.getString("ATMLReportSnifferMain.lblExportOptions.text")); //$NON-NLS-1$
		lblExportOptions.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblExportOptions = new GridBagConstraints();
		gbc_lblExportOptions.anchor = GridBagConstraints.WEST;
		gbc_lblExportOptions.insets = new Insets(0, 10, 5, 5);
		gbc_lblExportOptions.gridx = 0;
		gbc_lblExportOptions.gridy = 3;
		panel_2.add(lblExportOptions, gbc_lblExportOptions);
		
		chckbxPassResult = new JCheckBox(Messages.getString("ATMLReportSnifferMain.chckbxPassResult.text")); //$NON-NLS-1$
		chckbxPassResult.setSelected(true);
		GridBagConstraints gbc_chckbxPassResult = new GridBagConstraints();
		gbc_chckbxPassResult.anchor = GridBagConstraints.WEST;
		gbc_chckbxPassResult.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxPassResult.gridx = 0;
		gbc_chckbxPassResult.gridy = 4;
		panel_2.add(chckbxPassResult, gbc_chckbxPassResult);
		
		chckbxFailResult = new JCheckBox(Messages.getString("ATMLReportSnifferMain.chckbxFailResult.text")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxFailResult = new GridBagConstraints();
		gbc_chckbxFailResult.anchor = GridBagConstraints.WEST;
		gbc_chckbxFailResult.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxFailResult.gridx = 0;
		gbc_chckbxFailResult.gridy = 5;
		panel_2.add(chckbxFailResult, gbc_chckbxFailResult);
		
		JTextArea textArea = new JTextArea();
		textArea.setText(Messages.getString("ATMLReportSnifferMain.textArea.text")); //$NON-NLS-1$
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 1;
		panel.add(new JScrollPane(textArea), gbc_textArea);
		
		JPanel panel_1 = new JPanel();
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_1.add(panel_3, "cell 0 0,grow");
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JLabel lblFilteSteps = new JLabel(Messages.getString("ATMLReportSnifferMain.lblFilteSteps.text"));
		lblFilteSteps.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblFilteSteps = new GridBagConstraints();
		gbc_lblFilteSteps.anchor = GridBagConstraints.WEST;
		gbc_lblFilteSteps.insets = new Insets(0, 10, 5, 0);
		gbc_lblFilteSteps.gridx = 0;
		gbc_lblFilteSteps.gridy = 0;
		panel_3.add(lblFilteSteps, gbc_lblFilteSteps);
		
		chckbxActionStep = new JCheckBox(Messages.getString("ATMLReportSnifferMain.chckbxActionStep.text")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxActionStep = new GridBagConstraints();
		gbc_chckbxActionStep.anchor = GridBagConstraints.WEST;
		gbc_chckbxActionStep.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxActionStep.gridx = 0;
		gbc_chckbxActionStep.gridy = 1;
		panel_3.add(chckbxActionStep, gbc_chckbxActionStep);
		chckbxActionStep.setSelected(true);
		
		chckbxPassfailTestStep = new JCheckBox(Messages.getString("ATMLReportSnifferMain.chckbxPassfailTestStep.text")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxPassfailTestStep = new GridBagConstraints();
		gbc_chckbxPassfailTestStep.anchor = GridBagConstraints.WEST;
		gbc_chckbxPassfailTestStep.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxPassfailTestStep.gridx = 0;
		gbc_chckbxPassfailTestStep.gridy = 2;
		panel_3.add(chckbxPassfailTestStep, gbc_chckbxPassfailTestStep);
		chckbxPassfailTestStep.setSelected(true);
		
		chckbxNumericTestStep = new JCheckBox(Messages.getString("ATMLReportSnifferMain.chckbxNumericTestStep.text")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxNumericTestStep = new GridBagConstraints();
		gbc_chckbxNumericTestStep.anchor = GridBagConstraints.WEST;
		gbc_chckbxNumericTestStep.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxNumericTestStep.gridx = 0;
		gbc_chckbxNumericTestStep.gridy = 3;
		panel_3.add(chckbxNumericTestStep, gbc_chckbxNumericTestStep);
		chckbxNumericTestStep.setSelected(true);
		
		chckbxMultipleNumericLimit = new JCheckBox(Messages.getString("ATMLReportSnifferMain.chckbxMultipleNumericLimit.text")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxMultipleNumericLimit = new GridBagConstraints();
		gbc_chckbxMultipleNumericLimit.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxMultipleNumericLimit.gridx = 0;
		gbc_chckbxMultipleNumericLimit.gridy = 4;
		panel_3.add(chckbxMultipleNumericLimit, gbc_chckbxMultipleNumericLimit);
		chckbxMultipleNumericLimit.setSelected(true);
		
		chckbxStringValueTest = new JCheckBox(Messages.getString("ATMLReportSnifferMain.chckbxStringValueTest.text")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxStringValueTest = new GridBagConstraints();
		gbc_chckbxStringValueTest.anchor = GridBagConstraints.WEST;
		gbc_chckbxStringValueTest.gridx = 0;
		gbc_chckbxStringValueTest.gridy = 5;
		panel_3.add(chckbxStringValueTest, gbc_chckbxStringValueTest);
		chckbxStringValueTest.setSelected(true);
		chckbxStringValueTest.addItemListener(StepFilterOptionChangeListener);
		chckbxMultipleNumericLimit.addItemListener(StepFilterOptionChangeListener);
		chckbxNumericTestStep.addItemListener(StepFilterOptionChangeListener);
		chckbxPassfailTestStep.addItemListener(StepFilterOptionChangeListener);
		chckbxActionStep.addItemListener(StepFilterOptionChangeListener);
		
		stepFilterList = new JList();
		panel_1.add(new JScrollPane(stepFilterList), "cell 0 1,grow");
		
		splitPane.setDividerLocation(300);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setValue(50);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridwidth = 4;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 2;
		getContentPane().add(progressBar, gbc_progressBar);
		
		stepFilterList.setCellRenderer(new CheckListRenderer());
		stepFilterList.setSelectionMode(
	         ListSelectionModel.SINGLE_SELECTION);
		
		stepFilterList.addMouseListener(new MouseAdapter()
	      {
	         public void mouseClicked(MouseEvent event)
	         {
	            JList list = (JList) event.getSource();
	            
	            // Get index of item clicked
	            
	            int index = list.locationToIndex(event.getPoint());
	            CheckListItem item = (CheckListItem)
	               list.getModel().getElementAt(index);
	            
	            // Toggle selected state
	            
	            item.setSelected(! item.isSelected());
	            
	            // Repaint cell
	            
	            list.repaint(list.getCellBounds(index, index));
	         }
	      }); 
		
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Create a file chooser
				final JFileChooser fc = new JFileChooser("D:\\Projects\\WAC-4P\\Test_Solution\\Release\\20150429_WAC-140\\PCBA_TestReportsXML");
				fc.addChoosableFileFilter(new XmlFileFilter());
				int returnVal = fc.showOpenDialog(getContentPane());
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					
					
					SampleReport = fc.getSelectedFile().getAbsolutePath();
					ReportFolder = fc.getSelectedFile().getParent();
					lblReportSampleFile.setText(SampleReport);
					
					UpdateStepList();
					
				}				
			}
		});
		
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				
				if(null != ReportFolder && !ReportFolder.equals("") &&
						null != ExportFile && !ExportFile.equals("")) {	
					
					UIHandler.Cleanup();
					UIHandler.AppendMessage("###########################################################################################################");
					UIHandler.AppendMessage(" -- ");
					UIHandler.AppendMessage(" -- Export Folder: " + ReportFolder);
					UIHandler.AppendMessage(" -- ");
					UIHandler.AppendMessage(" -- Output File: " + ExportFile);
					UIHandler.AppendMessage(" -- ");
					UIHandler.AppendMessage("###########################################################################################################");
					
					new Thread() {
						@Override
						public void run() {
							List<Step> currentStepList = sampleTestResult.getSteps();
							List results = ReportParser.ParseFolder(new File(ReportFolder));	
							List<Step> currentExportStepList = new ArrayList<Step>();
							
							for(int i=0; i<currentStepList.size(); i++) {						
								if(currentCheckListItems.get(i).isSelected()) {
									currentExportStepList.add(currentStepList.get(i));
								}
							}
							ExportHandler.setIncludePassResult(chckbxPassResult.isSelected());
							ExportHandler.setIncludeFailResult(chckbxFailResult.isSelected());
							ExportHandler.Export(new File(ExportFile), results, currentExportStepList);
						}
					}.start();
				} else {
					UIHandler.Cleanup();
					UIHandler.AppendMessage("###########################################################################################################");
					UIHandler.AppendMessage(" -- ");
					UIHandler.AppendMessage(" -- Sample Report or Export File cannot be empty!!! ");
					UIHandler.AppendMessage(" -- ");
					UIHandler.AppendMessage("###########################################################################################################");
				}
				
			}
		});
		
		buttonBroswe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fchooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Report(*.xlsx)", "xlsx");
			    fchooser.setFileFilter(filter);
			    fchooser.setDialogTitle("Export to File:");
			    int returnVal = fchooser.showSaveDialog(ATMLReportSnifferMain.this);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {

			    	String FileExtension = Utils.getExtension(fchooser.getSelectedFile());
			    	if(null != FileExtension && FileExtension.equals("xlsx")) {
			    		ExportFile = fchooser.getSelectedFile().getAbsolutePath();
			    	} else {
				    	ExportFile = fchooser.getSelectedFile().getAbsolutePath() + ".xlsx";
			    	}  	
			    	
			    	textFieldExportFileName.setText(ExportFile);
			    	
			    }			
			}
		});
		
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					About dialog = new About(ATMLReportSnifferMain.this);
					dialog.setAlwaysOnTop(true);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		lblReportSampleFile.setText("");
		textFieldExportFileName.setText("");
		StepFilterOptionChangeListener.itemStateChanged(null);

		UIHandler.Init(textArea, progressBar);
		UIHandler.Cleanup();
		
	}
	
	private ItemListener StepFilterOptionChangeListener = new ItemListener() {
		public void itemStateChanged(ItemEvent arg0) {
			
			if(chckbxActionStep != null)
				ReportParser.setIncludeActionStep(chckbxActionStep.isSelected());
			if(chckbxPassfailTestStep != null)
				ReportParser.setIncludePassFailTest(chckbxPassfailTestStep.isSelected());
			if(chckbxNumericTestStep != null)
				ReportParser.setIncludeNumericLimitTest(chckbxNumericTestStep.isSelected());
			if(chckbxMultipleNumericLimit != null)
				ReportParser.setIncludeMultipleNumericLimitTest(chckbxMultipleNumericLimit.isSelected());
			if(chckbxStringValueTest != null)
				ReportParser.setIncludeStringValueTest(chckbxStringValueTest.isSelected());
			
			if(!SampleReport.equals("")) {
				UpdateStepList();
			}
		}
	};
	
	private void UpdateStepList() {
		
		UIHandler.Cleanup();
		UIHandler.AppendMessage("###########################################################################################################");
		UIHandler.AppendMessage("Imported Sample Report: " + SampleReport);
		
	
		ATMLReportSnifferMain.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		sampleTestResult = null;
		
		try {
			sampleTestResult = ReportParser.Parse(new File(SampleReport));
			UUT uut = sampleTestResult.getUut();
			List<Step> currentStepList = sampleTestResult.getSteps();
			currentCheckListItems = new ArrayList<CheckListItem>();
			
			for(Step step : currentStepList) {
				currentCheckListItems.add(new CheckListItem(step.getName()));
			}		
			stepFilterList.setListData(currentCheckListItems.toArray());
			
			UIHandler.AppendMessage("###########################################################################################################");
			UIHandler.AppendMessage(" -- ");
			UIHandler.AppendMessage(" -- Process: " + sampleTestResult.getUut().getProcess() + 
					"    Station: " + sampleTestResult.getUut().getStation() + 
					"    Operator: " + sampleTestResult.getUut().getOperator());
			UIHandler.AppendMessage(" -- ");
			UIHandler.AppendMessage(" -- SerialNumber: " + sampleTestResult.getUut().getSerialNumber() + 
					"    StartTime: " + sampleTestResult.getStartTime() + 
					"    Result: " + sampleTestResult.getResult());
			UIHandler.AppendMessage(" -- ");
			UIHandler.AppendMessage("###########################################################################################################");
			UIHandler.AppendMessage(" -- ");
			UIHandler.AppendMessage(" -- Total Step: " + sampleTestResult.getSteps().size());
			UIHandler.AppendMessage(" -- ");
			UIHandler.AppendMessage("###########################################################################################################");
			
			
		
		} catch (DocumentException e) {
			UIHandler.AppendMessage("###########################################################################################################");
			UIHandler.AppendMessage(" -- ");
			UIHandler.AppendMessage(" -- Error: " + e.getMessage());
			UIHandler.AppendMessage(" -- ");
			UIHandler.AppendMessage("###########################################################################################################");
			e.printStackTrace();
		}
		
		

		
		ATMLReportSnifferMain.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	// Represents items in the list that can be selected

	class CheckListItem
	{
	   private String  label;
	   private boolean isSelected = false;

	   public CheckListItem(String label)
	   {
	      this.label = label;
	      this.isSelected = true;
	   }

	   public boolean isSelected()
	   {
	      return isSelected;
	   }

	   public void setSelected(boolean isSelected)
	   {
	      this.isSelected = isSelected;
	   }

	   public String toString()
	   {
	      return label;
	   }
	}
	
	// Handles rendering cells in the list using a check box

	class CheckListRenderer extends JCheckBox
	   implements ListCellRenderer
	{
	   public Component getListCellRendererComponent(
	         JList list, Object value, int index, 
	         boolean isSelected, boolean hasFocus)
	   {
	      setEnabled(list.isEnabled());
	      setSelected(((CheckListItem)value).isSelected());
	      setFont(list.getFont());
	      setBackground(list.getBackground());
	      setForeground(list.getForeground());
	      setText(value.toString());
	      return this;
	   }
	} 

}
