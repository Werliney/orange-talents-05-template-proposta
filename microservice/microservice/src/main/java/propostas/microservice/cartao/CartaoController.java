package propostas.microservice.cartao;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import propostas.microservice.associaCartao.BloqueiaCartaoRequest;
import propostas.microservice.associaCartao.BloqueiaCartaoResponse;
import propostas.microservice.associaCartao.CartoesClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    CartaoRepository cartaoRepository;
    @Autowired
    BiometriaRepository biometriaRepository;
    @Autowired
    BloqueioRepository bloqueioRepository;
    @Autowired
    AvisoDeViagemRepository avisoDeViagemRepository;
    @Autowired
    CartoesClient cartoesClient;

    @PostMapping("/biometria/{id}")
    public ResponseEntity cadastrarBiometria(@PathVariable String id, @Valid @RequestBody BiometriaForm form, UriComponentsBuilder uriComponentsBuilder) {
        Optional<Cartao> cartao = cartaoRepository.findById(id);
        if(cartao.isPresent()) {
            Biometria biometria = form.converter();
            biometriaRepository.save(biometria);
            cartao.get().setBiometria(biometria);
            cartaoRepository.save(cartao.get());
            return ResponseEntity.created(uriComponentsBuilder.path("/cartoes/biometria/{id}").buildAndExpand(biometria.getId())
                    .toUri()).body(biometria);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/bloqueios")
    public ResponseEntity bloqueiaCartao(@PathVariable String id, BloqueioForm form, HttpServletRequest request) {
        Optional<Cartao> cartao = cartaoRepository.findById(id);

        if (!cartao.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        try {
            if (cartao.isPresent() && cartao.get().getSituacaoCartao() == SituacaoCartao.NAO_BLOQUEADO || cartao.get().getSituacaoCartao() == null) {
                String userAgent = request.getHeader("User-Agent");
                String ipCliente = request.getRemoteAddr();
                Bloqueio bloqueio = form.converter(userAgent, ipCliente, cartao.get());
                bloqueioRepository.save(bloqueio);
                cartao.get().setSituacaoCartao(SituacaoCartao.BLOQUEADO);
                cartaoRepository.save(cartao.get());
                BloqueiaCartaoRequest bloqueiaCartaoRequest = new BloqueiaCartaoRequest("proposta");
                BloqueiaCartaoResponse response = cartoesClient.bloqueia(id, bloqueiaCartaoRequest);
                return ResponseEntity.ok().build();
            }
        } catch (FeignException e) {
            e.printStackTrace();
        }
        if (cartao.isPresent() && cartao.get().getSituacaoCartao() == SituacaoCartao.BLOQUEADO) {
            return ResponseEntity.unprocessableEntity().body("Este cartão já está bloquado");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/avisoViagem")
    public ResponseEntity cadastraAvisoDeViagem(@PathVariable String id, @Valid @RequestBody AvisoDeViagemForm form, HttpServletRequest request) {
        Optional<Cartao> cartao = cartaoRepository.findById(id);
        if (!cartao.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        String userAgent = request.getHeader("User-Agent");
        String ipCliente = request.getRemoteAddr();
        AvisoDeViagem avisoDeViagem = form.converter(ipCliente, userAgent, cartao.get());
        avisoDeViagemRepository.save(avisoDeViagem);
        return ResponseEntity.ok().build();
    }
}
