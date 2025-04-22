package View;

import Controller.ApplicationContext;
import Controller.ControlloSessioneUtente;
import Entity.Entrata;
import Entity.Uscita;
import InterfaceGS.IMovimentoController;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ModificaForm extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField idField, importoField;
	private JSpinner dataSpinner;
	private JTextArea descrizioneArea;
	private JButton submitButton, mostraButton;

	private final ExpenseDashboard dashboard;
	private final IMovimentoController controller;

	public ModificaForm(String tipo, ExpenseDashboard dashboard) {
		this.dashboard = dashboard;
		this.controller = ApplicationContext.getInstance().getMovimentoController();

		setTitle("Modifica " + tipo);
		setSize(500, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new GridBagLayout());
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(255, 255, 240)); // giallo tenue

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

		JLabel importoLabel = new JLabel("Nuovo Importo:");
		importoLabel.setFont(new Font("Arial", Font.BOLD, 14));
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(importoLabel, gbc);

		importoField = new JTextField();
		importoField.setFont(new Font("Arial", Font.PLAIN, 14));
		gbc.gridx = 1;
		add(importoField, gbc);

		JLabel dataLabel = new JLabel("Nuova Data:");
		dataLabel.setFont(new Font("Arial", Font.BOLD, 14));
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(dataLabel, gbc);

		dataSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dataSpinner, "yyyy-MM-dd");
		dataSpinner.setEditor(dateEditor);
		gbc.gridx = 1;
		add(dataSpinner, gbc);

		JLabel descrizioneLabel = new JLabel("Nuova Descrizione:");
		descrizioneLabel.setFont(new Font("Arial", Font.BOLD, 14));
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(descrizioneLabel, gbc);

		descrizioneArea = new JTextArea(3, 20);
		descrizioneArea.setFont(new Font("Arial", Font.PLAIN, 14));
		descrizioneArea.setLineWrap(true);
		descrizioneArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(descrizioneArea);
		gbc.gridx = 1;
		add(scrollPane, gbc);

		submitButton = new JButton("Modifica");
		submitButton.setFont(new Font("Arial", Font.BOLD, 14));
		submitButton.setBackground(new Color(70, 130, 180)); // blu acciaio
		submitButton.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		add(submitButton, gbc);

		mostraButton = new JButton("Mostra " + tipo);
		mostraButton.setFont(new Font("Arial", Font.BOLD, 12));
		mostraButton.setBackground(Color.DARK_GRAY);
		mostraButton.setForeground(Color.WHITE);
		gbc.gridy = 5;
		add(mostraButton, gbc);

		submitButton.addActionListener(e -> handleSubmit(tipo));
		mostraButton.addActionListener(e -> mostraMovimenti(tipo));
		setVisible(true);
	}

	private void handleSubmit(String tipo) {
		try {
			int id = Integer.parseInt(idField.getText());
			BigDecimal nuovoImporto = new BigDecimal(importoField.getText());
			Date nuovaData = (Date) dataSpinner.getValue();
			String nuovaDescrizione = descrizioneArea.getText();

			boolean success = false;
			int userId = ControlloSessioneUtente.getInstance().getUserId();

			if (tipo.equalsIgnoreCase("Entrata")) {
				Entrata vecchia = controller.getEntrataById(id);
				if (vecchia != null) {
					BigDecimal differenza = nuovoImporto.subtract(vecchia.getImporto());
					success = controller.modificaEntrata(id, nuovoImporto, nuovaData, nuovaDescrizione);
					controller.aggiornaSaldoUtente(userId, differenza);
				}
			} else if (tipo.equalsIgnoreCase("Uscita")) {
				Uscita vecchia = controller.getUscitaById(id);
				if (vecchia != null) {
					BigDecimal differenza = vecchia.getImporto().subtract(nuovoImporto);
					success = controller.modificaUscita(id, nuovoImporto, nuovaData, nuovaDescrizione);
					controller.aggiornaSaldoUtente(userId, differenza);
				}
			}

			if (success) {
				JOptionPane.showMessageDialog(this, "Modifica effettuata per ID " + id);
				if (dashboard != null)
					dashboard.aggiornaDashboard();
				idField.setText("");
			} else {
				JOptionPane.showMessageDialog(this, "Errore nella modifica, controlla l'ID.");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Errore nei dati: " + ex.getMessage());
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