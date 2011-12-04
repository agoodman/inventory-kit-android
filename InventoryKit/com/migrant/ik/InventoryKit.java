/*
 * Created on Dec 2, 2011
 *
 */
package com.migrant.ik;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import static android.content.Context.MODE_PRIVATE;

public class InventoryKit {
	
	private static final Hashtable<String,Date> sActivatedProducts = new Hashtable<String,Date>();
	private static final Hashtable<String,Integer> sConsumableProducts = new Hashtable<String,Integer>();
	private static SharedPreferences sPreferences;

	public static final void registerWithPaymentQueue(Activity activity) {
		loadSettings(activity);
	}
	
	public static final void loadSettings(Activity activity) {
		Log.d("InventoryKit", "loading preferences");
		
		if( sPreferences==null ) {
			Log.d("InventoryKit", "assigning preferences pointer");
			sPreferences = activity.getPreferences(MODE_PRIVATE);
		}
		
		String tProductKeys = sPreferences.getString("ActivatedProducts", "");
		if( tProductKeys.length()>0 ) {
			String[] tKeys = tProductKeys.split(",");
			for (String key : tKeys) {
				String[] kv = key.split(":");
				sActivatedProducts.put(kv[0], new Date(kv[1]));
			}
		}

		String tConsumableKeys = sPreferences.getString("ConsumableProducts", "");
		if( tConsumableKeys.length()>0 ) {
			String[] tKeys = tConsumableKeys.split(",");
			for (String key : tKeys) {
				String[] kv = key.split(":");
				sConsumableProducts.put(kv[0], new Integer(kv[1]));
			}
		}
	}
	
	public static final void saveSettings() {
		Log.d("InventoryKit", "saving preferences");
		
		// save activated products
		StringBuilder tBuilder = new StringBuilder();
		
		Enumeration<String> tKeys = sActivatedProducts.keys();
		while( tKeys.hasMoreElements() ) {
			String key = tKeys.nextElement();
			tBuilder.append(key);
			tBuilder.append(":");
			tBuilder.append(sActivatedProducts.get(key));
			tBuilder.append(",");
		}
		tBuilder.trimToSize();
		String tString = tBuilder.toString();
		if( tString==null || tString=="" ) {
			sPreferences.edit().remove("ActivatedProducts").commit();
		}else{
			sPreferences.edit().putString("ActivatedProducts", tString).commit();
		}
		
		// save consumable products
		tBuilder = new StringBuilder();
		
		tKeys = sConsumableProducts.keys();
		while( tKeys.hasMoreElements() ) {
			String key = tKeys.nextElement();
			tBuilder.append(key);
			tBuilder.append(":");
			tBuilder.append(sConsumableProducts.get(key));
			tBuilder.append(",");
		}
		tBuilder.trimToSize();
		tString = tBuilder.toString();
		if( tString==null || tString=="" ) {
			sPreferences.edit().remove("ConsumableProducts").commit();
		}else{
			sPreferences.edit().putString("ConsumableProducts", tString).commit();
		}
		
	}
	
	/**
	 * Query product activation
	 * 
	 * @param productKey
	 * @return true if product with given key is active, false otherwise
	 */
	public static final boolean productActivated(String productKey) {
		Date tNow = new Date();
		Date tDate = sActivatedProducts.get(productKey);
		return tNow.compareTo(tDate)>0;
	}
	
	/**
	 * Set product activation (non-consumable)
	 * 
	 * @param productKey
	 */
	public static final void activateProduct(String productKey) {
		sActivatedProducts.put(productKey, null);
		saveSettings();
	}
	
	/**
	 * Set time-limited product activation (subscription)
	 * 
	 * @param productKey
	 * @param expirationDate last date subscription will be active
	 */
	public static final void activateProduct(String productKey, Date expirationDate) {
		sActivatedProducts.put(productKey, expirationDate);
		saveSettings();
	}
	
	/**
	 * Set product quantity (consumable)
	 * 
	 * @param productKey
	 * @param quantity number of items to add to total
	 */
	public static final void activateProduct(String productKey, int quantity) {
		Integer tValue = sConsumableProducts.get(productKey);
		if( tValue==null ) {
			sConsumableProducts.put(productKey, new Integer(quantity));
		}else{
			int tQuantity = tValue.intValue();
			tQuantity += quantity;
			sConsumableProducts.put(productKey, new Integer(tQuantity));
		}
		saveSettings();
	}
	
	/**
	 * Query available quantity (consumable)
	 * 
	 * @param productKey 
	 * @return number of items available
	 */
	public static final int quantityAvailable(String productKey) {
		Integer tValue = sConsumableProducts.get(productKey);
		if( tValue==null ) {
			return 0;
		}else{
			return tValue.intValue();
		}
	}
	
	/**
	 * Consume quantity of product items (consumable)
	 * 
	 * @param productKey
	 * @param quantity number of items to consume
	 * @return true if items successfully consumed, false if fewer than quantity were consumed
	 */
	public static final boolean consumeQuantity(String productKey, int quantity) {
		Integer tValue = sConsumableProducts.get(productKey);
		if( tValue==null ) {
			return false;
		}else{
			int tQuantity = tValue.intValue();
			tQuantity -= quantity;
			if( tQuantity>0 ) {
				sConsumableProducts.put(productKey, new Integer(tQuantity));
			}else{
				sConsumableProducts.remove(productKey);
			}
			return tQuantity >= 0;
		}
	}
	
}
