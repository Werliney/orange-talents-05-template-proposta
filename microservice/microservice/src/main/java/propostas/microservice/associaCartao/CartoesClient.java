package propostas.microservice.associaCartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "AssociaCartao", url = "${associa_cartao.url}")
public interface CartoesClient {

    @GetMapping
    AssociaCartaoResponse associar(@RequestParam(value = "idProposta") String idProposta);
    @PostMapping("/{id}/bloqueios")
    BloqueiaCartaoResponse bloqueia(@PathVariable String id, BloqueiaCartaoRequest request);
    @PostMapping("/{id}/avisos")
    NotificaAvisoViagemResponse notifica(@PathVariable String id, NotificaAvisoViagemRequest request);
}
