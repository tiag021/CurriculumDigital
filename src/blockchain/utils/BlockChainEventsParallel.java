/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blockchain.utils;

import curriculumDigital.core.Evento;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author almei
 */
public class BlockChainEventsParallel extends Thread {

    List<Block> blocks;
    public List<Evento> listaEventos = new ArrayList<>();
    int ini, fin;

    public BlockChainEventsParallel(List<Block> blocks, int ini, int fin) {
        this.blocks = blocks;
        this.ini = ini;
        this.fin = fin;
    }

    public void run() {
        for (int i = ini; i < fin; i++) {
            Evento evento = (Evento) ObjectUtils.convertBase64ToObject(blocks.get(i).getData());
            listaEventos.add(evento);
        }
    }

}
