package com.quanify.app;

import android.graphics.Color;
import android.os.Handler;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.quanify.models.DeviceRecord;

public class ImpactGraphSingleton {
	private static ImpactGraphSingleton instance = new ImpactGraphSingleton();
	
	private GraphViewSeries exampleSeries;
	
	public DeviceRecord device;
	
	private GraphViewSeries maxSeries;
	
	private double graph2LastXValue = 0d;

	int max=40;
	LinearLayout layout;
	volatile int lastnum =0;
	private final Handler mHandler = new Handler();
	private Runnable mTimer2;
	
	private ImpactGraphSingleton() {
		exampleSeries = new GraphViewSeries("Current Impact", new GraphViewSeriesStyle(Color.rgb(0x00,0x93,0x3B),5), new GraphViewData[] {
			new GraphViewData(graph2LastXValue, 0)

		});
		
		maxSeries = new GraphViewSeries("Max", new GraphViewSeriesStyle(Color.rgb(0xCE,0x2F,0x2F),5), new GraphViewData[] {
			new GraphViewData(graph2LastXValue, 20)
		});

	}
	
	public static ImpactGraphSingleton getInstance() {
		return instance;
	}
	
	
	public GraphViewSeries getExampleSeries() {
		return exampleSeries;
	}

	

	public GraphViewSeries getMaxSeries() {
		return maxSeries;
	}

	
	public double getGraph2LastXValue() {
		return graph2LastXValue;
	}


	public int getMax() {
		return max;
	}

	public int getLastnum() {
		return lastnum;
	}
	
	private double getRandom() {
		if(100* (Math.random()) - 99  > 0) {
			return 10* (Math.random())+15;
		}
		return 5 * (Math.random()) ;
	}
	
	public static void clearGraph(int min, int max, int avg, int lastnum, DeviceRecord device) {
		if(device != null)
			instance.device = device;
		instance.max = max;
	}
	
	public static void clearGraph() {
		instance.device = null;
		instance.max = 20;
	}
	
	
	public int getLastNum(){
		lastnum = (int) device.targetTemp;
		return lastnum;
	}

	
	public void startgraph() {
		
		mTimer2 = new Runnable() {
			@Override
			public void run() {
				boolean scroll = true;
				int num;
				num = (int) (getRandom());
				
				if (max < num) {
					max = num;
				}
				
				graph2LastXValue += 1d;
				exampleSeries.appendData(new GraphViewData(graph2LastXValue, num), scroll, 200);
				maxSeries.appendData(new GraphViewData(graph2LastXValue, max), scroll, 200);
				mHandler.postDelayed(this, 200);
			}
		};
		mHandler.postDelayed(mTimer2, 10);
	}
}
