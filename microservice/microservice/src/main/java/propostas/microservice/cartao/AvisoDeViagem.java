package propostas.microservice.cartao;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class AvisoDeViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destinoViagem;
    @Future
    private LocalDate dataTerminoDeViagem;
    private LocalDateTime instanteAvisoDeViagem = LocalDateTime.now();
    private String ipCliente;
    private String userAgent;

    @OneToOne
    private Cartao cartao;

    @Deprecated
    public AvisoDeViagem() {
    }

    public AvisoDeViagem(String destinoViagem, LocalDate dataTerminoDeViagem, String ipCliente, String userAgent, Cartao cartao) {
        this.destinoViagem = destinoViagem;
        this.dataTerminoDeViagem = dataTerminoDeViagem;
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }

    public String getDestinoViagem() {
        return destinoViagem;
    }

    public LocalDate getDataTerminoDeViagem() {
        return dataTerminoDeViagem;
    }
}
