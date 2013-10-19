/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quanify.fragments;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.quanify.R;
import com.quanify.json.JSONRecord;
import com.quanify.json.SensorDataEntryJSON;
import com.quanify.json.SensorJSON;
import com.quanify.json.TagJSON;

public class SyncJSONFragment extends Fragment {

	private EditText version;
	private EditText cnsShipmentID;
	private EditText customerID;
	private EditText userID;
	private EditText time;
	private EditText locationLatitude;
	private EditText locationLongitude;
	private EditText locationID;
	private EditText originLocationID;
	private EditText destinationLocationID;
	private EditText eventType;
	private EditText customerBarcode;
	private EditText lotNumber;
	private EditText tagID;
	private EditText batteryLevel;
	private EditText storageUsed;
	private EditText sensorNumber;
	private EditText sensorType;
	private EditText minimum;
	private EditText maximum;
	private EditText average;
	private EditText minThreshold;
	private EditText maxThreshold;
	private EditText spikeCount;
	private EditText tValue;
	private EditText vValue;
	private Button syncSensor;
	private Button initiateShipment;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		FrameLayout fl = new FrameLayout(getActivity());
		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
				.getDisplayMetrics());
		params.setMargins(margin, margin, margin, margin);
		fl.setLayoutParams(params);
		fl.setBackgroundColor(0xffa9a9a9);
		inflater.inflate(R.layout.fragment_device_sync, fl);

		version = (EditText)fl.findViewById(R.id.version_value);
		cnsShipmentID = (EditText)fl.findViewById(R.id.cnsShipmentID_value);
		userID = (EditText)fl.findViewById(R.id.userID_value);
		customerID = (EditText)fl.findViewById(R.id.customerID_value);
		time = (EditText)fl.findViewById(R.id.time_value);
		locationLatitude = (EditText)fl.findViewById(R.id.locationLatitude_value);
		locationLongitude = (EditText)fl.findViewById(R.id.locationLongitude_value);
		locationID = (EditText)fl.findViewById(R.id.locationID_value);
		originLocationID = (EditText)fl.findViewById(R.id.originLocationID_value);
		destinationLocationID = (EditText)fl.findViewById(R.id.destinationLocationID_value);
		eventType = (EditText)fl.findViewById(R.id.eventType_value);
		customerBarcode = (EditText) fl.findViewById(R.id.customerBarcode_value);
		lotNumber = (EditText)fl.findViewById(R.id.lotNumber_value);
		tagID = (EditText)fl.findViewById(R.id.tagID_value);
		batteryLevel = (EditText)fl.findViewById(R.id.batteryLevel_value);
		storageUsed = (EditText)fl.findViewById(R.id.storageUsed_value);
		sensorNumber = (EditText)fl.findViewById(R.id.sensorNumber_value);
		sensorType = (EditText)fl.findViewById(R.id.sensorType_value);
		minimum = (EditText)fl.findViewById(R.id.minimum_value);
		maximum = (EditText)fl.findViewById(R.id.maximum_value);
		average = (EditText)fl.findViewById(R.id.average_value);
		minThreshold = (EditText)fl.findViewById(R.id.minThreshold_value);
		maxThreshold = (EditText)fl.findViewById(R.id.maxThreshold_value);
		spikeCount = (EditText)fl.findViewById(R.id.spikeCount_value);
		tValue = (EditText)fl.findViewById(R.id.t_value);
		vValue = (EditText)fl.findViewById(R.id.v_value);
		initiateShipment = (Button) fl.findViewById(R.id.initiateShipment);
		initiateShipment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 new Thread(new Runnable() {
			        	public void run() {
			        		try {
			        			int eventtype  = Integer.parseInt(eventType.getText().toString());
			        			if( eventtype==1) {
			        			HttpResponse response = makeRequest("http://www.commnsense.info/wmccombie/CNS/InitiateShipment.php",createJSONRecord(true));
			        			Log.d("TAG",response.toString());
			        			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			        			StringBuilder builder = new StringBuilder();
			        			for (String line = null; (line = reader.readLine()) != null;) {
			        			    builder.append(line).append("\n");
			        			}
			        			JSONTokener tokener = new JSONTokener(builder.toString());
			        			JSONObject finalResult = new JSONObject(tokener);
			        			int shipmentID = (Integer)(finalResult.get("CNSShipmentID"));
			        			int eventID = (Integer)finalResult.get("EventID");
			        			throwDialog(shipmentID,eventID, true);
			        			} else if(eventtype == 2 || eventtype == 3) {
			        				wrongButtonErrorDialog(false);
			        			} else {
			        				throwErrorDialog();
			        			}
			        		} catch (JSONException e) {
			        			// TODO Auto-generated catch block
			        			e.printStackTrace();
			        		} catch (Exception e) {
			        			// TODO Auto-generated catch block
			        			e.printStackTrace();
			        		}
			        	}
			        }).start();
			}
		});
		syncSensor = (Button) fl.findViewById(R.id.syncSensor);
		syncSensor.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 new Thread(new Runnable() {
			        	public void run() {
			        		try {
			        			int eventtype  = Integer.parseInt(eventType.getText().toString());
			        			if( eventtype==2 || eventtype==3) {
			        			HttpResponse response = makeRequest("http://www.commnsense.info/wmccombie/CNS/DeviceSync.php",createJSONRecord(false));
			        			Log.d("TAG",response.toString());
			        			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			        			StringBuilder builder = new StringBuilder();
			        			for (String line = null; (line = reader.readLine()) != null;) {
			        			    builder.append(line).append("\n");
			        			}
			        			JSONTokener tokener = new JSONTokener(builder.toString());
			        			JSONObject finalResult = new JSONObject(tokener);
			        			int shipmentID = (Integer)(finalResult.get("CNSShipmentID"));
			        			int eventID = (Integer)finalResult.get("EventID");
			        			throwDialog(shipmentID,eventID, false);
			        			} else if(eventtype == 1) {
			        				wrongButtonErrorDialog(true);
			        			} else {
			        				throwErrorDialog();
			        			}
			        		} catch (JSONException e) {
			        			// TODO Auto-generated catch block
			        			e.printStackTrace();
			        		} catch (Exception e) {
			        			// TODO Auto-generated catch block
			        			e.printStackTrace();
			        		}
			        	}
			        }).start();
			}
		});
		return fl;
	}

	public HttpResponse makeRequest(String path, JSONRecord record) throws Exception 
	{
		//instantiates httpclient to make request
		DefaultHttpClient httpclient = new DefaultHttpClient();

		//url with the post data
		HttpPost httpost = new HttpPost(path);

		//passes the results to a string builder/entity
		StringEntity se = new StringEntity(record.toString());

		//sets the post request as the resulting string
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("data", record.toString()));
		httpost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		//sets a request header so the page receving the request
		//will know what to do with it
		//httpost.setHeader("Accept", "application/json");
		//httpost.setHeader("Content-type", "application/json");
		return httpclient.execute(httpost);
	}

	public JSONRecord createJSONRecord(boolean isInitiate) throws JSONException {
		JSONRecord record = new JSONRecord(Integer.parseInt(version.getText().toString()),
				Integer.parseInt(customerID.getText().toString()), 
				Integer.parseInt(userID.getText().toString()), 
				"2013-10-08T22:14:26", 
				Integer.parseInt(locationID.getText().toString()), 
				Integer.parseInt(eventType.getText().toString()),
				customerBarcode.getText().toString());
		if(!isInitiate) {
			record.addCNSShipmentID(Integer.parseInt(cnsShipmentID.getText().toString()));
		}
		record.addOriginLocationID(Integer.parseInt(originLocationID.getText().toString()));
		record.addDestinationLocationID(Integer.parseInt(destinationLocationID.getText().toString()));
		record.addLotNumber(lotNumber.getText().toString());

		TagJSON tag = new TagJSON(Integer.parseInt(tagID.getText().toString()), Integer.parseInt(batteryLevel.getText().toString()), Double.parseDouble(storageUsed.getText().toString()));
		SensorJSON sensor = new SensorJSON(Integer.parseInt(sensorNumber.getText().toString()),sensorType.getText().toString());
		sensor.addSpikeCount(Integer.parseInt(spikeCount.getText().toString()));
		sensor.addAverage(Double.parseDouble(average.getText().toString()));
		sensor.addMaximum(Double.parseDouble(maximum.getText().toString()));
		sensor.addMinimum(Double.parseDouble(minimum.getText().toString()));
		sensor.addMaxThreshold(Double.parseDouble(maxThreshold.getText().toString()));
		sensor.addMinThreshold(Double.parseDouble(minThreshold.getText().toString()));
		SensorDataEntryJSON data = new SensorDataEntryJSON(Integer.parseInt(tValue.getText().toString()),Double.parseDouble(vValue.getText().toString()));
		sensor.addSensorData(data);
		tag.addSensor(sensor);	
		record.addTags(tag);
		return record;
	}

	@Override public void onSaveInstanceState(Bundle outState) { 
		//first saving my state, so the bundle wont be empty. 
		outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE"); 
		super.onSaveInstanceState(outState); 
	} 
	
	void throwDialog(final int shipmentID,final int eventID, final boolean isInitiate ) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
    		public void run() {
				String sync = "unsuccessful";
				if(eventID != 0 ) {
					sync = "successful";
				}
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getActivity());                      
				dlgAlert.setTitle("Sync Status"); 
				dlgAlert.setMessage(!isInitiate?"CNShipmentID : "+shipmentID +"\n"+"Sync "+sync:"CNShipmentID : "+shipmentID +"\n"+"Initiate "+sync); 
				dlgAlert.setIcon(R.drawable.ic_launch);
				dlgAlert.setPositiveButton("OK",null);
				dlgAlert.create().show();
			}
		});
		
	}
	
	void throwErrorDialog( ) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
    		public void run() {
				boolean sync = false;
				String msg = "Invalid eventType";
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getActivity());                      
				dlgAlert.setTitle("Error"); 
				dlgAlert.setMessage(msg); 
				dlgAlert.setIcon(R.drawable.ic_launch);
				dlgAlert.setNegativeButton("Cancel",null);
				dlgAlert.create().show();
			}
		});	
	}
	
	void wrongButtonErrorDialog(final boolean isInitiate ) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
    		public void run() {
				boolean sync = false;
				String msg = isInitiate?"Click the initate Shipment button":"Click the Sync Button";
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getActivity());                      
				dlgAlert.setTitle("Error"); 
				dlgAlert.setMessage(msg); 
				dlgAlert.setIcon(R.drawable.ic_launch);
				dlgAlert.setNegativeButton("Cancel",null);
				dlgAlert.create().show();
			}
		});	
	}

}