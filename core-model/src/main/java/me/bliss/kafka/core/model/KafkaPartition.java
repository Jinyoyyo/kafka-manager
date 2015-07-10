package me.bliss.kafka.core.model;

import java.util.List;

/**
 *
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.model, v 0.1 7/9/15
 *          Exp $
 */
public class KafkaPartition {

    private int id;

    private KafkaBroker leader;

    private List<KafkaBroker> replicas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public KafkaBroker getLeader() {
        return leader;
    }

    public void setLeader(KafkaBroker leader) {
        this.leader = leader;
    }

    public List<KafkaBroker> getReplicas() {
        return replicas;
    }

    public void setReplicas(List<KafkaBroker> replicas) {
        this.replicas = replicas;
    }
}
