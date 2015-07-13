package me.bliss.kafka.web.home.controller;

import me.bliss.kafka.biz.service.KafkaService;
import me.bliss.kafka.core.model.KafkaBroker;
import me.bliss.kafka.core.model.KafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 *
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.web.home.controller, v 0.1 7/1/15
 *          Exp $
 */
@Controller
@RequestMapping(value = "/kafka")
public class KafkaController {

    @Autowired
    private KafkaService kafkaService;

    @RequestMapping(value = "/brokers",method = RequestMethod.GET)
    @ResponseBody
    public List<KafkaBroker> getBrokers(){
        return kafkaService.getKafkaBrokers();
    }

    @RequestMapping(value = "/topics",method = RequestMethod.GET)
    @ResponseBody
    public List<KafkaTopic> getTopics(){
        return kafkaService.getKafkaTopics();
    }

    @RequestMapping(value ="/status",method = RequestMethod.GET)
    @ResponseBody
    public HashMap<String, Object> getKakfaDetail() {
        return kafkaService.getKafkaStatus();
    }

    public void setKafkaService(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }
}
