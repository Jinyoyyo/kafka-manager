package me.bliss.kafka.core.model;

import java.util.List;
import java.util.Set;

/**
 *
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.model, v 0.1 7/13/15
 *          Exp $
 */
public class KafkaTopicMeta {

    private String name;

    private List<Integer> partitionIds;

    private Set<Integer> replicaIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPartitionIds() {
        return partitionIds;
    }

    public void setPartitionIds(List<Integer> partitionIds) {
        this.partitionIds = partitionIds;
    }

    public Set<Integer> getReplicaIds() {
        return replicaIds;
    }

    public void setReplicaIds(Set<Integer> replicaIds) {
        this.replicaIds = replicaIds;
    }

    @Override
    public String toString() {
        return new StringBuffer(" name: ").append(name)
                .append(" partitionIds: ").append(partitionIds)
                .append(" replicaIds: ").append(replicaIds)
                .toString();
    }
}
