package com.quanify.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TagJSON extends JSONObject {
	JSONArray sensors = new JSONArray();
	public TagJSON(int tagID, int batteryLevel, double storageUsed)  throws JSONException {
		this.put("TagID", tagID);
		this.put("BatteryLevel",batteryLevel);
		this.put("StorageUsed", storageUsed);
		this.put("Sensors", sensors);

	}

	public void addSensor(SensorJSON sensor) {
		sensors.put(sensor);
	}
}
