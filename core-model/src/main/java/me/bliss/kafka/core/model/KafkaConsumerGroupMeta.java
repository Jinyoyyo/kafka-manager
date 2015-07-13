package me.bliss.kafka.core.model;

import java.util.List;

/**
 *  kafka consumer group meta data
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.model, v 0.1 7/13/15
 *          Exp $
 */
public class KafkaConsumerGroupMeta {

    private String name;

    private List<String> owners;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOwners() {
        return owners;
    }

    public void setOwners(List<String> owners) {
        this.owners = owners;
    }
}
