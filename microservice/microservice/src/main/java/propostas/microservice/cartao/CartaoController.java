package propostas.microservice.cartao;

import feign.FeignException;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import propostas.microservice.associaCartao.*;
import propostas.microservice.carteira.CarteiraDigital;
import propostas.microservice.carteira.CarteiraDigitalForm;
import propostas.microservice.carteira.CarteiraDigitalRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    CarteiraDigitalRepository carteiraDigitalRepository;
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

        try {
            String userAgent = request.getHeader("User-Agent");
            String ipCliente = request.getRemoteAddr();
            AvisoDeViagem avisoDeViagem = form.converter(ipCliente, userAgent, cartao.get());
            String destinoViagem = form.getDestinoViagem();
            String validoAte = form.getDataTerminoDeViagem().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate validoAteNormal = form.getDataTerminoDeViagem();
            System.out.println(destinoViagem);
            System.out.println(validoAte);
            System.out.println(validoAteNormal);
            avisoDeViagemRepository.save(avisoDeViagem);
            NotificaAvisoViagemRequest notificaAvisoViagemRequest = new NotificaAvisoViagemRequest(destinoViagem, validoAteNormal);
            NotificaAvisoViagemResponse response = cartoesClient.notifica(cartao.get().getId(), notificaAvisoViagemRequest);
            return ResponseEntity.ok().build();
        } catch (FeignException e) {
            return ResponseEntity.unprocessableEntity().build();
        }

    }

    @PostMapping("/{id}/carteiras")
    public ResponseEntity associaCarteiraDigital(@PathVariable String id, @Valid @RequestBody CarteiraDigitalForm form, UriComponentsBuilder uriComponentsBuilder) {
        Optional<Cartao> cartao = cartaoRepository.findById(id);
        if (!cartao.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        CarteiraDigital carteiraDigital = form.converter(cartao.get());
        System.out.println(carteiraDigital.getEmail() + carteiraDigital.getCarteira());

        if(carteiraDigital.getCartao().equals(cartao.get())) {
            return ResponseEntity.unprocessableEntity().build();
        }

        try {
            carteiraDigitalRepository.save(carteiraDigital);
            AssociaCarteiraDigitalRequest associaCarteiraDigitalRequest = new AssociaCarteiraDigitalRequest(carteiraDigital.getEmail(), carteiraDigital.getCarteira());
            AssociaCarteiraDigitalResponse response = cartoesClient.associaCarteira(cartao.get().getId(), associaCarteiraDigitalRequest);
            return ResponseEntity.created(uriComponentsBuilder.path("/cartoes/carteiras/{id}").buildAndExpand(carteiraDigital.getId())
                    .toUri()).body(carteiraDigital);

        } catch (FeignException e) {
            return ResponseEntity.unprocessableEntity().body("Feign");
        }
    }
}
