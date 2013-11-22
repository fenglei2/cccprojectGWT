package com.cs.umbc.project.server.cccproject.myLogRankTest;



import java.util.List;

import com.cs.umbc.project.server.cccproject.common.DataModel;
import com.cs.umbc.project.shared.Pattern;

import Jama.Matrix;

public class LogRankTestMatrix {

	private List<Pattern> dataSet;
	private boolean calcuateProcess = true;
	Matrix matrix;
	
	public LogRankTestMatrix(){
		dataSet=DataModel.getInstance().getDataCombinations();
	}


	public void calTestStaticsMatrix() {
		int n = dataSet.size();
		double[][] testStaticsMatrix = new double[n][n];
		Pattern pat1 = null;
		Pattern pat2 = null;
		for (int i = 0; i < n; i++) {
			pat1 = dataSet.get(i);
			for (int j = 0; j <= i; j++) {
				
				pat2 = dataSet.get(j);
				if (calcuateProcess){
					System.out.println(i + " -- " + j+" size1:"+pat1.getCensor().size()+" size2:"+pat2.getCensor().size());
				}
				
				testStaticsMatrix[i][j] = compare(pat1, pat2);
			}
		}
		matrix = new Matrix(testStaticsMatrix);
	}
	
	public double compare(Pattern pattern1, Pattern pattern2) {
		PatternLogRankTest patternLogRanktest=new PatternLogRankTest(pattern1, pattern2);
		return Math.abs(patternLogRanktest.testStatistic);
	}

	public Matrix getMatrix() {
		return matrix;
	}

}
