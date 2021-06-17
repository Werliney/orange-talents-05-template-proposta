package propostas.microservice.associaCartao;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AssociaCarteiraDigitalResponse {

    private String resultado;
    private String id;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AssociaCarteiraDigitalResponse(String resultado, String id) {
        this.resultado = resultado;
        this.id = id;
    }

    public String getResultado() {
        return resultado;
    }

    public String getId() {
        return id;
    }
}
