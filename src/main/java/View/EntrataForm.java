package View;

import Controller.ApplicationContext;
import InterfaceGS.IEntrataController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Date;

public class EntrataForm extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField importoField;
    private JSpinner dataSpinner;
    private JTextArea descrizioneArea;
    private JButton submitButton;
    private final IEntrataController entrataController;
    private final ExpenseDashboard dashboard;

    public EntrataForm(String tipo, ExpenseDashboard dashboard) {
        this.dashboard = dashboard;
        this.entrataController = ApplicationContext.getInstance().getEntrataController();

        setTitle("Inserisci " + tipo);
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(240, 248, 255)); // azzurro chiaro
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel importoLabel = new JLabel("Importo:");
        importoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(importoLabel, gbc);

        importoField = new JTextField();
        importoField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(importoField, gbc);

        JLabel dataLabel = new JLabel("Data:");
        dataLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(dataLabel, gbc);

        dataSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dataSpinner, "yyyy-MM-dd");
        dataSpinner.setEditor(dateEditor);
        gbc.gridx = 1;
        add(dataSpinner, gbc);

        JLabel descrizioneLabel = new JLabel("Descrizione:");
        descrizioneLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(descrizioneLabel, gbc);

        descrizioneArea = new JTextArea(3, 20);
        descrizioneArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descrizioneArea.setLineWrap(true);
        descrizioneArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descrizioneArea);
        gbc.gridx = 1;
        add(scrollPane, gbc);

        submitButton = new JButton("Aggiungi Entrata");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(60, 179, 113)); // verde moderno
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(submitButton, gbc);

        submitButton.addActionListener(this::handleSubmit);
        setVisible(true);
    }

    private void handleSubmit(ActionEvent e) {
        try {
            BigDecimal importo = new BigDecimal(importoField.getText());
            Date data = (Date) dataSpinner.getValue();
            String descrizione = descrizioneArea.getText();

            boolean success = entrataController.inserisciEntrata(importo, descrizione, data);

            if (success) {
                JOptionPane.showMessageDialog(this, "Entrata inserita con successo!");
                if (dashboard != null)
                    dashboard.aggiornaDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Errore nell'inserimento dell'entrata.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore nei dati: " + ex.getMessage());
        }
    }
}