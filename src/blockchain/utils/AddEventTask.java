/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blockchain.utils;

import curriculumDigital.core.CurriculumDigital;
import curriculumDigital.core.Evento;
import javax.swing.SwingWorker;

/**
 *
 * @author almei
 */
public class AddEventTask extends SwingWorker<Void, Void> {

    private Evento evento;
    private String merkleRoot;
    private CurriculumDigital curriculumDigital;

    public AddEventTask(CurriculumDigital curriculumDigital, Evento evento, String merkleRoot) {
        this.curriculumDigital = curriculumDigital;
        this.evento = evento;
        this.merkleRoot = merkleRoot;
    }

    @Override
    protected Void doInBackground() throws Exception {
        curriculumDigital.addEvent(evento, merkleRoot);
        return null;
    }
}
