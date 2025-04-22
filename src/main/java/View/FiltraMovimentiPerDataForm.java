package View;

import Controller.ApplicationContext;

import Controller.ControlloSessioneUtente;
import Entity.Entrata;
import Entity.Uscita;
import InterfaceGS.IMovimentoController;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class FiltraMovimentiPerDataForm extends JFrame {
	private static final long serialVersionUID = 1L;
	private final IMovimentoController movimentoController;

	public FiltraMovimentiPerDataForm() {
		this.movimentoController = ApplicationContext.getInstance().getMovimentoController();

		setTitle("Filtra Movimenti per Data");
		setSize(450, 250);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new GridBagLayout());
		getContentPane().setBackground(new Color(255, 255, 240));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(12, 12, 12, 12);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel daLabel = new JLabel("Data Inizio:");
		daLabel.setFont(new Font("Arial", Font.BOLD, 14));
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(daLabel, gbc);

		JSpinner daSpinner = new JSpinner(new SpinnerDateModel());
		daSpinner.setEditor(new JSpinner.DateEditor(daSpinner, "yyyy-MM-dd"));
		gbc.gridx = 1;
		add(daSpinner, gbc);

		JLabel aLabel = new JLabel("Data Fine:");
		aLabel.setFont(new Font("Arial", Font.BOLD, 14));
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(aLabel, gbc);

		JSpinner aSpinner = new JSpinner(new SpinnerDateModel());
		aSpinner.setEditor(new JSpinner.DateEditor(aSpinner, "yyyy-MM-dd"));
		gbc.gridx = 1;
		add(aSpinner, gbc);

		JButton filtraButton = new JButton("Filtra");
		filtraButton.setBackground(new Color(0, 102, 204));
		filtraButton.setForeground(Color.WHITE);
		filtraButton.setFont(new Font("Arial", Font.BOLD, 14));
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		add(filtraButton, gbc);

		filtraButton.addActionListener(e -> {
			Date da = (Date) daSpinner.getValue();
			Date a = (Date) aSpinner.getValue();
			int userId = ControlloSessioneUtente.getInstance().getUserId();
			List<Entrata> entrate = movimentoController.getEntrateByDate(userId, da, a);
			List<Uscita> uscite = movimentoController.getUsciteByDate(userId, da, a);
			new MovimentiTableForm(entrate, uscite);
		});

		setVisible(true);
	}
}
