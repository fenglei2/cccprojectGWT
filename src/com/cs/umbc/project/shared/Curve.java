package com.cs.umbc.project.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Curve implements IsSerializable {
	double[] probabilityOfSurvival;// y
	double[] time;// x
	int  count;
	String label;

	public Curve(double[] prob, double[] time, String label) {
		this.probabilityOfSurvival = prob;
		this.time = time;
		this.label = label;
		//this.count = count;
	}

	public double[] getProbabilityOfSurvival() {
		return probabilityOfSurvival;
	}

	public void setProbabilityOfSurvival(double[] probabilityOfSurvival) {
		this.probabilityOfSurvival = probabilityOfSurvival;
	}

	public double[] getTime() {
		return time;
	}
	//public int[] getCount() {
	//	return count;
	//}
	public void setTime(double[] time) {
		this.time = time;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Curve() {

	}

}
