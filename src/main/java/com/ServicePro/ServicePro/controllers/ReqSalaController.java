package com.ServicePro.ServicePro.controllers;

import com.ServicePro.ServicePro.mensageria.RabbitmqConstantes;
import com.ServicePro.ServicePro.mensageria.RabbitmqService;
import com.ServicePro.ServicePro.models.*;
import com.ServicePro.ServicePro.service.FuncionarioService;
import com.ServicePro.ServicePro.service.OrdemDeServicoSalaService;
import com.ServicePro.ServicePro.service.ReqSALAService;
import com.ServicePro.ServicePro.template.TemplateMSG;
import com.ServicePro.ServicePro.utils.ValidacaoUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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

import static com.ServicePro.ServicePro.utils.FormatadorUtil.FormatadorDeData;

@Controller
public class ReqSalaController {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private OrdemDeServicoSalaService ordemDeServicoSalaService;

    @Autowired
    private ReqSALAService reqSALAService;

    @Autowired
    private RabbitmqService rabbitmqService;

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
        else if (!ValidacaoUtil.validarCPF(req.getCpf())) {
            attributes.addFlashAttribute("mensagem", "CPF invalido!");
            return "redirect:/cadastrarReqSala";

        }else if(reqSALAService.BuscarPorCpf(req.getCpf()) !=null){
            if(req.getStatus() == "PENDENTE")
                attributes.addFlashAttribute("mensagem", "Já Existe um requerimento pendente associado a esse cpf !");
            return "redirect:/cadastrarReqSala";

        }else if(funcionarioService.buscarPorCPF(req.getCpf())!=null){
            attributes.addFlashAttribute("mensagem", "Você é um funcionário, funcionário não pode abrir requerimento");
            return "redirect:/cadastrarReqSala";
        } else if (req.getHoraInicial() == req.getHoraFinal()) {
            attributes.addFlashAttribute("mensagem", "Horario inicial igual ao Horario final");
            return "redirect:/cadastrarReqSala";
        }

        try {
            req.setData(FormatadorDeData(req.getData()));
            if (reqSALAService.salvar(req)) {
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



    //recebendo parâmetros como inteiros , na pagina 0 exponha 10 requerimentos finalizados
    @GetMapping("/requerimentosSALAFinalizados")
    @CacheEvict(value="requerimentosSALAFinalizados", allEntries = true)
    @CachePut("requerimentosSALAFinalizados")
    public ModelAndView PagelistaDeFinalizados(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        ModelAndView mv = new ModelAndView("template/Sala/lista-req-sala-finalizado");

        Pageable pageable = PageRequest.of(page, size);

        Page<RequerimentoSala> requerimentosPage = reqSALAService.BuscarStatusFinalizado(pageable);

        mv.addObject("requerimentos", requerimentosPage.getContent());
        mv.addObject("currentPage", requerimentosPage.getNumber());
        mv.addObject("totalPages", requerimentosPage.getTotalPages());
        return mv;
    }




    @GetMapping("/requerimentosSALA")
    @CacheEvict(value="requerimentosSALA", allEntries = true)
    @CachePut("requerimentosSALA")
    public ModelAndView PagelistaPendente(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        ModelAndView mv = new ModelAndView("template/Sala/lista-sala.html");

        Pageable pageable = PageRequest.of(page, size);

        Page<RequerimentoSala> requerimentosPage = reqSALAService.BuscarStatusPendente(pageable);

        mv.addObject("requerimentos", requerimentosPage.getContent());
        mv.addObject("currentPage", requerimentosPage.getNumber());
        mv.addObject("totalPages", requerimentosPage.getTotalPages());
        return mv;
    }

    @GetMapping("/requerimentoSALA/{codigo}")
    public ModelAndView detalhesSALA(@PathVariable("codigo") long codigo) {
        RequerimentoSala requerimento = reqSALAService.buscarPorCodigo(codigo);
        ModelAndView mv = new ModelAndView("template/Sala/detalhes-sala.html");
        mv.addObject("requerimento", requerimento);
        return mv;
    }

    @GetMapping("/deletarReqSALA/{codigo}")
    public String deletarReq(@PathVariable("codigo") long codigo) {
        RequerimentoSala req = reqSALAService.buscarPorCodigo(codigo);
        reqSALAService.deletar(req);
        return "redirect:/requerimentosSALA";
    }

    @GetMapping("/editar-requerimentoSALA/{codigo}")
    public ModelAndView editarRequerimento(@PathVariable("codigo") long codigo) {
        RequerimentoSala requerimento = reqSALAService.buscarPorCodigo(codigo);
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
        reqSALAService.salvar(requerimento);
        attributes.addFlashAttribute("mensagem", "Requerimento alterado com sucesso!");
        return "redirect:/requerimentoSALA/" + codigo;
    }


    @GetMapping("/TelaBaixaReqSala/{codigo}")
    public ModelAndView baixaRequerimento(@PathVariable("codigo") long codigo) {

        Iterable<Funcionario> funcionarios = funcionarioService.BuscarPorsetor("LOGISTICA");
        ModelAndView mv = new ModelAndView("template/Sala/TelaBaixaReqSala");
        mv.addObject("funcionario", funcionarios);

        RequerimentoSala requerimento = reqSALAService.buscarPorCodigo(codigo);
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
        Funcionario aux = funcionarioService.buscarPorMatricula(matricula);

        if (!aux.getTipo().equals("LOGISTICA")) {
            attributes.addFlashAttribute("mensagem", "Você não é um funcionário da Logistica!");
            return "redirect:/TelaBaixaReqSala/" + codigo;
        }


        reqSALAService.salvar(requerimento);

        LocalDateTime data = LocalDateTime.now();
        OrdemDeServicoSala ordemDeServicoSala = new OrdemDeServicoSala(data,aux.getMatricula(), aux.getNome(),
                requerimento.getMatricula(),requerimento.getNomeRequerente());
        ordemDeServicoSalaService.salvar(ordemDeServicoSala);

        //mandando msg para o broker
        String msg = "O Requerimento de Sala "+requerimento.getCodigo()+" foi finalizado no dia "+data+
                "Mensagem da Área técnica "+requerimento.getMensagemRetorno();

        TemplateMSG mensagem = new TemplateMSG(msg,requerimento.getEmail(),requerimento.getNomeRequerente());
        this.rabbitmqService.enviaMensagem(RabbitmqConstantes.FILA_SALA,mensagem);

        attributes.addFlashAttribute("mensagem", "Requerimento finalizado com sucesso!");
        return "redirect:/requerimentoSALA/" + codigo;
    }








}
