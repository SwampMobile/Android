package com.quanify.activities;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.quanify.fragments.SimpleAlertFragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BaseActivity extends SherlockFragmentActivity
{
	private ProgressDialog progress;
	private DialogFragment alert;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        
        alert = new SimpleAlertFragment();
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	  switch (item.getItemId()) {
	    case android.R.id.home: onBackPressed(); return true;
	  }
	    
	  return super.onOptionsItemSelected(item);
	}
    
    protected void showLoadingDialog(String title, String msg)
    {
    	progress = ProgressDialog.show(this, title, msg);
    }
    
    protected void hideLoadingDialog()
    {
    	progress.dismiss();
    }
    
    protected void showSimpleAlert(String title, String msg)
    {
    	Bundle args = new Bundle();
    	args.putString("title", title);
    	args.putString("message", msg);
    	alert.setArguments(args);
    	alert.show(this.getSupportFragmentManager(), title);
    }
    
    protected void hideSimpleAlert()
    {
    	alert.dismiss();
    }
}
