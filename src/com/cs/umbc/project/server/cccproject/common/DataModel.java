package com.cs.umbc.project.server.cccproject.common;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.umbc.project.client.blobs.BlobDataFilter;
import com.cs.umbc.project.server.cccproject.PAM.LearntDisMatrix;
import com.cs.umbc.project.server.cccproject.myHierarchical.HierarchicalCluster;
import com.cs.umbc.project.server.cccproject.myHierarchical.dendrogram.Dendrogram;
//import com.cs.umbc.project.server.cccproject.myHierarchical.dendrogram.DrawDendrogram;
import com.cs.umbc.project.server.cccproject.myLogRankTest.LogRankTestMatrix;
//import com.cs.umbc.project.server.cccproject.survivalCurve.GraphingData;
import com.cs.umbc.project.server.cccproject.survivalCurve.KaplanPlot;
import com.cs.umbc.project.shared.Branch;
import com.cs.umbc.project.shared.Cluster;
import com.cs.umbc.project.shared.Curve;
import com.cs.umbc.project.shared.Pattern;

import Jama.Matrix;


public class DataModel {

	private static volatile DataModel instance = null;
	private static double maxSurvivalTime;
	

	// number of combinations
	int SIZE;

	List<Pattern> dataCombinations;
	Map<String, Pattern> allData;

	Matrix logRankTestMatrix;// initial dissimilarity matrix
	Matrix learntMatrix;// learnt dissimilarity matrix
	List<List<Cluster>> allClusters;// all hierarchical clusters

	private Cluster root;
	
	List<Curve> curves;
	Branch rootBranch;

	public static DataModel getInstance() {
		if (instance == null) {
			synchronized (DataModel.class) {
				if (instance == null) {
					instance = new DataModel();
				}
			}
		}
		return instance;
	}

	private DataModel() {
	}

	public void start(BufferedReader br,BlobDataFilter filter) {
		//read data file
		dataPreprocess(br, filter);
		logRankTest();
		//calculate learnt dissimilarity
		standardizeRound();
		hierarchicalCluster();
		dendrogram();
		KaplanMeierCurve();
	}


	private void dendrogram() {
		System.out.println("\n =========  Dendrogram ==========");
		Dendrogram den = new Dendrogram(root);
		rootBranch = den.getRootBranch();
		if (Config.SHOW_DENDROGRAM.bool) {
			rootBranch.print();
		}
		System.out.println();
	}

	public void KaplanMeierCurve() {
		List<Cluster> partition = allClusters.get(0);

		List<Double[]> times = new ArrayList<Double[]>();
		List<Double[]> censors = new ArrayList<Double[]>();
		int ct = 0;
		for (Cluster c : partition) {

			List<Integer> patternIndexes = c.getPatternIndexes();
			List<Double> timeList = new ArrayList<Double>();
			List<Double> censorList = new ArrayList<Double>();
			for (int patternIdx : patternIndexes) {

				List<Double> time = getPattern(patternIdx).getSurvival();
				timeList.addAll(time);
				List<Double> censor = getPattern(patternIdx).getCensor();
				censorList.addAll(censor);
			}
			if (Config.SHOW_CLUSTERSIZE.bool) {
				System.out.println("cluster" + ct++ + " size: " + timeList.size());//sizr
			}
			times.add(doubleArr(timeList));
			censors.add(doubleArr(censorList));
		}
		curves = new ArrayList<Curve>();
		System.out.println("==============  Kaplan estimating cluster ==============");
		for (int i = 0; i < times.size(); i++) {
			Double[] time=times.get(i);
			KaplanPlot kap = new KaplanPlot(time, censors.get(i));
			curves.add(new Curve(kap.probabilityOfSurvival, kap.time,dataCombinations.get(i).getPatternLabel()));

			setMaxSurvivalTime(Math.max(maxSurvivalTime, kap.time[time.length-1]));
		}
		
		System.out.println("maxtime: "+maxSurvivalTime);

//		GraphingData.plot(list);
	}

	public void hierarchicalCluster() {
		System.out.println("\n=========   Hierarchical clustering  =========\n");
		HierarchicalCluster hi = new HierarchicalCluster(learntMatrix,Config.SHOW_CLUSTERING_RESULT.bool);// show
		root = hi.partition().get(0);
		allClusters = hi.getAllClusters();
	}

	public void standardizeRound() {

		if (!Config.READ_LEARNT_MATRIX_RESULT.bool) {
			System.out.println("\n=========== Run Standardize rounds ========================\n");
			LearntDisMatrix learnt = new LearntDisMatrix(Config.ROUND_TIMES.num);
			learnt.run(Config.SHOW_LEARNT_MATRIX.bool);
			learntMatrix = learnt.getLearntDissimilarity();
		} else {
//			System.out.println("\n======= Read Standardize rounds result from file =======\n");
//			learntMatrix = disMatrixFromFile(Config.LEARNT_PATH.name, SIZE, Config.CLUSTER_ALGORITHM.alg);
		}
	}

	public void logRankTest() {
		if (dataCombinations == null || dataCombinations.size() == 0) {
			throw new IllegalArgumentException("Combination is empty");
		}

		if (!Config.READ_LEARNT_MATRIX_RESULT.bool) {
			if (!Config.READ_TEST_RESULT.bool) {
				System.out.println("\n=======Run Log rank cs.umbc============\n");
				LogRankTestMatrix logRankTest = new LogRankTestMatrix();
				logRankTest.calTestStaticsMatrix();
				logRankTestMatrix = logRankTest.getMatrix();
//				Util.writeResult2File(logRankTestMatrix, Config.TEST_OUTPUT_PATH.name);
			} else {
//				System.out.println("\n=======Get Log rank test result from file============\n");
//				logRankTestMatrix = Util.disMatrixFromFile(Config.TEST_PATH.name, SIZE, Config.CLUSTER_ALGORITHM.alg);
			}
		}

		if (Config.SHOW_TEST_MATRIX.bool) {
			logRankTestMatrix.print(8, 3);
		}
	}
	
	
	private void dataPreprocess(BufferedReader br, BlobDataFilter filter){
		System.out.println("\n==============  Pre-processing  ==============");

		if(br==null){
			throw new IllegalArgumentException("Data stream is empty");
		}
		// all combinations, including the combination with small number of
		// patients
		// key: combination
		// value: double[]
		allData = new HashMap<String, Pattern>();
		// after remove the combinations with small number of patients
		dataCombinations = new ArrayList<Pattern>();
		try {
			String strLine = br.readLine();
			int lineNumber = 0;
			System.out.println(strLine);

			int len = strLine.split("\t").length - 1;
			double[] features = new double[len];
			int timeCol=filter.getTimeCol();
			int censorCol=filter.getCensorCol();
			int minPatientsNo=filter.getMinPatientsNo();
			int[] selected=filter.getSelectItems();

			while ((strLine = br.readLine()) != null) {

				lineNumber++;
				StringBuffer combinationLabel = new StringBuffer();
				String[] featureStr = strLine.split("\t");

				int pos=0;
				// NOTE!! IN raw CSV data file:
				// The last column must be Survival time
				// The last second column must be censor indicator

				// totally n columns
				// column 1 to n-2 contains regular features
				// combination label is the level combination, eg:1221
				for (int i = 0; i < featureStr.length; i++) {
					if(contains(i,selected)){
						features[pos++] = Double.parseDouble(featureStr[i]);
						combinationLabel.append(featureStr[i]);
					}
				}

				String label = combinationLabel.toString();
				if (allData.get(label) == null) {
					Pattern pattern = new Pattern(features, label);
					allData.put(label, pattern);
				}

				Pattern pattern = allData.get(label);
				// add survival time and censor from each patient
				pattern.setSurvival(Double.parseDouble(featureStr[timeCol]));
				pattern.setCensor(Double.parseDouble(featureStr[censorCol]));

			}

			System.out.println("Total dataset: " + lineNumber);
			System.out.println("Combination size: " + allData.size());

			// Remove the combinations with small number of patients
			Iterator<String> classItr = allData.keySet().iterator();
			int index = 0;
			while (classItr.hasNext()) {

				String com = classItr.next();
				Pattern pat = allData.get(com);

				if (pat.getSurvival().size() >= minPatientsNo) {
					pat.setIndex(index++);
					dataCombinations.add(pat);
				}
			}

			SIZE = dataCombinations.size();
			System.out.println("After removing small sampleSet(<" + minPatientsNo + "): " + SIZE);

		} catch (Exception e) {
			System.out.println("Exception while reading csv file: " + e);
		}
	}
	
	
	

	private boolean contains(int i, int[] selected) {
		for(int s:selected){
			if(s==i){
				return true;
			}
		}
		return false;
	}


	public List<Pattern> getDataCombinations() {
		return dataCombinations;
	}

	public Matrix getLearntMatrix() {
		return learntMatrix;
	}

	public Matrix getLogRankTestMatrix() {
		return logRankTestMatrix;
	}

	public Map<String, Pattern> getAllData() {
		return allData;
	}

	public int getSIZE() {
		return SIZE;
	}
	

	public static double getMaxSurvivalTime() {
		return maxSurvivalTime;
	}

	public static void setMaxSurvivalTime(double maxSurvivalTime) {
		DataModel.maxSurvivalTime = maxSurvivalTime;
	}

	public List<Curve> getCurves() {
		return curves;
	}

	public void setCurves(List<Curve> curves) {
		this.curves = curves;
	}

	public Branch getRootBranch() {
		return rootBranch;
	}

	public void setRootBranch(Branch rootBranch) {
		this.rootBranch = rootBranch;
	}
	
	
	public Pattern getPattern(int index) {
	return dataCombinations.get(index);
}
	
	public static Double[] doubleArr(List<Double> survival) {

		Double[] arr = new Double[survival.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = survival.get(i);
		}
		return arr;
	}


}
