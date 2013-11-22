package com.cs.umbc.project.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;


public class MyClickHandler implements ClickHandler
{
	   public void onClick(ClickEvent event) {
		      Window.alert("Hello World!");
	   }
}
