package View;
import javax.swing.*;
import java.awt.*;

public class MainAppFrame extends JFrame {
    private static final long serialVersionUID = -9022811575702152687L;

    // ✅ Schermata iniziale (login/registrazione)
    public MainAppFrame() {
        setTitle("App Gestione Spese Familiari");
        setSize(520, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(245, 255, 250)); // verde menta chiaro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 18, 18, 18);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Benvenuto su App Gestione Spese Familiari", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JButton loginButton = createStyledButton("Login");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(loginButton, gbc);

        JButton registerButton = createStyledButton("Registrazione");
        gbc.gridx = 1;
        add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        registerButton.addActionListener(e -> {
            new RegistrationForm().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    // ✅ Costruttore per la dashboard dopo login
    public MainAppFrame(ExpenseDashboard dashboard) {
        setTitle("Dashboard Spese Familiari");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setContentPane(dashboard);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(160, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2, true));
        return button;
    }
}
