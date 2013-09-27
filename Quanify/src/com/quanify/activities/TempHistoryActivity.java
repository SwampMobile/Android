package com.quanify.activities;

import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.quanify.R;

public class TempHistoryActivity extends BaseActivity 
{
	ProgressBar pBar;
	ClipDrawable drawable;
	ImageView batteryImage;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_device_temp_history);
//        batteryImage = (ImageView) findViewById(R.id.batteryImage);
//        pBar = (ProgressBar) findViewById(R.id.batteryStatus);
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pBar.getLayoutParams();
//        pBar.setLayoutParams(layoutParams);
//        pBar.setProgress(100);
       
    }
    
    @Override
    public void onBackPressed()
    {
    	super.onBackPressed();
    	overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    
}
