package com.cs.umbc.project.shared;

import java.util.List;

//import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.IsSerializable;



public class Branch implements IsSerializable{
	
//	NumberFormat de=NumberFormat.getFormat("0.00");

	Branch left=null;
	Branch right=null;
	Point topLeft;
	Point topRight;
	Point bottomLeft;
	Point bottomRight;
	double height = -1;
	int size;
	
//	double LEAF_HEIGHT=0.1;
	double midline;
	double gap=0.2;
	Cluster cluster;
	
	public Cluster getCluster() {
		return cluster;
	}

	public Branch(){
		
	}

	public Branch(Cluster cluster,double midline) {
		this.cluster=cluster;
		height=cluster.getHeight();
		size=cluster.size();
		this.midline=midline;
		
		//distance to mid-line
		
		double xleft=midline+gap;
		double xright=midline-gap;
		double yleft=midline;
		double yright=midline;
		if(cluster.getLeft()!=null&&cluster.getLeft().size()>1){
			xleft-=cluster.getLeft().getRight().size();
			yleft=cluster.getLeft().getHeight();
		}else{
			xleft-=1;
//			yleft=height-LEAF_HEIGHT;
		}
		if(cluster.getRight()!=null&&cluster.getRight().size()>1){
			xright+=cluster.getRight().getLeft().size();
			yright=cluster.getRight().getHeight();
		}else{
			xright+=1;
//			yright=height-LEAF_HEIGHT;
		}
		
		topLeft=new Point(xleft,height);
		bottomLeft=new Point(xleft,yleft);
		topRight=new Point(xright,height);
		bottomRight=new Point(xright,yright);

	}
	

	public void print(){
		System.out.println(this);
		if(left!=null){
			left.print();
		}
		if(right!=null){
			right.print();
		}
	}
	
	public void getAllBranches(List<Branch> list){
		list.add(this);
		if(left!=null){
			left.getAllBranches(list);
		}
		if(right!=null){
			right.getAllBranches(list);
		}
	}
	
	
	

	public Branch getLeft() {
		return left;
	}

	public void setLeft(Branch left) {
		this.left = left;
	}

	public Branch getRight() {
		return right;
	}

	public void setRight(Branch right) {
		this.right = right;
	}

	public Point getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(Point topLeft) {
		this.topLeft = topLeft;
	}

	public Point getTopRight() {
		return topRight;
	}

	public void setTopRight(Point topRight) {
		this.topRight = topRight;
	}

	public Point getBottomLeft() {
		return bottomLeft;
	}

	public void setBottomLeft(Point bottomLeft) {
		this.bottomLeft = bottomLeft;
	}

	public Point getBottomRight() {
		return bottomRight;
	}

	public void setBottomRight(Point bottomRight) {
		this.bottomRight = bottomRight;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

//	public Cluster getCluster() {
//		return cluster;
//	}
//
//	public void setCluster(Cluster cluster) {
//		this.cluster = cluster;
//	}

	public String toString(){
		return "["+topLeft+" "+topRight+" "+bottomLeft+" "+bottomRight+"] height="+height+" size="+size;
	}


}
