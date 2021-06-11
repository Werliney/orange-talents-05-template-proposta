package propostas.microservice.cartao;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cartao {
    @Id
    private String id;
    @NotBlank
    private String emitidoEm;
    @NotBlank
    private String titular;
    private String idProposta;

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Biometria> biometria = new ArrayList<>();

    @Deprecated
    public Cartao() {
    }

    public Cartao(String id, String emitidoEm, String titular, String idProposta) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.idProposta = idProposta;
    }

    public List<Biometria> getBiometria() {
        return biometria;
    }

    public void setBiometria(Biometria biometria) {
        this.biometria.add(biometria);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmitidoEm() {
        return emitidoEm;
    }

    public String getTitular() {
        return titular;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
