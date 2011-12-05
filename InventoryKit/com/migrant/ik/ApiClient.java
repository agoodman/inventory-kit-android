/*
 * Created on Dec 4, 2011
 *
 */
package com.migrant.ik;

public interface ApiClient {

	public Customer getCustomer();
	public void findOrCreateCustomerByEmail(String email, CustomerListener listener);
	
}
