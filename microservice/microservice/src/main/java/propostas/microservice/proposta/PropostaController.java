package propostas.microservice.proposta;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private PropostaRepository propostaRepository;
    @Autowired
    private AnaliseSolicitacaoClient analiseDeSolicitacaoClient;

    @PostMapping
    public ResponseEntity criaProposta(@RequestBody @Valid PropostaForm form, UriComponentsBuilder uriComponentsBuilder) throws JsonProcessingException {
        Optional<Proposta> proposta2 = propostaRepository.findByDocumento(form.getDocumento());
        if(proposta2.isPresent()) {
            return ResponseEntity.unprocessableEntity().body("Já existe uma proposta para esse solicitante");
        }
        Proposta proposta = form.converter();
        proposta.analiseFinanceira(analiseDeSolicitacaoClient);
        propostaRepository.save(proposta);
        PropostaDto propostaDto = new PropostaDto(proposta);
        return ResponseEntity.created(uriComponentsBuilder.path("/propostas/{id}").buildAndExpand(propostaDto.getId()).
                toUri()).body(propostaDto);
    }
}
