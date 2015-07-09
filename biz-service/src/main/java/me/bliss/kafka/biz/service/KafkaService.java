package me.bliss.kafka.biz.service;

import me.bliss.kafka.core.model.KafkaBroker;
import me.bliss.kafka.core.service.KafkaClient;
import me.bliss.kafka.core.service.exception.JsonParseException;
import me.bliss.kafka.core.service.exception.ZookeeperException;

import java.util.Map;

/**
 * kafka service,provide some advanced method
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.biz.service, v 0.1 7/9/15
 *          Exp $
 */
public class KafkaService {

    private KafkaClient kafkaClient;

    public void getKafkaStatus() {
        try {
            final Map<String, KafkaBroker> brokers = kafkaClient.getBrokers();
        } catch (ZookeeperException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
    }

    public void setKafkaClient(KafkaClient kafkaClient) {
        this.kafkaClient = kafkaClient;
    }
}
