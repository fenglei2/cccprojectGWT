package com.cs.umbc.project.client;

import java.util.List;
import com.cs.umbc.project.client.widges.CommonWidge;
import com.cs.umbc.project.client.widges.SurvCurveWidge;
import com.cs.umbc.project.client.widges.SurvCurveWidgePre;
import com.cs.umbc.project.shared.Curve;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

//The panel for "Predict survival time" tab

public class PredictPanel extends Composite {

	
	private HorizontalPanel pWidget;
	private PushButton submitButton;
	private int factorNum;
	private String[] factorArr;
	private VerticalPanel leftPanel;
	private Grid grid;

	public PredictPanel() {
		initWidget(getTheWidget());
	}

	private HorizontalPanel getTheWidget() {
		if (pWidget == null) {
			pWidget = new HorizontalPanel();
			pWidget.setSpacing(15);
			setFactors();
		}
		return pWidget;
	}

	public String setFactors() {
		pWidget.clear();
		leftPanel = new VerticalPanel();
		pWidget.add(leftPanel);// 0
		pWidget.add(SurvCurveWidgePre.createEmptyPlot());// 1
		leftPanel.setSpacing(10);

		VerticalPanel options = CommonWidge.getOptions();
		ListBox factorBox = (ListBox) options.getWidget(1);
		String factors = factorBox.getName();
		System.out.println("factors resturned: " + factors);

		if (factors.equals("")) {
			leftPanel.add(new HTML("No factor selected"));
		} else {

			factorArr = factors.split(",");
			factorNum = factorArr.length;
			leftPanel.setStyleName(String.valueOf(factorNum));// save factor
																// number

			grid = new Grid(factorNum, 3);
			leftPanel.add(grid);
			for (int i = 0; i < factorNum; i++) {
				grid.setWidget(i, 0, new HTML(factorArr[i]));
				grid.setWidget(i, 1, new TextBox());
				String unit = translateFactorUnit(factorArr[i]);
				System.out.println("factorunit: " + unit);
				grid.setWidget(i, 2, new HTML(unit));
			}

			leftPanel.add(getButton());
		}

		return factors;
	}

	private PushButton getButton() {
		submitButton = new PushButton();
		submitButton.setWidth("50px");
		submitButton.setHTML("&nbspSubmit");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int[] factorCodes = new int[factorNum];
				double[] factorValues = new double[factorNum];
				for (int i = 0; i < factorNum; i++) {

					factorCodes[i] = translateFactorToCode(factorArr[i]);

					TextBox text = (TextBox) grid.getWidget(i, 1);
					double value = Double.parseDouble(text.getText());
					if(value<0){
						Window.alert("Cannot be negative value");
					}
					factorValues[i] = value;
				}

				String label = getLabel(factorCodes, factorValues);
				System.out.println("new label name: " + label);
				drawCurve(label);
			}

		});
		return submitButton;
	}

	private String translateFactorUnit(String factor) {
		if (factor.equalsIgnoreCase("size")) {
			return "cm";
		} else if (factor.equalsIgnoreCase("nodes")) {
			return "number";
		}

		return "invalid";
	}

	private void drawCurve(String label) {
		if (pWidget.getWidget(1) != null) {
			pWidget.getWidget(1).removeFromParent();
		}
		List<Curve> allCurves = SurvCurveWidge.getCurves();
		boolean find=false;
		for (Curve curve : allCurves) {
//			System.out.println("exist label: " + curve.getLabel());
			if (curve.getLabel().equals(label)) {
				pWidget.add(SurvCurveWidgePre.createPlot(curve, 50));
				find=true;
				break;
			}
		}
		if(!find){
			pWidget.add(SurvCurveWidgePre.createEmptyPlot());
		}

	}

	private int translateFactorToCode(String factor) {
		if (factor.equalsIgnoreCase("size")) {
			return 0;
		} else if (factor.equalsIgnoreCase("nodes")) {
			return 1;
		}
		return -1;
	}

	String levelHint = "";

	private String getLabel(int[] factorCodes, double[] values) {
		if (!levelHint.isEmpty()) {
			System.out.println("remove hint");
			leftPanel.getWidget(leftPanel.getWidgetCount() - 1).removeFromParent();
		}
		String label = "";
		levelHint = "The corresponding combination is: \n";
		for (int i = 0; i < factorCodes.length; i++) {
			int level = getLevel(factorCodes[i], values[i]);

			System.out.println("level: "+level+" "+"factorcode: " + factorCodes[i] + " factorvalue: " + values[i]);
			
			label += level;
			levelHint += factorArr[i] + "=" + level + "\n";
		}

		leftPanel.add(new HTML(levelHint));
		return label;
	}

	private int getLevel(int factorCode, double value) {

		int level = -1;
		switch (factorCode) {
		case 0:// T
			if (value < 0) {
				System.err.println("Value cannot be negtive");
			} else if (value <= 2) {
				level = 1;
			} else if (value <= 5) {
				level = 2;
			} else {
				level = 3;
			}
			break;

		case 1:
			if (value < 0) {
				System.err.println("Value cannot be negtive");
			} else if (value == 0) {
				level = 1;
			} else if (value <= 3) {
				level = 2;
			} else if (value <= 10) {
				level = 3;
			} else {
				level = 4;
			}
			break;
		}
		return level;
	}

}
