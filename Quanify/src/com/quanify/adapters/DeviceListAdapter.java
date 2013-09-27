package com.quanify.adapters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanify.R;
import com.quanify.app.MockData;
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
		return MockData.getInstance().getLocalDevices().size();
	}

	@Override
	public Object getItem(int position) {
		return MockData.getInstance().getLocalDevices().toArray()[position];
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
		((TextView)listItem.findViewById(R.id.temp)).setText(String.format("%.0f",device.targetTemp) + '\u00B0' + 'F');
		
		
		return listItem;
	}
	

}
