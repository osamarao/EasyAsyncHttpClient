package me.osama.easyasynchttpclient;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncHttpGetClient extends AsyncTask<String, Void, Boolean> {


	private String url;
	private ArrayList<BasicNameValuePair> parameters;
	private Handler handler;
	private Activity activity;
	private ProgressDialog pd;
	private String response;

	public AsyncHttpGetClient(Activity activity, String url, ArrayList<BasicNameValuePair> parameters, Handler handler) {
		
		String permission = "android.permission.INTERNET";          
		if (PackageManager.PERMISSION_GRANTED != activity.checkCallingOrSelfPermission(permission)){
			Log.e("AsyncHttpGetClient", "Please check if manifest contains android.permission.INTERNET");
			throw new RuntimeException("manifest must contain android.permission.INTERNET");
		}


		if ((url.contains("https://") || url.contains("http://"))){
			this.url = url;
		} else {
			Log.e("URL", "URL must contain http:// or https://");
			throw new RuntimeException("URL must contain http:// or https://");
		}

		this.activity = activity;		
		this.parameters = parameters;
		this.handler = handler;
	}

	@Override
	protected void onPreExecute() {		
		pd = new ProgressDialog(activity);
		pd.setMessage("Please Wait...");
		pd.setCancelable(false);
		pd.show();

		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		addParams();

		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get  = new HttpGet(url);
			ResponseHandler<String> handler = new BasicResponseHandler();
			response = client.execute(get, handler);
//			response = "response";
			return true;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			response = e.getMessage();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			response = e.getMessage();
			return false;
		}

	}
	/**
	 * 
	 * 
	 */
	@Override
	protected void onPostExecute(Boolean result) {
		pd.dismiss();


		Bundle data = new Bundle();
		if (result){
			data.putInt("error", 0);
		} else {
			data.putInt("error", 1);
		}
		data.putString("response", response);

		Message msg = new Message();
		msg.setData(data);
		Log.d("data", data.toString());
		
		handler.dispatchMessage(msg);

		super.onPostExecute(result);
	}

	private void addParams(){
		if (url.charAt(url.length()- 1) != "?".charAt(0))
			url = url + "?";

		for (int i = 0; i < parameters.size(); i++){
			url = url + parameters.get(i).getName() + "=" + parameters.get(i).getValue() + "&";
		}

		url = url.substring(0, url.length()-1);
		Log.e("URL: ", url);
	}

}
