package me.bliss.kafka.web.home.controller;

import me.bliss.kafka.core.service.ZookeeperClient;
import me.bliss.kafka.core.service.exception.ZookeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
    private ZookeeperClient zookeeperServcie;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<String> printWelcome(ModelMap model) {
        try {
            return zookeeperServcie.getChildrenByRecursive("/");
        } catch (ZookeeperException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setZookeeperServcie(ZookeeperClient zookeeperServcie) {
        this.zookeeperServcie = zookeeperServcie;
    }
}
