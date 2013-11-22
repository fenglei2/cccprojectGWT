package com.cs.umbc.project.client.blobs;


import com.google.gwt.user.client.rpc.IsSerializable;
import com.cs.umbc.project.shared.Branch;
import com.cs.umbc.project.shared.Curve;



public class PlotData  implements IsSerializable{
	

	Curve[] curves;
	double maxSurvivalTime;
	Branch root;
	
	
	public double getMaxSurvivalTime() {
		return maxSurvivalTime;
	}

	public void setMaxSurvivalTime(double maxSurvivalTime) {
		this.maxSurvivalTime = maxSurvivalTime;
	}



	public Curve[] getCurves() {
		return curves;
	}

	public void setCurves(Curve[] curves) {
		this.curves = curves;
	}

	public Branch getRoot() {
		return root;
	}

	public void setRoot(Branch root) {
		this.root = root;
	}
	
	
	

}
