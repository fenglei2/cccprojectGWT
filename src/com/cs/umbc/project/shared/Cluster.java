package com.cs.umbc.project.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.cs.umbc.project.shared.Branch;


public class Cluster implements IsSerializable{

	Cluster left = null;
	Cluster right = null;
	private List<Integer> patternIndexes;// combination id
	double height;
	String clusterLabel;
	Branch branch;
	
	public Cluster(){
		
	}

	public Branch createBranch(double midline) {
		branch = new Branch(this,midline);
		if (left != null&&left.size()>1) {
			branch.setLeft(left.createBranch(midline-left.right.size()));
		}
		if (right != null && right.size()>1){
			branch.setRight(right.createBranch(midline+right.left.size()));
		}
		return branch;

	}

	public int size() {
		return patternIndexes.size();
	}

	public String getClusterLabel() {
		return clusterLabel;
	}

//	public List<Pattern> getPatterns() {
//		List<Pattern> patterns = new ArrayList<Pattern>();
//		for (int index : patternIndexes) {
//			patterns.add(Util.getPattern(index));
//		}
//		return patterns;
//	}

	public Cluster(String clusterLabel, int patternIndex) {
		
		patternIndexes = new ArrayList<Integer>();
		patternIndexes.add(patternIndex);
		this.clusterLabel = clusterLabel;
	}

	public Cluster(Cluster left, Cluster right, double height) {
		this.left = left;
		this.right = right;
		this.height = height;
		// this.name = "[" + left.name + "," + right.name + "]";
		this.clusterLabel = left.clusterLabel + "-" + right.clusterLabel;

		patternIndexes = new ArrayList<Integer>();
		for (Integer index : left.getPatternIndexes()) {
			patternIndexes.add(index);
		}
		for (Integer index : right.getPatternIndexes()) {
			patternIndexes.add(index);
		}
	}

	public List<Integer> getPatternIndexes() {
		return patternIndexes;
	}
	
	

	public Cluster getLeft() {
		return left;
	}

	public Cluster getRight() {
		return right;
	}

	public double getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return clusterLabel;
	}
}
