package com.quanify.activities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.quanify.R;
import com.quanify.adapters.DeviceListAdapter;
import com.quanify.app.MockData;
import com.quanify.models.DeviceRecord;

public class LocalDevicesActivity extends BaseActivity implements PropertyChangeListener
{
	private ListView listView;
	private DeviceListAdapter listAdapter;
	private BluetoothAdapter mBluetoothAdapter;
	BluetoothManager bluetoothManager;
	private boolean mScanning;
    private Handler mHandler = new Handler();
    private final static int REQUEST_ENABLE_BT = 1;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
	
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private Button syncDevices;
    
    private PropertyChangeListener listener = this;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_device_list);
        
        listView = (ListView) findViewById(R.id.device_list);
        listAdapter = new DeviceListAdapter(this);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() 
        {
			@Override
			public void onItemClick(AdapterView<?> listView, View item, int position, long id) 
			{
				DeviceRecord device = (DeviceRecord) listView.getAdapter().getItem(position);
				
				Intent deviceInfoIntent = new Intent(LocalDevicesActivity.this, DeviceInfoActivity.class);
				deviceInfoIntent.putExtra("device_id", device.id);
				
				startActivity(deviceInfoIntent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
        });
        
        bluetoothManager = (BluetoothManager) getSystemService(Context.
        	BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mLeScanCallback =
                new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, int rssi,
                    byte[] scanRecord) {
                runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
//                	   if(!addressList.contains(device.getAddress())) {
//                		   bluetoothDevList.add(device);
//                		   addressList.add(device.getAddress());
//                	   }
//                	   Log.d("Detected Bluetooth",""+bluetoothDevList.size());
                       MockData.getInstance().addDevice(getApplicationContext(), device,listener);
                       listAdapter.notifyDataSetChanged();
                   }
               });
           }
        };
        
        syncDevices = (Button) findViewById(R.id.sync_all);
        syncDevices.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				syncAll();
			}
		});
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
    	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    	} 
    	refreshList();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
    	this.getSupportMenuInflater().inflate(R.menu.device_list, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	  switch (item.getItemId()) {
	    case R.id.action_refresh: 
	    	refreshList();
	    	
	    return true;
	  }
	    
	  return super.onOptionsItemSelected(item);
	}
    
    private void syncAll() {
    	List<DeviceRecord> localDevices = new ArrayList<DeviceRecord>(MockData.getInstance().getLocalDevices());
		for(DeviceRecord dRecord :localDevices) {
			dRecord.connectToDevice(getApplicationContext());
		}
    }
    
    private void refreshList()
    {
    	scanLeDevice(true);
//    	showLoadingDialog("", "Refreshing Device Info...");
//    	
//    	(new Thread(new Runnable()
//    	{
//			@Override
//			public void run() 
//			{
//				try {
//					Thread.sleep(1500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				runOnUiThread(new Runnable()
//				{
//					@Override
//					public void run() 
//					{
//						hideLoadingDialog();
//					}
//				});
//			}
//    	})).start();
    }
    
    private void scanLeDevice(final boolean enable) {
        if (enable) {
        	showLoadingDialog("", "Refreshing Device Info...");
        	MockData.getInstance().refershList();
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    runOnUiThread(new Runnable()
    				{
    					@Override
    					public void run() 
    					{
    						hideLoadingDialog();
    					}
    				});
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    @Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
    	runOnUiThread(new Runnable() {
    		@Override
    		public void run() {
    			listAdapter.notifyDataSetChanged();
    		}});
		
	}
    
}
