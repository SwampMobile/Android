package com.quanify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import com.quanify.R;
import com.quanify.activities.ShipmentConfigurationActivity;
import com.quanify.models.DeviceRecord;
public class ConfigureFragment extends Fragment {

	private Spinner spinner1 , spinner2, spinner3;
	EditText probe_max,probe_min,tag_max,tag_min;
	TextView deviceId,deviceName;
	DeviceRecord deviceInfo;
	EditText timeinterval,temperature,time;
	DeviceRecord device;
	
	public ConfigureFragment() {
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		device =((ShipmentConfigurationActivity)getActivity()).getDevice();
		FrameLayout fl = new FrameLayout(getActivity());
		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
				.getDisplayMetrics());
		params.setMargins(margin, margin, margin, margin);
		fl.setLayoutParams(params);
		inflater.inflate(R.layout.configure, fl);
		
		spinner1 = (Spinner) fl.findViewById(R.id.spinner1);
		spinner2 = (Spinner) fl.findViewById(R.id.spinner2);
		spinner3 = (Spinner) fl.findViewById(R.id.spinner3);

		deviceName = (TextView) fl.findViewById(R.id.device_name);
		deviceName.setText(device.name);
		deviceId = (TextView) fl.findViewById(R.id.device_id);
		deviceId.setText(device.id);
		probe_max = (EditText) fl.findViewById(R.id.probe_max);
		probe_min = (EditText) fl.findViewById(R.id.probe_min);
		tag_min = (EditText) fl.findViewById(R.id.tag_min);
		tag_max = (EditText) fl.findViewById(R.id.tag_max);
		timeinterval = (EditText) fl.findViewById(R.id.timeinterval);
		temperature = (EditText) fl.findViewById(R.id.temperature);
		time = (EditText) fl.findViewById(R.id.time);
		return fl;
	}

}