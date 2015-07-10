package me.bliss.kafka.core.service.utils;

import kafka.cluster.Broker;
import me.bliss.kafka.core.model.KafkaBroker;

import java.util.ArrayList;
import java.util.List;

/**
 * convert kafka object to system object
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.service.utils, v 0.1 7/9/15
 *          Exp $
 */
public class KafkaObjectConverter {

    public static KafkaBroker convertBroker(Broker broker){
        final KafkaBroker kafkaBroker = new KafkaBroker();
        kafkaBroker.setHost(broker.host());
        kafkaBroker.setPort(broker.port());
        return kafkaBroker;
    }

    public static List<KafkaBroker> batchConvertBroker(List<Broker> brokers){
        final List<KafkaBroker> kafkaBrokers = new ArrayList<>();
        for(Broker broker : brokers){
            kafkaBrokers.add(convertBroker(broker));
        }
        return kafkaBrokers;
    }
}
