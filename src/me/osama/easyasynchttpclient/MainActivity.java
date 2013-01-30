package me.osama.easyasynchttpclient;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Menu;

public class MainActivity extends Activity {

    private Handler handler;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this should change bin
        
        System.out.println("yea!");
        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
//        parameters.add(new BasicNameValuePair("name", "osama"));
//        parameters.add(new BasicNameValuePair("name", "tooba"));
//        parameters.add(new BasicNameValuePair("name", "daania"));
//        parameters.add(new BasicNameValuePair("name", "minahil"));
        
        handler = new Handler(new Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				Bundle data = msg.getData();
				if (data.getInt("error") == 1){
					//XXX ERROR HANDLING
					
					
					return false;
				} else if (data.getInt("error") == 0){
					//XXX NORMAL WORK
					// use activity.runOnUIThread() for UI calls
					
					final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
					.setMessage(data.getString("response"))
					.setPositiveButton("OK", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).create();
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							alertDialog.show();
						}
					});
					return false;
				}
				
				return false;
			}
		});
        String url = "http://petarosoft.com/android/samplejson";
        
        AsyncHttpGetClient asyncHttpGetClient = new AsyncHttpGetClient(MainActivity.this, url, parameters , handler);
        
        AsyncHttpPostClient asyncHttpPostClient = new AsyncHttpPostClient(MainActivity.this, url, parameters , handler);
//        asyncHttpGetClient.execute("");
        
        asyncHttpPostClient.execute("");
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
