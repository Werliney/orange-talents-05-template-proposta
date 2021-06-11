package propostas.microservice.associaCartao;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import propostas.microservice.cartao.Cartao;
import propostas.microservice.cartao.CartaoRepository;
import propostas.microservice.proposta.Proposta;
import propostas.microservice.proposta.PropostaRepository;

import java.util.List;

@Component
public class AssociaCartao {

    private Long idProposta;

    private final Logger logger = LoggerFactory.getLogger(AssociaCartao.class);

    @Autowired
    CartoesClient cartoesClient;
    @Autowired
    PropostaRepository propostaRepository;
    @Autowired
    CartaoRepository cartaoRepository;

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
                    Cartao cartao = associaCartaoResponse.converter();
                    System.out.println("Cartao " + associaCartaoResponse.getId() + "-------------------------");
                    cartaoRepository.save(cartao);
                    System.out.println("Cartao Salvo " + cartao.getId() + "-------------------------");
                    p.setCartao(cartao);
                    propostaRepository.save(p);
                } catch (FeignException e) {
                        logger.info("Não foi possível associar um cartão à proposta com o id={} e documento={}", p.getId(), p.getDocumento());
                }
        });
    }
}
