package curriculumDigital.core;

import blockchain.utils.SecurityUtils;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 *
 * @author almei
 */
public class Evento implements Serializable {

    private String eventName;
    private String user;
    private String userPub;
    private String signature;


    /**
     * 
     * @param eventName Nome do evento
     * @param user Utilizador
     * @throws Exception 
     */
    public Evento(String eventName, Utilizador user) throws Exception {
        this.eventName = eventName;
        this.user = user.getName();
        this.userPub = Base64.getEncoder().encodeToString(user.getPub().getEncoded());
        sign(user.getPriv());
    }

    /**
     * 
     * @param priv Assina o evento com a chave privada, publica e o nome do evento
     * @throws Exception 
     */
    public void sign(PrivateKey priv) throws Exception {
        byte[] dataSign = SecurityUtils.sign(
                (userPub + eventName).getBytes(),
                priv);
        this.signature = Base64.getEncoder().encodeToString(dataSign);
    }

    /**
     * 
     * @return Verifica se o evento é válido
     */
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

    /**
     * 
     * @return Retorna o nome do evento
     */
    public String getEventName() {
        return eventName;
    }
    
    /**
     * 
     * @param eventName Define o nome do evento
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * 
     * @return Retorna o utilizador 
     */
    public String getUser() {
        return user;
    }

    /**
     * 
     * @param user Define o utilizador através do nome fornecido numa string 
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 
     * @return Retorna a assinatura
     */
    public String getSignature() {
        return signature;
    }

    /**
     * 
     * @param signature Define a assinatura
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * 
     * @return Retorna a chave pública do utilizador 
     */
    public String getUserPub() {
        return userPub;
    }

    /**
     * 
     * @param userPub Define a chave pública do utilizador
     */
    public void setUserPub(String userPub) {
        this.userPub = userPub;
    }

    /**
     * 
     * @return Formata os dados do evento para "NomeUser: eventos" e retorna 
     */
    @Override
    public String toString() {
        //return String.format("%-10s -> %s", user, eventName);
        return user + ": " + eventName;
    }

    private static final long serialVersionUID = 202208224663L;

}
