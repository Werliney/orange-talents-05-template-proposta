package propostas.microservice.associaCartao;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AssociaCarteiraDigitalRequest {

    private String email;
    private String carteira;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AssociaCarteiraDigitalRequest(String email, String carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira;
    }
}
