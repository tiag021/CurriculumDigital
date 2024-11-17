/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blockchain.utils;

import curriculumDigital.core.CurriculumDigital;
import curriculumDigital.core.Evento;
import curriculumDigital.core.Utilizador;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 *
 * @author almei
 */
public class ShowCurriculumTask extends SwingWorker<Void, Void> {

    CurriculumDigital curriculum;
    Utilizador myUser;
    JTextArea txtCurriculum;

    public ShowCurriculumTask(CurriculumDigital curriculumDigital, Utilizador myUser, JTextArea txtCurriculum) {
        this.curriculum = curriculumDigital;
        this.myUser = myUser;
        this.txtCurriculum = txtCurriculum;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            List<Evento> userEvents = new ArrayList<>();

            for (Evento evento : curriculum.getBlockchainEvents()) {
                if (evento.getUser().equals(myUser.getName())) {
                    userEvents.add(evento);
                }
            }

            if (!userEvents.isEmpty()) {
                StringBuilder eventsDetails = new StringBuilder();
                for (Evento evento : userEvents) {
                    MerkleTree mt = new MerkleTree();

                    eventsDetails.append(evento.toString()).append("\n");
                }
                txtCurriculum.setText(eventsDetails.toString());
            } else {
                txtCurriculum.setText("Nenhum evento encontrado para " + myUser.getName() + ".");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar eventos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
