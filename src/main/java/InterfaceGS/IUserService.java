package InterfaceGS;

import Entity.Utente;

public interface IUserService {
    boolean registerUser(Utente utente);

    boolean authenticateUser(String username, String password);

    double getSaldoByUserId(int userId);

    double getTotaleEntrateByUserId(Integer userId);

    double getTotaleUsciteByUserId(Integer userId);

    Integer getCurrentUserId();

    String getCurrentUsername();

    Utente getUserById(int userId);

    boolean createUser(int userId, String username, String password);

    Utente login(String username, String password); // Metodo confermato

}
