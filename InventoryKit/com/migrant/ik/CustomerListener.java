/*
 * Created on Dec 4, 2011
 *
 */
package com.migrant.ik;

public interface CustomerListener {

	public void customerFound(Customer customer);
	public void customerCreated(Customer customer);
	public void customerError();
	
}
