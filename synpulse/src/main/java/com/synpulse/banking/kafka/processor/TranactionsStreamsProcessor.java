package com.synpulse.banking.kafka.processor;

import java.util.Objects;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.synpulse.banking.payload.TransactionPayLoad;


@Component
public class TranactionsStreamsProcessor implements Processor<Long, TransactionPayLoad> {

    private final Logger log = LoggerFactory.getLogger(TranactionsStreamsProcessor.class);

    private KeyValueStore<Long, TransactionPayLoad> stateStore;

    @Value(value = "${kafka.streams.stateStoreName}")
    private String stateStoreName;

    @Override
    public void init(ProcessorContext context) {
        stateStore =  context.getStateStore(stateStoreName);
        Objects.requireNonNull(stateStore, "State store can't be null");
    }

    @Override
    public void process(Long txId, TransactionPayLoad transactions) {
        log.info("Streams Request to save process transactions : {}", transactions);

        stateStore.put(txId, transactions);
    }

    @Override
    public void close() {

    }
}
