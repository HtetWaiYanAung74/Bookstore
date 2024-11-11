package com.bookstore.user.service;

import com.bookstore.user.domain.BillingAddress;
import com.bookstore.user.domain.UserBilling;

public interface BillingAddressService {

	BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
	
}
