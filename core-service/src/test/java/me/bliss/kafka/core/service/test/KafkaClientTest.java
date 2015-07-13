package me.bliss.kafka.core.service.test;

import me.bliss.kafka.core.model.KafkaTopicOffset;
import me.bliss.kafka.core.service.KafkaClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

/**
 * kafka client test class
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.service.test, v 0.1 7/8/15
 *          Exp $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/*.xml")
public class KafkaClientTest extends Assert{

    @Autowired
    private KafkaClient kafkaClient;

    @Test
    public void testGetBrokers() throws Exception {
        assertNotNull(kafkaClient);
        kafkaClient.getBrokers();
    }

    @Test
    public void testGetLeader() throws Exception {
        assertNotNull(kafkaClient);
        kafkaClient.getTopics();
    }

    @Test
    public void testGetEarliestOffset() throws Exception {
        assertNotNull(kafkaClient);
        final List<KafkaTopicOffset> offsets = kafkaClient.getEarliestOffset(
                Collections.singletonList("build"));
        System.out.println(offsets);
    }

    public void setKafkaClient(KafkaClient kafkaClient) {
        this.kafkaClient = kafkaClient;
    }
}
