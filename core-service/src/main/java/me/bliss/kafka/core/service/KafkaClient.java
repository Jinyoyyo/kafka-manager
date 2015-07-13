package me.bliss.kafka.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import me.bliss.kafka.core.model.*;
import me.bliss.kafka.core.service.exception.JsonParseException;
import me.bliss.kafka.core.service.exception.ZookeeperException;
import me.bliss.kafka.core.service.model.constant.KafkaConstants;
import me.bliss.kafka.core.service.utils.KafkaConsumerFactory;
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
                throw new ZookeeperException("NOT EXISTS KAFKA BROKER");
            }
            final KafkaBroker broker = brokers.get(0);
            simpleConsumer = new SimpleConsumer(
                    broker.getHost(),
                    broker.getPort(),
                    timeout,
                    bufferSize,
                    String.valueOf(new Date().getTime()));
        } catch (ZookeeperException e) {
            throw new RuntimeException("CREATE BROKER CONNECTION FAIL!");
        } catch (JsonParseException e) {
            throw new RuntimeException("KAFKA BROKER META ERROR!");
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

    public List<KafkaConsumerGroup> getConsumerGroup() throws ZookeeperException {
        final List<String> groups = zookeeperClient.getChildren(KafkaConstants.CONSUMER_GROUP);
        final ArrayList<KafkaConsumerGroup> kafkaConsumerGroups = new ArrayList<>();
        for (String group : groups) {
            //get group owners
            final List<String> topics = zookeeperClient
                    .getChildren(KafkaConstants.CONSUMER_GROUP + "/" + group + "/offsets");
            final KafkaConsumerGroup kafkaConsumerGroup = new KafkaConsumerGroup();
            final ArrayList<KafkaTopicOffset> kafkaTopicOffsets = new ArrayList<>();
            for (String topic : topics) {
                final KafkaTopicOffset kafkaTopicOffset = new KafkaTopicOffset();
                final ArrayList<KafkaPartitionOffset> kafkaPartitionOffsets = new ArrayList<>();
                final List<String> partitions = zookeeperClient.getChildren(
                        KafkaConstants.CONSUMER_GROUP + "/" + group + "/offsets/" + topic);
                for (String partition : partitions) {
                    final KafkaPartitionOffset kafkaPartitionOffset = new KafkaPartitionOffset();
                    final String offset = zookeeperClient.getData(
                            KafkaConstants.CONSUMER_GROUP + "/" + group + "/offsets/" + topic + "/"
                            + partition);
                    kafkaPartitionOffset.setId(Integer.parseInt(partition));
                    kafkaPartitionOffset.setLatest(Integer.parseInt(offset));
                    kafkaPartitionOffsets.add(kafkaPartitionOffset);
                }
                kafkaTopicOffset.setName(topic);
                kafkaTopicOffset.setKafkaPartitionOffsets(kafkaPartitionOffsets);
                kafkaTopicOffsets.add(kafkaTopicOffset);
            }
            kafkaConsumerGroup.setName(group);
            kafkaConsumerGroup.setOwners(kafkaTopicOffsets);
            kafkaConsumerGroups.add(kafkaConsumerGroup);
        }
        return kafkaConsumerGroups;
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

    public HashMap<Integer, KafkaBroker> getTopicLeaderInfo(String topic) {
        final KafkaTopic topicDetail = getTopicDetail(Collections.singletonList(topic)).get(0);
        final List<KafkaPartition> partitions = topicDetail.getPartitions();
        final HashMap<Integer, KafkaBroker> leaders = new HashMap<>();
        for (KafkaPartition kafkaPartition : partitions) {
            leaders.put(kafkaPartition.getId(), kafkaPartition.getLeader());
        }
        return leaders;
    }

    public KafkaBroker getLeaderInfoByTopicAndPartition(String topic, int partition) {
        final KafkaTopic topicDetail = getTopicDetail(Collections.singletonList(topic)).get(0);
        for (KafkaPartition kafkaPartition : topicDetail.getPartitions()) {
            if (kafkaPartition.getId() == partition) {
                return kafkaPartition.getLeader();
            }
        }
        return new KafkaBroker();
    }

    public List<KafkaTopicOffset> getEarliestOffset(List<String> topic) {
        final List<KafkaTopic> topicDetail = getTopicDetail(topic);
        final ArrayList<KafkaTopicOffset> kafkaTopicOffsets = new ArrayList<>();
        for (KafkaTopic kafkaTopic : topicDetail) {
            final KafkaTopicOffset kafkaTopicOffset = new KafkaTopicOffset();
            final ArrayList<KafkaPartitionOffset> kafkaPartitionOffsets = new ArrayList<>();
            for (KafkaPartition kafkaPartition : kafkaTopic.getPartitions()) {
                final KafkaPartitionOffset kafkaPartitionOffset = new KafkaPartitionOffset();
                kafkaPartitionOffset.setEarliest(
                        getEarliestOffset(kafkaTopic.getName(), kafkaPartition.getId())
                );
                kafkaPartitionOffset.setLatest(
                        getLatestOffset(kafkaTopic.getName(), kafkaPartition.getId())
                );
                kafkaPartitionOffsets.add(kafkaPartitionOffset);
            }
            kafkaTopicOffset.setName(kafkaTopic.getName());
            kafkaTopicOffset.setKafkaPartitionOffsets(kafkaPartitionOffsets);
            kafkaTopicOffsets.add(kafkaTopicOffset);
        }
        return kafkaTopicOffsets;
    }

    public long getEarliestOffset(String topic, int partition) {
        return execOffsetRequest(topic, partition, kafka.api.OffsetRequest.EarliestTime());
    }

    public long getLatestOffset(String topic, int partition) {
        return execOffsetRequest(topic, partition, kafka.api.OffsetRequest.LatestTime());
    }

    private long execOffsetRequest(String topic, int partition, long time) {
        final KafkaBroker leader = getLeaderInfoByTopicAndPartition(topic, partition);
        final SimpleConsumer simpleConsumer = KafkaConsumerFactory
                .createSimpleConsumer(leader.getHost(), leader.getPort());
        final Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<>();
        requestInfo.put(
                new TopicAndPartition(topic, partition),
                new PartitionOffsetRequestInfo(time, 1)
        );
        final OffsetRequest offsetRequest = new OffsetRequest(requestInfo,
                kafka.api.OffsetRequest.CurrentVersion(), simpleConsumer.clientId());
        final OffsetResponse offsetResponse = simpleConsumer.getOffsetsBefore(offsetRequest);
        KafkaConsumerFactory.freeSimpleConsumer(simpleConsumer);
        if (offsetResponse.hasError()) {
            //TODO offset error handle
            System.err.println("GET OFFSET ERROR!");
            return 0;
        }
        return offsetResponse.offsets(topic, partition)[0];
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
