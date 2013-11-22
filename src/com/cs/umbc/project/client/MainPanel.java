package com.cs.umbc.project.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.dom.client.ClickHandler;

//This is the panel for "Group patients" tab

public class MainPanel extends Composite {
	
	private HorizontalPanel main;
	
	public MainPanel(){
		initWidget(getMainContent());
	}
	
	private HorizontalPanel getMainContent() {
		main = new HorizontalPanel();
		for (int i = 0; i < 2; i++) {
			main.add(new HTML());//0,1
		}
		VerticalPanel leftPanel = new VerticalPanel();

		FileUploader fuw = new FileUploader();
		Options opt = new Options();
		leftPanel.add(fuw);//2.0
		leftPanel.add(opt);//2.1
		

		main.setSpacing(15);
		main.add(leftPanel);// 2

		for (int i = 0; i < 5; i++) {
			main.add(new HTML());// 34567
		}

		Charts cw = new Charts();
		main.add(cw);//8
		return main;
	}
	



}
