/*
 * Created on Dec 2, 2011
 *
 */
package com.migrant.ik.billing;

import com.android.vending.billing.IMarketBillingService;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class BillingService extends Service implements ServiceConnection {

	private Context mContext;
	private IMarketBillingService mService;
	private static final String TAG = "BillingService";
	private static final String BILLING_REQUEST = "BillingRequest";
	private static final String API_VERSION = "ApiVersion";
	private static final String PACKAGE_NAME = "PackageName";
	  
	@Override
	public void onCreate() {
		try {
			boolean bindResult = mContext.bindService(new Intent(
					"com.android.vending.billing.MarketBillingService.BIND"),
					this, Context.BIND_AUTO_CREATE);
			if (bindResult) {
				Log.i(TAG, "Service bind successful.");
			} else {
				Log.e(TAG, "Could not bind to the MarketBillingService.");
			}
		} catch (SecurityException e) {
			Log.e(TAG, "Security exception: " + e);
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	protected Bundle makeRequestBundle(String method) {
		  Bundle request = new Bundle();
		  request.putString(BILLING_REQUEST, method);
		  request.putInt(API_VERSION, 1);
		  request.putString(PACKAGE_NAME, getPackageName());
		  return request;
	}
	
	/**
	  * The Android system calls this when we are connected to the MarketBillingService.
	  */
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.i(TAG, "MarketBillingService connected.");
	    mService = IMarketBillingService.Stub.asInterface(service);
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
	}
	
}
