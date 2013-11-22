package com.cs.umbc.project.shared;

import java.util.List;

import ca.nanometrics.gflot.client.DataPoint;
import ca.nanometrics.gflot.client.PlotModel;
import ca.nanometrics.gflot.client.SeriesHandler;
import ca.nanometrics.gflot.client.SimplePlot;
import ca.nanometrics.gflot.client.options.AxisOptions;
import ca.nanometrics.gflot.client.options.GlobalSeriesOptions;
import ca.nanometrics.gflot.client.options.LegendOptions;
import ca.nanometrics.gflot.client.options.LegendOptions.LegendPosition;
import ca.nanometrics.gflot.client.options.LineSeriesOptions;
import ca.nanometrics.gflot.client.options.PlotOptions;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.cs.umbc.project.client.widges.HazardCurveWidge;
import com.cs.umbc.project.shared.Curve;

public class Hazard {
	
	public static void HazardCurveWidget (List<Curve> curves, HazardCurveWidge plot)
	{
		for (int i = 0; i < curves.size(); i++) {
			Curve curve = curves.get(i);
			
//			System.out.println("label: "+curves.get(i).getLabel());
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
}
