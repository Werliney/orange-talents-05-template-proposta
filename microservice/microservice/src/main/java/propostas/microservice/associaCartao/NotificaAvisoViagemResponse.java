package propostas.microservice.associaCartao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificaAvisoViagemResponse {

    @JsonProperty
    private String resultado;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public NotificaAvisoViagemResponse(String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }
}
