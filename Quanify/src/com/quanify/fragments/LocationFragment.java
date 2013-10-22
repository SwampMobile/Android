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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.quanify.R;

public class LocationFragment extends Fragment 
{
	private MapView mapView;
	private GoogleMap map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_location, null);
		
		mapView = (MapView)v.findViewById(R.id.map_view);
		mapView.onCreate(savedInstanceState);
		mapView.onResume();
		
		try
		{
			MapsInitializer.initialize(getActivity());
		}
		catch(GooglePlayServicesNotAvailableException e)
		{
			e.printStackTrace();
		}
		
		map = mapView.getMap();
		map.setMyLocationEnabled(true);
		
		map.moveCamera(CameraUpdateFactory.newCameraPosition(
				CameraPosition.fromLatLngZoom(
					new LatLng(
						29.6520f, 
						-82.3250f
					), 
				13)
		));
		
		return v;
		
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//
//		FrameLayout fl = new FrameLayout(getActivity());
//		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
//				.getDisplayMetrics());
//		params.setMargins(margin, margin, margin, margin);
//		fl.setLayoutParams(params);
//		fl.setBackgroundColor(0xffa9a9a9);
//		inflater.inflate(R.layout.fragment_device_graph, fl);

		/*final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
				.getDisplayMetrics());

		TextView v = new TextView(getActivity());
		params.setMargins(margin, margin, margin, margin);
		v.setLayoutParams(params);
		v.setLayoutParams(params);
		v.setGravity(Gravity.CENTER);
		v.setBackgroundResource(R.drawable.background_card);
		v.setText("CARD " + (position + 1));

		fl.addView(v);*/
		
//		return fl;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		mapView.onResume();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		mapView.onPause();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mapView.onDestroy();
	}
	
	@Override
	public void onLowMemory()
	{
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override public void onSaveInstanceState(Bundle outState) { 
		//first saving my state, so the bundle wont be empty. 
		outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE"); 
		super.onSaveInstanceState(outState); 
	} 

}