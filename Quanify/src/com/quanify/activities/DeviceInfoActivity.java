package com.quanify.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quanify.R;
import com.quanify.app.MockData;
import com.quanify.models.DeviceRecord;

public class DeviceInfoActivity extends BaseActivity 
{
	private DeviceRecord deviceInfo;
	
	private TextView deviceId;
	private TextView currentTemp;
	private TextView deviceName;
	private TextView deviceStatus;
	private TextView chargeText;
	private ProgressBar batteryImg;
	
	private ViewGroup locations;
	
	private Button tempHistory;
	private Button editRecord;
	private Button resetRecord;
	
	private int batteryProgress = 30;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_device_info);
        
        String deviceId = this.getIntent().getStringExtra("device_id");
        deviceInfo = MockData.getInstance().getDeviceById(deviceId);
        
        this.deviceId = (TextView) findViewById(R.id.device_id);
        this.deviceId.setText('#' + deviceInfo.id);
        
//        this.deviceStatus = (TextView) findViewById(R.id.device_status);
//        Log.d("DeviceInfoActivity", "Status: " + deviceInfo.status);
//        switch(deviceInfo.status)
//        {
//        	case LOW: 
//        		this.deviceStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_bk_blue)); 
//        		this.deviceStatus.setText("\u00B0F Low");
//        		break;
//        	case NORMAL: 
//        		this.deviceStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_bk_green));
//        		this.deviceStatus.setText("\u00B0F Normal");
//        		break;
//        	case HIGH: 
//        		this.deviceStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_bk_red)); 
//        		this.deviceStatus.setText("\u00B0F High");
//        		break;
//        }
        
        this.chargeText = (TextView) findViewById(R.id.chargeText);
        this.batteryImg = (ProgressBar) findViewById(R.id.batteryStatus);
         
        if(batteryProgress > 20 && batteryProgress < 40) {
        	batteryImg.setProgressDrawable(getResources().getDrawable(R.drawable.battery_status_ok));
        }else if(batteryProgress > 40 ) {
        	batteryImg.setProgressDrawable(getResources().getDrawable(R.drawable.battery_status_good));
        }else {
        	batteryImg.setProgressDrawable(getResources().getDrawable(R.drawable.battery_status_low));
        }
        chargeText.setText(""+batteryProgress+"%");
        batteryImg.setProgress(batteryProgress); 
        
        
        this.currentTemp = (TextView) findViewById(R.id.temp);
        this.currentTemp.setText(String.format("%.0f",deviceInfo.ambTemp) + '\u00B0' + 'F');
        
        this.deviceName = (TextView) findViewById(R.id.device_name);
        this.deviceName.setText(deviceInfo.name);
        
        this.locations = (ViewGroup) findViewById(R.id.locations);
        this.locations.removeAllViews();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup locationItem;
        DateFormat dateFormat = new SimpleDateFormat("H:m - M/F/y", Locale.ENGLISH);
        for(int i = 0; i < deviceInfo.locationHistory.size(); ++i)
        {
        	locationItem = (ViewGroup) inflater.inflate(R.layout.listitem_locationandtime, null);
        	((TextView)locationItem.findViewById(R.id.location)).setText(deviceInfo.locationHistory.get(i).location);
        	((TextView)locationItem.findViewById(R.id.date_time)).setText( dateFormat.format(deviceInfo.locationHistory.get(i).timestamp) );
        	this.locations.addView(locationItem);
        }
        
        tempHistory = (Button) findViewById(R.id.temp_history);
        tempHistory.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				startActivity(new Intent(DeviceInfoActivity.this, TempHistoryActivity.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
        });
        
        editRecord = (Button) findViewById(R.id.edit_record);
        editRecord.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				showSimpleAlert("Sorry", "Selection not available at this time.");
			}
        });
        
        resetRecord = (Button) findViewById(R.id.reset_record);
        resetRecord.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				showSimpleAlert("Sorry", "Selection not available at this time.");
			}
        });
    }

    @Override
    public void onBackPressed()
    {
    	super.onBackPressed();
    	overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
