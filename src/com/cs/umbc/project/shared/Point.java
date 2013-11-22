package com.cs.umbc.project.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class Point implements IsSerializable{
	double x;
	double y;
	
	
//	public String toString(){
//		return "("+de.format(x)+","+de.format(y)+")";
//	}
	
	
	
	public Point(){
		
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String toString(){
		return "("+x+","+y+")";
	}

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
}

