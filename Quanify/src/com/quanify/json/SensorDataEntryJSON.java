package com.quanify.json;

import org.json.JSONException;
import org.json.JSONObject;

public class SensorDataEntryJSON extends JSONObject {
	
	public SensorDataEntryJSON(int t, double v) throws JSONException {
		this.put("T", t);
		this.put("V", v);
	}
}
