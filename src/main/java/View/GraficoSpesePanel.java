package View;

import Controller.ApplicationContext;

import Controller.ControlloSessioneUtente;
import Controller.GraficoSpeseController;
import org.jfree.chart.*;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.Year;
import java.util.*;

public class GraficoSpesePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final GraficoSpeseController controller;

	private static final Color COLOR_ENTRATE = new Color(174, 213, 129); // Verde come il campo entrate
	private static final Color COLOR_USCITE = new Color(239, 154, 154); // Rosso come il campo uscite
	private static final Color COLOR_SALDO = new Color(255, 241, 118); // Giallo come il campo saldo

	public GraficoSpesePanel() {
		this.controller = new GraficoSpeseController(ApplicationContext.getInstance().getMovimentoController());
		setLayout(new BorderLayout());
		setBackground(new Color(250, 250, 250));
	}

	public void showMonthlyChart() {
		removeAll();
		add(createMonthlyChartPanel(), BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	public void showTotaliChart(BigDecimal entrate, BigDecimal uscite, BigDecimal saldo) {
		removeAll();
		add(createTotaliChartPanel(entrate, uscite, saldo), BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	private ChartPanel createTotaliChartPanel(BigDecimal entrate, BigDecimal uscite, BigDecimal saldo) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(entrate, "Entrate", "Entrate");
		dataset.addValue(uscite, "Uscite", "Uscite");
		dataset.addValue(saldo, "Saldo", "Saldo");

		JFreeChart chart = ChartFactory.createBarChart("Totali Finanziari", "Categoria", "Importo (€)", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		return formatChart(chart);
	}

	private ChartPanel createMonthlyChartPanel() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String[] mesi = { "Gen", "Feb", "Mar", "Apr", "Mag", "Giu", "Lug", "Ago", "Sett", "Ott", "Nov", "Dic" };

		int userId = ControlloSessioneUtente.getInstance().getUserId();
		int year = Year.now().getValue();

		Map<Integer, BigDecimal> entratePerMese = controller.getEntrateMensili(userId, year);
		Map<Integer, BigDecimal> uscitePerMese = controller.getUsciteMensili(userId, year);

		BigDecimal saldoProgressivo = BigDecimal.ZERO;
		for (int i = 0; i < 12; i++) {
			BigDecimal entrate = entratePerMese.get(i);
			BigDecimal uscite = uscitePerMese.get(i);
			saldoProgressivo = saldoProgressivo.add(entrate).subtract(uscite);

			dataset.addValue(entrate, "Entrate", mesi[i]);
			dataset.addValue(uscite, "Uscite", mesi[i]);
			dataset.addValue(saldoProgressivo, "Saldo", mesi[i]);
		}

		JFreeChart chart = ChartFactory.createBarChart("Andamento Finanziario Mensile - " + year, "Mese", "Importo (€)",
				dataset, PlotOrientation.VERTICAL, true, true, false);

		return formatChart(chart);
	}

	private ChartPanel formatChart(JFreeChart chart) {
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(new Color(250, 250, 250));
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
		plot.setRangeGridlinePaint(Color.GRAY);

		BarRenderer renderer = new BarRenderer();
		renderer.setSeriesPaint(0, COLOR_ENTRATE);
		renderer.setSeriesPaint(1, COLOR_USCITE);
		renderer.setSeriesPaint(2, COLOR_SALDO);

		renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setDefaultItemLabelsVisible(true);
		renderer.setDefaultItemLabelFont(new Font("Segoe UI", Font.BOLD, 11));
		renderer.setItemMargin(0.15);
		renderer.setMaximumBarWidth(0.1);
		renderer.setBarPainter(new StandardBarPainter());

		plot.setRenderer(renderer);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(700, 300));
		chartPanel.setMouseWheelEnabled(true);
		chartPanel.setDomainZoomable(true);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		chartPanel.setBackground(new Color(250, 250, 250));

		return chartPanel;
	}
}
