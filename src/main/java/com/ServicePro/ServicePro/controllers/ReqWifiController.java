package com.ServicePro.ServicePro.controllers;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.models.OrdemDeServico;
import com.ServicePro.ServicePro.models.OrdemDeServicoSala;
import com.ServicePro.ServicePro.models.Requerimento;
import com.ServicePro.ServicePro.repository.OrdemDeServicoRepository;
import com.ServicePro.ServicePro.repository.RequerimentoWIfiRepository;
import com.ServicePro.ServicePro.utils.ValidacaoUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import com.ServicePro.ServicePro.repository.FuncionarioRepository;


@Controller
public class ReqWifiController {

	@Autowired
	private RequerimentoWIfiRepository vr;

	@Autowired
	private FuncionarioRepository func;

	@Autowired
	private OrdemDeServicoRepository OS;


	@GetMapping("/cadastrarReq")
	public ModelAndView form() {
		ModelAndView modelAndView = new ModelAndView("template/reqWIFI/form-vaga.html");
		modelAndView.addObject("requerimento", new Requerimento());
		return modelAndView;
	}

	@PostMapping("/cadastrarReq")
	public String cadastrarReq(@Valid @ModelAttribute("requerimento") Requerimento req, BindingResult result,
							   RedirectAttributes attributes) {

		if (result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
			for (ObjectError error : errors) {
				attributes.addFlashAttribute("mensagem",
						"Erro no campo " + ((FieldError) error).getField() + ": " + error.getDefaultMessage());
			}

			return "redirect:/cadastrarReq";
		}
		if (!ValidacaoUtil.validarCPF(req.getCpf())) {
			attributes.addFlashAttribute("mensagem", "CPF invalido!");
			return "redirect:/cadastrarReq";

		}if(vr.findByCpf(req.getCpf()) !=null){
			if(req.getStatus() == "Pendente")
			attributes.addFlashAttribute("mensagem", "Já Existe um requerimento pendente associado a esse cpf !");
			return "redirect:/cadastrarReq";

		}if(func.findByCpf(req.getCpf())!=null){
			attributes.addFlashAttribute("mensagem", "Você é um funcionário, funcionário não pode abrir requerimento");
			return "redirect:/cadastrarReq";
		}
		try {
			if (vr.save(req) != null) {

				attributes.addFlashAttribute("mensagem", "Requerimento cadastrado com sucesso!");
			} else {
				attributes.addFlashAttribute("mensagem",
						"Não foi possível cadastrar o requerimento. Por favor, verifique se todos os campos foram preenchidos corretamente e tente novamente.");
			}
		} catch (DataIntegrityViolationException ex) {
			attributes.addFlashAttribute("mensagem", "Você esta tentando inserir um dado que ja esta cadastrado!");
			ex.printStackTrace();
		}

		return "redirect:/cadastrarReq";
	}

	//recebendo parâmetros como inteiros , na pagina 0 exponha 10 requerimentos finalizados
	@GetMapping("/requerimentosPageFinalizados")
	public ModelAndView PagelistaDeFinalizados(@RequestParam(defaultValue = "0") int page,
										   @RequestParam(defaultValue = "10") int size) {
		ModelAndView mv = new ModelAndView("template/reqWIFI/lista-req-finalizado");

		Pageable pageable = PageRequest.of(page, size);

		Page<Requerimento> requerimentosPage = vr.findByStatusFinalizado(pageable);

		mv.addObject("requerimentos", requerimentosPage.getContent());
		mv.addObject("currentPage", requerimentosPage.getNumber());
		mv.addObject("totalPages", requerimentosPage.getTotalPages());
		return mv;
	}

	@GetMapping("/requerimentos")
	public ModelAndView PagelistaPendente(@RequestParam(defaultValue = "0") int page,
							  @RequestParam(defaultValue = "10") int size) {
		ModelAndView mv = new ModelAndView("template/reqWIFI/lista-req");

		Pageable pageable = PageRequest.of(page, size);

		Page<Requerimento> requerimentosPage = vr.findByStatusPendente(pageable);

		mv.addObject("requerimentos", requerimentosPage.getContent());
		mv.addObject("currentPage", requerimentosPage.getNumber());
		mv.addObject("totalPages", requerimentosPage.getTotalPages());
		return mv;
	}

	@GetMapping("/requerimento/{codigo}")
	public ModelAndView detalhesReq(@PathVariable("codigo") long codigo) {
		Requerimento requerimento = vr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("template/reqWIFI/detalhes-vaga.html");
		mv.addObject("requerimento", requerimento);
		return mv;
	}

	@GetMapping("/deletarReq/{codigo}")
	public String deletarReq(@PathVariable("codigo") long codigo) {
		Requerimento req = vr.findByCodigo(codigo);
		vr.delete(req);
		return "redirect:/requerimentos";
	}

	@GetMapping("/editar-requerimento/{codigo}")
	public ModelAndView editarRequerimento(@PathVariable("codigo") long codigo) {
		Requerimento requerimento = vr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("template/reqWIFI/update-vaga");
		mv.addObject("requerimento", requerimento);
		return mv;
	}

	@PostMapping("/editar-requerimento/{codigo}")
	public String updateReq(@PathVariable("codigo") long codigo,
							@Valid @ModelAttribute("requerimento") Requerimento requerimento, BindingResult result,
							RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos...");
			return "redirect:/editar-requerimento/" + codigo;
		}
		if(requerimento.getStatus().equals("FINALIZADO")){
			attributes.addFlashAttribute("mensagem", "O requerimento já foi finalizado");
			return "redirect:/requerimento/" + codigo;
		}
		vr.save(requerimento);
		attributes.addFlashAttribute("mensagem", "Requerimento alterado com sucesso!");
		return "redirect:/requerimento/" + codigo;
	}

/*
	@GetMapping("/TelaBaixaReq/{codigo}")
	public ModelAndView baixaRequerimento(@PathVariable("codigo") long codigo) {

		Requerimento requerimento = vr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("template/vaga/TelaBaixaReq");
		mv.addObject("requerimento", requerimento);
		return mv;
	}
*/
@GetMapping("/TelaBaixaReq/{codigo}")
public ModelAndView baixaRequerimento(@PathVariable("codigo") long codigo) {

	//Iterable<Funcionario> funcionarios = func.findAllsetor("Coinfo");
	ModelAndView mv = new ModelAndView("template/reqWIFI/TelaBaixaReq");

	//mv.addObject("funcionario", funcionarios);

	Requerimento requerimento = vr.findByCodigo(codigo);
	mv.addObject("requerimento", requerimento);

	return mv;
}



	@PostMapping("/TelaBaixaReq/{codigo}")
	public String baixaRequerimento(@PathVariable("codigo") long codigo, @RequestParam("matri") String matricula,
									@Valid @ModelAttribute("requerimento") Requerimento requerimento, @NotNull BindingResult result,
									RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos...");
			return "redirect:/TelaBaixaReq/" + codigo;
		}
		Funcionario aux = func.findByMatricula(matricula);
		if (!aux.getTipo().equals("COINFO")) {
			attributes.addFlashAttribute("mensagem", "Você não é um funcionário da coinfo!");
			return "redirect:/TelaBaixaReq/" + codigo;
		}

		vr.save(requerimento);
		LocalDateTime data = LocalDateTime.now();
		OrdemDeServico ordemDeServico = new OrdemDeServico(data, aux,requerimento);
		OS.save(ordemDeServico);

		attributes.addFlashAttribute("mensagem", "Requerimento finalizado com sucesso!");
		return "redirect:/requerimento/" + codigo;
	}






}
