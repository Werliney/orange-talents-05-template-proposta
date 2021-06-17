package propostas.microservice.carteira;

import propostas.microservice.cartao.Cartao;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CarteiraDigitalForm {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String carteira;

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira;
    }

    public CarteiraDigital converter(Cartao cartao) {
        return new CarteiraDigital(email, carteira, cartao);
    }
}
