package com.quanify.changenotificationobjects;

public class TemperatureChangeObject extends ChangeNotificationObject {
	double oldvalue;
	double newvalue;

	public TemperatureChangeObject(double oldValue, double newValue) {
		oldvalue = oldValue;
		newvalue = newValue;
	}
	public Object getOldValue() {
		// TODO Auto-generated method stub
		return oldvalue;
	}

	@Override
	public Object getNewValue() {
		// TODO Auto-generated method stub
		return newvalue;
	}

}
