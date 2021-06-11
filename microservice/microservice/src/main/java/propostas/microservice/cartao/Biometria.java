package propostas.microservice.cartao;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Base64;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataBiometria = LocalDateTime.now();
    private String biometria;

    @ManyToOne
    private Cartao cartao;


    public Biometria() {
    }

    public Long getId() {
        return id;
    }

    public Biometria(byte[] biometria) {
        this.biometria = Base64.getEncoder().encodeToString(biometria);
    }
}
