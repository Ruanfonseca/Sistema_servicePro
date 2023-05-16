package com.ServicePro.ServicePro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SuporteController {

    //GET
    @RequestMapping("/suporte")
    public ModelAndView abrirIndex() {
        ModelAndView mv = new ModelAndView("Suporte");
        return mv;
    }
}
