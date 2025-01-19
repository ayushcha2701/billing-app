package billing.billing_app.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDto {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;
}
