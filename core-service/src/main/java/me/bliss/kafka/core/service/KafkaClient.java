package me.bliss.kafka.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import me.bliss.kafka.core.model.KafkaBroker;
import me.bliss.kafka.core.model.KafkaPartition;
import me.bliss.kafka.core.model.KafkaTopic;
import me.bliss.kafka.core.service.exception.JsonParseException;
import me.bliss.kafka.core.service.exception.ZookeeperException;
import me.bliss.kafka.core.service.model.constant.KafkaConstants;
import me.bliss.kafka.core.service.utils.KafkaObjectConverter;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;

/**
 * kafka client
 * It provide some function to handle kafka
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.service, v 0.1 7/9/15
 *          Exp $
 */
public class KafkaClient implements InitializingBean {

    private ZookeeperClient zookeeperClient;

    private ObjectMapper mapper;

    private SimpleConsumer simpleConsumer;

    private int timeout;

    private int bufferSize;

    public KafkaClient() {
        mapper = new ObjectMapper();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        createSimpleConsumer();
    }

    private void createSimpleConsumer() {
        try {
            final List<KafkaBroker> brokers = getBrokers();
            if (brokers.isEmpty()) {
                throw new Exception("NOT EXISTS KAFKA BROKER");
            }
            final KafkaBroker broker = brokers.get(0);
            simpleConsumer = new SimpleConsumer(
                    broker.getHost(),
                    broker.getPort(),
                    timeout,
                    bufferSize,
                    String.valueOf(new Date().getTime()));
        } catch (Exception e) {
            throw new RuntimeException("CREATE BROKER CONNECTION FAIL!");
        }
    }

    public void destory() {
        simpleConsumer.close();
    }

    public List<KafkaBroker> getBrokers() throws ZookeeperException, JsonParseException {
        try {
            final List<String> brokerIds = zookeeperClient
                    .getChildren(KafkaConstants.BROKER_IDS_PATH);
            final List<KafkaBroker> kafkaBrokers = new ArrayList<KafkaBroker>();
            for (String brokerId : brokerIds) {
                String broker = zookeeperClient
                        .getData(KafkaConstants.BROKER_IDS_PATH + "/" + brokerId);
                final KafkaBroker kafkaBroker = mapper.readValue(broker, KafkaBroker.class);
                kafkaBroker.setId(Integer.parseInt(brokerId));
                kafkaBrokers.add(kafkaBroker);
            }
            return kafkaBrokers;
        } catch (ZookeeperException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public List<KafkaTopic> getTopics() throws ZookeeperException {
        final List<String> topics = zookeeperClient.getChildren(KafkaConstants.TOPIC_PATH);
        return getTopicDetail(topics);
    }

    public List<KafkaTopic> getTopicDetail(List<String> topics) {
        final TopicMetadataRequest topicMetadataRequest = new TopicMetadataRequest(topics);
        final ArrayList<KafkaTopic> kafkaTopics = new ArrayList<>();
        final TopicMetadataResponse topicMetadataResponse = simpleConsumer
                .send(topicMetadataRequest);
        final List<TopicMetadata> topicMetadatas = topicMetadataResponse.topicsMetadata();
        for (TopicMetadata topicMetadata : topicMetadatas) {
            final KafkaTopic kafkaTopic = new KafkaTopic();
            final ArrayList<KafkaPartition> kafkaPartitions = new ArrayList<>();
            kafkaTopic.setName(topicMetadata.topic());
            final Iterator<PartitionMetadata> metadataIterator = topicMetadata.partitionsMetadata()
                    .iterator();
            while (metadataIterator.hasNext()) {
                final KafkaPartition kafkaPartition = new KafkaPartition();
                final PartitionMetadata partitionMetadata = metadataIterator.next();
                kafkaPartition.setId(partitionMetadata.partitionId());
                kafkaPartition
                        .setLeader(KafkaObjectConverter.convertBroker(partitionMetadata.leader()));
                kafkaPartition.setReplicas(
                        KafkaObjectConverter.batchConvertBroker(partitionMetadata.replicas()));
                kafkaPartitions.add(kafkaPartition);
            }
            kafkaTopic.setPartitions(kafkaPartitions);
            kafkaTopics.add(kafkaTopic);
        }
        return kafkaTopics;
    }

    public void setZookeeperClient(ZookeeperClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

}
