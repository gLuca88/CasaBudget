package View;

import Controller.ApplicationContext;

import Controller.ControlloSessioneUtente;
import Entity.Entrata;
import Entity.Uscita;
import InterfaceGS.IMovimentoController;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class EliminaForm extends JFrame {
    private static final long serialVersionUID = 1L;
    private final IMovimentoController controller;
    private final ExpenseDashboard dashboard;
    private JTextField idField;

    public EliminaForm(String tipo, ExpenseDashboard dashboard) {
        this.controller = ApplicationContext.getInstance().getMovimentoController();
        this.dashboard = dashboard;

        setTitle("Elimina " + tipo);
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(255, 240, 245)); // rosa pallido

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("ID " + tipo + ":");
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(idLabel, gbc);

        idField = new JTextField();
        idField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(idField, gbc);

        JButton eliminaButton = new JButton("Elimina " + tipo);
        eliminaButton.setFont(new Font("Arial", Font.BOLD, 14));
        eliminaButton.setBackground(new Color(178, 34, 34)); // rosso fuoco
        eliminaButton.setForeground(Color.WHITE);
        eliminaButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(eliminaButton, gbc);

        JButton mostraButton = new JButton("Mostra " + tipo);
        mostraButton.setFont(new Font("Arial", Font.BOLD, 12));
        mostraButton.setBackground(Color.DARK_GRAY);
        mostraButton.setForeground(Color.WHITE);
        gbc.gridy = 2;
        add(mostraButton, gbc);

        eliminaButton.addActionListener(e -> handleDelete(tipo));
        mostraButton.addActionListener(e -> mostraMovimenti(tipo));

        setVisible(true);
    }

    private void handleDelete(String tipo) {
        try {
            int id = Integer.parseInt(idField.getText());
            int userId = ControlloSessioneUtente.getInstance().getUserId();
            boolean success = false;

            if (tipo.equalsIgnoreCase("Entrata")) {
                Entrata entrata = controller.getEntrataById(id);
                if (entrata != null) {
                    BigDecimal importo = entrata.getImporto();
                    success = controller.eliminaEntrata(id);
                    controller.aggiornaSaldoUtente(userId, importo.negate());
                }
            } else if (tipo.equalsIgnoreCase("Uscita")) {
                Uscita uscita = controller.getUscitaById(id);
                if (uscita != null) {
                    BigDecimal importo = uscita.getImporto();
                    success = controller.eliminaUscita(id);
                    controller.aggiornaSaldoUtente(userId, importo);
                }
            }

            if (success) {
                JOptionPane.showMessageDialog(this, tipo + " eliminata con successo!");
                if (dashboard != null) dashboard.aggiornaDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Errore nell'eliminazione: ID non trovato.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Inserisci un ID valido.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }

    private void mostraMovimenti(String tipo) {
        int userId = ControlloSessioneUtente.getInstance().getUserId();
        List<Entrata> entrate = controller.getEntrate(userId);
        List<Uscita> uscite = controller.getUscite(userId);

        if (tipo.equalsIgnoreCase("Entrata")) {
            new MovimentiTableForm(entrate, List.of());
        } else if (tipo.equalsIgnoreCase("Uscita")) {
            new MovimentiTableForm(List.of(), uscite);
        }
    }
}