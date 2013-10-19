package com.quanify.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONRecord extends JSONObject {
	JSONArray tags = new JSONArray();

	public JSONRecord(int version, int customerID, int userID, 
		String time, int locationID, int eventType,String customerBarcode) 
		throws JSONException {		

		this.put("Version", version);
		this.put("CustomerID", customerID);
		this.put("UserID", userID);
		this.put("Time", time);
		this.put("LocationID", locationID);
		this.put("EventType", eventType);
		this.put("CustomerBarcode", customerBarcode);
		this.put("LocationLatitude", 0);
		this.put("LocationLongitude", 0);		
		this.put("Tags", tags);

	}

	public void addTags(TagJSON tag) throws JSONException {
		tags.put(tag);
	}
	
	public void addCNSShipmentID(int cnsShipmentID) throws JSONException {
		this.put("CNSShipmentID", cnsShipmentID);	
	}

	public void addLocationLatLong(int latitude, int longitude) throws JSONException {
		this.put("LocationLatitude", latitude);
		this.put("LocationLongitude", longitude);		
	}

	public void addLotNumber(String lotNumber) throws JSONException {
		this.put("LotNumber", lotNumber);	
	}

	public void addDestinationLocationID(int destinationLocationID) throws JSONException {
		this.put("DestinationLocationID", destinationLocationID);	
	}

	public void addOriginLocationID(int originLocationID) throws JSONException {
		this.put("OriginLocationID", originLocationID);		
	}

}
