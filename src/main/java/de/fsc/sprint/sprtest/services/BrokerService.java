package de.fsc.sprint.sprtest.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class BrokerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "weather";

    @Bean
    public KafkaTemplate<String, String> createKafkaTemplate() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(config));
    }

    public void sendMessage(String city, List<Double> temp) {
        log.info(String.format("#### -> Producing message -> %s", city + " " + temp));
        final ListenableFuture<SendResult<String, String>> sendResultListenableFuture = this.kafkaTemplate.send(TOPIC, city, temp.toString());
        try {
            final SendResult<String, String> stringSendResult = sendResultListenableFuture.get();
            log.info(stringSendResult.getProducerRecord().toString());
        } catch (InterruptedException e) {
            log.error("Kafka senden unterbrochen....");
        } catch (ExecutionException e) {
            log.error("Kafka senden allg. Fehler!");
        }
    }
}
