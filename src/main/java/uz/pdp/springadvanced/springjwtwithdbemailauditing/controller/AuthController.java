package uz.pdp.springadvanced.springjwtwithdbemailauditing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.entity.User;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.payload.ApiResponse;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.payload.UserLoginDto;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.payload.UserRegisterDto;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.security.JwtProvider;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public HttpEntity<?> registerUser(@RequestBody UserRegisterDto userRegisterDto){
        ApiResponse apiResponse = authService.registerUser(userRegisterDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse.getMessage());

    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String receiver){
        ApiResponse apiResponse = authService.verifyEmail(emailCode, receiver);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse.getMessage());
    }

    @PostMapping("/login")
    public HttpEntity<?> loginUser(@RequestBody UserLoginDto userLoginDto){
        ApiResponse apiResponse = authService.login(userLoginDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
