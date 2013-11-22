package com.cs.umbc.project.client;

import com.cs.umbc.project.client.blobs.BlobDataFilter;
import com.cs.umbc.project.client.blobs.PlotData;
import com.cs.umbc.project.client.rpc.RpcInit;
import com.cs.umbc.project.client.rpc.RpcServiceAsync;
import com.cs.umbc.project.client.widges.CommonWidge;
import com.cs.umbc.project.client.widges.DendgWidge;
import com.cs.umbc.project.client.widges.SurvCurveWidge;
import com.cs.umbc.project.client.widges.SurvSingleCurve;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.sun.java.swing.plaf.windows.resources.windows;
import com.cs.umbc.project.client.widges.HazardCurveWidge;

//One of the widgets(select factors, time, and censor indicator) for "Group patients" tab

public class Options extends Composite {

	private RpcServiceAsync rpc;
	private VerticalPanel pWidget;
	private TextBox text;
	private ListBox factorBox;
	private ListBox timeDropBox;
	private ListBox censorDropBox;
	private Button btnSubmit;
	private Button control;
	private ListBox comboBox;

	public VerticalPanel getpWidget() {
		return pWidget;
	}

	public Options() {
		initWidget(getTheWidget());
		rpc = RpcInit.init();
	}

	private VerticalPanel getTheWidget() {
		if (pWidget == null) {
			pWidget = new VerticalPanel();
			pWidget.add(new HTML("Select factors"));// 0
			pWidget.setSpacing(10);
			pWidget.add(multiBox());// 1
			pWidget.add(new HTML("Select time"));// 2
			pWidget.add(timeDropBox());// 3
			pWidget.add(new HTML("Select censor"));// 4
			pWidget.add(censorDropBox());// 5
			pWidget.add(new HTML("Elimination cutoff"));// 6
			pWidget.add(cutoff());
			{
				btnSubmit = new Button("submit");
				btnSubmit.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						// user select variables
						
						int n = factorBox.getItemCount();
						if (n == 0) {
							Window.alert("No variables, check the data file.");
						}

						int[] selectItems = new int[n];
						for (int i = 0; i < n; i++) {
							selectItems[i] = -1;
						}
						int k = 0;
						int time = -1, censor = -1;
						String factors="";
						for (int i = 0; i < n; i++) {
							if (factorBox.isItemSelected(i)) {
								selectItems[k++] = i;
								factors+=factorBox.getItemText(i)+",";
							} else if (timeDropBox.isItemSelected(i)) {
								time = i;
							} else if (censorDropBox.isItemSelected(i)) {
								censor = i;
							}
						}
						factorBox.setName(factors);
						btnSubmit.setStyleName(factors);
					//	System.out.println("factors str: "+submitButton.getStyleName());
						

						String txt = text.getText();

						if (k == 0) {
							Window.alert("Select one or more factors");
						} else if (time == -1) {
							Window.alert("Time is wrong");
						} else if (censor == -1) {
							Window.alert("Censor is wrong");
						} else if (txt.isEmpty() || !isInteger(txt)) {
							Window.alert("Enter a number for cutoff");
						} else {

							System.out.print("selected values: ");
							for (int i = 0; i < n; i++) {
								System.out.print(selectItems[i] + " ");
							}
							System.out.println();
							System.out.println("selected time: " + time);
							System.out.println("selected censor: " + censor);

							int minNo = Integer.valueOf(text.getText());
							System.out.println("minno " + minNo);

							btnSubmit.setEnabled(false);
							btnSubmit.setHTML("Waiting");
							// send user's selection to server and draw plots
							showPlot(selectItems, time, censor, minNo);
						}
					}
				});
				control = new Button("Control");
				control.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						// user select variables
						
						int n = factorBox.getItemCount();
						if (n == 0) {
							Window.alert("No variables, check the data file.");
						}

						int[] selectItems = new int[n];
						for (int i = 0; i < n; i++) {
							selectItems[i] = -1;
						}
						int k = 0;
						int time = -1, censor = -1;
						String factors="";
						for (int i = 0; i < n; i++) {
							if (factorBox.isItemSelected(i)) {
								selectItems[k++] = i;
								factors+=factorBox.getItemText(i)+",";
							} else if (timeDropBox.isItemSelected(i)) {
								time = i;
							} else if (censorDropBox.isItemSelected(i)) {
								censor = i;
							}
						}
						factorBox.setName(factors);
						btnSubmit.setStyleName(factors);
					//	System.out.println("factors str: "+submitButton.getStyleName());
						

						String txt = text.getText();

						if (k == 0) {
							Window.alert("Select one or more factors");
						} else if (time == -1) {
							Window.alert("Time is wrong");
						} else if (censor == -1) {
							Window.alert("Censor is wrong");
						} else if (txt.isEmpty() || !isInteger(txt)) {
							Window.alert("Enter a number for cutoff");
						} else {

							System.out.print("selected values: ");
							for (int i = 0; i < n; i++) {
								System.out.print(selectItems[i] + " ");
							}
							System.out.println();
							System.out.println("selected time: " + time);
							System.out.println("selected censor: " + censor);

							int minNo = Integer.valueOf(text.getText());
							System.out.println("minno " + minNo);

							btnSubmit.setEnabled(false);
							btnSubmit.setHTML("Waiting");
							
							int i = comboBox.getSelectedIndex();
							int m =SurvCurveWidge.getCurves().size();
							Window.alert(""+m);
							// send user's selection to server and draw plots
							showSinglePlot(selectItems, time, censor, minNo, i);
						}
					}
				});
				pWidget.add(btnSubmit);//10
				pWidget.add(control);
			}
			{
				comboBox = new ListBox();
				comboBox.addItem("1");
				comboBox.addItem("2");
				comboBox.addItem("3");
				comboBox.addItem("4");
				comboBox.addItem("5");
				comboBox.addItem("6");
				comboBox.addItem("7");
				comboBox.addItem("8");
				comboBox.addItem("9");
				comboBox.addItem("10");
				comboBox.addItem("11");
				comboBox.addItem("12");
				comboBox.addItem("13");
				comboBox.addItem("14");
				comboBox.addItem("15");
				comboBox.addItem("16");
				comboBox.addItem("17");
				comboBox.addItem("18");
				comboBox.addItem("19");
				comboBox.addItem("20");
				pWidget.add(comboBox);
				
			}
		}
		return pWidget;
	}



	public boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void showPlot(int[] selectItems, int time, int censor, int minNo) {

//		VerticalPanel options = CommonWidge.getOptions();
//		ListBox factorBox = (ListBox) options.getWidget(1);
		String blobKey = factorBox.getStyleName();

		BlobDataFilter filter = new BlobDataFilter();
		filter.setSelectItems(selectItems);
		filter.setBlobKey(blobKey);
		filter.setCensorCol(censor);
		filter.setTimeCol(time);
		filter.setMinPatientsNo(minNo);

		System.out.println("key: " + blobKey);
		rpc.getPlotData(filter, new AsyncCallback<PlotData>() {

			@Override
			public void onSuccess(PlotData result) {
				VerticalPanel charts = CommonWidge.getCharts();
				charts.clear();
				
				HorizontalPanel upper=new HorizontalPanel();
				upper.add(SurvCurveWidge.createPlot(result.getCurves(), result.getMaxSurvivalTime()));
				upper.add(HazardCurveWidge.createPlot(result.getCurves(), result.getMaxSurvivalTime()));
				for(int i=0;i<10;i++){
					upper.add(new HTML("&nbsp"));
				}
				charts.add(upper);
				charts.add(DendgWidge.createPlot(result.getRoot()));
				
				btnSubmit.setEnabled(true);
				btnSubmit.setHTML("Submit");
			}	

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("getPlotData error: " + caught);

			}

		});

	}
	private void showSinglePlot(int[] selectItems, int time, int censor, int minNo, final int i) {

//		VerticalPanel options = CommonWidge.getOptions();
//		ListBox factorBox = (ListBox) options.getWidget(1);
		String blobKey = factorBox.getStyleName();

		BlobDataFilter filter = new BlobDataFilter();
		filter.setSelectItems(selectItems);
		filter.setBlobKey(blobKey);
		filter.setCensorCol(censor);
		filter.setTimeCol(time);
		filter.setMinPatientsNo(minNo);

		System.out.println("key: " + blobKey);
		rpc.getPlotData(filter, new AsyncCallback<PlotData>() {

			@Override
			public void onSuccess(PlotData result) {
				VerticalPanel charts = CommonWidge.getCharts();
				charts.clear();
				
				HorizontalPanel upper=new HorizontalPanel();
				upper.add(SurvSingleCurve.createPlot(result.getCurves(), result.getMaxSurvivalTime(),i));
				upper.add(HazardCurveWidge.createPlot(result.getCurves(), result.getMaxSurvivalTime()));
				for(int i=0;i<10;i++){
					upper.add(new HTML("&nbsp"));
				}
				charts.add(upper);
				charts.add(DendgWidge.createPlot(result.getRoot()));
				
				btnSubmit.setEnabled(true);
				btnSubmit.setHTML("Submit");
			}	

			@Override
			
			public void onFailure(Throwable caught) {
				System.out.println("getPlotData error: " + caught);

			}

		});

	}


	private ListBox multiBox() {
		factorBox = new ListBox(true);
		factorBox.setWidth("11em");
		factorBox.setVisibleItemCount(5);
		System.out.println("factorbox's name: "+factorBox.getName());
		return factorBox;
	}

	private ListBox timeDropBox() {
		timeDropBox = new ListBox(false);
		return timeDropBox;
	}

	private ListBox censorDropBox() {
		censorDropBox = new ListBox(false);
		return censorDropBox;
	}

	private TextBox cutoff() {
		text = new TextBox();
		text.setText("100");
		return text;
	}

}
