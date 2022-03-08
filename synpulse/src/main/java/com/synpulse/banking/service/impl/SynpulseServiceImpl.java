package com.synpulse.banking.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.synpulse.banking.payload.RequestPayLoad;
import com.synpulse.banking.payload.TransactionPayLoad;
import com.synpulse.banking.service.SynpulseService;

@Service
public class SynpulseServiceImpl implements SynpulseService{

	private final Logger log = LoggerFactory.getLogger(SynpulseServiceImpl.class);
	
	
	private final KafkaStreams kafkaStreams;

    @Value(value = "${kafka.streams.stateStoreName}")
    private String stateStoreName;

    @Autowired
    public SynpulseServiceImpl(KafkaStreams kafkaStreams) {
        this.kafkaStreams = kafkaStreams;
    }
    
	@Override
	public List<TransactionPayLoad> fetchCustomerTransactions(RequestPayLoad requestPayLoad) {
		log.info("Inside SynpulseServiceImpl fetchCustomerTransactions");
		List<TransactionPayLoad> list =  new ArrayList<TransactionPayLoad>();
		KeyValueIterator<Long, TransactionPayLoad> iterator = getStore().all();
		while(iterator.hasNext()) {
			list.add(iterator.next().value);
		}
		iterator.close();
		return list;
	}
	
	private ReadOnlyKeyValueStore<Long, TransactionPayLoad> getStore() {
		
		return kafkaStreams.store(StoreQueryParameters.fromNameAndType(stateStoreName, QueryableStoreTypes.keyValueStore()));
	}

}
