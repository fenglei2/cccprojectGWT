package com.cs.umbc.project.client.widges;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.cs.umbc.project.client.blobs.BlobDataFilter;
import com.cs.umbc.project.client.blobs.PlotData;
import com.cs.umbc.project.client.rpc.RpcInit;
import com.cs.umbc.project.client.rpc.RpcServiceAsync;
import com.cs.umbc.project.shared.Curve;

public class SelectWidget extends Composite implements ClickHandler {


	private RpcServiceAsync rpc;
	private PushButton selectButton = new PushButton("Select");
	private String blobKey;
	

	public SelectWidget(String key) {
		this.blobKey = key;
		initWidget(selectButton);
		selectButton.addClickHandler(this);
		
		rpc = RpcInit.init();

	}

	@Override
	public void onClick(ClickEvent event) {
		selectButton.setEnabled(false);
		selectButton.setHTML("Wait");
		Widget sender = (Widget) event.getSource();
		if (sender == selectButton) {
			VerticalPanel options=CommonWidge.getOptions();
			ListBox factorBox=(ListBox) options.getWidget(1);
			factorBox.setStyleName(blobKey);
			ListBox timeDropBox=(ListBox) options.getWidget(3);
			ListBox censorDropBox=(ListBox) options.getWidget(5);
			factorBox.clear();
			timeDropBox.clear();
			censorDropBox.clear();
			
			populateData();
			
		}
	}
	
	
	private void populateData() {
		BlobDataFilter filter = new BlobDataFilter();
		filter.setBlobKey(blobKey);
		
		rpc.getPopulateData(filter, new AsyncCallback<String[]>(){

			@Override
			public void onSuccess(String[] result) {
				//result has all factors, 
				System.out.print("factor returned: ");
				
				for(String s:result){
					System.out.print(s+",");
				}
				System.out.println();
				
				//populate data to options
				VerticalPanel options=CommonWidge.getOptions();
				ListBox factorBox=(ListBox) options.getWidget(1);
				ListBox timeDropBox=(ListBox) options.getWidget(3);
				ListBox censorDropBox=(ListBox) options.getWidget(5);
				
				for(String factor:result){
					factorBox.addItem(factor);
					timeDropBox.addItem(factor);
					censorDropBox.addItem(factor);
				}
				
				selectButton.setEnabled(true);
				selectButton.setHTML("Select");
				PushButton submitButton=(PushButton) options.getWidget(9);
				submitButton.setEnabled(true);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("POPULATE DATA ERROR: "+caught);
			}
			
		});
		
	}
	
	private void checkresult(PlotData result) {
		
		System.out.println("maxtimeeee: "+result.getMaxSurvivalTime());
		
		Curve[] curves=result.getCurves();
		for(Curve c:curves){
			System.out.print("label: "+c.getLabel());
			for(int i=0;i<c.getTime().length/10;i++){
				System.out.print(" "+c.getTime()[i]+" "+c.getProbabilityOfSurvival()[i]);
			}
			
			System.out.println();
			
		}
		
	}

}
