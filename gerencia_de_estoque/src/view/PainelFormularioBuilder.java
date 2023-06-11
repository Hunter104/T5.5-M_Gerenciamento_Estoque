package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PainelFormularioBuilder {
    public <T extends JComponent> PainelFormularioBuilder(JPanel painel,ArrayList<T> componentesEsquerdos,
                                                   ArrayList<T> componentesDireitos, String titulo) {

        painel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel labelTitulo = new JLabel(titulo);

        // Ajuste de título
        c.anchor = GridBagConstraints.CENTER;   // alinhamento dentro das célula
        c.weightx = 1;                          // % do espaço horizontal
        c.gridwidth = 2;                        // quantas células horizontais
        c.gridx = 0;                            // x da célula
        c.gridy = 0;                            // y da célula
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        painel.add(labelTitulo, c);

        // Ajuste de labels
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.3;
        c.gridwidth = 1;
        c.insets = new Insets(5, 5, 5, 5);  // Padding
        for (T componenteEsquerdo : componentesEsquerdos) {
            c.gridy++;
            painel.add(componenteEsquerdo, c);
        }

        // Ajuste de campos
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(5, 0, 5, 5);
        c.weightx = 0.6;
        c.gridx = 1;
        c.gridy = 0;
        for (T componenteDireito : componentesDireitos) {
            c.gridy++;
            painel.add(componenteDireito, c);
        }
    }
}