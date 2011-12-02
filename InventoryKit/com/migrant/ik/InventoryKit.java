/*
 * Created on Dec 2, 2011
 *
 */
package com.migrant.ik;

import java.util.Date;
import java.util.Hashtable;

public class InventoryKit {
	
	private static final Hashtable<String,Date> sActivatedProducts = new Hashtable<String,Date>();
	private static final Hashtable<String,Integer> sConsumableProducts = new Hashtable<String,Integer>();

	public static final void registerWithPaymentQueue() {
		
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
	}
	
	/**
	 * Set time-limited product activation (subscription)
	 * 
	 * @param productKey
	 * @param expirationDate last date subscription will be active
	 */
	public static final void activateProduct(String productKey, Date expirationDate) {
		sActivatedProducts.put(productKey, expirationDate);
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
