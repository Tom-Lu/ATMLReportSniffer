package com.tomlu.ATMLReportSniffer.ATML;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.tomlu.ATMLReportSniffer.GUI.UIHandler;

public class ExportHandler {
	
	private static boolean includePassResult = true;
	private static boolean includeFailResult = true;	

	public static boolean isIncludePassResult() {
		return includePassResult;
	}

	public static void setIncludePassResult(boolean includePassResult) {
		ExportHandler.includePassResult = includePassResult;
	}

	public static boolean isIncludeFailResult() {
		return includeFailResult;
	}

	public static void setIncludeFailResult(boolean includeFailResult) {
		ExportHandler.includeFailResult = includeFailResult;
	}

	private File ExportFile;
	private List<TestResult> ResultList;
	private List<Step> ExportStepList;
	private Workbook workbook;
	private Sheet sheet;
	private int totalResult = 0;
	private int passResult = 0;
	private int failResult = 0;
	
	private int getTotalResult() {
		return totalResult;
	}

	private void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}

	private int getPassResult() {
		return passResult;
	}

	private void setPassResult(int passResult) {
		this.passResult = passResult;
	}

	private int getFailResult() {
		return failResult;
	}

	private void setFailResult(int failResult) {
		this.failResult = failResult;
	}

	private ExportHandler(File exportFile, List<TestResult> resultList, List<Step> exportStepList) {
		ExportFile = exportFile;
		ResultList = resultList;
		ExportStepList = exportStepList;
	}
	
	private void InitExcelFile() {
		workbook = new SXSSFWorkbook();
		sheet =  workbook.createSheet("TestData");
	}
	
	private void SaveExcelFile() {
	    FileOutputStream fileOut;
		try {
			
			fileOut = new FileOutputStream(ExportFile);
			workbook.write(fileOut);
		    fileOut.close();
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void ExportHeader() {
	    Row row = sheet.createRow(0);
	    row.createCell(0).setCellValue("SerialNumber");
	    row.createCell(1).setCellValue("StartTime");
	    row.createCell(2).setCellValue("Result");
	    
	    for(int i=3; i<ExportStepList.size()+3; i++) {
	    	row.createCell(i).setCellValue(ExportStepList.get(i-3).getName());
	    }		
	}
	
	private void ExportSingleResult(TestResult result, int rowIndex) {
		
	    Row row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue(result.getUut().getSerialNumber());
	    row.createCell(1).setCellValue(result.getStartTime());
	    row.createCell(2).setCellValue(result.getResult());
	    
		List<Step> stepList = result.getSteps();		
		for(int i=3; i<ExportStepList.size()+3; i++) {
			
			Step exportStep = ExportStepList.get(i-3);
			for(Step step : stepList) {			
				if(step.equals(exportStep)) {			    	
			    	if(step.getStepType().equals("NumericLimitTest") || step.getStepType().equals("NI_MultipleNumericLimitTest"))
			    	{
				    	row.createCell(i).setCellValue(Double.parseDouble(step.getMeasureValue()));			    		
			    	}
			    	else
			    	{
				    	row.createCell(i).setCellValue(step.getMeasureValue());			    		
			    	}
				}				
			}
		}
	}
	
	private void ExportResultList() {
		
		int rowIndex = 1;
		for(int i=0; i< ResultList.size(); i++) {
			TestResult result = ResultList.get(i);
			
			if(result.getResult().equals("Passed")) {
				setPassResult(getPassResult()+1);
			}
			if(result.getResult().equals("Failed")) {
				setFailResult(getFailResult()+1);
			}
			
			if((result.getResult().equals("Passed") && isIncludePassResult()) ||
					(result.getResult().equals("Failed") && isIncludeFailResult())) {				
				ExportSingleResult(result, rowIndex++);				
			}
			
		}
		
	}
	
	
	public static void Export(File exportFile, List<TestResult> resultList, List<Step> exportStepList) {
				
		ExportHandler handler = new ExportHandler(exportFile, resultList, exportStepList);
		
		handler.setTotalResult(resultList.size());
		handler.setPassResult(0);
		handler.setFailResult(0);
		
		handler.InitExcelFile();
		handler.ExportHeader();
		handler.ExportResultList();	
		handler.SaveExcelFile();
		
		UIHandler.AppendMessage("###########################################################################################################");
		UIHandler.AppendMessage(" -- ");
		UIHandler.AppendMessage(" -- Total: " + handler.getTotalResult());
		UIHandler.AppendMessage(" -- ");
		UIHandler.AppendMessage(" -- Pass Result: " + handler.getPassResult() + (isIncludePassResult() ? "" : " ( Skipped )") );
		UIHandler.AppendMessage(" -- ");
		UIHandler.AppendMessage(" -- Fail Result: " + handler.getFailResult() + (isIncludeFailResult() ? "" : " ( Skipped )") );
		UIHandler.AppendMessage(" -- ");
		UIHandler.AppendMessage("###########################################################################################################");
		UIHandler.AppendMessage(" -- ");
		UIHandler.AppendMessage(" -- Export Task Done -- :)");
		UIHandler.AppendMessage(" -- ");
		UIHandler.AppendMessage("###########################################################################################################");
	}

}
