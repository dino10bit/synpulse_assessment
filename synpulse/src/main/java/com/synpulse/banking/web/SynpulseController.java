package com.synpulse.banking.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.synpulse.banking.payload.ErrorMessage;
import com.synpulse.banking.payload.RequestPayLoad;
import com.synpulse.banking.payload.TransactionPayLoad;
import com.synpulse.banking.service.SynpulseService;

@RestController
@RequestMapping("/synpulse/customer")
public class SynpulseController {
	private final Logger log = LoggerFactory.getLogger(SynpulseController.class);
	@Autowired
	SynpulseService synpulseService;
	
	@PostMapping("/transactions")
	@ResponseBody
	public ResponseEntity<Object> createSettlementMessage(@RequestBody RequestPayLoad requestPayLoad){
		log.info("Inside SynpulseController");
		try {
			List<TransactionPayLoad> responsePayLoad =  synpulseService.fetchCustomerTransactions(requestPayLoad);
		return new ResponseEntity<Object>(responsePayLoad, HttpStatus.OK);
		}
		catch (Exception e) {
//			e.printStackTrace();
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(e.getMessage());
			return new ResponseEntity<Object>(errorMessage, HttpStatus.BAD_REQUEST);
		}
	} 
}
