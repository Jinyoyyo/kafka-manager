package me.bliss.kafka.web.home.controller;

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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "home";
    }
}
