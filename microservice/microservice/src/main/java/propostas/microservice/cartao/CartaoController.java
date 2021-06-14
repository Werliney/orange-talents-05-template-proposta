package propostas.microservice.cartao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes/biometria")
public class CartaoController {

    @Autowired
    CartaoRepository cartaoRepository;
    @Autowired
    BiometriaRepository biometriaRepository;

    @PostMapping("/{id}")
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

}
