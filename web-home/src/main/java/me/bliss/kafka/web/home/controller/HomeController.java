package me.bliss.kafka.web.home.controller;

import me.bliss.kafka.biz.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.web.home.controller, v 0.1 7/10/15
 *          Exp $
 */
@Controller
public class HomeController {

    @Autowired
    private KafkaService kafkaService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {
        model.put("brokers", kafkaService.getKafkaBrokers());
        model.put("topics",kafkaService.getKafkaTopicsMeta());
        model.put("groups",kafkaService.getKafkaConsumerGroupMeta());
        model.put("zookeeper",kafkaService.getZookeeperMeta());
        return "home";
    }

    public void setKafkaService(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }
}
