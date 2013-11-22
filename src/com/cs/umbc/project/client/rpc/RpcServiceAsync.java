package com.cs.umbc.project.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.cs.umbc.project.client.blobs.BlobData;
import com.cs.umbc.project.client.blobs.BlobDataFilter;
import com.cs.umbc.project.client.blobs.PlotData;

/**
 * The client side stub for the RPC service.
 */
public interface RpcServiceAsync {
	
	public void getBlobStoreUrl(AsyncCallback<String> callback);
	
	public void getBlobs(BlobDataFilter filter, AsyncCallback<BlobData[]> callback);
	 
	public void deleteBlob(BlobDataFilter filter, AsyncCallback<Boolean> callback);

	public void getContent(BlobDataFilter filter, AsyncCallback<String> callback);

	public void getPlotData(BlobDataFilter filter, AsyncCallback<PlotData> asyncCallback);

	public void getPopulateData(BlobDataFilter filter, AsyncCallback<String[]> asyncCallback);


	
}
