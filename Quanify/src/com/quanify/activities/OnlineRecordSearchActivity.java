package com.quanify.activities;

import android.os.Bundle;
import android.webkit.WebView;

import com.quanify.R;

public class OnlineRecordSearchActivity extends BaseActivity
{
	private WebView webView;
	
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.activity_online_record_search);
		
		webView = (WebView)findViewById(R.id.webview);
		webView.loadUrl("http://www.commnsense.info/wmccombie/CNS/ListShipments.php");
	}
}
