	package com.cs.umbc.project.client.widges;

import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

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

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.cs.umbc.project.client.MyClickHandler;

import com.cs.umbc.project.shared.Curve;

//Survival curve UI in "Group patients" tab

public class SurvCurveWidge extends SimplePlot implements ClickHandler{

	@UiField(provided = true)
	private static SurvCurveWidge plot = null;
	private static List<Curve> curves;
	public static  List<RadioButton> rb;
	
 static int ss =0;
	public static List<Curve> getCurves() {
		return curves;
	}
	public static List<RadioButton> getRadioButton() {
		return rb;
	}
	public static void setRadioButton(List<RadioButton> rb) {
		SurvCurveWidge.rb = rb;
	}
	public static void setCurves(List<Curve> curves) {
		SurvCurveWidge.curves = curves;
	}

	private static double maxTime;
	private static int step;

	public SurvCurveWidge(PlotModel plotModel, PlotOptions plotOptions) {
		super(plotModel, plotOptions);
	}

	public static SurvCurveWidge createEmptyPlot() {
		initial();
		plot.setData();
		return plot;
	}

	public static SurvCurveWidge createPlot(Curve[] list, double max) {
		
		setCurves(list);
		setRadioButton();
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
	
	
	private static void drawCurves() {
	
	    
		
		for (int i = 0; i < curves.size(); i++) 
		{
//			System.out.println("label: "+curves.get(i).getLabel());
			Curve curve = curves.get(i);
			RadioButton rbs= rb.get(i);
		
			rbs.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				public void onValueChange(ValueChangeEvent<Boolean> event) {
				//	Window.alert("s");
				}
			});
         // GET
			plot.getModel().addSeries(curves.get(i).getLabel()+"/"+curve.getTime().length+rbs);
		}
		 
				for (int i = 0; i < plot.getModel().getHandlers().size(); i++) {
					SeriesHandler series = plot.getModel().getHandlers().get(i);
					
			Curve curve = curves.get(i);
			for (int j = 0; j < curve.getTime().length; j++) {
     			System.out.println("---"+curve.getTime()[j]+" "+curve.getProbabilityOfSurvival()[j]);
				series.add(new DataPoint(curve.getTime()[j], curve.getProbabilityOfSurvival()[j]));
			
			}
		//	plot.getModel().addSeries(curves.get(i).getLabel()+"//"+s);
		}
	}
	

	private static void setCurves(Curve[] list) {
		curves = new ArrayList<Curve>();
		for (Curve cu : list) {
			curves.add(cu);
		}
	}
	private static void setRadioButton() {
		rb = new ArrayList<RadioButton>();
		for (int i =0; i<30;i++) {
			RadioButton s = new RadioButton("group1",""+i);
			rb.add(s);
		}
	}

	public static SurvCurveWidge createPlot() {
		initial();
		plot.generateRandomData();
		return plot;
	}

	private void setData() {
		plot.getModel().addSeries("");
		plot.getModel().getHandlers().get(0).add(new DataPoint(0, 0));
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
		
		plotOptions.setGlobalSeriesOptions( new GlobalSeriesOptions()
        .setLineSeriesOptions( new LineSeriesOptions().setLineWidth(2)).setShadowSize(0) );
		
		plotOptions.setLegendOptions(new LegendOptions().setNumOfColumns(2).setBackgroundOpacity(0).setPosition(LegendPosition.NORTH_EAST));
		plotOptions.addXAxisOptions(new AxisOptions().setLabel("Survival Time (month)").setMinimum(0).setMaximum(maxTime+30));
		plotOptions.addYAxisOptions(new AxisOptions().setLabel("Probability of survival").setMinimum(0).setMaximum(1));
		plot = new SurvCurveWidge(model, plotOptions);
		Window.alert("ss");
		
	}
	

	@Override
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		 Window.alert("Hello World!");
	}


}
