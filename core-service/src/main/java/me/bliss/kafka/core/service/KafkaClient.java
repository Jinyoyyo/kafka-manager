package me.bliss.kafka.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import me.bliss.kafka.core.model.KafkaBroker;
import me.bliss.kafka.core.service.exception.JsonParseException;
import me.bliss.kafka.core.service.exception.ZookeeperException;
import me.bliss.kafka.core.service.model.constant.KafkaConstants;
import org.apache.commons.lang.ArrayUtils;
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
    public void afterPropertiesSet() {
        try {
            final Map<String, KafkaBroker> brokers = getBrokers();
            final Set<String> keys = brokers.keySet();
            if (keys.isEmpty()) {
                throw new Exception("NOT EXISTS KAFKA BROKER");
            }
            final KafkaBroker broker = brokers.get(keys.toArray()[0]);
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

    public Map<String, KafkaBroker> getBrokers() throws ZookeeperException, JsonParseException {
        try {
            final List<String> brokerIds = zookeeperClient
                    .getChildren(KafkaConstants.BROKER_IDS_PATH);
            final Map<String, KafkaBroker> brokers = new HashMap<>();
            for (String brokerId : brokerIds) {
                String broker = zookeeperClient
                        .getData(KafkaConstants.BROKER_IDS_PATH + "/" + brokerId);
                brokers.put(brokerId, mapper.readValue(broker, KafkaBroker.class));
            }
            return brokers;
        } catch (ZookeeperException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public void getTopics() {
        try {
            final List<String> topics = zookeeperClient.getChildren(KafkaConstants.TOPIC_PATH);
            for (String topic : topics) {
                final String data = zookeeperClient
                        .getData(KafkaConstants.TOPIC_PATH + "/" + topic);

            }
        } catch (ZookeeperException e) {
            e.printStackTrace();
        }
    }

    public void getLeader(String topic) {
        final TopicMetadataRequest topicMetadataRequest = new TopicMetadataRequest(
                Collections.singletonList(topic));
        final TopicMetadataResponse topicMetadataResponse = simpleConsumer
                .send(topicMetadataRequest);
        final List<TopicMetadata> topicMetadatas = topicMetadataResponse.topicsMetadata();
        for (TopicMetadata topicMetadata : topicMetadatas) {
            final List<PartitionMetadata> partitionMetadatas = topicMetadata.partitionsMetadata();
            final Iterator<PartitionMetadata> metadataIterator = partitionMetadatas.iterator();
            while (metadataIterator.hasNext()) {
                System.out.println(metadataIterator.next().isr());
            }
            System.out.println(ArrayUtils.toString(partitionMetadatas));
        }
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
