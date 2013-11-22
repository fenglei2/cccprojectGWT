package com.cs.umbc.project.client;

import com.cs.umbc.project.client.widges.CommonWidge;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Ran Qi
 * 
 * This is the login page
 *
 */

public class Login extends Composite {

	private VerticalPanel pWidget;//This widget contains the following 3 items:username,password and submit button
	private TextBox username;
	private PasswordTextBox password;
	private PushButton submitButton;
	
	public Login() {
		initWidget(getTheWidget());
	}

	private Widget getTheWidget() {
		if (pWidget == null) {
			pWidget = new VerticalPanel();
			pWidget.setSpacing(10);
			HorizontalPanel p1=new HorizontalPanel();
			HorizontalPanel p2=new HorizontalPanel();
			p1.add(new HTML("Username:&nbsp&nbsp"));
			p1.add(getUsername());
			pWidget.add(p1);
			p2.add(new HTML("Password:&nbsp&nbsp&nbsp"));
			p2.add(getPassword());
			pWidget.add(p2);
			pWidget.add(getButton());
		}
		return pWidget;
	}

	private PushButton getButton() {
		submitButton=new PushButton();
		submitButton.setWidth("50px");
		submitButton.setHTML("&nbsp&nbspLogin");
		
		
		//after click submit button
		submitButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				//if the entered username&password are correct, go to main panel
				if(username.getText().equals("admin")&&password.getText().equals("admin")){
					RootPanel.get().getWidget(0).setVisible(false);
					showMainPanel();
				}else{//otherwise, alert
					Window.alert("Wrong username or password");
				}
				
			}


		});
		return submitButton;
	}
	
	
	//The main panel has two tabs
	//one of tabs shows the interface for grouping the patients
	//another tab shows the interface for predicting the survival time 
	private void showMainPanel() {
		
		
		TabPanel tabPanel=new TabPanel();
		RootPanel.get().add(tabPanel);
		tabPanel.add(getMainContent(),"Group Patients");//0
		tabPanel.add(new PredictPanel(),"Predict Survival Time");//1
		tabPanel.selectTab(0);
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if (event.getSelectedItem() == 1) {
					
				      PredictPanel prePanel=CommonWidge.getPrePanel();
				      prePanel.setFactors();
				}
				
			}
		});
		
		
	}
		

	private HorizontalPanel getMainContent() {
		HorizontalPanel main = new HorizontalPanel();
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

	private PasswordTextBox getPassword() {
		password = new PasswordTextBox();
		password.setWidth("150px");
		return password;
	}

	private TextBox getUsername() {
		username=new TextBox();
		username.setWidth("150px");
		return username;
	}

}
