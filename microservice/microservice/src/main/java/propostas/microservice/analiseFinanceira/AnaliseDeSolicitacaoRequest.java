package propostas.microservice.analiseFinanceira;

import propostas.microservice.annotations.CPFOrCNPJ;
import propostas.microservice.proposta.Proposta;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AnaliseDeSolicitacaoRequest {

    @NotBlank
    @CPFOrCNPJ
    private String documento;
    @NotBlank
    private String nome;
    @NotNull
    private Long idProposta;

    public AnaliseDeSolicitacaoRequest(@NotBlank String documento, @NotBlank String nome, @NotBlank Long idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public Long getIdProposta() {
        return idProposta;
    }
}
