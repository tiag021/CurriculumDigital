/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blockchain.utils;

import curriculumDigital.core.CurriculumDigital;
import curriculumDigital.core.Evento;
import curriculumDigital.gui.CurriculumDigitalGUI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author almei
 */
public class ListUsersTask extends SwingWorker<Void, Void> {

    CurriculumDigital curriculum;

    public ListUsersTask(CurriculumDigital curriculumDigital) {
        this.curriculum = curriculumDigital;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            List<Evento> eventos = curriculum.getBlockchainEvents();

            List<String> userNames = new ArrayList<>();

            // Extrair o userName a partir dos eventos
            for (Evento evento : eventos) {
                String nome = evento.getUser();
                // Prevenir duplicados
                if (!userNames.contains(nome)) {
                    userNames.add(nome);
                }
            }

            String[] namesArray = userNames.toArray(new String[0]);

            // Mostrar os nomes num popup
            String selectedName = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecione um utilizador:",
                    "Lista de Utilizadores",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    namesArray,
                    namesArray[0] // Default selection
            );

            // Verifica se um userName foi selecionado
            if (selectedName != null) {
                // Obtenção dos eventos desse userName
                StringBuilder eventsDetails = new StringBuilder();
                for (Evento evento : eventos) {
                    if (evento.getUser().equals(selectedName)) {
                        eventsDetails.append(evento.toString()).append("\n");
                    }
                }

                if (eventsDetails.length() > 0) {
                    JOptionPane.showMessageDialog(null, eventsDetails.toString(),
                            "Eventos de " + selectedName, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Nenhum evento encontrado para " + selectedName,
                            "Eventos", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CurriculumDigitalGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
