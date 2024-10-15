package curriculumDigital.core;

import blockchain.utils.Block;
import blockchain.utils.BlockChain;
import blockchain.utils.ObjectUtils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CurriculumDigital implements Serializable {

    private ArrayList<Evento> ledgerEventos;
    private BlockChain blockchain;
    private static final int DIFICULDADE = 4;

    public CurriculumDigital() throws Exception {
        ledgerEventos = new ArrayList<>();
        blockchain = new BlockChain();
    }

    public List<Evento> getledgerEventos() {
        return ledgerEventos;
    }

    public String toString() {
        StringBuilder txt = new StringBuilder();
        for (Block b : blockchain.getChain()) {
            Evento evento = (Evento) ObjectUtils.convertBase64ToObject(b.getData());
            txt.append(b.getPreviousHash()).append(" ")
                    .append(evento.toString()).append(" ")
                    .append(b.getNonce()).append(" ")
                    .append(b.getCurrentHash()).append("\n");
        }
        return txt.toString();
    }

    public void save(String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
        }
    }

    public static CurriculumDigital load(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (CurriculumDigital) in.readObject();
        }
    }

    public boolean isValid(Evento evento) throws Exception {
        // Lógica de validação
        return true;
    }

    public void addEvent(Evento evento) throws Exception {
        if (isValid(evento)) {
            ledgerEventos.add(evento);
            String eventData = ObjectUtils.convertObjectToBase64(evento);
            blockchain.add(eventData, DIFICULDADE);
        } else {
            throw new Exception("Evento não é válido");
        }
    }

    public List<Evento> getBlockchainEvents() throws Exception {
        List<Evento> listaEventos = new ArrayList<>();
        for (Block b : blockchain.getChain()) {
            Evento evento = (Evento) ObjectUtils.convertBase64ToObject(b.getData());
            listaEventos.add(evento);
        }
        return listaEventos;
    }

    public List<String> getUsers() {
        ArrayList<String> users = new ArrayList<>();
        // Obtem usuários a partir dos eventos
        for (Evento evento : ledgerEventos) {
            if (!users.contains(evento.getEventName())) {
                users.add(evento.getEventName());
            }
        }
        return users;
    }
}
