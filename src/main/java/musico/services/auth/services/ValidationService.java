package musico.services.auth.services;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; // RFC 5322
    private static final String PWD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

    public Boolean validateSignup(String username, String email, String password) {
        return validateUsername(username) && validateEmail(email) && validatePassword(password);
    }

    private Boolean validateUsername(String username) {
        return username.length() >= 3;
    }

    private Boolean validateEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    private Boolean validatePassword(String password) {
        return password.matches(PWD_REGEX);
    }

}
