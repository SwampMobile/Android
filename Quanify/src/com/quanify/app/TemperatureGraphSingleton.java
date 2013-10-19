package com.quanify.app;

import android.graphics.Color;
import android.os.Handler;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.quanify.models.DeviceRecord;

public class TemperatureGraphSingleton {
	private static TemperatureGraphSingleton instance = new TemperatureGraphSingleton();
	
	private GraphViewSeries exampleSeries;
	
	public DeviceRecord device;
	private GraphViewSeries minSeries;
	private GraphViewSeries maxSeries;
	private GraphViewSeries avgSeries;
	private double graph2LastXValue = 0d;
	int min=25;
	int max=40;
	LinearLayout layout;
	volatile int lastnum =0;
	float avg=36;
	private final Handler mHandler = new Handler();
	private Runnable mTimer2;
	private boolean temp = false;
	
	private TemperatureGraphSingleton() {
		exampleSeries = new GraphViewSeries("Current Temperature", new GraphViewSeriesStyle(Color.rgb(0x00,0x93,0x3B),5), new GraphViewData[] {
			new GraphViewData(graph2LastXValue, 35)

		});

		minSeries = new GraphViewSeries("Min",new GraphViewSeriesStyle(Color.rgb(0x33,0x69,0xE8),5) ,new GraphViewData[] {
			new GraphViewData(graph2LastXValue, 25)
			// another frequency
		});

		maxSeries = new GraphViewSeries("Max", new GraphViewSeriesStyle(Color.rgb(0xCE,0x2F,0x2F),5), new GraphViewData[] {
			new GraphViewData(graph2LastXValue, 40)
		});

		avgSeries = new GraphViewSeries("Avg", new GraphViewSeriesStyle(Color.rgb(0xff,0xbf,0x00),5), new GraphViewData[] {
			new GraphViewData(graph2LastXValue, 36)
		});
	}
	
//	private TemperatureGraphSingleton(int min, int max, int avg, int lastnum, DeviceRecord device) {
//		this.device = device;
//		exampleSeries = new GraphViewSeries("Current Temperature", new GraphViewSeriesStyle(Color.rgb(0x00,0x93,0x3B),5), new GraphViewData[] {
//			new GraphViewData(graph2LastXValue, lastnum)
//
//		});
//
//		minSeries = new GraphViewSeries("Min",new GraphViewSeriesStyle(Color.rgb(0x33,0x69,0xE8),5) ,new GraphViewData[] {
//			new GraphViewData(graph2LastXValue, min)
//			// another frequency
//		});
//
//		maxSeries = new GraphViewSeries("Max", new GraphViewSeriesStyle(Color.rgb(0xCE,0x2F,0x2F),5), new GraphViewData[] {
//			new GraphViewData(graph2LastXValue, max)
//		});
//
//		avgSeries = new GraphViewSeries("Avg", new GraphViewSeriesStyle(Color.rgb(0xff,0xbf,0x00),5), new GraphViewData[] {
//			new GraphViewData(graph2LastXValue, avg)
//		});
//		temp = true;
//	}
	
	public static TemperatureGraphSingleton getInstance() {
		return instance;
	}
	
	public float getAvg() {
		return avg;
	}

	public void setAvg(float avg) {
		this.avg = avg;
	}

	public GraphViewSeries getExampleSeries() {
		return exampleSeries;
	}

	public GraphViewSeries getMinSeries() {
		return minSeries;
	}

	public GraphViewSeries getMaxSeries() {
		return maxSeries;
	}

	public GraphViewSeries getAvgSeries() {
		return avgSeries;
	}

	public double getGraph2LastXValue() {
		return graph2LastXValue;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public int getLastnum() {
		return lastnum;
	}
	
	private double getRandom() {
		return 10* (Math.random()) - 4 ;
	}
	
	public static void clearGraph(int min, int max, int avg, int lastnum, DeviceRecord device) {
		if(device != null)
			instance.device = device;
		instance.min = min;
		instance.max = max;
		instance.avg = avg;
		instance.temp = true;
	}
	
	public static void clearGraph() {
		instance.device = null;
		instance.min = 25;
		instance.max = 40;
		instance.avg = 36;
		instance.temp = false;
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
				if(!temp)
					num = (int) (20+getRandom());
				else
					num = getLastNum();
				if (num < min) {
					min = num;
				}
				if (max < num) {
					max = num;
				}
				avg = (float)(((avg* graph2LastXValue) +num)/(graph2LastXValue+1));
				graph2LastXValue += 1d;
				exampleSeries.appendData(new GraphViewData(graph2LastXValue, num), scroll, 200);
				minSeries.appendData(new GraphViewData(graph2LastXValue, min), scroll, 200);
				maxSeries.appendData(new GraphViewData(graph2LastXValue, max), scroll, 200);
				avgSeries.appendData(new GraphViewData(graph2LastXValue, avg), scroll, 200);
				mHandler.postDelayed(this, 200);
			}
		};
		mHandler.postDelayed(mTimer2, 10);
	}
}
