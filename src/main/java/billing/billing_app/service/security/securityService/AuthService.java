package billing.billing_app.service.security.securityService;

import billing.billing_app.exceptions.UserAlreadyExists;
import billing.billing_app.exceptions.UserNotFoundException;
import billing.billing_app.exceptions.WrongPasswordException;

public interface AuthService {

    public boolean signUp(String email, String password) throws UserAlreadyExists;

    public String login(String email, String password) throws UserNotFoundException, WrongPasswordException;

    public boolean validate(String token);
}
