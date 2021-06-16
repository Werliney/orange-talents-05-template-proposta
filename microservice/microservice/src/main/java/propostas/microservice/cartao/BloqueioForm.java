package propostas.microservice.cartao;


public class BloqueioForm {

    private String ipCliente;
    private String userAgent;


    public String getIpCliente() {
        return ipCliente;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Bloqueio converter(String userAgent, String ipCliente, Cartao cartao) {
        return new Bloqueio(ipCliente, userAgent, cartao);
    }
}
