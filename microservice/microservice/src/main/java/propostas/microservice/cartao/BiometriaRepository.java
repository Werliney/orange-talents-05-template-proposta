package propostas.microservice.cartao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BiometriaRepository extends JpaRepository<Biometria, Long> {

    List<Biometria> findBiometriaByCartaoId(String id);
}
