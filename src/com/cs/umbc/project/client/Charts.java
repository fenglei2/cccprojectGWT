package com.cs.umbc.project.client;


import ca.nanometrics.gflot.client.SimplePlot;

import com.cs.umbc.project.client.widges.DendgWidge;
import com.cs.umbc.project.client.widges.HazardCurveWidge;
import com.cs.umbc.project.client.widges.SurvCurveWidge;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

//The widgets for two charts(survival curves and dendrogram) in "Group patients" tab

public class Charts extends Composite  {
	
	private VerticalPanel charts;
	public static SimplePlot plot;

	
	public Charts(){
		initWidget(getTheWidget());
	}

	private VerticalPanel getTheWidget() {
		if (charts == null) {
			charts = new VerticalPanel();
			charts.setWidth("100%");
			charts.setHeight("100%");
			charts.setSpacing(10);
			charts.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
			HorizontalPanel upper=new HorizontalPanel();
			upper.add(SurvCurveWidge.createEmptyPlot());
			upper.add(HazardCurveWidge.createEmptyPlot());
			for(int i=0;i<10;i++){
				upper.add(new HTML("&nbsp"));
			}
			charts.add(upper);
			charts.add(DendgWidge.createEmptyPlot());
			Button b = new Button("hello");
			b.addClickHandler(new MyClickHandler());
			charts.add(b);
		}
		return charts;
		
	}
	public VerticalPanel getCharts() {
		return charts;
	}
}
