package com.quanify.fragments;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.quanify.R;
import com.quanify.views.CameraPreview;

public class ScanFragment extends Fragment
{
	private ViewGroup cameraPreviewContainer;
	
	private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    TextView scanText;
    Button scanButton;

    ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;
    
    private BarcodeDataProcessor dataProcessor;

    static {
        System.loadLibrary("iconv");
    } 
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_scan, container, false);
		
		cameraPreviewContainer = (FrameLayout)view.findViewById(R.id.camera_preview);
		
		scanText = (TextView)view.findViewById(R.id.scan_text);
		
		Button skipButton = (Button)view.findViewById(R.id.skip_button);
		skipButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(dataProcessor != null)
					dataProcessor.skipRequested();
			}
			
		});
		
		return view;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this.getActivity(), mCamera, previewCb, autoFocusCB);
        cameraPreviewContainer.addView(mPreview);

        doScan();

        //scanButton = (Button)findViewById(R.id.ScanButton);

//        scanButton.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    if (barcodeScanned) {
//                        barcodeScanned = false;
//                        scanText.setText("Scanning...");
//                        mCamera.setPreviewCallback(previewCb);
//                        mCamera.startPreview();
//                        previewing = true;
//                        mCamera.autoFocus(autoFocusCB);
//                    }
//                }
//            });
	}
	
	public void doScan()
	{
		if (barcodeScanned) {
			barcodeScanned = false;
			scanText.setText("Scanning...");
			mCamera.setPreviewCallback(previewCb);
			mCamera.startPreview();
			previewing = true;
			mCamera.autoFocus(autoFocusCB);
		}
	}
	
	public void onPause() {
        super.onPause();
        releaseCamera();
    }
	
	public void setDataProcessor(BarcodeDataProcessor processor)
	{
		dataProcessor = processor;
	}
	
	public interface BarcodeDataProcessor
	{
		void processData(SymbolSet data);
		void skipRequested();
	}
	
	/** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (previewing)
                    mCamera.autoFocus(autoFocusCB);
            }
        };

    PreviewCallback previewCb = new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();

                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);

                int result = scanner.scanImage(barcode);
                
                if (result != 0) {
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    
                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                        scanText.setText(sym.getData());
                        barcodeScanned = true;
                    }
                    
                    if(dataProcessor != null)
                    {
                    	dataProcessor.processData(syms);
                    }
                    
                }
            }
        };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                autoFocusHandler.postDelayed(doAutoFocus, 1000);
            }
        };
}

