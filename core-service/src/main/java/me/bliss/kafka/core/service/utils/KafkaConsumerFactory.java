package me.bliss.kafka.core.service.utils;

import kafka.javaapi.consumer.SimpleConsumer;

import java.util.Date;

/**
 *
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.service.utils, v 0.1 7/13/15
 *          Exp $
 */
public class KafkaConsumerFactory {

    private static int timeout = 1000;

    private static int bufferSize = 1024;

    private static String clientName = String.valueOf(new Date().getTime());

    public static SimpleConsumer createSimpleConsumer(String host, int port) {
        return new SimpleConsumer(host, port, timeout, bufferSize, clientName);
    }

    public static void freeSimpleConsumer(SimpleConsumer simpleConsumer){
        if(simpleConsumer != null){
            simpleConsumer.close();
        }
    }
}
