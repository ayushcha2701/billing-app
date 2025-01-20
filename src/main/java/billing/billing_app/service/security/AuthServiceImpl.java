package billing.billing_app.service.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import billing.billing_app.exceptions.SessionNotFoundException;

import billing.billing_app.exceptions.UserAlreadyExists;
import billing.billing_app.exceptions.UserNotFoundException;
import billing.billing_app.exceptions.WrongPasswordException;
import billing.billing_app.model.Session;

import billing.billing_app.model.SessionStatus;

import billing.billing_app.model.User;
import billing.billing_app.repository.SessionRepository;
import billing.billing_app.repository.UserRepository;
import billing.billing_app.service.security.securityService.AuthService;
import java.util.*;

import javax.crypto.SecretKey;

public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private SessionRepository sessionRepository;

    public AuthServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
            SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public boolean signUp(String email, String password) throws UserAlreadyExists {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExists("User with the " + email + " already exist");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(user);

        return true;

    }

    @Override
    public String login(String email, String password) throws UserNotFoundException, WrongPasswordException {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User with the " + email + " does not exist");
        }

        boolean match = bCryptPasswordEncoder.matches(password, user.get().getPassword());

        if (match) {
            String token = createJwtToken(user.get().getId(), null, email);

            Session session = new Session();
            session.setToken(token);
            session.setUser(user.get());

            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date datePlus30Days = calendar.getTime();
            session.setExpiringAt(datePlus30Days);

            session.setSessionStatus(SessionStatus.ACTIVE);

            sessionRepository.save(session);

            return token;
        } else {
            throw new WrongPasswordException("Wrong password");
        }

    }

    private String createJwtToken(Long id, List<String> roles, String email) {

        Map<String, Object> dataInJwt = new HashMap();
        dataInJwt.put("id", id);
        dataInJwt.put("roles", roles);
        dataInJwt.put("email", email);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date datePlus30Days = calendar.getTime();

        String token = Jwts.builder().claims(dataInJwt)
                .expiration(datePlus30Days)
                .issuedAt(new Date())
                .signWith(key)
                .compact();

        return token;
    }

    @Override
    public boolean validate(String token) {
        try {

            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            Date expDate = claims.getPayload().getExpiration();

            if (expDate.before(new Date())) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }


    @Override
    public void logout(String token) {

        Optional<Session> sessionOptional = sessionRepository.findByToken(token);

        if (sessionOptional.isPresent()) {

            Session session = sessionOptional.get();
            session.setSessionStatus(SessionStatus.ENDED);
            sessionRepository.save(session);

        } else {
            throw new SessionNotFoundException("Session not found for the provided token.");
        }
    }

}
