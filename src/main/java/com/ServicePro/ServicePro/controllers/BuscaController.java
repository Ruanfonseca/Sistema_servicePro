package com.ServicePro.ServicePro.controllers;

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
	private FuncionarioRepository fr;
	
	@Autowired
	private RequerimentoWIfiRepository Rr;
	
	
	@Autowired
	private AuxiliarRepository dr;
	

	//GET
	@RequestMapping("/")
	public ModelAndView abrirIndex() {
		ModelAndView mv = new ModelAndView("index");
		return mv;
	}
	

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView buscarIndex(@RequestParam("buscar") String buscar, @RequestParam("nome") String nome) {
		
		ModelAndView mv = new ModelAndView("index");
		String mensagem = "Resultados da busca por " + buscar;
		
		if(nome.equals("nomefuncionario")) {
			mv.addObject("funcionarios", fr.findByNomes(buscar));
			
		}else if(nome.equals("nomeauxiliar")) {
			mv.addObject("auxiliares", dr.findByNomesAuxiliares(buscar));
			
				
		}else if(nome.equals("cpfrequerimento")) {
			mv.addObject("requerimentos", Rr.findByCpf(buscar));
			
		}else {
			mv.addObject("funcionarios", fr.findByNomes(buscar));
			mv.addObject("auxiliares", dr.findByNomesAuxiliares(buscar));
		
			//mv.addObject("requerimento", Reqrepo.findByNomesRequerimento(buscar));
		}
		
		mv.addObject("mensagem", mensagem);
		
		return mv;
	}
}