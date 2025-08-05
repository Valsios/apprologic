package mg.apprologic.apprologic.exception;

public class ExceptionValueNumber extends Exception{
    public ExceptionValueNumber(String message) {
        super("CAUSE :"+message);
    }
}
