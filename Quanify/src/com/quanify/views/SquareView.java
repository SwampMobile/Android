package com.quanify.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class SquareView extends View
{

	public SquareView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}
	
	public void onMeasure(int widthSpec, int heightSpec)
	{
		super.onMeasure(widthSpec, widthSpec);
	}
	
}
