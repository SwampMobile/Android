package com.quanify.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.quanify.app.WriteQueue;
import com.quanify.changenotificationobjects.TemperatureChangeObject;
import com.quanify.sensors.Temperature;

public class DeviceRecord extends BluetoothGattCallback
{
	public enum Status
	{
		LOW,
		NORMAL,
		HIGH
	}
	private BluetoothDevice device;
	public String id;
	public String name;
	public Status status = Status.NORMAL;
	public double ambTemp;
	public double targetTemp;
	public float minTemp;
	public float maxTemp;
	public float avgTemp;
	public int rssi;
	private int batteryProgress = 50;
	private int memoryProgress = 50;
	public boolean isDevice = false;

	public List<LocationAndTime> locationHistory;
	public BluetoothGatt mBluetoothGatt;
	private final static String TAG = "DeviceRecord";
	boolean enabled  = false;
	WriteQueue writeQueue = new WriteQueue();
	
	private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    
    public final static String PROPERTY_ACCELEROMETER = "ACCELEROMETER", 
    	PROPERTY_AMBIENT_TEMPERATURE = "AMBIENT", PROPERTY_IR_TEMPERATURE = "IR_TEMPERATURE",
    	PROPERTY_HUMIDITY = "HUMIDITY", PROPERTY_MAGNETOMETER = "MAGNETOMETER", 
    	PROPERTY_GYROSCOPE = "GYROSCOPE", PROPERTY_SIMPLE_KEYS = "SIMPLE_KEYS",
    	PROPERTY_BAROMETER = "BAROMETER";
    
    
    public int getBatteryProgress() {
		return batteryProgress;
	}

	private void setBatteryProgress(int batteryProgress) {
		this.batteryProgress = batteryProgress;
	}
	
	public int getMemoryProgress() {
		return memoryProgress;
	}
	
	public int getSignalStatus() {
		if(device == null)
			return 50;
		return 100 + rssi;
	}
	
	public void setrssi(int rssi) {
		this.rssi = rssi;
	}

	private void setMemoryProgress(int memoryProgress) {
		this.memoryProgress = memoryProgress;
	}
	
	public void addDevice(BluetoothDevice device) {
		this.device = device;
		name = device.getName();
		id = device.getAddress();
		isDevice = true;
	}
	
	@Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status,
            int newState) {
        String intentAction;
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            intentAction = ACTION_GATT_CONNECTED;
            mConnectionState = STATE_CONNECTED;
            broadcastUpdate(intentAction);
            Log.d("DeviceRecord", name +" Connected to GATT server.");
            Log.d("DeviceRecord", "Attempting to start service discovery for "+name);
            mBluetoothGatt.discoverServices();

        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            intentAction = ACTION_GATT_DISCONNECTED;
            mConnectionState = STATE_DISCONNECTED;
            Log.d(TAG, name +" Disconnected from GATT server.");
            broadcastUpdate(intentAction);
        }
    }

    @Override
    // New services discovered
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
    	Log.w(TAG, name +" onServicesDiscovered received: " + status+" no of services : "+gatt.getServices().size());
    	for(BluetoothGattService service: gatt.getServices() ) {
    		Log.d("TEMPERATURE","service UUID : "+service.getUuid().toString());
    		if(service.getUuid().toString().startsWith("f000aa00") ) {
    			changeNotificationStatus();
    			changeSensorStatus();
    		}
    	}
    		/*	BluetoothGattService magnetService = mBluetoothGatt.getService(UUID.fromString("f000aa00-0451-4000-b000-000000000000"));
                BluetoothGattCharacteristic data = magnetService.getCharacteristic(UUID.fromString("f000aa01-0451-4000-b000-000000000000"));
                mBluetoothGatt.setCharacteristicNotification(data, true);

    	        BluetoothGattDescriptor configDescriptor = data.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
    	        if(configDescriptor != null) {
    	        	byte[] configValue = enabled ? ENABLE_NOTIFICATION_VALUE : DISABLE_NOTIFICATION_VALUE;
    	        	boolean success = configDescriptor.setValue(configValue);

    	        	mBluetoothGatt.writeDescriptor(configDescriptor);
    	        }
    	           
                BluetoothGattCharacteristic config = magnetService.getCharacteristic(UUID.fromString("f000aa02-0451-4000-b000-000000000000"));
                config.setValue(new byte[]{1});
                enabled = mBluetoothGatt.writeCharacteristic(config);
    			/*if (enabled) { 
    				double ambient = Temperature.getInstance().extractAmbientTemperature(service.getCharacteristics().get(0));
    				double target = Temperature.getInstance().extractTargetTemperature(service.getCharacteristics().get(0), ambient);
    				Log.d("TEMPERATURE","ambient : "+ambient+" TARGET : "+target);
    			}
    		    
    		}
    	}*/
        if (status == BluetoothGatt.GATT_SUCCESS) {
            broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
        } 
    }
    
    private void changeNotificationStatus() {

        writeQueue.queueRunnable(new Runnable() {
          @Override
          public void run() {
        	  BluetoothGattService magnetService = mBluetoothGatt.getService(UUID.fromString("f000aa00-0451-4000-b000-000000000000"));
        	  BluetoothGattCharacteristic data = magnetService.getCharacteristic(UUID.fromString("f000aa01-0451-4000-b000-000000000000"));
        	  mBluetoothGatt.setCharacteristicNotification(data, true);

        	  BluetoothGattDescriptor configDescriptor = data.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        	  if(configDescriptor != null) {
        		  byte[] configValue = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
        		  boolean success = configDescriptor.setValue(configValue);
        		  mBluetoothGatt.writeDescriptor(configDescriptor);
        	  }

          }
        });
      }
    
    private void changeSensorStatus() {
    	writeQueue.queueRunnable(new Runnable() {
    		@Override
    		public void run() {
    			BluetoothGattService magnetService = mBluetoothGatt.getService(UUID.fromString("f000aa00-0451-4000-b000-000000000000"));
    			BluetoothGattCharacteristic config = magnetService.getCharacteristic(UUID.fromString("f000aa02-0451-4000-b000-000000000000"));
                config.setValue(new byte[]{1});
                enabled = mBluetoothGatt.writeCharacteristic(config);
    		}
    	});
    }


    @Override
    // Result of a characteristic read operation
    public void onCharacteristicRead(BluetoothGatt gatt,
            BluetoothGattCharacteristic characteristic,
            int status) {
    	Log.w(TAG, name +" onServicesDiscovered received: " + status);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    }
    
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
                
    }
    
    @Override
    public void onCharacteristicChanged (BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
    	Log.d("DeviceRecord", name +" characteristic changed");
    	double ambient = Temperature.getInstance().extractAmbientTemperature(characteristic);
		double target = Temperature.getInstance().extractTargetTemperature(characteristic, ambient);
		Log.d("TEMPERATURE","ambient : "+ambient+" TARGET : "+target);
		setAmbientTemperature(ambient);
		setTargetTemperature(target);
    }
    
    @Override
    public void onCharacteristicWrite (BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
    	Log.d("DeviceRecord", name +" characteristic write");
    	writeQueue.issue();
    }
    
    @Override
    public void onDescriptorRead (BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    	Log.d("DeviceRecord", name +" descriptor read");
    }
    
    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    	writeQueue.issue();
    }
    
    @Override
    public void onReadRemoteRssi (BluetoothGatt gatt, int rssi, int status){
    	Log.d("DeviceRecord", name +" descriptor write");
    	this.rssi = rssi;
    }
    
    @Override
    public void onReliableWriteCompleted (BluetoothGatt gatt, int status){
    	Log.d("DeviceRecord", name +" some write read");
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
    }
    
    public void connectToDevice(Context context) {	
    	if(device != null)
    		mBluetoothGatt = this.device.connectGatt(context, true, this);
    }
    
  //TODO: add support for addPropertyChangeListener(propertyName, listener);
    public void addPropertyChangeListener(PropertyChangeListener listener) {
      //Don't add the same object twice. I can't imagine a use-case where you would want to do that.
      List<PropertyChangeListener> listeners = Arrays.asList(changeSupport.getPropertyChangeListeners());
      if (!listeners.contains(listener))
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
      changeSupport.removePropertyChangeListener(listener);
    }

    public void setAmbientTemperature(double newValue) {
      double oldValue = ambTemp;
      ambTemp = newValue;
     
      changeSupport.firePropertyChange(PROPERTY_AMBIENT_TEMPERATURE, null, new TemperatureChangeObject(oldValue, newValue));
    }

    public void setTargetTemperature(double newValue) {
    	double oldValue = targetTemp;
    	targetTemp = newValue;

    	changeSupport.firePropertyChange(PROPERTY_AMBIENT_TEMPERATURE, null,  new TemperatureChangeObject(oldValue, newValue));

    }

	

//    public void setAccelerometer(double x, double y, double z) {
//      Point3D newValue = new Point3D(x, y, z);
//      Point3D oldValue = accelerometer;
//      accelerometer = newValue;
//      changeSupport.firePropertyChange(PROPERTY_ACCELEROMETER, oldValue, newValue);
//    }
//
//    public void setHumidity(double newValue) {
//      double oldValue = humidity;
//      humidity = newValue;
//      changeSupport.firePropertyChange(PROPERTY_HUMIDITY, oldValue, newValue);
//    }
//
//    public void setMagnetometer(double x, double y, double z) {
//      Point3D newValue = new Point3D(x, y, z);
//      Point3D oldValue = magnetometer;
//      magnetometer = newValue;
//      changeSupport.firePropertyChange(PROPERTY_MAGNETOMETER, oldValue, newValue);
//    }
//
//    public void setGyroscope(float x, float y, float z) {
//      Point3D newValue = new Point3D(x, y, z);
//      Point3D oldValue = gyroscope;
//      gyroscope = newValue;
//      changeSupport.firePropertyChange(PROPERTY_GYROSCOPE, oldValue, newValue);
//    }
//
//    public void setSimpleKeysStatus(SimpleKeysStatus newValue) {
//      SimpleKeysStatus oldValue = status;
//      status = newValue;
//      changeSupport.firePropertyChange(PROPERTY_SIMPLE_KEYS, oldValue, newValue);
//    }
//
//    public SimpleKeysStatus getStatus() {
//      return status;
//    }
//
//    public double getIrTemperature() {
//      return irTemperature;
//    }
//
//    public double getAmbientTemperature() {
//      return ambientTemperature;
//    }
//
//    public Point3D getAccelerometer() {
//      return accelerometer;
//    }
       
}
