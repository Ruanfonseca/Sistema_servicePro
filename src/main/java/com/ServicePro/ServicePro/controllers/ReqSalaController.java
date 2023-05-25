package com.ServicePro.ServicePro.controllers;

import com.ServicePro.ServicePro.models.*;
import com.ServicePro.ServicePro.repository.FuncionarioRepository;
import com.ServicePro.ServicePro.repository.OrdemDeServicoRepository;
import com.ServicePro.ServicePro.repository.OrdemDeServicoSalaRepository;
import com.ServicePro.ServicePro.repository.RequerimentoSalaRepository;
import com.ServicePro.ServicePro.utils.ValidacaoUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
public class ReqSalaController {

    @Autowired
    private FuncionarioRepository func;

    @Autowired
    private OrdemDeServicoSalaRepository OS;

    @Autowired
    private RequerimentoSalaRepository salaRepository;

    @GetMapping("/cadastrarReqSala")
    public ModelAndView form() {
        ModelAndView modelAndView = new ModelAndView("template/Sala/form-sala.html");
        modelAndView.addObject("requerimentoSALA", new RequerimentoSala());
        return modelAndView;
    }

    @PostMapping("/cadastrarReqSala")
    public String cadastrarReqSala(@Valid @ModelAttribute("requerimentoSALA") RequerimentoSala req, BindingResult result,
                               RedirectAttributes attributes) {

        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors) {
                attributes.addFlashAttribute("mensagem",
                        "Erro no campo " + ((FieldError) error).getField() + ": " + error.getDefaultMessage());
            }

            return "redirect:/cadastrarReqSala";
        }
        if (!ValidacaoUtil.validarCPF(req.getCpf())) {
            attributes.addFlashAttribute("mensagem", "CPF invalido!");
            return "redirect:/cadastrarReqSala";

        }if(salaRepository.findByCpf(req.getCpf()) !=null){
            if(req.getStatus() == "Pendente")
                attributes.addFlashAttribute("mensagem", "Já Existe um requerimento pendente associado a esse cpf !");
            return "redirect:/cadastrarReqSala";

        }if(func.findByCpf(req.getCpf())!=null){
            attributes.addFlashAttribute("mensagem", "Você é um funcionário, funcionário não pode abrir requerimento");
            return "redirect:/cadastrarReqSala";
        }
        try {
            if (salaRepository.save(req) != null) {

                attributes.addFlashAttribute("mensagem", "Requerimento cadastrado com sucesso!");
            } else {
                attributes.addFlashAttribute("mensagem",
                        "Não foi possível cadastrar o requerimento. Por favor, verifique se todos os campos foram preenchidos corretamente e tente novamente.");
            }
        } catch (DataIntegrityViolationException ex) {
            attributes.addFlashAttribute("mensagem", "Você esta tentando inserir um dado que ja esta cadastrado!");
            ex.printStackTrace();
        }

        return "redirect:/cadastrarReqSala";
    }


    @GetMapping("/requerimentosSALAFinalizados")
    public ModelAndView listaDeFinalizados() {
        ModelAndView mv = new ModelAndView("template/Sala/lista-req-sala-finalizado");

        Iterable<RequerimentoSala> requerimento = salaRepository.findByStatusFinalizado();

        mv.addObject("requerimentos", requerimento);
        return mv;
    }

    @GetMapping("/requerimentosSALA")
    public ModelAndView lista() {
        ModelAndView mv = new ModelAndView("template/Sala/lista-sala.html");
        Iterable<RequerimentoSala> requerimento = salaRepository.findByStatusPendente();
        mv.addObject("requerimentos", requerimento);
        return mv;
    }

    @GetMapping("/requerimentoSALA/{codigo}")
    public ModelAndView detalhesSALA(@PathVariable("codigo") long codigo) {
        RequerimentoSala requerimento = salaRepository.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("template/Sala/detalhes-sala.html");
        mv.addObject("requerimento", requerimento);
        return mv;
    }

    @GetMapping("/deletarReqSALA/{codigo}")
    public String deletarReq(@PathVariable("codigo") long codigo) {
        RequerimentoSala req = salaRepository.findByCodigo(codigo);
        salaRepository.delete(req);
        return "redirect:/requerimentosSALA";
    }

    @GetMapping("/editar-requerimentoSALA/{codigo}")
    public ModelAndView editarRequerimento(@PathVariable("codigo") long codigo) {
        RequerimentoSala requerimento = salaRepository.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("template/Sala/update-sala");
        mv.addObject("requerimento", requerimento);
        return mv;
    }

    @PostMapping("/editar-requerimentoSALA/{codigo}")
    public String updateReq(@PathVariable("codigo") long codigo,
                            @Valid @ModelAttribute("requerimento") RequerimentoSala requerimento, BindingResult result,
                            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors) {
                attributes.addFlashAttribute("mensagem",
                        "Erro no campo " + ((FieldError) error).getField() + ": " + error.getDefaultMessage());
            }
            return "redirect:/editar-requerimentoSALA/" + codigo;
        }
        if(requerimento.getStatus().equals("FINALIZADO")){
            attributes.addFlashAttribute("mensagem", "O requerimento já foi finalizado");
            return "redirect:/requerimentosSALA/" + codigo;
        }
        salaRepository.save(requerimento);
        attributes.addFlashAttribute("mensagem", "Requerimento alterado com sucesso!");
        return "redirect:/requerimentoSALA/" + codigo;
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
    @GetMapping("/TelaBaixaReqSala/{codigo}")
    public ModelAndView baixaRequerimento(@PathVariable("codigo") long codigo) {

        Iterable<Funcionario> funcionarios = func.findAllsetor("logistica");
        ModelAndView mv = new ModelAndView("template/Sala/TelaBaixaReqSala");
        mv.addObject("funcionario", funcionarios);

        RequerimentoSala requerimento = salaRepository.findByCodigo(codigo);
        mv.addObject("requerimento", requerimento);

        return mv;
    }



    @PostMapping("/TelaBaixaReqSala/{codigo}")
    public String baixaRequerimento(@PathVariable("codigo") long codigo, @RequestParam("matri") String matricula,
                                    @Valid @ModelAttribute("requerimento") RequerimentoSala requerimento, @NotNull BindingResult result,
                                    RedirectAttributes attributes) {
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors) {
                attributes.addFlashAttribute("mensagem",
                        "Erro no campo " + ((FieldError) error).getField() + ": " + error.getDefaultMessage());
            }
        }
        Funcionario aux = func.findByMatricula(matricula);

        if (!aux.getTipo().equals("LOGISTICA")) {
            attributes.addFlashAttribute("mensagem", "Você não é um funcionário da Logistica!");
            return "redirect:/TelaBaixaReqSala/" + codigo;
        }

        salaRepository.save(requerimento);
        LocalDateTime data = LocalDateTime.now();
        OrdemDeServicoSala ordemDeServicoSala = new OrdemDeServicoSala(data, aux,requerimento);
        OS.save(ordemDeServicoSala);

        attributes.addFlashAttribute("mensagem", "Requerimento finalizado com sucesso!");
        return "redirect:/requerimentoSALA/" + codigo;
    }








}
