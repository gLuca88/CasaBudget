package Controller;

import Entity.Utente;
import InterfaceGS.IUserService;

public class ControlloSessioneUtente {
	private static final ControlloSessioneUtente instance = new ControlloSessioneUtente();
	private Utente utenteLoggato;

	private ControlloSessioneUtente() {
	}

	public static ControlloSessioneUtente getInstance() {
		return instance;
	}

	public void setUtenteLoggato(Utente utente) {
		this.utenteLoggato = utente;
	}

	public Utente getUtenteLoggato() {
		return utenteLoggato;
	}

	public int getUserId() {
		return utenteLoggato != null ? utenteLoggato.getUserId() : -1;
	}

	public IUserService getUserService() {
		return ApplicationContext.getInstance().getUserService();
	}

	// 🔄 Nuovo login basato su username e password
    public boolean login(String username, String password) {
        IUserService userService = getUserService();
        boolean authenticated = userService.authenticateUser(username, password);
        if (authenticated) {
            Utente utente = userService.login(username, password);
            setUtenteLoggato(utente);
        }
        return authenticated;
    }
}
