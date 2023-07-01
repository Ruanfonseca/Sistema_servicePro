package com.ServicePro.ServicePro.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RelatorioController {

    //GET
    @RequestMapping("/relatorio")
    public ModelAndView PaginaRelatorio() {
        ModelAndView mv = new ModelAndView("template/relatorio/Relatorio.html");
        return mv;
    }
}
