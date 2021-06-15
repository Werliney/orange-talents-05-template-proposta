package propostas.microservice.cartao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

    @PostMapping("/bloqueio/{id}")
    public ResponseEntity bloqueiaCartao(@PathVariable String id, BloqueioForm form, HttpServletRequest request) {
        Optional<Cartao> cartao = cartaoRepository.findById(id);

        if (!cartao.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        if (cartao.isPresent() && cartao.get().getSituacaoCartao() == SituacaoCartao.NAO_BLOQUEADO || cartao.get().getSituacaoCartao() == null) {
            String userAgent = request.getHeader("User-Agent");
            String ipCliente = request.getRemoteAddr();
            Bloqueio bloqueio = form.converter(userAgent, ipCliente);
            bloqueioRepository.save(bloqueio);
            cartao.get().setBloqueio(bloqueio);
            cartao.get().setSituacaoCartao(SituacaoCartao.BLOQUEADO);
            cartaoRepository.save(cartao.get());
            return ResponseEntity.ok().build();
        }
        if (cartao.isPresent() && cartao.get().getSituacaoCartao() == SituacaoCartao.BLOQUEADO) {
            return ResponseEntity.unprocessableEntity().body("Este cartão já está bloquado");
        }
        return ResponseEntity.ok().build();
    }
}
