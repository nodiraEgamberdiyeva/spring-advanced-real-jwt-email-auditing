package uz.pdp.springadvanced.springjwtwithdbemailauditing.service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.entity.RoleName;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.entity.User;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.payload.ApiResponse;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.payload.UserLoginDto;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.payload.UserRegisterDto;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.repository.RoleRepository;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.repository.UserRepository;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.security.JwtProvider;

import java.sql.Struct;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;


    public ApiResponse registerUser(UserRegisterDto userRegisterDto){

        if (userRepository.existsByEmail(userRegisterDto.getEmail())){
            return new ApiResponse("user is exist", false);
        }

        String emailCode = UUID.randomUUID().toString();

        User user = new User();
        user.setFirstname(userRegisterDto.getFirstName());
        user.setLastName(userRegisterDto.getLastName());
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByName(RoleName.ROLE_USER)));
        user.setEmailCode(emailCode);
        userRepository.save(user);
        //we saved user with enabled false and user should verify email to make it true;
        if (sendEmail(emailCode, userRegisterDto.getEmail())){
            return new ApiResponse("check email and verify email", true);
        }
        Optional<User> byEmailAndEmailCode = userRepository.findByEmailAndEmailCode(userRegisterDto.getEmail(), emailCode);
        userRepository.delete(byEmailAndEmailCode.get());
        return new ApiResponse("error", false);

    }

    public ApiResponse verifyEmail(String emailCode, String receiver){
        Optional<User> byEmailAndEmailCode = userRepository.findByEmailAndEmailCode(receiver, emailCode);
        if (!byEmailAndEmailCode.isPresent()){
            return new ApiResponse("email not found", false);
        }
        User user = byEmailAndEmailCode.get();
        user.setEmailCode(null);
        user.setEnabled(true);
        userRepository.save(user);
        return new ApiResponse("saved and enabled", true);
    }

    //sending email to user for verification
    public boolean sendEmail(String emailCode, String receiver){
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("noreply@gmail.com");  //from
            simpleMailMessage.setTo(receiver);
            simpleMailMessage.setSubject("Email verification");
            simpleMailMessage.setText("http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&receiver=" + receiver);  //verification path
            javaMailSender.send(simpleMailMessage);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username + " not found"));
    }

    public ApiResponse login(UserLoginDto userLoginDto) {
        try{
            //check all fields in user and get userDetail if all are good(username, password, fields with boolean type)
            Authentication authenticate =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword())
                    );
            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateJwt(userLoginDto.getEmail(), user.getRoles());
            return new ApiResponse("token is generated", true, token);
        }
        catch (BadCredentialsException e){
            return new ApiResponse("token is not generated", true);
        }
    }


}
