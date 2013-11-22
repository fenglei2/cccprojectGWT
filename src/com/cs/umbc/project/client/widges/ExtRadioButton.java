package com.cs.umbc.project.client.widges;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
public class ExtRadioButton extends RadioButton {
	
	
    public ExtRadioButton(String name, String label) {
    	
    	
        super(name, label);
        // TODO Auto-generated constructor stub
        RadioButton RB  = new RadioButton(name, label);
        RB.addClickHandler(new ClickHandler(){
			  @Override
			   public void onClick(ClickEvent event) {
				  Window.alert("23");
			   }

		  });
        RB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				Window.alert("123");
			}
		});
        RB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				Window.alert("s");
			}
		});
    }

    public void messageb(String value)
    {
     
     }

}