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
import android.widget.ImageButton;

public class MainActivity extends Activity 
{
	private ImageButton localDevices;
	private ImageButton pastRecordsButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        localDevices = (ImageButton) findViewById(R.id.local_devices);
        localDevices.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				startActivity(new Intent(MainActivity.this, LocalDevicesActivity.class));
			}
        });
        
        pastRecordsButton = (ImageButton) findViewById(R.id.past_records);
        pastRecordsButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				startActivity(new Intent(MainActivity.this, DatabaseSearchActivity.class));
			}
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
