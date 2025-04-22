package View;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopupMenuHandler {
    private final String tipo;
    private final ExpenseDashboard dashboard;
    private final JPopupMenu popupMenu;

    public PopupMenuHandler(String tipo, ExpenseDashboard dashboard) {
        this.tipo = tipo;
        this.dashboard = dashboard;
        this.popupMenu = new JPopupMenu();

        JMenuItem inserisci = new JMenuItem("Inserisci " + tipo);
        JMenuItem modifica = new JMenuItem("Modifica " + tipo);
        JMenuItem elimina = new JMenuItem("Elimina " + tipo);

        inserisci.addActionListener(e -> {
            if ("Entrata".equalsIgnoreCase(tipo)) {
                new EntrataForm("Entrata",dashboard);
            } else if ("Uscita".equalsIgnoreCase(tipo)) {
                new UscitaForm(dashboard);
            }
        });

        modifica.addActionListener(e -> new ModificaForm(tipo, dashboard));
        elimina.addActionListener(e -> new EliminaForm(tipo, dashboard));

        popupMenu.add(inserisci);
        popupMenu.add(modifica);
        popupMenu.add(elimina);
    }

    public void attachToButton(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showMenu(e);
            }
        });
    }

    private void showMenu(MouseEvent e) {
        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

	public String getTipo() {
		return tipo;
	}

	public ExpenseDashboard getDashboard() {
		return dashboard;
	}
}