package propostas.microservice.carteira;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import propostas.microservice.cartao.Cartao;

import java.util.Optional;


public interface CarteiraDigitalRepository extends JpaRepository<CarteiraDigital, Long> {

    @Query("SELECT c FROM CarteiraDigital c WHERE c.cartao.id = :id AND c.carteira = :carteira")
    Optional<CarteiraDigital> findCartaoInCarteira(String id, String carteira);

}
