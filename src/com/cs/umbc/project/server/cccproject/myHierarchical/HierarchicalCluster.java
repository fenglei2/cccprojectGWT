package com.cs.umbc.project.server.cccproject.myHierarchical;


import java.util.ArrayList;
import java.util.List;

import com.cs.umbc.project.server.cccproject.common.DataModel;
import com.cs.umbc.project.server.cccproject.myHierarchical.DistanceCalulation.DistanceInfo;
import com.cs.umbc.project.shared.Cluster;


import Jama.Matrix;


public class HierarchicalCluster {

	private boolean PRINT_CLUSTER_RESULT;
	private boolean process = false;

	Matrix dissimilarityMatrix;
	Matrix copheneticMatrix;


	private List<List<Cluster>> allClusters;


	public HierarchicalCluster(Matrix disMatrix,boolean print) {
		this.PRINT_CLUSTER_RESULT = print;
		this.dissimilarityMatrix = disMatrix;
	}

	// ----ok---------

	// we need to merge clusters distanceInfo.row and
	// distanceInfo.column
	// into one cluster
	// this happens in two steps:
	// 1. merging the distances
	// 2. merging the clusters themselves that are stored in clusters
	// list
	public List<Cluster> partition() {
		// create a cluster for each pattern.
		List<Cluster> currPartition = new ArrayList<Cluster>();
		
		for (int i = 0; i < dissimilarityMatrix.getColumnDimension(); i++) {
//			String clusterLabel="C"+i;
			String clusterLabel=DataModel.getInstance().getDataCombinations().get(i).getPatternLabel();
			currPartition.add(new Cluster(clusterLabel, i));
		}

		DistanceCalulation cal = new DistanceCalulation(dissimilarityMatrix);

		
		int iterationCount = 0;
		allClusters = new ArrayList<List<Cluster>>();
		printClusterResult(currPartition);
		for (DistanceInfo shortestDistInfo = cal.getClosestPair(); shortestDistInfo != null; shortestDistInfo = cal.getClosestPair()) {
			allClusters.add(copy(currPartition));
			
			currPartition=cal.mergeTwoClosestClusters(shortestDistInfo,currPartition);
			printClusterResult(currPartition);
			iterationCount++;
		}
		allClusters.add(currPartition);
		
		copheneticMatrix=cal.getCopheneticMatrix();
		
		return currPartition;
	}



	private List<Cluster> copy(List<Cluster> currPartition) {
		List<Cluster> copy = new ArrayList<Cluster>();
		copy.addAll(currPartition);
		return copy;
	}

	private void printClusterResult(List<Cluster> partition) {
		if (PRINT_CLUSTER_RESULT) {
			System.out.print("cluster size = " + partition.size() + ": ");
			for (int i = 0; i < partition.size(); i++) {
				List<Integer> print = partition.get(i).getPatternIndexes();
				System.out.print("[");
				for (int ii = 0; ii < print.size() - 1; ii++) {
					System.out.print(print.get(ii) + 1 + ",");
				}
				System.out.print(print.get(print.size() - 1) + 1 + "] ");
			}
			System.out.println();
		}
		if (process)
			System.out.println();

	}
	public Matrix getDissimilarityMatrix() {
		return dissimilarityMatrix;
	}

	public Matrix getCopheneticMatrix() {
		return copheneticMatrix;
	}
	public List<List<Cluster>> getAllClusters() {
		return allClusters;
	}


}
