package propostas.microservice.cartao;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Bloqueio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime instanteBloqueio = LocalDateTime.now();
    private String ipCliente;
    private String userAgent;

    @Deprecated
    public Bloqueio() {
    }

    public Bloqueio(String ipCliente, String userAgent) {
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
    }
}
