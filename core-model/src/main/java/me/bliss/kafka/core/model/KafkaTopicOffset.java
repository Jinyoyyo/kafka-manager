package me.bliss.kafka.core.model;

import org.apache.commons.lang.ArrayUtils;

import java.util.List;

/**
 *  kafka topic offset model,include offset info of every partition
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.model, v 0.1 7/13/15
 *          Exp $
 */
public class KafkaTopicOffset {

    private String name;

    private List<KafkaPartitionOffset> kafkaPartitionOffsets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KafkaPartitionOffset> getKafkaPartitionOffsets() {
        return kafkaPartitionOffsets;
    }

    public void setKafkaPartitionOffsets(
            List<KafkaPartitionOffset> kafkaPartitionOffsets) {
        this.kafkaPartitionOffsets = kafkaPartitionOffsets;
    }

    @Override
    public String toString() {
        return new StringBuffer("name: ").append(name)
                .append(" partitionsOffsets: ").append(ArrayUtils.toString(kafkaPartitionOffsets))
                .toString();
    }
}
