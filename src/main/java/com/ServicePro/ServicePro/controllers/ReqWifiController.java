package com.ServicePro.ServicePro.controllers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.models.OrdemDeServico;
import com.ServicePro.ServicePro.models.Requerimento;
import com.ServicePro.ServicePro.service.FuncionarioService;
import com.ServicePro.ServicePro.service.OrdemDeServicoService;
import com.ServicePro.ServicePro.service.ReqWifiService;
import com.ServicePro.ServicePro.utils.ValidacaoUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
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

import static com.ServicePro.ServicePro.utils.FormatadorUtil.FormatadorDeData;


@Controller
public class ReqWifiController {

	@Autowired
	private ReqWifiService ReqWifiService;
	 @Autowired
	 private FuncionarioService funcionarioService;

	@Autowired
	private OrdemDeServicoService OSservice;



	@GetMapping("/cadastrarReq")
	public ModelAndView form() {
		ModelAndView modelAndView = new ModelAndView("template/reqWIFI/form-req.html");
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

		}if(ReqWifiService.buscarPorCPF(req.getCpf()) !=null){
			if(req.getStatus() == "PENDENTE")
			attributes.addFlashAttribute("mensagem", "Já Existe um requerimento pendente associado a esse cpf !");
			return "redirect:/cadastrarReq";

		}if(funcionarioService.buscarPorCPF(req.getCpf())!=null){
			attributes.addFlashAttribute("mensagem", "Você é um funcionário, funcionário não pode abrir requerimento");
			return "redirect:/cadastrarReq";
		}
		try {
			req.setData(FormatadorDeData(req.getData()));
			if (ReqWifiService.salvar(req)) {

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

		Page<Requerimento> requerimentosPage = ReqWifiService.buscarPorStatusFinalizado(pageable);

		mv.addObject("requerimentos", requerimentosPage.getContent());
		mv.addObject("currentPage", requerimentosPage.getNumber());
		mv.addObject("totalPages", requerimentosPage.getTotalPages());
		return mv;
	}

	@RequestMapping("/requerimentos/GerarPDF")
	public void geradorDePDFfinalizados(HttpServletResponse response) {
		try {

			List<Requerimento> requerimentosFinalizados = ReqWifiService.buscarPorStatusFinalizado();

			Document document = new Document();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=relatorio_finalizados.pdf");

			PdfWriter writer = PdfWriter.getInstance(document, baos);


			document.open();

			//Colocando imagem no pdf , estava dando muitos erros nessa chamada de imagem , resolvi colocar
			//no github , embora cause lentidão
			URL imageUrl = new URL("https://github.com/Ruanfonseca/Sistema_servicePro/raw/729e563093d78de37e536aeb88de391a4a08ad42/src/main/resources/static/images/logoUERJ.png");
			Image image = Image.getInstance(imageUrl);
			image.scaleToFit(100, 100);
			image.setAlignment(Element.ALIGN_CENTER);
			document.add(image);


			// Colocando titulo no documento
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
			Paragraph title = new Paragraph("Relatório de Requerimentos Finalizados", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			title.setSpacingAfter(20);
			document.add(title);

			Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
			for (Requerimento requerimento : requerimentosFinalizados) {
				Paragraph paragraph = new Paragraph();
				paragraph.setFont(contentFont);
				paragraph.add("ID: " + requerimento.getCodigo() +
						"\nNome Requerente: " + requerimento.getNomeRequerente() +
						"\nData: " + requerimento.getData() +
						"\nStatus: " + requerimento.getStatus() +
						"\nEmail: "+requerimento.getEmail() +
						"\nMatricula: "+requerimento.getMatricula()+
						"\nMensagem de retorno: "+requerimento.getMensagemRetorno()+
						"\nDescrição: "+requerimento.getDescricao() +
						"\n "

				);
				document.add(paragraph);
			}


			document.close();

			response.setHeader("Content-Disposition", "inline; filename=relatorio_finalizados.pdf");
			response.setContentType("application/pdf");
			response.setContentLength(baos.toByteArray().length);

			// Escreva os dados do PDF na resposta
			response.getOutputStream().write(baos.toByteArray());
			response.getOutputStream().flush();

		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/requerimentos")
	public ModelAndView PagelistaPendente(@RequestParam(defaultValue = "0") int page,
							  @RequestParam(defaultValue = "10") int size) {
		ModelAndView mv = new ModelAndView("template/reqWIFI/lista-req");

		Pageable pageable = PageRequest.of(page, size);

		Page<Requerimento> requerimentosPage = ReqWifiService.buscarPorStatusPendente(pageable);

		mv.addObject("requerimentos", requerimentosPage.getContent());
		mv.addObject("currentPage", requerimentosPage.getNumber());
		mv.addObject("totalPages", requerimentosPage.getTotalPages());
		return mv;
	}

	@GetMapping("/requerimento/{codigo}")
	public ModelAndView detalhesReq(@PathVariable("codigo") long codigo) {
		Requerimento requerimento = ReqWifiService.buscarPorCodigo(codigo);
		ModelAndView mv = new ModelAndView("template/reqWIFI/detalhes-req.html");
		mv.addObject("requerimento", requerimento);
		return mv;
	}

	@GetMapping("/deletarReq/{codigo}")
	public String deletarReq(@PathVariable("codigo") long codigo) {
		Requerimento req = ReqWifiService.buscarPorCodigo(codigo);
		ReqWifiService.deletar(req);
		return "redirect:/requerimentos";
	}

	@GetMapping("/editar-requerimento/{codigo}")
	public ModelAndView editarRequerimento(@PathVariable("codigo") long codigo) {
		Requerimento requerimento = ReqWifiService.buscarPorCodigo(codigo);
		ModelAndView mv = new ModelAndView("template/reqWIFI/update-req");
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
		ReqWifiService.salva(requerimento);
		attributes.addFlashAttribute("mensagem", "Requerimento alterado com sucesso!");
		return "redirect:/requerimento/" + codigo;
	}

@GetMapping("/TelaBaixaReq/{codigo}")
public ModelAndView baixaRequerimento(@PathVariable("codigo") long codigo) {

	//Iterable<Funcionario> funcionarios = func.findAllsetor("Coinfo");
	ModelAndView mv = new ModelAndView("template/reqWIFI/TelaBaixaReq");

	//mv.addObject("funcionario", funcionarios);

	Requerimento requerimento = ReqWifiService.buscarPorCodigo(codigo);

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
		Funcionario aux = funcionarioService.buscarPorMatricula(matricula);
		if (!aux.getTipo().equals("COINFO")) {
			attributes.addFlashAttribute("mensagem", "Você não é um funcionário da coinfo!");
			return "redirect:/TelaBaixaReq/" + codigo;
		}

		ReqWifiService.salva(requerimento);

		LocalDateTime data = LocalDateTime.now();

		OrdemDeServico ordemDeServico = new OrdemDeServico(data,aux.getMatricula(), aux.getNome(),
				requerimento.getMatricula(),requerimento.getNomeRequerente());

		OSservice.salva(ordemDeServico);

		attributes.addFlashAttribute("mensagem", "Requerimento finalizado com sucesso!");
		return "redirect:/requerimento/" + codigo;
	}






}
