package curriculumDigital.core;

import blockchain.utils.SecurityUtils;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Evento implements Serializable {

    private String eventName;
    private Utilizador user;
    private String userPub;
    private String signature;

    // Construtor para criar um evento a partir de uma string e um utilizador
    public Evento(String eventName, Utilizador user) throws Exception {
        this.eventName = eventName;
        this.user = user;
        this.userPub = Base64.getEncoder().encodeToString(user.getPub().getEncoded());
        sign(user.getPriv());
    }

    public void sign(PrivateKey priv) throws Exception {
        byte[] dataSign = SecurityUtils.sign(
                (userPub + eventName).getBytes(),
                priv);
        this.signature = Base64.getEncoder().encodeToString(dataSign);
    }

    public boolean isValid() {
        try {
            PublicKey pub = SecurityUtils.getPublicKey(Base64.getDecoder().decode(userPub));
            byte[] data = (userPub + eventName).getBytes();
            byte[] sign = Base64.getDecoder().decode(signature);
            return SecurityUtils.verifySign(data, sign, pub);
        } catch (Exception ex) {
            return false;
        }
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Utilizador getUser() {
        return user;
    }

    public void setUser(Utilizador user) {
        this.user = user;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUserPub() {
        return userPub;
    }

    public void setUserPub(String userPub) {
        this.userPub = userPub;
    }

    @Override
    public String toString() {
        return String.format("Evento: %s, Registado por: %s, Assinatura válida: %b", eventName, user != null ? user.getName() : "Desconhecido", isValid());
    }

}
