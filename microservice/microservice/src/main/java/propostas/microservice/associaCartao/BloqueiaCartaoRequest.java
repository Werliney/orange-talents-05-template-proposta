package propostas.microservice.associaCartao;

public class BloqueiaCartaoRequest {

    private String sistemaResponsavel;

    public BloqueiaCartaoRequest(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}
