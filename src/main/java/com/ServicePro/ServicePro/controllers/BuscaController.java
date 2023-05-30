package com.ServicePro.ServicePro.controllers;

import com.ServicePro.ServicePro.service.AuxiliarService;
import com.ServicePro.ServicePro.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ServicePro.ServicePro.repository.RequerimentoWIfiRepository;
import com.ServicePro.ServicePro.repository.AuxiliarRepository;
import com.ServicePro.ServicePro.repository.FuncionarioRepository;


@Controller
public class BuscaController {


	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private AuxiliarService auxiliarService;
	@Autowired
	private RequerimentoWIfiRepository Rr;


	

	//GET
	@RequestMapping("/")
	public ModelAndView abrirIndex() {
		ModelAndView mv = new ModelAndView("index");
		return mv;
	}
	

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView buscarIndex(@RequestParam("buscar") String buscar, @RequestParam("nome") String nome) {
		
		ModelAndView mv = new ModelAndView("index");
		String mensagem = " " + buscar;
		
		if(nome.equals("nomefuncionario")) {
			mv.addObject("funcionarios", funcionarioService.buscarPorNome(buscar));
			
		}else if(nome.equals("nomeauxiliar")) {
			mv.addObject("auxiliares", auxiliarService.buscarPorNomesAuxiliares(buscar));

		}else if(nome.equals("cpfrequerimento")) {
			mv.addObject("requerimentos", Rr.findByCpf(buscar));
			
		}else {
			mv.addObject("funcionario",funcionarioService.buscarPorNome(buscar));
			mv.addObject("auxiliares",auxiliarService.buscarPorNomesAuxiliares(buscar));
		}
		
		mv.addObject("mensagem", mensagem);
		
		return mv;
	}
}