package com.tomlu.ATMLReportSniffer.ATML;

public class Step {
	
	private int index;
	private String name;
	private String stepId;
	private String stepType;
	private String result;
	private String measureValue;
	private float totalTestTime;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}
	
	public String getMeasureValue() {
		return measureValue;
	}

	public void setMeasureValue(String measureValue) {
		this.measureValue = measureValue;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {

		if(obj instanceof Step) {
			Step step = (Step)obj;
			
			if(ExportHandler.isAccurateStepMatch()) {
				return getIndex() == step.getIndex() &&
						getName().equals(step.getName()) && 
						getStepId().equals(step.getStepId());
			}else{
				return getIndex() == step.getIndex() &&
						getName().equals(step.getName());
			}
			
		} else {
			return false;
		}
	}
	
}
