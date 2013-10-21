package com.quanify.activities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quanify.R;
import com.quanify.app.DeviceList;
import com.quanify.changenotificationobjects.BatteryStatusChangeObject;
import com.quanify.changenotificationobjects.ChangeNotificationObject;
import com.quanify.changenotificationobjects.MemoryStatusChangeObject;
import com.quanify.changenotificationobjects.TemperatureChangeObject;
import com.quanify.models.DeviceRecord;

public class DeviceInfoActivity extends BaseActivity implements PropertyChangeListener
{
	private DeviceRecord deviceInfo;

	private TextView deviceId;
	private TextView currentTemp;
	private TextView deviceName;
	private TextView deviceStatus;
	//private TextView chargeText;
	private ProgressBar batteryProgress;
	private TextView tagMin;
	private TextView tagAvg;
	private TextView tagMax;
	private TextView probeMin;
	private TextView probeAvg;
	private TextView probeMax;

	//private TextView memoryStatusText;
	//private ProgressBar memoryProgress;

	private ViewGroup locations;

	private Button viewData;
	private Button settings;
	private Button resetRecord;
	DecimalFormat twoDForm = new DecimalFormat("#.#");
	
	Dialog dialog; 

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_device_info);

		String deviceId = this.getIntent().getStringExtra("device_id");
		deviceInfo = DeviceList.getInstance().getDeviceById(deviceId);
		deviceInfo.addPropertyChangeListener(this);
		this.deviceId = (TextView) findViewById(R.id.device_id);
		this.deviceId.setText('#' + deviceInfo.id);

		//        this.deviceStatus = (TextView) findViewById(R.id.device_status);
		//        Log.d("DeviceInfoActivity", "Status: " + deviceInfo.status);
		//        switch(deviceInfo.status)
		//        {
		//        	case LOW: 
		//        		this.deviceStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_bk_blue)); 
		//        		this.deviceStatus.setText("\u00B0C Low");
		//        		break;
		//        	case NORMAL: 
		//        		this.deviceStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_bk_green));
		//        		this.deviceStatus.setText("\u00B0C Normal");
		//        		break;
		//        	case HIGH: 
		//        		this.deviceStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_bk_red)); 
		//        		this.deviceStatus.setText("\u00B0C High");
		//        		break;
		//        }

		//this.chargeText = (TextView) findViewById(R.id.chargeText);
		this.batteryProgress = (ProgressBar) findViewById(R.id.batteryStatus);

		setBatteryStatus(deviceInfo.getBatteryProgress());

		//this.memoryStatusText = (TextView) findViewById(R.id.memoryText);
		//this.memoryProgress = (ProgressBar) findViewById(R.id.memoryStatus);

		//setMemoryStatus(deviceInfo.getMemoryProgress());

		SpannableStringBuilder spanTxt = new SpannableStringBuilder(String.format("%.0f",deviceInfo.ambTemp));
		spanTxt.append("\u00B0C");
		//make the textsize 2 times.
		spanTxt.setSpan(new RelativeSizeSpan(0.5f), spanTxt.length() - "\u00B0C".length(), spanTxt.length(), 0  );
		this.currentTemp = (TextView) findViewById(R.id.temp);
		this.currentTemp.setText(spanTxt);

		this.tagMin = (TextView) findViewById(R.id.tagMin);
		spanTxt = new SpannableStringBuilder("35");
		spanTxt.append("\u00B0C");
		//make the textsize 2 times.
		spanTxt.setSpan(new RelativeSizeSpan(0.5f), spanTxt.length() - "\u00B0C".length(), spanTxt.length(), 0  );
		this.tagMin.setText(spanTxt );
		this.tagAvg = (TextView) findViewById(R.id.tagAvg);
		spanTxt = new SpannableStringBuilder("30");
		spanTxt.append("\u00B0C");
		//make the textsize 2 times.
		spanTxt.setSpan(new RelativeSizeSpan(0.5f), spanTxt.length() - "\u00B0C".length(), spanTxt.length(), 0  );
		this.tagAvg.setText(spanTxt );
		this.tagMax = (TextView) findViewById(R.id.tagMax);
		spanTxt = new SpannableStringBuilder("40");
		spanTxt.append("\u00B0C");
		//make the textsize 2 times.
		spanTxt.setSpan(new RelativeSizeSpan(0.5f), spanTxt.length() - "\u00B0C".length(), spanTxt.length(), 0  );
		this.tagMax.setText(spanTxt );
		this.probeMin = (TextView) findViewById(R.id.probeMin);
		spanTxt = new SpannableStringBuilder("32");
		spanTxt.append("\u00B0C");
		//make the textsize 2 times.
		spanTxt.setSpan(new RelativeSizeSpan(0.5f), spanTxt.length() - "\u00B0C".length(), spanTxt.length(), 0  );
		this.probeMin.setText(spanTxt );
		this.probeAvg = (TextView) findViewById(R.id.probeAvg);
		spanTxt = new SpannableStringBuilder("38");
		spanTxt.append("\u00B0C");
		//make the textsize 2 times.
		spanTxt.setSpan(new RelativeSizeSpan(0.5f), spanTxt.length() - "\u00B0C".length(), spanTxt.length(), 0  );
		this.probeAvg.setText(spanTxt );
		this.probeMax = (TextView) findViewById(R.id.probeMax);
		spanTxt = new SpannableStringBuilder("40");
		spanTxt.append("\u00B0C");
		//make the textsize 2 times.
		spanTxt.setSpan(new RelativeSizeSpan(0.5f), spanTxt.length() - "\u00B0C".length(), spanTxt.length(), 0  );
		this.probeMax.setText(spanTxt );

		this.deviceName = (TextView) findViewById(R.id.device_name);
		this.deviceName.setText(deviceInfo.name);

		//        this.locations = (ViewGroup) findViewById(R.id.locations);
		//        this.locations.removeAllViews();
		//        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//        ViewGroup locationItem;
		//        DateFormat dateFormat = new SimpleDateFormat("H:m - M/F/y", Locale.ENGLISH);
		//        for(int i = 0; i < deviceInfo.locationHistory.size(); ++i)
		//        {
		//        	locationItem = (ViewGroup) inflater.inflate(R.layout.listitem_locationandtime, null);
		//        	((TextView)locationItem.findViewById(R.id.location)).setText(deviceInfo.locationHistory.get(i).location);
		//        	((TextView)locationItem.findViewById(R.id.date_time)).setText( dateFormat.format(deviceInfo.locationHistory.get(i).timestamp) );
		//        	this.locations.addView(locationItem);
		//        }

		viewData = (Button) findViewById(R.id.temp_history);
		viewData.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent deviceInfoIntent = new Intent(DeviceInfoActivity.this, ViewDataActivity.class);
				deviceInfoIntent.putExtra("device_id", deviceInfo.id);
				startActivity(deviceInfoIntent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

		settings = (Button) findViewById(R.id.edit_record);
		settings.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				showDialog(DeviceInfoActivity.this);
			}
		});

	}

	public void showDialog(Context context){
		// x -->  X-Cordinate
		// y -->  Y-Cordinate
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.activitysettings);
		dialog.setCanceledOnTouchOutside(true);
		dialog.findViewById(R.id.begin_shipment).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent deviceInfoIntent = new Intent(DeviceInfoActivity.this, ViewDataActivity.class);
				deviceInfoIntent.putExtra("position", 3);
				deviceInfoIntent.putExtra("device_id", deviceInfo.id);
				startActivity(deviceInfoIntent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				dialog.dismiss();
			}
		});

		dialog.findViewById(R.id.configure).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent deviceInfoIntent = new Intent(DeviceInfoActivity.this, ShipmentConfigurationActivity.class);
				deviceInfoIntent.putExtra("device_id", deviceInfo.id);
				deviceInfoIntent.putExtra("position", 0);
				startActivity(deviceInfoIntent);
				dialog.dismiss();
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				
			}
		});
		dialog.findViewById(R.id.comments).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent deviceInfoIntent = new Intent(DeviceInfoActivity.this, ShipmentConfigurationActivity.class);
				deviceInfoIntent.putExtra("device_id", deviceInfo.id);
				deviceInfoIntent.putExtra("position", 1);
				startActivity(deviceInfoIntent);
				dialog.dismiss();
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
		wmlp.gravity = Gravity.CENTER;
		dialog.show();
	}


	private void setBatteryStatus(int batteryStatus) {
		if(batteryStatus > 20 && batteryStatus < 40) {
			batteryProgress.setProgressDrawable(getResources().getDrawable(R.drawable.status_ok));
		}else if(batteryStatus > 40 ) {
			batteryProgress.setProgressDrawable(getResources().getDrawable(R.drawable.status_good));
		}else {
			batteryProgress.setProgressDrawable(getResources().getDrawable(R.drawable.status_low));
		}
		//chargeText.setText(""+batteryStatus+"%");
		batteryProgress.setProgress(batteryStatus); 
	}

	private void setMemoryStatus(int memoryStatus) {
		//    	if(memoryStatus > 20 && memoryStatus < 40) {
		//        	memoryProgress.setProgressDrawable(getResources().getDrawable(R.drawable.status_ok));
		//        }else if(memoryStatus < 20 ) {
		//        	memoryProgress.setProgressDrawable(getResources().getDrawable(R.drawable.status_good));
		//        }else {
		//        	memoryProgress.setProgressDrawable(getResources().getDrawable(R.drawable.status_low));
		//        }
		//        memoryStatusText.setText(""+memoryStatus+"%");
		//        memoryProgress.setProgress(memoryStatus); 
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ChangeNotificationObject change = (ChangeNotificationObject)event.getNewValue();
				if(change instanceof BatteryStatusChangeObject) {
					setBatteryStatus((Integer)(change.getNewValue()));
				} else if (change instanceof TemperatureChangeObject) {

					SpannableStringBuilder spanTxt = new SpannableStringBuilder(""+twoDForm.format(change.getNewValue()));
					spanTxt.append("\u00B0C");
					//make the textsize 2 times.
					spanTxt.setSpan(new RelativeSizeSpan(0.5f), spanTxt.length() - "\u00B0C".length(), spanTxt.length(), 0  );
					currentTemp.setText(spanTxt);
				} else if (change instanceof MemoryStatusChangeObject) {
					setMemoryStatus((Integer)(change.getNewValue()));
				}
			}});

	}

}
