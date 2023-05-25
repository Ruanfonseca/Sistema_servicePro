package com.ServicePro.ServicePro.MockTest;
import com.ServicePro.ServicePro.controllers.ReqWifiController;
import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.models.OrdemDeServico;
import com.ServicePro.ServicePro.models.Requerimento;
import com.ServicePro.ServicePro.repository.FuncionarioRepository;
import com.ServicePro.ServicePro.repository.FuncionarioRepository;
import com.ServicePro.ServicePro.repository.OrdemDeServicoRepository;
import com.ServicePro.ServicePro.repository.RequerimentoWIfiRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RequerimentoControllerTest {
    @Mock
    private FuncionarioRepository func;

    @Mock
    private RequerimentoWIfiRepository vr;

    @Mock
    private OrdemDeServicoRepository OS;

    @InjectMocks
    private ReqWifiController requerimentoController;

    private MockMvc mockMvc;

    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(requerimentoController).build();
    }

    @Test
    public void testBaixaRequerimento_Finalizado() throws Exception {
        setup();
        RedirectAttributes attributes = mock(RedirectAttributes.class);
        Requerimento requerimento = new Requerimento();
        requerimento.setStatus("FINALIZADO");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/TelaBaixaReq/1")
                .param("matricula", "1923333070")
                .flashAttr("requerimento", requerimento);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.redirectedUrl("/requerimentos"))
                .andExpect(MockMvcResultMatchers.flash().attribute("mensagem", "O requerimento já foi finalizado"));

       // verifyZeroInteractions(func, vr, OS);
        verify(attributes).addFlashAttribute(eq("mensagem"), eq("O requerimento já foi finalizado"));
    }

    @Test
    public void testBaixaRequerimento_FuncionarioNaoExiste() throws Exception {
        setup();
        RedirectAttributes attributes = mock(RedirectAttributes.class);
        when(func.findByMatricula(anyString())).thenReturn(null);
        Requerimento requerimento = new Requerimento();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/TelaBaixaReq/1")
                .param("matricula", "1923333070")
                .flashAttr("requerimento", requerimento);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.redirectedUrl("/TelaBaixaReq/1"))
                .andExpect(MockMvcResultMatchers.flash().attribute("mensagem", "Esse funcionário não existe!"));

        verify(func).findByMatricula(eq("123"));
        //verifyZeroInteractions(vr, OS);
        verify(attributes).addFlashAttribute(eq("mensagem"), eq("Esse funcionário não existe!"));
    }

    @Test
    public void testBaixaRequerimento_FuncionarioNaoCoinfo() throws Exception {
        setup();
        RedirectAttributes attributes = mock(RedirectAttributes.class);
        Funcionario funcionario = new Funcionario();
        funcionario.setTipo("logistica");
        when(func.findByMatricula(anyString())).thenReturn(funcionario);
        Requerimento requerimento = new Requerimento();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/TelaBaixaReq/1")
                .param("matricula", "123")
                .flashAttr("requerimento", requerimento);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.redirectedUrl("/TelaBaixaReq/1"))
                .andExpect(MockMvcResultMatchers.flash().attribute("mensagem", "Você não é um funcionario da coinfo!"));

        verify(func).findByMatricula(eq("1923333070"));
        //verifyZeroInteractions(vr, OS);
        verify(attributes).addFlashAttribute(eq("mensagem"), eq("Você não é um funcionario da coinfo!"));
    }


    @Test
    public void testBaixaRequerimento_Sucesso() throws Exception {
        setup();
        RedirectAttributes attributes = mock(RedirectAttributes.class);
        Funcionario funcionario = new Funcionario();
        funcionario.setTipo("Coinfo");
        when(func.findByMatricula(anyString())).thenReturn(funcionario);
        Requerimento requerimento = new Requerimento();
        requerimento.setStatus("PENDENTE");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/TelaBaixaReq/1")
                .param("matricula", "1923333070")
                .flashAttr("requerimento", requerimento);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.redirectedUrl("/requerimento/1"))
                .andExpect(MockMvcResultMatchers.flash().attribute("mensagem", "Requerimento finalizado com sucesso!"));

        verify(func).findByMatricula(eq("1923333070"));
        verify(vr).save(eq(requerimento));
        verify(OS).save(any(OrdemDeServico.class));
        verify(attributes).addFlashAttribute(eq("mensagem"), eq("Requerimento finalizado com sucesso!"));
    }
}
