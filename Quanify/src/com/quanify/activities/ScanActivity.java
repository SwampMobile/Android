package com.quanify.activities;

import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.quanify.R;
import com.quanify.fragments.ScanFragment;
import com.quanify.fragments.SyncJSONFragment;

public class ScanActivity extends BaseActivity implements ScanFragment.BarcodeDataProcessor
{
	private String barcodeValue = "";
	
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.activity_scan);
		
		ScanFragment scanFragment = new ScanFragment();
		scanFragment.setDataProcessor(this);
		
		FragmentManager fragManager = getSupportFragmentManager();
		fragManager.beginTransaction()
			.replace(R.id.content, scanFragment)
			.commit();
	}
	
	private void openForm()
	{
		SyncJSONFragment formFragment = new SyncJSONFragment();
		
		if(barcodeValue != null)
			formFragment.setBarcodeValue(barcodeValue);
		
		FragmentManager fragManager = getSupportFragmentManager();
		fragManager.beginTransaction()
			.replace(R.id.content, formFragment)
			.commit();
	}

	@Override
	public void processData(SymbolSet data) 
	{
//		Log.d(TAG, "Processing scanned data: ");
//		for (Symbol sym : data) {
//			Log.d(TAG, sym.getData());
//        }
		
//		scannedMessage = new QRMessage();
		try 
		{
			barcodeValue = "";
			
			for(Symbol symbol : data)
			{
				barcodeValue += symbol.getData();
			}
//			scannedMessage.parse( ((Symbol)data.toArray()[0]).getData() );
//			Log.d(TAG, "Message: " + scannedMessage.toString());
			openForm();
		}
		catch(Exception e)
		{
			// Non-compliant QR code, we'll just ignore it
//			scannedMessage = null;
		}
		
		
	}

	@Override
	public void skipRequested() 
	{
		openForm();
	}
}
