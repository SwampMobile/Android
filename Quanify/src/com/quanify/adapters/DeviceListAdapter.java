package com.quanify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quanify.R;
import com.quanify.app.DeviceList;
import com.quanify.models.DeviceRecord;

public class DeviceListAdapter extends BaseAdapter 
{
	private Context context;
	
	public DeviceListAdapter(Context context)
	{
		this.context = context;
	}

	
	@Override
	public int getCount() {
		return DeviceList.getInstance().getLocalDevices().size();
	}

	@Override
	public Object getItem(int position) {
		return DeviceList.getInstance().getLocalDevices().toArray()[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewGroup listItem;
		
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			listItem = (ViewGroup) inflater.inflate(R.layout.listitem_device_status, null, false);
		}
		else
		{
			listItem = (ViewGroup) convertView;
		}
		
		DeviceRecord device = (DeviceRecord) getItem(position);
		//Log.d("DeviceListAdapter", "Device id: " + device.id);
		//Log.d("DeviceListAdapter", "TextView: " + listItem.findViewById(R.id.device_id));
		((TextView)listItem.findViewById(R.id.device_id)).setText('#' + device.id);
		((TextView)listItem.findViewById(R.id.device_name)).setText(device.name);
		((TextView)listItem.findViewById(R.id.temp)).setText(String.format("%.0f",device.targetTemp) + '\u00B0' + 'C');
		setBatteryStatus(((ProgressBar)listItem.findViewById(R.id.batteryStatus)),device.getBatteryProgress(),listItem.getContext());
		((ProgressBar)listItem.findViewById(R.id.signalStatus)).setProgress(device.getSignalStatus());
		return listItem;
	}
	
	 private void setBatteryStatus(ProgressBar battery, int batteryStatus, Context context) {
		 	
	    	if(batteryStatus > 20 && batteryStatus < 40) {
	    		battery.setProgressDrawable(context.getResources().getDrawable(R.drawable.status_ok));
	        }else if(batteryStatus > 40 ) {
	        	battery.setProgressDrawable(context.getResources().getDrawable(R.drawable.status_good));
	        }else {
	        	battery.setProgressDrawable(context.getResources().getDrawable(R.drawable.status_low));
	        } 
	    	battery.setProgress(batteryStatus);
	    }
	

}
