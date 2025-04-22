package Controller;


import InterfaceGS.ILoginController;

public class LoginController implements ILoginController {

    // 🔄 Metodo aggiornato: accetta solo username e password
    @Override
    public boolean authenticate(String username, String password) {
        return ControlloSessioneUtente.getInstance().login(username, password);
    }
}
