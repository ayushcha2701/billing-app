package billing.billing_app.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateRequestDto {

    private String name;
    private String email;
    private String phone;
    private String address;
}
