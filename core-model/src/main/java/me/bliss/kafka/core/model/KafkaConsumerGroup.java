package me.bliss.kafka.core.model;

import java.util.List;

/**
 * kafka consumer group model
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.model, v 0.1 7/13/15
 *          Exp $
 */
public class KafkaConsumerGroup {

    private String name;

    private List<KafkaTopicOffset> owners;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KafkaTopicOffset> getOwners() {
        return owners;
    }

    public void setOwners(List<KafkaTopicOffset> owners) {
        this.owners = owners;
    }
}
