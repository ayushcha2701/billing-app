package billing.billing_app.exceptions;

public class UserAlreadyExists extends Exception {
    public UserAlreadyExists(String msg) {
        super(msg);
    }
}
