	package com.cs.umbc.project.client.widges;

import com.cs.umbc.project.client.Charts;
import com.cs.umbc.project.client.Options;
import com.cs.umbc.project.client.PredictPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommonWidge {
	
	public static VerticalPanel getCharts(){
		TabPanel tabPanel=(TabPanel)RootPanel.get().getWidget(1);
		HorizontalPanel main=(HorizontalPanel)tabPanel.getWidget(0);
		Charts chartWidge=(Charts)main.getWidget(8);
		VerticalPanel charts=chartWidge.getCharts();
		return charts;
	}
	
	public static PredictPanel getPrePanel(){
		TabPanel tabPanel=(TabPanel)RootPanel.get().getWidget(1);
		PredictPanel prePanel=(PredictPanel)tabPanel.getWidget(1);
		return prePanel;
	}
	
	public static VerticalPanel getOptions(){
		//remember the order of widgets in its parent widget
		
		//rootpanel:0->login widge,1->tabpanel
		TabPanel tabPanel=(TabPanel)RootPanel.get().getWidget(1);
		//tabpanel:0->main, 1->predictnew
		HorizontalPanel main=(HorizontalPanel)tabPanel.getWidget(0);
		//main:0,1->space, 2->leftpanel
		VerticalPanel leftPanel=(VerticalPanel)main.getWidget(2);
		//leftpanel: 0->uploader  1->options
		Options optWidge=(Options)leftPanel.getWidget(1);//option widget
		return optWidge.getpWidget();
	}
	
	public static void clearCharts(){
		VerticalPanel charts=getCharts();
		charts.clear();
		charts.add(SurvCurveWidge.createEmptyPlot());
		charts.add(HazardCurveWidge.createEmptyPlot());
		charts.add(DendgWidge.createEmptyPlot());
	}

}
