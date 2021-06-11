package propostas.microservice.associaCartao;

import propostas.microservice.cartao.Cartao;

import javax.validation.constraints.NotBlank;


public class AssociaCartaoResponse {

    @NotBlank
    private String id;
    @NotBlank
    private String emitidoEm;
    @NotBlank
    private String titular;
    @NotBlank
    private String idProposta;


    public String getId() {
        return id;
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

    public Cartao converter() {
        return new Cartao(id, emitidoEm, titular, idProposta);
    }
}
