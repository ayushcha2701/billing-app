package billing.billing_app.service.serviceInterface;

import java.util.List;

import billing.billing_app.dto.RequestDto;
import billing.billing_app.dto.ResponseDto;
import billing.billing_app.dto.UpdateRequestDto;
import billing.billing_app.exceptions.UserAlreadyExists;
import billing.billing_app.exceptions.UserNotFoundException;

public interface CreateAccountService {

    public ResponseDto createAccount(RequestDto requestDto) throws UserAlreadyExists;

    public List<ResponseDto> getAllAcct();

    public ResponseDto getAcctById(Long id) throws UserNotFoundException;

    public ResponseDto updateAcct(UpdateRequestDto updateRequestDto, Long id) throws UserNotFoundException;

    public String deleteAcct(Long id) throws UserNotFoundException;

}
