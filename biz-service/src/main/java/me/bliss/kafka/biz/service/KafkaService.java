package me.bliss.kafka.biz.service;

import me.bliss.kafka.core.model.*;
import me.bliss.kafka.core.model.zookeeper.ZookeeperMeta;
import me.bliss.kafka.core.service.KafkaClient;
import me.bliss.kafka.core.service.ZookeeperClient;
import me.bliss.kafka.core.service.exception.JsonParseException;
import me.bliss.kafka.core.service.exception.ZookeeperException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * kafka service,provide some advanced method
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.biz.service, v 0.1 7/9/15
 *          Exp $
 */
public class KafkaService {

    private KafkaClient kafkaClient;

    @Autowired
    private ZookeeperClient zookeeperClient;

    public ZookeeperMeta getZookeeperMeta() {
        return zookeeperClient.getZookeeperMeta();
    }

    public List<KafkaBroker> getKafkaBrokers() {
        try {
            return kafkaClient.getBrokers();
        } catch (ZookeeperException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<KafkaTopic> getKafkaTopics() {
        try {
            return kafkaClient.getTopics();
        } catch (ZookeeperException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<KafkaTopicMeta> getKafkaTopicsMeta() {
        try {
            final List<KafkaTopicMeta> kafkaTopicMetas = new ArrayList<>();
            for (KafkaTopic kafkaTopic : kafkaClient.getTopics()) {
                final KafkaTopicMeta kafkaTopicMeta = new KafkaTopicMeta();
                final List<Integer> partitionIds = new ArrayList<>();
                final Set<Integer> replicaIds = new HashSet<>();
                for (KafkaPartition kafkaPartition : kafkaTopic.getPartitions()) {
                    for (KafkaBroker kafkaBroker : kafkaPartition.getReplicas()) {
                        replicaIds.add(kafkaBroker.getId());
                    }
                    partitionIds.add(kafkaPartition.getId());
                }
                kafkaTopicMeta.setName(kafkaTopic.getName());
                kafkaTopicMeta.setPartitionIds(partitionIds);
                kafkaTopicMeta.setReplicaIds(replicaIds);
                kafkaTopicMetas.add(kafkaTopicMeta);
            }
            return kafkaTopicMetas;
        } catch (ZookeeperException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<KafkaConsumerGroup> getKafkaConsumerGroups() {
        try {
            return kafkaClient.getConsumerGroup();
        } catch (ZookeeperException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ArrayList<KafkaConsumerGroupMeta> getKafkaConsumerGroupMeta(){
        try {
            final List<KafkaConsumerGroup> consumerGroups = kafkaClient.getConsumerGroup();
            final ArrayList<KafkaConsumerGroupMeta> kafkaConsumerGroupMetas = new ArrayList<>();
            for(KafkaConsumerGroup kafkaConsumerGroup : consumerGroups){
                final KafkaConsumerGroupMeta kafkaConsumerGroupMeta = new KafkaConsumerGroupMeta();
                final List<String> owners = new ArrayList<>();
                for (KafkaTopicOffset kafkaTopicOffset : kafkaConsumerGroup.getOwners()){
                    owners.add(kafkaTopicOffset.getName());
                }
                kafkaConsumerGroupMeta.setName(kafkaConsumerGroup.getName());
                kafkaConsumerGroupMeta.setOwners(owners);
                kafkaConsumerGroupMetas.add(kafkaConsumerGroupMeta);
            }
            return kafkaConsumerGroupMetas;
        } catch (ZookeeperException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, Object> getKafkaStatus() {
        try {
            final List<KafkaBroker> brokers = kafkaClient.getBrokers();
            final List<KafkaTopic> topics = kafkaClient.getTopics();
            final HashMap<String, Object> kafka = new HashMap<String, Object>();
            kafka.put("brokers", brokers);
            kafka.put("topics", topics);
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

    public void setZookeeperClient(ZookeeperClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }
}
