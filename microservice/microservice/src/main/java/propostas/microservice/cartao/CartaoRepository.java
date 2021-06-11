package propostas.microservice.cartao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartaoRepository extends JpaRepository<Cartao, String> {

}
