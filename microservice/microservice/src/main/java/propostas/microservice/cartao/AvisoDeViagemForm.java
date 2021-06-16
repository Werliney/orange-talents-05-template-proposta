package propostas.microservice.cartao;

import com.fasterxml.jackson.annotation.JsonFormat;


import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AvisoDeViagemForm {

    @NotBlank
    private String destinoViagem;
    @NotNull
    @Future
    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate dataTerminoDeViagem;
    private String ipCliente;
    private String userAgent;


    public String getDestinoViagem() {
        return destinoViagem;
    }

    public LocalDate getDataTerminoDeViagem() {
        return dataTerminoDeViagem;
    }

    public void setDataTerminoDeViagem(LocalDate dataTerminoDeViagem) {
        this.dataTerminoDeViagem = dataTerminoDeViagem;
    }

    public String getIpCliente() {
        return ipCliente;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public AvisoDeViagem converter( String ipCliente, String userAgent, Cartao cartao) {
        return new AvisoDeViagem(this.destinoViagem, this.dataTerminoDeViagem, ipCliente, userAgent, cartao);
    }
}
