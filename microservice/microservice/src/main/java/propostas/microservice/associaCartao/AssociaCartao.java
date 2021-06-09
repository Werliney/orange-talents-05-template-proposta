package propostas.microservice.associaCartao;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import propostas.microservice.proposta.Proposta;
import propostas.microservice.proposta.PropostaRepository;

import java.util.List;

@Component
public class AssociaCartao {

    private Long idProposta;

    @Autowired
    CartoesClient cartoesClient;
    @Autowired
    PropostaRepository propostaRepository;

    public Long getIdProposta() {
        return idProposta;
    }

    @Scheduled(initialDelay = 2000, fixedRate = 5000)
    public void associaCartao() {
        List<Proposta> proposta = propostaRepository.findByStatusIsElegivelAndCartaoIsNull();
        proposta.stream().forEach(p -> {
            System.out.println(p.getId() + p.getNome());
                try {
                    AssociaCartaoResponse associaCartaoResponse = cartoesClient.associar(p.getId().toString());
                    p.setNumeroCartao(associaCartaoResponse.getId());
                    propostaRepository.save(p);
                } catch (FeignException e) {
                        System.out.println("Erro no Serviço");
                        e.printStackTrace();
                }
        });
    }
}
