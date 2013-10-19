package com.quanify.changenotificationobjects;

public class BatteryStatusChangeObject extends ChangeNotificationObject {
	int oldvalue;
	int newvalue;
	@Override
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
