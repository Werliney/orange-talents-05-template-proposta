package propostas.microservice.associaCartao;

import com.fasterxml.jackson.annotation.JsonCreator;

public class BloqueiaCartaoResponse {

    private String resultado;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BloqueiaCartaoResponse(String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }
}
