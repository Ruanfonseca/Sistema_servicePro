package com.ServicePro.ServicePro.controllers;

import javax.validation.Valid;

import com.ServicePro.ServicePro.models.Auxiliar;
import com.ServicePro.ServicePro.models.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ServicePro.ServicePro.repository.AuxiliarRepository;
import com.ServicePro.ServicePro.repository.FuncionarioRepository;

@Controller
public class FuncionarioController {

	@Autowired
	private FuncionarioRepository fr;

	@Autowired
	private AuxiliarRepository dr;

	// GET que chama o form para cadastrar funcionários
	@RequestMapping("/cadastrarFuncionario")
	public String form() {
		return "template/funcionario/form-funcionario.html";
	}

	// POST que cadastra funcionários
	@RequestMapping(value = "/cadastrarFuncionario", method = RequestMethod.POST)
	public String form(@Valid Funcionario funcionario, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/cadastrarFuncionario";
		}

		fr.save(funcionario);
		attributes.addFlashAttribute("mensagem", "Funcionário cadastrado com sucesso!");
		return "redirect:/cadastrarFuncionario";
	}

	// GET que lista funcionários
	@RequestMapping("/funcionarios")
	public ModelAndView listaFuncionarios() {
		ModelAndView mv = new ModelAndView("template/funcionario/lista-funcionario");
		Iterable<Funcionario> funcionarios = fr.findAll();
		mv.addObject("funcionarios", funcionarios);
		return mv;
	}


	// GET que lista auxiliares e detalhes dos funcionário
	@RequestMapping("/detalhes-funcionario/{id}")
	public ModelAndView detalhesFuncionario(@PathVariable("id") long id) {
		Funcionario funcionario = fr.findById(id);
		ModelAndView mv = new ModelAndView("template/funcionario/detalhes-funcionario");
		mv.addObject("funcionarios", funcionario);

		// lista de dependentes baseada no id do funcionário
		Iterable<Auxiliar> auxiliares = dr.findByFuncionario(funcionario);
		mv.addObject("auxiliares", auxiliares);

		return mv;

	}

	// POST que adiciona dependentes
	@RequestMapping(value="/detalhes-funcionario/{id}", method = RequestMethod.POST)
	public String detalhesFuncionarioPost(@PathVariable("id") long id, Auxiliar auxiliares, BindingResult result,
			RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/detalhes-funcionario/{id}";
		}
		
		if(dr.findByCpf(auxiliares.getCpf()) != null) {
			attributes.addFlashAttribute("mensagem_erro", "CPF duplicado");
			return "redirect:/detalhes-funcionario/{id}";
		}
		
		Funcionario funcionario = fr.findById(id);
		auxiliares.setFuncionario(funcionario);
		dr.save(auxiliares);
		attributes.addFlashAttribute("mensagem", "Dependente adicionado com sucesso");
		return "redirect:/detalhes-funcionario/{id}";
		
	}
	
	//GET que deleta funcionário
	@RequestMapping("/deletarFuncionario")
	public String deletarFuncionario(long id) {
		Funcionario funcionario = fr.findById(id);
		fr.delete(funcionario);
		return "redirect:/funcionarios";
		
	}
	
	// Métodos que atualizam funcionário
	// GET que chama o FORM de edição do funcionário
	@RequestMapping("/editar-funcionario")
	public ModelAndView editarFuncionario(long id) {
		Funcionario funcionario = fr.findById(id);
		ModelAndView mv = new ModelAndView("template/funcionario/update-funcionario");
		mv.addObject("funcionario", funcionario);
		return mv;
	}
	
	// POST que atualiza o funcionário
	@RequestMapping(value = "/editar-funcionario", method = RequestMethod.POST)
	public String updateFuncionario(@Valid Funcionario funcionario,  BindingResult result, RedirectAttributes attributes){
		
		fr.save(funcionario);
		attributes.addFlashAttribute("successs", "Funcionário alterado com sucesso!");
		
		long idLong = funcionario.getId();
		String id = "" + idLong;
		return "redirect:/detalhes-funcionario/" + id;
		
	}
	
	// GET que deleta dependente
	@RequestMapping("/deletarAuxiliar")
	public String deletarAuxiliar(String cpf) {
		Auxiliar auxiliar = dr.findByCpf(cpf);
		
		Funcionario funcionario = auxiliar.getFuncionario();
		String codigo = "" + funcionario.getId();
		
		dr.delete(auxiliar);
		return "redirect:/detalhes-funcionario/" + codigo;
	
	}
}