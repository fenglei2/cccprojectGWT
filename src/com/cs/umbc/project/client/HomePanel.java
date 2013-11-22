package com.cs.umbc.project.client;

import com.cs.umbc.project.client.widges.ExtRadioButton;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * 
 * @author Ran
 * 
 */
public class HomePanel implements EntryPoint {

	public void onModuleLoad() {

		Login login=new Login();
		RootPanel rootPanel = RootPanel.get();
		rootPanel.add(login);
		
		
		

		
		
	}
}
