package com.ServicePro.ServicePro.controllers;

import com.ServicePro.ServicePro.models.*;
import com.ServicePro.ServicePro.repository.*;
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

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ReqProjetorController {

    @Autowired
    private FuncionarioRepository func;

    @Autowired
    private OrdemDeServicoProjetorRepository OS;

    @Autowired
    private RequerimentoProjetorRepository projetorRepository;



    @GetMapping("/cadastrarReqProj")
    public ModelAndView form() {
        ModelAndView modelAndView = new ModelAndView("template/projetor/form-projetor.html");
        modelAndView.addObject("requerimentoProjetor", new RequerimentoSala());
        return modelAndView;
    }

    @PostMapping("/cadastrarReqProj")
    public String cadastrarReq(@Valid @ModelAttribute("requerimentoProjetor") RequerimentoProjetor req, BindingResult result,
                               RedirectAttributes attributes) {
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors) {
                attributes.addFlashAttribute("mensagem",
                        "Erro no campo " + ((FieldError) error).getField() + ": " + error.getDefaultMessage());
            }

            return "redirect:/cadastrarReqProj";
        }
        if (!ValidacaoUtil.validarCPF(req.getCpf())) {
            attributes.addFlashAttribute("mensagem", "CPF invalido!");
            return "redirect:/cadastrarReqProj";

        }if(projetorRepository.findByCpf(req.getCpf()) !=null){
            if(req.getStatus() == "PENDENTE")
                attributes.addFlashAttribute("mensagem", "Já Existe um requerimento pendente associado a esse cpf !");
            return "redirect:/cadastrarReqProj";

        }if(func.findByCpf(req.getCpf())!=null){
            attributes.addFlashAttribute("mensagem", "Você é um funcionário, funcionário não pode abrir requerimento");
            return "redirect:/cadastrarReqProj";
        }
        try {
            if (projetorRepository.save(req) != null) {

                attributes.addFlashAttribute("mensagem", "Requerimento cadastrado com sucesso!");
            } else {
                attributes.addFlashAttribute("mensagem",
                        "Não foi possível cadastrar o requerimento. Por favor, verifique se todos os campos foram preenchidos corretamente e tente novamente.");
            }
        } catch (DataIntegrityViolationException ex) {
            attributes.addFlashAttribute("mensagem", "Você esta tentando inserir um dado que ja esta cadastrado!");
            ex.printStackTrace();
        }

        return "redirect:/cadastrarReqProj";
    }




    //recebendo parâmetros como inteiros , na pagina 0 exponha 10 requerimentos finalizados
    @GetMapping("/requerimentosProjFinalizados")
    public ModelAndView PagelistaDeFinalizados(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        ModelAndView mv = new ModelAndView("template/projetor/lista-req-projetor-finalizado");

        Pageable pageable = PageRequest.of(page, size);

        Page<RequerimentoProjetor> requerimentosPage = projetorRepository.findByStatusFinalizado(pageable);

        mv.addObject("requerimento", requerimentosPage.getContent());
        mv.addObject("currentPage", requerimentosPage.getNumber());
        mv.addObject("totalPages", requerimentosPage.getTotalPages());
        return mv;
    }


    @GetMapping("/requerimentosProj")
    public ModelAndView PagelistaPendente(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        ModelAndView mv = new ModelAndView("template/projetor/lista-projetor");

        Pageable pageable = PageRequest.of(page, size);

        Page<RequerimentoProjetor> requerimentosPage = projetorRepository.findByStatusPendente(pageable);

        mv.addObject("requerimento", requerimentosPage.getContent());
        mv.addObject("currentPage", requerimentosPage.getNumber());
        mv.addObject("totalPages", requerimentosPage.getTotalPages());
        return mv;
    }

    @GetMapping("/requerimentoProj/{codigo}")
    public ModelAndView detalhesReq(@PathVariable("codigo") long codigo) {
        RequerimentoProjetor requerimento = projetorRepository.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("template/projetor/detalhes-projetor.html");
        mv.addObject("requerimento", requerimento);
        return mv;
    }

    @GetMapping("/deletarReqProj/{codigo}")
    public String deletarReq(@PathVariable("codigo") long codigo) {
        RequerimentoProjetor req = projetorRepository.findByCodigo(codigo);
        projetorRepository.delete(req);
        return "redirect:/requerimentosProj";
    }

    @GetMapping("/editar-requerimentoProj/{codigo}")
    public ModelAndView editarRequerimento(@PathVariable("codigo") long codigo) {
        RequerimentoProjetor requerimento = projetorRepository.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("template/projetor/update-projetor");
        mv.addObject("requerimento", requerimento);
        return mv;
    }

    @PostMapping("/editar-requerimentoProj/{codigo}")
    public String updateReq(@PathVariable("codigo") long codigo,
                            @Valid @ModelAttribute("requerimentoProjetor") RequerimentoProjetor requerimento, BindingResult result,
                            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors) {
                attributes.addFlashAttribute("mensagem",
                        "Erro no campo " + ((FieldError) error).getField() + ": " + error.getDefaultMessage());
            }

            return "redirect:/editar-requerimentoProj/" + codigo;
        }
        if(requerimento.getStatus().equals("FINALIZADO")){
            attributes.addFlashAttribute("mensagem", "O requerimento já foi finalizado");
            return "redirect:/requerimentosProj/" + codigo;
        }
        projetorRepository.save(requerimento);

        attributes.addFlashAttribute("mensagem", "Requerimento alterado com sucesso!");

        return "redirect:/requerimentoProj/" + codigo;
    }

    @GetMapping("/TelaBaixaReqProj/{codigo}")
    public ModelAndView baixaRequerimento(@PathVariable("codigo") long codigo) {

        Iterable<Funcionario> funcionarios = func.findAllsetor("logistica");
        ModelAndView mv = new ModelAndView("template/projetor/TelaBaixaProjetor");
        mv.addObject("funcionario", funcionarios);

        RequerimentoProjetor requerimento = projetorRepository.findByCodigo(codigo);
        mv.addObject("requerimento", requerimento);

        return mv;
    }



    @PostMapping("/TelaBaixaReqProj/{codigo}")
    public String baixaRequerimento(@PathVariable("codigo") long codigo, @RequestParam("matri") String matricula,
                                    @Valid @ModelAttribute("requerimento") RequerimentoProjetor requerimento, @NotNull BindingResult result,
                                    RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/TelaBaixaReqProj/" + codigo;
        }
        Funcionario aux = func.findByMatricula(matricula);

        if (!aux.getTipo().equals("LOGISTICA")) {
            attributes.addFlashAttribute("mensagem", "Você não é um funcionário da Logistica!");
            return "redirect:/TelaBaixaReqProj/" + codigo;
        }

        projetorRepository.save(requerimento);
        LocalDateTime data = LocalDateTime.now();
        OrdemDeServicoProjetor ordemDeServicoProjetor = new OrdemDeServicoProjetor(data, aux,requerimento);
        OS.save(ordemDeServicoProjetor);

        attributes.addFlashAttribute("mensagem", "Requerimento finalizado com sucesso!");
        return "redirect:/requerimentosProj/" + codigo;
    }

}
