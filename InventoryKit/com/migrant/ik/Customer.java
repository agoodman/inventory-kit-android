/*
 * Created on Dec 4, 2011
 *
 */
package com.migrant.ik;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonTypeName;

@JsonTypeName( value = "Customer" )
@JsonAutoDetect
public class Customer {

	String email;
	Subscription[] subscriptions;
	
	public Customer() {}
	
	public Customer(String aEmail) {
		this.email = aEmail;
	}

	public final String getEmail() {
		return email;
	}

	public final void setEmail(String email) {
		this.email = email;
	}

	public final Subscription[] getSubscriptions() {
		return subscriptions;
	}

	public final void setSubscriptions(Subscription[] subscriptions) {
		this.subscriptions = subscriptions;
	}
	
}
