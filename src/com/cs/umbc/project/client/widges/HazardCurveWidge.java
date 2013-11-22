package com.cs.umbc.project.client.widges;
import java.util.ArrayList;
import java.util.List;

import ca.nanometrics.gflot.client.DataPoint;
import ca.nanometrics.gflot.client.ImageDataPoint;
import ca.nanometrics.gflot.client.PlotModel;
import ca.nanometrics.gflot.client.Series;
import ca.nanometrics.gflot.client.SeriesHandler;
import ca.nanometrics.gflot.client.SimplePlot;
import ca.nanometrics.gflot.client.options.AxisOptions;
import ca.nanometrics.gflot.client.options.GlobalSeriesOptions;
import ca.nanometrics.gflot.client.options.GridOptions;
import ca.nanometrics.gflot.client.options.ImageSeriesOptions;
import ca.nanometrics.gflot.client.options.LegendOptions;
import ca.nanometrics.gflot.client.options.LegendOptions.LegendPosition;
import ca.nanometrics.gflot.client.options.LineSeriesOptions;
import ca.nanometrics.gflot.client.options.PlotOptions;
import ca.nanometrics.gflot.client.options.PointsSeriesOptions;
import ca.nanometrics.gflot.client.options.SelectionOptions;
import ca.nanometrics.gflot.client.options.SelectionOptions.SelectionMode;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Widget;
import com.cs.umbc.project.shared.Curve;
public class HazardCurveWidge extends SimplePlot{
	


	@UiField(provided = true)
	private static HazardCurveWidge plot = null;
	private static List<Curve> curves;
	public static List<Curve> getCurves() {
		return curves;
	}

	public static void setCurves(List<Curve> curves) {
		HazardCurveWidge.curves = curves;
	}

	private static double maxTime;
	private static int step;

	public HazardCurveWidge(PlotModel plotModel, PlotOptions plotOptions) {
		super(plotModel,plotOptions);
	}

	public static HazardCurveWidge createEmptyPlot() {
		initial();
		plot.setData();
		return plot;
	}
	private void setData() {
		plot.getModel().addSeries("");//It's a label for level.
		plot.getModel().getHandlers().get(0).add(new DataPoint(0, 0));
	}
	

private static void drawCurves() {
		
	for (int i = 0; i < curves.size(); i++) {
		Curve curve = curves.get(i);
		
//		System.out.println("label: "+curves.get(i).getLabel());
		plot.getModel().addSeries(curves.get(i).getLabel()+"/"+curve.getTime().length);
		
	}

	for (int i = 0; i < plot.getModel().getHandlers().size(); i++) {
		SeriesHandler series = plot.getModel().getHandlers().get(i);
		
		Curve curve = curves.get(i);
    //	series.add(new DataPoint(curve.getTime()[0], 0));
		   double z=0;
			int m =0;
		double average_count[]=new double[5] ;
		average_count[11]=0;
		average_count[10]=0;
		average_count[9]=0;
		average_count[8]=0;

	    average_count[7]=0;
		average_count[6]=0;
		average_count[5]=0;
		 average_count[4]=0;
         average_count[3]=0;
         average_count[2]=0;
         average_count[1]=0;
         average_count[0]=0;
		for (int j = 0; j < curve.getTime().length; j++) {
			if ( curve.getProbabilityOfSurvival()[j]-curve.getProbabilityOfSurvival()[j+1]!=0)
			{ 
			
			 if(curve.getTime()[j+1]-curve.getTime()[j]<0.1)
			 {
		//	double x = curve.getProbabilityOfSurvival()[j]-curve.getProbabilityOfSurvival()[j+1];
          //  double y = curve.getProbabilityOfSurvival()[j];		
           
            m++;
            			 }
			 else if (curve.getTime()[j+1]-curve.getTime()[j]>=0.1&&curve.getProbabilityOfSurvival()[j]-curve.getProbabilityOfSurvival()[j+1]!=0)
			 {
			double x = curve.getProbabilityOfSurvival()[j-m]-curve.getProbabilityOfSurvival()[j+1];
			
            double y = curve.getProbabilityOfSurvival()[j];
    		average_count[11]=	average_count[10];
    		average_count[10]=	average_count[9];
    		average_count[9]=	average_count[8];
    		average_count[8]= average_count[7];
          average_count[7]=average_count[6];
            average_count[6]=average_count[5];
           average_count[5]=average_count[4];
            average_count[4]=average_count[3];
            average_count[3]=average_count[2];
            average_count[2]=average_count[1];
            average_count[1]=average_count[0];
            average_count[0]=x/y;
            z = (average_count[11]+average_count[10]+average_count[9]+average_count[8]+average_count[7]+average_count[6]+average_count[5]+average_count[4]+average_count[3]+average_count[2]+average_count[1]+average_count[0])/12;
            m=0;
			 }
			}
			else if ( curve.getProbabilityOfSurvival()[j]-curve.getProbabilityOfSurvival()[j+1]==0)
			{
					 
          
			}
			//	series.add(new DataPoint(curve.getTime()[j], (curve.getProbabilityOfSurvival()[j]-curve.getProbabilityOfSurvival()[j+1])/(curve.getProbabilityOfSurvival()[j]*(curve.getTime()[j+1]-curve.getTime()[j]))));		
			//	series.add(new DataPoint(curve.getTime()[j], curve.getProbabilityOfSurvival()[j]));
				series.add(new DataPoint(curve.getTime()[j], z));
			
		}
		
	}
}
	public static HazardCurveWidge createPlot(Curve[] list, double max) {
		
		setCurves(list);
		maxTime = round(max);
		System.out.println("maxtime "+maxTime);
		initial();
		drawCurves();
		// plot.generateRandomData();
		return plot;
	}

	private static double round(double time) {
		int i = 0;
		int n = 10;
		// int step;

		if (time < 100) {
			while (time > n && n < 100) {
				n *= 10;
			}
			step = n / 10;
		} else if (time < 500) {
			step = 50;
		} else {
			step = 100;
		}
		// System.out.println("step: "+step+" time: "+time+" i: "+i);

		n = 10;
		while (time > n) {
			n *= 10;
			i++;
		}

		int power10 = (int) (Math.pow(10, i));
		int round = (int) time / power10 * power10;
		// System.out.println("n: "+n+" round: "+round);
		while (round < time) {
			round += step;
		}
		return round;
	}

	
	

	private static void setCurves(Curve[] list) {
		curves = new ArrayList<Curve>();
		for (Curve cu : list) {
			curves.add(cu);
		}
	}

	public static HazardCurveWidge createPlot() {
		initial();
		plot.generateRandomData();
		return plot;
	}
	
	public void generateRandomData() {
		int nbSeries = Random.nextInt(5) + 1;
		for (int i = 0; i < nbSeries; i++) {
			plot.getModel().addSeries("Random Series " + i);
		}
		for (int i = 1; i < 13; i++) {
			for (SeriesHandler series : plot.getModel().getHandlers()) {
				series.add(new DataPoint(i, Random.nextInt(30)));
			}
		}
	}
	
	public static void initial() {
		PlotModel model = new PlotModel();

		PlotOptions plotOptions = new PlotOptions();
		plotOptions.setGlobalSeriesOptions(new GlobalSeriesOptions().setLineSeriesOptions(
				new LineSeriesOptions().setLineWidth(2)).setShadowSize(0));

		plotOptions.setLegendOptions(new LegendOptions().setNumOfColumns(2).setBackgroundOpacity(0)
				.setPosition(LegendPosition.NORTH_EAST));
		plotOptions.addXAxisOptions(new AxisOptions().setLabel("Hazard Time (month)").setMinimum(0)
				.setMaximum(maxTime+30));
		plotOptions.addYAxisOptions(new AxisOptions().setLabel("Probability of Hazard").setMinimum(0).setMaximum(0.01));
		plot = new HazardCurveWidge(model, plotOptions);
	}
}
