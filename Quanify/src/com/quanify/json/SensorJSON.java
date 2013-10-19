package com.quanify.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SensorJSON extends JSONObject {
	JSONArray sensorData = new JSONArray();
	
	public SensorJSON(int sensorNumber, String sensorType ) throws JSONException {
		this.put("SensorNumber", sensorNumber);
		this.put("SensorType", sensorType);
		this.put("SensorData", sensorData);
		this.put("MinThreshold", 0);
		this.put("MaxThreshold", 0);
	}
	
	public void addMinimum(double min) throws JSONException {
		this.put("Minimum", min);
	}
	
	public void addMaximum(double min) throws JSONException {
		this.put("Maximum", min);
	}
	
	public void addAverage(double avg) throws JSONException {
		this.put("Average", avg);
	}
	
	public void addMinThreshold(double minThreshold) throws JSONException {
		this.put("MinThreshold", minThreshold);
	}
	
	public void addMaxThreshold(double maxThreshold) throws JSONException {
		this.put("MaxThreshold", maxThreshold);
	}
	
	public void addSpikeCount(int spikeCount) throws JSONException {
		this.put("SpikeCount", spikeCount);
	}
	
	
	public void addSensorData(SensorDataEntryJSON data) {
		sensorData.put(data);
	}
}
