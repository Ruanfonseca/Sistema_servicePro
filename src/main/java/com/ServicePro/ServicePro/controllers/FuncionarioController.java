package com.ServicePro.ServicePro.controllers;

import javax.validation.Valid;

import com.ServicePro.ServicePro.models.Auxiliar;
import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.service.AuxiliarService;
import com.ServicePro.ServicePro.service.FuncionarioService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class FuncionarioController {
	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private AuxiliarService auxiliarService;


	// GET que chama o form para cadastrar funcionários
	@RequestMapping("/cadastrarFuncionario")
	public String form() {
		return "template/funcionario/form-funcionario.html";
	}

	// POST que cadastra funcionários
	@RequestMapping(value = "/cadastrarFuncionario", method = RequestMethod.POST)
	public String form(@Valid Funcionario funcionario, @NotNull BindingResult result, RedirectAttributes attributes) {


		if (result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
			for (ObjectError error : errors) {
				attributes.addFlashAttribute("mensagem",
						"  Erro no campo " + ((FieldError) error).getField() + ": " + error.getDefaultMessage());
			}
			return "redirect:/cadastrarFuncionario";
		}


		if(!funcionarioService.salvarFuncionario(funcionario)){
			attributes.addFlashAttribute("mensagem",
					"  Funcionário já cadastrado ! ");
			return "redirect:/cadastrarFuncionario";
		}

		attributes.addFlashAttribute("mensagem", "Funcionário cadastrado com sucesso!");
		return "redirect:/cadastrarFuncionario";
	}

	@GetMapping("/funcionarios")
	public ModelAndView PagelistaPendente(@RequestParam(defaultValue = "0") int page,
										  @RequestParam(defaultValue = "10") int size) {
		ModelAndView mv = new ModelAndView("template/funcionario/lista-funcionario");

		Pageable pageable = PageRequest.of(page, size);

		Page<Funcionario> funcionariosPage = funcionarioService.encontrarTodosPaginacao(pageable);

		mv.addObject("funcionarios", funcionariosPage.getContent());
		mv.addObject("currentPage", funcionariosPage.getNumber());
		mv.addObject("totalPages", funcionariosPage.getTotalPages());
		return mv;
	}


	// GET que lista auxiliares e detalhes dos funcionário
	@RequestMapping("/detalhes-funcionario/{id}")
	public ModelAndView detalhesFuncionario(@PathVariable("id") long id) {

		Funcionario funcionario = funcionarioService.encontrarPorId(id);

		ModelAndView mv = new ModelAndView("template/funcionario/detalhes-funcionario");

		mv.addObject("funcionarios", funcionario);

		// lista de dependentes baseada no id do funcionário
		Iterable<Auxiliar> auxiliares = auxiliarService.buscarPor(funcionario);
		mv.addObject("auxiliares", auxiliares);

		return mv;

	}

	// POST que adiciona auxiliares
	@RequestMapping(value="/detalhes-funcionario/{id}", method = RequestMethod.POST)
	public String detalhesFuncionarioPost(@PathVariable("id") long id, Auxiliar auxiliar, BindingResult result,
										  RedirectAttributes attributes) {

		if (result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
			for (ObjectError error : errors) {
				attributes.addFlashAttribute("mensagem",
						"  Erro no campo " + ((FieldError) error).getField() + ": " + error.getDefaultMessage());
			}
			return "redirect:/detalhes-funcionario/{id}";
		}


		if(auxiliarService.buscarPorCPF(auxiliar) != null) {
			attributes.addFlashAttribute("mensagem_erro", "CPF duplicado");
			return "redirect:/detalhes-funcionario/{id}";
		}

		Funcionario funcionario = funcionarioService.encontrarPorId(id);
		auxiliar.setFuncionario(funcionario);

		if(!auxiliarService.salvar(auxiliar)){
			attributes.addFlashAttribute("mensagem_erro", "Já existe auxiliar !");
			return "redirect:/detalhes-funcionario/{id}";
		}

		attributes.addFlashAttribute("mensagem", "auxiliar adicionado com sucesso");
		return "redirect:/detalhes-funcionario/{id}";

	}

	//GET que deleta funcionário
	@RequestMapping("/deletarFuncionario")
	public String deletarFuncionario(long id) {
		Funcionario funcionario = funcionarioService.encontrarPorId(id);
		funcionarioService.deletar(funcionario);
		return "redirect:/funcionarios";

	}

	// Métodos que atualizam funcionário
	// GET que chama o FORM de edição do funcionário
	@RequestMapping("/editar-funcionario")
	public ModelAndView editarFuncionario(long id) {
		Funcionario funcionario = funcionarioService.encontrarPorId(id);
		ModelAndView mv = new ModelAndView("template/funcionario/update-funcionario");
		mv.addObject("funcionario", funcionario);
		return mv;
	}

	// POST que atualiza o funcionário
	@RequestMapping(value = "/editar-funcionario", method = RequestMethod.POST)
	public String updateFuncionario(@Valid @ModelAttribute("funcionario") Funcionario funcionario, @NotNull BindingResult result, RedirectAttributes attributes){

		if (result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
			for (ObjectError error : errors) {
				attributes.addFlashAttribute("mensagem",
						"  Erro no campo " + ((FieldError) error).getField() + ": " + error.getDefaultMessage());
			}
			return "redirect:/editar-funcionario";
		}


		funcionarioService.salvarFuncionario(funcionario);

		attributes.addFlashAttribute("successs", "Funcionário alterado com sucesso!");

		long idLong = funcionario.getId();
		String id = "" + idLong;
		return "redirect:/detalhes-funcionario/" + id;

	}

	// GET que deleta auxu
	@RequestMapping("/deletarAuxiliar")
	public String deletarAuxiliar(String cpf) {
		Auxiliar auxiliar = auxiliarService.buscarPorCPF(cpf);

		Funcionario funcionario = auxiliar.getFuncionario();
		String codigo = "" + funcionario.getId();

		auxiliarService.deletar(auxiliar);
		return "redirect:/detalhes-funcionario/" + codigo;

	}
}