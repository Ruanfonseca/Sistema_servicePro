package com.ServicePro.ServicePro.controllers;

import com.ServicePro.ServicePro.models.*;
import com.ServicePro.ServicePro.repository.*;
import com.ServicePro.ServicePro.utils.ValidacaoUtil;
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
            if(req.getStatus() == "Pendente")
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

    /*

    @GetMapping("/requerimentosFinalizados")
    public ModelAndView listaDeFinalizados() {
        ModelAndView mv = new ModelAndView("template/vaga/lista-req-finalizado");

        Iterable<Requerimento> requerimento = vr.findByStatusFinalizado();

        mv.addObject("req", requerimento);
        return mv;
    }

     */
    @GetMapping("/requerimentosProj")
    public ModelAndView lista() {
        ModelAndView mv = new ModelAndView("template/projetor/lista-projetor");

        Iterable<RequerimentoProjetor> requerimento = projetorRepository.findByStatusPendente();

        mv.addObject("requerimentos", requerimento);
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
        if(requerimento.getStatus().equals("Finalizado")){
            attributes.addFlashAttribute("mensagem", "O requerimento já foi finalizado");
            return "redirect:/requerimentosProj/" + codigo;
        }
        projetorRepository.save(requerimento);
        attributes.addFlashAttribute("mensagem", "Requerimento alterado com sucesso!");
        return "redirect:/requerimentosProj/" + codigo;
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
    @GetMapping("/TelaBaixaReqProj/{codigo}")
    public ModelAndView baixaRequerimento(@PathVariable("codigo") long codigo) {

        Iterable<Funcionario> funcionarios = func.findAllsetor("logistica");
        ModelAndView mv = new ModelAndView("template/projetor/TelaBaixaProjetor");
        mv.addObject("funcionario", funcionarios);

        RequerimentoProjetor requerimento = projetorRepository.findByCodigo(codigo);
        mv.addObject("requerimento", requerimento);

        return mv;
    }




    @PostMapping("/TelaBaixaProjetor/{codigo}")
    public String baixaRequerimento(@PathVariable("codigo") long codigo, @RequestParam("nome") String nome,
                                    @Valid @ModelAttribute("requerimentoProjetor") RequerimentoProjetor requerimento, BindingResult result,
                                    RedirectAttributes attributes) {

        if (requerimento.getStatus().equals("Finalizado")) {
            attributes.addFlashAttribute("mensagem", "O requerimento já foi finalizado");
            return "redirect:/requerimentosProj";
        }


        if(func.findByNome(nome)==null){
            attributes.addFlashAttribute("mensagem", "Esse funcionário não existe!");
            return "redirect:/TelaBaixaProjetor/" + codigo ;
        }

        Funcionario aux = func.findByNome(nome);

        if(aux.getTipo() !="logistica"){
            attributes.addFlashAttribute("mensagem", "Você não é um funcionario da coinfo!");
            return "redirect:/TelaBaixaProjetor/" + codigo ;
        }


        projetorRepository.save(requerimento);
        LocalDateTime data = LocalDateTime.now();
        OrdemDeServicoProjetor ordemDeServico = new OrdemDeServicoProjetor(data, aux, requerimento);
        OS.save(ordemDeServico);

        attributes.addFlashAttribute("mensagem", "Requerimento finalizado com sucesso!");
        return "redirect:/requerimentoProj/" + codigo;
    }


}
