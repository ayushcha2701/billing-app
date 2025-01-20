package billing.billing_app.service;

import billing.billing_app.dto.RequestDto;
import billing.billing_app.dto.ResponseDto;
import billing.billing_app.dto.UpdateRequestDto;
import billing.billing_app.exceptions.UserAlreadyExists;
import billing.billing_app.exceptions.UserNotFoundException;
import billing.billing_app.mapper.BillingMapper;
import billing.billing_app.model.BillingEntity;
import billing.billing_app.model.User;
import billing.billing_app.repository.CreateAcctRepository;
import billing.billing_app.repository.UserRepository;
import billing.billing_app.service.serviceInterface.CreateAccountService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CreateAcctServiceImpl implements CreateAccountService {

    private CreateAcctRepository createAcctRepo;
    private BillingMapper billingMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

    public CreateAcctServiceImpl(CreateAcctRepository createAcctRepo,
            BillingMapper billingMapper,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserRepository userRepository) {
        this.createAcctRepo = createAcctRepo;
        this.billingMapper = billingMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseDto createAccount(RequestDto requestDto) throws UserAlreadyExists {

        Optional<BillingEntity> existingUser = createAcctRepo.findByEmail(requestDto.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExists("User with " + existingUser + " already exists");
        }

        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(requestDto.getPassword()));
        userRepository.save(user);

        BillingEntity billing = billingMapper.toEntity(requestDto);
        billing.setUser(user);
        createAcctRepo.save(billing);
        return billingMapper.toResponseDto(billing);
    }

    @Override
    public List<ResponseDto> getAllAcct() {

        List<BillingEntity> billingEntity = createAcctRepo.findAll();

        return billingEntity.stream()
                .map(billingMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseDto getAcctById(Long id) throws UserNotFoundException {

        BillingEntity BillingEntity = createAcctRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with this id " + id + " does not exist"));

        ResponseDto responseDto = billingMapper.toResponseDto(BillingEntity);

        return responseDto;
    }

    @Override
    public ResponseDto updateAcct(UpdateRequestDto requestDto, Long id) throws UserNotFoundException {

        BillingEntity billingEntity = createAcctRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with this id " + id + " does not exist"));

        if (requestDto.getAddress() != null) {
            billingEntity.setAddress(requestDto.getAddress());
        }

        User user = billingEntity.getUser();

        if (requestDto.getEmail() != null) {
            user.setEmail(requestDto.getEmail());
            userRepository.save(user); // Update email in User
        }

        billingMapper.updateEntityFromDto(requestDto, billingEntity);
        createAcctRepo.save(billingEntity);

        return billingMapper.toResponseDto(billingEntity);
    }

    @Override
    public String deleteAcct(Long id) throws UserNotFoundException {
        BillingEntity billingEntity = createAcctRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with this id " + id + " does not exist"));
        createAcctRepo.delete(billingEntity);

        return "Account " + id + " deleted successfully";
    }
}
