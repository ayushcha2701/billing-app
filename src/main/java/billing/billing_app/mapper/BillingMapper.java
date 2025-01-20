package billing.billing_app.mapper;

import billing.billing_app.dto.RequestDto;
import billing.billing_app.dto.ResponseDto;
import billing.billing_app.dto.UpdateRequestDto;
import billing.billing_app.model.BillingEntity;
import org.springframework.stereotype.Component;

@Component
public class BillingMapper {

    public BillingEntity toEntity(RequestDto requestDto) {

        return BillingEntity.builder()
                .name(requestDto.getName())
                .phone(requestDto.getPhone())
                .address(requestDto.getAddress())
                .build();
    }

    public ResponseDto toResponseDto(BillingEntity billingEntity) {

        return ResponseDto.builder()
                .id(billingEntity.getId())
                .name(billingEntity.getName())
                .email(billingEntity.getUser().getEmail())
                .phone(billingEntity.getPhone())
                .address(billingEntity.getAddress())
                .build();
    }

    public void updateEntityFromDto(UpdateRequestDto updateRequestDto, BillingEntity billingEntity) {
        if (updateRequestDto.getName() != null) {
            billingEntity.setName(updateRequestDto.getName());
        }
        if (updateRequestDto.getPhone() != null) {
            billingEntity.setPhone(updateRequestDto.getPhone());
        }
        if (updateRequestDto.getAddress() != null) {
            billingEntity.setAddress(updateRequestDto.getAddress());
        }
    }
}
