package billing.billing_app.controller;

import billing.billing_app.dto.RequestDto;
import billing.billing_app.dto.ResponseDto;
import billing.billing_app.dto.UpdateRequestDto;
import billing.billing_app.exceptions.UserAlreadyExists;
import billing.billing_app.exceptions.UserNotFoundException;
import billing.billing_app.service.serviceInterface.CreateAccountService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/infyBilling")
@RestController
public class CreateAcctController {

    private CreateAccountService createAccountService;

    public CreateAcctController(CreateAccountService createAccountService) {
        this.createAccountService = createAccountService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createAcct(@RequestBody RequestDto requestDto) {

        try {
            ResponseDto responseDto = createAccountService.createAccount(requestDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (UserAlreadyExists u) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAccts")
    public ResponseEntity<List<ResponseDto>> getAllAcct() {
        List<ResponseDto> responseDtos = createAccountService.getAllAcct();
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/getAcct/{id}")
    public ResponseEntity<ResponseDto> getAcctById(@PathVariable Long id) {

        try {
            ResponseDto responseDto = createAccountService.getAcctById(id);
            return ResponseEntity.ok(responseDto);
        } catch (UserNotFoundException d) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateAllInfo/{id}")
    public ResponseEntity<ResponseDto> updateAcctInfo(@RequestBody UpdateRequestDto updateRequestDto,
            @PathVariable Long id) {

        try {
            ResponseDto responseDto = createAccountService.updateAcct(updateRequestDto, id);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (UserNotFoundException u) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAcct/{id}")
    public void deleteAcct(@PathVariable Long id) {
        try {
            createAccountService.deleteAcct(id);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
