package me.bliss.kafka.biz.service;

import me.bliss.kafka.core.model.KafkaBroker;
import me.bliss.kafka.core.model.KafkaTopic;
import me.bliss.kafka.core.service.KafkaClient;
import me.bliss.kafka.core.service.exception.JsonParseException;
import me.bliss.kafka.core.service.exception.ZookeeperException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * kafka service,provide some advanced method
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.biz.service, v 0.1 7/9/15
 *          Exp $
 */
public class KafkaService {

    private KafkaClient kafkaClient;

    public List<KafkaBroker> getKafkaBrokers(){
        try {
            return kafkaClient.getBrokers();
        } catch (ZookeeperException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<KafkaTopic> getKafkaTopics(){
        try {
            return kafkaClient.getTopics();
        } catch (ZookeeperException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public HashMap<String, Object> getKafkaStatus() {
        try {
            final List<KafkaBroker> brokers = kafkaClient.getBrokers();
            final List<KafkaTopic> topics = kafkaClient.getTopics();
            final HashMap<String, Object> kafka = new HashMap<String, Object>();
            kafka.put("brokers",brokers);
            kafka.put("topics",topics);
            return kafka;
        } catch (ZookeeperException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setKafkaClient(KafkaClient kafkaClient) {
        this.kafkaClient = kafkaClient;
    }
}
