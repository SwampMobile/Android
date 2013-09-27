package com.quanify.activities;

import com.quanify.R;
import com.quanify.R.id;
import com.quanify.R.layout;
import com.quanify.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DatabaseSearchActivity extends BaseActivity 
{
	private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_search);
        
        searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				showLoadingDialog("", "Searching Database...");
				
				(new Thread(new Runnable() 
				{
					@Override
					public void run() 
					{
						// Hold for 1.5 seconds
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						// Dismiss loading dialog
						runOnUiThread(new Runnable() 
						{
							@Override
							public void run() 
							{
								hideLoadingDialog();
								
								startActivity(new Intent(DatabaseSearchActivity.this, DatabaseSearchResultsActivity.class));
							}
						});
					}
				})).start();
			}
        });
    }

    
}
