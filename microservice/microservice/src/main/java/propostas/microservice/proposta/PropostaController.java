package propostas.microservice.proposta;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import propostas.microservice.analiseFinanceira.AnaliseSolicitacaoClient;
import propostas.microservice.associaCartao.AssociaCartao;
import propostas.microservice.associaCartao.CartoesClient;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private final Tracer tracer;

    public PropostaController(Tracer tracer) {
        this.tracer = tracer;
    }

    @Autowired
    private PropostaRepository propostaRepository;
    @Autowired
    private AnaliseSolicitacaoClient analiseDeSolicitacaoClient;

    @PostMapping
    public ResponseEntity criaProposta(@RequestBody @Valid PropostaForm form, UriComponentsBuilder uriComponentsBuilder) throws JsonProcessingException {
        TextEncryptor encryptor = Encryptors.queryableText("encryptorSenha", "932653696ae557f8e17f");
        String documento = encryptor.encrypt(form.getDocumento());
        Optional<Proposta> proposta2 = propostaRepository.findByDocumento(documento);
        if(proposta2.isPresent()) {
            return ResponseEntity.unprocessableEntity().body("Já existe uma proposta para esse solicitante");
        }
        Proposta proposta = form.converter();
        proposta.analiseFinanceira(analiseDeSolicitacaoClient);
        propostaRepository.save(proposta);
        PropostaDto propostaDto = new PropostaDto(proposta);
        Span activeSpan = tracer.activeSpan();
        activeSpan.setTag("user.email", propostaDto.getEmail());
        return ResponseEntity.created(uriComponentsBuilder.path("/propostas/{id}").buildAndExpand(propostaDto.getId()).
                toUri()).body(propostaDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhaProposta(@PathVariable Long id) {
        Optional<Proposta> proposta = propostaRepository.findById(id);

        if(proposta.isPresent()) {
            return ResponseEntity.ok().body(new DetalhaPropostaDto(proposta.get()));
        }
        return ResponseEntity.notFound().build();
    }
}
