package com.synpulse.banking.service;

import java.util.List;

import com.synpulse.banking.payload.RequestPayLoad;
import com.synpulse.banking.payload.TransactionPayLoad;

public interface SynpulseService {

	public List<TransactionPayLoad> fetchCustomerTransactions(RequestPayLoad requestPayLoad) ;
}
