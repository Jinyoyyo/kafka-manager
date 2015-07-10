package me.bliss.kafka.core.model;

import java.util.List;

/**
 * kafka topic model
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.model, v 0.1 7/9/15
 *          Exp $
 */
public class KafkaTopic {

    private String name;

    private List<KafkaPartition> partitions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KafkaPartition> getPartitions() {
        return partitions;
    }

    public void setPartitions(List<KafkaPartition> partitions) {
        this.partitions = partitions;
    }
}
