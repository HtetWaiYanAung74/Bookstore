package com.bookstore.user.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class BillingAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long billingAddressId;
	private String billingAddressName;
	private String billingAddressStreet1;
	private String billingAddressStreet2;
	private String billingAddressCity;
	private String billingAddressState;
	private String billingAddressCountry;
	private String billingAddressZipcode;
	
	@OneToOne
	private Order order;
	
	public BillingAddress() {}

	public Long getBillingAddressId() {
		return billingAddressId;
	}

	public void setBillingAddressId(Long billingAddressId) {
		this.billingAddressId = billingAddressId;
	}

	public String getBillingAddressName() {
		return billingAddressName;
	}

	public void setBillingAddressName(String billingAddressName) {
		this.billingAddressName = billingAddressName;
	}

	public String getBillingAddressStreet1() {
		return billingAddressStreet1;
	}

	public void setBillingAddressStreet1(String billingAddressStreet1) {
		this.billingAddressStreet1 = billingAddressStreet1;
	}

	public String getBillingAddressStreet2() {
		return billingAddressStreet2;
	}

	public void setBillingAddressStreet2(String billingAddressStreet2) {
		this.billingAddressStreet2 = billingAddressStreet2;
	}

	public String getBillingAddressCity() {
		return billingAddressCity;
	}

	public void setBillingAddressCity(String billingAddressCity) {
		this.billingAddressCity = billingAddressCity;
	}

	public String getBillingAddressState() {
		return billingAddressState;
	}

	public void setBillingAddressState(String billingAddressState) {
		this.billingAddressState = billingAddressState;
	}

	public String getBillingAddressCountry() {
		return billingAddressCountry;
	}

	public void setBillingAddressCountry(String billingAddressCountry) {
		this.billingAddressCountry = billingAddressCountry;
	}

	public String getBillingAddressZipcode() {
		return billingAddressZipcode;
	}

	public void setBillingAddressZipcode(String billingAddressZipcode) {
		this.billingAddressZipcode = billingAddressZipcode;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
}
