package com.ServicePro.ServicePro.mensageria;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConexao implements ApplicationRunner {

    private static final String NOME_EXCHANGE = "amq.direct";

    private final AmqpAdmin amqpAdmin;

    public RabbitMQConexao(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }


    private Queue fila(String nomeFila) {
        return new Queue(nomeFila, true, false, false);
    }

    private DirectExchange envioDeMsg() {
        return new DirectExchange(NOME_EXCHANGE);
    }

    private Binding relacionamento(Queue fila, DirectExchange enviomsg) {
        return new Binding(fila.getName(), Binding.DestinationType.QUEUE, enviomsg.getName(), fila.getName(), null);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //preparando as filas
        Queue filaRequerimentoWIFI = this.fila(RabbitmqConstantes.FILA_WIFI);
        Queue filaRequerimentoSALA = this.fila(RabbitmqConstantes.FILA_SALA);
        Queue filaRequerimentoProjetor = this.fila(RabbitmqConstantes.FILA_PROJETOR);
        DirectExchange enviomsg = this.envioDeMsg();

        Binding LigacaoWIFI = this.relacionamento(filaRequerimentoWIFI, enviomsg);
        Binding LigacaoSALA = this.relacionamento(filaRequerimentoSALA, enviomsg);
        Binding LigacaoPROJETOR = this.relacionamento(filaRequerimentoProjetor, enviomsg);

        //criando as filas no RabbitMQ
        this.amqpAdmin.declareQueue(filaRequerimentoWIFI);
        this.amqpAdmin.declareQueue(filaRequerimentoSALA);
        this.amqpAdmin.declareQueue(filaRequerimentoProjetor);

        //canal onde as filas estarão
        this.amqpAdmin.declareExchange(enviomsg);

        this.amqpAdmin.declareBinding(LigacaoWIFI);
        this.amqpAdmin.declareBinding(LigacaoSALA);
        this.amqpAdmin.declareBinding(LigacaoPROJETOR);
    }
}
