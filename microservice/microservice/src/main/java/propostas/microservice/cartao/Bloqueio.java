package propostas.microservice.cartao;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Bloqueio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime instanteBloqueio = LocalDateTime.now();
    private String ipCliente;
    private String userAgent;

    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public Bloqueio() {
    }

    public Bloqueio(String ipCliente, String userAgent, Cartao cartao) {
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }

    public Bloqueio(String ipCliente, String userAgent) {
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
    }
}
