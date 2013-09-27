package com.quanify.app;

import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.quanify.models.DeviceRecord;
import com.quanify.models.LocationAndTime;

public class MockData 
{
	private static MockData instance;
	public static MockData getInstance()
	{
		if(instance == null)
			instance = new MockData();
		
		return instance;
	}
	
	private HashMap<String,DeviceRecord> deviceMap = new HashMap<String,DeviceRecord>();
	{
		DateFormat dateFormat = new SimpleDateFormat("M/F/y H:m", Locale.ENGLISH);
		
		LocationAndTime loc1 = new LocationAndTime();
		loc1.location = "13th Street";
		try {
			loc1.timestamp = dateFormat.parse("9/7/13 12:57");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LocationAndTime loc2 = new LocationAndTime();
		loc2.location = "Proc Center";
		try {
			loc2.timestamp = dateFormat.parse("9/7/13 15:31");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LocationAndTime loc3 = new LocationAndTime();
		loc3.location = "Shands Cardiac";
		try {
			loc3.timestamp = dateFormat.parse("9/7/13 10:33");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		deviceMap = new HashMap<String,DeviceRecord>();
		
		DeviceRecord device1 = new DeviceRecord();
		device1.id = "157609";
		device1.name = "Whole Blood";
		device1.ambTemp = 35f;
		device1.locationHistory = new ArrayList<LocationAndTime>();
		device1.locationHistory.add(loc1);
		device1.locationHistory.add(loc2);
		device1.locationHistory.add(loc3);
		deviceMap.put(device1.id,device1);
		
		DeviceRecord device2 = new DeviceRecord();
		device2.id = "046153";
		device2.name = "Platelets";
		device2.ambTemp = 53f;
		device2.status = DeviceRecord.Status.HIGH;
		device2.locationHistory = new ArrayList<LocationAndTime>();
		device2.locationHistory.add(loc1);
		device2.locationHistory.add(loc2);
		device2.locationHistory.add(loc3);
		deviceMap.put(device2.id,device2);
		
		DeviceRecord device3 = new DeviceRecord();
		device3.id = "000245";
		device3.name = "Red Cells";
		device3.ambTemp = 36f;
		device3.locationHistory = new ArrayList<LocationAndTime>();
		device3.locationHistory.add(loc1);
		device3.locationHistory.add(loc2);
		device3.locationHistory.add(loc3);
		deviceMap.put(device3.id,device3);
	}
	
	private MockData() { }
	
	public void addDevice(Context context, BluetoothDevice device, PropertyChangeListener listener) {
		if(!deviceMap.containsKey(device.getAddress()) && device.getName().contains("SensorTag")) {
			DeviceRecord deviceRecord = new DeviceRecord();
			deviceRecord.addPropertyChangeListener(listener);
			deviceRecord.addDevice(device);
			deviceRecord.locationHistory = new ArrayList<LocationAndTime>();
			deviceMap.put(device.getAddress(),deviceRecord);
		}
	}
	
	public Collection<DeviceRecord> getLocalDevices()
	{
		return deviceMap.values();
	}
	
	public DeviceRecord getDeviceById(String id)
	{
		return deviceMap.get(id);
	}
	
	public void refershList() {
		deviceMap.clear();
	}
}
