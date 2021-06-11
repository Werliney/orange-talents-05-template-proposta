package propostas.microservice.cartao;

import javax.validation.constraints.NotNull;

public class BiometriaForm {

    @NotNull
    private byte[] biometria;

    public byte[] getBiometria() {
        return biometria;
    }


    public Biometria converter() {
        return new Biometria(biometria);
    }
}
