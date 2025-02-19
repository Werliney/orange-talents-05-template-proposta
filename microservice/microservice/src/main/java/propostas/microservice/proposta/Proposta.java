package propostas.microservice.proposta;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.encrypt.Encryptors;
import propostas.microservice.analiseFinanceira.AnaliseDeSolicitacaoRequest;
import propostas.microservice.analiseFinanceira.AnaliseDeSolicitacaoResponse;
import propostas.microservice.analiseFinanceira.AnaliseSolicitacaoClient;
import propostas.microservice.cartao.Cartao;

import javax.persistence.*;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String documento;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String nome;
    @NotBlank
    private String endereco;
    @NotNull
    @Positive
    private BigDecimal salario;
    @Enumerated(EnumType.STRING)
    private StatusProposta statusProposta;
    @OneToOne
    private Cartao cartao;

        @Deprecated
    public Proposta() {
    }

    public Proposta(String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public StatusProposta getStatusProposta() {
        return statusProposta;
    }

    public void setStatusProposta(StatusProposta statusProposta) {
        this.statusProposta = statusProposta;
    }

    public void analiseFinanceira(AnaliseSolicitacaoClient analiseSolicitacaoClient) throws JsonProcessingException {
        try {
            AnaliseDeSolicitacaoRequest analiseDeSolicitacaoRequest = new AnaliseDeSolicitacaoRequest(documento, nome, id);
            AnaliseDeSolicitacaoResponse analiseResponse = analiseSolicitacaoClient.analisar(analiseDeSolicitacaoRequest);
            if(analiseResponse.getResultadoSolicitacao().equals("SEM_RESTRICAO")) {
                statusProposta = StatusProposta.ELEGIVEL;
            }
        } catch (FeignException e) {
            AnaliseDeSolicitacaoResponse analiseResponse = new ObjectMapper().readValue(e.contentUTF8(),
                    AnaliseDeSolicitacaoResponse.class);
            if (e.status() == HttpStatus.UNPROCESSABLE_ENTITY.value()
                    && analiseResponse.getResultadoSolicitacao().equals("COM_RESTRICAO")) {
                statusProposta = StatusProposta.NAO_ELEGIVEL;
            }
        }
    }
}
