package curriculumDigital.core;

import blockchain.utils.AddEventTask;
import blockchain.utils.Block;
import blockchain.utils.BlockChain;
import blockchain.utils.BlockChainEventsParallel;
import blockchain.utils.ObjectUtils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author almei
 */
public class CurriculumDigital implements Serializable {

    private ArrayList<Evento> ledgerEventos;
    private BlockChain blockchain;
    private static final int DIFICULDADE = 4;

    /**
     *
     */
    public CurriculumDigital() {
        ledgerEventos = new ArrayList<>();
        blockchain = new BlockChain();
    }

    /**
     *
     * @return
     */
    public List<Evento> getledgerEventos() {
        return ledgerEventos;
    }

    /**
     *
     * @return Formata os dados obtidos de um bloco para string
     */
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

    /**
     *
     * @param fileName
     * @throws java.io.IOException
     * @throws java.lang.Exception
     * @throws java.lang.ClassNotFoundException
     */
    public void save(String fileName) throws IOException, ClassNotFoundException, Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(fileName))) {
            out.writeObject(this);
        }
    }

    /**
     *
     * @param fileName - Pede um ficheiro para carregar
     * @return Retorna o objet CurriculumDigital com os dados da blockchain e
     * ledger de eventos
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static CurriculumDigital load(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(fileName))) {
            return (CurriculumDigital) in.readObject();
        }
    }

    /**
     *
     * @param evento
     * @return Permite validar um evento
     * @throws java.lang.Exception
     */
    public boolean isValid(Evento evento) throws Exception {
        // Lógica de validação, implementar futuramente
        return true;
    }

    /**
     *
     * @param evento Permite adicionar um evento na blokchain
     * @throws Exception
     */
    public void addEvent(Evento evento, String merkleRoot) throws Exception {
        if (isValid(evento)) {
            ledgerEventos.add(evento);
            String txtEvent = ObjectUtils.convertObjectToBase64(evento);
            blockchain.add(txtEvent, DIFICULDADE, merkleRoot);
        } else {
            throw new Exception("Evento não é válido");
        }
    }

    /**
     *
     * @return Retorna os eventos presentes nos blocos da blockchain utilizado
     * Threads
     * @throws Exception
     */
    public List<Evento> getBlockchainEvents() throws Exception {
        
        int cores = Runtime.getRuntime().availableProcessors();
        // Se houver menos blocos do que cores, o numero de threads criado vai ser o numero de blocos existentes
        cores = Math.min(cores, blockchain.getChain().size());

        BlockChainEventsParallel[] eventos = new BlockChainEventsParallel[cores];
        int workLoad = blockchain.getChain().size() / cores;
        List<Evento> listaEventos = new ArrayList<>();

        for (int i = 0; i < cores; i++) {
            int ini = i * workLoad;
            int fin = ini + workLoad;

            eventos[i] = new BlockChainEventsParallel(blockchain.getChain(), ini, fin);
            eventos[i].start();
        }

        for (int i = 0; i < cores; i++) {
            eventos[i].join();
            listaEventos.addAll(eventos[i].listaEventos);
        }

        return listaEventos;
    }

    /**
     *
     * @return Retorna todos os utilizadores guardados na Ledger
     */
    public List<String> getUsers() {
        ArrayList<String> users = new ArrayList<>();
        // Obtem utilizadores a partir dos eventos
        for (Evento evento : ledgerEventos) {
            if (!users.contains(evento.getEventName())) {
                users.add(evento.getEventName());
            }
        }
        return users;
    }

    private static final long serialVersionUID = 2022082217356L;

}
