package billing.billing_app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ResponseDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
}
