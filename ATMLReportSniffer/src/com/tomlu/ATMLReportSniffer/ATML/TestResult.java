package com.tomlu.ATMLReportSniffer.ATML;

import java.util.List;

public class TestResult {
	
	private UUT uut;
	private String result;
	private String startTime;
	private String stopTime;


	private float totalTestTime;
	private List<Step> steps;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public float getTotalTestTime() {
		return totalTestTime;
	}

	public void setTotalTestTime(float totalTestTime) {
		this.totalTestTime = totalTestTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public UUT getUut() {
		return uut;
	}

	public void setUut(UUT uut) {
		this.uut = uut;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

}
