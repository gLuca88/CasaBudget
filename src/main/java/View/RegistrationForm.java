package View;

import Controller.ApplicationContext;

import InterfaceGS.IRegistrationController;

import javax.swing.*;
import java.awt.*;

public class RegistrationForm extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField userIdField;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;
	private JTextField initialBalanceField;
	private final IRegistrationController controller;

	public RegistrationForm() {
		this.controller = ApplicationContext.getInstance().getRegistrationController();

		setTitle("Registrazione - Gestione Spese Familiari");
		setSize(520, 480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new GridBagLayout());
		getContentPane().setBackground(new Color(245, 255, 250)); // verde menta chiaro

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(14, 14, 14, 14);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel titleLabel = new JLabel("Crea un nuovo account", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setForeground(new Color(34, 139, 34));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		add(titleLabel, gbc);

		gbc.gridwidth = 1;

		JLabel userIdLabel = new JLabel("ID Utente:");
		userIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(userIdLabel, gbc);

		userIdField = new JTextField();
		userIdField.setFont(new Font("Arial", Font.PLAIN, 15));
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		add(userIdField, gbc);

		JLabel userLabel = new JLabel("Nome:");
		userLabel.setFont(new Font("Arial", Font.BOLD, 14));
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(userLabel, gbc);

		usernameField = new JTextField();
		usernameField.setFont(new Font("Arial", Font.PLAIN, 15));
		gbc.gridx = 1;
		add(usernameField, gbc);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(passwordLabel, gbc);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
		gbc.gridx = 1;
		add(passwordField, gbc);

		JLabel confirmPasswordLabel = new JLabel("Conferma Password:");
		confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(confirmPasswordLabel, gbc);

		confirmPasswordField = new JPasswordField();
		confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 15));
		gbc.gridx = 1;
		add(confirmPasswordField, gbc);

		JLabel saldoLabel = new JLabel("Saldo Iniziale:");
		saldoLabel.setFont(new Font("Arial", Font.BOLD, 14));
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(saldoLabel, gbc);

		initialBalanceField = new JTextField();
		initialBalanceField.setFont(new Font("Arial", Font.PLAIN, 15));
		gbc.gridx = 1;
		add(initialBalanceField, gbc);

		JButton registerButton = new JButton("Registrati");
		registerButton.setFont(new Font("Arial", Font.BOLD, 14));
		registerButton.setBackground(new Color(34, 139, 34));
		registerButton.setForeground(Color.WHITE);
		registerButton.setFocusPainted(false);
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		add(registerButton, gbc);

		// 🔁 Pulsante per tornare al login
		JButton backButton = new JButton("Torna al Login");
		backButton.setFont(new Font("Arial", Font.BOLD, 13));
		backButton.setBackground(Color.LIGHT_GRAY);
		backButton.setForeground(Color.DARK_GRAY);
		backButton.setFocusPainted(false);
		gbc.gridy = 7;
		add(backButton, gbc);

		registerButton.addActionListener(e -> handleRegistration());
		backButton.addActionListener(e -> {
			new LoginForm().setVisible(true);
			dispose();
		});

		setVisible(true);
	}

	private void handleRegistration() {
		String userIdStr = userIdField.getText();
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		String confirmPassword = new String(confirmPasswordField.getPassword());
		String saldoInizialeStr = initialBalanceField.getText();

		boolean isRegistered = controller.registerUser(userIdStr, username, password, confirmPassword,
				saldoInizialeStr);

		if (isRegistered) {
			JOptionPane.showMessageDialog(this, "Registrazione avvenuta con successo!", "Successo",
					JOptionPane.INFORMATION_MESSAGE);
			new LoginForm().setVisible(true);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Errore nella registrazione. Controlla i dati inseriti.", "Errore",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}