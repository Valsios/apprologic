package mg.apprologic.apprologic.exception;

public class ExceptionLogin extends Exception {
    public ExceptionLogin(String message) {
        super("Login refusé ou utilisateur non trouvé.");

    }
}
