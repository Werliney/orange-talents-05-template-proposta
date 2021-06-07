package propostas.microservice.proposta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    private PropostaRepository propostaRepository;

    @PostMapping
    public ResponseEntity criaProposta(@RequestBody @Valid PropostaForm form, UriComponentsBuilder uriComponentsBuilder) {
        Proposta proposta = form.converter();
        propostaRepository.save(proposta);
        PropostaDto propostaDto = new PropostaDto(proposta);
        return ResponseEntity.created(uriComponentsBuilder.path("/propostas/{id}").buildAndExpand(propostaDto.getId()).
                toUri()).body(propostaDto);
    }
}
