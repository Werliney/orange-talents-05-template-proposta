package propostas.microservice.proposta;

import java.math.BigDecimal;

public class DetalhaPropostaDto {

    private String documento;
    private String email;
    private String endereco;
    private String nome;
    private BigDecimal salario;
    private StatusProposta statusProposta;
    private String numeroCartao;

    public DetalhaPropostaDto(Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.email = proposta.getEmail();
        this.endereco = proposta.getEndereco();
        this.nome = proposta.getNome();
        this.salario = proposta.getSalario();
        this.statusProposta = proposta.getStatusProposta();
        this.numeroCartao = proposta.getNumeroCartao();
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public StatusProposta getStatusProposta() {
        return statusProposta;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }
}
