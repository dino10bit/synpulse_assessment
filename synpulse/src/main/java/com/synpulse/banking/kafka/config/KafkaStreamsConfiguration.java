package com.synpulse.banking.kafka.config;


import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG;

import java.util.Properties;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.synpulse.banking.kafka.processor.TranactionsStreamsProcessor;
import com.synpulse.banking.payload.TransactionPayLoad;

@Configuration
public class KafkaStreamsConfiguration {

    private final Logger log = LoggerFactory.getLogger(KafkaStreamsConfiguration.class);

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.streams.applicationId}")
    private String applicationId;

    @Value(value = "${kafka.topics.msgTransactions.name}")
    private String msgTransactionsTopic;

    @Value(value = "${kafka.streams.stateStoreName}")
    private String stateStoreName;

    private final ObjectFactory<TranactionsStreamsProcessor> TranactionsStreamsProcessorObjectFactory;

    private final Deserializer<Long> keyDeSerializer = new LongDeserializer();

    private final Deserializer<TransactionPayLoad> valueDeSerializer =
            new JsonDeserializer<>(TransactionPayLoad.class).ignoreTypeHeaders();

    private final Serde<Long> keySerializer = Serdes.Long();

    private final Serde<TransactionPayLoad> valueSerializer = new JsonSerde<>(TransactionPayLoad.class).ignoreTypeHeaders();

    public KafkaStreamsConfiguration(ObjectFactory<TranactionsStreamsProcessor> TranactionsStreamsProcessorObjectFactory) {
        this.TranactionsStreamsProcessorObjectFactory = TranactionsStreamsProcessorObjectFactory;
    }

    public TranactionsStreamsProcessor getTranactionsStreamsProcessor() {
        return TranactionsStreamsProcessorObjectFactory.getObject();
    }


    @Bean
    @Primary
    public KafkaStreams kafkaStreams() {
        log.info("Create Kafka Stream Bean with defined topology");
        Topology topology = this.buildTopology(new StreamsBuilder());
        final KafkaStreams kafkaStreams = new KafkaStreams(topology, createConfigurationProperties());
        kafkaStreams.start();
        return kafkaStreams;
    }

    /**
     * This method is used for defining topology for KafkaStreams
     * Topology:
     * 1. read the topic
     * 2. send to stream processor for processing the message
     * 3. persist message to key-value State Store
     *
     * @param streamsBuilder new Stream Builder
     * @return Topology
     */
    public Topology buildTopology(StreamsBuilder streamsBuilder) {
        Topology topology = streamsBuilder.build();

        StoreBuilder<KeyValueStore<Long, TransactionPayLoad>> stateStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(stateStoreName), keySerializer, valueSerializer);

        topology.addSource("Source", keyDeSerializer, valueDeSerializer, msgTransactionsTopic)
                .addProcessor("Process", this::getTranactionsStreamsProcessor, "Source")
                .addStateStore(stateStoreBuilder, "Process");
        return topology;
    }

    /**
     * This method is used for setting the configuration of Kafka Stream
     *
     * @return Properties
     */
    private Properties createConfigurationProperties() {
        final Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(APPLICATION_ID_CONFIG, applicationId);
        props.put(DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
        props.put(StreamsConfig.STATE_DIR_CONFIG, "D:\\statestore");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        return props;
    }
}

