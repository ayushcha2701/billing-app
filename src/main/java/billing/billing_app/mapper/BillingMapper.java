package billing.billing_app.mapper;

import billing.billing_app.dto.RequestDto;
import billing.billing_app.dto.ResponseDto;
import billing.billing_app.model.BillingEntity;
import org.springframework.stereotype.Component;

@Component
public class BillingMapper {

    public BillingEntity toEntity(RequestDto requestDto) {

        return BillingEntity.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .phone(requestDto.getPhone())
                .address(requestDto.getAddress())
                .password(requestDto.getPassword())
                .build();
    }

    public ResponseDto toResponseDto(BillingEntity billingEntity) {

        return ResponseDto.builder()
                .id(billingEntity.getId())
                .name(billingEntity.getName())
                .email(billingEntity.getEmail())
                .phone(billingEntity.getPhone())
                .address(billingEntity.getAddress())
                .build();
    }
}
