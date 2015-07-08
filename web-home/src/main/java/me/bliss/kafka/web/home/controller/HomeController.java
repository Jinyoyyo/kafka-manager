package me.bliss.kafka.web.home.controller;

import me.bliss.kafka.core.service.ZookeeperServcie;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    private ZookeeperServcie zookeeperServcie;

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        try {
            model.addAttribute("message", zookeeperServcie.getChildrenByRecursive("/"));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    }

    public void setZookeeperServcie(ZookeeperServcie zookeeperServcie) {
        this.zookeeperServcie = zookeeperServcie;
    }
}
