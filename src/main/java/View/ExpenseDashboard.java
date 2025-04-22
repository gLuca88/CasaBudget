package View;

import javax.swing.*;

import java.awt.*;
import java.math.BigDecimal;

import Controller.ApplicationContext;
import Controller.ControlloSessioneUtente;
import InterfaceGS.IMovimentoController;

public class ExpenseDashboard extends JPanel {
    private static final long serialVersionUID = 5390520200031828144L;

    private JTextField userNameField;
    private JTextField totalIncomeField;
    private JTextField totalExpensesField;
    private JTextField balanceField;
    private JPanel mainPanel;
    private JPanel chartWrapperPanel;
    private boolean showingMonthly = true;
    private GraficoSpesePanel graficoPanel;

    public ExpenseDashboard() {
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));

        add(createTopStatsPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createTopStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panel.setBackground(new Color(250, 250, 250));

        userNameField = createDashboardCard("Utente", new Color(197, 224, 249));
        totalIncomeField = createDashboardCard("Entrate Totali", new Color(174, 213, 129));
        totalExpensesField = createDashboardCard("Uscite Totali", new Color(239, 154, 154));
        balanceField = createDashboardCard("Saldo", new Color(255, 241, 118));

        panel.add(wrapCard("Nome Utente", userNameField));
        panel.add(wrapCard("Entrate Totali", totalIncomeField));
        panel.add(wrapCard("Uscite Totali", totalExpensesField));
        panel.add(wrapCard("Saldo", balanceField));

        return panel;
    }

    private JPanel createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        buttonPanel.setBackground(new Color(250, 250, 250));

        JButton btnEntrate = createDashboardButton("Entrate");
        JButton btnUscite = createDashboardButton("Uscite");
        JButton btnMostraUscite = createDashboardButton("Mostra Movimenti");
        JButton btnMovimentiPerData = createDashboardButton("Movimenti per Data");
        JButton btnToggleGrafico = createDashboardButton("Visualizza Totali");

        buttonPanel.add(btnEntrate);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(btnUscite);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(btnMostraUscite);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(btnMovimentiPerData);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(btnToggleGrafico);

        PopupMenuHandler entrateMenu = new PopupMenuHandler("Entrata", this);
        PopupMenuHandler usciteMenu = new PopupMenuHandler("Uscita", this);
        entrateMenu.attachToButton(btnEntrate);
        usciteMenu.attachToButton(btnUscite);

        btnMostraUscite.addActionListener(e -> {
            int userId = ControlloSessioneUtente.getInstance().getUserId();
            IMovimentoController movimentoController = ApplicationContext.getInstance().getMovimentoController();
            new MovimentiTableForm(movimentoController.getEntrate(userId), movimentoController.getUscite(userId));
        });

        btnMovimentiPerData.addActionListener(e -> new FiltraMovimentiPerDataForm());

        btnToggleGrafico.addActionListener(e -> toggleChart());

        chartWrapperPanel = new JPanel(new BorderLayout());
        chartWrapperPanel.setBackground(new Color(250, 250, 250));
        graficoPanel = new GraficoSpesePanel();
        chartWrapperPanel.add(graficoPanel, BorderLayout.CENTER);

        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(chartWrapperPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    public void inizializzaDashboard() {
        aggiornaDashboard();
    }

    public void aggiornaDashboard() {
        ApplicationContext.getInstance().getDashboardController().aggiornaDashboardUI(this);
        updateChart();
    }

    private void updateChart() {
        chartWrapperPanel.removeAll();

        if (showingMonthly) {
            graficoPanel.showMonthlyChart();
        } else {
            BigDecimal entrate = new BigDecimal(totalIncomeField.getText().replace(" €", ""));
            BigDecimal uscite = new BigDecimal(totalExpensesField.getText().replace(" €", ""));
            BigDecimal saldo = new BigDecimal(balanceField.getText().replace(" €", ""));
            graficoPanel.showTotaliChart(entrate, uscite, saldo);
        }

        chartWrapperPanel.add(graficoPanel, BorderLayout.CENTER);
        chartWrapperPanel.revalidate();
        chartWrapperPanel.repaint();
    }

    private void toggleChart() {
        showingMonthly = !showingMonthly;
        updateChart();
    }

    public void updateFields(String username, BigDecimal entrate, BigDecimal uscite, BigDecimal saldo) {
        userNameField.setText(username);
        totalIncomeField.setText(entrate + " €");
        totalExpensesField.setText(uscite + " €");
        balanceField.setText(saldo + " €");
    }

    public void showErrore(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    private JTextField createDashboardCard(String label, Color background) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.BOLD, 16));
        field.setEditable(false);
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.setBackground(background);
        field.setForeground(new Color(45, 45, 45));
        return field;
    }

    private JPanel wrapCard(String title, JTextField field) {
        JPanel wrapper = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(new Color(45, 45, 45));
        wrapper.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        wrapper.setBackground(Color.WHITE);
        wrapper.add(label, BorderLayout.NORTH);
        wrapper.add(field, BorderLayout.CENTER);
        return wrapper;
    }

    private JButton createDashboardButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(180, 35));
        return button;
    }
}

