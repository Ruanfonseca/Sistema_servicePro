package com.ServicePro.ServicePro.controllers;

import com.ServicePro.ServicePro.models.Auxiliar;
import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.models.Requerimento;
import com.ServicePro.ServicePro.service.AuxiliarService;
import com.ServicePro.ServicePro.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ServicePro.ServicePro.repository.RequerimentoWIfiRepository;

import java.util.List;


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
			Funcionario func = funcionarioService.buscarPorMatricula(buscar);
			mv.addObject("funcionario",func);
			
		}else if(nome.equals("nomeauxiliar")) {
			Auxiliar aux = auxiliarService.buscarPorCPF(buscar);
			mv.addObject("auxiliares",aux);

		}else if(nome.equals("cpfrequerimento")) {
			Requerimento req =  Rr.findByCpf(buscar);
			mv.addObject("requerimentos",req);
			
		}else {
			mv.addObject("funcionario",funcionarioService.buscarPorNome(buscar));
			mv.addObject("auxiliares",auxiliarService.buscarPorNomesAuxiliares(buscar));
		}
		
		mv.addObject("mensagem", mensagem);
		
		return mv;
	}
}