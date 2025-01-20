package billing.billing_app.dto.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInRequestDto {

    private String email;
    private String password;

}
