package View;

import Controller.ApplicationContext;
import InterfaceGS.ILoginController;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private final ILoginController loginController;

    public LoginForm() {
        this.loginController = ApplicationContext.getInstance().getLoginController();

        setTitle("Login - Gestione Spese Familiari");
        setSize(500, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(240, 248, 255)); // azzurro chiaro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 14, 14, 14);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Accesso Utente", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        JLabel userLabel = new JLabel("Nome Utente:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(userLabel, gbc);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 15));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setEchoChar('●');
        gbc.gridx = 1;
        add(passwordField, gbc);

        JButton loginButton = new JButton("Accedi");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        // 🔁 Bottone per andare alla registrazione
        JButton goToRegisterButton = new JButton("Vai a Registrazione");
        goToRegisterButton.setFont(new Font("Arial", Font.BOLD, 13));
        goToRegisterButton.setBackground(Color.LIGHT_GRAY);
        goToRegisterButton.setForeground(Color.DARK_GRAY);
        goToRegisterButton.setFocusPainted(false);
        gbc.gridy = 4;
        add(goToRegisterButton, gbc);

        loginButton.addActionListener(e -> handleLogin());
        goToRegisterButton.addActionListener(e -> {
            new RegistrationForm().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        boolean success = loginController.authenticate(username, password);

        if (success) {
            JOptionPane.showMessageDialog(this, "Login effettuato con successo!", "Successo",
                    JOptionPane.INFORMATION_MESSAGE);

            // ✅ Inizializza dashboard e controller PRIMA di accedere alla dashboard
            ApplicationContext.getInstance().inizializzaDashboardEDependenze();

            ExpenseDashboard dashboard = ApplicationContext.getInstance().getDashboard();
            dashboard.inizializzaDashboard();

            new MainAppFrame(dashboard).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Nome utente o password errati.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}