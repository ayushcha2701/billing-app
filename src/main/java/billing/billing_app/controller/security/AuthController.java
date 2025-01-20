package billing.billing_app.controller.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import billing.billing_app.dto.security.LogInRequestDto;
import billing.billing_app.dto.security.LogInResponseDto;
import billing.billing_app.dto.security.RequestStatus;
import billing.billing_app.dto.security.SignUpRequestDto;
import billing.billing_app.dto.security.SignUpResponseDto;
import billing.billing_app.exceptions.SessionNotFoundException;
import billing.billing_app.service.security.securityService.AuthService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign_up")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {

        SignUpResponseDto responseDto = new SignUpResponseDto();

        try {
            if (authService.signUp(signUpRequestDto.getEmail(), signUpRequestDto.getPassword())) {
                responseDto.setRequestStatus(RequestStatus.SUCCESS);
                return ResponseEntity.ok(responseDto);
            } else {

                responseDto.setRequestStatus(RequestStatus.FAILURE);
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            responseDto.setRequestStatus(RequestStatus.FAILURE);
            return new ResponseEntity<>(responseDto, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LogInResponseDto> logIn(@RequestBody LogInRequestDto requestDto) {

        try {

            String token = authService.login(requestDto.getEmail(), requestDto.getPassword());
            LogInResponseDto responseDto = new LogInResponseDto();
            responseDto.setRequestStatus(RequestStatus.SUCCESS);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Auth Token", token);

            return new ResponseEntity<>(responseDto, headers, HttpStatus.OK);

        } catch (Exception e) {

            LogInResponseDto responseDto = new LogInResponseDto();
            responseDto.setRequestStatus(RequestStatus.FAILURE);
            return new ResponseEntity<>(responseDto, null, HttpStatus.BAD_REQUEST);

        }

    }

    @GetMapping("/validate")
    public boolean validate(@RequestParam("token") String token) {
        return authService.validate(token);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authHeader.replace("Bearer ", "").trim();
        try {
            authService.logout(token);
            return ResponseEntity.ok("Logout Successful");
        } catch (SessionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
