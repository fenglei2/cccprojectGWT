package com.cs.umbc.project.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Widget;
import com.cs.umbc.project.client.Blobs;
import com.cs.umbc.project.client.rpc.RpcInit;
import com.cs.umbc.project.client.rpc.RpcServiceAsync;
import com.cs.umbc.project.client.widges.CommonWidge;

//The widget for uploading the data file in "Group patients" tab

public class FileUploader extends Composite {

	private RpcServiceAsync rpc;

	private VerticalPanel pWidget;
	private FormPanel form;
	private VerticalPanel formElements;
	private FileUpload fileUpload;
	private Button uploadbutton;
	private Button clearButton;
	private Blobs blobs;

	

	/**
	 * constructor - init widget
	 */
	public FileUploader() {

		initWidget(getTheWidget());

		// get url needed for blobstore
		rpc = RpcInit.init();

		// get the form url for the blobstore
		getFormUrl();
	}

	private VerticalPanel getTheWidget() {
		if (pWidget == null) {
			pWidget = new VerticalPanel();
			pWidget.add(getFormPanel());
			// pWidget.add(getPResponse());
		}
		return pWidget;
	}

	private FormPanel getFormPanel() {
		if (form == null) {
			form = new FormPanel();
			form.setAction("/upload");
			form.setEncoding(FormPanel.ENCODING_MULTIPART);
			form.setMethod(FormPanel.METHOD_POST);
			form.setWidget(getFormElements());

			// add submit handler
			form.addSubmitHandler(new SubmitHandler() {
				public void onSubmit(SubmitEvent event) {
					System.out.println("onSubmit");
					String fileName = fileUpload.getFilename();
					if (fileName.length() == 0) {
						Window.alert("Did you select a file?");
						event.cancel();
					} else if (!fileName.endsWith(".txt")) {
						Window.alert("Please upload a txt file");
						event.cancel();
					} else {
						uploadbutton.setEnabled(false);
						uploadbutton.setHTML("uploading");
					}

				}
			});

			// add submit complete handler
			form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
				public void onSubmitComplete(SubmitCompleteEvent event) {
					System.out.println("onSubmitComplete");
					uploadbutton.setHTML("upload");
					uploadbutton.setEnabled(true);

					String results = event.getResults();
					System.out.println("result: " + results);
					blobs.draw();

					getFormUrl();
				}
			});

		}
		return form;
	}

	private VerticalPanel getFormElements() {
		if (formElements == null) {
			formElements = new VerticalPanel();
			formElements.setSize("100%", "100%");
			formElements.add(getFileUpload());
			HorizontalPanel buttons = new HorizontalPanel();
			buttons.add(getButton());
			buttons.add(getClearButton());
			buttons.setSpacing(5);
			formElements.add(buttons);
			formElements.add(getBlobs());
		}
		return formElements;
	}

	private Widget getBlobs() {
		if (blobs == null) {
			blobs = new Blobs();
			blobs.draw();
			formElements.setCellHorizontalAlignment(blobs, VerticalPanel.ALIGN_CENTER);
		}
		return blobs;
	}

	private FileUpload getFileUpload() {
		if (fileUpload == null) {
			fileUpload = new FileUpload();
			fileUpload.setName("myFile");
		}
		return fileUpload;
	}

	private Button getClearButton() {
		if (clearButton == null) {
			clearButton = new Button("Clear plot");

			clearButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					CommonWidge.clearCharts();

				}
				
			});		

		}
		return clearButton;
	}

	private Button getButton() {
		if (uploadbutton == null) {
			uploadbutton = new Button("Upload");
			uploadbutton.setEnabled(false);

			uploadbutton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					System.out.println("upload button clicked");
					form.submit();
				}
			});

		}
		return uploadbutton;
	}

	private void getFormUrl() {
		System.out.println("jjjjjjjjjjjjjjjjjj");

		rpc.getBlobStoreUrl(new AsyncCallback<String>() {
			public void onSuccess(String url) {
				System.out.println("retrieved url for blob store: " + url);
				form.setAction(url);
				uploadbutton.setEnabled(true);
			}

			public void onFailure(Throwable caught) {
				Window.alert("Something went wrong with the rpc call.");
			}
		});

	}
}
