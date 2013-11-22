package com.cs.umbc.project.client.widges;

import java.util.ArrayList;
import java.util.List;


import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.swtb.font.client.Simplex;
import com.swtoolbox.canvasfont.client.SWTBCanvasText;
import com.cs.umbc.project.shared.Branch;
import com.cs.umbc.project.shared.Point;

//Dendrogram UI in "Group patients" tab

public class DendgWidge extends SWTBCanvasText {

	private static NumberFormat ROUND_TO_INT = NumberFormat.getFormat("0");
	private static NumberFormat ROUND_T0_ONEDEGIT = NumberFormat.getFormat("0.0");

	private static SWTBCanvasText canvas;
	private static int width = 1200, height = 500;
	private static int centerX, centerY;

	static List<Branch> allBranches;
	static double MAX_HEIGHT;// the distance between 2 last merging
									// clusters
	static double LEAF_HEIGHT;
	static int DIST_PAD;// the distance pad for a proper distance from leftmost
						// node to scale

	private DendgWidge(int width, int height) {
		super(width, height);
	}

	public static SWTBCanvasText createPlot(Branch root) {
		createEmptyPlot() ;
		allBranches = new ArrayList<Branch>();
		root.getAllBranches(allBranches);
		
		MAX_HEIGHT = Integer.valueOf(ROUND_TO_INT.format(root.getCluster().getHeight()));
		System.out.println("max height: "+MAX_HEIGHT+" "+root.getCluster().getHeight());
		LEAF_HEIGHT = MAX_HEIGHT / 20;
		DIST_PAD = 2-leftMostPos(root);
		drawScale();
		draw(root);

		return canvas;
	}

	private static void draw(Branch root) {
		for (Branch branch : allBranches) {
			drawBranch(branch);
		}
	}

	private static void drawBranch(Branch branch) {
		int leftSize = branch.getCluster().getLeft().size();
		int rightSize = branch.getCluster().getRight().size();

		
		if (leftSize == 1) {
			branch.getBottomLeft().setY(branch.getTopLeft().getY() - LEAF_HEIGHT);
			drawLeafLabel(branch.getCluster().getLeft().getClusterLabel(), branch.getBottomLeft());
		}

		if (rightSize == 1) {
			branch.getBottomRight().setY(branch.getTopRight().getY() - LEAF_HEIGHT);
			drawLeafLabel(branch.getCluster().getRight().getClusterLabel(), branch.getBottomRight());
		}

		drawLine(branch.getTopLeft(), branch.getTopRight());
		drawLine(branch.getTopLeft(), branch.getBottomLeft());
		drawLine(branch.getTopRight(), branch.getBottomRight());
	}

	private static void drawLine(Point p1, Point p2) {
		drawLine2(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	private static void drawLeafLabel(String clusterLabel, Point point) {

		canvas.saveContext();
		canvas.setFont(new Simplex());
		canvas.beginPath();
		canvas.textTo(clusterLabel, getX2(point.getX()-0.18), getY(point.getY()), 0, 0.3);
		canvas.stroke();
		canvas.restoreContext();
	}
	

	private static void drawLine(double x1, double y1, double x2, double y2) {
		canvas.beginPath();
		x1 = getX(x1);
		x2 = getX(x2);
		y1 = getY(y1);
		y2 = getY(y2);

		canvas.lineTo(x1, y1);
		canvas.lineTo(x2, y2);
		canvas.stroke();
	}
	
	private static void drawLine2(double x1, double y1, double x2, double y2) {
		canvas.beginPath();
		x1 = getX2(x1);
		x2 = getX2(x2);
		y1 = getY(y1);
		y2 = getY(y2);

		canvas.lineTo(x1, y1);
		canvas.lineTo(x2, y2);
		canvas.stroke();
	}
	
	

	private static double getY(double y) {
		return centerY - 350 * y;
	}

	private static double getX(double x) {
		return centerX + 50 * x;
	}
	
	private static double getX2(double x) {
		return centerX + 40 * (x+DIST_PAD);
	}


	private static void drawScale() {

		drawLine(0, 0, 0, 1);
		drawLine(-0.1, 0, 0, 0);

		for (double h = 0; h <= 1; h += 0.2) {
			drawLine(-0.1, h, 0, h);
//			System.out.println("mh:" + ROUND_T0_ONEDEGIT.format(MAX_HEIGHT * h));
			drawLabel(String.valueOf(ROUND_T0_ONEDEGIT.format(MAX_HEIGHT * h)), -0.8, h+0.02);
		}
	}
	

	private static void drawTitle() {
		canvas.saveContext();
		canvas.setFont(new Simplex());
		canvas.beginPath();
		canvas.textTo("Dendrogram", width/3, 13, 0,0.6);
		canvas.stroke();
		canvas.restoreContext();
	}
	private static void drawLabel(String label, double x, double y) {
		canvas.saveContext();
		canvas.setFont(new Simplex());
		canvas.beginPath();
		canvas.textTo(label, getX(x), getY(y), 0, 0.4);
		canvas.stroke();
		canvas.restoreContext();
	}

	public static SWTBCanvasText createEmptyPlot() {
		
		drawBorder();
		drawTitle();

		return canvas;
	}

	private static void drawBorder() {
		canvas = new SWTBCanvasText(width, height);
		centerX = 50;
		centerY = 420;

		canvas.setLineWidth(1);
		canvas.setStrokeStyle(Color.BLACK);
		canvas.beginPath();
		canvas.moveTo(1, 1);
		canvas.lineTo(1, height - 1);
		canvas.lineTo(width - 1, height - 1);
		canvas.lineTo(width - 1, 1);
		canvas.closePath();

		canvas.stroke();

	}

	private static int leftMostPos(Branch root) {
		Branch node = root;
		while (node.getLeft() != null) {
			node = node.getLeft();
		}
		return (int) node.getBottomLeft().getX();
	}
	

	public static SWTBCanvasText create() {
		createEmptyPlot();

		canvas.beginPath();
		canvas.moveTo(20, 20);
		canvas.lineTo(10, 40);
		canvas.lineTo(40, 60);
		canvas.lineTo(60, 80);
		canvas.closePath();
		canvas.stroke();

		return canvas;
	}

}
