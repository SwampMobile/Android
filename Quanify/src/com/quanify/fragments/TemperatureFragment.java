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

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;
import com.quanify.R;
import com.quanify.activities.ViewDataActivity;
import com.quanify.app.TemperatureGraphSingleton;
import com.quanify.models.DeviceRecord;

public class TemperatureFragment extends Fragment {

	private GraphView graphView;
	private LinearLayout layout;
	DeviceRecord device;
	

	public TemperatureFragment() {
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		device =((ViewDataActivity)getActivity()).getDevice();
		FrameLayout fl = new FrameLayout(getActivity());
		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
				.getDisplayMetrics());
		params.setMargins(margin, margin, margin, margin);
		fl.setLayoutParams(params);
		fl.setBackgroundColor(0xffa9a9a9);
		inflater.inflate(R.layout.fragment_device_graph, fl);
		GraphViewStyle graphViewStyle = new GraphViewStyle(Color.rgb(51, 51, 51), Color.rgb(51, 51, 51), Color.rgb(51, 51, 51) );
		graphView = new LineGraphView(this.getActivity(), "", graphViewStyle);
		((LineGraphView) graphView).setDrawBackground(false);
		graphView.addSeries(TemperatureGraphSingleton.getInstance().getExampleSeries()); 
		graphView.addSeries(TemperatureGraphSingleton.getInstance().getMinSeries());// data
		graphView.addSeries(TemperatureGraphSingleton.getInstance().getMaxSeries());
		graphView.addSeries(TemperatureGraphSingleton.getInstance().getAvgSeries());
		graphView.setViewPort(1, 200);
		graphView.setScalable(true);
		graphView.setScrollable(true);
		graphView.setShowLegend(true);  
		graphView.setLegendAlign(LegendAlign.TOP);  
		graphView.setLegendWidth(600); 
		//graphView.setManualYAxisBounds(100, 0);
		layout = (LinearLayout) fl.findViewById(R.id.graph);
		layout.addView(graphView);
		if(device.isDevice == true) {
			TemperatureGraphSingleton.clearGraph(25, 35, 34, 30, device);			
			TemperatureGraphSingleton.getInstance().startgraph();
		}else {
			TemperatureGraphSingleton.clearGraph();
			TemperatureGraphSingleton.getInstance().startgraph();
		}

		return fl;
	}

	@Override public void onSaveInstanceState(Bundle outState) { 
		//first saving my state, so the bundle wont be empty. 
		outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE"); 
		super.onSaveInstanceState(outState); 
	} 

}