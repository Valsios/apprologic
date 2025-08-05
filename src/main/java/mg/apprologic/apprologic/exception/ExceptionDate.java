package mg.apprologic.apprologic.exception;

public class ExceptionDate extends Exception {
    public ExceptionDate(String message) {
        super("CAUSE: "+message);

    }
}

