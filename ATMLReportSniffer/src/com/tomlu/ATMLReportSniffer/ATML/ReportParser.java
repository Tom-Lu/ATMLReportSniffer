package com.tomlu.ATMLReportSniffer.ATML;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.tomlu.ATMLReportSniffer.ATML.Step;
import com.tomlu.ATMLReportSniffer.ATML.TestResult;
import com.tomlu.ATMLReportSniffer.ATML.UUT;
import com.tomlu.ATMLReportSniffer.GUI.UIHandler;
import com.tomlu.ATMLReportSniffer.GUI.Utils;

public class ReportParser {
	
	private static boolean includeActionStep;
	private static boolean includePassFailTest;
	private static boolean includeNumericLimitTest;
	private static boolean includeMultipleNumericLimitTest;
	private static boolean includeStringValueTest;

	public static boolean isIncludeActionStep() {
		return includeActionStep;
	}

	public static void setIncludeActionStep(boolean includeActionStep) {
		ReportParser.includeActionStep = includeActionStep;
	}

	public static boolean isIncludePassFailTest() {
		return includePassFailTest;
	}

	public static void setIncludePassFailTest(boolean includePassFailTest) {
		ReportParser.includePassFailTest = includePassFailTest;
	}

	public static boolean isIncludeNumericLimitTest() {
		return includeNumericLimitTest;
	}

	public static void setIncludeNumericLimitTest(boolean includeNumericLimitTest) {
		ReportParser.includeNumericLimitTest = includeNumericLimitTest;
	}

	public static boolean isIncludeMultipleNumericLimitTest() {
		return includeMultipleNumericLimitTest;
	}

	public static void setIncludeMultipleNumericLimitTest(
			boolean includeMultipleNumericLimitTest) {
		ReportParser.includeMultipleNumericLimitTest = includeMultipleNumericLimitTest;
	}

	public static boolean isIncludeStringValueTest() {
		return includeStringValueTest;
	}

	public static void setIncludeStringValueTest(boolean includeStringValueTest) {
		ReportParser.includeStringValueTest = includeStringValueTest;
	}

	public static TestResult Parse(File ReportFile) throws DocumentException {

		
		Map<String, String>map=new HashMap<String, String>();  
		map.put("trc","urn:IEEE-1636.1:2011:01:TestResultsCollection");
		map.put("tr","urn:IEEE-1636.1:2011:01:TestResults");
		map.put("c","urn:IEEE-1671:2010:Common");
		map.put("xsi","http://www.w3.org/2001/XMLSchema-instance");
		map.put("ts","www.ni.com/TestStand/ATMLTestResults/2.0");
		
		
		TestResult result = new TestResult();

		SAXReader saxReader = new SAXReader();
		saxReader.getDocumentFactory().setXPathNamespaceURIs(map); 
		
		Document document = saxReader.read(ReportFile);
		Node TestResultsNode = document.selectSingleNode("//trc:TestResults");

		result.setUut(ParseUUT(TestResultsNode));

		Node ResultSetNode = TestResultsNode.selectSingleNode("tr:ResultSet");
		result.setStartTime(GetNodeAttValue(ResultSetNode,null, "startDateTime").trim());
		result.setStopTime(GetNodeAttValue(ResultSetNode,null, "endDateTime").trim());
		result.setTotalTestTime(Float.parseFloat(GetNodeAttValue(ResultSetNode,"tr:Extension/ts:TSStepProperties/ts:TotalTime", "value").trim()));
		result.setResult(GetNodeAttValue(ResultSetNode,"tr:Outcome", "value").trim());
		
		List<Node> StepNodes = (List<Node>)ResultSetNode.selectNodes("//tr:Test");
		List<Step> steps = new ArrayList<Step>();
		for (Node stepNode : StepNodes) {

			List<Step> parsedSteps = ParseSteps(stepNode);
			
			for(Step step : parsedSteps) {
				
				if ((step.getStepType().equals("Action") && isIncludeActionStep())
						|| (step.getStepType().equals("PassFailTest") && isIncludePassFailTest())
						|| (step.getStepType().equals("NumericLimitTest") && isIncludeNumericLimitTest())
						|| (step.getStepType().equals("NI_MultipleNumericLimitTest") && isIncludeMultipleNumericLimitTest())
						|| (step.getStepType().equals("StringValueTest") && isIncludeStringValueTest())) {

					step.setIndex(steps.size());
					steps.add(step);
				}		
			}


		}

		result.setSteps(steps);
		return result;
	}
	
	public static List<TestResult> ParseFolder(File ReportFolder) {
		List<TestResult> ResultList = new ArrayList<TestResult>();
		
		if(ReportFolder.exists() && ReportFolder.isDirectory()) {
			File[] ReportFiles = ReportFolder.listFiles(new FileFilter(){
				@Override
				public boolean accept(File f) {
			        if (f.isDirectory()) {
			            return false;
			        }
			        String extension = Utils.getExtension(f);
			        if (extension != null) {
			            if (extension.equals("xml")) {
			                    return true;
			            } else {
			                return false;
			            }
			        }
			        return false;
				}
				
			});
			
			UIHandler.UpdateProgress(0);
			for(int i=0; i<ReportFiles.length; i++) {
				UIHandler.AppendMessage("Parsing: " + ReportFiles[i]);
				TestResult result = null;
				

				try {
					result = Parse(ReportFiles[i]);
				} catch (DocumentException e) {
					UIHandler.AppendMessage("###########################################################################################################");
					UIHandler.AppendMessage(" -- ");
					UIHandler.AppendMessage(" -- Error: " + e.getMessage());
					UIHandler.AppendMessage(" -- ");
					UIHandler.AppendMessage("###########################################################################################################");
					e.printStackTrace();
				}
				
				if(null != result) {
					ResultList.add(result);					
				}	
				
				UIHandler.UpdateProgress((i*100)/ReportFiles.length);
			}
			UIHandler.UpdateProgress(100);
		}
		
		return ResultList;
	}
	
	private static UUT ParseUUT(Node UutNode) {
		UUT uut = new UUT();
		
		uut.setSerialNumber(GetNodeValue(UutNode, "tr:UUT/c:SerialNumber").trim());
		uut.setStation(GetNodeValue(UutNode, "tr:TestStation/c:SerialNumber").trim());
		uut.setOperator(GetNodeAttValue(UutNode, "tr:Personnel/tr:SystemOperator", "name").trim());
		uut.setProcess(GetNodeValue(UutNode, "tr:TestProgram/c:SerialNumber").trim());
		
		return uut;
	}
	
	private static List<Step> ParseSteps(Node StepNode) {
		List<Step> steps = new ArrayList<Step>();
		
		String stepType = GetNodeValue(StepNode, "tr:Extension/ts:TSStepProperties/ts:StepType");
		
		// We want each result data in multiple numeric limit test, so treat each result data as separate step
		if(stepType.equals("NI_MultipleNumericLimitTest")) {
			
			// Get All Result Data Nodes
			List<Node> TestResultNodes = StepNode.selectNodes("tr:TestResult");
			
			for(Node ResultNode : TestResultNodes) {
				
				Step step = new Step();
				
				// [Test Step Name] + " @ " + [Result Name]
				step.setName(GetNodeAttValue(StepNode, "", "name") + " @ " + GetNodeAttValue(ResultNode, "", "name"));
				step.setStepId(GetNodeAttValue(StepNode, "", "testReferenceID"));
				step.setStepType(GetNodeValue(StepNode, "tr:Extension/ts:TSStepProperties/ts:StepType"));		
				step.setResult(GetNodeAttValue(ResultNode, "tr:Outcome", "value"));
				step.setTotalTestTime(Float.parseFloat(GetNodeAttValue(StepNode, "tr:Extension/ts:TSStepProperties/ts:TotalTime", "value")));			
				step.setMeasureValue(GetNodeAttValue(ResultNode, "tr:TestData/c:Datum", "value"));
				
				steps.add(step);
			}			
		}
		else
		{
			Step step = new Step();
			
			step.setName(GetNodeAttValue(StepNode, "", "name"));
			step.setStepId(GetNodeAttValue(StepNode, "", "testReferenceID"));
			step.setStepType(GetNodeValue(StepNode, "tr:Extension/ts:TSStepProperties/ts:StepType"));		
			step.setResult(GetNodeAttValue(StepNode, "tr:Outcome", "value"));
			step.setTotalTestTime(Float.parseFloat(GetNodeAttValue(StepNode, "tr:Extension/ts:TSStepProperties/ts:TotalTime", "value")));
			
			if(step.getStepType().equals("NumericLimitTest")) {
				step.setMeasureValue(GetNodeAttValue(StepNode, "tr:TestResult/tr:TestData/c:Datum", "value"));
			} else if(step.getStepType().equals("StringValueTest")) {
				step.setMeasureValue(GetNodeValue(StepNode, "tr:TestResult/tr:TestData/c:Datum/c:Value"));
			}
			
			steps.add(step);
		}
		return steps;
	}
	
	private static String GetNodeValue(Node RootNode, String NodePath)
	{
		String Value = null;
		
		Node ValueNode = RootNode.selectSingleNode(NodePath);
		
		if(ValueNode != null)
		{
			Value = ValueNode.getText();		
		}
		
		return Value;
	}
	
	private static String GetNodeAttValue(Node RootNode, String NodePath, String Attribute)
	{
		String Value = null;
		
		if(NodePath == null || NodePath.isEmpty())
		{
			Value = ((Element) RootNode).attributeValue(Attribute);		
		}
		else
		{
			Node ValueNode = RootNode.selectSingleNode(NodePath);		
			if(ValueNode != null)
			{
				Value = ((Element) ValueNode).attributeValue(Attribute);		
			}
		}
		return Value;
	}
}
