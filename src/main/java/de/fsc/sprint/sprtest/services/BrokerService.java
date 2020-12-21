package de.fsc.sprint.sprtest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class BrokerService {

    private static final String TOPIC = "weather";
    private KafkaTemplate<String, String> kafkaTemplate;

    public BrokerService(BrokerConfig brokerConfig) {
        kafkaTemplate = brokerConfig.createKafkaTemplate();
    }


    public void sendMessage(String city, List<Double> temp) {
        try {
            String tempJson = new ObjectMapper().writeValueAsString(temp);
            log.info(String.format("#### -> Producing message -> %s", city + " " + tempJson));
            final ListenableFuture<SendResult<String, String>> sendResultListenableFuture = this.kafkaTemplate.send(TOPIC, city, tempJson);
            final SendResult<String, String> stringSendResult = sendResultListenableFuture.get();
            log.info(stringSendResult.getProducerRecord().toString());
        } catch (InterruptedException e) {
            log.error("Kafka senden unterbrochen....");
        } catch (ExecutionException e) {
            log.error("Kafka senden allg. Fehler!");
        } catch (JsonProcessingException e) {
            log.error("Kafka senden JSON-Fehler!");
        }
    }
}
