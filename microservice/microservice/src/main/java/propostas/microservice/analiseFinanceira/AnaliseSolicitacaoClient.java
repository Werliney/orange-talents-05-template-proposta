package propostas.microservice.analiseFinanceira;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "analiseSolicitacao", url = "${analise_financeira.url}")
public interface AnaliseSolicitacaoClient {

    @RequestMapping(method = RequestMethod.POST, value = "/solicitacao")
    AnaliseDeSolicitacaoResponse analisar(AnaliseDeSolicitacaoRequest request);
}
