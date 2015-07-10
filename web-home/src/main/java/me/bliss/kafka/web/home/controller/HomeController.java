package me.bliss.kafka.web.home.controller;

import me.bliss.kafka.biz.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 *
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.web.home.controller, v 0.1 7/1/15
 *          Exp $
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private KafkaService kafkaService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public HashMap<String, Object> printWelcome(ModelMap model) {
        return kafkaService.getKafkaStatus();
    }

    public void setKafkaService(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }
}
